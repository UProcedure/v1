package com.wm.v1productservice;

import com.github.pagehelper.PageInfo;
import com.wm.base.entity.ResultBean;
import com.wm.entity.Cart;
import com.wm.entity.Product;
import com.wm.mapper.CartMapper;
import com.wm.mapper.ProductMapper;
import com.wm.service.ICartService;
import com.wm.service.IProductService;
import com.wm.vo.CartItemVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class V1ProductServiceApplicationTests {


    @Autowired
    private IProductService productService;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private ICartService cartService;
    @Resource(name = "redisTemplate1")
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private ProductMapper productMapper;

    @Test
    public void poolTest(){
        List<Cart> list = new ArrayList<>();
        list.add(new Cart(6L,12L,new Date(),6));
        list.add(new Cart(6L,15L,new Date(),8));
        System.out.println(productMapper.batchAddCart(list));
    }

    @Test
    public void pageListTest(){
        PageInfo<Product> pageList = productService.getPageList(1, 1, 3);
        System.out.println(pageList.getList().size());
    }

    @Test
    public void cartTest() throws InterruptedException {
        String token = "b655c770-7f7d-4e8c-9cb3-e362d4fb5162";
        String key = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2Iiwic3ViIjoid20iLCJpYXQiOjE1NzM4MTcwNTMsImV4cCI6MTU3MzgxODg1M30.w7Q33KPYXSbIy5lUY_AYv0kSYz6uCChgt1xpNRmGS50";
        /*cartService.add(token,15L,5);
        cartService.update(token,14L,10);
        cartService.del(token,12L);
        cartService.add(token,17L,6);*/
        cartService.add(key,21L,6);
        cartService.update(key,18L,18);
        cartService.del(key,12L);
        xxx(key);
    }

    private void xxx(String token) {
        ResultBean query = cartService.query(token);
        List<CartItemVO> cartItemVO = (List<CartItemVO>) query.getData();
        for (CartItemVO itemVO : cartItemVO) {
            System.out.println(itemVO.getProduct().getId()+"-->"+itemVO.getCount()+"-->"+itemVO.getUpdateTime());
        }
    }
    /**
     * 18-->4-->Sat Nov 16 00:32:04 CST 2019
     * 17-->6-->Fri Nov 15 19:13:31 CST 2019
     * 12-->6-->Fri Nov 15 16:54:00 CST 2019
     * 15-->8-->Fri Nov 15 16:54:00 CST 2019
     * 14-->4-->Thu Nov 14 04:15:50 CST 2019
     */


}
