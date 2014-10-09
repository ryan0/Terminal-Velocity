/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ourgame;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.export.JmeImporter;
import com.jme3.export.binary.BinaryImporter;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.screen.DefaultScreenController;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.io.IOException;

/**
 *
 * @author Mark
 */
public class GameMenuController extends SimpleApplication implements ScreenController {
    
    private Nifty nifty;
    
    @Override
    public void simpleInitApp() {
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager,
                                                          inputManager,
                                                          audioRenderer,
                                                          guiViewPort);
        nifty = niftyDisplay.getNifty();
        nifty.fromXml("Interface/GameMenu.xml", "start", this);
        
        // attach the nifty display to the gui view port as a processor
        guiViewPort.addProcessor(niftyDisplay);

        // disable the fly cam
        flyCam.setEnabled(false);
        flyCam.setDragToRotate(true);
        inputManager.setCursorVisible(true);
        
        nifty.registerMusic("Sandstorm", "Sounds/Sandstorm.ogg");
        nifty.addScreen("Game Start", new ScreenBuilder("First Screen"){{
            controller(new DefaultScreenController());
            
            layer(new LayerBuilder("The Layer"){{
                childLayoutVertical();
                
                visibleToMouse(true);
                interactOnClick("playMusic(\"Sounds/Sandstorm.ogg\")");
                
                panel(new PanelBuilder("Image Panel"){{
                    childLayoutCenter();
                    alignCenter();
                    valignCenter();
                    height("80%");
                    backgroundColor("#000f");
                    
                    image(new ImageBuilder(){{
                        height("100%");
                        width("100%");
                        filename("Interface/MainScreen.jpeg");
                        
                    }});
                    
                }});
                
                panel(new PanelBuilder("Button Panel"){{
                    childLayoutCenter();
                    alignCenter();
                    valignCenter();
                    height("20%");
                    backgroundColor("#0000");
                    
                    control(new ButtonBuilder("StartButton","Start"){{
                      height("100%");
                      width("100%");
                      visibleToMouse(true);
                      interactOnClick("");
                    }});
                }});
                
            }});
            
        }}.build(nifty));
        nifty.gotoScreen("Game Start");
    }

    private void playMusic(String songFilePath) throws IOException
    {
        System.out.println("Supposed to play music now");
        AudioNode music = new AudioNode(assetManager, songFilePath);
        rootNode.attachChild(music);
        music.setVolume(2);
        music.play();
        
    }
    
    private void toNextMenu()
    {
        this.stop();
    }
    
    public void bind(Nifty nifty, Screen screen) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void onStartScreen() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void onEndScreen() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
