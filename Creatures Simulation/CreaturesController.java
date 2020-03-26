/*
* Filename: CreaturesController.java
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
 * Name: CreaturesController
 * Purpose: This class creates canvas, buttons, and labels. It creates 
 * three creatures and controls their behavior.  
 */

public class CreaturesController extends WindowController
				implements ActionListener 
{
  private final static int WINDOW_SIZE = 600; 
  private ActionEvent evt; 
  //creates panels, buttons, and labels,
  private JPanel topPanel1;
  private JPanel topPanel2; 
  private JPanel topPanel;
  private JButton startButton; 
  private JButton stopButton;
  private JButton clearButton;
  private JLabel topLabel; 
  private JLabel bottomLabel;
  private JButton seekerButton; 
  private JButton escaperButton;
  private JButton bouncerButton; 
  //creature class objects
  private Seeker seeker;
  private Escaper escaper; 
  private Bouncer bouncer; 
  //simulation class object
  private CreaturesSimulator moveCreatures;  
  //array to store creature
  public ArrayList<Creature>creatures = new ArrayList<Creature>(); 
  //determines whether start or stop button is clicked
  private boolean runState = true; 
  private int i = 0; 
  //minimum number of creatures that must be in the canvas in order for 
  //simulation to run
  private final static int MIN_CREAT = 2;  
  private int button; //whether seeker, escaper, or bouncer button was clicked
  //identifies which monster was created
  private final static int SEEKER = 1; 
  private final static int ESCAPER = 2;
  private final static int BOUNCER = 3;
  //number of rows and columns in grid layout in panels
  private final static int NUM_ROW_1 = 1;
  private final static int NUM_ROW_2 = 2;  
  private final static int NUM_COL_3= 3;
  private final static int NUM_COL_2 = 2;
  private final static int NUM_COL_1 = 1;

  /*
 * Name: begin
 * Purpose: This creates the labels, buttons, and panels.
 */
  public void begin()
  { //creates the panels, buttons, and labels
    topPanel1 = new JPanel();
    topPanel2 = new JPanel(); 
    topPanel = new JPanel(); 
    topPanel.setLayout(new GridLayout(NUM_ROW_1, NUM_COL_2)); 
    topLabel = new JLabel("Please add two or more creatures.", JLabel.CENTER); 
    startButton = new JButton("Start"); 
    stopButton = new JButton("Stop");
    clearButton = new JButton("Clear"); 
    topPanel1.setLayout(new GridLayout(NUM_ROW_1,NUM_COL_1)); 
    topPanel2.setLayout(new GridLayout(NUM_ROW_1,NUM_COL_3));
    topPanel1.add(topLabel); 
    topPanel2.add(startButton);
    topPanel2.add(stopButton); 
    topPanel2.add(clearButton); 
    topPanel.add(topPanel1); 
    topPanel.add(topPanel2); 
    this.add(topPanel, BorderLayout.NORTH); 

    startButton.addActionListener(this); 
    stopButton.addActionListener(this);  
    clearButton.addActionListener(this); 

    JPanel bottomPanel1 = new JPanel(); 
    JPanel bottomPanel2 = new JPanel();
    JPanel bottomPanel = new JPanel();
    bottomPanel.setLayout(new GridLayout(NUM_ROW_2, NUM_COL_1)); 
    seekerButton = new JButton("Seeker"); 
    escaperButton = new JButton("Escaper");
    bouncerButton = new JButton("Bouncer"); 
    bottomLabel = new JLabel("Select which creature to place: ", 
		              JLabel.CENTER);
    bottomPanel1.add(bottomLabel);
    bottomPanel2.setLayout(new GridLayout(NUM_ROW_1,NUM_COL_3));
    bottomPanel2.add(seekerButton);
    bottomPanel2.add(escaperButton);
    bottomPanel2.add(bouncerButton);
    bottomPanel.add(bottomPanel1);
    bottomPanel.add(bottomPanel2);
    this.add(bottomPanel, BorderLayout.SOUTH);

    seekerButton.addActionListener(this); 
    escaperButton.addActionListener(this);  
    bouncerButton.addActionListener(this); 
    moveCreatures = new CreaturesSimulator(creatures, runState); 

    this.validate(); 
  }
   /*
   * actionPerformed
   * Every time the button is pressed, boolean values will be set and labels
   * will be changed. 
   * @param evt represents ActionEvent  
   */  
  public void actionPerformed(ActionEvent evt)
  { 	  
    this.evt = evt; 
    //if start button is clicked
    if (evt.getSource() == startButton)
    { //change the top label
      runState = true;
	    
      changeLabels();
      moveCreatures.updateConditions(creatures, runState); 
      
    }
    //if stop button is clicked
    else if (evt.getSource() == stopButton)
    {//change the top label
      topLabel.setText("Simulation is stopped.");
      runState = false; 
      moveCreatures.updateConditions(creatures, runState);   
    } //if clear button is clicked
    else if (evt.getSource() == clearButton)
    { //clear the canvas
      canvas.clear(); 
      creatures.clear();
      //change to appropriate labels
      if (runState)
      {
        changeLabels(); 
      }
    } //depending on which creature button is clicked, change the bottom label
    else if (evt.getSource() == seekerButton)
    {
      bottomLabel.setText("Click on canvas to place a Seeker");
      button = SEEKER; 
    }
    else if (evt.getSource() == escaperButton)
    {
      bottomLabel.setText("Click on canvas to place a Escaper"); 
      button = ESCAPER;
    } 
    else if (evt.getSource() == bouncerButton)
    {
      bottomLabel.setText("Click on canvas to place a Bouncer");
      button = BOUNCER;
    }
  }
  /*
   * onMouseClick
   * Depending on whether the seeker, escaper, or bouncer button is clicked
   * canvas will call the constructor in each creature class. The creature
   * will also be added to the creature array.
   * @param point represents location of mouse cursor  
   */  

  public void onMouseClick(Location point)
  { //depending on which button is clicked, create the creature and add it to 
    //an array
    if (button == SEEKER)
    {
      seeker = new Seeker(point, canvas); 
      creatures.add(seeker); 
      moveCreatures.updateConditions(creatures, runState); 
    }
    else if (button == ESCAPER)
    {
      escaper = new Escaper(point, canvas); 
      creatures.add(escaper);
      moveCreatures.updateConditions(creatures, runState); 
    }
    else if (button == BOUNCER)
    {
      bouncer = new Bouncer(point, canvas); 
      creatures.add(bouncer);
      moveCreatures.updateConditions(creatures, runState); 
    }
    changeLabels();
  }
   /*
   * changeLabels
   * Changes labels depending on number of creatures created
   */  
  public void changeLabels()
  {
    if(creatures.size() >= MIN_CREAT && runState)
      //changes top label when two or more creatures are created
      topLabel.setText("Simulation is running.");
    else if (creatures.size() < MIN_CREAT && runState)
      //changes label when less than two creatures are created
      topLabel.setText("Please add two or more creatures."); 
  }
/*
  * main
  * This runs the applet while executing all the above methods.
  * @param String[] args this array passes in the command line arguments 
  * into the main method.
  */
  public static void main(String[] args)
  {
    //runs applet
    new Acme.MainFrame(new CreaturesController(), args, WINDOW_SIZE, 
		        WINDOW_SIZE); 
  }
}
