package yaw;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL;
import yaw.InputCallbackTest;
import yaw.engine.Mouse3DClickCallBack;
import yaw.engine.RayCaster;
import yaw.engine.Window;
import yaw.engine.World;
import yaw.engine.camera.Camera;
import yaw.engine.collision.Collision;
import yaw.engine.items.HitBox;
import yaw.engine.items.ItemObject;
import yaw.engine.meshs.Mesh;
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
    private HitBox hitbox2;
    private World world;
    private HitBox selected = null;

    public MouseClickTest(Camera c, HitBox h,HitBox h2, World w){
        this.camera=c;
        this.hitbox=h;
        this.hitbox2=h2;
        this.world=w;
    }

    static Mesh createCube() {
        Mesh mesh = MeshBuilder.generateBlock(1, 1, 1);
        return mesh;
    }

    @Override
    public void mouse_click_callback(long window, int button, int action, int mods) {
        this.window = window;
        this.button = button;
        this.action = action;
        this.mods = mods;
        if(button==GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {


            if(RayCaster.isHitBoxClicked(this.window, hitbox, camera)){
                hitbox.getMesh().getMaterial().setColor(new Vector3f(255, 0, 0));
                hitbox2.getMesh().getMaterial().setColor(new Vector3f(0, 255, 0));
                selected = hitbox;
            }else {
                hitbox.getMesh().getMaterial().setColor(new Vector3f(0, 255, 0));

                if (RayCaster.isHitBoxClicked(this.window, hitbox2, camera)) {
                    hitbox2.getMesh().getMaterial().setColor(new Vector3f(255, 0, 0));
                    selected = hitbox2;
                } else {
                    hitbox2.getMesh().getMaterial().setColor(new Vector3f(0, 255, 0));
                    selected = null;
                }
            }
        }



    }



    public static void main(String[] args){
        World world = new World(0, 0, 800, 600);


        HitBox h = world.createHitBox("cube", 5f, 0f, -25f, 4f, 1, 1, 1);
        HitBox h2 = world.createHitBox("cube2", -5f, 0f, -25f, 4f, 1, 1, 1);
        MouseClickTest key = new MouseClickTest(world.getCamera(), h,h2, world);
        world.registerMouseCallback(key);
        //ItemObject cube = world.createHitBox("cube", 0f, 0f, -2f, 1.0f, MeshBuilder.generateBlock(1, 1, 1));
        //cube.getMesh().getMaterial().setTexture(new Texture("/ressources/diamond.png"));
        world.getCamera().translate(0,0,0);
        //world.getCamera().rotate(0,180,0);
        world.launch();
        world.waitFortermination();

    }

}
