package yaw;

import org.joml.Vector3f;
import org.joml.Vector4f;
import yaw.InputCallbackTest;
import yaw.engine.Mouse3DClickCallBack;
import yaw.engine.RayCaster;
import yaw.engine.Window;
import yaw.engine.World;
import yaw.engine.camera.Camera;
import yaw.engine.collision.Collision;
import yaw.engine.items.HitBox;
import yaw.engine.items.ItemObject;
import yaw.engine.meshs.MeshBuilder;
import yaw.engine.meshs.Texture;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

public class MouseClickTest implements Mouse3DClickCallBack {
    private long window;
    private int button;
    private int action;
    private int mods;
    private Camera camera;
    private HitBox hitbox;

    public MouseClickTest(Camera c, HitBox h){
        this.camera=c;
        this.hitbox=h;
    }

    @Override
    public void mouse_click_callback(long window, int button, int action, int mods) {
        this.window = window;
        this.button = button;
        this.action = action;
        this.mods = mods;
        Vector3f v = null;
        Vector3f v2 = null;
        if(button==GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {
            v = RayCaster.getWorldRay(window, camera);
            Vector4f V = new Vector4f(v, 1);
            v2 = new Vector3f(v.x,v.y,v.z*10);
            Vector4f V2 = new Vector4f(v2, 1);
            Vector4f cam = new Vector4f(camera.position,1);
            ArrayList<Vector4f> vertex = HitBox.tabToListVertex(hitbox);
            int[] tabIndexFaces = {0, 1, 2, 3, 1, 2, 6, 5, 0, 3, 7, 4, 1, 5, 4, 0, 2, 3, 7, 6, 4, 5, 6, 7};


            boolean isIntersect = false;
            for (int j = 0; j < tabIndexFaces.length; j += 4) {
                if (HitBox.isIntersectSegmentAndFace(V, V2,
                        vertex.get(tabIndexFaces[j]), vertex.get(tabIndexFaces[j + 1]),
                        vertex.get(tabIndexFaces[j + 2]), vertex.get(tabIndexFaces[j + 3]))) {
                    hitbox.rotateX(45);
                    break;
                }
            }
        }


    }



    public static void main(String[] args){
        World world = new World(0, 0, 800, 600);


        HitBox h = world.createHitBox("cube", 0f, 0f, -2f, 1.0f, 1, 1, 1);
        MouseClickTest key = new MouseClickTest(world.getCamera(), h);
        world.registerMouseCallback(key);
        //ItemObject cube = world.createHitBox("cube", 0f, 0f, -2f, 1.0f, MeshBuilder.generateBlock(1, 1, 1));
        //cube.getMesh().getMaterial().setTexture(new Texture("/ressources/diamond.png"));
        world.getCamera().setPosition(0,0,5);
        //world.getCamera().rotate(0,180,0);
        world.launch();
        world.waitFortermination();

    }

}
