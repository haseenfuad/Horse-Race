import java.util.concurrent.TimeUnit;
import java.lang.Math;

/**
 * A three-horse race, each horse running in its own lane
 * for a given distance
 * 
 * @version 1.0
 */
public class Race
{
    private int raceLength;
    private Horse lane1Horse;
    private Horse lane2Horse;
    private Horse lane3Horse;

    /**
     * Constructor for objects of class Race
     * Initially there are no horses in the lanes
     * 
     * @param distance the length of the racetrack (in metres/yards...)
     */
    public Race(int distance)
    {
        // initialise instance variables
        raceLength = distance;
        lane1Horse = null;
        lane2Horse = null;
        lane3Horse = null;
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
            System.out.println("Cannot add horse to lane " + laneNumber + " because there is no such lane");
        }
    }

    /**
     * Check if all horses have fallen
     * 
     * @return true if all horses have fallen, false otherwise
     */
    private boolean allHorsesFallen()
    {
        return lane1Horse.hasFallen() && lane2Horse.hasFallen() && lane3Horse != null && lane3Horse.hasFallen();
    }
    
    /**
     * Start the race
     * The horses are brought to the start and
     * then repeatedly moved forward until the 
     * race is finished
     */
    public void startRace()
    {
        boolean finished = false;
        Horse winner = null;
        
        lane1Horse.goBackToStart();
        lane2Horse.goBackToStart();
        if (lane3Horse != null) {
            lane3Horse.goBackToStart();
        }
                      
        while (!finished && !allHorsesFallen())
        {
            moveHorse(lane1Horse);
            moveHorse(lane2Horse);
            if (lane3Horse != null) {
                moveHorse(lane3Horse);
            }   
                        
            printRace();
            
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
           
            try{ 
                TimeUnit.MILLISECONDS.sleep(100);
            }catch(Exception e){}
        }
        if (winner != null) {
            winner.setConfidence(Math.min(1.0, winner.getConfidence() + 0.1));
            System.out.println("And the winner is " + winner.getName());
        }
    }

    /**
     * Randomly make a horse move forward or fall depending
     * on its confidence rating
     * 
     * @param theHorse the horse to be moved
     */
    private void moveHorse(Horse theHorse)
    {
        if (!theHorse.hasFallen())
        {
            if (Math.random() < theHorse.getConfidence())
            {
               theHorse.moveForward();
            }
            
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
        return theHorse != null && theHorse.getDistanceTravelled() == raceLength;
    }
    
    /**
     * Print the race to the console
     */
    private void printRace()
    {
        printLane(lane1Horse);
        printLane(lane2Horse);
        if (lane3Horse != null) {
            printLane(lane3Horse);
        }
        else {
            System.out.println("|" + String.format("%" + (raceLength + 2) + "s", "") + "|");
        }
        System.out.println();
    }
    
    /**
     * print a horse's lane during the race
     */
    private void printLane(Horse theHorse)
    {
        int spacesBefore = theHorse.getDistanceTravelled();
        int spacesAfter = raceLength - spacesBefore;
        
        System.out.print('|');
        
        for (int i = 0; i < spacesBefore; i++) {
            System.out.print(' ');
        }
        
        if (theHorse.hasFallen()) {
            System.out.print('X');
        } else {
            System.out.print(theHorse.getSymbol());
        }
        
        for (int i = 0; i < spacesAfter; i++) {
            System.out.print(' ');
        }
        
        System.out.println('|' + theHorse.getName() + " (Current Confidence " + String.format("%.1f", theHorse.getConfidence()) + ")");
    }
}
