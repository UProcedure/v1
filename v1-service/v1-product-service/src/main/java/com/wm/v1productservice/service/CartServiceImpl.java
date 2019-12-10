package com.wm.v1productservice.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import com.alibaba.dubbo.config.annotation.Service;
import com.wm.base.BaseServiceImpl;
import com.wm.base.IBaseDao;
import com.wm.base.entity.ResultBean;
import com.wm.entity.Cart;
import com.wm.entity.ErrorEmail;
import com.wm.entity.Product;
import com.wm.mapper.CartMapper;
import com.wm.mapper.ProductDescMapper;
import com.wm.mapper.ProductMapper;
import com.wm.service.ICartService;
import com.wm.util.JwtTokenUtils;
import com.wm.vo.CartItem;
import com.wm.vo.CartItemVO;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author weimin
 * @ClassName CartServiceImpl
 * @Description TODO
 * @date 2019/11/14 16:49
 */
@Service
public class CartServiceImpl extends BaseServiceImpl<Cart> implements ICartService {
    private final String CART_KEY_HEADER = "user:cart:";
    private final int MAX_TOKEN_LENGTH = 40;

    @Resource(name = "redisTemplate1")
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductDescMapper productDescMapper;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ExecutorService executorService;

    @Override
    public ResultBean add(String token, Long productId, int count) {
        String key = new StringBuilder(CART_KEY_HEADER).append(token).toString();
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        if(entries!=null && entries.size()!=0){
            if(redisTemplate.opsForHash().hasKey(key,productId)){
                CartItem cartItem = (CartItem) redisTemplate.opsForHash().get(key, productId);
                count += cartItem.getCount();
            }
        }
        redisTemplate.opsForHash().put(key,productId,
                new CartItem(productId,count,new Date()));
        redisTemplate.expire(key,15,TimeUnit.DAYS);
        if(token.length()>MAX_TOKEN_LENGTH){
            executorService.submit(
                    new CartTakeOneUpdate(
                            new Cart(productId, new Date(), count),token));
        }
        return new ResultBean(200,true);
    }

    @Override
    public ResultBean del(String token, Long productId) {
        String key = new StringBuilder(CART_KEY_HEADER).append(token).toString();
        redisTemplate.opsForHash().delete(key,productId);
        redisTemplate.expire(key,15,TimeUnit.DAYS);
        if(token.length()>MAX_TOKEN_LENGTH){
            executorService.submit(() -> {
                try {
                    JwtTokenUtils jwtTokenUtils = new JwtTokenUtils("user_token");
                    Claims body = jwtTokenUtils.getBody(token);
                    cartMapper.delByProductIdUserId(Long.valueOf(body.getId()),productId);
                }catch (RuntimeException e){
                }
            });
        }
        return new ResultBean(200,true);
    }

    @Override
    public ResultBean query(String token) {
        String key = new StringBuilder(CART_KEY_HEADER).append(token).toString();
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        if(entries==null || entries.size()==0){
            return new ResultBean(300,null);
        }
        Set<CartItemVO> set = new TreeSet<>((o1, o2) -> {
            int i = o2.getUpdateTime().compareTo(o1.getUpdateTime());
            if(i==0){
                return o1.getProduct().getId().compareTo(o2.getProduct().getId());
            }
            return i;
        });
        List<Long> listLong = Convert.convert(new TypeReference<List<Long>>() {}, entries.keySet());
        List<Product> list = productDescMapper.getListByIds(listLong);
        for (Product product : list) {
            CartItemVO cartItemVO = new CartItemVO();
            cartItemVO.setProduct(product);
            CartItem cartItem = (CartItem) entries.get(product.getId());
            cartItemVO.setCount(cartItem.getCount());
            cartItemVO.setUpdateTime(cartItem.getUpdateTime());
            set.add(cartItemVO);
        }
        redisTemplate.expire(key,15,TimeUnit.DAYS);
        return new ResultBean(200,new ArrayList<>(set));
    }

    @Override
    public ResultBean update(String token, Long productId, int count) {
        String key = new StringBuilder(CART_KEY_HEADER).append(token).toString();
        if(redisTemplate.opsForHash().hasKey(key,productId)){
            redisTemplate.opsForHash().put(key,productId,new CartItem(productId,count,new Date()));
            redisTemplate.expire(key,15,TimeUnit.DAYS);
            if(token.length()>MAX_TOKEN_LENGTH){
                executorService.submit(
                        new CartTakeOneUpdate(
                                new Cart(productId, new Date(), count),token));
            }
            return new ResultBean(200,true);
        }
        return new ResultBean(404,false);
    }

    @Override
    public ResultBean combine(String token,String userToken) {
        Long userId = null;
        try {
            JwtTokenUtils jwtTokenUtils = new JwtTokenUtils("user_token");
            Claims body = jwtTokenUtils.getBody(userToken);
            userId = Long.valueOf(body.getId());
        }catch (RuntimeException e){
            return new ResultBean(404,false);
        }
        List<Cart> list = cartMapper.getListByUserId(userId);
        String key = new StringBuilder(CART_KEY_HEADER).append(token).toString();
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        if(list!=null && list.size()!=0){
            if(entries==null){
                entries = new HashMap<>(list.size());
            }
            for (Cart cart : list) {
                if(entries.containsKey(cart.getProductId())){
                    CartItem o = (CartItem) entries.get(cart.getProductId());
                    o.setCount(cart.getCount()+o.getCount());
                }else {
                    entries.put(cart.getProductId(),
                            new CartItem(cart.getProductId(),
                                    cart.getCount(),cart.getUpdateTime()));
                }
            }
        }
        redisTemplate.opsForHash().putAll(
                new StringBuilder(CART_KEY_HEADER).append(userToken).toString(),entries);
        redisTemplate.expire(key,15,TimeUnit.DAYS);
        if(entries!=null && entries.size()!=0){
            executorService.submit(new CartTake(userId,new ArrayList(entries.values())));
        }
        return new ResultBean(200,true);
    }

    private class CartTake implements Runnable{
        private Long userId;
        private List list;

        public CartTake(Long userId, List list) {
            this.userId = userId;
            this.list = list;
        }

        @Override
        public void run() {
            List<Cart> carts = new ArrayList<>();
            for (Object o: list) {
                CartItem cartItem = (CartItem) o;
                carts.add(new Cart(userId,cartItem.getProductId(),
                        cartItem.getUpdateTime(),cartItem.getCount()));
            }
            productMapper.batchAddCart(carts);
        }
    }
    private class CartTakeOneUpdate implements Runnable{
        private Cart cart;
        private String token;

        public CartTakeOneUpdate(Cart cart, String token) {
            this.cart = cart;
            this.token = token;
        }
        @Override
        public void run() {
            try {
                JwtTokenUtils jwtTokenUtils = new JwtTokenUtils("user_token");
                Claims body = jwtTokenUtils.getBody(token);
                cart.setUserId(Long.valueOf(body.getId()));
                List<Cart> list = new ArrayList<>(1);
                list.add(cart);
                productMapper.batchAddCart(list);
            }catch (RuntimeException e){

            }
        }
    }

    @Override
    public IBaseDao<Cart> getBaseDao() {
        return cartMapper;
    }


}
