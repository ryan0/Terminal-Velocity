package ourgame;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class LevelScreenController implements ScreenController {
    private String levelSelection = "";
    private Nifty nifty;
    private Screen screen;
    
    public void selectLevel(String x)
    {
        levelSelection = x;
        Element niftyElement = nifty.getCurrentScreen().findElementByName("levelText");
        niftyElement.getRenderer(TextRenderer.class).setText(x+ " Selected");
    }
    
    public void changeScreens(String screenName)
    {
        nifty.gotoScreen(screenName);
    }
    
    public void startLevel()
    {
        if (levelSelection.compareTo("Level One")==0)
        {
            Level gameState = new Level();
            //gameState.initialize(app.getStateManager(), app);
            //app.stop(); 
        }
        else
        {
            Element niftyElement = nifty.getCurrentScreen().findElementByName("levelText");
            niftyElement.getRenderer(TextRenderer.class).setText("Please Select a Level to Continue");
        }
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