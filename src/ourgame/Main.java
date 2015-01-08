package ourgame;

import com.jme3.app.SimpleApplication;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import java.awt.Dimension;
import java.awt.Toolkit;


public class Main extends SimpleApplication {
    
    public static void main(String[] args)
    //Creates an instance of the application and starts to run it
    {
        Main app = new Main();
        AppSettings newSettings = new AppSettings(true);
        
        //Comment out the following 9 lines for debugging purposes
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        double width = screenSize.getWidth();
//        double height = screenSize.getHeight();
//        newSettings.setResolution((int)width, (int)height);
//        newSettings.setFullscreen(false);
//        app.setSettings(newSettings);
//        app.setDisplayFps(false);
//        app.setDisplayStatView(false);
//        app.setShowSettings(false);
        
        app.start();
    }
 
    @Override
    public void simpleInitApp() 
    //Accesses assets, inputs, audio, the xml files for the menus, and the GUI
    {
        flyCam.setEnabled(false);
        //flyCam.setDragToRotate(true);
        
        GuiStateController guiController = new GuiStateController(); 
        stateManager.attach(guiController);
        
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
        niftyDisplay.getNifty().fromXml("Interface/GameMenu.xml", "start", guiController);
        getGuiViewPort().addProcessor(niftyDisplay);
        
//        PlayerData data = new PlayerData();
//        data.addItem(new FuzzySlippers());
//        data.addItem(new FuzzySlippers());
//        data.setCurrency(1450);
//        data.setHUD(4);
//        data.save("SAVE FILE.txt");
        
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
