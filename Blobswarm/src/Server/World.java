package Server;

import java.awt.Point;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import Common.Blob;
import Common.NPBlob;


public class World {
	//Field
	private Blob[] playerBlobs;
	private List<NPBlob> nonPlayerBlobs;
	private int numNPB = 5;
	private int height;
	private int width;
	
	//Constructor
	public World(){	
		nonPlayerBlobs = Collections.synchronizedList(new LinkedList<NPBlob>());
	}
	
	//Methods
	/** TODO
	 * Side effect: Moves the Blob if allowed and nothing hinders it
	 * @param blob
	 * @param direction
	 * @return true if moved false if not
	 */
	public boolean attemptMove(Blob blob, int direction){
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
				if(playerBlobs[i].contains(destination) && playerBlobs[i].getAlive()){
					blob.setDirection(direction);					
					return false;
				}					
			}	
		}
		this.move(blob, direction);
		return true;
	}
	
	
	/**
	 * Spawns n NPBs
	 * @param n
	 */
	public void spawnNPB(){
		for (int i = 0; i < numNPB; i++) {
			NPBlob blob = new NPBlob(nonPlayerBlobs, new Point(50,50));
			spawnLocation(blob);
			nonPlayerBlobs.add(blob);
			new Thread(blob).start();
		}
	}
	
	
	/**
	 * Updates the position of a spawning blob, such that 
	 * it is not in conflict with another blob. 
	 * @param blob
	 */
	public void spawnLocation(Blob blob){
		while (checkConflictingPosition(blob)){
			blob.getPosition().x += 100;
			blob.getPosition().y += 50;
		}
	}
	
	/**
	 * Checks if a position is safe to spawn in.
	 * @param blob
	 * @return
	 */
	private boolean checkConflictingPosition(Blob blob){
		boolean conflict = false;
		if(playerBlobs != null){
		for (Blob otherBlob : playerBlobs) {
			if (!blob.equals(otherBlob)) {
				if (otherBlob.contains(blob.getPosition())) {
					conflict = true;
				}
			}
		}
		}
		for (Blob otherBlob : nonPlayerBlobs) {
			if (!blob.equals(otherBlob)) {
				if (otherBlob.contains(blob.getPosition())) {
					conflict = true;
				}
			}
		}
		return conflict;
	}
	public Blob[] getPlayers(){
		return playerBlobs;
	}
	
	public List<NPBlob> getNPB(){
		return nonPlayerBlobs;
	}
	
	public void setPlayers(Blob[] listOfPlayers){
		this.playerBlobs = listOfPlayers;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
	
	public synchronized void move(Blob blob, int dir){
		blob.move(dir);
	}
	
}
