package com.zetcode;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;
import javax.swing.*;
import javax.swing.JPanel;
import javax.swing.Timer;

class SPoint {

    int x;
    int y;

    public SPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public SPoint() {
        x = 0;
        y = 0;
    }
}

class SLine {

    public SPoint st, end;

    public SLine(SPoint st, SPoint end) {
        this.st = st;
        this.end = end;
    }
}

class SRectangle {

    public SPoint st, end;

    public SRectangle(SPoint st, SPoint end) {
        this.st = st;
        this.end = end;
    }
}

class SCircle {

    public SPoint c;
    int R;

    public SCircle(int R, SPoint c) {
        this.R = R;
        this.c = c;
    }
}

public class Surface extends JPanel {
    //các chế độ vẽ hiện tại của panel
    //mới hoàn thiện vẽ Point
    enum Mode { 
        Point,
        Line,
        Circle,
        Rectangle,
        Configure
    }

    private Mode mode = Mode.Configure; //Chế độ mặc định, vẽ lại các grid và hệ tọa độ
    
    //Các biến lưu trữ giúp xác định hình vẽ hiện tại tạm thời
    private SPoint dPoint;
    private SLine dLine;
    private SRectangle dRectangle;
    private SCircle dCircle;
    
    public SPoint Opoint; //Tọa độ gốc Oxy mới
    public static int dpi = 7; // 7 pixel tương đương với 5 pixel khi vẽ
    private int DELAY = 10; // không cần quan tâm
    private Timer timer; //không cần quan tâm

    public Surface() {
        initTimer(); //không cần quan tâm
    }
    
    //không cần quan tâm
    private void initTimer() {
//
//        timer = new Timer(DELAY, this);
//        timer.start();
    }
    
    //không cần quan tâm
    public Timer getTimer() {
        return timer;
    }

    private SPoint getCenter() {
        return new SPoint(getWidth() / (dpi * 2), getHeight() / (dpi * 2));

    }

    //Biến đổi một điểm từ toa độ bình thường sang tọa độ hệ thống 
    public SPoint transPoint(SPoint p) {
        return new SPoint(p.x + Opoint.x, (-p.y + Opoint.y ));
    }
    
    //Vẽ lưới pixel
    private void drawGrid(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int w = getWidth();
        int h = getHeight();

        for (int i = 0; i < h + 1; i++) {
            if (i == Opoint.y || i == Opoint.y + 1) {
                g2d.setColor(Color.blue);
            } else {
                g2d.setColor(new Color(230, 230, 230));
            }
            g2d.draw(new Line2D.Double(0, i * Surface.dpi, w * Surface.dpi, i * Surface.dpi));
        }

        for (int i = 0; i < w + 1; i++) {
            if (i == Opoint.x || i == Opoint.x + 1) {
                g2d.setColor(Color.blue);
            } else {
                g2d.setColor(new Color(230, 230, 230));
            }
            g2d.draw(new Line2D.Double(i * Surface.dpi, 0, i * Surface.dpi, h * Surface.dpi));
        }
    }

    //Vẽ một điểm ảnh
    private void drawPixel(Graphics g, SPoint p) {

        p = transPoint(p);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.red);
        Rectangle2D.Double d = new Rectangle2D.Double(p.x * Surface.dpi + 1, p.y * Surface.dpi + 1, Surface.dpi - 1, Surface.dpi - 1);
        g2d.fill(d);
    }

    //Hàm này được public để các đối tượng bên ngoài có thể dùng
    public void drawPixel(SPoint p) {
        mode = Mode.Point;
        dPoint = p;

        repaint();
    }

    //không cần quan tâm
    private void drawOxy() {

    }
    
    //không cần quan tâm
    private void drawBlink(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setPaint(Color.blue);

        int w = getWidth();
        int h = getHeight();

        Random r = new Random();

        for (int i = 0; i < 2000; i++) {

            int x = Math.abs(r.nextInt()) % w;
            int y = Math.abs(r.nextInt()) % h;
            g2d.drawLine(x, y, x, y);
        }
    }

    //Xác định chế độ vẽ
    //Xác định hình cần vẽ
    //Tiến hành vẽ
    private void draw(Graphics g) {
        switch (mode) {
            case Circle: {//chưa xong
                drawPixel(g, dPoint);
                break;
            }
            case Line: { //chưa xong
                drawPixel(g, dPoint);
                break;
            }
            case Point: {
                drawPixel(g, dPoint);
                break;
            }
            case Rectangle: {//chưa xong
                drawPixel(g, dPoint);
                break;
            }
            case Configure: {
                drawGrid(g);
                break;
            }
        }

    }

    
    //mỗi khi cần vẽ thì hệ thống sẽ gọi hàm này
    //Có thể chủ động gọi hàm này thông qua hàm repaint()
    //Đọc https://zetcode.com/gfx/java2d/basicdrawing/ để hiểu rõ
    @Override
    public void paintComponent(Graphics g) {
        Opoint = getCenter();
        super.paintComponent(g);
        //drawBlink(g);
        drawGrid(g);
//      drawPixel(g, new SPoint(1, 5));

        draw(g);
    }

}

//public class PointsEx extends JFrame {
//
//    public PointsEx() {
//
//        initUI();
//    }
//
//    private void initUI() {
//
//        final Surface surface = new Surface();
//        add(surface);
//
//        addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e) {
//                Timer timer = surface.getTimer();
//                timer.stop();
//            }
//        });
//
//        setTitle("Points");
//        setSize(600, 750);
//        setLocationRelativeTo(null);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    }
//
////    public static void main(String[] args) {
////
////        PointsEx ex = new PointsEx();
////        ex.setVisible(true);
////
////    }
//}
