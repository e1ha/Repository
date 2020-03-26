/*
* Filename: Seeker.java
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
 * Name: Seeker
 * Purpose: This class creates the seeker object and determines how the
 * seeker reacts. 
 */
public class Seeker extends Creature
{
  private DrawingCanvas canvas;
  private Location point; //point where mouse is clicked
  private FilledRect seekerShape; //the rectangle that consists of the seeker
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
  //the location of all four corners of the seeker
  private Location rightCornerLoc; 
  private Location leftCornerLoc;
  private Location bottomLeftCornerLoc;
  private Location bottomRightCornerLoc;
  //new location after seeker moves 
  private Location updatedPoint;
  //checks which direction the seeker should move 
  private boolean leftUpDir; 
  private boolean upDir; 
  private boolean rightUpDir; 
  private boolean leftDir; 
  private boolean rightDir; 
  private boolean downDir; 
  private boolean downRightDir; 
  private boolean downLeftDir;
  //move in the x direction
  private int x; 
  //move in the y direction
  private int y; 
  //movement if seeker hits margins
  private int newX;
  private int newY; 
  private final static int SIDE_LENGTH= 15;
  private final static double HALF_SIDE_LENGTH = 7.5;
  //new location if seeker hits margins
  private Location updatedPoint1; 
 
   /*
   * Name: Seeker
   * Purpose: This method creates the seeker at a specified point. 
   * Parameters: 
   * @param point represents the location of where seeker is created
   * @param canvas
   */
  public Seeker (Location point, DrawingCanvas canvas)
  {
    super(point, canvas); 
    this.point = point; //the mouse cursor point
    this.canvas = canvas;
    //creates blue seeker
    seekerShape = new FilledRect(point.getX() - HALF_SIDE_LENGTH, 
		                 point.getY()- HALF_SIDE_LENGTH, 
				 SIDE_LENGTH, SIDE_LENGTH, canvas); 
    seekerShape.setColor(Color.CYAN); 
  }

  /*
   * Name: reactTo
   * Purpose: This method determines how the seeker moves and reacts 
   * to other creatures. 
   * Parameters: 
   * @param other is the closest creature to the seeker
   */
  public void reactTo(Creature other)
  {
    if (other != null) //if other creature exists
    { //location of closest creature
      otherLoc = new Location (other.getLoc()); 
      distance = Double.MAX_VALUE;  
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

      //determines the smallest distance out of all 8 directions
      if (distance > leftUp.distanceTo(otherLoc) && leftUp.getX() >= 0
		      && leftUp.getY() <= canvas.getHeight())
      {
        distance = leftUp.distanceTo(otherLoc); 
        leftUpDir = true;
      }
      if (distance > up.distanceTo(otherLoc) && up.getY()<=canvas.getHeight())
      {
        distance = up.distanceTo(otherLoc);
        leftUpDir = false;
        upDir = true;
      }
      if (distance > rightUp.distanceTo(otherLoc) && 
	rightUp.getX() <= canvas.getWidth() && 
	rightUp.getY() <= canvas.getHeight())
      {
        distance = rightUp.distanceTo(otherLoc);
        leftUpDir = false;
        upDir = false;
        rightUpDir = true;
      }
      if (distance > left.distanceTo(otherLoc) && left.getX() >= 0)
      {
        distance = left.distanceTo(otherLoc); 
        leftUpDir = false;
        upDir = false;
        rightUpDir = false;
        leftDir = true;
      }
      if (distance > right.distanceTo(otherLoc) && 
		   right.getX() <= canvas.getWidth())
      {
        distance = right.distanceTo(otherLoc); 
        leftUpDir = false;
        upDir = false;
        rightUpDir = false;
        leftDir = false;
        rightDir = true;
      }
      if (distance > downLeft.distanceTo(otherLoc) && downLeft.getX() >= 0 
		      && downLeft.getY() >= 0)
      {
        distance = downLeft.distanceTo(otherLoc); 
        leftUpDir = false;
        upDir = false;
        rightUpDir = false;
        leftDir = false;
        rightDir = false;
        downLeftDir = true;
      }
      if (distance > down.distanceTo(otherLoc) && down.getY() >= 0)
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
      if (distance > downRight.distanceTo(otherLoc) &&
		      downRight.getX() <= canvas.getHeight() && 
		      downRight.getY() >= 0)
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
      //moves the seeker to the direction with the shortest distance compared 
      //to closest object
      if (leftUpDir) 
      {
        x = -1; 
        y = 1; 
        seekerShape.move(-1,1); 
      }
      else if (upDir) 
      {
        x = 0; 
        y = 1;
        seekerShape.move(0,1); 
      }
      else if (rightUpDir) 
      {
        x = 1; 
        y = 1;
       seekerShape.move(1,1); 
      }
      else if (leftDir) 
      {
        x = -1; 
        y = 0;
        seekerShape.move(-1,0); 
      }
      else if (rightDir) 
      {
        x = 1; 
        y = 0;
        seekerShape.move(1,0); 
      }
      else if (downLeftDir) 
      {
        x = -1; 
        y = -1;
        seekerShape.move(-1,-1); 
      }
      else if (downDir)
      {
        x = 0; 
        y = -1;
        seekerShape.move(0,-1); 
      }
      else if (downRightDir)
      {
        x = 1; 
        y = -1;
        seekerShape.move(1,-1); 
      }

      leftUpDir = false;
      upDir = false;
      rightUpDir = false;
      leftDir = false;
      rightDir = false;
      downLeftDir = false;
      downDir = false;
      downRightDir = false;

      updatedPoint = new Location(seekerShape.getX()+x, seekerShape.getY()+y); 
      this.setLoc(updatedPoint); 

    }
  }
  /*
   * Name: notRun
   * Purpose: This will ensure that the seeker will not move when stop button
   * is clicked. 
   */
  public void notRun()
  {
    seekerShape.move(0,0); //seeker does not move if stop button is clicked
  }


}
