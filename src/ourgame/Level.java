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
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FogFilter;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;

public class Level extends AbstractAppState{
    private SimpleApplication app;
    private AppStateManager stateManager;
    
    private BulletAppState bulletAppState;
    Player player;
   
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
        app.getRootNode().attachChild(player);
        bulletAppState.getPhysicsSpace().setAccuracy(1f/500f);
        
        AudioNode soundNode = new AudioNode(app.getAssetManager(), "Sounds/Sandstorm.ogg", false);
        soundNode.setPositional(false);
        soundNode.setLooping(true);
        soundNode.setVolume(.1f);
        soundNode.play();
        
        app.getRootNode().attachChild(soundNode);
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
}
