package client.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import client.model.Blob;
import client.model.BlobSwarmModel;

public class BlobSwarmView {

	protected static int WIDTH_PXL;
	protected static int HEIGHT_PXL;
	private Set<Blob> blobs;
	private JFrame frame;
	private BlobSwarmModel model;
	
	
	public BlobSwarmView(){
		WIDTH_PXL = 600;
		HEIGHT_PXL = WIDTH_PXL*((int)model.getFieldSize().getHeight())/((int)model.getFieldSize().getWidth());
		
		this.frame = new JFrame("BlobSwarm");
		try {
		SwingUtilities.invokeAndWait(new Runnable() {
			
			@Override
			public void run() {
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setLayout(null);
                JLabel background = new JLabel("");
                background.setBackground(Color.WHITE);
                background.setOpaque(true);
                background.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                background.setBounds(0,0,WIDTH_PXL,HEIGHT_PXL);
                frame.getContentPane().setPreferredSize(new Dimension(WIDTH_PXL, HEIGHT_PXL));             
                for (Blob b : blobs) {
					frame.getContentPane().add(b.getJComponent());
				}
                frame.getContentPane().add(background);
			}
		});
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
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
                    ball.update(scalePoint(model.getPos()),model.getDir());
                }
            });
    }
}
