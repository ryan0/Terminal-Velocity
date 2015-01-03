
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
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl;
import com.jme3.util.TangentBinormalGenerator;




//import com.jme3.material.RenderState.FaceCullMode.jme3.material.FaceCullMode;

/**
 * A <code>Player</code> is a node which includes a
 * playerMesh and the mechanics for the actual falling
 * character.
 * 
 * @author Ryan
 */
public class Player extends Node implements AnalogListener, ActionListener
{
    private Node playerMesh = new Node(); //
    private RigidBodyControl physicsControl;
    private AudioNode soundNode;
    private SimpleApplication app;
    private Camera cam;
    private Node pivotNode;
    private CameraNode camNode;
    

    private boolean parachuteUsed = false;
    private boolean parachuting = false;
    private Spatial parachuteMesh;
    
    private int points = 0;
    private boolean hitGround = false;
    
    BulletAppState bulletState;
    
    /**
     * Creates a node containing a player <code>playerMesh</code> and adds
     * it to a specific <code>Application</code> and <code>bulletAppState</code>.
     * 
     * @param bulletAppState
     * @param appRef
     */
    public Player(BulletAppState bulletAppState, Application appRef)
    {
        bulletState = bulletAppState;
        setName("Player");
        
        app = (SimpleApplication)appRef;
        cam = app.getCamera();
        //playerMesh = app.getAssetManager().loadModel("Models/Dude/WIP Dude Frame - unjoined.obj");
        //Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        
        Spatial backpackS = app.getAssetManager().loadModel("Models/Dude/Backpack.obj");
        Material mat1 = new Material (app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        mat1.setTexture("DiffuseMap", app.getAssetManager().loadTexture("Textures/Royal Blue.png"));
        backpackS.setMaterial(mat1);
        Spatial headS = app.getAssetManager().loadModel("Models/Dude/Head.obj"); 
        Material mat2 = new Material (app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        mat2.setTexture("DiffuseMap", app.getAssetManager().loadTexture("Textures/Skin Tone.png"));
        headS.setMaterial(mat2);
        Spatial helmetS = app.getAssetManager().loadModel("Models/Dude/Helmet.obj");
        Material mat3 = new Material (app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        mat3.setTexture("DiffuseMap", app.getAssetManager().loadTexture("Textures/Black.png"));
        helmetS.setMaterial(mat3);
        Spatial leftElbowS = app.getAssetManager().loadModel("Models/Dude/Left Elbow.obj");        
        leftElbowS.setMaterial(mat3);
        Spatial leftFootS = app.getAssetManager().loadModel("Models/Dude/Left Foot.obj");        
        leftFootS.setMaterial(mat3);
        Spatial leftHandS = app.getAssetManager().loadModel("Models/Dude/Left Hand.obj");        
        leftHandS.setMaterial(mat1);
        Spatial leftKneeS = app.getAssetManager().loadModel("Models/Dude/Left Knee.obj");        
        leftKneeS.setMaterial(mat3);
        Spatial leftLowerArmS = app.getAssetManager().loadModel("Models/Dude/Left Lower Arm.obj");        
        leftLowerArmS.setMaterial(mat3);
        Spatial leftLowerLegS = app.getAssetManager().loadModel("Models/Dude/Left Lower Leg.obj");        
        leftLowerLegS.setMaterial(mat3);
        Spatial leftShoulderS = app.getAssetManager().loadModel("Models/Dude/Left Shoulder.obj");        
        leftShoulderS.setMaterial(mat3);
        Spatial leftUpperArmS = app.getAssetManager().loadModel("Models/Dude/Left Upper Arm.obj");       
        leftUpperArmS.setMaterial(mat3);
        Spatial leftUpperLegS = app.getAssetManager().loadModel("Models/Dude/Left Upper Leg.obj");        
        leftUpperLegS.setMaterial(mat3);
        Spatial leftWristS = app.getAssetManager().loadModel("Models/Dude/Left Wrist.obj");        
        leftWristS.setMaterial(mat1);
        Spatial lowerTorsoS = app.getAssetManager().loadModel("Models/Dude/Lower Torso.obj");        
        lowerTorsoS.setMaterial(mat3);
        Spatial neckS = app.getAssetManager().loadModel("Models/Dude/Neck.obj");        
        neckS.setMaterial(mat2);
        Spatial rightElbowS = app.getAssetManager().loadModel("Models/Dude/Right Elbow.obj");        
        rightElbowS.setMaterial(mat3);
        Spatial rightFootS = app.getAssetManager().loadModel("Models/Dude/Right Foot.obj");        
        rightFootS.setMaterial(mat3);
        Spatial rightHandS = app.getAssetManager().loadModel("Models/Dude/Right Hand.obj");        
        rightHandS.setMaterial(mat1);
        Spatial rightKneeS = app.getAssetManager().loadModel("Models/Dude/Right Knee.obj");        
        rightKneeS.setMaterial(mat3);
        Spatial rightLowerArmS = app.getAssetManager().loadModel("Models/Dude/Right Lower Arm.obj");        
        rightLowerArmS.setMaterial(mat3);
        Spatial rightLowerLegS = app.getAssetManager().loadModel("Models/Dude/Right Lower Leg.obj");        
        rightLowerLegS.setMaterial(mat3);
        Spatial rightShoulderS = app.getAssetManager().loadModel("Models/Dude/Right Shoulder.obj");       
        rightShoulderS.setMaterial(mat3);
        Spatial rightUpperArmS = app.getAssetManager().loadModel("Models/Dude/Right Upper Arm.obj");       
        rightUpperArmS.setMaterial(mat3);
        Spatial rightUpperLegS = app.getAssetManager().loadModel("Models/Dude/Right Upper Leg.obj");        
        rightUpperLegS.setMaterial(mat3);
        Spatial rightWristS = app.getAssetManager().loadModel("Models/Dude/Right Wrist.obj"); 
        rightWristS.setMaterial(mat1);
        Spatial upperTorsoS = app.getAssetManager().loadModel("Models/Dude/Upper Torso.obj");       
        upperTorsoS.setMaterial(mat3);
        Spatial waistS = app.getAssetManager().loadModel("Models/Dude/Waist.obj");
        waistS.setMaterial(mat3);
        Spatial wingsS = app.getAssetManager().loadModel("Models/Dude/Wings.obj");
        Material mat4 = new Material (app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        mat4.setTexture("DiffuseMap", app.getAssetManager().loadTexture("Textures/polycotton_blue_2_PNG.png"));
        wingsS.setMaterial(mat4);
                
        playerMesh.attachChild(backpackS);
        playerMesh.attachChild(headS);
        playerMesh.attachChild(helmetS);
        playerMesh.attachChild(leftElbowS);
        playerMesh.attachChild(leftFootS);
        playerMesh.attachChild(leftHandS);
        playerMesh.attachChild(leftKneeS);
        playerMesh.attachChild(leftLowerArmS);
        playerMesh.attachChild(leftLowerLegS);
        playerMesh.attachChild(leftShoulderS);
        playerMesh.attachChild(leftUpperArmS);
        playerMesh.attachChild(leftUpperLegS);
        playerMesh.attachChild(leftWristS);
        playerMesh.attachChild(lowerTorsoS);
        playerMesh.attachChild(neckS);
        playerMesh.attachChild(rightElbowS);
        playerMesh.attachChild(rightFootS);
        playerMesh.attachChild(rightHandS);
        playerMesh.attachChild(rightKneeS);
        playerMesh.attachChild(rightLowerArmS);
        playerMesh.attachChild(rightLowerLegS);
        playerMesh.attachChild(rightShoulderS);
        playerMesh.attachChild(rightUpperArmS);
        playerMesh.attachChild(rightUpperLegS);
        playerMesh.attachChild(rightWristS);
        playerMesh.attachChild(upperTorsoS);
        playerMesh.attachChild(waistS);
        playerMesh.attachChild(wingsS);
        
            
        Quaternion rotation = new Quaternion();
        rotation.fromAngles(0f, FastMath.PI/2f, -FastMath.PI/2f);
        playerMesh.setLocalRotation(rotation);
        this.attachChild(playerMesh);
        
        BoxCollisionShape PlayerShape = new BoxCollisionShape(new Vector3f(1.5f, 6f, 1));
        physicsControl = new RigidBodyControl(PlayerShape, 70f);
        addControl(physicsControl);
        bulletState.getPhysicsSpace().add(physicsControl);
        bulletState.getPhysicsSpace().addCollisionListener(new PlayerPhysicsListener());
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
        
        parachuteMesh = app.getAssetManager().loadModel("Models/Parachute/Parachute2.j3o");
        parachuteMesh.setLocalTranslation(new Vector3f(-2,12,3.5f));
        parachuteMesh.setMaterial(mat1);
        registerInput();
    }
    public int getPoints()
    {
        return points;
    }
    public void update(float tpf)
    {
         if(!hitGround&&!parachuteUsed)
            points+=tpf;
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
        
        if(parachuting)
        {
            if(angularV.getX()>20)
                angularV.setX(20);
            else if (angularV.getX()<-20)
                angularV.setX(-20);
            
            if(angularV.getZ()>20)
                angularV.setZ(20);
            else if (angularV.getZ()<-20)
                angularV.setZ(-20);
            
            if(angularV.getY()>40)
                angularV.setY(40);
            else if (angularV.getY()<-40)
                angularV.setY(-40);
        }
        physicsControl.setLinearVelocity(angularV);
        
        //report velocity
        System.out.println("X:"+angularV.getX());
        System.out.println("Y:"+angularV.getY());
        System.out.println("Z:"+angularV.getZ());
        
        //update the playerMesh orientation
        if(!parachuting)
        {
            Quaternion quat2 = pivotNode.getLocalRotation();
            getLocalRotation().slerp(quat2, tpf);
        }
        else
        {
            this.setLocalRotation(Quaternion.DIRECTION_Z);
            //Quaternion pitch90 = new Quaternion();
            //pitch90.fromAngleAxis(FastMath.PI/2, new Vector3f(0,0,1));
            //this.setLocalRotation(pitch90);
            playerMesh.getLocalRotation().slerp(new Quaternion(0,1,0,1), 5*tpf); //*** (0, 1, 0,1)
        }
        
        if(parachuteUsed && !parachuting)
        {
            parachuteMesh.setLocalScale(.8f,.8f,.8f);
            parachuteMesh.setLocalRotation(Quaternion.DIRECTION_Z);
            //parachuteMesh.setLocalTranslation()
            this.attachChild(parachuteMesh);
            parachuting = true;
        }
        if(parachuting && parachuteMesh.getLocalScale().getX()<6f)
        {
            parachuteMesh.setLocalScale(parachuteMesh.getLocalScale().add(.1f, .1f, .1f));
            parachuteMesh.setLocalTranslation(parachuteMesh.getLocalTranslation().add(0,.3f,0));
            Vector3f increaseVector = new Vector3f(camNode.getLocalTranslation());
            camNode.setLocalTranslation(camNode.getLocalTranslation().add(increaseVector.mult(.02f)));
            setLocalTranslation(getLocalTranslation().add(new Vector3f(0,-.08f,0)));
        }
    }
    
    public class PlayerPhysicsListener implements PhysicsCollisionListener 
    {
        public void collision(PhysicsCollisionEvent event) 
        {
            
            if((event.getNodeA().getName().equals("le terrain") && event.getNodeB().getName().equals("Player"))|| (event.getNodeA().getName().equals("Player")&&event.getNodeB().getName().equals("le terrain"))){
                if(Math.abs(event.getAppliedImpulse())+
                        Math.abs(event.getAppliedImpulseLateral1())+
                        Math.abs(event.getAppliedImpulseLateral2())>800)
                    soundNode.play();
                hitGround = true;
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
            //For debugging purposes
//            System.out.println("----------------------------------");
//            System.out.println("Collision Between:");
//            System.out.println(event.getNodeA().getName());
//            System.out.println(event.getNodeB().getName());
//            System.out.println("----------------------------------");
        }
    }
    public void cleanup()
    {
        this.detachAllChildren();
        physicsControl.destroy();
        app.getInputManager().clearMappings();
    }
    public boolean getHitGround()
    {
        return hitGround;
    }
    private void registerInput() {
        
        app.getInputManager().addMapping("rotateRight", new MouseAxisTrigger(MouseInput.AXIS_X, true));
        app.getInputManager().addMapping("rotateLeft", new MouseAxisTrigger(MouseInput.AXIS_X, false));
        app.getInputManager().addMapping("rotateUp", new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        app.getInputManager().addMapping("rotateDown", new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        app.getInputManager().addMapping("use parachute", new KeyTrigger(KeyInput.KEY_SPACE));
        app.getInputManager().addListener(this, "rotateRight", "rotateLeft", "rotateUp", "rotateDown", "use parachute");
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
        
        if (name.equals("use parachute") && !parachuting) {
            parachuteUsed = true;
        }
    }
}
