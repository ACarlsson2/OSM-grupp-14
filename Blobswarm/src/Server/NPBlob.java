package Server;

import java.awt.Point;

import Common.Blob;

public class NPBlob extends Blob {
	
		
	private Point separation(NPBlob[] npblobs){
		Point c = new Point(0,0);
		
		for (Blob blob : npblobs) {
			if (!blob.equals(this)) {
				
				if (blob.getPosition().distance(this.getPosition()) <= 100) {
					double newX = c.getX() - (blob.getPosition().getX() - this.getPosition().getX());
					double newY = c.getY() - (blob.getPosition().getY() - this.getPosition().getY());
					c.setLocation(newX, newY);
				}
			}
		}
		return c;
	}
	
	private Point alignment(NPBlob[] npblobs){
		
		double vx = 0;
		double vy = 0;
		
		for (Blob b : npblobs) {
			if (!b.equals(this)) {
				vx += b.getVelocity().x;
				vy += b.getVelocity().y;
			}
		}
		int N = npblobs.length;
		
		if (N > 0) {
			vx = vx/(N-1);
			vy = vy/(N-1);
		}
		Point pv = new Point();
		pv.setLocation((vx - this.getVelocity().x)/8, (vy - this.getVelocity().y)/8);
		return pv;
	}
	
	private Point cohesion(NPBlob[] npblobs){
		double x = 0;
		double y = 0;
		
		Point pos = this.getPosition();
		
		for (Blob b : npblobs) {
			Point bpos = b.getPosition();
			if (!b.equals(this)) {
				 x = x + bpos.x;
				 y = y + bpos.y;
			}
		}
		if (npblobs.length > 0) {
			x = x / (npblobs.length - 1);
			y = y / (npblobs.length - 1);
		}
		
		Point pc = new Point();
		pc.setLocation(x - pos.x , y - pos.y);
		return pc;
	}
	
	 
	private void updateNPBPositions(NPBlob[] npblobs) {
		Point v1 = separation(npblobs);
		Point v2 = cohesion(npblobs);
		Point v3 = alignment(npblobs);
		Point pos = this.getPosition();
		Point vel = this.getVelocity();
		double vx = vel.getX() + v1.getX() + v2.getX() + v3.getX();
		double vy = vel.getY() + v1.getY() + v2.getY() + v3.getY();
		
		double px = pos.getX() + vx;
		double py = pos.getY() + vy;
		
		this.getPosition().setLocation(px,py);
		this.getVelocity().setLocation(vx, vy);
	}
	
	
}
