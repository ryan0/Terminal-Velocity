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
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.util.TangentBinormalGenerator;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class Level2 extends AbstractAppState implements ActionListener,ScreenController
{
    private Nifty nifty;
    private Screen screen;
    private int points = 0;
    
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
        
        
        
//        Node terrain = new Node("terrain");
//        Spatial mesh = app.getAssetManager().loadModel("Models/Terrain/terrain.obj");
//
//        TangentBinormalGenerator.generate(mesh);
//        Material mat = new Material (app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
//        mat.setTexture("DiffuseMap", app.getAssetManager().loadTexture("Textures/TerrainTexture.png"));
//        mat.setTexture("NormalMap", app.getAssetManager().loadTexture("Textures/TerrainNormalMap.png"));
//        mesh.setMaterial(mat);
//        mesh.setLocalScale(new Vector3f(100000f, 100000f, 100000f));
//
//        
//        CollisionShape terrainShape = CollisionShapeFactory.createMeshShape(mesh);
//        RigidBodyControl physicsControl = new RigidBodyControl(terrainShape, 0);
//        mesh.addControl(physicsControl);
//        terrain.attachChild(mesh);
//        bulletAppState.getPhysicsSpace().add(mesh);
        
        Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Terrain/TerrainLighting.j3md");
        mat.setTexture("AlphaMap", app.getAssetManager().loadTexture("Textures/terrain/alphaMap.png"));

        Texture grass = app.getAssetManager().loadTexture("Textures/terrain/images/grass.png");
        grass.setWrap(Texture.WrapMode.Repeat);
        mat.setTexture("DiffuseMap", grass);
        mat.setFloat("DiffuseMap_0_scale", 128f);

        Texture midRock = app.getAssetManager().loadTexture("Textures/terrain/images/midRock.png");
        midRock.setWrap(Texture.WrapMode.Repeat);
        mat.setTexture("DiffuseMap_2", midRock);
        mat.setFloat("DiffuseMap_2_scale", 32f);
        
        Texture rock = app.getAssetManager().loadTexture("Textures/terrain/images/rock.png");
        rock.setWrap(Texture.WrapMode.Repeat);
        mat.setTexture("DiffuseMap_1", rock);
        mat.setFloat("DiffuseMap_1_scale", 64f);


        Texture grassNormalMap = app.getAssetManager().loadTexture("Textures/terrain/images/grassNormalMap.png");
        grassNormalMap.setWrap(WrapMode.Repeat);
        Texture midRockNormalMap = app.getAssetManager().loadTexture("Textures/terrain/images/midRockNormalMap.png");
        midRockNormalMap.setWrap(WrapMode.Repeat);
        Texture rockNormalMap = app.getAssetManager().loadTexture("Textures/terrain/images/rockNormalMap.png");
        rockNormalMap.setWrap(WrapMode.Repeat);
        mat.setTexture("NormalMap", grassNormalMap);
        mat.setTexture("NormalMap_1", rockNormalMap);
        mat.setTexture("NormalMap_2", midRockNormalMap);
        
        AbstractHeightMap heightmap;
        Texture heightMapImage = app.getAssetManager().loadTexture("Textures/terrain/heightMap.png");
        heightmap = new ImageBasedHeightMap(heightMapImage.getImage());
        heightmap.load();

        int patchSize = 65;
        TerrainQuad terrain = new TerrainQuad("my terrain", patchSize, 257, heightmap.getHeightMap());

        terrain.setMaterial(mat);
        terrain.setLocalScale(2000f, 500f, 2000f);
        terrain.setLocalTranslation(-50000f, -94000f, -40000);

        TerrainLodControl control = new TerrainLodControl(terrain, app.getCamera());
        terrain.addControl(control);
        
        terrain.addControl(new RigidBodyControl(0));
        bulletAppState.getPhysicsSpace().add(terrain);
        
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
        
        if(Math.random()>.66)
            musicNode = new AudioNode(app.getAssetManager(), "Sounds/Sandstorm.ogg", false);
        else
            if(Math.random()<.33)
            musicNode = new AudioNode(app.getAssetManager(), "Sounds/Nightcore_Clip.wav", false);
            else
                musicNode = new AudioNode(app.getAssetManager(), "Sounds/Epic Music Clip.wav", false);
               
        
        musicNode.setPositional(false);
        musicNode.setLooping(true);
        musicNode.setVolume(.1f);
        musicNode.play();
        app.getRootNode().attachChild(musicNode);
        
        soundNode = new AudioNode(app.getAssetManager(), "Sounds/scream.ogg", false);
        soundNode.setPositional(false);
        soundNode.setLooping(false);
        app.getRootNode().attachChild(soundNode);
        
        
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
        if (player.getHitGround() == true)
        {
            gameEnded();
        }
        //System.out.println("Points: " +points);
    }
    public int getPoints()
    {
        return points;
    }
    private void initLight()
    {
        app.getCamera().setFrustumFar(1000000);
        
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
        lamp.setColor(ColorRGBA.White.mult(.2f));
        app.getRootNode().addLight(lamp);
        sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White.mult(2f));
        sun.setDirection(new Vector3f(0f,-1f, -2f));
        app.getRootNode().addLight(sun);
        
        
        app.getRootNode().setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        final int SHADOWMAP_SIZE = 4096;
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(app.getAssetManager(), SHADOWMAP_SIZE, 4);
        dlsr.setLight(sun);
        dlsr.setLambda(0.55f);
        dlsr.setShadowIntensity(0.6f);
        dlsr.setEdgeFilteringMode(EdgeFilteringMode.PCFPOISSON);
        app.getViewPort().addProcessor(dlsr);
        
        

        FilterPostProcessor fpp = new FilterPostProcessor(app.getAssetManager());      
        
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
            gameEnded();
        }

    }
    public void gameEnded()
    {
        nifty.gotoScreen("gameEnded");
        stateManager.detach(this);
    }
    @Override
    public void cleanup()
    {
        System.out.println("Cleanup");
        super.cleanup();
        app.getInputManager().setCursorVisible(true);
        player.cleanup();
        app.getRootNode().detachAllChildren();
        app.getRootNode().removeLight(sun);
        app.getRootNode().removeLight(lamp);
        musicNode.stop();
        bulletAppState.cleanup();
        //bulletAppState.getPhysicsSpace().destroy(); <----- This line of code causes a null pointer exception, kept here just because
        app.getViewPort().clearProcessors();
        app.getInputManager().clearMappings();
    }
     public void onStartScreen() 
    {
        
    }

    public void onEndScreen() 
    {
       
    }
}