
package ourgame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.ChaseCamera;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
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
public class Player extends Node
{
    private Spatial mesh;
    private RigidBodyControl physicsControl;
    private AudioNode soundNode;
    private SimpleApplication app;
    private Camera cam;

    
    
    /**
     * Creates a node containing a player <code>mesh</code> and adds
     * it to a specific <code>Application</code> and <code>bulletAppState</code>.
     * 
     * @param bulletAppState
     * @param appRef
     */
    public Player(BulletAppState bulletAppState, Application appRef)
    {
        setName("Player");
        
        app = (SimpleApplication)appRef;
        cam = app.getCamera();
        mesh = app.getAssetManager().loadModel("Models/Dude/WIP Dude Frame.obj");
        mesh.setMaterial(new Material(app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md"));
        
        Quaternion rotation = new Quaternion();
        rotation.fromAngles(0f, FastMath.PI, -FastMath.PI/2f);
        mesh.setLocalRotation(rotation);
        attachChild(mesh);
        
        CapsuleCollisionShape PlayerShape = new CapsuleCollisionShape(1.5f, 6f, 1);
        physicsControl = new RigidBodyControl(PlayerShape, .05f);
        addControl(physicsControl);
        bulletAppState.getPhysicsSpace().add(physicsControl);
        bulletAppState.getPhysicsSpace().addCollisionListener(new PlayerPhysicsListener());

        physicsControl.setGravity(new Vector3f(0f, -9.8f, 0f));
        physicsControl.setPhysicsLocation(new Vector3f(-18000, 23000, -10000));
        
        
        soundNode = new AudioNode(app.getAssetManager(), "Sounds/scream.ogg", false);
        soundNode.setPositional(false);
        soundNode.setLooping(false);
        attachChild(soundNode);
        
        
        CameraNode camNode = new CameraNode("Camera Node", cam);
        camNode.setControlDir(CameraControl.ControlDirection.SpatialToCamera);
        camNode.lookAt(this.getLocalTranslation(), Vector3f.UNIT_Y);
        camNode.setEnabled(true);
        camNode.setCamera(cam);
        attachChild(camNode);
        camNode.setLocalTranslation(new Vector3f(0, 5, -20));
        setLocalTranslation(new Vector3f(-18000, 23000, -10000));
            
    }
    
    public void update(float tpf)
    {
        Vector3f xPlusZ = new Vector3f(
                app.getCamera().getDirection().x, 
                0, 
                app.getCamera().getDirection().z);
        xPlusZ.normalize();
        
        
        Vector3f angularV = new Vector3f(
                xPlusZ.x * physicsControl.getLinearVelocity().y * -2,
                physicsControl.getLinearVelocity().y,
                xPlusZ.z * physicsControl.getLinearVelocity().y * -2);
        physicsControl.setLinearVelocity(angularV);
    }
    
    public class PlayerPhysicsListener implements PhysicsCollisionListener 
    {
        public void collision(PhysicsCollisionEvent event) 
        {
            if(event.getNodeA().getName().equals("Player") || event.getNodeB().getName().equals("Player"));
                soundNode.play();
        }
    }
    
}
