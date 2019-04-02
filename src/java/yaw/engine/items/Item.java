package yaw.engine.items;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Abstract Class for all Items with all basic getters and setters defined
 */
public abstract class Item {
    protected String mId;
    protected float mScale;
    protected Vector3f mPosition;
    protected Vector3f mRotation;


    /**
     * Construct a item with the specified mesh, scale, rotation, translation
     *
     * @param pId            id of th item
     * @param pRotation      rotation vector3f
     * @param pPosition      position vector3f
     * @param pScale         scale
     */
    public Item(String pId, Vector3f pRotation, Vector3f pPosition, float pScale) {
        mScale = pScale;
        mRotation = pRotation;
        mPosition = pPosition;
        mId = pId;
    }

    /**
     * Create an item with the specified id position mesh and scale and default rotation is (0,0,0)
     * pPosition size must be 3
     *
     * @param pId       id
     * @param pPosition position
     * @param pScale    scale
     */
    public Item(String pId, float[] pPosition, float pScale) {
        this(pId, new Vector3f(), new Vector3f(pPosition[0], pPosition[1], pPosition[2]), pScale);
    }

    public Item(Item item){
        this(item.mId, item.mRotation,item.mPosition,item.mScale);
    }


    public abstract Item clone();


    /**Rotating an item
     *
     * @param x degree of rotation on axis X
     * @param y degree of rotation on axis Y
     * @param z degree of rotation on axis Z
     */
    public void rotate(float angle, float ax, float ay, float az) {
        rotateAround(angle, ax, ay, az, mPosition.x, mPosition.y, mPosition.z);
    }


    /** Moving an Item
     *
     * @param x the distance that we want the Item to move on axis X
     * @param y the distance that we want the Item to move on axis Y
     * @param z the distance that we want the Item to move on axis Z
     */
    public abstract void translate(float x, float y, float z);

    /**Make an Item revolve around a point, like a planet around a star
     *
     * @param center the central point on which the item will revolve
     * @param degX   degree of rotation on axis X
     * @param degY   degree of rotation on axis Y
     * @param degZ   degree of rotation on axis Z
     */
    public abstract void revolveAround(Vector3f center, float degX, float degY, float degZ);

    public void rotateAround(float angle, float ax, float ay, float az, float cx, float cy, float cz) {
        Vector3f center = new Vector3f(cx, cy, cz);
        new Matrix4f().translate(center)
                .rotate((float) Math.toRadians(angle), ax, ay, az)
                .translate(center.negate())
                .transformPosition(mPosition);
    }

    public abstract void repelBy(Vector3f center, float dist);



    //Method for future input feature
    public abstract void update();


    public Matrix4f getWorldMatrix() {
        return new Matrix4f().identity().translate(mPosition).
                rotateX((float) Math.toRadians(mRotation.x)).
                rotateY((float) Math.toRadians(mRotation.y)).
                rotateZ((float) Math.toRadians(mRotation.z)).
                scale(mScale);
    }

    // ----- Getters and Setters -----

    //Scale
    public float getScale() {
        return mScale;
    }

    public void setScale(float val) {
        mScale = val;
    }


    //Id
    public String getId() {return mId;}


    //Rotation
    public Vector3f getRotation() {
        return mRotation;
    }

    public void setRotation(Vector3f rotation) {
        this.mRotation = rotation;
    }


    //Translation
    public Vector3f getPosition() {
        return mPosition;
    }

    public void setPosition(Vector3f pos){this.mPosition = pos;}


}


