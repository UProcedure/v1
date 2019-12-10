package com.wm.v1register.utils;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;

import java.util.Map;

/**
 * @author weimin
 * @ClassName SendSms
 * @Description TODO
 * @date 2019/11/11 8:43
 */
public class SendSms {
    public  String getHttpAddr(String to,String code) throws Exception {
        String accessKeyId = "LTAI4FqH2v37r4b8NfRkeih1";
        String accessSecret = "bnFZBSuaNWBn4dkSWkgayCBPSIzR0a";
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(new java.util.SimpleTimeZone(0, "GMT"));// 这里一定要设置GMT时区
        java.util.Map<String, String> paras = new java.util.HashMap<String, String>();
        // 1. 系统参数
        paras.put("SignatureMethod", "HMAC-SHA1");
        paras.put("SignatureNonce", java.util.UUID.randomUUID().toString());
        paras.put("AccessKeyId", accessKeyId);
        paras.put("SignatureVersion", "1.0");
        paras.put("Timestamp", df.format(new java.util.Date()));
        paras.put("Format", "JSON");
        // 2. 业务API参数
        paras.put("Action", "SendSms");
        paras.put("Version", "2017-05-25");
        paras.put("RegionId", "cn-hangzhou");
        paras.put("PhoneNumbers", to);
        paras.put("SignName", "彼岸花网站");
        paras.put("TemplateParam", "{\"code\":"+code+"}");
        paras.put("TemplateCode", "SMS_177241071");
        paras.put("OutId", "123");
        // 3. 去除签名关键字Key
        if (paras.containsKey("Signature"))  {paras.remove("Signature");}
        // 4. 参数KEY排序
        java.util.TreeMap<String, String> sortParas = new java.util.TreeMap<String, String>();
        sortParas.putAll(paras);
        // 5. 构造待签名的字符串
        java.util.Iterator<String> it = sortParas.keySet().iterator();
        StringBuilder sortQueryStringTmp = new StringBuilder();
        while (it.hasNext()) {
            String key = it.next();
            sortQueryStringTmp.append("&").append(specialUrlEncode(key)).append("=").append(specialUrlEncode(paras.get(key)));
        }
        String sortedQueryString = sortQueryStringTmp.substring(1);// 去除第一个多余的&符号
        StringBuilder stringToSign = new StringBuilder();
        stringToSign.append("GET").append("&");
        stringToSign.append(specialUrlEncode("/")).append("&");
        stringToSign.append(specialUrlEncode(sortedQueryString));
        String sign = sign(accessSecret + "&", stringToSign.toString());
        // 6. 签名最后也要做特殊URL编码
        String signature = specialUrlEncode(sign);
        // 最终打印出合法GET请求的URL
        return  "http://dysmsapi.aliyuncs.com/?Signature=" + signature + sortQueryStringTmp;
    }
    private   String specialUrlEncode(String value) throws Exception {
        return java.net.URLEncoder.encode(value, "UTF-8").replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
    }
    private   String sign(String accessSecret, String stringToSign) throws Exception {
        javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA1");
        mac.init(new javax.crypto.spec.SecretKeySpec(accessSecret.getBytes("UTF-8"), "HmacSHA1"));
        byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
        return new sun.misc.BASE64Encoder().encode(signData);
    }


    public static void main(String[] args) throws ClientException {
        DefaultProfile profile = DefaultProfile.getProfile(
                "cn-qingdao",// 地域ID
                "LTAI4FqH2v37r4b8NfRkeih1",// 您的AccessKey ID
                "bnFZBSuaNWBn4dkSWkgayCBPSIzR0a");// 您的AccessKey Secret
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        // 接收短信的手机号码
        request.putQueryParameter("PhoneNumbers", "17770724957");
        // 短信签名名称。请在控制台签名管理页面签名名称一列查看（必须是已添加、并通过审核的短信签名）。
        request.putQueryParameter("SignName", "彼岸花网站");
        // 短信模板ID
        request.putQueryParameter("TemplateCode", "SMS_177241071");
        // 短信模板变量对应的实际值，JSON格式。
        request.putQueryParameter("TemplateParam", "{\"code\":\"8888\"}");
        CommonResponse commonResponse = client.getCommonResponse(request);
        String data = commonResponse.getData();
        System.out.println("1="+data);
        String sData = data.replaceAll("'\'", "");
        System.out.println("2"+sData);
        Gson gson = new Gson();
        Map map = gson.fromJson(sData, Map.class);
        Object bizId = map.get("BizId");
        System.out.println(bizId);
    }
}
