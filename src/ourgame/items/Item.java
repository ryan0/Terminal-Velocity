/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ourgame.items;

/**
 *
 * @author Mark
 */
public abstract class Item 
{
    protected String ID;
    protected int price;
    
    @Override
    public String toString()
    {
        return ID;
    }
    
    public static Item fromString(String string)
    {
        if(string.equals("Fuzzy Slippers"))
            return new FuzzySlippers();
        else if (string.equals("Balloon"))
            return new Balloon();
        else
            return null;
    }
    
    public int getPrice()
    {
        return price;
    }
    
    public String getID()
    {
        return ID;
    }
}
