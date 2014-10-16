
package ourgame;

import com.jme3.app.Application;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl;

/**
 *
 * @author Ryan
 */
public class Player extends Node
{
    private Spatial mesh;
    private RigidBodyControl physicsControl;
    private AudioNode soundNode;
    
    
    public Player(BulletAppState bulletAppState, Application app)
    {
        initKeys(app.getInputManager());
        setName("Player");
        
        mesh = app.getAssetManager().loadModel("Models/Dude/WIP Dude Frame.obj");
        mesh.setMaterial(new Material(app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md"));
        mesh.rotate(135, 0, -90);
        attachChild(mesh);
        
        CapsuleCollisionShape PlayerShape = new CapsuleCollisionShape(1.5f, 6f, 1);
        physicsControl = new RigidBodyControl(PlayerShape, .05f);
        mesh.addControl(physicsControl);
        bulletAppState.getPhysicsSpace().add(physicsControl);
        bulletAppState.getPhysicsSpace().addCollisionListener(new PlayerPhysicsListener());
        physicsControl.setGravity(new Vector3f(0f, 5*-9.8f, 0f));
        physicsControl.setPhysicsLocation(new Vector3f(-20000, 20000, 5000));
        
        
        
        soundNode = new AudioNode(app.getAssetManager(), "Sounds/scream.ogg", false);
        soundNode.setPositional(false);
        soundNode.setLooping(false);
        attachChild(soundNode);
        
        
        CameraNode camNode = new CameraNode("Camera Node", app.getCamera());
        camNode.setControlDir(CameraControl.ControlDirection.SpatialToCamera);
        attachChild(camNode);
        
        
        ChaseCamera chaseCam = new ChaseCamera(app.getCamera(), camNode, app.getInputManager());
        chaseCam.setDragToRotate(true);
        chaseCam.setMinDistance(15);
        chaseCam.setMaxDistance(30);
        chaseCam.setDefaultDistance(20);
        chaseCam.setEnabled(true);
        chaseCam.setSpatial(mesh);
        
    }
    
    public class PlayerPhysicsListener implements PhysicsCollisionListener 
    {
        public void collision(PhysicsCollisionEvent event) 
        {
            if(event.getNodeA().getName().equals("Player") || event.getNodeB().getName().equals("Player"));
                soundNode.play();
        }
    }
    
    private void initKeys(InputManager inputManager)
    {
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addListener(analogListener,"Up", "Right", "Down", "Left");
    }
    
    private AnalogListener analogListener = new AnalogListener()
    {
        public void onAnalog(String name, float value, float tpf)
        {
            if (name.equals("Right"))
            {
                physicsControl.applyCentralForce(new Vector3f(0, 0, -2));
            }
            else if (name.equals("Up"))
            {
                physicsControl.applyCentralForce(new Vector3f(-2, 0, 0));
            }
            else if (name.equals("Left"))
            {
                physicsControl.applyCentralForce(new Vector3f(0, 0, 2));
            }
            else if (name.equals("Down"))
            {
                physicsControl.applyCentralForce(new Vector3f(2, 0, 0));
            }
        }
    };
}
