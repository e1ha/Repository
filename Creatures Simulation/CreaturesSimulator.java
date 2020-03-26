/*
* Filename: CreatureSimulator.java
* Author: Elaine Ha
* UserId: cs11s219bo
* Date: 8/29/19
* Sources of help: tutor, textbook
*/
import objectdraw.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*; 
/*
 * Name: CreaturesSimulator
 * Purpose: This class creates the simulation that allows the creatures
 * to move. 
 */

public class CreaturesSimulator extends ActiveObject
{ //this array stores all the creatures
  private ArrayList<Creature> creatures; 
  //this is the null creature to fill in the parameter for the closest 
  //creature for the bouncer 
  private Creature otherCreature; 
  //creature that we will be comparing the closest creatures to
  private Creature aCreature;
  //saves the shortest distance for the closest creature for the escaper
  //and the seeker
  private double shortDistance = Double.MAX_VALUE; 
  private double shortDistance1 = Double.MAX_VALUE;
  //closest creature for escaper and seeker respectively
  private Creature closestCreature; 
  private Creature closestCreature1; 
  //whether simulation is ran or paused
  private boolean run; 
  //index number of closest creature for seeker
  private int k; 
  //minimum number of creatures
  private static final int MIN_CREAT = 2;
  private static final int DELAY_TIME = 40; 
   /*
   * Name: CreaturesSimulator
   * Purpose: This method sends intial creatures and the run state before 
   * the start button is pressed. 
   * Parameters: 
   * @param creatures is the array of creatures
   * @param runState is the boolean to determine whether start or stop 
   * button is pressed
   */

  public CreaturesSimulator(ArrayList<Creature> creatures, boolean runState)
  {
    this.creatures = creatures; //passes in the array of creatures
    run = runState; //whether start or stop button is pressed
    start();
  }
 /*
   * Name: updateConditions
   * Purpose: This method updates the array and the run state. 
   * Parameters: 
   * @param creatures is the array of creatures
   * @param runState is the boolean to determine whether start or stop 
   * button is pressed
   */
  public void updateConditions(ArrayList<Creature> creatures, boolean runState)
  {
    this.creatures = creatures; //updates creature array
    run = runState; //updates whether start or stop button is pressed
  }
   /*
   * Name: run
   * Purpose: This method controls the simulation. 
   */

  public void run()
  {
    while(true)
    { //if at least two creatures have been created  
      if (creatures.size() >= MIN_CREAT && run)	
      {
        for (int i = 0; i < creatures.size(); i++)
        { //resets the short distance values
	  shortDistance = Double.MAX_VALUE; 
	  shortDistance1 = Double.MAX_VALUE;
	  //creature that we will be comparing closest creature to
          aCreature = creatures.get(i); 
  
	  if (aCreature instanceof Bouncer && run) //if creature is bouncer
	  { //bouncer will move in the way it is supposed to
	    aCreature.reactTo(otherCreature); 	
	  }
	  else if (aCreature instanceof Escaper && run) //if it is escaper
	  { //determines shortest distance and closest creature to escaper            
	    for (int j = 0; j < creatures.size(); j++)
	    {
	      if (i != j && shortDistance > 
	        creatures.get(i).getLoc().distanceTo(creatures.get(j).getLoc())
	       	&& run) 	    
	      { 	
                shortDistance = 
	        creatures.get(i).getLoc().distanceTo(creatures.get(j).getLoc());
                closestCreature = creatures.get(j); 
	      }
	      else if (!run) //if stop button is pressed
	      {
                break; 
	      }
	    }
	    if (!run) //if stop button is pressed
	    {
	      for (int l = 0; l < creatures.size(); l++)
              {
	        creatures.get(l).notRun(); //stop all creatures from moving 
	      }
	    }
            else //if stop is not pressed
	    {//escaper will move in the way it is supposed to
	      aCreature.reactTo(closestCreature); 
	    }
	  }//if creature is seeker
	  else if (aCreature instanceof Seeker && run)
          { //determine shortest distance and closest creature to seeker
    	    for (k = 0; k < creatures.size(); k++)
            {
	      if (i != k && shortDistance1 > 
	       creatures.get(i).getLoc().distanceTo(creatures.get(k).getLoc()) 
	       && !(creatures.get(k) instanceof Seeker) && run) 			         
	      { 				
	        shortDistance1 = 
	        creatures.get(i).getLoc().distanceTo(creatures.get(k).getLoc())
		;
	        closestCreature1 = creatures.get(k); 
	      }
	      else if (!run) //if stop button is pressed
	      {
	        break;
	      }
            }
	    if (!run) //if stop button is pressed
	    {
	      for (int l = 0; l < creatures.size(); l++)
	      {
                creatures.get(l).notRun(); //stop all creatures from moving  
	      }
	    }
            else 
	    { //seeker will move in the way it is supposed to
	      aCreature.reactTo(closestCreature1);
	    }
          }	
        }
      }
      pause(DELAY_TIME); 
    }
  }
}
