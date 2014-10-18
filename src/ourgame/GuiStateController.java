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
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class GuiStateController extends AbstractAppState implements ScreenController
{
    private SimpleApplication app;
    private AppStateManager stateManager;
    
    private Nifty nifty;
    private Screen screen;
    
    private String[] HUDs = {"None", "Steel", "Slim Red", "Jungle", "Contrast"};
    private int currentHUD = 1;
    
    private AudioNode clickSound;
    private AudioNode windSound;
    
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
    
    public void startLevel()
    {
        clickSound.playInstance();
        Level level = new Level();
        stateManager.attach(level);
        nifty.gotoScreen("hud");
        windSound.stop();
    }

    public void changeScreens(String screenName)
    {
        if(!nifty.getCurrentScreen().getScreenId().equals("start"))
            clickSound.playInstance();
        else
            windSound.setVolume(.1f);
        nifty.gotoScreen(screenName);
    }
    
    public String getHUD()
    {
        return HUDs[currentHUD];
    }
    
    public void cycleHUD()
    {
        clickSound.playInstance();
        if(currentHUD==HUDs.length-1)
            currentHUD=0;
        else
            currentHUD++;
        
        //change the thumbnail image
        NiftyImage img = nifty.getRenderEngine().createImage(null, "Interface/Images/HUD-"+HUDs[currentHUD]+".png", false);
        Element HudThumbnail = nifty.getCurrentScreen().findElementByName("currentHUD");
        HudThumbnail.getRenderer(ImageRenderer.class).setImage(img);
        
        //change the actuall gameplay HUD image
        Element actualHUD = nifty.getScreen("hud").findElementByName("theHUD");
        actualHUD.getRenderer(ImageRenderer.class).setImage(img);
    }
    
    public void changeRes()
    {
        
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
