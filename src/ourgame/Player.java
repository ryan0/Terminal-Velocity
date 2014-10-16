/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
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
    private SimpleApplication app;
    
    
    public Player(BulletAppState bulletAppState, Application appRef)
    {
        setName("Player");
        
        app = (SimpleApplication)appRef;
        mesh = app.getAssetManager().loadModel("Models/Dude/WIP Dude Frame.obj");
        mesh.setMaterial(new Material(app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md"));
        
        Quaternion rotation = new Quaternion();
        rotation.fromAngles(0f, FastMath.PI, -FastMath.PI/2f);
        mesh.setLocalRotation(rotation);
        attachChild(mesh);
        
        CapsuleCollisionShape PlayerShape = new CapsuleCollisionShape(1.5f, 6f, 1);
        physicsControl = new RigidBodyControl(PlayerShape, .05f);
        mesh.addControl(physicsControl);
        bulletAppState.getPhysicsSpace().add(physicsControl);
        bulletAppState.getPhysicsSpace().addCollisionListener(new PlayerPhysicsListener());
        physicsControl.setGravity(new Vector3f(0f, -9.8f, 0f));
        physicsControl.setPhysicsLocation(new Vector3f(-18000, 23000, -10000));
        
        
        
        
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
