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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.JPanel;
import javax.swing.Timer;

class SPoint2D {

    int x;
    int y;

    public SPoint2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public SPoint2D() {
        x = 0;
        y = 0;
    }
}

class SPoint3D {

    int x;
    int y;
    int z;

    public SPoint3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public SPoint3D() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }
}

class SLine {

    public SPoint2D st, end;

    public SLine(SPoint2D st, SPoint2D end) {
        this.st = st;
        this.end = end;
    }

    public SLine(int x1, int y1, int x2, int y2) {
        this.st = new SPoint2D(x1, y1);
        this.end = new SPoint2D(x2, y2);
    }
}

class SRectangle {

    public SPoint2D p;
    int w, h;

    public SRectangle(SPoint2D st, int w, int h) {
        p = new SPoint2D();
        this.p = st;
        this.w = w;
        this.h = h;
    }

    public SRectangle(int x, int y, int w, int h) {
        p = new SPoint2D();
        this.p.x = x;
        this.p.y = y;
        this.w = w;
        this.h = h;
    }
}

class SCircle {

    public SPoint2D o;
    int R;

    public SCircle(int R, SPoint2D o) {
        this.R = R;
        this.o = o;
    }

    SCircle(int x, int y, int R) {
        o = new SPoint2D();
        this.o.x = x;
        this.o.y = y;
        this.R = R;
    }
}

class SGlobular {

    SPoint3D c;
    int r;

    public SGlobular(SPoint3D c, int r) {
        this.c = c;
        this.r = r;
    }

    public SGlobular() {
        SPoint3D c = new SPoint3D();
        int r = 0;
    }
}

class SEllipse {

    SPoint2D c;
    float Rx, Ry;

    public SEllipse(SPoint2D c, float Rx, float Ry) {
        this.c = c;
        this.Rx = Rx;
        this.Ry = Ry;
    }

    public SEllipse(int x, int y, float Rx, float Ry) {
        this.c = new SPoint2D(x, y);
        this.Rx = Rx;
        this.Ry = Ry;
    }

    public SEllipse() {
        this.c = new SPoint2D();
        this.Rx = 0;
        this.Ry = 0;
    }

}

