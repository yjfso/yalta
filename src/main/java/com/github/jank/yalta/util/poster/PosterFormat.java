package com.github.jank.yalta.util.poster;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author yinjianfeng
 * @date 2018/11/19
 */
@NoArgsConstructor
@AllArgsConstructor
public enum PosterFormat {
    //jpeg
    JPEG,
    JPEG10(1),
    JPEG6(0.6f),
    JPEG4(0.4f),
    PNG {
        @Override
        public void write(BufferedImage image, ByteArrayOutputStream output) throws IOException {
            ImageIO.write(image, "png", output);
        }
    };

    private float quality = 0.8f;

    public void write(BufferedImage image, ByteArrayOutputStream output) throws IOException{
        Thumbnails.of(image).scale(1).outputQuality(quality).outputFormat("jpeg").toOutputStream(output);
    }
}
