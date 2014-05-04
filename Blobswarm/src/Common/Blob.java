package Common;

import java.awt.Point;

public class Blob {
	
	private Point position;
	private int id;
	private int direction;
	
	//Constructor
	public Blob() { 
		this.position = new Point(50,50);
		this.id = 0;
		this.direction = 0;
	}
	public Blob(Point pos, int ID, int dir) { 
		this.position = pos;
		this.id = ID;
		this.direction = dir;
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
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	

	//Help methods
	private void updateLocation(int direction) {		
		switch (direction) {
		case 1:	//Upp
			this.position.y -= 10;
		break;
		case 2:	//Down
			this.position.y += 10;
		break;
		case 3:	//Left
			this.position.x -= 10;
		break;
		case 4:	//Right
			this.position.x += 10;
		break;
			default:
			return;
		}
		return;
	}
	
}
