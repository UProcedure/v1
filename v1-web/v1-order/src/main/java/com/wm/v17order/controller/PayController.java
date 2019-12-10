package com.wm.v17order.controller;

import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.wm.v17order.entity.BizContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author weimin
 * @ClassName PayController
 * @Description TODO
 * @date 2019/11/18 16:53
 */
@Controller
@RequestMapping("pay")
public class PayController {
    @Autowired
    private AlipayClient alipayClient;
    @Value("${pay.Alipay_public_key}")
    private String ALIPAY_PUBLIC_KEY;

    @RequestMapping("payPage/{orderId}")
    public void payPage(HttpServletResponse response, @PathVariable String orderId) throws IOException {
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl("https://www.baidu.com/");
        alipayRequest.setNotifyUrl("http://s7ukbe.natappfree.cc/pay/notifyResult");
        BizContent content = new BizContent();
        content.setOut_trade_no(orderId);
        content.setProduct_code("FAST_INSTANT_TRADE_PAY");
        content.setSubject("Iphone6 16G");
        content.setTotal_amount("9999");
        content.setBody("一个手机");
        alipayRequest.setBizContent(JSONUtil.toJsonStr(content));
        String form="";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(form);
        response.getWriter().flush();
        response.getWriter().close();
    }


    @PostMapping("notifyResult")
    @ResponseBody
    public void notifyResult(HttpServletRequest request,HttpServletResponse response) throws AlipayApiException, IOException {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, String> paramsMap = new HashMap<>(parameterMap.size());
        parameterMap.forEach((s, val) -> {
            String par = "";
            for (int i = 0; i < val.length-1; i++) {
                par = par+val[i]+",";
            }
            par += val[val.length-1];
            paramsMap.put(s,par);
        });
        boolean signVerified = AlipaySignature.rsaCheckV1(
                paramsMap,
                ALIPAY_PUBLIC_KEY,
                "utf-8", "RSA2");
        if(signVerified){
           // TODO 验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure
            System.out.println("验签成功");
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write("success");
            response.getWriter().flush();
            response.getWriter().close();
        }else{
            // TODO 验签失败则记录异常日志，并在response中返回failure.
            System.out.println("验签失败");
        }
    }
}
