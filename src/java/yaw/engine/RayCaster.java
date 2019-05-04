package yaw.engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import yaw.engine.camera.Camera;
import yaw.engine.items.HitBox;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;

public class RayCaster {



    public static Vector3f getWorldRay(long window, Camera c){
        DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(window, xBuffer, yBuffer);
        double mouse_x = xBuffer.get(0);
        double mouse_y = yBuffer.get(0);



        float x = (2.0f * (float)mouse_x) / 800 - 1.0f;
        float y = 1.0f- (2.0f * (float)mouse_y) / 600;
        System.out.println("x = "+mouse_x+" y = "+mouse_y);


        /*Vector2f ray = new Vector2f(x,y);

        Vector4f ray_clip = new Vector4f(x, y,0, 1);
        Matrix4f projection = c.getCameraMat();
        Matrix4f view = c.setupViewMatrix();
        Matrix4f inv_projection = projection.invert();
        Matrix4f inv_view = view.invert();
        inv_projection.mul(inv_view);
        Vector4f mouse_worldspace = inv_projection.transform(ray_clip);

        Vector4f p = new Vector4f(c.getPosition(), 1);
        Vector3f wp = new Vector3f(mouse_worldspace.x-p.x,mouse_worldspace.y-p.y,mouse_worldspace.z-p.z);
        return wp.normalize();*/





        Vector2f ray = new Vector2f(x,y);

        Vector4f ray_clip = new Vector4f(x, y,-1, 1);
        Matrix4f projection = c.getCameraMat();
        Matrix4f view = c.setupViewMatrix();
        Matrix4f inv_projection = projection.invert();

        Vector4f eyeC=new Vector4f();
        inv_projection.transform(ray_clip, eyeC);
        Vector4f eyeCoords = new Vector4f(eyeC.x, eyeC.y, -1 ,0);
        view = view.invert();
        Vector4f rayW= new Vector4f();
        view.transform(eyeCoords, rayW);
        Vector3f worldRay = new Vector3f(rayW.x, rayW.y, rayW.z);

        //Vector3f mousePick = new Vector3f(worldRay.x-c.getPosition().x,worldRay.y-c.getPosition().y,worldRay.z-c.getPosition().z );
        //mousePick.normalize();
        worldRay.normalize();
        //System.out.println(worldRay);
        return worldRay;
    }

    public static HitBox isHitBoxClicked(long window, Vector3f orientation, HitBox h, Camera c){



        return null;
    }
}
