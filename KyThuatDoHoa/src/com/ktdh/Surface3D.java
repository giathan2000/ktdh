/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ktdh;

import static com.ktdh.Surface.dpi;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;

/**
 *
 * @author minh
 */
class SRectangular {

    SPoint3D c;
    int l, w, h;

    public SRectangular() {
        this.c = new SPoint3D(0, 0, 0);
        this.l = 0;
        this.w = 0;
        this.h = 0;
    }

    public SRectangular(SPoint3D c, int w, int l, int h) {
        this.c = c;
        this.l = l;
        this.w = w;
        this.h = h;
    }
}

public class Surface3D extends JPanel {

    enum Mode {
        Globular,
        Rectangular
    }

    public Surface3D() {
        setBackground(Color.WHITE);
    }

    private Mode mode = Mode.Rectangular;
    public SPoint2D Opoint; //Tọa độ gốc Oxy mới
    SGlobular globular;
    SRectangular rectangular = new SRectangular(new SPoint3D(0, 0, 0), 10, 15, 20);

    public SPoint2D trans3DPointTo2DPoint(SPoint3D p3d) {
        return new SPoint2D((p3d.x - (int) (0.707106 * p3d.y)), (p3d.z - (int) (0.707106 * p3d.y)));
    }

    private SPoint2D getCenter() {
        return new SPoint2D(getWidth() / (dpi * 2), getHeight() / (dpi * 2));

    }

    private void drawGlobular(Graphics g, SGlobular gl) {
        drawGrid3D(g);
        drawCircle3D_z(g, gl.c, gl.r);
        drawCircle3D_y(g, gl.c, gl.r);
        g.drawString("Tâm hình cầu : " + gl.c.x + ", " + gl.c.y + ", " + gl.c.z , 20, 30);
        //drawCircle3D_x(g, gl.c, gl.r);
    }

    public void drawGlobular(SGlobular gl) {
        this.globular = gl;
        mode = Mode.Globular;
        repaint();
    }

    public void drawRectangular(SRectangular re) {
        this.rectangular = re;
        mode = Mode.Rectangular;
        repaint();
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

    public SPoint2D transPointToSystemCoordinates(SPoint2D p) {
        return new SPoint2D(p.x + Opoint.x, (-p.y + Opoint.y));
    }

    private void drawlineStyle1(Graphics g, SLine l, Color cl) {
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
                if (n % 4 != 0) {
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
                if (n % 4 != 0) {
                    drawPixel(g, new SPoint2D(x, y), cl);

                }
                n++;
            }
        }
    }

