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
        if(string.equals("fuzzy slippers"))
            return new FuzzySlippers();
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
