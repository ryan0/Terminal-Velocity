package ourgame;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.util.SkyFactory;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        rootNode.attachChild(SkyFactory.createSky(assetManager, 
                assetManager.loadTexture("Textures/Sky/Sky_West.jpg"), 
                assetManager.loadTexture("Textures/Sky/Sky_East.jpg"), 
                assetManager.loadTexture("Textures/Sky/Sky_North.jpg"), 
                assetManager.loadTexture("Textures/Sky/Sky_South.jpg"), 
                assetManager.loadTexture("Textures/Sky/Sky_Up.jpg"), 
                assetManager.loadTexture("Textures/Sky/Sky_Down.jpg")));
        
        
        Box b = new Box(1, 1, 1);
        Geometry geom = new Geometry("Box", b);
        //Hi Ryan
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);

        rootNode.attachChild(geom);
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
