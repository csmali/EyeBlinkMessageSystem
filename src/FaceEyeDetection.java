
import com.google.zxing.WriterException;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import javax.sound.sampled.*;

/**
 *
 * @author user
 */
public class FaceEyeDetection {

    static int NUMBER_OF_NECESSARY_PING = 12;
    static JFrame frame;
    static JLabel lbl;
    static ImageIcon icon;
    static Server srv = new Server();
    static boolean isMessagesOpen = false;
    static TakePictureExample tpe;
    public static float SAMPLE_RATE = 8000f;

    public static void tone(int hz, int msecs)
            throws LineUnavailableException {
        tone(hz, msecs, 1.0);
    }

    public static void tone(int hz, int msecs, double vol)
            throws LineUnavailableException {
        byte[] buf = new byte[1];
        AudioFormat af
                = new AudioFormat(
                        SAMPLE_RATE, // sampleRate
                        8, // sampleSizeInBits
                        1, // channels
                        true, // signed
                        false);      // bigEndian
        SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
        sdl.open(af);
        sdl.start();
        for (int i = 0; i < msecs * 8; i++) {
            double angle = i / (SAMPLE_RATE / hz) * 2.0 * Math.PI;
            buf[0] = (byte) (Math.sin(angle) * 127.0 * vol);
            sdl.write(buf, 0, 1);
        }
        sdl.drain();
        sdl.stop();
        sdl.close();
    }

