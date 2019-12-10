package com.wm.v1item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.rabbitmq.client.Channel;
import com.wm.base.entity.ResultBean;
import com.wm.entity.Product;
import com.wm.service.IProductService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author weimin
 * @ClassName ItemController
 * @Description TODO
 * @date 2019/11/4 17:03
 */
@Controller
@RequestMapping("item")
@Slf4j
@Component
public class ItemController {

    @Reference
    private IProductService productService;
    @Autowired
    private Configuration configuration;
    @Autowired
    private ExecutorService pool;

    /*@RabbitHandler
    @RabbitListener(queues = "one")
    public void test(Message message, Channel channel, String result) throws IOException {
        System.out.println("信息是："+result);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }*/

    @RequestMapping("create/{id}")
    @ResponseBody
    public ResultBean<String> create(@PathVariable Long id){
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
            return new ResultBean<>(500,"读取模版失败");
        } catch (TemplateException e) {
            log.error(e.getMessage());
            return new ResultBean<>(500,"生成模版失败");
        }
        return new ResultBean<>(200,"创建模版成功！！！");
    }
    @RequestMapping("batchCreate")
    @ResponseBody
    public ResultBean<String> batchCreate(@RequestParam List<Long> ids) {
        List<Product> list = productService.getListByIds(ids);
        final CountDownLatch latch=new CountDownLatch(list.size());
        List<Future> futureList = new ArrayList<>(list.size());
        for (Product product : list) {
            Future<Long> submit = pool.submit(new CreateHtmlTask(product));
            futureList.add(submit);
        }
        try {
            latch.await();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (Future future : futureList) {
            try {
                System.out.println(future.get());
                Long rs = (Long) future.get();
                if(rs!=0){

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return new ResultBean<>(200,"批量创建成功！！！");
    }

    private class CreateHtmlTask implements Callable<Long> {

        private Product product;


        public CreateHtmlTask(Product product){
            this.product = product;
        }

        @Override
        public Long call() throws Exception {
            try {
                Template template = configuration.getTemplate("list.ftl");
                Map<String,Object> data = new HashMap<>(1);
                data.put("product",product);
                String serverPath= ResourceUtils.getURL("classpath:static").getPath();
                FileWriter out = new FileWriter(serverPath+ File.separator+product.getId()+".html");
                template.process(data,out);
            } catch (IOException | TemplateException e) {
                log.error(e.getMessage());
                return product.getId();
            }
            return 0L;
        }
    }
}


