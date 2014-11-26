/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ourgame;
import com.jme3.app.Application;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
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
        mesh = app.getAssetManager().loadModel("Models/coin.obj");

        Material mat = new Material (app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseMaterialColors",true);
        mat.setFloat("Shininess",128f);
        mat.setColor("Specular",ColorRGBA.White);
        mat.setColor("Ambient",new ColorRGBA(1f,1f,.165f,.8f));
        mat.setColor("Diffuse",new ColorRGBA(1f,1f,0f,.8f));
        mesh.setMaterial(mat);
        mesh.setLocalScale(size);
        mesh.setLocalTranslation(position);

        mesh.rotate(FastMath.HALF_PI, 0, FastMath.nextRandomFloat()*FastMath.TWO_PI);
        
        CollisionShape coinShape = CollisionShapeFactory.createMeshShape(mesh);
        GhostControl ghost = new GhostControl(coinShape);
        mesh.addControl(ghost);
        this.attachChild(mesh);
        bulletAppState.getPhysicsSpace().add(mesh);
    }
    
}
