package com.github.jankwq.yalta.util.poster;

import com.github.jankwq.yalta.util.poster.cover.AbstractCover;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * 海报生成器
 *   目前支持二维码/文字/远程图片cover
 * demo：
 *        DirectStream stream = Poster.create().setPosterImgUrl("/static/image/activity.png")
 *                 .addCover(
 *                         new QrCover("hahahah")
 *                                 .setSideLength(190).setPosition(550, 1130)
 *                 )
 *                 .addCover(
 *                         new WebImgCover()
 *                                 .setImgUrl("https://img-dm.csdnimg.cn/dangdang/23703129.jpg")
 *                                 .setSideLength(100).setPosition(34, 16)
 *                 )
 *                 .addCover(
 *                         new TextCover("hahahah")
 *                                 .setColor(new Color(0x0))
 *                                 .setFont(new Font(null, 1, 40))
 *                                 .setPosition(34, 500)
 *                 )
 *                 .build();
 * @author yinjianfeng
 * @date 2018/11/14
 */
@Slf4j
@Getter
public class Poster {

    private final static String BG_IMG_SAVE_PATH = "./runtime/post/bgimg/";

    private String posterImgUrl;

    private List<AbstractCover> covers = new ArrayList<>();

    private PosterFormat posterFormat = PosterFormat.JPEG;

    private static Map<String, BufferedImage> bgImageMap = new WeakHashMap<>();

    public Poster addCover(AbstractCover coverEntity){
        this.covers.add(coverEntity);
        return this;
    }

    public Poster setPosterImgUrl(String posterImgUrl){
        this.posterImgUrl = posterImgUrl;
        return this;
    }

    public Poster setFormat(PosterFormat posterFormat){
        this.posterFormat = posterFormat;
        return this;
    }

    public static Poster create(){
        return new Poster();
    }

    /**
     * 构建海报
     *
     * @return
     * @throws IOException
     */
    public ByteArrayOutputStream build() throws IOException {
        long start = System.currentTimeMillis();
        BufferedImage bgImage = bgImageMap.get(posterImgUrl);
        if (bgImage == null) {
            File file = new File(BG_IMG_SAVE_PATH + DigestUtils.md5Hex(posterImgUrl));
            if (!file.exists()) {
                URL url = new URL(posterImgUrl);
                file.getParentFile().mkdirs();
                if (file.createNewFile()) {
                    FileUtils.copyURLToFile(url, file);
                    bgImage = ImageIO.read(file);
                } else {
                    bgImage = ImageIO.read(url);
                }
            } else {
                bgImage = ImageIO.read(file);
            }
        }
        long end  = System.currentTimeMillis();
        log.info("[WMP] poster load image time:{}", end - start);
//        BufferedImage bgImage = ImageIO.read(this.getClass().getResourceAsStream(posterImgUrl));
        start = System.currentTimeMillis();
        BufferedImage image = new BufferedImage(bgImage.getWidth(), bgImage.getHeight(), BufferedImage.TYPE_INT_BGR);
        Graphics2D graphics2D = image.createGraphics();
        graphics2D.drawImage(bgImage, 0, 0, null);
        end  = System.currentTimeMillis();
        log.info("[WMP] poster draw bgimage time:{}", end - start);
        covers.forEach(cover -> cover.drawProxy(graphics2D));

        graphics2D.dispose();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        posterFormat.write(image, output);
        return output;
    }
    
}
