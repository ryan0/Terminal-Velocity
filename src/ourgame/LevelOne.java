/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ourgame;
import com.jme3.util.SkyFactory;
import com.jme3.app.SimpleApplication;
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
    private BulletAppState bulletAppState;
    private RigidBodyControl terrainControl;
    private RigidBodyControl playerControl;
    
    public void simpleInitApp()
    {   
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
        
        
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        
        Spatial terrain = assetManager.loadModel("Models/Terrain/Terrain.j3o");
        Material theGround = new Material (assetManager, "Common/MatDefs/Light/Lighting.j3md");
        theGround.setTexture("DiffuseMap", assetManager.loadTexture("Textures/GroundStuffs.png"));
        terrain.setMaterial(theGround);
        terrain.setLocalScale(new Vector3f(1000f, 1000f, 1000f));
        
        CollisionShape terrainShape = CollisionShapeFactory.createMeshShape(terrain);
        terrainControl = new RigidBodyControl(terrainShape, 0);
        terrain.addControl(terrainControl);
        rootNode.attachChild(terrain);
        
        
        Node pNode = new Node("pNode");
        Spatial player;
        player = assetManager.loadModel("Models/Dude/WIP Dude Frame.obj");
        Material skinAndClothes = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
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
        
        ChaseCamera chaseCam = new ChaseCamera(cam, camNode, inputManager);
        chaseCam.setDragToRotate(false);
        chaseCam.setMinDistance(15);
        chaseCam.setMaxDistance(30);
        chaseCam.setDefaultDistance(20);
        chaseCam.setEnabled(true);
        chaseCam.setSpatial(player);
        
        rootNode.attachChild(pNode);
        
        bulletAppState.getPhysicsSpace().add(terrainControl);
        bulletAppState.getPhysicsSpace().add(playerControl);
        
        initKeys();
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