    private void drawRectangular(Graphics g, SRectangular re) {
        SPoint2D[] temp = new SPoint2D[8];
        SPoint3D p = new SPoint3D(rectangular.c.x + rectangular.w, rectangular.c.y, rectangular.c.z);
        SPoint2D p2d1 = trans3DPointTo2DPoint(rectangular.c);
        SPoint2D p2d2 = trans3DPointTo2DPoint(p);
        drawlineStyle1(g, new SLine(p2d1, p2d2), Color.red);
        temp[0] = transPointToSystemCoordinates(p2d1);
        g.drawString("A : " + rectangular.c.x+", "+rectangular.c.y+", "+rectangular.c.z, 10 , 10);
        temp[1] = transPointToSystemCoordinates(p2d2);
        g.drawString("B : " + p.x+", "+p.y+", "+p.z, 10 , 10+15);

        p = new SPoint3D(rectangular.c.x, rectangular.c.y + rectangular.l, rectangular.c.z);
        p2d1 = trans3DPointTo2DPoint(rectangular.c);
        p2d2 = trans3DPointTo2DPoint(p);
        drawlineStyle1(g, new SLine(p2d1, p2d2), Color.red);
        g.drawString("D : " + p.x+", "+p.y+", "+p.z, 10 , 40);
        temp[2] = transPointToSystemCoordinates(p2d2);

        p = new SPoint3D(rectangular.c.x, rectangular.c.y, rectangular.c.z + rectangular.h);
        p2d1 = trans3DPointTo2DPoint(rectangular.c);
        p2d2 = trans3DPointTo2DPoint(p);
        drawlineStyle1(g, new SLine(p2d1, p2d2), Color.red);
        g.drawString("E : " + p.x+", "+p.y+", "+p.z, 10 , 70);
        temp[3] = transPointToSystemCoordinates(p2d2);
//-------------------------------------------------------------------
        p = new SPoint3D(rectangular.c.x + rectangular.w, rectangular.c.y, rectangular.c.z + rectangular.h);
        p2d1 = trans3DPointTo2DPoint(new SPoint3D(rectangular.c.x, rectangular.c.y, rectangular.c.z + rectangular.h));
        p2d2 = trans3DPointTo2DPoint(p);
        drawLine(g, new SLine(p2d1, p2d2), Color.red);
        g.drawString("F : " + p.x+", "+p.y+", "+p.z, 10 , 85);
        temp[4] = transPointToSystemCoordinates(p2d2);

        p = new SPoint3D(rectangular.c.x, rectangular.c.y + rectangular.l, rectangular.c.z + rectangular.h);
        p2d1 = trans3DPointTo2DPoint(new SPoint3D(rectangular.c.x, rectangular.c.y, rectangular.c.z + rectangular.h));
        p2d2 = trans3DPointTo2DPoint(p);
        drawLine(g, new SLine(p2d1, p2d2), Color.red);
        g.drawString("H : " + p.x+", "+p.y+", "+p.z, 10 , 115);
        temp[5] = transPointToSystemCoordinates(p2d2);
//---------------------------------------------------------------------
        p = new SPoint3D(rectangular.c.x + rectangular.w, rectangular.c.y, rectangular.c.z + rectangular.h);
        p2d1 = trans3DPointTo2DPoint(new SPoint3D(rectangular.c.x + rectangular.w, rectangular.c.y, rectangular.c.z));
        p2d2 = trans3DPointTo2DPoint(p);
        drawLine(g, new SLine(p2d1, p2d2), Color.red);

        p = new SPoint3D(rectangular.c.x + rectangular.w, rectangular.c.y + rectangular.l, rectangular.c.z);
        p2d1 = trans3DPointTo2DPoint(new SPoint3D(rectangular.c.x + rectangular.w, rectangular.c.y, rectangular.c.z));
        p2d2 = trans3DPointTo2DPoint(p);
        drawLine(g, new SLine(p2d1, p2d2), Color.red);

//-----------------------------------------------------------------------
        p = new SPoint3D(rectangular.c.x + rectangular.w, rectangular.c.y + rectangular.l, rectangular.c.z);
        p2d1 = trans3DPointTo2DPoint(new SPoint3D(rectangular.c.x, rectangular.c.y + rectangular.l, rectangular.c.z));
        p2d2 = trans3DPointTo2DPoint(p);
        drawLine(g, new SLine(p2d1, p2d2), Color.red);
        temp[6] = transPointToSystemCoordinates(p2d2);
        g.drawString("C : " + p.x+", "+p.y+", "+p.z, 10 , 55);

        p = new SPoint3D(rectangular.c.x, rectangular.c.y + rectangular.l, rectangular.c.z + rectangular.h);
        p2d1 = trans3DPointTo2DPoint(new SPoint3D(rectangular.c.x, rectangular.c.y + rectangular.l, rectangular.c.z));
        p2d2 = trans3DPointTo2DPoint(p);
        drawLine(g, new SLine(p2d1, p2d2), Color.red);
//---------------------------------------------------------------------    
        SPoint3D temp3d = new SPoint3D(rectangular.c.x + rectangular.w, rectangular.c.y + rectangular.l, rectangular.c.z + rectangular.h);
        p = new SPoint3D(rectangular.c.x + rectangular.w, rectangular.c.y, rectangular.c.z + rectangular.h);
        p2d1 = trans3DPointTo2DPoint(temp3d);
        p2d2 = trans3DPointTo2DPoint(p);
        drawLine(g, new SLine(p2d1, p2d2), Color.red);

        p = new SPoint3D(rectangular.c.x, rectangular.c.y + rectangular.l, rectangular.c.z + rectangular.h);
        p2d1 = trans3DPointTo2DPoint(temp3d);
        p2d2 = trans3DPointTo2DPoint(p);
        drawLine(g, new SLine(p2d1, p2d2), Color.red);

        p = new SPoint3D(rectangular.c.x + rectangular.w, rectangular.c.y + rectangular.l, rectangular.c.z);
        p2d1 = trans3DPointTo2DPoint(temp3d);
        p2d2 = trans3DPointTo2DPoint(p);
        drawLine(g, new SLine(p2d1, p2d2), Color.red);
        g.drawString("G : " + temp3d.x+", "+temp3d.y+", "+temp3d.z, 10 , 100);
        temp[7] = transPointToSystemCoordinates(p2d1);

        g.setColor(Color.BLACK);
        g.drawString("A", (temp[0].x + 30) * 5, (temp[0].y + 10) * 5);
        g.drawString("B", (temp[1].x + 30) * 5, (temp[1].y + 10) * 5);
        g.drawString("C", (temp[6].x + 30) * 5, (temp[6].y + 10) * 5);
        g.drawString("D", (temp[2].x + 30) * 5, (temp[2].y + 10) * 5);
        g.drawString("E", (temp[3].x + 30) * 5, (temp[3].y + 10) * 5);
        g.drawString("F", (temp[4].x + 30) * 5, (temp[4].y + 10) * 5);
        g.drawString("G", (temp[7].x + 30) * 5, (temp[7].y + 10) * 5);
        g.drawString("H", (temp[5].x + 30) * 5, (temp[5].y + 10) * 5);

    }

