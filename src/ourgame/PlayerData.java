/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ourgame;

import ourgame.items.Item;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Mark
 */
public class PlayerData 
{
    private int currency;
    private int currentHUD;
    private ArrayList<Item> itemList = new ArrayList<Item>();
    
    public boolean save(String fileName)
    {
        //write to the text file with the current variables
        try 
        {
            BufferedWriter outputStream = new BufferedWriter(new FileWriter(fileName));
            outputStream.write(currency+"");
            outputStream.newLine();
            outputStream.write(currentHUD+"");
            outputStream.newLine();
            for(Item item:itemList)
            {
                outputStream.write(item.toString()+"|");
            }
            outputStream.close();
            return true;
        }
        catch (IOException ex1) 
        {
            return false;
        }
    }
    
    public boolean load(String fileName)
    {
        try 
        {
            BufferedReader inputStream = new BufferedReader(new FileReader(fileName));
            //set the values to what is read
            setCurrency(Integer.parseInt(inputStream.readLine()));
            setHUD(Integer.parseInt(inputStream.readLine()));
            String itemString = inputStream.readLine();
            itemList.clear();
            //Separate each of the items
            if(itemString!=null)
            {
                String[] loadedItemList = itemString.split("|");
                for(String item:loadedItemList)
                {
                    itemList.add(Item.fromString(item));
                }
            }
            inputStream.close();
            return true;
        }
        catch (FileNotFoundException ex) 
        {
            return false;
        }
        catch (IOException ex) 
        {
            return false;
        }
    }
    
    public int getCurrency()
    {
        return currency;
    }
    
    public void setCurrency(int newValue)
    {
        currency = newValue;
    }
    
    public int getHUD()
    {
        return currentHUD;
    }
    
    public void setHUD(int theHUD)
    {
        currentHUD = theHUD;
    }
    
    public void addItem(Item item)
    {
        itemList.add(item);
    }
}
