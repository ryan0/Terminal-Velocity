/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ourgame;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author Mark
 */
public class Example_TreeRender extends SimpleApplication
{
    
    public static void main(String[] args) 
    {
        Example_TreeRender app = new Example_TreeRender();
        app.start();
    }
    
    @Override
    public void simpleInitApp() 
    {
        //Change the default camera's start position
        cam.setLocation(new Vector3f(-10,0,0));
        
        //Make a global ambient light
        AmbientLight lamp = new AmbientLight();
        lamp.setColor(ColorRGBA.White);
        rootNode.addLight(lamp);
        
        //Make a global directional light to make things not boring
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White.mult(2));
        sun.setDirection(new Vector3f(0,-1,0));
        rootNode.addLight(sun);
        
        //Start adding a bunch of trees
        for(int countX = -100;countX<0;countX+=(int)(Math.random()*6+6))
        {
            for(int countZ = -100;countZ<100;countZ+=(int)(Math.random()*6+6))
            {
                //Make a node to manage the following spatials
                //Allows for manipulation of both spatials with one command
                //(Rotating treeNode rotates he leaves as well as the tree itself
                Node treeNode = new Node();
        
                Spatial tree = assetManager.loadModel("Models/Tree/Tree.j3o");
                Material treeStuff = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                treeStuff.setTexture("ColorMap", assetManager.loadTexture("Textures/Tree Bark.jpg"));
                tree.setMaterial(treeStuff);
                tree.setCullHint(Spatial.CullHint.Inherit);
                treeNode.attachChild(tree);

                Spatial leaves = assetManager.loadModel("Models/Leaves/Leaves.j3o");
                Material leavesStuff = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                leavesStuff.setTexture("ColorMap", assetManager.loadTexture("Textures/Tree Leaves.jpg"));
                leaves.setMaterial(leavesStuff);
                leaves.setCullHint(Spatial.CullHint.Inherit);
                treeNode.attachChild(leaves);
                
                //Add some randomness so the forest doesn't look ugly
                treeNode.setLocalTranslation(countX+(int)(Math.random()*2), 0, countZ+(int)(Math.random()*2));
                treeNode.setLocalScale((float) (Math.random()*1.5+.3));
                treeNode.setLocalRotation(new Matrix3f((int)(Math.random()*90),0,0,0,0,0,20,0,0));
                treeNode.setCullHint(Spatial.CullHint.Never);
                rootNode.attachChild(treeNode);
                 
            }
        }
    }
    
}
