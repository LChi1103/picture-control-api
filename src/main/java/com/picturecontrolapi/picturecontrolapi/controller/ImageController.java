package com.picturecontrolapi.picturecontrolapi.controller;

import com.picturecontrolapi.picturecontrolapi.bean.ImagesDisposeData;
import com.picturecontrolapi.picturecontrolapi.common.Result;
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

    /* 访问图片 */
    @GetMapping(value = "/visit/{name}",produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getImage (@PathVariable String name) throws Exception {

        System.out.println("用户的当前工作目录:" + System.getProperty("user.dir"));
        System.out.println("文件分隔符（在 UNIX 系统中是“/”）:" + System.getProperty("file.separator"));
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

    /* 图片（文件）上传 */
    @PostMapping("/upload")
    public Result upload (@RequestParam(value = "file") MultipartFile file) {
        if (file.isEmpty()) {
            return new Result(false, "上传失败","文件为空");
        }
        // 获取文件名
        String oldFileName = file.getOriginalFilename();
        System.out.println("上传的文件名为：" + oldFileName);
        // 获取后缀，并且生成随即名称
        String suffix = oldFileName.substring(oldFileName.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString();
        String newFileName = uuid + suffix;
        System.out.println("保存的文件名为：" + newFileName);
        // 路径
        String path = ((System.getProperty("user.dir")).length() == 1 ? "" : System.getProperty("user.dir")) +
                      System.getProperty("file.separator") + "images";
        System.out.println("路径为：" + path);
        // 完整路径
        String fullPath = path + System.getProperty("file.separator") + newFileName;
        System.out.println("完整路径为：" + fullPath);

        // 创建文件进行保存
        File dest = new File(fullPath);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        // 保存
        try {
            file.transferTo(dest);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return new Result(false, "上传失败", e);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, "上传失败", e);
        }
        return new Result(true, "上传成功", newFileName);
    }

    @Autowired
    private ResourceLoader resourceLoader;

    /* 图片（文件）下载 */
    @GetMapping("/download/{name}")
    public ResponseEntity<?> getImages (@PathVariable String name){
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("content-type", "application/octet-stream");
            headers.add("Content-Disposition", "attachment;fileName=" + name);
            String path = ((System.getProperty("user.dir")).length() == 1 ? "" : System.getProperty("user.dir")) +
                    System.getProperty("file.separator") + "images" +
                    System.getProperty("file.separator") + name;
            System.out.println("路径为：" + path);
            Resource resource = resourceLoader.getResource("file:" + path);
            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        } catch (Exception e) {
            throw e;
        }
    }

    /* 根据数据下载切割之后的图片 */
    @PostMapping("/download-dispose/{name}")
    public ResponseEntity<?> getImagesDispose (@PathVariable String name, @RequestBody ImagesDisposeData data) throws IOException {
        try {
            System.out.println("=-----------------------------" );
            System.out.println("name：" + name );
            System.out.println("data：" + data );
            // 基础路径
            String basePath = ((System.getProperty("user.dir")).length() == 1 ? "" : System.getProperty("user.dir")) +
                              System.getProperty("file.separator") + "images" + System.getProperty("file.separator");
            System.out.println("基础路径：" + basePath);
            // 后缀
            String suffix = name.substring(name.lastIndexOf("."));
            System.out.println("后缀：" + suffix);
            // 新名字
            String uuid = UUID.randomUUID().toString();
            System.out.println("新名字：" + uuid);

            // 裁剪图片并保存
            Thumbnails.of(basePath + name)
                    .sourceRegion(Positions.BOTTOM_RIGHT,data.getX(),data.getY())
                    .size(data.getHeight(), data.getWidth())
                    .rotate(data.getRotate())
                    .scale(data.getScaleX(), data.getScaleY())
                    .toFile(basePath + uuid + suffix);

            // 发送下载
            HttpHeaders headers = new HttpHeaders();
            headers.add("content-type", "application/octet-stream");
            headers.add("Content-Disposition", "attachment;fileName=" + uuid + suffix);
            String path = basePath + uuid + suffix;
            System.out.println("发送下载：" + path);
            Resource resource = resourceLoader.getResource("file:" + path);
            System.out.println("=-----------------------------" );
            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        } catch (Exception e) {
            throw e;
        }
    }
}
