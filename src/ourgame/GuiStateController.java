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
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import ourgame.items.*;

public class GuiStateController extends AbstractAppState implements ScreenController
{
    private float time = 0;
    private float points = 0;
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
    private int selectedUpgrade=0;
    private Level level;
    
    private String[] HUDs = {"None", "Steel", "Slim Red", "Jungle", "Contrast", "Prints", "Goggles"};
    
    private AudioNode clickSound;
    private AudioNode windSound;
    
    private boolean fullScreen = false;
    private Dimension res = new Dimension(640,480);
    private Dimension maxScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
    
    
    /**
     * 
     * @param stateManager1 - allows the continuous execution of the main code 
     * @param dahApp - instance of the application which will be executed
     */
    @Override
        public void initialize(AppStateManager stateManager1, Application dahApp)
        /* Initializes the application
         * "Turns off" the two sounds that would occur during the menu display of the game
         */    
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
    
    /**
     * The method receives one float value and assigns it to a private global variable.
     * 
     * @param tpf - a float standing for the time per frame
     */
    @Override
    //gives time
    public void update(float tpf)
    /* Displays one of two end screens (depending on a safe or unsafe landing) with the earned currency visible
     * The first indicates that the base jumper did not land safely, and their currency is updated according to the most recent jump.
     * The second indicates that the player has passed the level, returning the points to 0 and increasing the shop currency by however much was earned in the last base jump
     * 
     */
    {
        time += tpf;
        if (nifty.getCurrentScreen().getScreenId().equals("gameEnded")&&!gameEndScreenOne&&!gameEndScreenTwo)
        {
            gameHasEnded();
        }
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
                //Element scrElement = nifty.getCurrentScreen().findElementByName("container");
                //scrElement.getElementInteraction().setOnMouseOver(new NiftyMethodInvoker(nifty,"gameHasEnded()",this));
                gameEndScreenTwo = false;
                Element el = nifty.getCurrentScreen().findElementByName("Congrats");
                el.getRenderer(TextRenderer.class).setText("Congrats, you have finished level one!");
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
    /* Loads the selected level (terrain, HUD skin, ends wind audio) along with any items the user previously bought
     * 
     * 
     */
    {
        clickSound.playInstance();
        System.out.println("In startLevel()");
        if(selectedLevel.equals("level1"))
        {
            System.out.println("Selected level = level one");
            setupHud();
            System.out.println("Setup HUD finished");
            level = new Level("Textures/terrain", saveData.getItems());
            System.out.println("Level instaniated");
            level.setNifty(nifty);
            stateManager.attach(level);
            System.out.println("Level attached");
            nifty.gotoScreen("hud");
            System.out.println("Changed screen to HUD");
            windSound.stop();
            System.out.println("Stopped wind sound");
        }
        if(selectedLevel.equals("level2"))
        {
            level = new Level("Textures/island", saveData.getItems());
            level.setNifty(nifty);
            stateManager.attach(level);
            nifty.gotoScreen("hud");
            windSound.stop();
        }
    }
    public void setupHud()
    //initializes all the possible items in the shop and displays their respective icons      
    {
        ArrayList<Item> itemList = saveData.getItems();
        System.out.println("Setting up HUD");
        for (Item i:itemList)
        {
            System.out.println("Iterating through itemList");
            if(i!=null)
            {
                if(i.toString()==null)
                {
                    System.out.println("THE ITEM'S TOSTRING IS NULL!");
                }
                else
                    System.out.println(i.toString());
                if (i.toString().equals("Balloon"))
                {
                    System.out.println("That item is a balloon");
                    Element hudBalloonIcon = nifty.getScreen("hud").findElementByName("balloon");
                    hudBalloonIcon.show();
                }
                else if (i.toString().equals("Fuzzy Slippers"))
                {
                    System.out.println("That item is slippers");
                    Element hudSlippersIcon = nifty.getScreen("hud").findElementByName("slippers");
                    hudSlippersIcon.show();
                }
                else if (i.toString().equals("Magnet"))
                {
                    System.out.println("That item is a magnet");
                    Element hudMagnetIcon = nifty.getScreen("hud").findElementByName("magnet");
                    hudMagnetIcon.show();
                }
                else if (i.toString().equals("Bunch of Balloons"))
                {
                    System.out.println("That item is a BOB");
                    Element hudBOBIcon = nifty.getScreen("hud").findElementByName("bunch");
                    hudBOBIcon.show();
                }
                else System.out.println("That item is a ??????????");
            }
            else
            {
                System.out.println("THE ITEM IS NULL!");
            }
        }
    }
    /**
     * 
     * @param screenName - a string which represents the various strings within the menus before starting gameplay 
     */
    public void changeScreens(String screenName)
    //allows navigation through the different screens before starting a level
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
        nifty.getScreen("makeNewGame").findElementByName("tField").hide();
        nifty.getScreen("makeNewGame").findElementByName("confirm").hide();
        nifty.getScreen("makeNewGame").findElementByName("middleText").hide();
        nifty.getScreen("loadGame").findElementByName("tField").hide();
        nifty.getScreen("loadGame").findElementByName("confirm").hide();
        nifty.getScreen("loadGame").findElementByName("middleText").hide();
        nifty.getScreen("shopMenu").findElementByName("buyIt").hide();
        nifty.getScreen("hud").findElementByName("balloon").hide();
        nifty.getScreen("hud").findElementByName("slippers").hide();
        nifty.getScreen("hud").findElementByName("bunch").hide();
        nifty.getScreen("hud").findElementByName("magnet").hide();
    }
    
