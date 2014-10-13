package ourgame;

import com.jme3.app.SimpleApplication;
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
        stateManager.attach(new GuiStateController());
        stateManager.attach(new Level());
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
