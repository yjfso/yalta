package com.github.jankwq.yalta.util.poster;

import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
* 二维码的生成需要借助MatrixToImageWriter类，该类是由Google提供的，可以将该类直接拷贝到源码中使用
*/
public class MatrixToImageWriter {
	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;
	
	private MatrixToImageWriter() {
	}
	
	public static BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
			image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
			}
		}
		return image;
	}
	
	public static void writeToFile(BitMatrix matrix, String format, File file) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		if (!ImageIO.write(image, format, file)) {
			throw new IOException("Could not write an image of format " + format + " to " + file);
		}
	}

    /**
     * 使用本方法一定注意tomcat 中是否有temp 目录，没有需要 创建temp目录，由于本方法 使用 ImageIO.write(image, format, stream) 方法
     *
     * @param matrix
     * @param format
     * @param stream
     * @param logoBytes
     * @throws IOException
     */
    public static void writeToStream(BitMatrix matrix, String format, OutputStream stream, byte[] logoBytes) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (logoBytes != null) {
            Graphics2D g = image.createGraphics();
            ByteArrayInputStream input = new ByteArrayInputStream(logoBytes);
            //载入logo
            BufferedImage logo = ImageIO.read(input);
            int widthLogo = logo.getWidth(null) > image.getWidth() * 2 / 10 ? (image.getWidth() * 2 / 10) : logo.getWidth(null),
                    heightLogo = logo.getHeight(null) > image.getHeight() * 2 / 10 ? (image.getHeight() * 2 / 10) : logo.getWidth(null);

            int x = (image.getWidth() - widthLogo) / 2;
            int y = (image.getHeight() - heightLogo) / 2;

            g.drawImage(logo, x, y, widthLogo, heightLogo, null);
            g.drawRoundRect(x, y, widthLogo, heightLogo, 15, 15);
            g.setStroke(new BasicStroke(1));
            g.setColor(Color.WHITE);
            g.drawRect(x, y, widthLogo, heightLogo);

            g.dispose();
            logo.flush();
            image.flush();
        }

        //ImageIO.write(image, format, stream)，如果 tomcat 中没有设置 temp 临时目录，
        // 需要添加因为 ImageIO 调用 write 方法之前需要设置 ImageIO.setCacheDirectory();缓存目录
        // 如果没有，默认 java.io.tmpdir 系统变量，即使用 tomcat 中的 temp 目录，如果没有会报错 "Can't create output stream!" "Can't create cache file!"
        if (!ImageIO.write(image, format, stream)) {
            throw new IOException("Could not write an image of format " + format);
        }
    }
} 