public class Surface extends JPanel implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    //các chế độ vẽ hiện tại của panel
    //mới hoàn thiện vẽ Point
    enum Mode {
        Point,
        Line,
        Circle,
        Rectangle,
        Draw2D,
        Draw3D,
        Line1,
        Line2,
        Line3,
        Line4
    }

    private Mode mode = Mode.Draw2D; //Chế độ mặc định, vẽ lại các grid và hệ tọa độ
    //Các biến lưu trữ giúp xác định hình vẽ hiện tại tạm thời
    private SPoint2D dPoint;
    private SLine dLine;
    private SRectangle dRectangle;
    private SCircle dCircle;

    public SPoint2D Opoint; //Tọa độ gốc Oxy mới
    public static int dpi = 7; // 7 pixel tương đương với 5 pixel khi vẽ
    private int DELAY = 200; // không cần quan tâm
    private Timer timer; //không cần quan tâm

    //Cường code
    private SCircle c1, c2, c3, c4, c5, c6;
    private SEllipse e;
    private SRectangle r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11;
    private SLine l1, l2, l3, l4, l5, l6, l7, l8, l9, l10, l11, l12;
    private SPoint2D p1, p2, p3;

    public Surface() {

        initTimer(); //không cần quan tâm
        setBackground(Color.WHITE);
        initCar();
    }

    SPoint2D carPos = new SPoint2D(90, -60);

    private void initCar() {
        l1 = new SLine(-90, -35, -65, -6);
        l2 = new SLine(-40, -35, -15, -6);
        l3 = new SLine(10, -35, 35, -6);
        l4 = new SLine(60, -35, 85, -6);
        l5 = new SLine(-109, -15, -77, -19);
        l6 = new SLine(-80, -25, -24, -15);
        l7 = new SLine(-16, -9, 14, -30);
        l8 = new SLine(24, -20, 72, -20);
        l9 = new SLine(82, -10, 109, -34);
        l10 = new SLine(-109, 5, -60, 20);
        l11 = new SLine(-60, 20, 29, 5);
        l12 = new SLine(29, 5, 109, 25);

        r3 = new SRectangle(-150, -70, 300, 25); //đường xe chạy
        r8 = new SRectangle(-150, -35, 300, 30); //cánh đồng
        //--------xe 1
        r1 = new SRectangle(carPos.x, carPos.y, 30, 8);//thân xe
        r2 = new SRectangle(carPos.x + 7, carPos.y + 3, 21, 9);//mui xe
        r4 = new SRectangle(carPos.x + 10, carPos.y + 1, 12, 7);//cửa xe
        c6 = new SCircle(carPos.x + 6, carPos.y - 1, 1);//bánh xe 1
        c5 = new SCircle(carPos.x + 6, carPos.y - 1, 2);//bánh xe 1
        c1 = new SCircle(carPos.x + 6, carPos.y - 1, 3);//bánh xe 1
        c2 = new SCircle(carPos.x + 26, carPos.y - 1, 3);//bánh xe 2
        c3 = new SCircle(carPos.x + 26, carPos.y - 1, 2);//bánh xe 2
        c4 = new SCircle(carPos.x + 26, carPos.y - 1, 1);//bánh xe 2
        p1 = new SPoint2D(carPos.x + 18, carPos.y + 5);//tay nắm cửa
        p2 = new SPoint2D(carPos.x + 19, carPos.y + 5);//tay nắm cửa
        p3 = new SPoint2D(carPos.x + 20, carPos.y + 5);//tay nắm cửa
        r5 = new SRectangle(carPos.x + 30, carPos.y + 1, 2, 2);//pô xe
        r6 = new SRectangle(carPos.x + 28, carPos.y + 4, 2, 2);// đèn xe sau
        r7 = new SRectangle(carPos.x, carPos.y + 4, 1, 4);// đèn xe trước
    }

    //không cần quan tâm
    private void initTimer() {

        timer = new Timer(DELAY, this);
        timer.start();
    }

    //không cần quan tâm
    public Timer getTimer() {
        return timer;
    }

    private SPoint2D getCenter() {
        return new SPoint2D(getWidth() / (dpi * 2), getHeight() / (dpi * 2));

    }

    //Biến đổi một điểm từ toa độ bình thường sang tọa độ hệ thống 
    public SPoint2D transPointToSystemCoordinates(SPoint2D p) {
        return new SPoint2D(p.x + Opoint.x, (-p.y + Opoint.y));
    }

    public SPoint2D trans3DPointTo2DPoint(SPoint3D p3d) {
        return new SPoint2D((int) (p3d.y - 0.707106 * p3d.x), (int) (p3d.z - 0.707106 * p3d.x));
    }

    //Vẽ lưới pixel
    private void drawGrid2D(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int w = getWidth();
        int h = getHeight();

        for (int i = 0; i < h + 1; i++) {
//            if (i == Opoint.y || i == Opoint.y + 1) {
//                g2d.setColor(Color.blue);
//            } else {
//                g2d.setColor(new Color(240, 240, 240));
//            }
            g2d.setColor(new Color(240, 240, 240));
            g2d.draw(new Line2D.Double(0, i * Surface.dpi, w * Surface.dpi, i * Surface.dpi));
        }

        for (int i = 0; i < w + 1; i++) {
//            if (i == Opoint.x || i == Opoint.x + 1) {
//                g2d.setColor(Color.blue);
//            } else {
//                g2d.setColor(new Color(240, 240, 240));
//            }
            g2d.setColor(new Color(240, 240, 240));
            g2d.draw(new Line2D.Double(i * Surface.dpi, 0, i * Surface.dpi, h * Surface.dpi));
        }
    }

    private void drawGrid3D(Graphics g) {
        g.setColor(Color.blue);
        drawLine(g, new SLine(-1000, 0, 0, 0), new Color(200, 200, 200));
        drawLine(g, new SLine(0, 0, +1000, 0), Color.blue);
        drawLine(g, new SLine(-1000, -1000, 0, 0), Color.blue);
        drawLine(g, new SLine(0, 0, +1000, +1000), new Color(200, 200, 200));
        drawLine(g, new SLine(0, -1000, 0, 0), new Color(200, 200, 200));
        drawLine(g, new SLine(0, 0, 0, +1000), Color.blue);
    }

    //Vẽ một điểm ảnh
    private void drawPixel(Graphics g, SPoint2D p) {

        p = transPointToSystemCoordinates(p);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.red);
        Rectangle2D.Double d = new Rectangle2D.Double(p.x * Surface.dpi + 1, p.y * Surface.dpi + 1, Surface.dpi - 1, Surface.dpi - 1);
        g2d.fill(d);
    }

    private void drawPixel(Graphics g, SPoint2D p, Color c) {

        p = transPointToSystemCoordinates(p);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(c);
        Rectangle2D.Double d = new Rectangle2D.Double(p.x * Surface.dpi + 1, p.y * Surface.dpi + 1, Surface.dpi - 1, Surface.dpi - 1);
        g2d.fill(d);
    }

    //Hàm này được public để các đối tượng bên ngoài có thể dùng
    public void drawPixel(SPoint2D p) {
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

        drawPixel(g, new SPoint2D(x, y));

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
                drawPixel(g, new SPoint2D(x, y));
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
                drawPixel(g, new SPoint2D(x, y));
            }
        }
    }

    private void drawLine(Graphics g, SLine l, Color c) {
        short Dx = (short) (l.end.x - l.st.x);
        short Dy = (short) (l.end.y - l.st.y);
        short x = (short) l.st.x, y = (short) l.st.y;

        short dx = (short) ((Dx < 0) ? -1 : 1);
        short dy = (short) ((Dy < 0) ? -1 : 1);

        drawPixel(g, new SPoint2D(x, y), c);

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
                drawPixel(g, new SPoint2D(x, y), c);
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
                drawPixel(g, new SPoint2D(x, y), c);
            }
        }
    }

    public void drawLine(SLine l) {
        mode = Mode.Line;
        dLine = l;
        repaint();
    }
    //không cần quan tâm

    private void drawlineStyle1(Graphics g, SLine l) {
        short Dx = (short) (l.end.x - l.st.x);
        short Dy = (short) (l.end.y - l.st.y);
        short x = (short) l.st.x, y = (short) l.st.y;

        short dx = (short) ((Dx < 0) ? -1 : 1);
        short dy = (short) ((Dy < 0) ? -1 : 1);

        drawPixel(g, new SPoint2D(x, y));

        Dx = (short) Math.abs(Dx);
        Dy = (short) Math.abs(Dy);
        int n = 0;
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
                if (n % 5 != 4) {
                    drawPixel(g, new SPoint2D(x, y));
                }
                n++;
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
                if (n % 5 != 4) {
                    drawPixel(g, new SPoint2D(x, y));

                }
                n++;
            }
        }
    }

    public void drawlineStyle1(SPoint2D po, int lenght) {
        mode = Mode.Line1;
        dLine = new SLine(po.x, po.y, po.x + lenght, po.y);
        repaint();
    }

    private void drawlineStyle2(Graphics g, SLine l, Color cl) {
        short Dx = (short) (l.end.x - l.st.x);
        short Dy = (short) (l.end.y - l.st.y);
        short x = (short) l.st.x, y = (short) l.st.y;

        short dx = (short) ((Dx < 0) ? -1 : 1);
        short dy = (short) ((Dy < 0) ? -1 : 1);

        drawPixel(g, new SPoint2D(x, y), cl);

        Dx = (short) Math.abs(Dx);
        Dy = (short) Math.abs(Dy);
        int n = 0;
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
                if (n % 8 != 5 && n % 8 != 7) {
                    drawPixel(g, new SPoint2D(x, y), cl);
                }
                n++;
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
                if (n % 8 != 5 && n % 8 != 7) {
                    drawPixel(g, new SPoint2D(x, y), cl);
                }
                n++;
            }
        }
    }

    public void drawlineStyle2(SPoint2D po, int lenght) {
        mode = Mode.Line2;
        dLine = new SLine(po.x, po.y, po.x + lenght, po.y);
        repaint();
    }

    private void drawlineStyle3(Graphics g, SLine l) {
        short Dx = (short) (l.end.x - l.st.x);
        short Dy = (short) (l.end.y - l.st.y);
        short x = (short) l.st.x, y = (short) l.st.y;

        short dx = (short) ((Dx < 0) ? -1 : 1);
        short dy = (short) ((Dy < 0) ? -1 : 1);

        drawPixel(g, new SPoint2D(x, y));

        Dx = (short) Math.abs(Dx);
        Dy = (short) Math.abs(Dy);
        int n = 0;
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
                if (n % 9 != 4 && n % 9 != 6 && n % 9 != 8) {
                    System.out.println(n);
                    drawPixel(g, new SPoint2D(x, y));
                }
                n++;
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
                if (n % 9 != 4 && n % 9 != 6 && n % 9 != 8) {
                    System.out.println(n);
                    drawPixel(g, new SPoint2D(x, y));
                }
                n++;
            }
        }
        System.out.println("com.ktdh.Surface.drawlineStyle3()");
    }

    public void drawlineStyle3(SPoint2D po, int lenght) {
        mode = Mode.Line3;
        dLine = new SLine(po.x, po.y, po.x + lenght, po.y);
        repaint();
    }

    private void drawlineStyle4(Graphics g, SLine l) {
        short Dx = (short) (l.end.x - l.st.x);
        short Dy = (short) (l.end.y - l.st.y);
        short x = (short) l.st.x, y = (short) l.st.y;

        short dx = (short) ((Dx < 0) ? -1 : 1);
        short dy = (short) ((Dy < 0) ? -1 : 1);

        drawPixel(g, new SPoint2D(x, y));

        Dx = (short) Math.abs(Dx);
        Dy = (short) Math.abs(Dy);
        int n = 0;
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
                drawPixel(g, new SPoint2D(x, y));
                n++;
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
                drawPixel(g, new SPoint2D(x, y));
                n++;
            }
        }
        drawPixel(g, new SPoint2D(x - dx, y + dy));
        drawPixel(g, new SPoint2D(x - dx, y - dy));
    }

    public void drawlineStyle4(SPoint2D po, int lenght) {
        mode = Mode.Line4;
        dLine = new SLine(po.x, po.y, po.x + lenght, po.y);
        repaint();
    }

    private void drawRectangle(Graphics g, SRectangle r) {
        for (int i = 0; i < r.h; i++) {
            drawLine(g, new SLine(r.p.x, r.p.y + i, r.p.x + r.w, r.p.y + i));
        }
    }

    public void drawRectangle(SRectangle r) {
        mode = Mode.Rectangle;
        dRectangle = r;
        repaint();
    }

    public void EightWaySymmetricPlot(Graphics g, int xc, int yc, int x, int y) {
        drawPixel(g, new SPoint2D(x + xc, y + yc));
        drawPixel(g, new SPoint2D(x + xc, -y + yc));
        drawPixel(g, new SPoint2D(-x + xc, -y + yc));
        drawPixel(g, new SPoint2D(-x + xc, y + yc));
        drawPixel(g, new SPoint2D(y + xc, x + yc));
        drawPixel(g, new SPoint2D(y + xc, -x + yc));
        drawPixel(g, new SPoint2D(-y + xc, -x + yc));
        drawPixel(g, new SPoint2D(-y + xc, x + yc));
    }

    private void drawCircle(Graphics g, SCircle c) {
        int x = 0, y = c.R, d = 3 - (2 * c.R);
        //EightWaySymmetricPlot(g, c.o.x, c.o.y, x, y);
        int n = 0;
        while (x <= y) {
            if (d <= 0) {
                d = d + (4 * x) + 6;
            } else {
                d = d + (4 * x) - (4 * y) + 10;
                y = y - 1;
            }
            x = x + 1;
            if (n % 5 != 3 && n % 5 != 43) {
                EightWaySymmetricPlot(g, c.o.x, c.o.y, x, y);
            }
            n++;
        }
    }

    public void drawCircle(SCircle c) {
        mode = Mode.Circle;
        dCircle = c;
        repaint();
    }
    //Xác định chế độ vẽ
    //Xác định hình cần vẽ
    //Tiến hành vẽ

    public void drawCircle3D_z(Graphics g, SPoint3D c, int r) {
        int x = 0, y = r, d = 3 - (2 * r);
        int xc = c.x;
        int yc = c.y;
        int zc = c.z;
        int n = 0;
        drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(x + xc, y + yc, zc)));
        drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(x + xc, -y + yc, zc)));
        drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(-x + xc, -y + yc, zc)));
        drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(-x + xc, y + yc, zc)));
        drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(y + xc, x + yc, zc)));
        drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(y + xc, -x + yc, zc)));
        drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(-y + xc, -x + yc, zc)));
        drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(-y + xc, x + yc, zc)));
        while (x <= y) {
            if (d <= 0) {
                d = d + (4 * x) + 6;
            } else {
                d = d + (4 * x) - (4 * y) + 10;
                y = y - 1;
            }
            x = x + 1;

            drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(x + xc, y + yc, zc)));
            drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(x + xc, -y + yc, zc)));
            drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(-x + xc, -y + yc, zc)));
            drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(-x + xc, y + yc, zc)));
            drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(y + xc, x + yc, zc)));
            drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(y + xc, -x + yc, zc)));
            drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(-y + xc, -x + yc, zc)));
            drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(-y + xc, x + yc, zc)));

            n++;
        }

    }

    public void drawCircle3D_x(Graphics g, SPoint3D c, int r) {
        int x = 0, y = r, d = 3 - (2 * r);
        int xc = c.x;
        int yc = c.y;
        int zc = c.z;
        int n = 0;
        drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(xc, x + yc, y + zc)));
        drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(xc, x + yc, -y + zc)));
        drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(xc, -x + yc, -y + zc)));
        drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(xc, -x + yc, y + zc)));
        drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(xc, y + yc, x + zc)));
        drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(xc, y + yc, -x + zc)));
        drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(xc, -y + yc, -x + zc)));
        drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(xc, -y + yc, x + zc)));
        while (x <= y) {
            if (d <= 0) {
                d = d + (4 * x) + 6;
            } else {
                d = d + (4 * x) - (4 * y) + 10;
                y = y - 1;
            }
            x = x + 1;

            drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(xc, x + yc, y + zc)));
            drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(xc, x + yc, -y + zc)));
            drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(xc, -x + yc, -y + zc)));
            drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(xc, -x + yc, y + zc)));
            drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(xc, y + yc, x + zc)));
            drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(xc, y + yc, -x + zc)));
            drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(xc, -y + yc, -x + zc)));
            drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(xc, -y + yc, x + zc)));

            n++;
        }

    }

    private void drawRectangle(Graphics g, SRectangle r, Color cl) {
        for (int i = 0; i < r.h; i++) {
            drawLine(g, new SLine(r.p.x, r.p.y + i, r.p.x + r.w, r.p.y + i), cl);
        }
    }

    private void drawFillRectangle(Graphics g, SRectangle r, Color cl) {

        drawLine(g, new SLine(r.p.x, r.p.y, r.p.x + r.w, r.p.y), cl);
        drawLine(g, new SLine(r.p.x, r.p.y, r.p.x, r.p.y + r.h), cl);
        drawLine(g, new SLine(r.p.x + r.w, r.p.y, r.p.x + r.w, r.p.y + r.h), cl);
        drawLine(g, new SLine(r.p.x, r.p.y + r.h, r.p.x + r.w, r.p.y + r.h), cl);
    }

    public void EightWaySymmetricPlot(Graphics g, int xc, int yc, int x, int y, Color cl) {

        drawPixel(g, new SPoint2D(x + xc, y + yc), cl);
        drawPixel(g, new SPoint2D(x + xc, -y + yc), cl);
        drawPixel(g, new SPoint2D(-x + xc, -y + yc), cl);
        drawPixel(g, new SPoint2D(-x + xc, y + yc), cl);
        drawPixel(g, new SPoint2D(y + xc, x + yc), cl);
        drawPixel(g, new SPoint2D(y + xc, -x + yc), cl);
        drawPixel(g, new SPoint2D(-y + xc, -x + yc), cl);
        drawPixel(g, new SPoint2D(-y + xc, x + yc), cl);

    }

    private void drawCircle(Graphics g, SCircle c, Color cl) {
        int x = 0, y = c.R, d = 3 - (2 * c.R);
        EightWaySymmetricPlot(g, c.o.x, c.o.y, x, y, cl);
        int n = 0;
        while (x <= y) {
            if (d <= 0) {
                d = d + (4 * x) + 6;
            } else {
                d = d + (4 * x) - (4 * y) + 10;
                y = y - 1;
            }
            x = x + 1;
            EightWaySymmetricPlot(g, c.o.x, c.o.y, x, y, cl);
            n++;
        }
    }

    public void drawEllipse(Graphics g, SEllipse e) {
        int rx = (int) e.Rx, ry = (int) e.Ry, xc = e.c.x, yc = e.c.y;
        int dx, dy, d1, d2, x, y;
        x = 0;
        y = ry;

        // Initial decision parameter of region 1
        d1 = (int) ((ry * ry) - (rx * rx * ry)
                + (0.25 * rx * rx));
        dx = 2 * ry * ry * x;
        dy = 2 * rx * rx * y;

        // For region 1
        while (dx < dy) {

            // Print points based on 4-way symmetry
            drawPixel(g, new SPoint2D(((x + xc)), ((y + yc))));
            drawPixel(g, new SPoint2D(((-x + xc)), ((y + yc))));
            drawPixel(g, new SPoint2D(((x + xc)), ((-y + yc))));
            drawPixel(g, new SPoint2D(((-x + xc)), ((-y + yc))));

            // Checking and updating value of
            // decision parameter based on algorithm
            if (d1 < 0) {
                x++;
                dx = dx + (2 * ry * ry);
                d1 = d1 + dx + (ry * ry);
            } else {
                x++;
                y--;
                dx = dx + (2 * ry * ry);
                dy = dy - (2 * rx * rx);
                d1 = d1 + dx - dy + (ry * ry);
            }
        }

        // Decision parameter of region 2
        d2 = (int) (((ry * ry) * ((x + 0.5f) * (x + 0.5f)))
                + ((rx * rx) * ((y - 1) * (y - 1)))
                - (rx * rx * ry * ry));

        // Plotting points of region 2
        while (y >= 0) {

            // printing points based on 4-way symmetry
            drawPixel(g, new SPoint2D(((x + xc)), ((y + yc))));
            drawPixel(g, new SPoint2D(((-x + xc)), ((y + yc))));
            drawPixel(g, new SPoint2D(((x + xc)), ((-y + yc))));
            drawPixel(g, new SPoint2D(((-x + xc)), ((-y + yc))));

            // Checking and updating parameter
            // value based on algorithm
            if (d2 > 0) {
                y--;
                dy = dy - (2 * rx * rx);
                d2 = d2 + (rx * rx) - dy;
            } else {
                y--;
                x++;
                dx = dx + (2 * ry * ry);
                dy = dy - (2 * rx * rx);
                d2 = d2 + dx - dy + (rx * rx);
            }
        }
    }

    public void drawEllipse(Graphics g, SEllipse e, Color c) {
        int rx = (int) e.Rx, ry = (int) e.Ry, xc = e.c.x, yc = e.c.y;
        int dx, dy, d1, d2, x, y;
        x = 0;
        y = ry;

        // Initial decision parameter of region 1
        d1 = (int) ((ry * ry) - (rx * rx * ry)
                + (0.25 * rx * rx));
        dx = 2 * ry * ry * x;
        dy = 2 * rx * rx * y;

        // For region 1
        while (dx < dy) {

            // Print points based on 4-way symmetry
            drawPixel(g, new SPoint2D(((x + xc)), ((y + yc))), c);
            drawPixel(g, new SPoint2D(((-x + xc)), ((y + yc))), c);
            drawPixel(g, new SPoint2D(((x + xc)), ((-y + yc))), c);
            drawPixel(g, new SPoint2D(((-x + xc)), ((-y + yc))), c);

            // Checking and updating value of
            // decision parameter based on algorithm
            if (d1 < 0) {
                x++;
                dx = dx + (2 * ry * ry);
                d1 = d1 + dx + (ry * ry);
            } else {
                x++;
                y--;
                dx = dx + (2 * ry * ry);
                dy = dy - (2 * rx * rx);
                d1 = d1 + dx - dy + (ry * ry);
            }
        }

        // Decision parameter of region 2
        d2 = (int) (((ry * ry) * ((x + 0.5f) * (x + 0.5f)))
                + ((rx * rx) * ((y - 1) * (y - 1)))
                - (rx * rx * ry * ry));

        // Plotting points of region 2
        while (y >= 0) {

            // printing points based on 4-way symmetry
            drawPixel(g, new SPoint2D(((x + xc)), ((y + yc))), c);
            drawPixel(g, new SPoint2D(((-x + xc)), ((y + yc))), c);
            drawPixel(g, new SPoint2D(((x + xc)), ((-y + yc))), c);
            drawPixel(g, new SPoint2D(((-x + xc)), ((-y + yc))), c);

            // Checking and updating parameter
            // value based on algorithm
            if (d2 > 0) {
                y--;
                dy = dy - (2 * rx * rx);
                d2 = d2 + (rx * rx) - dy;
            } else {
                y--;
                x++;
                dx = dx + (2 * ry * ry);
                dy = dy - (2 * rx * rx);
                d2 = d2 + dx - dy + (rx * rx);
            }
        }
    }

    public void drawCircle3D_y(Graphics g, SPoint3D c, int r) {
        int x = 0, y = r, d = 3 - (2 * r);
        int xc = c.x;
        int yc = c.y;
        int zc = c.z;
        int n = 0;
        drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(x + xc, yc, y + zc)));
        drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(x + xc, yc, -y + zc)));
        drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(-x + xc, yc, -y + zc)));
        drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(-x + xc, yc, y + zc)));
        drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(y + xc, yc, x + zc)));
        drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(y + xc, yc, -x + zc)));
        drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(-y + xc, yc, -x + zc)));
        drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(-y + xc, yc, x + zc)));
        while (x <= y) {
            if (d <= 0) {
                d = d + (4 * x) + 6;
            } else {
                d = d + (4 * x) - (4 * y) + 10;
                y = y - 1;
            }
            x = x + 1;

            drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(x + xc, yc, y + zc)));
            drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(x + xc, yc, -y + zc)));
            drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(-x + xc, yc, -y + zc)));
            drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(-x + xc, yc, y + zc)));
            drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(y + xc, yc, x + zc)));
            drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(y + xc, yc, -x + zc)));
            drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(-y + xc, yc, -x + zc)));
            drawPixel(g, trans3DPointTo2DPoint(new SPoint3D(-y + xc, yc, x + zc)));

            n++;
        }
    }

    private void drawGlobular(Graphics g, SGlobular gl) {
        drawGrid3D(g);
        //drawCircle3D_z(g, gl.c, gl.r);
        drawCircle3D_y(g, gl.c, gl.r);
        drawCircle3D_x(g, gl.c, gl.r);
    }

    public void LineEightWaySymmetricPlot(Graphics g, int xc, int yc, int x, int y, Color c) {
//        drawPixel(g, new SPoint2D(x + xc, y + yc),c);
//        drawPixel(g, new SPoint2D(x + xc, -y + yc),c);
        SLine l1 = new SLine(x + xc, y + yc, x + xc, -y + yc);
        drawLine(g, l1, c);
//        drawPixel(g, new SPoint2D(-x + xc, -y + yc),c);
//        drawPixel(g, new SPoint2D(-x + xc, y + yc),c);
        SLine l2 = new SLine(-x + xc, -y + yc, -x + xc, y + yc);
        drawLine(g, l2, c);
//        drawPixel(g, new SPoint2D(y + xc, x + yc),c);
//        drawPixel(g, new SPoint2D(y + xc, -x + yc),c);
        SLine l3 = new SLine(y + xc, x + yc, y + xc, -x + yc);
        drawLine(g, l3, c);
//        drawPixel(g, new SPoint2D(-y + xc, -x + yc),c);
//        drawPixel(g, new SPoint2D(-y + xc, x + yc),c);
        SLine l4 = new SLine(-y + xc, -x + yc, -y + xc, x + yc);
        drawLine(g, l4, c);
    }

    private void drawFullCircle(Graphics g, SCircle c, Color cl) {
        int x = 0, y = c.R, d = 3 - (2 * c.R);
        EightWaySymmetricPlot(g, c.o.x, c.o.y, x, y);
        int n = 0;
        while (x <= y) {
            if (d <= 0) {
                d = d + (4 * x) + 6;
            } else {
                d = d + (4 * x) - (4 * y) + 10;
                y = y - 1;
            }
            x = x + 1;
            LineEightWaySymmetricPlot(g, c.o.x, c.o.y, x, y, cl);
            n++;
        }
        SLine l5 = new SLine(c.o.x, c.o.y - c.R, c.o.x, c.o.y + c.R);
        drawLine(g, l5, cl);
    }

    SPoint2D sp1 = new SPoint2D(-150, 50); //vi tri mac dinh cua may bay,tang giam gia tri se tang giam do cao & vi tri
    int plane_speed = 3;

    public void drawPlane(Graphics g) {
        if (sp1.x > 150) {
            sp1.x = -150;
        }
        sp1.x += plane_speed;

        SPoint2D sp2 = new SPoint2D();
        SPoint2D sp3 = new SPoint2D();
        SPoint2D sp4 = new SPoint2D();
        sp2.x = sp1.x + 3;
        sp2.y = sp1.y - 1;
        sp3.x = sp1.x + 3;
        sp3.y = sp1.y + 5;
        SRectangle sr = new SRectangle(sp1, 12, 5);
        SLine l1 = new SLine(sp2.x, sp2.y, sp2.x, sp2.y - 3);
        drawLine(g, l1, Color.gray);
        SLine l2 = new SLine(sp2.x + 3, sp2.y, sp2.x, sp2.y - 3);
        drawLine(g, l2, Color.gray);

        SLine l3 = new SLine(sp3.x, sp3.y, sp3.x, sp3.y + 3);
        drawLine(g, l3, Color.gray);
        SLine l4 = new SLine(sp3.x + 3, sp3.y, sp3.x, sp3.y + 3);
        drawLine(g, l4, Color.gray);

        sp4.x = sp1.x + 13;
        sp4.y = sp1.y;
        SLine l5 = new SLine(sp4.x, sp4.y, sp4.x + 2, sp4.y + 2);
        drawLine(g, l5, Color.gray);
        SLine l6 = new SLine(sp4.x, sp4.y + 4, sp4.x + 2, sp4.y + 2);
        drawLine(g, l6, Color.gray);
        drawRectangle(g, sr);
    }

    //ve mat troi quay quanh truc
    SPoint2D spSun = new SPoint2D(70, 60); //vi tri mac dinh cua may bay,tang giam gia tri se tang giam do cao & vi tri
    float goc = (float) Math.PI / 24;
    int dai = 8; //chieu dai tia sang mat troi tinh tu tam

    //cac diem ve tia mac dinh: duong thang,duong ngang
    SPoint2D sl1 = new SPoint2D(spSun.x - dai, spSun.y);
    SPoint2D sl2 = new SPoint2D(spSun.x + dai, spSun.y);
    SPoint2D sl3 = new SPoint2D(spSun.x, spSun.y - dai);
    SPoint2D sl4 = new SPoint2D(spSun.x, spSun.y + dai);

    int dai2 = 9; //chieu dai tia sang mat troi tinh tu tam

    //cac diem ve tia mac dinh: duong cheo
    SPoint2D sl5 = new SPoint2D(spSun.x - dai2, spSun.y + dai2);
    SPoint2D sl6 = new SPoint2D(spSun.x + dai2, spSun.y - dai2);
    SPoint2D sl7 = new SPoint2D(spSun.x - dai2, spSun.y - dai2);
    SPoint2D sl8 = new SPoint2D(spSun.x + dai2, spSun.y + dai2);
    int i = -1;

    public void drawSun(Graphics g) {

        SCircle sun = new SCircle(4, spSun);
        i++;

        //toc do quay cham gap 5 lan so voi binh thuong
        if (i % 5 == 0) {
            goc += (float) Math.PI / 24;
        }
        float sin = (float) Math.sin(goc);
        float cos = (float) Math.cos(goc);
        SPoint2D s1 = new SPoint2D();
        SPoint2D s2 = new SPoint2D();
        s1.x = (int) ((sl1.x - spSun.x) * cos - (sl1.y - spSun.y) * sin + spSun.x);
        s1.y = (int) ((sl1.x - spSun.x) * sin + (sl1.y - spSun.y) * cos + spSun.y);

        s2.x = (int) ((sl2.x - spSun.x) * cos - (sl2.y - spSun.y) * sin + spSun.x);
        s2.y = (int) ((sl2.x - spSun.x) * sin + (sl2.y - spSun.y) * cos + spSun.y);

        SPoint2D s3 = new SPoint2D();
        SPoint2D s4 = new SPoint2D();
        s3.x = (int) ((sl3.x - spSun.x) * cos - (sl3.y - spSun.y) * sin + spSun.x);
        s3.y = (int) ((sl3.x - spSun.x) * sin + (sl3.y - spSun.y) * cos + spSun.y);

        s4.x = (int) ((sl4.x - spSun.x) * cos - (sl4.y - spSun.y) * sin + spSun.x);
        s4.y = (int) ((sl4.x - spSun.x) * sin + (sl4.y - spSun.y) * cos + spSun.y);
        g.setColor(Color.BLACK);
        g.drawString("Dường thẳng trong mặt trời: " + s1.x + " ," + s1.y + " ," + s3.x + " ," + s3.x + " | " + s2.x + " ," + s2.x + " ," + s4.x + " ," + s4.x, posx, posy + 15);

        SLine l1 = new SLine(s1, s2);
        SLine l2 = new SLine(s3, s4);

        SPoint2D s5 = new SPoint2D();
        SPoint2D s6 = new SPoint2D();
        s5.x = (int) ((sl5.x - spSun.x) * cos - (sl5.y - spSun.y) * sin + spSun.x);
        s5.y = (int) ((sl5.x - spSun.x) * sin + (sl5.y - spSun.y) * cos + spSun.y);

        s6.x = (int) ((sl6.x - spSun.x) * cos - (sl6.y - spSun.y) * sin + spSun.x);
        s6.y = (int) ((sl6.x - spSun.x) * sin + (sl6.y - spSun.y) * cos + spSun.y);

        SPoint2D s7 = new SPoint2D();
        SPoint2D s8 = new SPoint2D();
        s7.x = (int) ((sl7.x - spSun.x) * cos - (sl7.y - spSun.y) * sin + spSun.x);
        s7.y = (int) ((sl7.x - spSun.x) * sin + (sl7.y - spSun.y) * cos + spSun.y);

        s8.x = (int) ((sl8.x - spSun.x) * cos - (sl8.y - spSun.y) * sin + spSun.x);
        s8.y = (int) ((sl8.x - spSun.x) * sin + (sl8.y - spSun.y) * cos + spSun.y);

        SLine l3 = new SLine(s5, s6);
        SLine l4 = new SLine(s7, s8);

        drawLine(g, l1, Color.orange);
        drawLine(g, l2, Color.orange);
        drawLine(g, l3, Color.orange);
        drawLine(g, l4, Color.orange);

        drawFullCircle(g, sun, Color.red);
    }

    int distance = 80; // khoảng các đến khi biến mấy
    int speed = 3; // vận tóc rơi
    SPoint2D pos = sp1; // vị trí thả rơi
    int time = 0; //thoi gian giưa cac lan roi
    ArrayList<SEllipse> list = new ArrayList<>();
    float minRx = 1f;
    float minRy = 1.5f;

    public void drawBoom(Graphics g) {
        //Viết code tại đây
        //có thể sử dụng các biến của class để điều khiển vật thể
        //code mẫu vẽ một hình tròn đang đi lên  hình chữ z

        if (time % 13 == 0) {
            list.add(new SEllipse(pos.x, pos.y, minRx, minRy));
        }
        time++;
        ArrayList<SEllipse> rml = new ArrayList<>();

        for (SEllipse e : list) {

            boolean did = false;
            if ((pos.y - e.c.y) > distance) {
                drawPixel(g, new SPoint2D(e.c.x, pos.y - distance), Color.BLACK);
                drawEllipse(g, new SEllipse(new SPoint2D(e.c.x, pos.y - distance), e.Ry, e.Rx), Color.LIGHT_GRAY);
                drawEllipse(g, new SEllipse(new SPoint2D(e.c.x, pos.y - distance), e.Ry / 2, e.Rx / 2), Color.YELLOW);
                drawEllipse(g, new SEllipse(new SPoint2D(e.c.x, pos.y - distance), e.Ry / 4, e.Rx / 4), Color.RED);
                //rml.add(e);
                did = true;
            }
            if ((pos.y - e.c.y) > distance + 30) { //hoãn thời gian biến mất của hố bom
                drawPixel(g, new SPoint2D(e.c.x, pos.y - distance), Color.BLACK);
                drawEllipse(g, new SEllipse(new SPoint2D(e.c.x, pos.y - distance), e.Ry, e.Rx), Color.LIGHT_GRAY);
                drawEllipse(g, new SEllipse(new SPoint2D(e.c.x, pos.y - distance), e.Ry / 2, e.Rx / 2), Color.YELLOW);
                drawEllipse(g, new SEllipse(new SPoint2D(e.c.x, pos.y - distance), e.Ry / 4, e.Rx / 4), Color.RED);
                rml.add(e);

                // did = true;
            } else {

                float minac = 3;
                if ((pos.y - e.c.y) <= distance - 3) {
                    if (e.Rx > minac * minRx) {
                        drawEllipse(g, new SEllipse(e.c, minac * minRx, minac * minRy), Color.GRAY);
                        drawLine(g, new SLine(e.c.x, e.c.y - (int) (minac * minRy) / 2, e.c.x, e.c.y + (int) (minac * minRy) / 2), Color.RED);
                        drawLine(g, new SLine(e.c.x - (int) (minac * minRx) / 2, e.c.y, e.c.x + (int) (minac * minRx) / 2, e.c.y), Color.RED);
                    } else {
                        drawEllipse(g, e, Color.GRAY);
                        drawLine(g, new SLine(e.c.x, e.c.y - (int) e.Ry / 2, e.c.x, e.c.y + (int) e.Ry / 2), Color.RED);
                        drawLine(g, new SLine(e.c.x - (int) e.Rx / 2, e.c.y, e.c.x + (int) e.Rx / 2, e.c.y), Color.RED);
                    }
                }

                float zoom = 1.075f;
                e.Rx *= zoom;
                e.Ry *= zoom;

                e.c.y -= speed;

            }
        }

        for (SEllipse e : rml) {
            list.remove(e);
        }
    }
    SRectangle hedge1 = new SRectangle(-150, -45, 300, 5);
    SRectangle hedge2 = new SRectangle(-150, -40, 300, 5);
    SLine separator = new SLine(-150, -57, 150, -57);
    int carSpeed = 3;

    public void drawCarAndTextTure(Graphics g) {
        //Viết code tại đây
        //có thể sử dụng các biến của class để điều khiển vật thể
        // code mẫu vẽ một hình tròn đang đi lên  hình chữ z

        // đi qua hết màn bên trái sẽ vẽ lại bên phải màn hình
        if (r1.p.x < -90 - 50) {
            r1.p.x = 100;
        }

        if (r2.p.x < -83 - 50) {
            r2.p.x = 107;
        }
        if (r4.p.x < -80 - 50) {
            r4.p.x = 110;
        }

        if (r5.p.x < -60 - 50) {
            r5.p.x = 130;
        }
        if (r6.p.x < -62 - 50) {
            r6.p.x = 128;
        }

        if (r7.p.x < -90 - 50) {
            r7.p.x = 100;
        }

        if (c1.o.x < -84 - 50) {
            c1.o.x = 106;
        }
        if (c5.o.x < -84 - 50) {
            c5.o.x = 106;
        }
        if (c6.o.x < -84 - 50) {
            c6.o.x = 106;
        }

        if (c2.o.x < -64 - 50) {
            c2.o.x = 126;
        }
        if (c3.o.x < -64 - 50) {
            c3.o.x = 126;
        }
        if (c4.o.x < -64 - 50) {
            c4.o.x = 126;
        }

        if (p1.x < -70 - 50) {
            p1.x = 120;
        }
        if (p2.x < -70 - 50) {
            p2.x = 120;
        }
        if (p3.x < -70 - 50) {
            p3.x = 120;
        }

        //các bộ phận xe tịnh tiến từ phải qua trái
        r1.p.x -= carSpeed;
        r2.p.x -= carSpeed;
        r4.p.x -= carSpeed;
        r5.p.x -= carSpeed;
        r6.p.x -= carSpeed;
        r7.p.x -= carSpeed;

        c1.o.x -= carSpeed;
        c5.o.x -= carSpeed;
        c6.o.x -= carSpeed;

        c2.o.x -= carSpeed;
        c3.o.x -= carSpeed;
        c4.o.x -= carSpeed;

        p1.x -= carSpeed;
        p2.x -= carSpeed;
        p3.x -= carSpeed;

        //vẽ đường xá
        drawRectangle(g, r3, new Color(214, 201, 202));
        drawlineStyle2(g, separator, Color.WHITE);
// --------xe 1
        //vẽ thân xe
        drawRectangle(g, r1, Color.GREEN);
        //vẽ mui xe
        drawFillRectangle(g, r2, Color.GREEN);
        //vẽ cửa xe
        drawRectangle(g, r4, Color.BLACK);
        //vẽ bánh xe đầu tiên
        drawCircle(g, c1, Color.BLACK);
        drawCircle(g, c5, Color.WHITE);
        drawCircle(g, c6, Color.WHITE);
        //vẽ bánh xe thứ hai
        drawCircle(g, c2, Color.BLACK);
        drawCircle(g, c3, Color.WHITE);
        drawCircle(g, c4, Color.WHITE);
        //tay nắm cửa
        drawPixel(g, p1, Color.ORANGE);
        drawPixel(g, p2, Color.ORANGE);
        drawPixel(g, p3, Color.ORANGE);
        //vẽ pô xe
        drawRectangle(g, r5, Color.BLACK);
        //vẽ đèn xe
        drawRectangle(g, r6, Color.red);
        drawRectangle(g, r7, Color.yellow);
        //vẽ đồng ruộng
        drawRectangle(g, r8, new Color(206, 245, 66));
        drawRectangle(g, hedge1, new Color(100, 198, 43));
        drawRectangle(g, hedge2, Color.GREEN);
        //các đường vân của đồng lúa
        drawLine(g, l1, Color.GREEN);
        drawLine(g, l2, Color.GREEN);
        drawLine(g, l3, Color.GREEN);
        drawLine(g, l4, Color.GREEN);
        drawLine(g, l5, Color.GREEN);
        drawLine(g, l6, Color.GREEN);
        drawLine(g, l7, Color.GREEN);
        drawLine(g, l8, Color.GREEN);
        drawLine(g, l9, Color.GREEN);
        drawLine(g, l10, new Color(0, 117, 10));
        drawLine(g, l11, new Color(0, 117, 10));
        drawLine(g, l12, new Color(0, 117, 10));
    }

    SPoint2D center = new SPoint2D(80, 10);
    SPoint2D pwm1 = new SPoint2D(center.x + 15, center.y);
    SPoint2D pwm2 = new SPoint2D(center.x + 15, center.y + 7);
    SPoint2D fwm1 = new SPoint2D(center.x + 5, center.y - 20);
    SPoint2D fwm2 = new SPoint2D(center.x - 5, center.y - 20);

    public void drawWindmill(Graphics g) {
        drawLine(g, new SLine(fwm1, fwm2), new Color(165, 136, 54));
        drawLine(g, new SLine(center, fwm2), new Color(165, 136, 54));
        drawLine(g, new SLine(center, fwm1), new Color(165, 136, 54));

        SPoint2D t1 = new SPoint2D();
        SPoint2D t2 = new SPoint2D();
        float sin = (float) Math.sin(goc);
        float cos = (float) Math.cos(goc);
        t1.x = (int) ((pwm1.x - center.x) * cos - (pwm1.y - center.y) * sin + center.x);
        t1.y = (int) ((pwm1.x - center.x) * sin + (pwm1.y - center.y) * cos + center.y);

        t2.x = (int) ((pwm2.x - center.x) * cos - (pwm2.y - center.y) * sin + center.x);
        t2.y = (int) ((pwm2.x - center.x) * sin + (pwm2.y - center.y) * cos + center.y);

        drawLine(g, new SLine(center, t1), new Color(214, 202, 160));
        drawLine(g, new SLine(center, t2), new Color(214, 202, 160));
        drawLine(g, new SLine(t1, t2), new Color(214, 202, 160));

        SPoint2D cpw1 = new SPoint2D(2 * center.x - t1.x, 2 * center.y - t1.y);
        SPoint2D cpw2 = new SPoint2D(2 * center.x - t2.x, 2 * center.y - t2.y);

        drawLine(g, new SLine(center, cpw1), new Color(214, 202, 160));
        drawLine(g, new SLine(center, cpw2), new Color(214, 202, 160));
        drawLine(g, new SLine(cpw1, cpw2), new Color(214, 202, 160));

        drawPixel(g, center, Color.BLACK);
    }

    private void runAnimation2D(Graphics g) {

        drawSun(g);
        drawPlane(g);
        drawCarAndTextTure(g);
        drawWindmill(g);
        drawBoom(g);

    }

    private void draw(Graphics g) {
        switch (mode) {
            case Circle -> {//chưa xong
                drawCircle(g, dCircle);
            }
            case Line -> { //chưa xong
                drawLine(g, dLine);
            }
            case Point -> {
                drawPixel(g, dPoint);
            }
            case Rectangle -> {//chưa xong
                drawRectangle(g, dRectangle);
            }
            case Draw2D -> {
                drawGrid2D(g);
                runAnimation2D(g);
            }
            case Line1 -> {
                drawlineStyle1(g, dLine);
            }
            case Line2 -> {
                //drawlineStyle2(g, dLine);
            }
            case Line3 -> {
                drawlineStyle3(g, dLine);
            }
            case Line4 -> {
                drawlineStyle4(g, dLine);
            }
            case Draw3D -> {
                drawGrid3D(g);
            }
        }

    }

    int posx = 10;
    int posy = 15;

    private void drawStatus(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawString("Mặt Trời (Hình tròn): " + spSun.x + " ," + spSun.y, posx, posy);

        SPoint2D t1 = new SPoint2D();
        SPoint2D t2 = new SPoint2D();
        float sin = (float) Math.sin(goc);
        float cos = (float) Math.cos(goc);
        t1.x = (int) ((pwm1.x - center.x) * cos - (pwm1.y - center.y) * sin + center.x);
        t1.y = (int) ((pwm1.x - center.x) * sin + (pwm1.y - center.y) * cos + center.y);

        t2.x = (int) ((pwm2.x - center.x) * cos - (pwm2.y - center.y) * sin + center.x);
        t2.y = (int) ((pwm2.x - center.x) * sin + (pwm2.y - center.y) * cos + center.y);
        g.drawString("Đoạn thẳng viền cánh cối xoay gió (Đoạn thẳng): " + t1.x + ", " + t1.y + ", " + t2.x + ", " + t2.y, posx, posy + 30);
        g.drawString("Tâm cối xay gió (Pixel): " + center.x + ", " + center.y, posx, posy + 45);
        String elip = "";
        for (SEllipse e : list) {
            elip += e.c.x + ", " + e.c.y + " | ";
        }
        g.drawString("Tâm các quả boom (Elip): " + elip, posx, posy + 60);
        elip = "";
        for (SEllipse e : list) {
            elip += (e.c.x) + ", " + (int) (e.c.y + e.Rx / 2) + ", " + (e.c.x) + ", " + (int) (e.c.y - e.Rx / 2) + " | ";
        }
        g.drawString("Đoạn thẳng dài trong các quả boom (Đoạn thẳng): " + elip, posx, posy + 75);
    }

    //mỗi khi cần vẽ thì hệ thống sẽ gọi hàm này
    //Có thể chủ động gọi hàm này thông qua hàm repaint()
    //Đọc https://zetcode.com/gfx/java2d/basicdrawing/ để hiểu rõ
    @Override
    public void paintComponent(Graphics g) {
        Opoint = getCenter();
        super.paintComponent(g);
        draw(g);
        drawStatus(g);

    }

}
