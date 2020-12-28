package com.picturecontrolapi.picturecontrolapi.controller;

import com.picturecontrolapi.picturecontrolapi.bean.ImagesDisposeData;
import com.picturecontrolapi.picturecontrolapi.common.Result;
import com.picturecontrolapi.picturecontrolapi.server.ImageServer;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.UUID;

@RestController
@RequestMapping(value="/images")
//@Slf4j
public class ImageController {

    @Autowired
    private ImageServer imageServer;

    /* 访问图片 */
    @GetMapping(value = "/visit/{name}",produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getImage (@PathVariable String name) throws Exception {
        return imageServer.getImage(name);
    }

    /* 图片（文件）上传 */
    @PostMapping("/upload")
    public Result upload (@RequestParam(value = "file") MultipartFile file) {
        return imageServer.upload(file);
    }

    /* 图片（文件）下载 */
    @GetMapping("/download/{name}")
    public ResponseEntity<?> getImages (@PathVariable String name){
        return imageServer.getImages(name);
    }

    /* 根据数据把切割之后的图片的名字返回然后前台下载 */
    @PostMapping("/download-dispose/{name}")
    public Result getImagesDispose (@PathVariable String name, @RequestBody ImagesDisposeData data) throws IOException {
        return imageServer.getImagesDispose(name, data);
    }
}
