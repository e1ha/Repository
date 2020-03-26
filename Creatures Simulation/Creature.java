/*
* Filename: Creature.java
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
 * Name: Creature
 * Purpose: This class provides a set of common functions to all the 
 * creature classes.
 */

public abstract class Creature
{
  protected Location loc; 
  protected DrawingCanvas canvas; 
  /*
   * Name: Creature
   * Purpose: The location of the creature and the canvas are passed into
   * this constructor. 
   * Parameters: 
   * @param loc represents location of creature
   * @param canvas represents the canvas
    */
  public Creature (Location loc, DrawingCanvas canvas) 
  {
    this.canvas = canvas; 
    this.loc = loc;
  }

  /*
   * Name: reactTo
   * Purpose: The method will determine how each creature reacts in
   * the simulation. 
   * Parameters: 
   * @param other represents the nearest creature
   */
  public abstract void reactTo(Creature other);

  /*
   * Name: getLoc
   * Purpose: The method saves the location of the creature. 
   * @return loc is the location of the creature
   */
  public Location getLoc()
  {
    return loc; 
  }
  /*
   * Name: setLoc
   * Purpose: The method updates the location of the creature. 
   * @param loc is the location of the creature
   */
  public void setLoc(Location loc)
  {
    this.loc = loc; 
  }
/*
   * Name: notRun
   * Purpose: The method prevents creature from moving when
   * stop button is pressed. 
   */
  public abstract void notRun(); 
 }
