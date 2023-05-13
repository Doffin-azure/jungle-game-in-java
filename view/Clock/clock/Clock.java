package view.Clock.clock;


// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//



import view.Clock.shadow.ShadowRenderer;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

public class Clock extends JComponent {
    private BufferedImage background;
    private ModelTime time = new ModelTime();
    private Thread thread;
    private boolean start = true;

    public Clock() {

        this.setPreferredSize(new Dimension(200,200));
        this.setBackground(Color.WHITE);
        this.initTime();
        this.init();
    }

    private void init() {
        this.thread = new Thread(new Runnable() {
            public void run() {
                while(Clock.this.start) {
                    Clock.this.initTime();
                    Clock.this.repaint();
                    Clock.this.sleep();
                }

            }
        });
        this.thread.start();
    }

    private void initTime() {
        SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");
        String t = df.format(new Date());
        this.time.setHour(Integer.valueOf(t.split(":")[0]));
        this.time.setMinute(Integer.valueOf(t.split(":")[1]));
        this.time.setSeconds(Integer.valueOf(t.split(":")[2]));
    }

    private void sleep() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException var2) {
            System.err.println(var2);
        }

    }

    public void paint(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D)grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int width = this.getWidth();
        int height = this.getHeight();
        int size = Math.min(width, height);
        int x = (width - size) / 2;
        int y = (height - size) / 2;
        int centerX = width / 2;
        int centerY = height / 2;
        g2.drawImage(this.background, x, y, (ImageObserver)null);
        this.drawMinute(g2, centerX, centerY, size / 2);
        this.drawHour(g2, centerX, centerY, size / 2 - 14);
        this.drawSeconds(g2, centerX, centerY, size / 2);
        int centerSize = 7;
        g2.setColor(new Color(50, 50, 50));
        g2.fillOval(centerX - centerSize / 2, centerY - centerSize / 2, centerSize, centerSize);
        g2.dispose();
    }

    private void createBackground() {
        int width = this.getWidth();
        int height = this.getHeight();
        int size = Math.min(width, height);
        int shadowSize = 10;
        this.background = new BufferedImage(size, size, 2);
        Graphics2D g2 = this.background.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawImage(this.createCircle(size, shadowSize), 0, 0, (ImageObserver)null);

        BufferedImage backgroundImage = null;
        try {
            backgroundImage = ImageIO.read(new File("resource\\BackgroundPicture\\Sustech.jfif"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        g2.drawImage(backgroundImage, 0, 0, size, size, null);


        this.drawPoint(g2, size, size);
        g2.dispose();
    }

    private void drawPoint(Graphics2D g2, int width, int height) {
        int centerX = width / 2;
        int centerY = height / 2;
        float angle = 90.0F;
        int space = 30;

        for(int i = 1; i <= 12; ++i) {
            int r = width / 2;
            byte s;
            if (i % 3 == 0) {
                g2.setColor(new Color(50, 50, 50));
                g2.setStroke(new BasicStroke(2.0F));
                s = 25;
            } else {
                g2.setColor(new Color(150, 150, 150));
                g2.setStroke(new BasicStroke(1.0F));
                s = 20;
            }

            Point locationStart = this.getLocationOf(angle - (float)(space * i), r - s);
            Point locationEnd = this.getLocationOf(angle - (float)(space * i), r - 15);
            g2.drawLine(centerX + locationStart.x, centerY - locationStart.y, centerX + locationEnd.x, centerY - locationEnd.y);
        }

    }

    private void drawBackground(){}
    private void drawSeconds(Graphics2D g2, int centerX, int centerY, int size) {
        float angle = (float)(-6 * this.time.getSeconds() + 90);
        Point point = this.getLocationOf(angle, size - 18);
        Point pointStart = this.getLocationOf(angle + 180.0F, 20);
        g2.setColor(new Color(255, 119, 119));
        g2.drawLine(centerX, centerY, centerX + point.x, centerY - point.y);
        g2.drawLine(centerX, centerY, centerX + pointStart.x, centerY - pointStart.y);
        g2.fillOval(centerX + pointStart.x - 2, centerY - pointStart.y - 2, 5, 5);
    }

    private void drawHour(Graphics2D g, int centerX, int centerY, int size) {
        BufferedImage img = new BufferedImage(this.getWidth(), this.getHeight(), 2);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        float angle = (float)(-30 * this.time.getHour() + 90);
        double t = (double)(30.0F * ((float)(this.time.getMinute() * 100 / 60) / 100.0F));
        angle = (float)((double)angle - t);
        float angleLeft = angle + 90.0F;
        float angleRight = angle - 90.0F;
        float angleCircle = angle + 180.0F;
        Point point = this.getLocationOf(angle, size - 35);
        Point pointLeft = this.getLocationOf(angleLeft, 7);
        Point pointRight = this.getLocationOf(angleRight, 7);
        Point pointCircle = this.getLocationOf(angleCircle, 15);
        g2.setColor(new Color(50, 50, 50));
        Path2D.Float p = new Path2D.Float();
        p.moveTo((float)(centerX + pointLeft.x), (float)(centerY - pointLeft.y));
        p.lineTo((float)(centerX + point.x), (float)(centerY - point.y));
        p.lineTo((float)(centerX + pointRight.x), (float)(centerY - pointRight.y));
        p.curveTo((float)(centerX + pointRight.x), (float)(centerY - pointRight.y), (float)(centerX + pointCircle.x), (float)(centerY - pointCircle.y), (float)(centerX + pointLeft.x), (float)(centerY - pointLeft.y));
        g2.fill(p);
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.setColor(Color.WHITE);
        g2.fillOval(centerX - size / 2, centerY - size / 2, size, size);
        g.drawImage((new ShadowRenderer(5, 0.3F, Color.BLACK)).createShadow(img), -4, -4, (ImageObserver)null);
        g.drawImage(img, 0, 0, (ImageObserver)null);
        g2.dispose();
    }

    private void drawMinute(Graphics2D g, int centerX, int centerY, int size) {
        BufferedImage img = new BufferedImage(this.getWidth(), this.getHeight(), 2);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        float angle = (float)(-6 * this.time.getMinute() + 90);
        float angleLeft = angle + 90.0F;
        float angleRight = angle - 90.0F;
        float angleCircle = angle + 180.0F;
        Point point = this.getLocationOf(angle, size - 35);
        Point pointLeft = this.getLocationOf(angleLeft, 7);
        Point pointRight = this.getLocationOf(angleRight, 7);
        Point pointCircle = this.getLocationOf(angleCircle, 15);
        g2.setColor(new Color(255, 94, 94));
        Path2D.Float p = new Path2D.Float();
        p.moveTo((float)(centerX + pointLeft.x), (float)(centerY - pointLeft.y));
        p.lineTo((float)(centerX + point.x), (float)(centerY - point.y));
        p.lineTo((float)(centerX + pointRight.x), (float)(centerY - pointRight.y));
        p.curveTo((float)(centerX + pointRight.x), (float)(centerY - pointRight.y), (float)(centerX + pointCircle.x), (float)(centerY - pointCircle.y), (float)(centerX + pointLeft.x), (float)(centerY - pointLeft.y));
        g2.fill(p);
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.setColor(Color.WHITE);
        g2.fillOval(centerX - size / 2, centerY - size / 2, size, size);
        g.drawImage((new ShadowRenderer(5, 0.3F, Color.BLACK)).createShadow(img), -4, -4, (ImageObserver)null);
        g.drawImage(img, 0, 0, (ImageObserver)null);
        g2.dispose();
    }

    private BufferedImage createCircle(int size, int shadowSize) {
        BufferedImage img = new BufferedImage(size, size, 2);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(this.getBackground());
        g2.fillOval(shadowSize, shadowSize, size - shadowSize * 2, size - shadowSize * 2);
        g2.drawImage((new ShadowRenderer(shadowSize, 0.3F, new Color(50, 50, 50))).createShadow(img), -shadowSize, -shadowSize, (ImageObserver)null);
        g2.fillOval(shadowSize, shadowSize, size - shadowSize * 2, size - shadowSize * 2);
        g2.dispose();
        return img;
    }

    private Point getLocationOf(float angle, int size) {
        double x = Math.cos(Math.toRadians((double)angle)) * (double)size;
        double y = Math.sin(Math.toRadians((double)angle)) * (double)size;
        return new Point((int)x, (int)y);
    }

    public void setBounds(int i, int i1, int i2, int i3) {
        super.setBounds(i, i1, i2, i3);
        this.createBackground();
    }
}
