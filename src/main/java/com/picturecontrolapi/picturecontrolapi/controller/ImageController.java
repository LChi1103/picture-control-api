package com.picturecontrolapi.picturecontrolapi.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;

@RestController
@RequestMapping(value="/images")
public class ImageController {

    /* 访问图片 */
    @GetMapping(value = "/{name}",produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getImage(@PathVariable String name) throws Exception {

        System.out.println("用户的当前工作目录:" + System.getProperty("user.dir"));
//        System.out.println("Java类路径："+System.getProperty("java.class.path"));
//        System.out.println("文件分隔符（在 UNIX 系统中是“/”）:" + System.getProperty("file.separator"));
//        System.out.println("路径分隔符（在 UNIX 系统中是“:”）:" + System.getProperty("path.separator"));
        String path = ((System.getProperty("user.dir")).length() == 1 ? "" : System.getProperty("user.dir")) +
                      System.getProperty("file.separator") + "images" +
                      System.getProperty("file.separator") + name;
        System.out.println(path);

        File file = new File(path);
        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes, 0, inputStream.available());
        return bytes;

    }
}
