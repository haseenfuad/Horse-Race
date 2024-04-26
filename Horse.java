/**
 * Write a description of class Horse here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Horse
{
    //Fields of class Horse
    private String horseName;
    private char horseSymbol;
    private int distanceTravelled;
    private boolean fallen;
    private double horseConfidence;
    //Constructor of class Horse
    /**
     * Constructor for objects of class Horse
     */
    public Horse(char horseSymbol, String horseName, double horseConfidence)
    {
        this.horseSymbol = horseSymbol;
        this.horseName = horseName;
        this.horseConfidence = Math.min(Math.max(horseConfidence, 0.0), 1.0);
        this.distanceTravelled = 0;
        this.fallen = false;
    }
    
    //Other methods of class Horse
    public void fall()
    {
        this.fallen = true;
        this.horseConfidence = Math.max(0.0, this.horseConfidence - 0.1);
    }
    
    public double getConfidence()
    {
        return horseConfidence;
    }
    
    public int getDistanceTravelled()
    {
        return distanceTravelled;
    }
    
    public String getName()
    {
        return horseName;
    }
    
    public char getSymbol()
    {
        return horseSymbol;
    }
    
    public void goBackToStart()
    {
        this.distanceTravelled = 0;
        this.fallen = false;
    }
    
    public boolean hasFallen()
    {
        return fallen;
    }

    public void moveForward()
    {
        if (!fallen)
        {
            distanceTravelled++;
        }
    }

    public void setConfidence(double newConfidence)
    {
        this.horseConfidence = Math.min(Math.max(newConfidence, 0.0), 1.0);
    }
    
    public void setSymbol(char newSymbol)
    {
        this.horseSymbol = newSymbol;
    }
    
}
