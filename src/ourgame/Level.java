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
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;

public class Level extends AbstractAppState
{
    public static final Quaternion PITCH045 = new Quaternion().fromAngleAxis(FastMath.PI/4,   new Vector3f(1,0,0));
    public static final Quaternion ROLL045  = new Quaternion().fromAngleAxis(FastMath.PI/4,   new Vector3f(0,0,1));
    public static final Quaternion YAW045   = new Quaternion().fromAngleAxis(FastMath.PI/4,   new Vector3f(0,1,0));
    
    public static final Quaternion PITCH001 = new Quaternion().fromAngleAxis(FastMath.PI/180,   new Vector3f(1,0,0));
    public static final Quaternion ROLL001  = new Quaternion().fromAngleAxis(FastMath.PI/180,   new Vector3f(0,0,1));
    public static final Quaternion YAW001   = new Quaternion().fromAngleAxis(FastMath.PI/180,   new Vector3f(0,1,0));
    
    private SimpleApplication app;
    private AppStateManager stateManager;
    
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
        
        
        //coin stuff
        for(int count = 0; count <= 100; count++)
        {
            Coin coin = new Coin(bulletAppState, app,
                    new Vector3f((int)(-21000+FastMath.nextRandomFloat()*6000),
                        (int)(16000+FastMath.nextRandomFloat()*6000),
                        (int)(-13000+FastMath.nextRandomFloat()*6000)),
                    new Vector3f(30,30,30));
            app.getRootNode().attachChild(coin);
        }
        
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
        
        DirectionalLightShadowFilter dlsf;
        dlsf = new DirectionalLightShadowFilter(app.getAssetManager(), SHADOWMAP_SIZE, 4);
        dlsf.setLight(sun);
        dlsf.setLambda(0.55f);
        dlsf.setShadowIntensity(0.6f);
        dlsf.setEdgeFilteringMode(EdgeFilteringMode.PCF4);
        dlsf.setEnabled(true);

        FilterPostProcessor fpp = new FilterPostProcessor(app.getAssetManager());
        fpp.addFilter(dlsf);
        app.getViewPort().addProcessor(fpp);
    }
}
