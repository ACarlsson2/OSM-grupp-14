package Common;

import java.awt.Point;

public class InfoNPB {
	//Fields
	private Point point;
	private int direction;
	
	//Constructor
	public InfoNPB(){
		this.point = new Point(0,0);
		this.direction = 0;
	}
	
	public InfoNPB(Point point, int direction){
		this.point = point;
		this.direction = direction;
	}
	
	//Method
	/**
	 * Update every Field from the npb
	 * @param npb - Non player blob
	 */
	public void update(NPBlob npb) {
		this.point = npb.getPosition();
		this.direction = npb.getDirection();
	}
	public int getDirection(){
		return this.direction;
	}
	public Point getPosition(){
		return this.point;
	}
	
	
}
