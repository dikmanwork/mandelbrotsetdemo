package mandelbrotsetdemo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author HuangDiWen
 * @created 2014-1-17 14:46:29
 */
public class Frame extends JFrame {

    public Frame() {
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new Dimension(512, 384));
        setLocationRelativeTo(this);
        //
        this.getContentPane().setLayout(new BorderLayout());
        final MandelbrotCanvas canvas = new MandelbrotCanvas();
        canvas.setThreadNum(10);
        this.getContentPane().add(canvas);
        //
        JPanel operationPanel = new JPanel();
        operationPanel.setLayout(new FlowLayout());
        final JLabel zoomOutLabel = new JLabel(new ImageIcon(this.getClass().getResource("/mandelbrotsetdemo/zoom_out.png")));
        final JLabel zoomInLabel = new JLabel(new ImageIcon(this.getClass().getResource("/mandelbrotsetdemo/zoom_in.png")));
        final JLabel resizetLabel = new JLabel(new ImageIcon(this.getClass().getResource("/mandelbrotsetdemo/resize.png")));
        operationPanel.add(zoomInLabel);
        operationPanel.add(resizetLabel);
        operationPanel.add(zoomOutLabel);
        zoomOutLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                zoomOutLabel.setIcon(new ImageIcon(this.getClass().getResource("/mandelbrotsetdemo/zoom_out_big.png")));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                canvas.zoomOut();
            }

        });
        resizetLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                resizetLabel.setIcon(new ImageIcon(this.getClass().getResource("/mandelbrotsetdemo/resize_big.png")));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                canvas.resize();
            }

        });
        zoomInLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                zoomInLabel.setIcon(new ImageIcon(this.getClass().getResource("/mandelbrotsetdemo/zoom_in_big.png")));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                canvas.zoomIn();
            }
        });
        operationPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                zoomOutLabel.setIcon(new ImageIcon(this.getClass().getResource("/mandelbrotsetdemo/zoom_out.png")));
                resizetLabel.setIcon(new ImageIcon(this.getClass().getResource("/mandelbrotsetdemo/resize.png")));
                zoomInLabel.setIcon(new ImageIcon(this.getClass().getResource("/mandelbrotsetdemo/zoom_in.png")));
            }
        });
        JComponent glassPanel = (JComponent) this.getGlassPane();
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        glassPanel.setLayout(layout);
        glassPanel.add(operationPanel);
        glassPanel.setVisible(true);
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Frame().setVisible(true);
            }
        });
    }
}
