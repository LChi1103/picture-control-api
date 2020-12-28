package com.picturecontrolapi.picturecontrolapi.server;

import com.picturecontrolapi.picturecontrolapi.bean.ImagesDisposeData;
import com.picturecontrolapi.picturecontrolapi.common.Result;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Component
public class ImageServer {

    @Autowired
    private ResourceLoader resourceLoader;

    public byte[] getImage (String name) throws Exception {
        System.out.println("用户的当前工作目录:" + System.getProperty("user.dir"));
        System.out.println("文件分隔符（在 UNIX 系统中是“/”）:" + System.getProperty("file.separator"));
        String path = this.getSavePath() + name;
        System.out.println(path);

        File file = new File(path);
        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes, 0, inputStream.available());
        return bytes;
    }

    public Result upload (MultipartFile file) {
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
        if (!suffix.toLowerCase().equals(".png") && !suffix.toLowerCase().equals(".jpg") && !suffix.toLowerCase().equals(".jpeg")) {
            return new Result(false, "上传失败", "请上传 png / jpg / jpeg 格式的图片");
        }
        // 路径
        String path = this.getSavePath();
        System.out.println("路径为：" + path);
        // 完整路径
        String fullPath = path + newFileName;
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

    public ResponseEntity<?> getImages (String name){
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("content-type", "application/octet-stream");
            headers.add("Content-Disposition", "attachment;fileName=" + name);
            String path = this.getSavePath() + name;
            System.out.println("路径为：" + path);
            Resource resource = resourceLoader.getResource("file:" + path);
            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        } catch (Exception e) {
            throw e;
        }
    }

    @PostMapping("/download-dispose/{name}")
    public Result getImagesDispose (String name, ImagesDisposeData data) throws IOException {
        try {
            System.out.println("=-----------------------------" );
            System.out.println("name：" + name );
            System.out.println("data：" + data );
            // 基础路径
            String basePath = this.getSavePath();
            System.out.println("基础路径：" + basePath);
            // 后缀
            String suffix = name.substring(name.lastIndexOf("."));
            System.out.println("后缀：" + suffix);
            // 新名字
            String uuid = UUID.randomUUID().toString();
            System.out.println("新名字：" + uuid);

            // 裁剪图片并保存
            Thumbnails.of(basePath + name)
                    .sourceRegion(data.getX(),data.getY(), data.getWidth(), data.getHeight())
                    .rotate(data.getRotate())
                    .scale(data.getScaleX(), data.getScaleY())
                    .toFile(basePath + uuid + suffix);

            // 发送图片路径
            System.out.println("=-----------------------------" );
            return new Result(true, "成功", uuid + suffix);
        } catch (Exception e) {
            return new Result(false, "失败", e);
        }
    }

    // 获取当前保存图片文件的目录
    private String getSavePath () {
        return ((System.getProperty("user.dir")).length() == 1 ? "" : System.getProperty("user.dir")) + System.getProperty("file.separator") + "images" + System.getProperty("file.separator");
    }
}
