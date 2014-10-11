package ourgame;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class MenuScreenController extends AbstractAppState implements ScreenController {
    private Nifty nifty;
    private Application app;
    private Screen screen;
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        this.app = app;
    }
    public void changeScreens(String screenName)
    {
        nifty.gotoScreen(screenName);
    }
    
    @Override
    public void update(float tpf) {
    }
    
    @Override

    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }
    
    public void onStartScreen() {
    }
    
    public void onEndScreen() {
    }
}