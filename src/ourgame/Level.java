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
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.ArrayList;
import ourgame.items.Item;

public class Level extends AbstractAppState implements ActionListener,ScreenController
{
    private Nifty nifty;
    private Screen screen;
    private float points = 0;
    
    private SimpleApplication app;
    private AppStateManager stateManager;
    private String assetFolder;
    
    private boolean death = false;
    private AmbientLight lamp;
    private DirectionalLight sun;
    
    private BulletAppState bulletAppState;
    Player player;
    CameraNode camNode;
    
    AudioNode musicNode;
    
    private AudioNode soundNode;
    
    private boolean hasBalloon = false;
    private boolean hasFuzzySlippers= false;
    private boolean hasMagnet = false;
    private boolean hasBOB = false;
    private Node coinNode;
    private Coin [] coinList = new Coin[101];
    public Level(String assetFolder, ArrayList<Item> items)
    {
        this.assetFolder = assetFolder;
        for(Item item: items)
        {
            if(item.getID().equals("Fuzzy Slippers"))
                hasFuzzySlippers = true;
            else if(item.getID().equals("Balloon"))
                hasBalloon = true;
            else if (item.getID().equals("Magnet"))
                hasMagnet = true;
            else if (item.getID().equals("Bunch of Balloons"))
            {
                hasBOB = true;
            }
        }
    }
    
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
        bulletAppState.getPhysicsSpace().setAccuracy(1f/256f);
        
        
        Terrain terrain = new Terrain(bulletAppState, app, assetFolder);
        app.getRootNode().attachChild(terrain);
        
        
        player = new Player(bulletAppState, app);
        player.sendItems(hasBalloon, hasFuzzySlippers,hasMagnet,hasBOB);
        app.getRootNode().attachChild(player);
        
        
        coinNode = new Node();
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
            coinList[count]=coin;
            coinNode.attachChild(coin);
        }
        app.getRootNode().attachChild(coinNode);
        
        double rand = Math.random();
        if(rand>.75)
            musicNode = new AudioNode(app.getAssetManager(), "Sounds/Sandstorm.ogg", false);
        else
        {
            if(rand<.25)
                musicNode = new AudioNode(app.getAssetManager(), "Sounds/Nightcore_Clip.wav", false);
            else
            {
                if(rand<.5)
                    musicNode = new AudioNode(app.getAssetManager(), "Sounds/Epic Music Clip.wav", false);
                else
                    musicNode = new AudioNode(app.getAssetManager(), "Sounds/Don't Crash!_by_Michael_Dillon.wav", false); 
            }
                
        }
            
               
        
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
        if (hasMagnet)
        {
            for (Coin c: coinList)
            {
                Spatial s = c.getMesh();
                double hyp = Math.pow(Math.pow((c.getMesh().getWorldTranslation().x-player.getWorldTranslation().x),2)//radius from coin to player
                            +Math.pow((c.getMesh().getWorldTranslation().y-player.getWorldTranslation().y),2)
                            +Math.pow((c.getMesh().getWorldTranslation().z-player.getWorldTranslation().z),2),0.5);
                if (hyp<=1000)
                {
                    c.setForce(player.getWorldTranslation().x-c.getMesh().getWorldTranslation().x,
                            player.getWorldTranslation().y-c.getMesh().getWorldTranslation().y,
                            player.getWorldTranslation().z-c.getMesh().getWorldTranslation().z);
                }
                else
                {
                    c.setVelZero();
                }
            }
        }
        player.update(tpf);
        points = player.getPoints();
        Element niftyElement = nifty.getCurrentScreen().findElementByName("points");
        niftyElement.getRenderer(TextRenderer.class).setText("Score: "+ (int) points);
        if (player.getHitGround() == true||player.getDeath()==true)
        {
            if (player.getDeath()==true)
            {
                death = true;
            }
            gameEnded();
        }
        //System.out.println("Points: " +points);
    }
    public boolean getDeath()
    {
        return death;
    }
    public float getPoints()
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
            death = true;
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
