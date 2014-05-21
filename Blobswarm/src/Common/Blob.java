package Common;

import java.awt.Point;


public class Blob {
	//Fields
	private Point position;
	private Point velocity;
	private int ID;
	private int direction;
	private String name;
	private int speed = 5;
	private Size size = new Size(70,55); //To Change blobSize change this
	
	//Constructor
	public Blob() { 
		this.position = new Point(50,50);
		this.ID = 0;
		this.direction = 0;
		this.name = "";
	}
	
	public Blob(String name) { 
		this.position = new Point(50,50);
		this.ID = 0;
		this.direction = 0;
		this.name = name;
	}
	
	public Blob(Point pos, int ID, int dir, String name) { 
		this.position = pos;
		this.ID = ID;
		this.direction = dir;
		this.name = name;
	}
	
	//Methods
	public void move(int direction){
			this.direction = direction;
			updateLocation(direction);
	}
	/**
	 * Check if pos exist inside a blob
	 * @param pos 
	 * @return
	 */
	public boolean contains(Point pos){
		double width = pos.getX();
		double height = pos.getY();
		if(((this.position.getX()-(size.getWidth()/2)) <= width) && ((this.position.getX()+(size.getWidth()/2))) >= width) {
			if(((this.position.getY()-(size.getHeight()/2)) <= height) && ((this.position.getY()+(size.getHeight()/2))) >= height) {
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
	
	public int getID(){
		return this.ID;
	}
	public String getName(){
		return this.name;
	}
	
	public void setID(int id){
		this.ID = id;
	}
	
	public int getSpeed(){
		return speed;
	}
	
	public Size getSize(){
		return this.size;
	}
	
	public void setPosition(Point position){
		this.position = position;
	}
	
	public void setDirection(int number){
		this.direction = number;
	}
	

	//Help methods
	private void updateLocation(int direction) {		
		switch (direction) {
		case 1:	//Upp
			if(this.position.y >= 25)
			this.position.y -= speed;
		break;
		case 2:	//Down
			if(this.position.y <= 535)
			this.position.y += speed;
		break;
		case 3:	//Left
			if(this.position.x >= 25)
			this.position.x -= speed;
		break;
		case 4:	//Right
			if(this.position.x <= 550)
			this.position.x += speed;
		break;
			default:
			return;
		}
		return;
	}
	
	
	/**
	 * Equals 
	 * return true if the ID's are the same.
	 */
	@Override
	public boolean equals(Object other){
		if(other instanceof Blob){
			return this.ID == ((Blob)other).ID;
		}
		return false;
	}
	
}
