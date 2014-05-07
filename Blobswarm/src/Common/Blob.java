package Common;

import java.awt.Point;

public class Blob {
	//Fields
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
	}
	
	public Blob(String name) { 
		this.position = new Point(50,50);
		this.id = 0;
		this.direction = 0;
		this.name = name;
	}
	
	public Blob(Point pos, int ID, int dir, String name) { 
		this.position = pos;
		this.id = ID;
		this.direction = dir;
		this.name = name;
	}
	
	//Methods
	public void move(int direction){
			this.direction = direction;
			updateLocation(direction);
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
			if(this.position.y >= 25)
			this.position.y -= 10;
		break;
		case 2:	//Down
			if(this.position.y <= 535)
			this.position.y += 10;
		break;
		case 3:	//Left
			if(this.position.x >= 25)
			this.position.x -= 10;
		break;
		case 4:	//Right
			if(this.position.x <= 550)
			this.position.x += 10;
		break;
			default:
			return;
		}
		return;
	}
	
}
