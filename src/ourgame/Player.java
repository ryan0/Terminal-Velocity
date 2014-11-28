
package ourgame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl;

/**
 * A <code>Player</code> is a node which includes a
 * mesh and the mechanics for the actual falling
 * character.
 * 
 * @author Ryan
 */
public class Player extends Node implements AnalogListener, ActionListener
{
    private Spatial mesh;
    private RigidBodyControl physicsControl;
    private AudioNode soundNode;
    private SimpleApplication app;
    private Camera cam;
    private Node pivotNode;
    private CameraNode camNode;

    private int points = 0;
    
    BulletAppState bullS;
    
    /**
     * Creates a node containing a player <code>mesh</code> and adds
     * it to a specific <code>Application</code> and <code>bulletAppState</code>.
     * 
     * @param bulletAppState
     * @param appRef
     */
    public Player(BulletAppState bulletAppState, Application appRef)
    {
        bullS = bulletAppState;
        setName("Player");
        
        app = (SimpleApplication)appRef;
        cam = app.getCamera();
        mesh = app.getAssetManager().loadModel("Models/Dude/WIP Dude Frame.obj");
        mesh.setMaterial(new Material(app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md"));
        
        Quaternion rotation = new Quaternion();
        rotation.fromAngles(0f, FastMath.PI, -FastMath.PI/2f);
        mesh.setLocalRotation(rotation);
        this.attachChild(mesh);
        
        BoxCollisionShape PlayerShape = new BoxCollisionShape(new Vector3f(1.5f, 6f, 1));
        physicsControl = new RigidBodyControl(PlayerShape, .05f);
        addControl(physicsControl);
        bullS.getPhysicsSpace().add(physicsControl);
        bullS.getPhysicsSpace().addCollisionListener(new PlayerPhysicsListener());
        physicsControl.setAngularDamping(.999f);
        physicsControl.setRestitution(0);
        physicsControl.setGravity(new Vector3f(0f, 2*-9.8f, 0f));
        physicsControl.setPhysicsLocation(new Vector3f(-18000, 22000, -10000));
        
        
        soundNode = new AudioNode(app.getAssetManager(), "Sounds/scream.ogg", false);
        soundNode.setPositional(false);
        soundNode.setLooping(false);
        this.attachChild(soundNode);
        
        pivotNode = new Node("Pivot");
        pivotNode.setLocalRotation(rotation);
        
        camNode = new CameraNode("Camera Node", cam);
        camNode.setControlDir(CameraControl.ControlDirection.SpatialToCamera);
        camNode.setEnabled(true);
        pivotNode.attachChild(camNode);
        this.attachChild(pivotNode);
        camNode.setLocalTranslation(new Vector3f(-3, -20, 0));
        camNode.lookAt(this.getLocalTranslation(), Vector3f.UNIT_Y);
        setLocalTranslation(new Vector3f(-18000, 23000, -10000));
        
        registerInput();
    }
    public int getPoints()
    {
        return points;
    }
    public void update(float tpf)
    {
        points++;
        //update the velocity crap
        Vector3f xPlusZ = new Vector3f(
                cam.getDirection().x, 
                0, 
                cam.getDirection().z);
        xPlusZ.normalize();
        
        Vector3f angularV = new Vector3f(
                xPlusZ.x * physicsControl.getLinearVelocity().y * -2,
                physicsControl.getLinearVelocity().y,
                xPlusZ.z * physicsControl.getLinearVelocity().y * -2);
        physicsControl.setLinearVelocity(angularV);
        
        //update the mesh orientation
        Quaternion quat2 = pivotNode.getLocalRotation();
        
        mesh.getLocalRotation().slerp(quat2, tpf);
    }
    
    public class PlayerPhysicsListener implements PhysicsCollisionListener 
    {
        public void collision(PhysicsCollisionEvent event) 
        {
            if((event.getNodeA().getName().equals("terrain-geom-0") && event.getNodeB().getName().equals("Player"))|| (event.getNodeA().getName().equals("Player")&&event.getNodeB().getName().equals("terrain-geom-0"))){
                soundNode.play();
            }
            
            //if there's a coin, remove it
            if((event.getNodeA().getName().equals("goldCoin-geom-0")&&event.getNodeB().getName().equals("Player")) || (event.getNodeA().getName().equals("Player")&&event.getNodeB().getName().equals("goldCoin-geom-0")))
            {
                if(event.getNodeA().getName().equals("goldCoin-geom-0"))
                {
                    event.getNodeA().removeFromParent();
                    points = points+100;
                }
                else
                {
                    event.getNodeB().removeFromParent();
                    points = points+100;
                }
            }
            System.out.println("----------------------------------");
            System.out.println("Collision Between:");
            System.out.println(event.getNodeA().getName());
            System.out.println(event.getNodeB().getName());
            System.out.println("----------------------------------");
        }
    }
    public void cleanup()
    {
        this.detachAllChildren();
        physicsControl.destroy();
    }
    private void registerInput() {
        
        app.getInputManager().addMapping("rotateRight", new MouseAxisTrigger(MouseInput.AXIS_X, true));
        app.getInputManager().addMapping("rotateLeft", new MouseAxisTrigger(MouseInput.AXIS_X, false));
        app.getInputManager().addMapping("rotateUp", new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        app.getInputManager().addMapping("rotateDown", new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        app.getInputManager().addMapping("rollLeft", new KeyTrigger(KeyInput.KEY_A));
        app.getInputManager().addMapping("rollRight", new KeyTrigger(KeyInput.KEY_D));
        app.getInputManager().addListener(this, "rotateRight", "rotateLeft", "rotateUp", "rotateDown", "rollLeft", "rollRight");
    }

    public void onAnalog(String name, float value, float tpf) {
        
        if (name.equals("rotateRight")) {
            pivotNode.rotate(-.01f,0,0);
        }
        if (name.equals("rotateLeft")) {
            pivotNode.rotate(.01f,0,0);
        }
        if (name.equals("rotateUp")) {
            pivotNode.rotate(0,0,-.01f);
        }
        if (name.equals("rotateDown")) {
            pivotNode.rotate(0,0,.01f);
        }
        camNode.lookAt(this.getLocalTranslation(),Vector3f.UNIT_Y);
    }
    public void onAction(String name, boolean keyPressed, float tpf) {
        
        if (name.equals("rollLeft")) {
          //getControl(RigidBodyControl.class).applyTorqueImpulse(new Vector3f(-1, 0, 0).mult(.5f*tpf));
          mesh.rotate(0, -.1f, 0);
        }
        if (name.equals("rollRight")) {
          //getControl(RigidBodyControl.class).applyTorqueImpulse(new Vector3f(1, 0, 0).mult(.5f*tpf));
          mesh.rotate(0, .1f, 0);
        }
    }
}
