/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ourgame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class GuiStateController extends AbstractAppState implements ScreenController
{
    private SimpleApplication app;
    private AppStateManager stateManager;
    
    private Nifty nifty;
    private Screen screen;
    
   
    @Override
    public void initialize(AppStateManager stateManager1, Application dahApp)
    {
        super.initialize(stateManager1, app);
        app = (SimpleApplication)dahApp;
        stateManager = stateManager1;
    }
    
    public void startLevel()
    {
        Level level = new Level();
        stateManager.attach(level);
        nifty.gotoScreen("hud");
    }

    public void changeScreens(String screenName)
    {
        nifty.gotoScreen(screenName);
    }
    
    public void bind(Nifty nifty, Screen screen) 
    {
        this.nifty = nifty;
        this.screen = screen;
    }

    public void onStartScreen() 
    {
        
    }

    public void onEndScreen() 
    {
       
    }
}
