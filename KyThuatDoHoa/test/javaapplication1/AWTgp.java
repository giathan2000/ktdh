/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import java.awt.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.*;

/**
 *
 * @author hesac
 */
public class AWTgp extends Frame {

    public static int dpi = 5;
    public static int windowWidth = 1500;
    public static int windowHight = 1000;
    public static int xO = 100;
    public static int yO = 100;

    public AWTgp() throws HeadlessException {
        super("Tesstt 1");
        this.setSize(AWTgp.windowWidth, AWTgp.windowHight);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });


    }

    public static void main(String[] args) {
        AWTgp demo = new AWTgp();
        demo.setVisible(true);
    }
    
    
    @Override
    public void paint(Graphics a) {
        Graphics2D g = (Graphics2D) a;

        drawGrid(g, 200, 200);
        drawLineBresenham(g, 2, 3, 200, 100);
//        for (int i = 0; i < 100; i++) {
//            putPixel(g, 10 + i, 10 + i);
//        }
        System.err.println("1");
    }

    public void drawPixel(Graphics2D g, int x, int y) {
        Rectangle2D.Double d = new Rectangle2D.Double(x * AWTgp.dpi, y * AWTgp.dpi, AWTgp.dpi, AWTgp.dpi);
        g.fill(d);
    }

    public void drawGrid(Graphics2D g, int w, int h) {
        g.setColor(new Color(230, 230, 230));

        for (int i = 0; i < h + 1; i++) {
            g.draw(new Line2D.Double(0, i * AWTgp.dpi, w * AWTgp.dpi, i * AWTgp.dpi));
        }

        for (int i = 0; i < w + 1; i++) {
            g.draw(new Line2D.Double(i * AWTgp.dpi, 0, i * AWTgp.dpi, h * AWTgp.dpi));
        }
    }

    public void drawLineDDA(Graphics2D g, double x1, double y1, double x2, double y2) {
        double m = (y2 - y1) / (x2 - x1);
        for (int i = (int) x1; i <= (int) x2; i++) {
            y1 = y1 + m;
            drawPixel(g, i, (int) Math.round(y1));
        }
    }

    public void drawLineBresenham(Graphics2D g, int x1, int y1, int x2, int y2) {
        int Dx = (x2 - x1);
        int Dy = (y2 - y1);
        int pi = 2 * Dy - Dx;
        int c1 = (Dy - Dx) < 0 ? -(2 * Dy - 2 * Dx) : (2 * Dy - 2 * Dx);
        for (int i = x1; i < x2; i++) {
            if (pi < 0) {
                pi = pi + 2 * Dy;
//                System.err.println("do1");
//                System.err.println(pi + "   " + 2 * Dy);
            } else {
                pi = pi + 2 * Dy - 2 * Dx;
                y1++;
//                System.err.println("do2");
//                System.err.println(pi + "   " + c1);
            }
            drawPixel(g, i, y1);
        }
    }
}
