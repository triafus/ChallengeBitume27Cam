package lml.snir.mavenproject2;

// Java code to read the QR code
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.awt.image.BufferedImage;

public class QRCodeReaderFromFile {

    // Function to read the QR file
    public static String readQR(String path, String charset,
            Map hashMap)
            throws FileNotFoundException, IOException,
            NotFoundException {
        FileInputStream f = new FileInputStream(path);
        BufferedImage bf= ImageIO.read(f);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                        new BufferedImageLuminanceSource(bf
                                )));

        Result result
                = new MultiFormatReader().decode(binaryBitmap);

        return result.getText();
    }

    // Driver code
    public static void main(String[] args)
            throws WriterException, IOException,
            NotFoundException {

        // Path where the QR code is saved
        String path = "./dossart-test.png";

        // Encoding charset
        String charset = "UTF-8";

        Map<EncodeHintType, ErrorCorrectionLevel> hashMap
                = new HashMap<EncodeHintType, ErrorCorrectionLevel>();

        hashMap.put(EncodeHintType.ERROR_CORRECTION,
                ErrorCorrectionLevel.L);

        System.out.println(
                "QRCode output: "
                + readQR(path, charset, hashMap));
    }

}
