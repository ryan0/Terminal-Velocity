/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ourgame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.audio.AudioNode;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyMethodInvoker;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JOptionPane;

public class GuiStateController extends AbstractAppState implements ScreenController
{
    private float time = 0;
    private int points = 0;
    private int currencyEarned = 0;
    private int pointsCounted = 0;
    private int currencyCounted = 0;
    private boolean gameEndScreenOne = false;
    private boolean gameEndScreenTwo = false;
    private SimpleApplication app;
    private AppStateManager stateManager;
    
    private PlayerData saveData;
    private String fileName;
    
    private Nifty nifty;
    private Screen screen;
    private String selectedLevel;
    private Level level1;
    private Level2 level2;
    
    private String[] HUDs = {"None", "Steel", "Slim Red", "Jungle", "Contrast", "Prints", "Goggles"};
    
    private AudioNode clickSound;
    private AudioNode windSound;
    
    private boolean fullScreen = false;
    private Dimension res = new Dimension(640,480);
    private Dimension maxScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
    
    
    @Override
    public void initialize(AppStateManager stateManager1, Application dahApp)
    {
        super.initialize(stateManager1, app);
        app = (SimpleApplication)dahApp;
        stateManager = stateManager1;
        
        clickSound = new AudioNode(app.getAssetManager(), "Sounds/Click.wav", false);
        clickSound.setPositional(false);
        clickSound.setLooping(false);
        clickSound.setVolume(.5f);
        app.getRootNode().attachChild(clickSound);
        
        windSound = new AudioNode(app.getAssetManager(), "Sounds/Wind.wav", false);
        windSound.setPositional(false);
        windSound.setLooping(true);
        windSound.setVolume(.3f);
        app.getRootNode().attachChild(windSound);
        windSound.play();
    }
    @Override
    //gives time
    public void update(float tpf)
    {
        time += tpf;
        if (gameEndScreenOne)
        {
            if (currencyCounted <currencyEarned)
            {
                currencyCounted++;
                updateCurrency(currencyCounted);
            }
            else
            {
                gameEndScreenOne = false;
                gameEndScreenTwo = true;
                time = 0;
            }
        }
        else if (gameEndScreenTwo)
        {
            if (time>5.0)
            {
                pointsCounted = 0;
                currencyCounted = 0;
                updatePoints(0);
                updateCurrency(0);
                updateShopCurrency();
                Element scrElement = nifty.getCurrentScreen().findElementByName("container");
                scrElement.getElementInteraction().setOnMouseOver(new NiftyMethodInvoker(nifty,"gameHasEnded()",this));
                gameEndScreenTwo = false;
                changeScreens("gameScreen");
            }
        }
        
    }
    /**
     * Creates a <code>Level</code> appState and attaches it
     * to the <code>stateManager</code>. 
     */
    public void selectLevel(String levelName)
    {
        selectedLevel = levelName;
    }
    
    public void startLevel()
    {
        clickSound.playInstance();
        if(selectedLevel.equals("level1"))
        {
        level1 = new Level();
        level1.setNifty(nifty);
        stateManager.attach(level1);
        nifty.gotoScreen("hud");
        windSound.stop();
        }
        if(selectedLevel.equals("level2"))
        {
        level2 = new Level2();
        level2.setNifty(nifty);
        stateManager.attach(level2);
        nifty.gotoScreen("hud");
        windSound.stop();
        }
    }

    public void changeScreens(String screenName)
    {   
        if(!nifty.getCurrentScreen().getScreenId().equals("start")&&!nifty.getCurrentScreen().getScreenId().equals("gameEnded"))
            clickSound.playInstance();
        else if (nifty.getCurrentScreen().getScreenId().equals("start"))
            windSound.setVolume(.1f);
        
        if(screenName.equals("settings"))
        {
            NiftyImage img = nifty.getRenderEngine().createImage(null, "Interface/Images/HUD-"+HUDs[saveData.getHUD()]+".png", false);
            Element HudThumbnail = nifty.getScreen("settings").findElementByName("currentHUD");
            HudThumbnail.getRenderer(ImageRenderer.class).setImage(img);
        }
        nifty.gotoScreen(screenName);
    }
    
    public void updateShopCurrency()
    {
        Element niftElem = nifty.getScreen("shopMenu").findElementByName("shopCurrency");
        String curr = "Currency: "+saveData.getCurrency();
        niftElem.getRenderer(TextRenderer.class).setText(curr);
    }
    
