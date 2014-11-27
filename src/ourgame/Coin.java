/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ourgame;
import com.jme3.app.Application;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.TangentBinormalGenerator;
/**
 *
 * @author SD
 */
public class Coin extends Node{
    private Spatial mesh;
    
    /**
     * Creates a node containing a coin <code>mesh</code> and adds
     * it to a specific <code>Application</code> and <code>bulletAppState</code>.
     * 
     * @param bulletAppState the desired parent bulletAppState
     * @param app the desired parent Application
     */
    public Coin(BulletAppState bulletAppState, Application app, Vector3f position,Vector3f size)
    {
        setName("Coin");
        
        this.setShadowMode(RenderQueue.ShadowMode.Off);
        mesh = app.getAssetManager().loadModel("Models/CoinByRyan/goldCoin.obj");
        TangentBinormalGenerator.generate(mesh);
        
        Material mat = new Material (app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        mat.setTexture("DiffuseMap", app.getAssetManager().loadTexture("Textures/ryanCoin/goldCoinTex.png"));
        mat.setTexture("NormalMap", app.getAssetManager().loadTexture("Textures/ryanCoin/goldCoinNormalMap.png"));
        mat.setColor("GlowColor",ColorRGBA.Yellow.mult(.8f));
        mesh.setMaterial(mat);
        mesh.setLocalScale(size);
        mesh.setLocalTranslation(position);
        mesh.rotate(FastMath.HALF_PI,FastMath.TWO_PI*FastMath.nextRandomFloat(),0);
        
        CollisionShape coinShape = new BoxCollisionShape(new Vector3f(30, 30, 30));
        RigidBodyControl control = new RigidBodyControl(coinShape, .000001f);
        mesh.addControl(control);
        this.attachChild(mesh);
        bulletAppState.getPhysicsSpace().add(mesh);
        control.setGravity(new Vector3f());
    }
    
}
