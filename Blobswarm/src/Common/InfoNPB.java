package Common;

import java.awt.Point;
import java.util.List;

public class InfoNPB {
	//Fields
	private Point point;
	private int direction;
	
	//Constructor
	InfoNPB(Point point, int direction){
		this.point = point;
		this.direction = direction;
	}
	
	//Method
	public void update(NPBlob npb) {
		this.point = npb.getPosition();
		this.direction = npb.getDirection();
	}
	
	
}
