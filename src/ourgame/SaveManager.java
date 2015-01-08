/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ourgame;

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
public class SaveManager 
{
    
    /**
     * 
     * @param fileName - the String variable the user inputs to access a previous game
     */
    public static void recordSave(String fileName)
    //Writes a new file name to savefiles.list in a new ine
    {
        try 
        {
            ArrayList<String> currentSaves = getSaves();
            
            BufferedWriter outputStream = new BufferedWriter(new FileWriter("savefiles.list"));
            for(String save:currentSaves)
            {
                if(save!=null)
                {
                    outputStream.write(save);
                    outputStream.newLine();
                }
            }
            outputStream.write(fileName);
            outputStream.newLine();
            outputStream.close();
        }
        catch (IOException ex1) 
        {
            
        }
    }
    
    
    public static ArrayList<String> getSaves()
    //Load the save files' names from the list into the ArrayList
    {
        ArrayList<String> saveFiles = new ArrayList<String>();
        try 
        {
            BufferedReader inputStream = new BufferedReader(new FileReader("savefiles.list"));
            String line = inputStream.readLine();
            while(line!=null)
            {
                saveFiles.add(line);
                line = inputStream.readLine();
            }
            inputStream.close();
        }
        catch (FileNotFoundException ex) 
        {
            
        }
        catch (IOException ex) 
        {
            
        }
        return saveFiles;
    }
}
