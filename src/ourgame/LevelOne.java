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
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.system.AppSettings;

public class LevelOne extends AbstractAppState{
    
    private SimpleApplication app;
    private AppStateManager stateManager;
    private BulletAppState bulletAppState;
    private RigidBodyControl terrainControl;
    private RigidBodyControl playerControl;
    private Camera cam;
   
    @Override
    public void initialize(AppStateManager stateManager1, Application dahApp)
    {
        super.initialize(stateManager1, app);
        app = (SimpleApplication)dahApp;
//        stateManager1.detach(stateManager1.getState(LevelScreenController.class));
//        stateManager1.detach(stateManager1.getState(StartScreenController.class));
//        stateManager1.detach(stateManager1.getState(MenuScreenController.class));
        
        
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
        sun.setDirection(new Vector3f(0,-1,0));
        app.getRootNode().addLight(sun);
        
        app.getRootNode().setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        final int SHADOWMAP_SIZE = 1028;
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(app.getAssetManager(), SHADOWMAP_SIZE, 3);
        dlsr.setLight(sun);
        //dlsr.setLambda(0.55f);
        dlsr.setShadowIntensity(0.6f);
        //dlsr.setEdgeFilteringMode(EdgeFilteringMode.Nearest);
        app.getViewPort().addProcessor(dlsr);
        
        /*
         * DirectionalLightShadowFilter dlsf;
         * dlsf = new DirectionalLightShadowFilter(app.getAssetManager(), SHADOWMAP_SIZE, 3);
         * dlsf.setLight(sun);
         * dlsf.setLambda(0.55f);
         * dlsf.setShadowIntensity(0.6f);
         * dlsf.setEdgeFilteringMode(EdgeFilteringMode.Nearest);
         * dlsf.setEnabled(false);
         * 
         * FilterPostProcessor fpp = new FilterPostProcessor(app.getAssetManager());
         * fpp.addFilter(dlsf);
         * */
        
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        
        Spatial terrain = app.getAssetManager().loadModel("Models/Terrain/Terrain.j3o");
        Material theGround = new Material (app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        theGround.setTexture("DiffuseMap", app.getAssetManager().loadTexture("Textures/GroundStuffs.png"));
        terrain.setMaterial(theGround);
        terrain.setLocalScale(new Vector3f(1000f, 1000f, 1000f));
        
        CollisionShape terrainShape = CollisionShapeFactory.createMeshShape(terrain);
        terrainControl = new RigidBodyControl(terrainShape, 0);
        terrain.addControl(terrainControl);
        app.getRootNode().attachChild(terrain);
        
        
        Node pNode = new Node("pNode");
        Spatial player;
        player = app.getAssetManager().loadModel("Models/Dude/WIP Dude Frame.obj");
        Material skinAndClothes = new Material(app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        player.setMaterial(skinAndClothes);
        player.setLocalTranslation(0, 1000, 0);
        player.rotate(135, 0, -90);
        
        CapsuleCollisionShape PlayerShape = new CapsuleCollisionShape(1.5f, 6f, 1);
        playerControl = new RigidBodyControl(PlayerShape, .05f);
        playerControl.setGravity(new Vector3f(0f, 9.8f, 0f));
        //playerControl.setPhysicsLocation(new Vector3f(0f, 10000f, 0f));
        player.addControl(playerControl);
        
        pNode.attachChild(player);
        
        CameraNode camNode = new CameraNode("Camera Node", cam);
        camNode.setControlDir(ControlDirection.SpatialToCamera);
        pNode.attachChild(camNode);
        
        ChaseCamera chaseCam = new ChaseCamera(cam, camNode, app.getInputManager());
        chaseCam.setDragToRotate(false);
        chaseCam.setMinDistance(15);
        chaseCam.setMaxDistance(30);
        chaseCam.setDefaultDistance(20);
        chaseCam.setEnabled(true);
        chaseCam.setSpatial(player);
        
        app.getRootNode().attachChild(pNode);
        
        bulletAppState.getPhysicsSpace().add(terrainControl);
        bulletAppState.getPhysicsSpace().add(playerControl);
        
        initKeys();
    }
    
    private void initKeys()
    {
        app.getInputManager().addMapping("Up", new KeyTrigger(KeyInput.KEY_UP));
        app.getInputManager().addMapping("Right", new KeyTrigger(KeyInput.KEY_RIGHT));
        app.getInputManager().addMapping("Down", new KeyTrigger(KeyInput.KEY_DOWN));
        app.getInputManager().addMapping("Left", new KeyTrigger(KeyInput.KEY_LEFT));
        app.getInputManager().addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        app.getInputManager().addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        app.getInputManager().addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        app.getInputManager().addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        app.getInputManager().addListener(analogListener,"Up", "Right", "Down", "Left");
    }
    private AnalogListener analogListener = new AnalogListener()
    {
        public void onAnalog(String name, float value, float tpf)
        {
            if (name.equals("Right"))
            {
                playerControl.applyCentralForce(new Vector3f(0, 0, -2));
            }
            else if (name.equals("Up"))
            {
                playerControl.applyCentralForce(new Vector3f(-2, 0, 0));
            }
            else if (name.equals("Left"))
            {
                playerControl.applyCentralForce(new Vector3f(0, 0, 2));
            }
            else if (name.equals("Down"))
            {
                playerControl.applyCentralForce(new Vector3f(2, 0, 0));
            }
        }
    };
    
}
