package Client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class BlobView {
	//Fields
    public static final int SIZE = 50;
    private int ID;
    
    BufferedImage image = null;
    
    private JComponent comp;
    private JLabel nameLabel;
    private boolean alive;
    
    String path0 = "Blob0.png";
    String path1 = "Blob1.png";
    String path2 = "Blob2.png";
    String path3 = "Blob3.png";
    String path4 = "Blob4.png";
    
    File file0 = new File(path0);
    File file1 = new File(path1);  
    File file2 = new File(path2); 
    File file3 = new File(path3);  
    File file4 = new File(path4);

    //Constructor
    public BlobView(int ID, String name) {
        this.ID = ID;
        this.alive = true;
        nameLabel = new JLabel("name");
        nameLabel.setText(name);

		try {
			image = ImageIO.read(file0);
			comp = new JLabel(new ImageIcon(image));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
        this.comp.setBackground(new Color(0,0,0,0));
        this.comp.setOpaque(false);

        styleBlobName();

    }
    
    //Methods
    
    /**
	 * Updates the position and direction of the blob.
	 * @param loc - new position
	 * @param dir - new direction
	 */
    
    public void update(Point loc, int dir) { 	
   	
        this.comp.setBounds((int)loc.getX() - SIZE/2, (int)loc.getY() - SIZE/2, SIZE, SIZE);
        nameLabel.setBounds(comp.getBounds().x, comp.getBounds().y-35, SIZE, SIZE);
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
        if(temp.getIcon()!=image)
        if(alive){
        	temp.setIcon(new ImageIcon(image));
        }
    }

    /**
	 * Sets the font, color and alignment of the name text.
	 */
    
	private void styleBlobName() {
		//Styling of text above the blobs
        Font styleText = new Font("Arial", Font.BOLD, 12);
        nameLabel.setFont(styleText);
        nameLabel.setForeground(Color.white);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
	}
	
    public JComponent getJComponent() {
        return this.comp;
    }
    public JLabel getNameLabel() {
        return this.nameLabel;
    }
    
    public int getID(){
    	return this.ID;
    }
    
    /**
	 * Updates the alive state of the blob and changes its picture if it becomes dead.
	 * @param alive - new alive state.
	 */
    
    public void setAlive(boolean alive){
    	this.alive = alive;
    	if(!this.alive){
    		try {
    			image = ImageIO.read(new File("DeadBlob.png"));
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}        
            JLabel temp = (JLabel) comp;
            if(temp.getIcon()!=image)
            temp.setIcon(new ImageIcon(image));
    	}
    }
    public boolean getAlive(){
    	return this.alive;
    }
    
}