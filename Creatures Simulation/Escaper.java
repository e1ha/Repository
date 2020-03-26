/*
* Filename: Escaper.java
* Author: Elaine Ha
* UserId: cs11s219bo
* Date: 8/29/19
* Sources of help: tutor
*/

import objectdraw.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*; 
/*
 * Name: Escaper
 * Purpose: This class creates the escaper object and determines how the
 * escaper reacts. 
 */
public class Escaper extends Creature
{
  private DrawingCanvas canvas;
  private Location point; //point where mouse is clicked
  private FilledOval escaperShape; //the circle that consists of the escaper
  private double distance; //the shortest distance

  private Location otherLoc; //location of the closest creature
  private Location creatureLoc; //location of the reference creature
  //all 8 directions
  private Location leftUp; 
  private Location up; 
  private Location rightUp; 
  private Location left; 
  private Location right;
  private Location downLeft;
  private Location down;
  private Location downRight;
  //new location after escaper moves 
  private Location updatedPoint;
  //checks which direction the escaper should move 
  private boolean leftUpDir; 
  private boolean upDir; 
  private boolean rightUpDir; 
  private boolean leftDir; 
  private boolean rightDir; 
  private boolean downDir; 
  private boolean downRightDir; 
  private boolean downLeftDir;
  //move in x or y direction
  private int x; 
  private int y; 
  //movement if escaper hits margins
  private int newX;
  private int newY;
  private final static int SIDE_LENGTH= 15;
  private final static double HALF_SIDE_LENGTH = 7.5;
  //the location of all four corners of the escaper
  private Location rightCornerLoc; 
  private Location leftCornerLoc;
  private Location bottomLeftCornerLoc;
  private Location bottomRightCornerLoc;
  Random randomGenerator =  new Random();
  //maximum number random number generator generates
  private int MAX_RAND_NUM = 580;
  
