import java.util.concurrent.TimeUnit;
import javax.swing.JTextArea;
import java.lang.Math;

/**
 * A three-horse race, each horse running in its own lane
 * for a given distance
 * 
 * @author McFarewell
 * @version 1.0
 */
public class Race
{
    private int raceLength;
    private Horse lane1Horse;
    private Horse lane2Horse;
    private Horse lane3Horse;
    private JTextArea GUI;

    /**
     * Constructor for objects of class Race
     * Initially there are no horses in the lanes
     * 
     * @param distance the length of the racetrack (in metres/yards...)
     */
    public Race(int distance, JTextArea newGUI)
    {
        // initialise instance variables
        raceLength = distance;
        lane1Horse = null;
        lane2Horse = null;
        lane3Horse = null;
        GUI = newGUI;
    }
    
    /**
     * Adds a horse to the race in a given lane
     * 
     * @param theHorse the horse to be added to the race
     * @param laneNumber the lane that the horse will be added to
     */
    public void addHorse(Horse theHorse, int laneNumber)
    {
        if (laneNumber == 1)
        {
            lane1Horse = theHorse;
        }
        else if (laneNumber == 2)
        {
            lane2Horse = theHorse;
        }
        else if (laneNumber == 3)
        {
            lane3Horse = theHorse;
        }
        else
        {
            GUI.append("Cannot add horse to lane " + laneNumber + " because there is no such lane");
        }
    }

    /**
     * Check if all horses have fallen
     * 
     * @return true if all horses have fallen, false otherwise
     */
    private boolean allHorsesFallen()
    {
        if (lane1Horse.hasFallen() && lane2Horse.hasFallen() && (lane3Horse != null && lane3Horse.hasFallen()))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * Start the race
     * The horse are brought to the start and
     * then repeatedly moved forward until the 
     * race is finished
     */
    public void startRace()
    {
        //declare a local variable to tell us when the race is finished
        boolean finished = false;
        Horse winner = null;
        
        //reset all the lanes (all horses not fallen and back to 0). 
        lane1Horse.goBackToStart();
        lane2Horse.goBackToStart();
        if (lane3Horse != null) { // Check if lane3Horse is not null
            lane3Horse.goBackToStart();
        }
                      
        while (!finished && !allHorsesFallen())
        {
            //move each horse
            moveHorse(lane1Horse);
            moveHorse(lane2Horse);
            if (lane3Horse != null) { // Check if lane3Horse is not null
                moveHorse(lane3Horse);
            }   
                        
            //print the race positions
            printRace();
            
            //if any of the three horses has won the race is finished
            if (raceWonBy(lane1Horse)){
                finished = true;
                winner = lane1Horse;
            }
            else if (raceWonBy(lane2Horse)){
                finished = true;
                winner = lane2Horse;
            }
            else if (lane3Horse != null && raceWonBy(lane3Horse)){
                finished = true;
                winner = lane3Horse;
            }
           
            //wait for 100 milliseconds
            try{ 
                TimeUnit.MILLISECONDS.sleep(100);
            }catch(Exception e){}
        }
        if (winner != null) {
            winner.setConfidence(Math.min(1.0, winner.getConfidence() + 0.1));
            printRace();
            GUI.append("And the winner is " + winner.getName());
        }
    }
    
    /**
     * Randomly make a horse move forward or fall depending
     * on its confidence rating
     * A fallen horse cannot move
     * 
     * @param theHorse the horse to be moved
     */
    private void moveHorse(Horse theHorse)
    {
        //if the horse has fallen it cannot move, 
        //so only run if it has not fallen
        
        if  (!theHorse.hasFallen())
        {
            //the probability that the horse will move forward depends on the confidence;
            if (Math.random() < theHorse.getConfidence())
            {
               theHorse.moveForward();
            }
            
            //the probability that the horse will fall is very small (max is 0.1)
            //but will also will depends exponentially on confidence 
            //so if you double the confidence, the probability that it will fall is *2
            if (Math.random() < (0.1*theHorse.getConfidence()*theHorse.getConfidence()))
            {
                theHorse.fall();
            }
        }
    }
        
    /** 
     * Determines if a horse has won the race
     *
     * @param theHorse The horse we are testing
     * @return true if the horse has won, false otherwise.
     */
    private boolean raceWonBy(Horse theHorse)
    {
        if (theHorse != null && theHorse.getDistanceTravelled() == raceLength)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /***
     * Print the race on the terminal
     */
    private void printRace()
    {
        GUI.setText("");  //clear the GUI
        
        multiplePrint('=',raceLength/2); //top edge of track
        GUI.append("\n");
        
        printLane(lane1Horse);
        GUI.append("\n");
        
        printLane(lane2Horse);
        GUI.append("\n");
        
        if (lane3Horse != null) { // Check if lane3Horse is not null
            printLane(lane3Horse);
            GUI.append("\n");
        }
        else { // If lane3Horse is null, print an empty lane
            GUI.append(String.format("|%" + (raceLength+2) + "s|\n", ""));
        }
        
        multiplePrint('=',raceLength/2); //bottom edge of track
        GUI.append("\n");    
    }
    
    /**
     * print a horse's lane during the race
     * for example
     * |           X                      |
     * to show how far the horse has run
     */
    private void printLane(Horse theHorse)
    {
        //calculate how many spaces are needed before
        //and after the horse
        int spacesBefore = theHorse.getDistanceTravelled();
        int spacesAfter = raceLength - theHorse.getDistanceTravelled();
        
        //print a | for the beginning of the lane
        GUI.append('|'+"");
        
        //print the spaces before the horse
        multiplePrint(' ',spacesBefore);
        
        //if the horse has fallen then print dead
        //else print the horse's symbol
        if(theHorse.hasFallen())
        {
            GUI.append('X'+"");
        }
        else
        {
            GUI.append(theHorse.getSymbol()+"");
        }

        //print the spaces after the horse
        multiplePrint(' ',spacesAfter);
        
        //print the | for the end of the track
        GUI.append('|'+"");
        GUI.append(theHorse.getName() + " (Current Confidence " + String.format("%.1f", theHorse.getConfidence()) + ")");
    }
        
    
    /***
     * print a character a given number of times.
     * e.g. printmany('x',5) will print: xxxxx
     * 
     * @param aChar the character to Print
     */
    private void multiplePrint(char aChar, int times)
    {
        int i = 0;
        while (i < times)
        {
            GUI.append(aChar+"");
            i = i + 1;
        }
    }
}
