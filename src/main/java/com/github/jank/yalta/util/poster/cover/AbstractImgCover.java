package com.github.jank.yalta.util.poster.cover;

import lombok.Getter;

import java.awt.*;

/**
 * @author yinjianfeng
 * @date 2018/11/14
 */
@Getter
public abstract class AbstractImgCover extends AbstractCover {

    protected Integer width = 100;

    protected Integer height = 100;

    protected void draw0(Graphics2D graphics2D, Image image){
        graphics2D.drawImage(image, x, y, width, height, null);
    }

    public AbstractImgCover setSideLength(Integer value){
        this.width = value;
        this.height = value;
        return this;
    }

    public AbstractImgCover setSideLength(Integer width, Integer height){
        this.width = width;
        this.height = height;
        return this;
    }

    public AbstractImgCover setWidth(Integer value){
        this.width = value;
        return this;
    }

    public AbstractImgCover setHeight(Integer value){
        this.height = value;
        return this;
    }

}