  /*
   * Name: Escaper
   * Purpose: This method creates the escaper at a specified point. 
   * Parameters: 
   * @param point represents the location of where escaper is created
   * @param canvas
   */
  public Escaper (Location point, DrawingCanvas canvas)
  {
    super(point, canvas); 
    this.point = point; 
    this.canvas = canvas;
    escaperShape = new FilledOval(point.getX() - HALF_SIDE_LENGTH, 
		                  point.getY()- HALF_SIDE_LENGTH, 
				  SIDE_LENGTH, SIDE_LENGTH, canvas); 
    escaperShape.setColor(Color.MAGENTA); 
  }
  /*
   * Name: reactTo
   * Purpose: This method determines how the escaper moves and reacts 
   * to other creatures.
   * Parameters: 
   * @param other is the closest creature to the escaper
   */
  public void reactTo(Creature other)
  { //location of closest creature
    otherLoc = new Location (other.getLoc()); 
    distance = this.getLoc().distanceTo(otherLoc); 
    //location of the reference creature    
    creatureLoc = new Location (this.getLoc());
    //creates a location object for all 8 directions    
    leftUp = new Location(creatureLoc.getX() - 1, creatureLoc.getY() + 1); 
    up = new Location(creatureLoc.getX(), creatureLoc.getY() + 1); 
    rightUp = new Location(creatureLoc.getX() + 1, creatureLoc.getY() + 1);
    left = new Location(creatureLoc.getX() -1 , creatureLoc.getY());  
    right = new Location(creatureLoc.getX() + 1, creatureLoc.getY()); 
    downLeft = new Location(creatureLoc.getX() -1, creatureLoc.getY()-1); 
    down = new Location(creatureLoc.getX(), creatureLoc.getY() - 1);
    downRight = new Location(creatureLoc.getX() + 1, creatureLoc.getY() -1); 

    //determines the largest distance out of all 8 directions
    if (distance < leftUp.distanceTo(otherLoc))
    {
      distance = leftUp.distanceTo(otherLoc); 
      leftUpDir = true;
    } 
    if (distance < up.distanceTo(otherLoc))
    {
      distance = up.distanceTo(otherLoc);
      leftUpDir = false;
      upDir = true;
    }
    if (distance < rightUp.distanceTo(otherLoc))
    {
      distance = rightUp.distanceTo(otherLoc);
      leftUpDir = false;
      upDir = false;
      rightUpDir = true;
    }
    if (distance < left.distanceTo(otherLoc))
    {
      distance = left.distanceTo(otherLoc); 
      leftUpDir = false;
      upDir = false;
      rightUpDir = false;
      leftDir = true;
    }
    if (distance < right.distanceTo(otherLoc))
    {
      distance = right.distanceTo(otherLoc); 
      leftUpDir = false;
      upDir = false;
      rightUpDir = false;
      leftDir = false;
      rightDir = true;
    }
    if (distance < downLeft.distanceTo(otherLoc))
    {
      distance = downLeft.distanceTo(otherLoc); 
      leftUpDir = false;
      upDir = false;
      rightUpDir = false;
      leftDir = false;
      rightDir = false;
      downLeftDir = true;
    }
    if (distance < down.distanceTo(otherLoc))
    {
      distance = down.distanceTo(otherLoc); 
      leftUpDir = false;
      upDir = false;
      rightUpDir = false;
      leftDir = false;
      rightDir = false;
      downLeftDir = false;
      downDir = true;
    }
    if (distance < downRight.distanceTo(otherLoc))
    {
      leftUpDir = false;
      upDir = false;
      rightUpDir = false;
      leftDir = false;
      rightDir = false;
      downLeftDir = false;
      downDir = false;
      downRightDir = true;
    }
    //moves the escaper to the direction with the longest distance compared 
    //to closest object
    if (leftUpDir)
    {
      x = -1; 
      y = 1; 
      escaperShape.move(-1,1); 
    }
    else if (upDir)
    {
      x = 0; 
      y = 1;
      escaperShape.move(0,1); 
    }
    else if (rightUpDir)
    {
      x = 1; 
      y = 1;
      escaperShape.move(1,1); 
    } 
    else if (leftDir)
    {
      x = -1; 
      y= 0;
      escaperShape.move(-1,0); 
    }
    else if (rightDir)
    { 
      x = 1; 
      y = 0;
      escaperShape.move(1,0); 
    }
    else if (downLeftDir)
    { 
      x = -1; 
      y = -1;
      escaperShape.move(-1,-1); 
    }
    else if (downDir)
    {
      x = 0; 
      y = -1;
      escaperShape.move(0,-1); 
    }
    else if (downRightDir)
    {
      x = 1; 
      y = -1;
      escaperShape.move(1,-1); 
    }

    leftUpDir = false;
    upDir = false;
    rightUpDir = false;
    leftDir = false;
    rightDir = false;
    downLeftDir = false;
    downDir = false;
    downRightDir = false;

    //locations of all 4 corners
    leftCornerLoc = new Location(this.getLoc()); 
    rightCornerLoc = new Location(leftCornerLoc.getX() + SIDE_LENGTH, 
		                   leftCornerLoc.getY() + SIDE_LENGTH); 
    bottomLeftCornerLoc = new Location(rightCornerLoc.getX(), 
		                         rightCornerLoc.getY() + SIDE_LENGTH);
    bottomRightCornerLoc = new Location(rightCornerLoc.getX(), 
		                         rightCornerLoc.getY() + SIDE_LENGTH);
      
    //if escaper is not in canvas
    if (leftCornerLoc.getX() < 0 || leftCornerLoc.getY() < 0 || 
          rightCornerLoc.getX() > canvas.getWidth() || 
	  bottomLeftCornerLoc.getY() > canvas.getHeight() || 
	  bottomRightCornerLoc.getY() > canvas.getHeight())
    { //generate random coordinates
      newX = randomGenerator.nextInt(MAX_RAND_NUM); 
      newY = randomGenerator.nextInt(MAX_RAND_NUM); 
      //escaper moves to the random coordinates
      escaperShape.moveTo(newX, newY);
      //updates location of escaper
      updatedPoint = new Location(escaperShape.getX(), escaperShape.getY()); 
      this.setLoc(updatedPoint); 	
    }
    else 
    { //updates the location of the escaper after moving it*/
      updatedPoint = new Location(escaperShape.getX()+x, 
			           escaperShape.getY()+y); 
      this.setLoc(updatedPoint); 
    }

  }
  /*
   * Name: notRun
   * Purpose: This will ensure that the escaper will not move when stop button
   * is clicked. 
   */

  public void notRun()
  { //if stop button is pressed, escaper does not move
    escaperShape.move(0,0);
  }


}
