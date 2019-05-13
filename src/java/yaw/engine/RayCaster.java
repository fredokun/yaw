package yaw.engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import yaw.engine.camera.Camera;
import yaw.engine.items.HitBox;

import java.nio.DoubleBuffer;
import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;

public class RayCaster {


    /**
     * Calculate the ray casted by the mouse click in our world, using the camera
     * @param window
     * @param c the world camera
     * @return
     */
    public static Vector3f getWorldRay(long window, Camera c){
        DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(window, xBuffer, yBuffer);
        double mouse_x = xBuffer.get(0);
        double mouse_y = yBuffer.get(0);



        float x = (2.0f * (float)mouse_x) / 800 - 1.0f;
        float y = 1.0f- (2.0f * (float)mouse_y) / 600;



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

        worldRay.normalize();

        return worldRay;
    }


    /**
     *
     * @param window the window of the world
     * @param h the hitbox we want to test
     * @param c the world camera
     * @return true if the ray intersects with one of the six faces of the hitbox, else false
     */
    public static boolean isHitBoxClicked(long window, HitBox h, Camera c){
        Vector3f v ;
        Vector3f v1;
        v = RayCaster.getWorldRay(window, c);
        Vector4f V = new Vector4f(v, 1);
        float tmp = RayCaster.distanceHitBoxCamera(h,c);
        v1 = new Vector3f(tmp*v.x,tmp*v.y,tmp*v.z);
        Vector4f V1 = new Vector4f(v1, 1);
        Vector4f cam = new Vector4f(c.position,1);
        ArrayList<Vector4f> vertex = HitBox.tabToListVertex(h);
        int[] tabIndexFaces = {0, 1, 2, 3, 1, 2, 6, 5, 0, 3, 7, 4, 1, 5, 4, 0, 2, 3, 7, 6, 4, 5, 6, 7};

        Vector4f segEnd = new Vector4f(V1.x+cam.x,V1.y+cam.y, V1.z+cam.z,1);

        for (int j = 0; j < tabIndexFaces.length; j += 4) {
            if (HitBox.isIntersectSegmentAndFace(cam, segEnd,
                    vertex.get(tabIndexFaces[j]), vertex.get(tabIndexFaces[j + 1]),
                    vertex.get(tabIndexFaces[j + 2]), vertex.get(tabIndexFaces[j + 3]))) {
                return true;
            }
        }


        return false;
    }

    /**
     * This method returns the distance between the camera position and the farest vertex of the passed HitBox
     */
    public static float distanceHitBoxCamera(HitBox h, Camera c){
        ArrayList<Vector4f> listVertexboundingBox1 = HitBox.tabToListVertex(h);
        float distance = 0;
        Vector4f cameraCoords = new Vector4f(c.position, 1);

        for(Vector4f v : listVertexboundingBox1){
            float d = cameraCoords.distance(v);
            if(d>distance){
                distance = d;
            }
        }
        return distance;
    }
}
