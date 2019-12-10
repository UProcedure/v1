package com.wm.v1center.controller;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.wm.base.entity.ResultBean;
import com.wm.v1center.pojo.WangEditorResultBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author weimin
 * @ClassName FileController
 * @Description TODO
 * @date 2019/11/1 9:59
 */
@RestController
@RequestMapping("file")
@Slf4j
public class FileController {

    @Autowired
    private FastFileStorageClient client;
    @Value("${images.serverPath}")
    private String imageServer;

    @RequestMapping("uploadImage")
    @ResponseBody
    public ResultBean<String> uploadImage(MultipartFile file){
        System.out.println("文件名="+file.getOriginalFilename());
        try {
            String originalFilename = file.getOriginalFilename();
            String substring = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            log.info(substring);
            StorePath storePath = client.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(), substring, null);
            StringBuilder stringBuilder = new StringBuilder(imageServer).append(storePath.getFullPath());
            log.info(stringBuilder.toString());
            return new ResultBean<>(200,stringBuilder.toString());
        }catch (IOException e){
            log.error(e.toString());
        }
        return new ResultBean<>(500,"上传失败");
    }

    @RequestMapping("batchUploadImages")
    @ResponseBody
    public WangEditorResultBean batchUploadImages(MultipartFile[] files){
        String[] data = new String[files.length];
        try {
            for(int i=0;i<files.length;i++){
                String originalFilename = files[i].getOriginalFilename();
                String substring = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
                StorePath storePath = client.uploadImageAndCrtThumbImage(files[i].getInputStream(), files[i].getSize(), substring, null);
                StringBuilder stringBuilder = new StringBuilder(imageServer).append(storePath.getFullPath());
                log.info(stringBuilder.toString());
                data[i] = stringBuilder.toString();
            }
            return new WangEditorResultBean(0,data);
        }catch (IOException e){
            log.error(e.getMessage());
        }
        return new WangEditorResultBean(500,null);
    }

}
