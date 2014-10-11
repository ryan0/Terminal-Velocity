package ourgame;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class LevelScreenController extends AbstractAppState implements ScreenController {
    private String levelSelection = "";
    private Nifty nifty;
    private Application app;
    private Screen screen;
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        this.app = app;
    }
    public void selectLevel(String x)
    {
        levelSelection = x;
        Element niftyElement = nifty.getCurrentScreen().findElementByName("levelText");
        niftyElement.getRenderer(TextRenderer.class).setText(x+ " Selected");
    }
    public void quitGame()
    {
        app.stop();
    }
    public void startLevel()
    {
        if (levelSelection.compareTo("Level One")==0)
        {
            //AppSettings sets = new AppSettings(true);
            //sets.setFullscreen(true);
            //sets.setResolution(1280,720);
            LevelOne gameScr = new LevelOne();
            gameScr.setShowSettings(false);
            //gameScr.setSettings(sets);
            gameScr.start();
            
        }
        else
        {
            Element niftyElement = nifty.getCurrentScreen().findElementByName("levelText");
            niftyElement.getRenderer(TextRenderer.class).setText("Please Select a Level to Continue");
        }
        
    }
    @Override
    public void update(float tpf) {
    }
    
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }
    
    public void onStartScreen() {
    }
    
    public void onEndScreen() {
    }
}