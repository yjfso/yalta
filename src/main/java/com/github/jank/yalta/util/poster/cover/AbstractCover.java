package com.github.jank.yalta.util.poster.cover;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

/**
 * @author yinjianfeng
 * @date 2018/11/14
 */
@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = QrCover.class, name = "qr"),
                @JsonSubTypes.Type(value = TextCover.class, name = "text"),
                @JsonSubTypes.Type(value = WebImgCover.class, name = "webImg")
        }
)
@Slf4j
public abstract class AbstractCover {

    protected Integer x = 0;

    protected Integer y = 0;

    protected String type;

    /**
     * 执行绘制动作
     *
     * @param graphics2D 画布
     */
    public abstract void draw(Graphics2D graphics2D);

    public void drawProxy(Graphics2D graphics2D) {
        long start = System.currentTimeMillis();
        draw(graphics2D);
        long end  = System.currentTimeMillis();
        log.info("[WMP] drow cover: {} time:{}", type, end - start);
    }

    public AbstractCover setPosition(Integer x, Integer y){
        this.x = x;
        this.y = y;
        return this;
    }

    public AbstractCover setX(Integer x){
        this.x = x;
        return this;
    }

    public AbstractCover setY(Integer y){
        this.y = y;
        return this;
    }

}
