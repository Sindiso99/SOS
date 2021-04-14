package com.swansea.sindiso;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;

import java.nio.ByteBuffer;

import static android.graphics.ImageFormat.YUV_420_888;
import static android.graphics.ImageFormat.YUV_422_888;
import static android.graphics.ImageFormat.YUV_444_888;

public class QRLabelAnalyzer implements ImageAnalysis.Analyzer {
    private QRCodeFoundListener listener;
    private ByteBuffer byteBuffer;
    private PlanarYUVLuminanceSource source;
    private BinaryBitmap binaryBitmap;
    private Result result;

    public QRLabelAnalyzer(QRCodeFoundListener listener) {
        this.listener = listener;
    }
    @Override
    public void analyze(@NonNull ImageProxy image) {
        if (image.getFormat() == YUV_420_888 || image.getFormat() == YUV_422_888 || image.getFormat() == YUV_444_888) {
            byteBuffer = image.getPlanes()[0].getBuffer();
            byte[] imageData = new byte[byteBuffer.capacity()];
            byteBuffer.get(imageData);


            source = new PlanarYUVLuminanceSource(
                    imageData,
                    image.getWidth(), image.getHeight(),
                    0, 0,
                    image.getWidth(), image.getHeight(),
                    false
            );
            binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));

            try {
                result = new QRCodeMultiReader().decode(binaryBitmap);
                listener.onQRCodeFound(result.getText());
            } catch (ChecksumException | NotFoundException | FormatException e) {
                listener.qrCodeNotFound();
            }
        }
        image.close();
    }
}