/*
* Filename: Bouncer.java
* Author: Elaine Ha
* Date: 8/28/19
*/

import objectdraw.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*; 
/*
   * Name: Bouncer
   * Purpose: This method creates the bouncer object and determines how 
   * the bouncer reacts.
   */
public class Bouncer extends Creature
{
  private DrawingCanvas canvas;
  //point where mouse cursor is clicked
  private Location point; 
  private Line bouncerHorizontal; //horizontal line of bouncer
  private Line bouncerVertical; //vertical line of bouncer
  private Location updatedPoint; //location after bouncer moves
  Random randomGenerator =  new Random();
  private int x; //x movement
  private int y; //y movement
  //half of the diameter of bouncer
  private final static double HALF_OF_LENGTH = 7.5;
  //maximum number that random number generator should be
  private final static int MAX_RAND = 21;
  //subtract from 10 for the random number generator 
  private final static int SUB_TEN = 10;
  //new location if bouncer hits margins
  private Location updatedPoint1; 
  //movement if bouncer hits margins
  private int newX;
  private int newY; 
 
  /*
   * Name: Bouncer
   * Purpose: This method creates the bouncer at a specified point. 
   * Parameters: 
   * @param point represents the location of where seeker is created
   * @param canvas
   */
  public Bouncer (Location point, DrawingCanvas canvas)
  {
    super(point, canvas);   
    this.canvas = canvas;
    //creates plus sign shape of bouncer
    bouncerHorizontal = new Line(getLoc().getX()-HALF_OF_LENGTH, 
		        getLoc().getY(), getLoc().getX() + HALF_OF_LENGTH, 
			getLoc().getY(), this.canvas);
    bouncerVertical = new Line(getLoc().getX(), 
		    point.getY() - HALF_OF_LENGTH, getLoc().getX(), 
		    getLoc().getY() + HALF_OF_LENGTH, this.canvas);
    //makes color orange
    bouncerHorizontal.setColor(Color.ORANGE);
    bouncerVertical.setColor(Color.ORANGE); 

  }

  /*
   * Name: reactTo
   * Purpose: This method determines how the bouncer moves. 
   * Parameters: 
   * @param other is the closest creature to the seeker
   */
  public void reactTo(Creature other)
  {
    //generates the random number for x and y coordinates
    x = randomGenerator.nextInt(MAX_RAND)-SUB_TEN; 
    y = randomGenerator.nextInt(MAX_RAND)-SUB_TEN;
    //moves bouncer to those x and y coordinates
    bouncerHorizontal.moveTo(getLoc().getX() + x - HALF_OF_LENGTH, 
		    getLoc().getY() + y);
    bouncerVertical.moveTo(getLoc().getX() + x, 
		    getLoc().getY() +y - HALF_OF_LENGTH);
    updatedPoint = new Location(getLoc().getX()+x, getLoc().getY()+y); 
    this.setLoc(updatedPoint); 


    if (bouncerHorizontal.getStart().getX() <= 0 || 
        bouncerVertical.getStart().getY() <= 0 || 
        bouncerHorizontal.getEnd().getX() >= canvas.getWidth() || 
	bouncerVertical.getEnd().getY() >= canvas.getHeight())
      { //move bouncer back on canvas
        newX = -1*x; 
        newY = -1*y; 
        bouncerHorizontal.move(newX, newY);
	bouncerVertical.move(newX, newY); 
	updatedPoint1 = new Location(getLoc().getX()+newX, 
			     getLoc().getY()+newY); 
	this.setLoc(updatedPoint1); 	
      }

  }
  /*
   * Name: notRun
   * Purpose: This will ensure that the bouncer will not move when stop button
   * is clicked. 
   */
  public void notRun()
  { //if stop button pressed, bouncer does not move
    bouncerHorizontal.move(0,0);
    bouncerVertical.move(0,0);
  }

}
