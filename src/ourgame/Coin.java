/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ourgame;
import com.jme3.app.Application;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
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
     * Creates a node containing a terrain <code>mesh</code> and adds
     * it to a specific <code>Application</code> and <code>bulletAppState</code>.
     * 
     * @param bulletAppState the desired parent bulletAppState
     * @param app the desired parent Application
     */
    public Coin(BulletAppState bulletAppState, Application app, Vector3f position,Vector3f size)
    {
        setName("Coin");
        //mesh = app.getAssetManager().loadModel("insert coin stuffs here");

        //note: the bottom stuff has been copied and pasted from terrain.java, waiting for the models to be finished and the texture
        TangentBinormalGenerator.generate(mesh);
        Material mat = new Material (app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        mat.setTexture("DiffuseMap", app.getAssetManager().loadTexture("Textures/TerrainTexture.png"));
        mat.setTexture("NormalMap", app.getAssetManager().loadTexture("Textures/TerrainNormalMap.png"));
        mesh.setMaterial(mat);
        mesh.setLocalScale(size);
        mesh.setLocalTranslation(position);

        CollisionShape coinShape = CollisionShapeFactory.createMeshShape(mesh);
        GhostControl ghost = new GhostControl(coinShape);
        mesh.addControl(ghost);
        this.attachChild(mesh);
        bulletAppState.getPhysicsSpace().add(mesh);
    }
    
}
