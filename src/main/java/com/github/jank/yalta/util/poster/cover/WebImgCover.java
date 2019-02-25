package com.github.jank.yalta.util.poster.cover;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.net.URL;

/**
 * @author yinjianfeng
 * @date 2018/11/14
 */
@Getter
@Slf4j
@NoArgsConstructor
public class WebImgCover extends AbstractImgCover {

    private String imgUrl;

    public WebImgCover(String imgUrl) {
        setImgUrl(imgUrl);
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        try {
            if (StringUtils.isNotEmpty(imgUrl)) {
                URL url = new URL(imgUrl);
                draw0(graphics2D, ImageIO.read(url));
            }
        } catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

    public WebImgCover setImgUrl(String imgUrl){
        this.imgUrl = imgUrl;
        return this;
    }
}
