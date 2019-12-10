package com.wm.v1item.handler;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wm.constant.MQConstant;
import com.wm.entity.Product;
import com.wm.service.IProductService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author weimin
 * @ClassName RabbitHandler
 * @Description TODO
 * @date 2019/11/7 11:56
 */
@Component
@Slf4j
public class RabbitMqHandler {

    @Autowired
    private Configuration configuration;
    @Reference
    private IProductService productService;

    @RabbitListener(queues = MQConstant.QUEUE.PRODUCT_EXCHANGE_ITEM)
    @RabbitHandler
    public void create(Long id) throws IOException, TemplateException {
        Product product = productService.selectByPrimaryKey(id);
        try {
            Template template = configuration.getTemplate("list.ftl");
            Map<String,Object> data = new HashMap<>(1);
            data.put("product",product);
            String serverPath= ResourceUtils.getURL("classpath:static").getPath();
            FileWriter out = new FileWriter(serverPath+ File.separator+id+".html");
            template.process(data,out);
        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (TemplateException e) {
            log.error(e.getMessage());
        }
    }


}
