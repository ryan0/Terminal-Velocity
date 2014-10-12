package ourgame;

import com.jme3.app.Application;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;


public class Main extends Application {
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }
    
    @Override
    public void start(JmeContext.Type contextType)
    {
        AppSettings settings = new AppSettings(true);
        settings.setResolution(1024, 768);
        setSettings(settings);
        
        super.start(contextType);
    }
    
    @Override
    public void initialize()
    {
        super.initialize();
        
        //LevelOne state = new LevelOne();
        //stateManager.attach(state);
        
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager,
                                                           inputManager,
                                                           audioRenderer,
                                                           guiViewPort);
        niftyDisplay.getNifty().fromXml("Interface/GameMenu.xml", "start");
        
        guiViewPort.addProcessor(niftyDisplay);
    }
    
    @Override
    public void update(){
        super.update();

        // do some animation
        float tpf = timer.getTimePerFrame();

        stateManager.update(tpf);
        stateManager.render(renderManager);

        // render the viewports
        renderManager.render(tpf, context.isRenderable());
    }

    @Override
    public void destroy(){
        super.destroy();

        System.out.println("Destroy");
    }
}
