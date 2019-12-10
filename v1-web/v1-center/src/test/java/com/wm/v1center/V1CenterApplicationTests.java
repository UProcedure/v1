package com.wm.v1center;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import com.github.pagehelper.PageInfo;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.wm.entity.Product;
import com.wm.service.IProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest
@EnableDubbo
public class V1CenterApplicationTests {

    @Reference
    private IProductService productService;
    @Autowired
    private FastFileStorageClient client;

    @Test
    public void contextLoads() {
    }

    @Test
    public void pageListTest(){
        PageInfo<Product> pageList = productService.getPageList(1, 1, 3);
        System.out.println(pageList.getList().size());
    }

    @Test
    public void uploadTest() throws FileNotFoundException {
        File file = new File("E:\\Java1907Work\\v1\\v1-web\\v1-center\\lo.jpg");
        FileInputStream is = new FileInputStream(file);
        StorePath storePath = client.uploadFile(is, file.length(), "jpg", null);
        System.out.println(storePath.getFullPath());
        System.out.println(storePath.getPath());
    }

    @Test
    public void deleteUploadTest(){
        client.deleteFile("group1/M00/00/00/rBAAB126inSAB5W7ABuElaIOxFU283.jpg");
    }

}
