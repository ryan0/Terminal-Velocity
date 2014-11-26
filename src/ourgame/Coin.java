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
        mesh = app.getAssetManager().loadModel("Models/CoinByRyan/Coin_I_Put_Far_Too_Much_Time_Into.obj");
        TangentBinormalGenerator.generate(mesh);
        
        Material mat = new Material (app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        mat.setTexture("DiffuseMap", app.getAssetManager().loadTexture("Textures/ryanCoin/ryanCoinTex.png"));
        mat.setTexture("NormalMap", app.getAssetManager().loadTexture("Textures/ryanCoin/ryanCoinNormalMap.png"));
        mat.setBoolean("UseMaterialColors",true);
        mat.setColor("Specular",ColorRGBA.White);
        mat.setColor("Diffuse",ColorRGBA.White);
        mat.setFloat("Shininess", 2.5f);
        mesh.setMaterial(mat);
        mesh.setLocalScale(size);
        mesh.setLocalTranslation(position);

        
        this.attachChild(mesh);
    }
    
}
