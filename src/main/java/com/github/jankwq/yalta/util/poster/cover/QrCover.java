package com.github.jankwq.yalta.util.poster.cover;

import com.github.jankwq.yalta.util.poster.QrCodeUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * @author yinjianfeng
 * @date 2018/11/14
 */
@Slf4j
@Getter
@Setter
@NoArgsConstructor
public class QrCover extends AbstractImgCover {

    private String text;

    /**
     * 边框
     */
    private Integer margin = 0;

    private Format format = Format.PNG;

    public QrCover(String text) {
        setText(text);
    }
    public enum Format{
        //
        PNG, JPEG
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        try {
            ByteArrayOutputStream stream = QrCodeUtil.makeQrcode(this);
            InputStream inputStream = new ByteArrayInputStream(stream.toByteArray());
            Image tmpImg = ImageIO.read(inputStream);
            draw0(graphics2D, tmpImg);
        } catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

    public QrCover setText(String text){
        this.text = text;
        return this;
    }
}
