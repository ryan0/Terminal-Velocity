package ourgame;

import com.jme3.app.SimpleApplication;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;


public class Main extends SimpleApplication {
    public static void main(String[] args) {
        AppSettings settings = new AppSettings(true);
        settings.setFullscreen(false);
        settings.setResolution(1280,720);
        Main app = new Main();
        app.setShowSettings(false);
        app.setSettings(settings);
        app.start();
    }
    
    @Override
    public void simpleInitApp() {
        setDisplayFps(false);
        setDisplayStatView(false);
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
                assetManager, inputManager, audioRenderer, guiViewPort);
        Nifty nifty = niftyDisplay.getNifty();
        nifty.addXml("Interface/GameMenu.xml");
        nifty.gotoScreen("start");
        
        StartScreenController startScreen = (StartScreenController) nifty.getScreen("start").getScreenController();
        MenuScreenController menuScreen = (MenuScreenController) nifty.getScreen("mainMenu").getScreenController();
        LevelScreenController levelScreen = (LevelScreenController) nifty.getScreen("levelSelect").getScreenController();
        LevelOne levelOne = new LevelOne();
        
        stateManager.attach(startScreen);
        stateManager.attach(menuScreen);
        stateManager.attach(levelScreen);
        //stateManager.attach(levelOne);
        
        guiViewPort.addProcessor(niftyDisplay);
        flyCam.setDragToRotate(true);
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }
    
    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
