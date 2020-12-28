package com.picturecontrolapi.picturecontrolapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TimeTask {

    int count = 0;

    @Scheduled(cron = "59 59 23 * * ? ")
    public void cronTask() throws InterruptedException {
        this.callCMD("cp -fp /images/picture.jpg /picture.jpg");
        this.callCMD("rm -rf /images");
        this.callCMD("mkdir /images");
        this.callCMD("cp -fp /picture.jpg /images/picture.jpg");
    }

    // 执行 shell 命令
    private void callCMD(String cmd) {
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            int status = process.waitFor();
            if (status != 0) {
                System.err.println("Failed to call shell's command and the return status's is: " + status);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
