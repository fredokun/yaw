package yaw.engine.camera;


import yaw.engine.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Class which will hold the position and rotation state of our camera.
 * To move from a world described in 3D to a window with 2D pixels we must lose a dimension and therefore project.
 * The perspective is defined by the pyramid clipping.
 */
public class Camera {

    public Vector3f position; /* Position of the camera (the camera is represented by a point in space). */
    public Vector3f orientation;/* Sets the position of the point fixed by the camera. */
    private Matrix4f perspectiveMat;
    /* Angle of the field of view
       A small angle gives a zoom effect.
       Like a zoom on a pair of binoculars.. */
    private float fieldOfView;
    /* Scope of vision min and max.
       Outside this range objects will not be displayed.*/
    private float zNear = 0.01f;
    private float zFar = 1000.f;

    /**
     * The constructors of the class Camera Creates the camera of the 3D scene with the position of the camera, the perspective.
     * The near and far parameters determine the minimum and maximum distances of objects. Outside this range objects will not be displayed.
     *
     * @param fieldOfView fieldOfView
     * @param zNear       zNear
     * @param zFar        zFar
     */
    public Camera(float fieldOfView, float zNear, float zFar) {
        this.perspectiveMat = new Matrix4f().perspective(fieldOfView, (float) Window.aspectRatio(), zNear, zFar);
        this.fieldOfView = fieldOfView;
        this.zFar = zFar;
        this.zNear = zNear;
        this.position = new Vector3f(0f, 0f, 0f);
        this.orientation = new Vector3f();
    }

    public Camera(float zNear, float zFar) {
        this.perspectiveMat = new Matrix4f().perspective((float) Math.toRadians(60.0f),
                (float) Window.aspectRatio(), zNear, zFar);
        this.fieldOfView = (float) Math.toRadians(60.0f);
        this.zFar = zFar;
        this.zNear = zNear;
        this.position = new Vector3f(0f, 0f, 0f);
        this.orientation = new Vector3f();
    }

    public Camera() {
        this.perspectiveMat = new Matrix4f().perspective((float) Math.toRadians(60.0f),
                (float) Window.aspectRatio(), 0.01f, 1000.f);
        this.fieldOfView = (float) Math.toRadians(60.0f);
        this.zFar = 0.01f;
        this.zNear = 1000.f;
        this.position = new Vector3f(0f, 0f, 0f);
        this.orientation = new Vector3f();
    }

    public Camera(float fieldOfView, float zNear, float zFar, Vector3f position) {
        this.perspectiveMat = new Matrix4f().perspective(fieldOfView,
                (float) Window.aspectRatio(), zNear, zFar).translate(position);
        this.fieldOfView = fieldOfView;
        this.zFar = zFar;
        this.zNear = zNear;
        this.position = position;
        this.orientation = new Vector3f();
    }


    public Camera(float fieldOfView, float zNear, float zFar, float px, float py, float pz) {
        this.position = new Vector3f(px, py, pz);
        this.perspectiveMat = new Matrix4f().perspective(fieldOfView,
                (float) Window.aspectRatio(), zNear, zFar).translate(this.position);
        this.fieldOfView = fieldOfView;
        this.zFar = zFar;
        this.zNear = zNear;
        this.orientation = new Vector3f();
    }

    public Camera(float fieldOfView, float zNear, float zFar, float px, float py, float pz, float ox, float oy, float oz) {
        this.position = new Vector3f(px, py, pz);
        this.perspectiveMat = new Matrix4f().perspective(fieldOfView,
                (float) Window.aspectRatio(), zNear, zFar).translate(this.position);
        this.fieldOfView = fieldOfView;
        this.zFar = zFar;
        this.zNear = zNear;
        this.orientation = new Vector3f(ox, oy, oz);
    }


    public Camera(float zNear, float zFar, Vector3f position) {
        this.perspectiveMat = new Matrix4f().perspective((float) Math.toRadians(60.0f),
                (float) Window.aspectRatio(), zNear, zFar).translate(position);
        this.fieldOfView = (float) Math.toRadians(60.0f);
        this.zFar = zFar;
        this.zNear = zNear;
        this.position = position;
        this.orientation = new Vector3f();
    }


    public Camera(Vector3f position) {
        this.perspectiveMat = new Matrix4f().perspective((float) Math.toRadians(60.0f),
                (float) Window.aspectRatio(), 0.01f, 1000.f).translate(position);
        this.fieldOfView = (float) Math.toRadians(60.0f);
        this.zFar = 0.01f;
        this.zNear = 1000.f;
        this.position = position;
        this.orientation = new Vector3f();
    }

    public Matrix4f getCameraMat() {
        return perspectiveMat;
    }

    /**
     * Changes the position of the camera.
     *
     * @param x coordonnée d'axe x
     * @param y coordonnée d'axe y
     * @param z coordonnée d'axe z
     */
    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f pos) {
        this.position = pos;
    }

    /**
     * Changes the orientation of the camera.
     *
     * @param x coordonnée d'axe x
     * @param y coordonnée d'axe y
     * @param z coordonnée d'axe z
     */
    public void setOrientation(float x, float y, float z) {
        this.orientation.x = x;
        this.orientation.y = y;
        this.orientation.z = z;
    }

    public Vector3f getOrientation() {
        return orientation;
    }

    public void setOrientation(Vector3f pos) {
        this.orientation = pos;
    }

    /**
     * Allows to change the place of our object and therefore to make it navigate in our 3D scene.
     *
     * @param x x
     * @param y y
     * @param z z
     */
    public void rotate(float x, float y, float z) {
        orientation.add(x, y, z);
    }

    /**
     * Changes the size of the object to adjust the 3D scene.
     *
     * @param x x
     * @param y y
     * @param z z
     */
    public void translate(float x, float y, float z) {
        position.add(x, y, z);
    }

    public void update() {

    }


    /**
     * Generate the Matrix for the camera position
     *
     * @return viewMatrix
     */
    public Matrix4f setupViewMatrix() {
        Matrix4f viewMatrix = new Matrix4f().identity();
        //viewMatrix.rotateX((float) Math.toRadians(-orientation.x)).rotateY((float) Math.toRadians(-orientation.y)).rotateZ((float) Math.toRadians(-orientation.z));
        viewMatrix.rotate((float) Math.toRadians(-orientation.x), new Vector3f(1, 0, 0));
        viewMatrix.rotate((float) Math.toRadians(-orientation.y), new Vector3f(0, 1, 0));
        viewMatrix.rotate((float) Math.toRadians(-orientation.z), new Vector3f(0, 0, 1));
        Vector3f negativeCameraPos = new Vector3f(-position.x, -position.y, -position.z);
        viewMatrix.translate(negativeCameraPos);
        return viewMatrix;

    }

    /**
     * Updates the perspective of the scene.
     */
    public void updateCameraMat() {
        perspectiveMat = new Matrix4f().perspective(fieldOfView,
                (float) Window.aspectRatio(), zNear, zFar);
       /* perspectiveMat = perspectiveMat.mul(setupViewMatrix()); */
    }


    public float getzNear() {
        return zNear;
    }

    public float getzFar() {
        return zFar;
    }

    public float getFieldOfView() {
        return fieldOfView;
    }
}
