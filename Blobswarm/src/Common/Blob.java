package Common;

import java.awt.Point;


public class Blob {
	//Fields
	private Point position;
	private Point velocity;
	private int id;
	private int direction;
	private String name;
	private int momentspeed = 10;
	private Size size; //To Change blobSize change this
	
	//Constructor
	public Blob() { 
		this.position = new Point(50,50);
		this.id = 0;
		this.direction = 0;
		this.name = "";
		this.size = new Size(45,37);
	}
	
	public Blob(String name) { 
		this.position = new Point(50,50);
		this.id = 0;
		this.direction = 0;
		this.name = name;
		this.size = new Size(45,37);
	}
	
	public Blob(Point pos, int ID, int dir, String name) { 
		this.position = pos;
		this.id = ID;
		this.direction = dir;
		this.name = name;
		this.size = new Size(45,37);
	}
	
	//Methods
	public void move(int direction){
			this.direction = direction;
			updateLocation(direction);
	}
	
	public boolean insideBody(Point pos){
		double width = pos.getX();
		double higth = pos.getY();
		if((this.position.getX() <= width) && (this.position.getX()+size.getWidth()) >= width) {
			if((this.position.getY() <= higth) && (this.position.getY()+size.getHight()) >= higth) {
				return true;
			}
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
	
	public int getMomentspeed(){
		return momentspeed;
	}
	
	public Size getSize(){
		return this.size;
	}
	

	//Help methods
	private void updateLocation(int direction) {		
		switch (direction) {
		case 1:	//Upp
			if(this.position.y >= 25)
			this.position.y -= momentspeed;
		break;
		case 2:	//Down
			if(this.position.y <= 535)
			this.position.y += momentspeed;
		break;
		case 3:	//Left
			if(this.position.x >= 25)
			this.position.x -= momentspeed;
		break;
		case 4:	//Right
			if(this.position.x <= 550)
			this.position.x += momentspeed;
		break;
			default:
			return;
		}
		return;
	}
	
	//TODO equal funktion that check ID and return true if same ID
	
}
