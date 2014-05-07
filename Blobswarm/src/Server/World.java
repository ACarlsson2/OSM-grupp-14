package Server;

import java.awt.Point;
import java.util.LinkedList;

import Common.Blob;


public class World {
	//Field
	private Blob[] playerBlobs;
	private LinkedList<NPBlob> nonPlayerBlobs;
	
	//Constructor
	public World(){	
		
	}
	
	//Methods
	/** TODO
	 * Side effect: Moves the Blob if allowed and nothing hinders it
	 * @param blob
	 * @param direction
	 * @return true if moved false if not
	 */
	public synchronized boolean attemptMove(Blob blob, int direction){
		Point destination = new Point(blob.getPosition());
		switch (direction) {
		case 1:	//Up
			destination.y -= blob.getSpeed();
		break;
		case 2:	//Down
			destination.y += blob.getSpeed();
		break;
		case 3:	//Left
			destination.x -= blob.getSpeed();
		break;
		case 4:	//Right
			destination.x += blob.getSpeed();
		break;
			default:
				return false; //Unknown
		}

		for(int i = 0; i < playerBlobs.length; i++){
			if(!blob.equals(playerBlobs[i])){
				if(blob.contains(destination, playerBlobs[i])){
					blob.setDirection(direction);
					return false;
				}					
			}	
		}
		blob.move(direction);
		return true;
	}
	
	public Blob[] getPlayers(){
		return playerBlobs;
	}
	
	public LinkedList<NPBlob> getNPB(){
		return nonPlayerBlobs;
	}
	
	public void setPlayers(Blob[] listOfPlayers){
		this.playerBlobs = listOfPlayers;
	}
	
}
