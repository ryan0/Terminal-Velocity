package ourgame;

import com.jme3.app.SimpleApplication;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import java.awt.Dimension;
import java.awt.Toolkit;


public class Main extends SimpleApplication {
    
    public static void main(String[] args) 
    {
        
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        double width = screenSize.getWidth();
//        double height = screenSize.getHeight();
        Main app = new Main();
        AppSettings blah = new AppSettings(true);
//        blah.setResolution((int)width, (int)height);
//        blah.setFullscreen(true);
//        app.setSettings(blah);
//        app.setDisplayFps(false);
//        app.setDisplayStatView(false);
//        app.setShowSettings(false);
        app.start();
    }
 
    @Override
    public void simpleInitApp() 
    {
        flyCam.setEnabled(false);
        //flyCam.setDragToRotate(true);
        
        GuiStateController guiController = new GuiStateController(); 
        stateManager.attach(guiController);
        
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
        niftyDisplay.getNifty().fromXml("Interface/GameMenu.xml", "start", guiController);
        getGuiViewPort().addProcessor(niftyDisplay);
        
    }
 
    @Override
    public void simpleUpdate(float tpf) 
    {
        
    }
 
    @Override
    public void simpleRender(RenderManager rm) 
    {
       
    }
}
