/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ourgame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.niftygui.NiftyJmeDisplay;

public class GuiStateController extends AbstractAppState
{
    private SimpleApplication app;
    private AppStateManager stateManager;
    
   
    @Override
    public void initialize(AppStateManager stateManager1, Application dahApp)
    {
        super.initialize(stateManager1, app);
        app = (SimpleApplication)dahApp;
        stateManager = stateManager1;
        
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
                app.getAssetManager(), 
                app.getInputManager(), 
                app.getAudioRenderer(), 
                app.getGuiViewPort());
        niftyDisplay.getNifty().fromXml("Interface/GameMenu.xml", "start");
        app.getGuiViewPort().addProcessor(niftyDisplay);
    }
}
