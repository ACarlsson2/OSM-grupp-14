package Server;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;

import Common.Blob;


public class World {
	//Field
	private Blob[] players;
	private LinkedList<NPBlob> npb;
	
	//Constructor
	public World(){	
		
	}
	
	//Methods
	/** TODO
	 * Side effekt: Move the Blob if allowed and nothing to hinder it
	 * @param blob
	 * @param direction
	 * @return true if moved false if not
	 */
	public synchronized boolean tryToMove(Blob blob, int direction){
		Point tryNextPos = new Point(blob.getPosition());
		switch (direction) {
		case 1:	//Upp
			tryNextPos.y -= blob.getMomentspeed();
		break;
		case 2:	//Down
			tryNextPos.y += blob.getMomentspeed();
		break;
		case 3:	//Left
			tryNextPos.x -= blob.getMomentspeed();
		break;
		case 4:	//Right
			tryNextPos.x += blob.getMomentspeed();
		break;
			default:
				return false; //Unknown
		}

		for(int i = 0; i < players.length; i++){
			if(!blob.equals(players[i])){
				if(blob.insideBody(tryNextPos, players[i])){
					blob.setDirection(direction);
					return false;
				}					
			}	
		}
		blob.move(direction);
		return true;
	}
	
	public Blob[] getPlayers(){
		return players;
	}
	
	public LinkedList<NPBlob> getNPB(){
		return npb;
	}
	
	public void setPlayers(Blob[] listOfPlayers){
		this.players = listOfPlayers;
	}
	
}
