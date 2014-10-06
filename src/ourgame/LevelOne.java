/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ourgame;
import com.jme3.util.SkyFactory;
import com.jme3.app.SimpleApplication;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl.ControlDirection;

public class LevelOne extends SimpleApplication{
    public static void main(String [] args)
    {
        LevelOne app = new LevelOne();
        app.start();
    }
    private Spatial player;
    
    double g = -9.8;
    double yVel = 0;
    int terminalVel = 100;
    
    public void simpleInitApp()
    {
        flyCam.setEnabled(false);
        
        rootNode.attachChild(SkyFactory.createSky(assetManager, 
                assetManager.loadTexture("Textures/Sky/Sky_West.jpg"), 
                assetManager.loadTexture("Textures/Sky/Sky_East.jpg"), 
                assetManager.loadTexture("Textures/Sky/Sky_North.jpg"), 
                assetManager.loadTexture("Textures/Sky/Sky_South.jpg"), 
                assetManager.loadTexture("Textures/Sky/Sky_Up.jpg"), 
                assetManager.loadTexture("Textures/Sky/Sky_Down.jpg")));
        
        AmbientLight lamp = new AmbientLight();
        lamp.setColor(ColorRGBA.White);
        rootNode.addLight(lamp);
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(new Vector3f(0,-1,0));
        rootNode.addLight(sun);
        
        Node pNode = new Node("pNode");
        player = assetManager.loadModel("Models/Dude/WIP Dude Frame.obj");
        Material skinAndClothes = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        player.setMaterial(skinAndClothes);
        
        player.rotate(135, 0, -90);
        pNode.attachChild(player);
        
        
        CameraNode camNode = new CameraNode("Camera Node", cam);
        camNode.setControlDir(ControlDirection.SpatialToCamera);
        pNode.attachChild(camNode);
        
        ChaseCamera chaseCam = new ChaseCamera(cam, camNode, inputManager);
        chaseCam.setSpatial(player);
        chaseCam.setMinDistance(10);
        chaseCam.setMaxDistance(20);
        chaseCam.setDefaultDistance(15);
        chaseCam.setEnabled(true);
        
        rootNode.attachChild(pNode);
        
        
        Spatial terrain = assetManager.loadModel("Models/Terrain/Terrain.j3o");
        Material theGround = new Material (assetManager, "Common/MatDefs/Light/Lighting.j3md");
        theGround.setTexture("DiffuseMap", assetManager.loadTexture("Textures/GroundStuffs.png"));
        terrain.setMaterial(theGround);
        terrain.setLocalScale(new Vector3f(500f,200f,500f));
        terrain.setLocalTranslation(new Vector3f(0,-900,0));
        rootNode.attachChild(terrain);
        initKeys();
    }
    @Override
    public void simpleUpdate (float tpf)
    {
        player.move(0,(int)(yVel*tpf),0);
        yVel += g*tpf;
        if(yVel>terminalVel)
            yVel = terminalVel;
        
    }
    private void initKeys()
    {
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_LEFT));
        
        inputManager.addListener(analogListener,"Up", "Right", "Down", "Left");
    }
    private AnalogListener analogListener = new AnalogListener()
    {
        public void onAnalog(String name, float value, float tpf)
        {
            Vector3f v = player.getLocalTranslation();
            if (name.equals("Down"))
            {
                player.setLocalTranslation(v.x, v.y,v.z-50*tpf);
            }     
            else if (name.equals("Right"))
            {
                player.setLocalTranslation(v.x-50*tpf,v.y,v.z);
            }
            else if (name.equals("Up"))
            {
                player.setLocalTranslation(v.x,v.y,v.z+50*tpf);
            }
            else if (name.equals("Left"))
            {
                player.setLocalTranslation(v.x+50*tpf,v.y, v.z);
            }
        }
    };
    
}
