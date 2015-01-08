/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ourgame;

import com.jme3.app.Application;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import ourgame.items.Item;


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

        float size1 = 0f, size2 = 0f, size3 = 0f;
        try 
        {
            BufferedReader inputStream = new BufferedReader(new FileReader("assets/" + assetFolder + "/texSizes.txt")); 
            try 
            {
                size1 = Float.parseFloat(inputStream.readLine().trim());
                size2 = Float.parseFloat(inputStream.readLine().trim());
                size3 = Float.parseFloat(inputStream.readLine().trim());
            } 
            catch (IOException ex) 
            {
                System.out.print("error parsing file size string");
            }
            inputStream.close();
        }
        catch (FileNotFoundException ex) 
        {
            System.out.print("error loading tex sizes FileNotFoundException");
        }
        catch (IOException ex) 
        {
            System.out.print("error loading tex sizes");
        }

        
        
        Texture tex1 = app.getAssetManager().loadTexture(assetFolder + "/images/tex1.png");
        tex1.setWrap(Texture.WrapMode.Repeat);
        mat.setTexture("DiffuseMap", tex1);
        mat.setFloat("DiffuseMap_0_scale", size1);

        Texture tex2 = app.getAssetManager().loadTexture(assetFolder + "/images/tex2.png");
        tex2.setWrap(Texture.WrapMode.Repeat);
        mat.setTexture("DiffuseMap_2", tex2);
        mat.setFloat("DiffuseMap_2_scale", size2);
        
        Texture tex3 = app.getAssetManager().loadTexture(assetFolder + "/images/tex3.png");
        tex3.setWrap(Texture.WrapMode.Repeat);
        mat.setTexture("DiffuseMap_1", tex3);
        mat.setFloat("DiffuseMap_1_scale", size3);


        Texture grassNormalMap = app.getAssetManager().loadTexture(assetFolder + "/images/tex1NormalMap.png");
        grassNormalMap.setWrap(Texture.WrapMode.Repeat);
        Texture midRockNormalMap = app.getAssetManager().loadTexture(assetFolder + "/images/tex2NormalMap.png");
        midRockNormalMap.setWrap(Texture.WrapMode.Repeat);
        Texture rockNormalMap = app.getAssetManager().loadTexture(assetFolder + "/images/tex3NormalMap.png");
        rockNormalMap.setWrap(Texture.WrapMode.Repeat);
        mat.setTexture("NormalMap", grassNormalMap);
        mat.setTexture("NormalMap_1", rockNormalMap);
        mat.setTexture("NormalMap_2", midRockNormalMap);
        
        AbstractHeightMap heightmap;
        Texture heightMapImage = app.getAssetManager().loadTexture(assetFolder + "/heightMap.png");
        heightmap = new ImageBasedHeightMap(heightMapImage.getImage());
        heightmap.load();
        

        int patchSize = 17;
        TerrainQuad terrain = new TerrainQuad("le terrain", patchSize, 257, heightmap.getHeightMap());

        terrain.setMaterial(mat);
        terrain.setLocalScale(2000f, 500f, 2000f);
        terrain.setLocalTranslation(-50000f, -94000f, -40000);

        TerrainLodControl control = new TerrainLodControl(terrain, app.getCamera());
        terrain.addControl(control);
        
        terrain.addControl(new RigidBodyControl(0));
        bulletAppState.getPhysicsSpace().add(terrain);
        this.attachChild(terrain);
        this.setShadowMode(RenderQueue.ShadowMode.Off);
    }
}
