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

public class Surface extends JPanel  implements ActionListener{

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

    private Mode mode = Mode.Draw3D; //Chế độ mặc định, vẽ lại các grid và hệ tọa độ

    //Các biến lưu trữ giúp xác định hình vẽ hiện tại tạm thời
    private SPoint2D dPoint;
    private SLine dLine;
    private SRectangle dRectangle;
    private SCircle dCircle;

    public SPoint2D Opoint; //Tọa độ gốc Oxy mới
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

    private SPoint2D getCenter() {
        return new SPoint2D(getWidth() / (dpi * 2), getHeight() / (dpi * 2));

    }

    //Biến đổi một điểm từ toa độ bình thường sang tọa độ hệ thống 
    public SPoint2D transPointToSystemCoordinates(SPoint2D p) {
        return new SPoint2D(p.x + Opoint.x, (-p.y + Opoint.y));
    }

    public SPoint2D trans3DPointTo2DPoint(SPoint3D p3d) {
        return new SPoint2D((int) (p3d.z - 0.707106 * p3d.x), (int) (p3d.y - 0.707106 * p3d.x));
    }

    //Vẽ lưới pixel
    private void drawGrid2D(Graphics g) {
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

    private void drawGrid3D(Graphics g) {
        g.setColor(Color.blue);
        drawLine(g, new SLine(-1000, 0, +1000, 0), Color.blue);
        drawLine(g, new SLine(-1000, -1000, +1000, +1000), Color.blue);
        drawLine(g, new SLine(0, -1000, 0, +1000), Color.blue);
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

    private void drawlineStyle2(Graphics g, SLine l) {
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
                if (n % 8 != 5 && n % 8 != 7) {
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
                if (n % 8 != 5 && n % 8 != 7) {
                    drawPixel(g, new SPoint2D(x, y));
                }
                n++;
            }
        }
        System.out.println("com.ktdh.Surface.drawlineStyle2()");
        System.out.println(mode.toString());
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
        drawPixel(g, trans3DPointTo2DPoint(new SPoint3D( y + xc, x + yc, zc)));
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
            }
            case Line1 -> {
                drawlineStyle1(g, dLine);
            }
            case Line2 -> {
                drawlineStyle2(g, dLine);
            }
            case Line3 -> {
                drawlineStyle3(g, dLine);
            }
            case Line4 -> {
                drawlineStyle4(g, dLine);
            }
            case Draw3D -> {

                drawGrid3D(g);
                drawCircle3D_z(g, new SPoint3D(20, 10, 6), 50);
                drawCircle3D_y(g, new SPoint3D(20, 10, 6), 50);
                //drawCircle3D_x(g, new SPoint3D(20, 10, 6), 50);
            }
        }
        repaint();
    }

    //mỗi khi cần vẽ thì hệ thống sẽ gọi hàm này
    //Có thể chủ động gọi hàm này thông qua hàm repaint()
    //Đọc https://zetcode.com/gfx/java2d/basicdrawing/ để hiểu rõ
    @Override
    public void paintComponent(Graphics g) {
        Opoint = getCenter();
        super.paintComponent(g);
        drawGrid3D(g);
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
