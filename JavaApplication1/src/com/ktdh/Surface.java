package com.ktdh;

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

    public SLine(int x1, int y1, int x2, int y2) {
        this.st = new SPoint(x1, y1);
        this.end = new SPoint(x2, y2);
    }
}

class SRectangle {

    public SPoint p;
    int w,h;

    public SRectangle(SPoint st, int w, int h) {
        this.p = st;
        this.w = w;
        this.h = h;
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
        Configure,
        Line1,
        Line2,
        Line3,
        Line4
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
        return new SPoint(p.x + Opoint.x, (-p.y + Opoint.y));
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

    private void drawLine(Graphics g, SLine l) {
        short Dx = (short) (l.end.x - l.st.x);
        short Dy = (short) (l.end.y - l.st.y);
        short x = (short) l.st.x, y = (short) l.st.y;

        short dx = (short) ((Dx < 0) ? -1 : 1);
        short dy = (short) ((Dy < 0) ? -1 : 1);

        drawPixel(g,new SPoint(x, y));

        Dx = (short) Math.abs(Dx);
        Dy = (short) Math.abs(Dy);

        if (Dx > Dy) {
            short p = (short) ((Dy << 1) - Dx);
            short Const1 = (short) (Dy << 1), Const2 = (short) ((Dy - Dx) << 1);
            while (x != l.end.x) {
                if (p < 0) {
                    p += Const1;
                } else {
                    p += Const2;
                    y += dy;
                }
                x += dx;
                drawPixel(g,new SPoint(x, y));
            }
        } else {
            short p = (short) ((Dx << 1) - Dy);
            short Const1 = (short) (Dx << 1), Const2 = (short) ((Dx - Dy) << 1);
            while (y != l.end.y) {
                if (p < 0) {
                    p += Const1;
                } else {
                    p += Const2;
                    x += dx;
                }
                y += dy;
                drawPixel(g,new SPoint(x, y));
            }
        }
    }

    public void drawLine(SLine l) {
        mode = Mode.Line;
        dLine = l;
        repaint();
    }
    //không cần quan tâm

    private void drawLine1(Graphics g, SLine l) {
        
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
                drawLine(g, dLine);
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
            case Line1: {
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
        drawGrid(g);
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
