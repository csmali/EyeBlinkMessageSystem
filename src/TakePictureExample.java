
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import java.awt.Dimension;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TakePictureExample {

    static Mesajlar msj;

    public TakePictureExample() throws IOException, WriterException {
        File file = new File("name.txt");
        FileReader fileReader = null;
        try {

            fileReader = new FileReader(file);
            Mesajlar mesajlar = new Mesajlar();
            msj = mesajlar;
            FaceEyeDetection.isMessagesOpen = true;

            mesajlar.setVisible(true);
            System.out.println("1");
        } catch (FileNotFoundException ex) {
            HastaKayt hastakayit = new HastaKayt();
            hastakayit.setVisible(true);
            System.out.println("2");
        }
    }

    public Mesajlar getMesajlar() {
        return msj;
    }
}
