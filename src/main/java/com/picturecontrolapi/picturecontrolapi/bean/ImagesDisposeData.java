package com.picturecontrolapi.picturecontrolapi.bean;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ImagesDisposeData {
    @NotNull
    private int x = 0;

    @NotNull
    private int y = 0;

    @NotNull
    private int width = 0;

    @NotNull
    private int height = 0;

    @NotNull
    private Double rotate = 0.00;

    @NotNull
    private Double scaleX = 0.00;

    @NotNull
    private Double scaleY = 0.00;
}
