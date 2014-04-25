package model;

import java.util.Set;
import java.awt.Point;
import java.awt.Dimension;


/**
 * The PongModel keeps track of the bars, the ball and the game state.
 */
public interface BlobswarmModel {

    /**
     * Takes the inputs and applies them to the model, computing one
     * simulation step. delta_t is the time that has passed since the
     * last compute step -- use this in your time integration to have
     * the items move at the same speed, regardless of the framerate.
     * @throws InterruptedException 
     */
    public void compute(Set<Input> input, long delta_t) throws InterruptedException;

    /**
     * getters that take a BarKey LEFT or RIGHT
     * and return positions of the various items on the board
     */

    public Point getBallPos();

    /**
     * Will output information about the state of the game to be
     * displayed to the players
     */
    
    /**
     * a valid implementation of the model will keep the field size
     * will remain constant forever.
     */
    public Dimension getFieldSize();

	public int getDir();
}