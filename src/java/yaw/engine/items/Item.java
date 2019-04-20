package yaw.engine.items;

import org.joml.*;
import org.joml.Math;

/**
 * Abstract class representing the common features of 3D items.
 */
public abstract class Item {

    /** String identifier of the item. */
    private String id;

    /** Position of (the center) of the Item in the World coordinates system. */
    protected Vector3f position;

    /** Orientation of the Item. */
    protected Quaternionf orientation;

    /** Scaling factor (1.0f default) */
    protected float scale;

    /**
     * Create a new item with the speficied values.
     * @param id the (unique) identifier for the item
     * @param position the initial world-coordinates position
     * @param orientation the initial orientation (angles in degrees)
     * @param scale the initial scaling factor
     */
    public Item(String id, Vector3f position, Quaternionf orientation, float scale) {
        this.id = id;
        this.position = position;
        this.orientation = orientation;
        this.scale = scale;
    }

    /**
     * Convert degrees to radians
     * @param angle expressed in degrees
     * @return the same angle in radians
     */
    public static float toRadians(float angle) {
        return (float) Math.toRadians(angle);
    }

    public static float toDegrees(float angle) {
        return (float) Math.toDegrees(angle);
    }

    /* ----- Getters and Setters ----- */

    public String getId() {return id;}


    public Quaternionf getOrientation() {
        return orientation;
    }

    protected void setOrientation(Quaternionf orientation) {
        this.orientation = orientation;
    }


    public Vector3f getPosition() {
        return position;
    }

    protected void setPosition(Vector3f pos){
        this.position = pos;
    }

    public float getScale() {
        return scale;
    }

    public void scale(float val) {
        scale = val;
        invalidate();
    }

    /* ----- Transformations ----- */

    public abstract void invalidate();

    /** Translation
     *
     * @param tx the distance that we want the Item to move on axis X
     * @param ty the distance that we want the Item to move on axis Y
     * @param tz the distance that we want the Item to move on axis Z
     */
    public void translate(float tx, float ty, float tz) {
        position.add(tx, ty, tz);
        invalidate();
    }

    /**
     * Rotate along X axis
     * @param angle of rotation (in degree)
     */
    public abstract void rotateX(float angle);

    /**
     * Rotate along Y axis
     * @param angle of rotation (in degree)
     */
    public abstract void rotateY(float angle);

    /**
     * Rotate along Z axis
     * @param angle of rotation (in degree)
     */
    public abstract void rotateZ(float angle);

    /**
     * Rotation along three axes (Euler angles rotation)
     * @param angleX angle of rotation along axis X (in degrees)
     * @param angleY same for axis Y
     * @param angleZ same for axis Z
     */
    public abstract void rotateXYZ(float angleX, float angleY, float angleZ) ;

    /**
     * Rotation of given angle along an axis
     * @param angle angle of rotation in degrees
     * @param axis axis of rotation
     */
    public abstract void rotateAxis(float angle, Vector3f axis) ;

    /**
     * Rotation of given angle along an axis, and aroung the specified center
     * @param angle the angle of rotation (in degrees)
     * @param axis the axis of rotation
     * @param center the center of rotation
     */
    public abstract void rotateAxisAround(float angle, Vector3f axis, Vector3f center);

    /**
     * Rotate along X axis, around center
     * @param angle of rotation (in degree)
     * @param center the center of rotation
     */
    public  abstract void rotateXAround(float angle, Vector3f center) ;

    /**
     * Rotate along Y axis, around center
     * @param angle of rotation (in degree)
     * @param center the center of rotation
     */
    public abstract void rotateYAround(float angle, Vector3f center) ;

    /**
     * Rotate along Z axis, around center
     * @param angle of rotation (in degree)
     * @param center the center of rotation
     */
    public abstract void rotateZAround(float angle, Vector3f center);

    /**
     * Rotation along three axes (Euler angles rotation), around center
     * @param angleX angle of rotation along axis X (in degrees)
     * @param angleY same for axis Y
     * @param angleZ same for axis Z
     * @param center the center of rotation
     */
    public abstract void rotateXYZAround(float angleX, float angleY, float angleZ, Vector3f center) ;


    // XXX : use this ?
    //public abstract void repelBy(Vector3f center, float dist);

}


