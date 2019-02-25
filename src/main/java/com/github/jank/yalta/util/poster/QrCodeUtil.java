package com.github.jank.yalta.util.poster;

import com.github.jank.yalta.util.poster.cover.QrCover;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yinjianfeng
 * @date 2018/11/14
 */
public class QrCodeUtil {

    public static ByteArrayOutputStream makeQrcode(QrCover qrCoverEntity) throws WriterException, IOException {
        Map<EncodeHintType, Object> hints = new HashMap<>(3);
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        hints.put(EncodeHintType.MARGIN, qrCoverEntity.getMargin());

        BitMatrix bitMatrix = new MultiFormatWriter().encode(
                qrCoverEntity.getText(), BarcodeFormat.QR_CODE,
                qrCoverEntity.getWidth(), qrCoverEntity.getHeight(), hints
        );
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, qrCoverEntity.getFormat().name().toLowerCase(), outStream, null);
        return outStream;
    }

}
