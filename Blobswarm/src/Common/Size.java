package Common;

public class Size {
	//Fields
	private double width;  
	private double hight;
	
	//Constructors
	public Size(){
	}
	
	public Size(double width, double hight) {
		this.width = width;
		this.hight = hight;	
	}
	
	//Methods
	public double getHight(){
		return this.hight;
	}
	
	public double getWidth(){
		return this.width;
	}
	
	public void setHight(int hight){
		this.hight = hight;
	}
	
	public void setWidth(int width){
		this.width = width;
	}
	
}