    public boolean makeNewSave()
    {
        saveData = new PlayerData();
        saveData.setCurrency(0);
        saveData.setHUD(1);
        fileName = JOptionPane.showInputDialog(null, "Enter save name");
        saveData.save("saves/"+fileName+".ryansucks");
        SaveManager.recordSave(fileName);
        changeScreens("gameScreen");
        return true;
    }
    
    public boolean loadSave()
    {
        saveData = new PlayerData();
        fileName = JOptionPane.showInputDialog(null, "Enter save name");
        boolean success = saveData.load("saves/"+fileName+".sav");
        if(success)
        {
            changeScreens("gameScreen");
        }
        return success;
    }
    
    public String getHUD()
    {
        return HUDs[saveData.getHUD()];
    }
    
    public void cycleHUD()
    {
        clickSound.playInstance();
        
        int currentHUD = saveData.getHUD();
        if(currentHUD==HUDs.length-1)
            saveData.setHUD(0);
        else
            saveData.setHUD(currentHUD+1);
        saveData.save("saves/"+fileName+".sav");
        //change the thumbnail image
        NiftyImage img = nifty.getRenderEngine().createImage(null, "Interface/Images/HUD-"+HUDs[saveData.getHUD()]+".png", false);
        Element HudThumbnail = nifty.getCurrentScreen().findElementByName("currentHUD");
        HudThumbnail.getRenderer(ImageRenderer.class).setImage(img);
        
        //change the actuall gameplay HUD image
        Element actualHUD = nifty.getScreen("hud").findElementByName("theHUD");
        actualHUD.getRenderer(ImageRenderer.class).setImage(img);
    }
    
    public void changeRes(String resValues)
    {
        AppSettings newSettings = new AppSettings(true);
        //Retrieve the individual values from the string "widthxheight"
        String [] XAndY = resValues.split("x");
        int x = Integer.parseInt(XAndY[0]);
        int y = Integer.parseInt(XAndY[1]);
        newSettings.setResolution(x, y);
        
        clickSound.playInstance();
        
        //Make sure that we don't get a runtime error due
        //to the resolution being to large for fullscreen
        
        //(Making the computer try to squeeze more pixels
        //than it has makes it say "DERP")
        if(fullScreen)
        {
            if(maxScreenSize.width>=x && maxScreenSize.height>=y)
            {
                res.setSize(x, y);
                newSettings.setFullscreen(fullScreen);
                app.setSettings(newSettings);
                app.restart();
            }
        }
        //Don't perform the check if it isn't fullscreen
        else
        {
            res.setSize(x, y);
            newSettings.setFullscreen(fullScreen);
            app.setSettings(newSettings);
            app.restart();
        }
        
    }
    public void doNothing()
    {
        
    }
    public void gameHasEnded()
    {
        Element scrElement = nifty.getCurrentScreen().findElementByName("container");
        scrElement.getElementInteraction().setOnMouseOver(new NiftyMethodInvoker(nifty,"doNothing()",this));
        if(selectedLevel.equals("level1")) points = level1.getPoints();
        if(selectedLevel.equals("level2")) points = level2.getPoints();
        updatePoints(points);
        currencyEarned = points/10;
        saveData.setCurrency(saveData.getCurrency()+currencyEarned);
        saveData.save("saves/"+fileName+".sav");
        gameEndScreenOne = true;
        windSound.play();
    }
    public void updatePoints(int val)
    {
        Element niftyElement = nifty.getCurrentScreen().findElementByName("points");
        niftyElement.getRenderer(TextRenderer.class).setText("Points: "+ val);
    }
    public void updateCurrency(int val)
    {
        Element niftyElement = nifty.getCurrentScreen().findElementByName("currency");
        niftyElement.getRenderer(TextRenderer.class).setText("Currency: "+ val);
    }
    public void toggleFullScreen()
    {
        
        clickSound.playInstance();
        
        //Perform the same maximum screen size check as above when changing resolution
        if(!(res.width>maxScreenSize.width || res.height>maxScreenSize.height))
        {
            AppSettings newSettings = new AppSettings(true);
            if(fullScreen)
            {
                newSettings.setFullscreen(false);
                fullScreen = false;
            }
            else
            {
                newSettings.setFullscreen(true);
                fullScreen = true;
            }
            newSettings.setResolution(res.width, res.height);
            newSettings.setFullscreen(fullScreen);
            app.setSettings(newSettings);
            app.restart();
        }
    }
    
    public void bind(Nifty nifty, Screen screen) 
    {
        this.nifty = nifty;
        this.screen = screen;
    }

    public void quit()
    {
        clickSound.playInstance();
        app.stop();
    }
    
    public void onStartScreen() 
    {
        
    }

    public void onEndScreen() 
    {
       
    }
}


