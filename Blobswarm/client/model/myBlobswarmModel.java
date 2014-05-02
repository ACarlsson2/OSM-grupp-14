package model;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Set;

import javax.swing.SwingConstants;

public class myBlobswarmModel implements BlobswarmModel {

	private Blob PongBall;
	private int dir; //0 = still, 1 = up, 2 = down, 3 = left, 4 = right
	private String LeftPlayer;
	private String RightPlayer;
	private Dimension FieldSize;
	
public myBlobswarmModel(){

}

public myBlobswarmModel(String leftPlayer, String rightPlayer){
	LeftPlayer = leftPlayer;
	RightPlayer = rightPlayer;
	dir = 0;
	FieldSize = new Dimension(2000,2000);

	PongBall = new Blob(new Point(FieldSize.width/2,FieldSize.height/2),new Point(10,20),30,30);

	}



@Override
public void compute(Set<Input> input, long delta_t) throws InterruptedException{
	PongBall.InitSpeed = (int) (30 * delta_t/33);

	for(Input inputValue: input){ // For each element of type Input in the hashset input.
		switch(inputValue.key){
		case LEFT:					// Do this if the input is one of the left players inputs.
			switch(inputValue.dir){
			case UP:				// Do this if the input is the up button.
				if(PongBall.Position.y-60 > 0){
				PongBall.Position.y -= 10;
				dir = 1;
				}
				continue;
			case DOWN:               // Do this if the input is the down button.
				if(PongBall.Position.y+60 < FieldSize.height){
				PongBall.Position.y += 10;
				dir = 2;
				}
				continue;
			}
			continue;
		case RIGHT:                 // Do this if the input is one of the right players inputs.
			switch(inputValue.dir){
			case UP:				// Do this if the input is the up button.
				if(PongBall.Position.x-60 > 0){
				PongBall.Position.x -= 10;
				dir = 3;
				}
				continue;
			case DOWN:				// Do this if the input is the down button.
				if(PongBall.Position.x < FieldSize.width-60){
				PongBall.Position.x += 10;
				dir = 4;
				}
				continue;
			}
			continue;	
		}
	}
	
	
}




@Override
public Point getBallPos() {
	return PongBall.Position;
}




@Override
public Dimension getFieldSize() {
	return FieldSize;
}
public Point getVelocity(){
	return PongBall.Velocity;
}
public void setVelocity(Point p){
	PongBall.Velocity = p;
}
public void setBallPos(Point p){
	PongBall.Position = p;
}
public int getDir(){
	return dir;
}
}