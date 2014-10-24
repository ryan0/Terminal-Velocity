/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ourgame;
import com.jme3.app.Application;
import com.jme3.util.SkyFactory;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;

public class Level extends AbstractAppState implements AnalogListener, ActionListener
{
    private SimpleApplication app;
    private AppStateManager stateManager;
    
    private BulletAppState bulletAppState;
    Player player;
    CameraNode camNode;
    
    AudioNode musicNode;
    
    private Vector3f direction = new Vector3f();
    private boolean rotate = false;
   
    @Override
    public void initialize(AppStateManager stateManager1, Application dahApp)
    {
        
        super.initialize(stateManager1, app);
        app = (SimpleApplication)dahApp;
        stateManager = stateManager1;
        initLight();
        
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        bulletAppState.getPhysicsSpace().setAccuracy(1f/480f);
        
        Terrain terrain = new Terrain(bulletAppState, app);
        app.getRootNode().attachChild(terrain);
        
        player = new Player(bulletAppState, app); 
        
//        camNode = new CameraNode("Camera Node", app.getCamera());
//        camNode.setControlDir(CameraControl.ControlDirection.SpatialToCamera);
//        camNode.setLocalTranslation(player.getLocalTranslation().add(new Vector3f(0, -5, -20)));
//        System.out.println(player.getLocalTranslation().toString());
//        camNode.lookAt(player.getLocalTranslation(), Vector3f.UNIT_Y);
//        camNode.setEnabled(true);
        
        app.getRootNode().attachChild(player);
        
        bulletAppState.getPhysicsSpace().setAccuracy(1f/250f);
        
        musicNode = new AudioNode(app.getAssetManager(), "Sounds/Sandstorm.ogg", false);
        musicNode.setPositional(false);
        musicNode.setLooping(true);
        musicNode.setVolume(.1f);
        musicNode.play();
        
        app.getRootNode().attachChild(musicNode);
        
        registerInput();
    }
    
    @Override
    public void update(float tpf)
    {
        player.update(tpf);
    }
    
    private void initLight()
    {
        app.getCamera().setFrustumFar(100000);
        
        Node skyNode = new Node();
        skyNode.attachChild(SkyFactory.createSky(app.getAssetManager(),
                app.getAssetManager().loadTexture("Textures/Sky/Sky_West.jpg"),
                app.getAssetManager().loadTexture("Textures/Sky/Sky_East.jpg"),
                app.getAssetManager().loadTexture("Textures/Sky/Sky_North.jpg"),
                app.getAssetManager().loadTexture("Textures/Sky/Sky_South.jpg"),
                app.getAssetManager().loadTexture("Textures/Sky/Sky_Up.jpg"),
                app.getAssetManager().loadTexture("Textures/Sky/Sky_Down.jpg")));
        skyNode.setShadowMode(RenderQueue.ShadowMode.Off);
        app.getRootNode().attachChild(skyNode);
        
        AmbientLight lamp = new AmbientLight();
        lamp.setColor(ColorRGBA.White);
        app.getRootNode().addLight(lamp);
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(new Vector3f(1f,-1f, 0f));
        app.getRootNode().addLight(sun);
        
        app.getRootNode().setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        final int SHADOWMAP_SIZE = 4096;
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(app.getAssetManager(), SHADOWMAP_SIZE, 4);
        dlsr.setLight(sun);
        dlsr.setLambda(0.55f);
        dlsr.setShadowIntensity(0.6f);
        dlsr.setEdgeFilteringMode(EdgeFilteringMode.Dither);
        app.getViewPort().addProcessor(dlsr);
        
        //THIS CAN BE USED IN CHANGING THE LINEAR VELOCITY BASED ON CAMERA ANGLE
        //direction.set(app.getCamera().getDirection()).normalizeLocal();
        
//        DirectionalLightShadowFilter dlsf;
//        dlsf = new DirectionalLightShadowFilter(app.getAssetManager(), SHADOWMAP_SIZE, 4);
//        dlsf.setLight(sun);
//        dlsf.setLambda(0.55f);
//        dlsf.setShadowIntensity(0.6f);
//        dlsf.setEdgeFilteringMode(EdgeFilteringMode.PCF4);
//        dlsf.setEnabled(true);
//
//        FilterPostProcessor fpp = new FilterPostProcessor(app.getAssetManager());
//        fpp.addFilter(dlsf);
//        app.getViewPort().addProcessor(fpp);
    }
    
    private void registerInput() {
        app.getInputManager().addMapping("toggleRotate", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        app.getInputManager().addMapping("rotateRight", new MouseAxisTrigger(MouseInput.AXIS_X, true));
        app.getInputManager().addMapping("rotateLeft", new MouseAxisTrigger(MouseInput.AXIS_X, false));
        app.getInputManager().addMapping("rotateUp", new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        app.getInputManager().addMapping("rotateDown", new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        app.getInputManager().addListener(this, "rotateRight", "rotateLeft", "rotateUp", "rotateDown", "toggleRotate");
    }

    public void onAnalog(String name, float value, float tpf) {
        

        if (name.equals("rotateRight") && rotate) {
          player.rotate(0, 5 * tpf, 0);
        }
        if (name.equals("rotateLeft") && rotate) {
          player.rotate(0, -5 * tpf, 0);
        }
        if (name.equals("rotateUp") && rotate) {
          player.rotate(0, 0, 5 * tpf);
        }
        if (name.equals("rotateDown") && rotate) {
          player.rotate(0, 0, -5 * tpf);
        }

    }
    public void onAction(String name, boolean keyPressed, float tpf) {
        //toggling rotation on or off
        if (name.equals("toggleRotate") && keyPressed) {
          rotate = true;
          app.getInputManager().setCursorVisible(false);
        }
        if (name.equals("toggleRotate") && !keyPressed) {
          rotate = false;
          app.getInputManager().setCursorVisible(true);
        }
    }
}
