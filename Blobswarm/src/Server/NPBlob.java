package Server;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import Common.Blob;

/**
 * 
 * 						NPBlob
 *
 *
 */
public class NPBlob extends Blob implements Runnable{
	
	private List<NPBlob> npblobs;
	private Point velocity;
	
	
	public NPBlob(List<NPBlob> npblobs, Point pos, int ID, int dir, String name){
		super(pos, ID, dir, name);
		this.npblobs = npblobs;
		this.getVelocity().setLocation(0,0);
		
	}
	
	
	public NPBlob(List<NPBlob> npblobs){
		super(new Point(0,0), 0, 0, "NPB");
		this.npblobs = npblobs;
		this.velocity = new Point(0,0);
		
	}
	
	
	
	
	/**
	 * Calculates a point to steer such that the separation of the npb's are kept and
	 * they do not get arbitrarily close.  
	 * @param npblobs - neighbours of this blob
	 * @return A target point.
	 */
	private Point separation(){
		Point c = new Point(0,0);
		
		for (NPBlob blob : npblobs) {
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
	
	/**
	 * Scales the velocity such that NBP's try to align their directions
	 * @return
	 */
	private Point alignment(){
		
		double vx = 0;
		double vy = 0;
		
		for (NPBlob b : npblobs) {
			if (!b.equals(this)) {
				vx += b.getVelocity().x;
				vy += b.getVelocity().y;
			}
		}
		int N = npblobs.size();
		
		if (N > 0) {
			vx = vx/(N-1);
			vy = vy/(N-1);
		}
		Point pv = new Point();
		pv.setLocation((vx - this.getVelocity().getX())/8, (vy - this.getVelocity().getY())/8);
		return pv;
	}
	
	
	/**
	 * NPB's try to stay close to their immediate neighbors.
	 * @return a point to steer towards.
	 */
	private Point cohesion(){
		double x = 0;
		double y = 0;
		
		Point pos = this.getPosition();
		
		for (NPBlob b : npblobs) {
			Point bpos = b.getPosition();
			if (!b.equals(this)) {
				 x = x + bpos.x;
				 y = y + bpos.y;
			}
		}
		if (npblobs.size() > 0) {
			x = x / (npblobs.size() - 1);
			y = y / (npblobs.size() - 1);
		}
		
		Point pc = new Point();
		pc.setLocation(x - pos.x , y - pos.y);
		return pc;
	}
	
	 
	private synchronized void updateNPBPositions() {
		Point v1 = separation();
		Point v2 = cohesion();
		Point v3 = alignment();
		Point pos = this.getPosition();
		Point vel = this.getVelocity();
		double vx = vel.getX() + v1.getX() + v2.getX() + v3.getX();
		double vy = vel.getY() + v1.getY() + v2.getY() + v3.getY();
		
		double px = pos.getX() + vx;
		double py = pos.getY() + vy;
		
		this.getPosition().setLocation(px,py);
		this.getVelocity().setLocation(vx, vy);
	}
	
	@Override
	public void move(int direction) {
		updateNPBPositions();
	}

	@Override
	public void run() {
		while (true) {
			try {
				move(0);
				Thread.sleep(1000/30); // 30 FPS
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Point getVelocity(){
		return velocity;
	}
	
	public void setVelocity(Point v){
		this.velocity = v;
	}
	
	public void setVelocity(double vx, double vy){
		Point v = new Point();
		v.setLocation(vx, vy);
		this.velocity = v;
	}
}
