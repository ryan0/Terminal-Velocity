/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ourgame;

import com.jme3.app.Application;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.TangentBinormalGenerator;


public class Terrain extends Node
{
    private RigidBodyControl physicsControl;
    private Spatial mesh;
    
    public Terrain(BulletAppState bulletAppState, Application app)
    {
        mesh = app.getAssetManager().loadModel("Models/Terrain/terrain.obj");
        TangentBinormalGenerator.generate(mesh);
        Material mat = new Material (app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        mat.setTexture("DiffuseMap", app.getAssetManager().loadTexture("Textures/TerrainTexture.png"));
        mat.setTexture("NormalMap", app.getAssetManager().loadTexture("Textures/TerrainNormalMap.png"));
        mesh.setMaterial(mat);
        mesh.setLocalScale(new Vector3f(100000f, 100000f, 100000f));
        
        CollisionShape terrainShape = CollisionShapeFactory.createMeshShape(mesh);
        physicsControl = new RigidBodyControl(terrainShape, 0);
        mesh.addControl(physicsControl);
        this.attachChild(mesh);
        bulletAppState.getPhysicsSpace().add(mesh);
    }
}
