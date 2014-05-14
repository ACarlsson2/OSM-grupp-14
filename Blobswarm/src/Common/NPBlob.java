package Common;

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
	private static double velocitylimit = 5.0;
	
	public NPBlob(List<NPBlob> npblobs, Point pos, int ID, int dir, String name){
		super(pos, ID, dir, name);
		this.npblobs = npblobs;
		this.velocity = new Point(0,0);
	}
	
	
	public NPBlob(List<NPBlob> npblobs, Point position){
		super(position, 0, 0, "NPB");
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
		
		return scaleVelocity(c);
	}
	
	/**
	 * Scales the velocity such that NBP's try to align their directions
	 * @return
	 */
	private  Point alignment(){
		
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
		return scaleVelocity(pv);
	}
	
	
	/**
	 * NPB's try to stay close to their immediate neighbors.
	 * @return a point to steer towards.
	 */
	private  Point cohesion(){
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
		return scaleVelocity(pc);
	}
	
	 
	private  void updateNPBPosition() {
		Point v1 = separation();
		Point v2 = cohesion();
		Point v3 = alignment();
		Point pos = this.getPosition();
		Point vel = this.getVelocity();
		double vx = vel.getX() + v1.getX() + 0.2*v2.getX() + v3.getX();
		double vy = vel.getY() + v1.getY() + 0.2*v2.getY() + v3.getY();
		
		Point V = scaleVelocity(vx,vy);
		
		double px = pos.getX() + V.getX();
		double py = pos.getY() + V.getY();
		
		
		
		if (px < this.getSize().getWidth()) {
			px = this.getSize().getWidth();
		}
		if(px > 1200 - this.getSize().getWidth()){
			px = 1200.0 - this.getSize().getWidth();
		}
		if(py < this.getSize().getHeight()){
			py = this.getSize().getHeight();
		}
		if(py > 600 - this.getSize().getHeight()){
			py = 600.0 - this.getSize().getHeight();
		}
		
		this.getPosition().setLocation(px,py);
		this.velocity = V;
	}
	
	@Override
	public void move(int direction) {
		synchronized (npblobs) {
			updateNPBPosition();
			System.out.println(this.getPosition());
		}
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
	
	@Override
	public int getDirection(){
		
		int x = this.getVelocity().x;
		int y = this.getVelocity().y;
		if (x > y) {
			if(x >=0){
				return 4;
			}
			else{
				return 3;
			}
		}
		else{
			if(y >= 0){
				return 1;
			}
			else{
				return 2;
			}
		}
	}
	
	
	public Point scaleVelocity(Point velocity){
		double vx = velocity.getX();
		double vy = velocity.getY();
		double r = Math.sqrt(vx*vx + vy*vy);
		if (r > velocitylimit) {
			vx = (vx/r)*velocitylimit;
			vy = (vy/r)*velocitylimit;
		}
		Point newV = new Point();
		newV.setLocation(vx, vy);
		return newV;
	}
	
	public Point scaleVelocity(double vx, double vy){
		double r = Math.sqrt(vx*vx + vy*vy);
		if (r > velocitylimit) {
			vx = (vx/r)*velocitylimit;
			vy = (vy/r)*velocitylimit;
		}
		Point newV = new Point();
		newV.setLocation(vx, vy);
		return newV;
	}
	
	
	public Point getVelocity(){
		return velocity;
	}
	
	public void setVelocity(Point v){
		this.velocity = v;
	}
	
	public void setVelocity(double vx, double vy){
		this.velocity.setLocation(vx, vy);
	}
	
	public InfoNPB getInfo(){
		return new InfoNPB(getPosition(),getDirection());
	}
}

