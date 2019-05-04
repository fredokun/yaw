package yaw.engine.items;

import org.joml.*;
import yaw.engine.meshs.Material;
import yaw.engine.meshs.Mesh;

/**
 * An ItemObject is a concrete 3D item associated to a Mesh
 */
public class ItemObject extends Item {

    /** The mesh (geometry) of the object. */
    private Mesh mesh;

    /** The transformation matrix to world coordinates */
    private Matrix4f worldMatrix;



    public ItemObject(String id, Vector3f position, Quaternionf orientation, float scale, Mesh mesh){
        super(id, position, orientation, scale);
        this.mesh= mesh;
        worldMatrix = new Matrix4f();
        invalidate();
    }

    public void buildWorldMatrix() {
        worldMatrix.identity()
                .translate(getPosition())
                .rotate(getOrientation())
                .scale(getScale());
    }

    public Matrix4f getWorldMatrix() {
        return worldMatrix;
    }

    @Override
    public void invalidate() {
        buildWorldMatrix();
    }

    public void repelBy(Vector3f center, float dist) {
        Vector3f dif = new Vector3f(position.x - center.x, position.y - center.y, position.z - center.z);
        float norm = dif.length();
        if (norm != 0) {
            float move = (dist / norm) + 1;
            dif.mul(move);
            dif.add(center);
            position = dif;
        }
    }


    /**
     * Get the mesh (geometry) of the item
     */
    public Mesh getMesh(){ return mesh; }

    /**
     * Rotate along X axis
     * @param angle of rotation (in degree)
     */
    public void rotateX(float angle) {
        orientation.rotateX(toRadians(angle));
        invalidate();
    }

    /**
     * Rotate along Y axis
     * @param angle of rotation (in degree)
     */
    public void rotateY(float angle) {
        orientation.rotateY(toRadians(angle));
        invalidate();
    }

    /**
     * Rotate along Z axis
     * @param angle of rotation (in degree)
     */
    public void rotateZ(float angle) {
        orientation.rotateZ(toRadians(angle));
        invalidate();
    }

    /**
     * Rotation along three axes (Euler angles rotation)
     * @param angleX angle of rotation along axis X (in degrees)
     * @param angleY same for axis Y
     * @param angleZ same for axis Z
     */
    public void rotateXYZ(float angleX, float angleY, float angleZ) {
        orientation.rotateXYZ(toRadians(angleX), toRadians(angleY), toRadians(angleZ));
        invalidate();
    }

    /**
     * Rotation of given angle along an axis
     * @param angle angle of rotation in degrees
     * @param axis axis of rotation
     */
    public void rotateAxis(float angle, Vector3f axis) {
        orientation.rotateAxis(toRadians(angle), axis);
        invalidate();
    }

    /**
     * Rotation of given angle along an axis, and aroung the specified center
     * @param angle the angle of rotation (in degrees)
     * @param axis the axis of rotation
     * @param center the center of rotation
     */
    public void rotateAxisAround(float angle, Vector3f axis, Vector3f center) {
        axis = axis.normalize();
        // change orientation
        orientation.rotateAxis(toRadians(angle), axis);

        // change position
        new Matrix4f().rotateAround(new Quaternionf(new AxisAngle4f(toRadians(angle), axis)), center.x, center.y, center.z)
                .transformPosition(position);

//        new Matrix4f()
//                .translate(center)
//                .rotate(toRadians(angle), axis)
//                .translate(new Vector3f(center).negate())
//                .transformPosition(position);

        invalidate();
    }

    /**
     * Rotate along X axis, around center
     * @param angle of rotation (in degree)
     * @param center the center of rotation
     */
    public void rotateXAround(float angle, Vector3f center) {
        rotateAxisAround(angle, new Vector3f(1.0f, 0.0f, 0.0f), center);
    }

    /**
     * Rotate along Y axis, around center
     * @param angle of rotation (in degree)
     * @param center the center of rotation
     */
    public void rotateYAround(float angle, Vector3f center) {
        rotateAxisAround(angle, new Vector3f(0.0f, 1.0f, 0.0f), center);
    }

    /**
     * Rotate along Z axis, around center
     * @param angle of rotation (in degree)
     * @param center the center of rotation
     */
    public void rotateZAround(float angle, Vector3f center) {
        rotateAxisAround(angle, new Vector3f(0.0f, 0.0f, 1.0f), center);
    }

    /**
     * Rotation along three axes (Euler angles rotation), around center
     * @param angleX angle of rotation along axis X (in degrees)
     * @param angleY same for axis Y
     * @param angleZ same for axis Z
     * @param center the center of rotation
     */
    public void rotateXYZAround(float angleX, float angleY, float angleZ, Vector3f center) {
        if(!(angleX == 0 && angleY == 0 && angleZ == 0)) {
            AxisAngle4f aaxis = new AxisAngle4f(new Quaternionf().rotationXYZ(toRadians(angleX)
                    , toRadians(angleY)
                    , toRadians(angleZ))).normalize();
            System.out.println(aaxis.x + " " + aaxis.y + " " + aaxis.z + " ");
            rotateAxisAround(toDegrees(aaxis.angle), new Vector3f(aaxis.x, aaxis.y, aaxis.z), center);
        }
    }







}
