package view;

import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.Point;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import model.BlobswarmModel;
import model.BarKey;

public class BlobswarmView {
    private final int WIDTH_PXL;
    private final int HEIGHT_PXL;

    private final BlobswarmModel model;

    final Ball ball;

    final JFrame frame;

  

    public BlobswarmView(BlobswarmModel model) {
        //initialize the View members:
        this.model = model;

        WIDTH_PXL = 600;
        HEIGHT_PXL = WIDTH_PXL*((int)model.getFieldSize().getHeight())/((int)model.getFieldSize().getWidth());

        this.ball = new Ball();

        final Map<BarKey,JLabel> scorelabels = new HashMap<BarKey,JLabel>();

        // initialize the graphics stuff:
        final JFrame frame = new JFrame("Blobswarm");
        try {
        SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    
                    frame.setLayout(null);
                    JLabel background = new JLabel("");
                    background.setBackground(Color.WHITE);
                    background.setOpaque(true);
                    background.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                    background.setBounds(0,0,WIDTH_PXL,HEIGHT_PXL);
                    frame.getContentPane().setPreferredSize(new Dimension(WIDTH_PXL, HEIGHT_PXL));             
                    frame.getContentPane().add(ball.getJComponent());
                    frame.getContentPane().add(background);
                    
                    frame.pack();
                }
            });
        } catch (Exception e) {
            System.err.println("Couldn't initialize PongView: " + e);
            System.exit(1);
        }
        this.frame = frame;

    }

    public void show() {
        this.frame.setVisible(true);
    }

    public JFrame getFrame() {
        return frame;
    }

    private Point scalePoint(Point modelPoint) {
        return new Point(scaleXPos((int)modelPoint.getX()), scaleYPos((int)modelPoint.getY()));
    }

    private int scaleXPos(int modelXPos) {
        return (int)(((modelXPos)*1.0/((int)model.getFieldSize().getWidth()))
                     * WIDTH_PXL);
    }

    private int scaleYPos(int modelYPos) {
        return (int)((modelYPos*1.0/((int)model.getFieldSize().getHeight())) * HEIGHT_PXL);
    }

    public void update() {
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    ball.update(scalePoint(model.getBallPos()),model.getDir());
                }
            });
    }
}

/**
 * visualizing the ball
 */
class Ball {
    public static final int SIZE = 50;
    BufferedImage image;
    private JComponent comp;
    String path0 = "Blob0.png";
    File file0 = new File(path0);
    String path1 = "Blob1.png";
    File file1 = new File(path1);
    String path2 = "Blob2.png";
    File file2 = new File(path2);
    String path3 = "Blob3.png";
    File file3 = new File(path3);
    String path4 = "Blob4.png";
    File file4 = new File(path4);

    public Ball() {
        

		try {
			image = ImageIO.read(file0);
			comp = new JLabel(new ImageIcon(image));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
        this.comp.setBackground(Color.WHITE);
        this.comp.setOpaque(true);
    }

    public void update(Point loc, int dir) {
        this.comp.setBounds((int)loc.getX() - SIZE/2, (int)loc.getY() - SIZE/2, SIZE, SIZE);
        File tempfile;
        switch(dir){
        case 0: tempfile = file0;
        break;
        case 1: tempfile = file1;
        break;
        case 2: tempfile = file2;
        break;
        case 3: tempfile = file3;
        break;
        default: tempfile = file4;
        break;
        }
        try {
			image = ImageIO.read(tempfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        JLabel temp = (JLabel) comp;
        temp.setIcon(new ImageIcon(image));
    }

    public JComponent getJComponent() {
        return this.comp;
    }
}