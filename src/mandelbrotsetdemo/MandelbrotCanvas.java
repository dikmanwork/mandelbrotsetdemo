package mandelbrotsetdemo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 *
 * @author HuangDiWen
 * @created 2014-1-17 9:55:34
 */
public class MandelbrotCanvas extends JComponent {

    private double centerX = -0.75;
    private double centerY = 0;
    private double pxScale = 0.005;
    private final int maxEscapeVal = 256;
    private final float zoomRate = 2f;
    private int threadNum = 1;
    private ColorScheme colorScheme = new HSBColorScheme();

    public MandelbrotCanvas() {
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                centerX = getMinX() + (e.getX() * pxScale);
                centerY = getMaxY() - (e.getY() * pxScale);
                zoomIn();
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, 0), "zoom in");
        this.getActionMap().put("zoom in", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                zoomIn();
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, 0), "zoom out");
        this.getActionMap().put("zoom out", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                zoomOut();
            }
        });
    }

    public void zoomIn() {
        this.pxScale = this.pxScale / this.zoomRate;
        repaint();
    }

    public void zoomOut() {
        this.pxScale = this.pxScale * this.zoomRate;
        repaint();
    }

    public void resize() {
        this.centerX = -0.75;
        this.centerY = 0;
        this.pxScale = 0.005;
        repaint();
    }

    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }

    @Override
    protected void paintComponent(Graphics g) {
        BufferedImage bi = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
        this.parallelDraw(bi, this.threadNum);
        g.drawImage(bi, 0, 0, null);
    }

    public double getMinX() {
        return (this.centerX - ((1 - (getWidth() % 2)) * this.pxScale * 0.5)) - ((getWidth() / 2) * this.pxScale);
    }

    public double getMaxY() {
        return (this.centerY + ((1 - (getHeight() % 2)) * this.pxScale * 0.5)) + ((getHeight() / 2) * this.pxScale);
    }

    public void setColorScheme(ColorScheme colorScheme) {
        this.colorScheme = colorScheme;
    }

    private void parallelDraw(BufferedImage bi, int threadNum) {
        List<Rectangle> rectangles = this.splitImage(bi, threadNum);
        List<Thread> threads = new ArrayList();
        for (Rectangle rectangle : rectangles) {
            DrawRunnable runnable = new DrawRunnable(bi, rectangle);
            Thread thread = new Thread(runnable);
            thread.start();
            threads.add(thread);
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(MandelbrotCanvas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private List<Rectangle> splitImage(BufferedImage bi, int threadNum) {
        List<Rectangle> rectangles = new ArrayList();
        int x = 0, y = 0;
        for (int i = 0; i < threadNum; i++) {
            int height = bi.getHeight() / threadNum;
            Rectangle rectangle = new Rectangle(x, y, bi.getWidth(), height);
            y += height;
            rectangles.add(rectangle);
        }
        if (bi.getHeight() % threadNum != 0) {
            Rectangle rectangle = new Rectangle(x, y, bi.getWidth(), bi.getHeight() % threadNum);
            rectangles.add(rectangle);
        }
        return rectangles;
    }

    private class DrawRunnable implements Runnable {

        private BufferedImage bi;
        private Rectangle rectangle;

        public DrawRunnable(BufferedImage bi, Rectangle rectangle) {
            this.bi = bi;
            this.rectangle = rectangle;
        }

        @Override
        public void run() {
            for (int x = this.rectangle.x; x < this.rectangle.width + this.rectangle.x; x++) {
                for (int y = this.rectangle.y; y < this.rectangle.height + this.rectangle.y; y++) {
                    double real = getMinX() + (x * pxScale);
                    double image = getMaxY() - (y * pxScale);
                    Complex c = new Complex(real, image);
                    int escapeValue = Complex.getMandelbrotEscapeVal(c, maxEscapeVal);
                    if (escapeValue > maxEscapeVal) {
                        bi.setRGB(x, y, Color.BLACK.getRGB());
                    } else {
                        bi.setRGB(x, y, colorScheme.getColor(escapeValue, maxEscapeVal));
                    }
                }
            }
        }

    }
}
