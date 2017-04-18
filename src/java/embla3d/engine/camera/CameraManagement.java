package embla3d.engine.camera;

import embla3d.engine.World;
import org.joml.Vector3f;

import java.util.Collections;

/**
 * This class allows to manage the cameras of our world.
 */
public class CameraManagement {
    /**
     * Creates a camera
     * Adds the camera created in the camera listings of our World.
     *
     * @param world world
     * @return The camera creates .
     */
    public static Camera addCamera(World world) {
        Camera c = new Camera();
        world.getCamerasList().add(c);
        return c;
    }

    /**
     * Allows to Check if the camera passed in argument is already present in the world.
     * If the camera already exists in our World it does not add and returns the camera.
     * Otherwise the camera is added to our world and returns the camera.
     *
     * @param world world
     * @param c     camera
     * @return the camera.
     */
    public static Camera addCamera(World world, Camera c) {
        if (!world.getCamerasList().contains(c)) world.getCamerasList().add(c);
        return c;
    }

    /**
     * Allow to get the current ( in live) camera that is stored in the World class.
     *
     * @param world world
     * @return the camera.
     */
    public static Camera getLiveCamera(World world) {
        return world.getCamera();
    }

    /**
     * Allows to modify the current camera (in live).
     * 1) Stores the current camera (in live) in the World class camera list
     * 2) The camera passed in argument becomes the current camera (in live).
     * 3) Removes the new camera (in live) from the list of cameras (not in live) of the world.
     *
     * @param world world
     * @param c     camera
     */
    public static void setLiveCamera(World world, Camera c) {
        Camera x = world.getCamera();
        world.getCamerasList().add(x);
        world.setCamera(c);
        world.getCamerasList().removeAll(Collections.singleton(c));

    }

    /**
     * @param world world
     * @param i     Index of the camera
     * @return the camera store in position i, if i> 0 or i <= to the numbers of cameras (not in live) in the worlds.
     */
    public static Camera getCamera(World world, int i) {
        if (i < 0 || i >= world.getCamerasList().size())
            return null;
        return world.getCamerasList().get(i);
    }

    /**
     * Removes from the list of cameras (not in live) of the world the camera passed in argument.
     *
     * @param world world
     * @param c     camera
     */
    public static void removeCamera(World world, Camera c) {
        world.getCamerasList().remove(c);
    }

    /**
     * Removes from the list of camera (not in live) of the world the camera corresponding to the index passed in argument.
     *
     * @param world world
     * @param i     Index of the camera
     */
    public static void removeCameraNumber(World world, int i) {
        if (i < 0 || i >= world.getCamerasList().size())
            return;
        world.getCamerasList().remove(i);
    }

    /**
     * Changes the position of the camera in the world.
     * (In reality it will modify the position of all the objects of the world in the opposite direction to the movement of the camera)
     *
     * @param c camera
     * @param x coordinate X of the camera
     * @param y coordinate Y of the camera
     * @param z coordinate Z of the camera
     */
    public static void setPositionCamera(Camera c, float x, float y, float z) {
        c.setPosition(new Vector3f(x, y, z));
    }

    /**
     * Allows to obtain the cardinal coordinates of the camera
     *
     * @param c camera
     * @return The position of the camera
     */
    public static Vector3f getPosition(Camera c) {
        return c.getPosition();
    }

}