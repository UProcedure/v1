package com.wm.v1item.common;

import com.wm.entity.Product;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author weimin
 * @ClassName ItemUtils
 * @Description TODO
 * @date 2019/11/7 11:57
 */
@Slf4j
public class ItemUtils {

    public static void create(Configuration configuration,String item,Map<String,Object> data,String itemName) throws IOException, TemplateException {
        Template template = configuration.getTemplate(item);
        String serverPath= ResourceUtils.getURL("classpath:static").getPath();
        FileWriter out = new FileWriter(serverPath+ File.separator+itemName+".html");
        template.process(data,out);
    }

}
