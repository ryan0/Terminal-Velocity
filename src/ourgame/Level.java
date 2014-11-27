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
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class Level extends AbstractAppState implements ActionListener,ScreenController
{
    private Nifty nifty;
    private Screen screen;
    private int points;
    public static final Quaternion PITCH045 = new Quaternion().fromAngleAxis(FastMath.PI/4,   new Vector3f(1,0,0));
    public static final Quaternion ROLL045  = new Quaternion().fromAngleAxis(FastMath.PI/4,   new Vector3f(0,0,1));
    public static final Quaternion YAW045   = new Quaternion().fromAngleAxis(FastMath.PI/4,   new Vector3f(0,1,0));
    
    public static final Quaternion PITCH001 = new Quaternion().fromAngleAxis(FastMath.PI/180,   new Vector3f(1,0,0));
    public static final Quaternion ROLL001  = new Quaternion().fromAngleAxis(FastMath.PI/180,   new Vector3f(0,0,1));
    public static final Quaternion YAW001   = new Quaternion().fromAngleAxis(FastMath.PI/180,   new Vector3f(0,1,0));
    
    private SimpleApplication app;
    private AppStateManager stateManager;
    
    private AmbientLight lamp;
    private DirectionalLight sun;
    
    private BulletAppState bulletAppState;
    Player player;
    CameraNode camNode;
    
    AudioNode musicNode;
    
    private AudioNode soundNode;
   
    @Override
    public void initialize(AppStateManager stateManager1, Application dahApp)
    {
        super.initialize(stateManager1, app);
        app = (SimpleApplication)dahApp;
        stateManager = stateManager1;
        initLight();
        
        app.getInputManager().setCursorVisible(false);
        
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
        
        Node coinNode = new Node();
        AmbientLight coinLamp = new AmbientLight();
        coinLamp.setColor(ColorRGBA.White.mult(2));
        coinNode.addLight(coinLamp);
        
        //coin stuff
        for(int count = 0; count <= 100; count++)
        {
            Coin coin = new Coin(bulletAppState, app,
                    new Vector3f((int)(-21000+FastMath.nextRandomFloat()*6000),
                        (int)(16000+FastMath.nextRandomFloat()*6000),
                        (int)(-13000+FastMath.nextRandomFloat()*6000)),
                    new Vector3f(30,30,30));
            coinNode.attachChild(coin);
        }
        app.getRootNode().attachChild(coinNode);
                
        musicNode = new AudioNode(app.getAssetManager(), "Sounds/Sandstorm.ogg", false);
        musicNode.setPositional(false);
        musicNode.setLooping(true);
        musicNode.setVolume(.1f);
        musicNode.play();
        
        soundNode = new AudioNode(app.getAssetManager(), "Sounds/scream.ogg", false);
        soundNode.setPositional(false);
        soundNode.setLooping(false);
        app.getRootNode().attachChild(soundNode);
        
        app.getRootNode().attachChild(musicNode);
        registerInput();
    }
    public void setNifty(Nifty nif)
    {
        nifty = nif;
    }
    @Override
    public void update(float tpf)
    {
        player.update(tpf);
        points = player.getPoints();
        Element niftyElement = nifty.getCurrentScreen().findElementByName("points");
        niftyElement.getRenderer(TextRenderer.class).setText("Score: "+points);
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
        
        lamp = new AmbientLight();
        lamp.setColor(ColorRGBA.White);
        app.getRootNode().addLight(lamp);
        sun = new DirectionalLight();
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
        
        DirectionalLightShadowFilter dlsf;
        dlsf = new DirectionalLightShadowFilter(app.getAssetManager(), SHADOWMAP_SIZE, 4);
        dlsf.setLight(sun);
        dlsf.setLambda(0.55f);
        dlsf.setShadowIntensity(0.6f);
        dlsf.setEdgeFilteringMode(EdgeFilteringMode.PCF4);
        dlsf.setEnabled(true);

        FilterPostProcessor fpp = new FilterPostProcessor(app.getAssetManager());
        fpp.addFilter(dlsf);
        
        
        BloomFilter bloom = new BloomFilter(BloomFilter.GlowMode.Objects);
        fpp.addFilter(bloom);
        
        app.getViewPort().addProcessor(fpp);
    }
    public void bind(Nifty nifty, Screen screen) 
    {
        this.nifty = nifty;
        this.screen = screen;
    }
    private void registerInput() {
        app.getInputManager().addMapping("leave", new KeyTrigger(KeyInput.KEY_E));
        app.getInputManager().addListener(this, "leave");
    }


    public void onAction(String name,boolean keyPressed, float tpf) {
        
        if (name.equals("leave")&& nifty!=null) {
            nifty.gotoScreen("gameScreen");            
            stateManager.detach(this);
        }

    }
    public void cleanup()
    {
        app.getInputManager().setCursorVisible(true);
        player.cleanup();
        app.getRootNode().detachAllChildren();
        app.getRootNode().removeLight(sun);
        app.getRootNode().removeLight(lamp);
        musicNode.stop();
        app.getViewPort().clearProcessors();
    }
     public void onStartScreen() 
    {
        
    }

    public void onEndScreen() 
    {
       
    }
}
