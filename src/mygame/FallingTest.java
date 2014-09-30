
package mygame;


import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

public class FallingTest extends SimpleApplication {

    protected Geometry player;
    protected Material mat;

    public static void main(String[] args) {
        FallingTest app = new FallingTest();
        app.start();
    }

    public void simpleInitApp() {
        Box b = new Box(1, 1, 1);
        player = new Geometry("Blue Cube", b);
        mat = new Material (assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Gray);
        player.setMaterial(mat);
        rootNode.attachChild(player);
    }

    @Override
    public void simpleUpdate(float tpf) {
        player.move(0,-2*tpf,0);
    }
}