    public void updateShopCurrency()
    // Updates the currency amount within the shop from the saved information        
    {
        Element niftElem = nifty.getScreen("shopMenu").findElementByName("shopCurrency");
        String curr = "Currency: "+saveData.getCurrency();
        niftElem.getRenderer(TextRenderer.class).setText(curr);
    }
    
    public boolean makeNewSave()
    //the user can save his game (with all initial settings such as no currency) under any inputed String (no blank saves)
    {
        saveData = new PlayerData();
        saveData.setCurrency(0);
        saveData.setHUD(1);
        TextField file = nifty.getCurrentScreen().findNiftyControl("saveInput",TextField.class);
        fileName = file.getText();
        if (!fileName.equals(""))
        {
            saveData.save("saves/"+fileName+".sav");
            SaveManager.recordSave(fileName);
            changeScreens("gameScreen");
            return true;
        }
        
        return false;
    }
    
    public boolean loadSave()
    //Allows the user to continue a previous game, retaining items and earned currency
    {
        saveData = new PlayerData();
        TextField file = nifty.getCurrentScreen().findNiftyControl("loadInput",TextField.class);
        fileName = file.getText();
        boolean success = saveData.load("saves/"+fileName+".sav");
        if(success)
        {
            changeScreens("gameScreen");
        }
        updateShopCurrency();
        return success;
    }
    public void unhideElem()
    //Allows access to menu features such as text fields (for user input)
    {
        nifty.getCurrentScreen().findElementByName("tField").show();
        nifty.getCurrentScreen().findElementByName("confirm").show();
        nifty.getCurrentScreen().findElementByName("middleText").show();
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
    /**
     * This method receives a String variable and splits it to set the resolution of the screen
     * 
     * @param resValues - a string variable that represents the resolution of the screen (for example, 800x600) that will be split at the x
     */
    public void changeRes(String resValues)
    //Allows the user to change the resolution of the window
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
    public void gameHasEnded()
    /* Accessed either when the character crashes and dies or when they successfully complete a level
     * If the level is completed successfully, the currency earned is calculated and credited to the user's save file
     */
    {
        //Element scrElement = nifty.getCurrentScreen().findElementByName("container");
        //scrElement.getElementInteraction().setOnMouseOver(new NiftyMethodInvoker(nifty,"doNothing()",this));
        points = level.getPoints();
        if (level.getDeath())
        {
            //you have failed
            windSound.play();
            gameEndScreenTwo = true;
            time = 0;
            Element el = nifty.getCurrentScreen().findElementByName("Congrats");
            el.getRenderer(TextRenderer.class).setText("You have failed and Died!");
        }
        else
        {
            updatePoints(points);
            currencyEarned = (int) (points/10);
            saveData.setCurrency(saveData.getCurrency()+currencyEarned);
            saveData.save("saves/"+fileName+".sav");
            gameEndScreenOne = true;
            windSound.play();
        }
    }

    public void updatePoints(float val)
    //Updates the points for the user to see
    {
        Element niftyElement = nifty.getCurrentScreen().findElementByName("points");
        niftyElement.getRenderer(TextRenderer.class).setText("Points: "+ (int) val);
    }
    public void updateCurrency(int val)
    //Updates the currency for the user to see
    {
        Element niftyElement = nifty.getCurrentScreen().findElementByName("currency");
        niftyElement.getRenderer(TextRenderer.class).setText("Currency: "+ val);
    }
    public void buyTheUpgrade()
    //Removes an amount of currency equivalent to the price of an item from the user's currency when an item is bought
    {
        //subtracts money from currency
        //updates currency in save file and gui
        //updates items in save file
        int currentCurrency = saveData.getCurrency();
        //Item upgrade = new Item();
        if (selectedUpgrade==1)
        {
            currentCurrency = currentCurrency-300;
            saveData.setCurrency(currentCurrency);
            saveData.addItem(new Balloon());
        }
        else if(selectedUpgrade==2)
        {
            currentCurrency = currentCurrency-450;
            saveData.setCurrency(currentCurrency);
            saveData.addItem(new Magnet());
        }
        else if(selectedUpgrade==3)
        {
            currentCurrency = currentCurrency-600;
            saveData.setCurrency(currentCurrency);
            saveData.addItem(new BunchOfBalloons());
        }
        else if(selectedUpgrade==4)
        {
            currentCurrency = currentCurrency-250;
            saveData.setCurrency(currentCurrency);
            saveData.addItem(new FuzzySlippers());
        }
        updateShopCurrency();
        saveData.save("saves/"+fileName+".sav");
    }
    public void selectUpgrade(String upgrade)
    //Allows the user to pick specifically which upgrade he will purchase
    {
        int upgradeNum = Integer.parseInt(upgrade);
        if (upgradeNum==1&&(saveData.getCurrency()>=300))
        {
            nifty.getCurrentScreen().findElementByName("buyIt").show();
            selectedUpgrade = 1;
        }
        else if (upgradeNum==2&&(saveData.getCurrency()>=450))
        {
            nifty.getCurrentScreen().findElementByName("buyIt").show();
            selectedUpgrade = 2;
        }
        else if (upgradeNum==3&&(saveData.getCurrency()>=600))
        {
            nifty.getCurrentScreen().findElementByName("buyIt").show();
            selectedUpgrade = 3;
        }
        else if (upgradeNum==4&&(saveData.getCurrency()>=250))
        {
            nifty.getCurrentScreen().findElementByName("buyIt").show();
            selectedUpgrade = 4;
        }
        else
        {
            nifty.getCurrentScreen().findElementByName("buyIt").hide();
        }
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


