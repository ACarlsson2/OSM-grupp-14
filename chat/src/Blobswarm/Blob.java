package Blobswarm;

import java.awt.Point;

public class Blob {
	
	public Point position;
	public int ID;
	
	public Blob() { 
		this.position = new Point(50,50);
		this.ID = 0;
	}
	public Blob(Point pos, int ID) { 
		this.position = pos;
		this.ID = ID;
	}

}
