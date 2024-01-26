package lml.snir.chronometre;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static javafx.application.Application.launch;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import com.github.sarxos.example1.WebcamQRCodeExample;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import javax.swing.JTextArea;

public class Test extends JFrame implements Runnable, ThreadFactory {

    private static final long serialVersionUID = 6441489157408381878L;

    private Executor executor = Executors.newSingleThreadExecutor(this);

    Timer t;
    private int millisec = 0;
    private boolean stop = true; // set to false to stop the program.
    JLabel lab, lap;
    JButton startBtn, stopBtn, lapBtn, resetBtn;

    private int sec = 0;
    private int min = 0;
    private int hours = 0;

    int nbr_Part = 30;
    int index = 0;
    String[] dataTimeList = new String[nbr_Part];

    private Webcam webcam = null;
    private WebcamPanel panel = null;
    private JTextArea textarea = null;

    public Test() {

        setTitle(" frame");
        this.setSize(770, 440);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new FlowLayout());
        lab = new JLabel();
        lap = new JLabel();

        Dimension size = WebcamResolution.QVGA.getSize();

        webcam = Webcam.getWebcams().get(0);
        webcam.setViewSize(size);

        panel = new WebcamPanel(webcam);
        panel.setPreferredSize(size);
        panel.setFPSDisplayed(true);

        textarea = new JTextArea();
        textarea.setEditable(false);
        textarea.setPreferredSize(size);

        add(panel);
        add(textarea);

        startBtn = new JButton("start");
        startBtn.addActionListener((e) -> {
            stop = false;
        });
        stopBtn = new JButton("stop");
        stopBtn.addActionListener((e) -> {
            stop = true;
        });
        lapBtn = new JButton("lap");
        lapBtn.addActionListener((e) -> {
            laps();
        });
        resetBtn = new JButton("reset");
        resetBtn.addActionListener((e) -> {
            reset();
        });
        lab.setText("h : 0 , min : 0 , sec : 0 , millisec : 0");
        lap.setText("lap : ");
        this.add(lab);
        this.add(lap);
        this.add(startBtn);
        this.add(stopBtn);
        this.add(lapBtn);
        this.add(resetBtn);

        pack();
        setVisible(true);

        executor.execute(this);
    }

    @Override
    public void run() {

        do {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Result result = null;
            BufferedImage image = null;

            if (webcam.isOpen()) {

                if ((image = webcam.getImage()) == null) {
                    continue;
                }

                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                try {
                    result = new MultiFormatReader().decode(bitmap);
                } catch (NotFoundException e) {
                    // fall thru, it means there is no QR code in image
                }
            }

            if (result != null) {
                laps();
                textarea.setText(result.getText());
            }

        } while (true);
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!stop) {
                    millisec++;
                    SwingUtilities.invokeLater(() -> {
                        lab.setText(convertTimeToString());
                    });
                }
            }
        },
                0, 1);
    }

    public void dataLap() {

        String[] lapParticipant = new String[nbr_Part]; // 0 -> 29 = 30
        if (nbr_Part > -1) {
            laps();
            nbr_Part--;
        } else {
            stop = true;
        }

    }

    public void laps() {
        if (!stop) {
            SwingUtilities.invokeLater(() -> {
                lap.setText(convertTimeToString());
                dataTimeList[index] = convertTimeToString();
                index++;
            });
        }
    }

    public void reset() {
        if (stop) {
            millisec = 0;
            sec = 0;
            min = 0;
            hours = 0;
            Timer t = new Timer();
            lab.setText("h : 0 , min : 0 , sec : 0 , millisec : 0");
        }

    }

    private String convertTimeToString() {

        if (millisec > 999) {
            this.millisec = 0;
            sec++;
        }

        if (sec == 60) {
            this.sec = 0;
            min++;
        }

        if (min == 60) {
            this.min = 0;
            hours++;
        }

        return "h : " + hours + ", min : " + min + ", sec : " + sec + ", millisec : " + millisec;

    }

    public static void main(String[] args) {
        Test t = new Test();

        t.setVisible(true);

        t.test();
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, "example-runner");
        t.setDaemon(true);
        return t;
    }

    public void test() {
        List<String> list = getLapAsList();
        System.out.println(list);
    }

    public List<String> getLapAsList() {
        return Arrays.asList(dataTimeList);
    }
}