    public void drawCircle3D_z(Graphics g, SPoint3D c, int r) {
        int rx = r, ry = (int) (0.707106 * r);
        SPoint2D p2d = trans3DPointTo2DPoint(c);
        int xc = p2d.x, yc = p2d.y;
        int dx, dy, d1, d2, x, y;
        x = 0;
        y = ry;

        // Initial decision parameter of region 1
        d1 = (int) ((ry * ry) - (rx * rx * ry)
                + (0.25 * rx * rx));
        dx = 2 * ry * ry * x;
        dy = 2 * rx * rx * y;
        int n = 0;
        // For region 1
        while (dx < dy) {
            n++;
            // Print points based on 4-way symmetry
            if (n % 4 != 0) {
                drawPixel(g, new SPoint2D(((x + xc)), ((y + yc))));
                drawPixel(g, new SPoint2D(((-x + xc)), ((y + yc))));
            }

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

            n++;
            // Print points based on 4-way symmetry
            if (n % 4 != 0) {
                drawPixel(g, new SPoint2D(((x + xc)), ((y + yc))));
                drawPixel(g, new SPoint2D(((-x + xc)), ((y + yc))));
            }
            // printing points based on 4-way symmetry

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

    public void drawCircle3D_y(Graphics g, SPoint3D cp3d, int r) {
        SCircle c = new SCircle(r, trans3DPointTo2DPoint(cp3d));
        int x = 0, y = c.R, d = 3 - (2 * c.R);
        drawPixel(g, c.o);
        EightWaySymmetricPlot(g, c.o.x, c.o.y, x, y);
        while (x <= y) {
            if (d <= 0) {
                d = d + (4 * x) + 6;
            } else {
                d = d + (4 * x) - (4 * y) + 10;
                y = y - 1;
            }
            x = x + 1;
            EightWaySymmetricPlot(g, c.o.x, c.o.y, x, y);
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

        g.setColor(Color.red);
        SPoint2D p = transPointToSystemCoordinates(new SPoint2D(2, 60));
        g.drawString("z", p.x * dpi, p.y * dpi);
        p = transPointToSystemCoordinates(new SPoint2D(80, 2));
        g.drawString("x", p.x * dpi, p.y * dpi);
        p = transPointToSystemCoordinates(new SPoint2D(-65, -60));
        g.drawString("y", p.x * dpi, p.y * dpi);

        drawPixel(g, new SPoint2D(0, 0));

    }

    private void draw(Graphics g) {
        switch (mode) {
            case Globular -> {
                drawGlobular(g, globular);
            }
            case Rectangular -> {
                drawRectangular(g, rectangular);
            }
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        Opoint = getCenter();
        super.paintComponent(g);
        drawGrid3D(g);
        draw(g);
    }
}
