package com.picturecontrolapi.picturecontrolapi.common;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class Result {

    private Boolean success = false;

    private String msg = "失败";

    private Object content = new Object();

    public Result () {};

    public Result (Boolean success, String msg, Object content) {
        this.success = success;
        this.msg = msg;
        this.content = content;
    }

}
