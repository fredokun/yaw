package yaw.engine.items;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Abstract class representing the common features of 3D items.
 */
public abstract class Item {
    /** String identifier of the item. */
    private String id;

    /** Position of (the center) of the Item in the World coordinates system. */
    protected Vector3f position;

    /** Orientation of the Item. */
    protected Vector3f orientation;

    /** Scaling factor (1.0f default) */
    protected float scale;


    /**
     * Create a new item with the speficied values.
     * @param id the (unique) identifier for the item
     * @param position the initial world-coordinates position
     * @param orientation the initial orientation (angles in degrees)
     * @param scale the initial scaling factor
     */
    public Item(String id, Vector3f position, Vector3f orientation, float scale) {
        this.id = id;
        this.position = position;
        this.orientation = orientation;
        this.scale = scale;
    }

    /* ----- Getters and Setters ----- */

    public String getId() {return id;}


    public Vector3f getOrientation() {
        return orientation;
    }
    public void setOrientation(Vector3f orientation) {
        this.orientation = orientation;
    }


    public Vector3f getPosition() {
        return position;
    }
    public void setPosition(Vector3f pos){this.position = pos;}

    public float getScale() {
        return scale;
    }
    public void setScale(float val) {
        scale = val;
    }

    /* ----- Transformations ----- */


    /** Translation
     *
     * @param tx the distance that we want the Item to move on axis X
     * @param ty the distance that we want the Item to move on axis Y
     * @param tz the distance that we want the Item to move on axis Z
     */
    public void translate(float tx, float ty, float tz) {
        position.add(tx, ty, tz);
    }

    /**
     * Rotate along X axis
     * @param angle of rotation (in degree)
     */
    public void rotateX(float angle) {
        rotateAxis(angle, new Vector3f(1.0f, 0.0f, 0.0f));
    }

    /**
     * Rotate along Y axis
     * @param angle of rotation (in degree)
     */
    public void rotateY(float angle) {
        rotateAxis(angle, new Vector3f(0.0f, 1.0f, 0.0f));
    }

    /**
     * Rotate along Z axis
     * @param angle of rotation (in degree)
     */
    public void rotateZ(float angle) {
        rotateAxis(angle, new Vector3f(0.0f, 0.0f, 1.0f));
    }

    public void rotateAxis(float angle, Vector3f axis) {
        new Matrix3f().rotate((float) Math.toRadians(angle), axis).transform(orientation);
    }

    public void rotateAxisAround(float angle, float ax, float ay, float az, float cx, float cy, float cz) {
        Vector3f center = new Vector3f(cx, cy, cz);
        new Matrix4f().translate(center)
                .rotate((float) Math.toRadians(angle), ax, ay, az)
                .translate(center.negate())
                .transformPosition(position);
    }

    /**Make an Item revolve around a point, like a planet around a star
     *
     * @param center the central point on which the item will revolve
     * @param degX   degree of rotation on axis X
     * @param degY   degree of rotation on axis Y
     * @param degZ   degree of rotation on axis Z
     */
    public abstract void revolveAround(Vector3f center, float degX, float degY, float degZ);


    public abstract void repelBy(Vector3f center, float dist);



    //Method for future input feature
    public abstract void update();


    public Matrix4f getWorldMatrix() {
        return new Matrix4f().identity().translate(position).
                rotateX((float) Math.toRadians(orientation.x)).
                rotateY((float) Math.toRadians(orientation.y)).
                rotateZ((float) Math.toRadians(orientation.z)).
                scale(scale);
    }



}


