package Common;

public class Size {
	//Fields
	private double width;  
	private double height;
	
	//Constructors
	public Size(){
	}
	
	public Size(double width, double hight) {
		this.width = width;
		this.height = hight;	
	}
	
	//Methods
	public double getHeight(){
		return this.height;
	}
	
	public double getWidth(){
		return this.width;
	}
	
	public void setHeight(int hight){
		this.height = hight;
	}
	
	public void setWidth(int width){
		this.width = width;
	}
	
}
