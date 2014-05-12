package Client;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

public class NPBlobView {
	//Fields
    public static final int SIZE = 50;
    
    BufferedImage image = null;
    
    private JComponent comp;
    
    String path0 = "NPB0.png";
    String path1 = "NPB1.png";
    String path2 = "NPB2.png";
    String path3 = "NPB3.png";
    
    File file0 = new File(path0);
    File file1 = new File(path1);  
    File file2 = new File(path2); 
    File file3 = new File(path3);  
    //Constructor
    public NPBlobView() {
        
		try {
			image = ImageIO.read(file0);
			comp = new JLabel(new ImageIcon(image));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
        this.comp.setBackground(new Color(0,0,0,0));
        this.comp.setOpaque(false);


    }
    
    //Methods
    public void set(Point loc, int dir) { 	
   	
        this.comp.setBounds((int)loc.getX() - SIZE/2, (int)loc.getY() - SIZE/2, SIZE, SIZE);
        File tempfile;
        switch(dir){
        case 0: tempfile = file0;
        break;
        case 1: tempfile = file0;
        break;
        case 2: tempfile = file1;
        break;
        case 3: tempfile = file2;
        break;
        default: tempfile = file3;
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
        temp.setIcon(new ImageIcon(image));
    }

	
    public JComponent getJComponent() {
        return this.comp;
    }
    
}