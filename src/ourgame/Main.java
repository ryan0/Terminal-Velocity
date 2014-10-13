package ourgame;

import com.jme3.app.SimpleApplication;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;


public class Main extends SimpleApplication {
    
    public static void main(String[] args) 
    {
        Main app = new Main();
        app.start();
    }
 
    @Override
    public void simpleInitApp() 
    {
        flyCam.setDragToRotate(true);
        
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