    public static void main(String[] args) throws IOException, WriterException, InterruptedException, LineUnavailableException {

        tpe = new TakePictureExample();
        int levelofChoice = 1;
        int minValueofChoice = 1;
        int numberofPingChoice = 0;
        int numberofConfirmationChoice = 0;
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        boolean shortClosed = false;
        boolean shortOpen = false;
        int closedCounter = 0;
        int openCounter = 0;
        int oldClosedCounter = 0;
        int oldOpenCounter = 0;
        boolean isTuned = false;
        boolean isMessageMode = false;
        String prevState = "noeye";
        String state = "";
        int move = 0;
        ArrayList<Integer> actStates = new ArrayList<Integer>();
        actStates.add(0);
        boolean bool = false;
        Scanner scan = new Scanner(System.in);
        CascadeClassifier cascadeFaceClassifier = new CascadeClassifier(
                "/root/Desktop/opencv-3.2.0/data/haarcascades/haarcascade_frontalface_default.xml");

        /*goz acik ve kapaliyken */
        CascadeClassifier EyeClassifier = new CascadeClassifier(
                "/root/Desktop/opencv-3.2.0/data/haarcascades/haarcascade_eye.xml");
        CascadeClassifier EyeTreeEyeClassifier = new CascadeClassifier(
                "/root/Desktop/opencv-3.2.0/data/haarcascades/haarcascade_eye_tree_eyeglasses.xml");
        /*goz acikken*/
        CascadeClassifier LeftEye2SplitsClassifier = new CascadeClassifier(
                "/root/Desktop/opencv-3.2.0/data/haarcascades/haarcascade_lefteye_2splits.xml");
        CascadeClassifier RightEye2SplitsClassifier = new CascadeClassifier(
                "/root/Desktop/opencv-3.2.0/data/haarcascades/haarcascade_righteye_2splits.xml");
        CascadeClassifier MCSLeftClassifier = new CascadeClassifier(
                "/root/Desktop/opencv-3.2.0/data/haarcascades/haarcascade_mcs_lefteye.xml");
        CascadeClassifier MCSRightClassifier = new CascadeClassifier(
                "/root/Desktop/opencv-3.2.0/data/haarcascades/haarcascade_mcs_righteye.xml");

        VideoCapture videoDevice = new VideoCapture();
        videoDevice.open(0);

        if (videoDevice.isOpened()) {
            while (true) {
                System.getenv("asdf"); // bunun ne oldugunu bilen yok

                /*       if (oldClosedCounter >= 5 && !isTuned) {
                    System.getenv("asdf"); // bunun ne oldugunu bilen yok

                    isTuned = true;
                    tone(400, 500);
                    Thread.sleep(1000);

                }*/
                if (isMessagesOpen) {
                    tpe.getMesajlar().getLabel(2).setVisible(false);

                    try {

                        if (state.equals("open") && oldClosedCounter >= 5) {
                            isMessageMode = !isMessageMode;
                            openCounter = -1;
                            closedCounter = 0;
                            tpe.getMesajlar().getLabel(2).setText("Mesaj moduna gectiniz");
                            tpe.getMesajlar().getLabel(2).setForeground(Color.GREEN);
                            levelofChoice = 1;
                            minValueofChoice = 1;
                            tpe.getMesajlar().getLabel(2).setVisible(true);

                        } else if (!isMessageMode) {
                            unpaintAllButtons(tpe);
                           tpe.getMesajlar().getLabel(2).setText("Mesaj Modu  icin  5 olana kadar gozunuzu kapali tutun  " + closedCounter);
                           tpe.getMesajlar().getLabel(2).setForeground(null);
                          

                            tpe.getMesajlar().getLabel(2).setVisible(true);
                        }
                        if (tpe.getMesajlar().isVisible() && isMessageMode) {

                            paintButtons(levelofChoice, minValueofChoice, tpe);

                            if (levelofChoice >= 4) {
                                numberofConfirmationChoice++;
                                if (numberofConfirmationChoice != NUMBER_OF_NECESSARY_PING) {

                                    tpe.getMesajlar().getLabel(2).setText(tpe.getMesajlar().getButton(minValueofChoice).getText() + " mesajini gonderiyorsunuz");
                                    tpe.getMesajlar().getLabel(2).setVisible(true);
                                    tpe.getMesajlar().getLabel(2).setForeground(Color.RED);
                                    if (levelofChoice == 5) {
                                        tpe.getMesajlar().getLabel(2).setText(tpe.getMesajlar().getButton(minValueofChoice).getText() + " mesaji gonderiliyor !");
                                        tpe.getMesajlar().getLabel(2).setVisible(true);
                                        tpe.getMesajlar().getLabel(2).setForeground(Color.GREEN);
                                        srv.sendNotification(getQr(), tpe.getMesajlar().getButton(minValueofChoice).getText());
                                        Thread.sleep(5000);
                                        unpaintAllButtons(tpe);
                                        tpe.getMesajlar().getLabel(2).setText("Mesaj moduna gectiniz");
                                        tpe.getMesajlar().getLabel(2).setForeground(Color.GREEN);
                                        tpe.getMesajlar().getLabel(2).setVisible(true);
                                        numberofConfirmationChoice = 0;
                                        levelofChoice = 1;
                                        minValueofChoice = 1;
                                        numberofPingChoice = 0;

                                    }

                                } else if (numberofConfirmationChoice == NUMBER_OF_NECESSARY_PING) {
                                    unpaintAllButtons(tpe);
                                    numberofConfirmationChoice = 0;
                                    levelofChoice = 1;
                                    minValueofChoice = 1;
                                    numberofPingChoice = 0;
                                    if (!shortClosed) {
                                        tpe.getMesajlar().getLabel(2).setText("mesaj gonderilmedi");
                                    }
                                }
                            }

                            Thread.sleep(50);
                            tpe.getMesajlar().getLabel(2).setBackground(null);

                            if (levelofChoice < 4) {
                                incrementNumberofChoice(numberofPingChoice);
                                numberofPingChoice++;

                                if (numberofPingChoice == NUMBER_OF_NECESSARY_PING) {
                                    unpaintButtons(levelofChoice, minValueofChoice, tpe);

                                    if (moveAhead(levelofChoice, minValueofChoice, tpe, numberofPingChoice) == 0) {
                                        minValueofChoice++;
                                    } else {
                                        minValueofChoice = moveAhead(levelofChoice, minValueofChoice, tpe, numberofPingChoice);
                                        numberofPingChoice = 1;
                                    }

                                }
                            }
                        }
                    } catch (InterruptedException ex) {
                        Logger.getLogger(FaceEyeDetection.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //   System.out.println(levelofChoice + "   "+ minValueofChoice + "     " + numberofPingChoice );
                    if (tpe.getMesajlar().isVisible()) {
                        Mat frameCapture = new Mat();
                        videoDevice.read(frameCapture);

                        /*open*/
                        MatOfRect eye = new MatOfRect();
                        EyeClassifier.detectMultiScale(frameCapture, eye);
                        MatOfRect eyetreeeye = new MatOfRect();
                        EyeTreeEyeClassifier.detectMultiScale(frameCapture, eyetreeeye);
                        /*open and closed*/
                        MatOfRect lefteye2splits = new MatOfRect();
                        LeftEye2SplitsClassifier.detectMultiScale(frameCapture, lefteye2splits);
                        MatOfRect mcsleft = new MatOfRect();
                        MCSLeftClassifier.detectMultiScale(frameCapture, mcsleft);
                        state = state(eye, eyetreeeye, lefteye2splits);

                        if (state.equals("open")) {
                            openCounter++;
                            oldClosedCounter = closedCounter;
                            closedCounter = 0;
                            tpe.getMesajlar().getLabel(3).setText("Gözünüz  açık");
                            tpe.getMesajlar().getLabel(3).setForeground(Color.GREEN);
                            shortOpen = true;
                            shortClosed = false;

                        } else if (state.equals("closed")) {
                            tpe.getMesajlar().getLabel(3).setText("Gözünüz  kapalı");
                            tpe.getMesajlar().getLabel(3).setForeground(Color.RED);
                            // openCounter = 0;
                            shortClosed = true;
                            closedCounter++;

                        } else if (state.equals("noeye")) {
                            tpe.getMesajlar().getLabel(3).setText("Göz bulunamadı");
                            tpe.getMesajlar().getLabel(3).setForeground(Color.BLACK);
                            closedCounter = 0;
                            openCounter = 0;
                            shortClosed = false;
                            shortOpen = false;
                        }

                        if (shortClosed && openCounter != -1 && isMessageMode) {
                            if (levelofChoice == 5) {
                                levelofChoice = 1;
                                unpaintAllButtons(tpe);

                            } else {
                                levelofChoice++;
                                unpaintAllButtons(tpe);
                                shortClosed = false;
                                closedCounter = 0;
                                openCounter = -1;
                                numberofPingChoice = 0;
                            }

                        }
                        System.out.println(state + "  " + openCounter + " " + closedCounter + " level  " + levelofChoice);
                        prevState = state;
                     //   PushImage(ConvertMat2Image(frameCapture));
                    }
                }
            }
        } else {
            System.out.println("Video aygitina baglanilamadi");
            return;
        }
    }

    public static String getQr() {
        File file = new File("Qr.txt");
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(QRCodeFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        String qr = null;

        BufferedReader br = new BufferedReader(fileReader);

        try {
            qr = br.readLine();
        } catch (IOException ex) {
            Logger.getLogger(QRCodeFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(QRCodeFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return qr;
    }

    // String state = state(eye, eyetreeeye,lefteye2splits, mcsleft);
    public static String state(MatOfRect eye, MatOfRect eyetreeeye, MatOfRect lefteye2splits) {

        if ((eye.empty() || eyetreeeye.empty()) && !lefteye2splits.empty()) {
            return "closed";
        } else if (eye.empty() && eyetreeeye.empty() && lefteye2splits.empty()) {
            return "noeye";
        } else {
            return "open";
        }

    }

    //Mat nesnesini image tipine donusturur
    private static BufferedImage ConvertMat2Image(Mat kameraVerisi) {

        MatOfByte byteMatVerisi = new MatOfByte();
        Imgcodecs.imencode(".jpg", kameraVerisi, byteMatVerisi);
        byte[] byteArray = byteMatVerisi.toArray();
        BufferedImage goruntu = null;
        try {
            InputStream in = new ByteArrayInputStream(byteArray);
            goruntu = ImageIO.read(in);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return goruntu;
    }

    public static void PencereHazirla() {
        frame = new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(700, 600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void PushImage(Image img2) {
        if (frame == null) {
            PencereHazirla();
        }
        if (lbl != null) {
            frame.remove(lbl);
        }
        icon = new ImageIcon(img2);
        lbl = new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.revalidate();
    }

    private static void removeButtons(int levelofChoice, TakePictureExample tpe) {
        if (levelofChoice == 2) {

            if (tpe.getMesajlar().getButton(1).getBackground() == Color.RED) {
                tpe.getMesajlar().getButton(5).setVisible(false);
                tpe.getMesajlar().getButton(6).setVisible(false);
                tpe.getMesajlar().getButton(7).setVisible(false);
                tpe.getMesajlar().getButton(8).setVisible(false);
            } else {
                tpe.getMesajlar().getButton(1).setVisible(false);
                tpe.getMesajlar().getButton(2).setVisible(false);
                tpe.getMesajlar().getButton(3).setVisible(false);
                tpe.getMesajlar().getButton(4).setVisible(false);
            }

        } else if (levelofChoice == 3) {
            System.out.println("ucte");
            if (tpe.getMesajlar().getButton(1).getBackground() == Color.RED) {
                tpe.getMesajlar().getButton(3).setVisible(false);
                tpe.getMesajlar().getButton(4).setVisible(false);
            } else if (tpe.getMesajlar().getButton(3).getBackground() == Color.RED) {
                tpe.getMesajlar().getButton(1).setVisible(false);
                tpe.getMesajlar().getButton(2).setVisible(false);
            } else if (tpe.getMesajlar().getButton(7).getBackground() == Color.RED) {
                tpe.getMesajlar().getButton(9).setVisible(false);
                tpe.getMesajlar().getButton(10).setVisible(false);
            } else {
                tpe.getMesajlar().getButton(7).setVisible(false);
                tpe.getMesajlar().getButton(8).setVisible(false);
            }

        }
    }

    private static void paintButtons(int levelofChoice, int minValueofChoice, TakePictureExample tpe) {
        if (levelofChoice == 1) {
            tpe.getMesajlar().getButton(minValueofChoice).setBackground(Color.RED);
            tpe.getMesajlar().getButton(minValueofChoice + 1).setBackground(Color.RED);
            tpe.getMesajlar().getButton(minValueofChoice + 2).setBackground(Color.RED);
            tpe.getMesajlar().getButton(minValueofChoice + 3).setBackground(Color.RED);
        } else if (levelofChoice == 2) {
            tpe.getMesajlar().getButton(minValueofChoice).setBackground(Color.RED);
            tpe.getMesajlar().getButton(minValueofChoice + 1).setBackground(Color.RED);

        } else if (levelofChoice == 3) {
            tpe.getMesajlar().getButton(minValueofChoice).setBackground(Color.RED);

        }
    }

    private static void unpaintButtons(int levelofChoice, int minValueofChoice, TakePictureExample tpe) {
        if (levelofChoice == 1) {
            tpe.getMesajlar().getButton(minValueofChoice).setBackground(null);
            tpe.getMesajlar().getButton(minValueofChoice + 1).setBackground(null);
            tpe.getMesajlar().getButton(minValueofChoice + 2).setBackground(null);
            tpe.getMesajlar().getButton(minValueofChoice + 3).setBackground(null);
        } else if (levelofChoice == 2) {
            tpe.getMesajlar().getButton(minValueofChoice).setBackground(null);
            tpe.getMesajlar().getButton(minValueofChoice + 1).setBackground(null);

        } else if (levelofChoice == 3) {
            tpe.getMesajlar().getButton(minValueofChoice).setBackground(null);
        }
    }

    private static void incrementNumberofChoice(int numberofPingChoice) {
        numberofPingChoice++;
    }

    private static int moveAhead(int levelofChoice, int minValueofChoice, TakePictureExample tpe, int numberofPingChoice) {
        if (levelofChoice == 1) {
            if (minValueofChoice == 1) {
                return 7;
            } else if (minValueofChoice == 7) {
                return 1;
            }

        } else if (levelofChoice == 2) {
            if (minValueofChoice == 1) {
                return 3;
            } else if (minValueofChoice == 3) {
                return 1;
            }
            if (minValueofChoice == 7) {
                return 9;
            } else if (minValueofChoice == 9) {
                return 7;
            }

        } else if (levelofChoice == 3) {
            if (minValueofChoice == 1) {
                return 2;
            } else if (minValueofChoice == 2) {
                return 1;
            } else if (minValueofChoice == 3) {
                return 4;
            } else if (minValueofChoice == 4) {
                return 3;
            } else if (minValueofChoice == 7) {
                return 8;
            } else if (minValueofChoice == 8) {
                return 7;
            } else if (minValueofChoice == 9) {
                return 10;
            } else if (minValueofChoice == 10) {
                return 9;
            } else if (minValueofChoice == 9) {
                return 1;
            }

        }
        return 0;
    }

    private static void unpaintAllButtons(TakePictureExample tpe) {
        tpe.getMesajlar().getButton(1).setBackground(null);
        tpe.getMesajlar().getButton(2).setBackground(null);
        tpe.getMesajlar().getButton(3).setBackground(null);
        tpe.getMesajlar().getButton(4).setBackground(null);
        tpe.getMesajlar().getButton(5).setBackground(null);
        tpe.getMesajlar().getButton(6).setBackground(null);
        tpe.getMesajlar().getButton(7).setBackground(null);
        tpe.getMesajlar().getButton(8).setBackground(null);
        tpe.getMesajlar().getButton(9).setBackground(null);
        tpe.getMesajlar().getButton(10).setBackground(null);
    }
}
