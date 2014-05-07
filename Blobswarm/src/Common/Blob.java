package Common;

import java.awt.Point;
import java.awt.geom.Dimension2D;

public class Blob {
	//Fields
	private int borderLeft = 25;
	private int borderRight = 575;
	private int borderBotton = 555;
	private int borderTop = 25;
	private Dimension2D blobSize;
	
	
	private Point position;
	private Point velocity;
	private int id;
	private int direction;
	private String name;
	
	//Constructor
	public Blob() { 
		this.position = new Point(50,50);
		this.id = 0;
		this.direction = 0;
		this.name = "";
		blobSize.setSize(45,37);
	}
	
	public Blob(String name) { 
		this.position = new Point(50,50);
		this.id = 0;
		this.direction = 0;
		this.name = name;
		blobSize.setSize(45,37);
	}
	
	public Blob(Point pos, int ID, int dir, String name, Dimension2D blobSize) { 
		this.position = pos;
		this.id = ID;
		this.direction = dir;
		this.name = name;
		this.blobSize = blobSize;
	}
	
	//Methods
	public void move(int direction){
			this.direction = direction;
			updateLocation(direction);
	}
	
	public boolean canItMove(int direction, Blob blob){ //TODO linked list for server? with all blobs to check dimensions
		if(true){
			return true;
		}
		return false;
	}
	
	public Point getPosition(){
		return this.position;
	}
	
	public int getDirection(){
		return this.direction;
	}
	
	public Point getVelocity(){
		return velocity;
	}
	
	public int getId(){
		return this.id;
	}
	public String getName(){
		return this.name;
	}
	
	public void setId(int id){
		this.id = id;
	}
	

	//Help methods
	private void updateLocation(int direction) {		
		switch (direction) {
		case 1:	//Upp
			if(this.position.y >= borderTop)							
			this.position.y -= 10;
		break;
		case 2:	//Down
			if(this.position.y <= borderBotton)
			this.position.y += 10;
		break;
		case 3:	//Left
			if(this.position.x >= borderLeft)
			this.position.x -= 10;
		break;
		case 4:	//Right
			if(this.position.x <= borderRight)
			this.position.x += 10;
		break;
			default:
			return;
		}
		return;
	}
	
}
