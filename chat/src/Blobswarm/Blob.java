package Blobswarm;

import java.awt.Point;

public class Blob {
	
	public Point position;
	public int ID;
	public int dir;
	
	public Blob() { 
		this.position = new Point(50,50);
		this.ID = 0;
		this.dir = 0;
	}
	public Blob(Point pos, int ID, int dir) { 
		this.position = pos;
		this.ID = ID;
		this.dir = dir;
	}

}
