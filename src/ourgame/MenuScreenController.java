package ourgame;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class MenuScreenController implements ScreenController {
    private Nifty nifty;
    private Screen screen;

    public void changeScreens(String screenName)
    {
        nifty.gotoScreen(screenName);
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