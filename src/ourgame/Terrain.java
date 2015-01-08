/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ourgame;

import com.jme3.app.Application;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.scene.Node;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;


/**
 * A node extension that contains a <code>mesh</code>
 * for the terrain and its specifications.
 * 
 * @author Ryan
 */
public class Terrain extends Node
{
    /**
     * Creates a node containing a terrain <code>mesh</code> and adds
     * it to a specific <code>Application</code> and <code>bulletAppState</code>.
     * 
     * @param bulletAppState the desired parent bulletAppState
     * @param app the desired parent Application
     */
    public Terrain(BulletAppState bulletAppState, Application app, String assetFolder)
    //Accesses the terain and applies grass, midRock, and rock textures to it according to normal maps and a height map
    //attaches the terrain to this after sizing it and adding a physics space
    {
        Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Terrain/TerrainLighting.j3md");
        mat.setTexture("AlphaMap", app.getAssetManager().loadTexture(assetFolder + "/alphaMap.png"));

        Texture grass = app.getAssetManager().loadTexture(assetFolder + "/images/grass.png");
        grass.setWrap(Texture.WrapMode.Repeat);
        mat.setTexture("DiffuseMap", grass);
        mat.setFloat("DiffuseMap_0_scale", 128f);

        Texture midRock = app.getAssetManager().loadTexture(assetFolder + "/images/midRock.png");
        midRock.setWrap(Texture.WrapMode.Repeat);
        mat.setTexture("DiffuseMap_2", midRock);
        mat.setFloat("DiffuseMap_2_scale", 32f);
        
        Texture rock = app.getAssetManager().loadTexture(assetFolder + "/images/rock.png");
        rock.setWrap(Texture.WrapMode.Repeat);
        mat.setTexture("DiffuseMap_1", rock);
        mat.setFloat("DiffuseMap_1_scale", 64f);


        Texture grassNormalMap = app.getAssetManager().loadTexture(assetFolder + "/images/grassNormalMap.png");
        grassNormalMap.setWrap(Texture.WrapMode.Repeat);
        Texture midRockNormalMap = app.getAssetManager().loadTexture(assetFolder + "/images/midRockNormalMap.png");
        midRockNormalMap.setWrap(Texture.WrapMode.Repeat);
        Texture rockNormalMap = app.getAssetManager().loadTexture(assetFolder + "/images/rockNormalMap.png");
        rockNormalMap.setWrap(Texture.WrapMode.Repeat);
        mat.setTexture("NormalMap", grassNormalMap);
        mat.setTexture("NormalMap_1", rockNormalMap);
        mat.setTexture("NormalMap_2", midRockNormalMap);
        
        AbstractHeightMap heightmap;
        Texture heightMapImage = app.getAssetManager().loadTexture(assetFolder + "/heightMap.png");
        heightmap = new ImageBasedHeightMap(heightMapImage.getImage());
        heightmap.load();

        int patchSize = 65;
        TerrainQuad terrain = new TerrainQuad("le terrain", patchSize, 257, heightmap.getHeightMap());

        terrain.setMaterial(mat);
        terrain.setLocalScale(2000f, 500f, 2000f);
        terrain.setLocalTranslation(-50000f, -94000f, -40000);

        TerrainLodControl control = new TerrainLodControl(terrain, app.getCamera());
        terrain.addControl(control);
        
        terrain.addControl(new RigidBodyControl(0));
        bulletAppState.getPhysicsSpace().add(terrain);
        this.attachChild(terrain);
    }
}
