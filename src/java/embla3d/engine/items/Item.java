package embla3d.engine.items;

import embla3d.engine.meshs.Material;
import embla3d.engine.meshs.Mesh;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;

public class Item {

    private Mesh mAppearance;
    private float mScale;
    private Vector3f mRotation;
    private Vector3f mPosition;
    private ArrayList<ItemGroup> mGroups;
    private Item mBoundingBox;
    private boolean mIsBoundingBox;
    private String mId;

    /**
     * Construct a item with the specified mesh, scale, rotation, translation and
     * an item can be in several groups of items
     *
     * @param pId            id of th item
     * @param pRotation      rotation vector3f
     * @param pPosition      position vector3f
     * @param pScale         scale
     * @param pAppearance    mesh
     * @param pGroups        itemGoup
     */
    public Item(String pId, Vector3f pRotation, Vector3f pPosition, float pScale, boolean isBoundingBox, Mesh pAppearance, ArrayList<ItemGroup> pGroups) {
        mAppearance = pAppearance;
        mScale = pScale;
        mRotation = pRotation;
        mPosition = pPosition;
        mGroups = pGroups == null ? new ArrayList<>() : pGroups;
        mId = pId;
        mBoundingBox = null;
        mIsBoundingBox = isBoundingBox;
    }

    /**
     * Clone source
     *
     * @param source source
     */
    public Item(Item source) {
        this(source.getId() + source.toString(),
                new Vector3f(source.mRotation),
                new Vector3f(source.mPosition),
                source.mScale,
                source.mIsBoundingBox,
                source.mAppearance,
                null);
    }

    /**
     * Create an item with the specified id position mesh and scale
     * pPosition size must be 3
     *
     * @param pId       id
     * @param pPosition position
     * @param pMesh     mesh
     * @param pScale    scale
     */
    public Item(String pId, float[] pPosition, float pScale, boolean pIsBoundingBox, Mesh pMesh) {
        this(pId, new Vector3f(), new Vector3f(pPosition[0], pPosition[1], pPosition[2]), pScale, pIsBoundingBox, pMesh, null);
    }

    public Item clone() {

        return new Item(this);
    }

    public void rotate(float x, float y, float z) {
        this.setRotation(getRotation().add(x, y, z));
        if (mBoundingBox != null)
            this.mBoundingBox.rotate(x, y, z);
    }

    public void setPosition(float x, float y, float z) {
        setPosition(new Vector3f(x, y, z), null);
    }

    public void setPosition(Vector3f pos, ItemGroup g) {
        this.mPosition = pos;
        for (ItemGroup gr : mGroups) {
            if (gr != g)
                gr.updateCenter();
        }
    }

    public void translate(float x, float y, float z) {
        translate(x, y, z, null);
        if (mBoundingBox != null)
            this.mBoundingBox.translate(x, y, z, null);
    }

    public void translate(float x, float y, float z, ItemGroup g) {
        Vector3f old = getPosition(), vect = new Vector3f(x + old.x, y + old.y, z + old.z);
        this.setPosition(vect, g);
    }

    //Group Moves
    public void revolveAround(Vector3f center, float degX, float degY, float degZ) {
        Vector4f pos = new Vector4f(mPosition, 1f);
        pos.add(-center.x, -center.y, -center.z, 0);
        Matrix4f trans = new Matrix4f();
        trans.rotateX((float) Math.toRadians(degX));
        trans.rotateY((float) Math.toRadians(degY));
        trans.rotateZ((float) Math.toRadians(degZ));
        trans.transform(pos);
        pos.add(center.x, center.y, center.z, 0);
        mPosition = new Vector3f(pos.x, pos.y, pos.z);
    }

    public void repelBy(Vector3f center, float dist) {
        Vector3f dif = new Vector3f(mPosition.x - center.x, mPosition.y - center.y, mPosition.z - center.z);
        float norm = dif.length();
        if (norm != 0) {
            float move = (dist / norm) + 1;
            dif.mul(move);
            dif.add(center);
            mPosition = dif;
        }
    }

    // Don't use in Clojure addToGroup and removeFromGroup
    public void addToGroup(ItemGroup g) {
        mGroups.add(g);
    }

    public void removeFromGroup(ItemGroup g) {
        mGroups.remove(g);
    }

    // Input Function
    public void update() {
    }

    // Material getter
    public void setColor(float r, float g, float b) {
        this.getAppearance().setMaterial(new Material(new Vector3f(r, g, b), 0.f));
    }

    // OpenGl function
    public Matrix4f getWorldMatrix() {
        return new Matrix4f().identity().translate(mPosition).
                rotateX((float) Math.toRadians(mRotation.x)).
                rotateY((float) Math.toRadians(mRotation.y)).
                rotateZ((float) Math.toRadians(mRotation.z)).
                scale(mScale);
    }

    //Scale
    public float getScale() {
        return mScale;
    }

    public void setScale(float val) {
        mScale = val;
    }

    public String getId() {
        return mId;
    }

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

    public void setPosition(Vector3f pos) {
        setPosition(pos, null);
    }

    // Groups Management
    public ArrayList<ItemGroup> getGroups() {
        return mGroups;
    }

    public Mesh getAppearance() {
        return mAppearance;
    }

    public float getReflectance() {
        return this.getAppearance().getMaterial().getReflectance();
    }

    public void setReflectance(float refl) {
        this.getAppearance().getMaterial().setReflectance(refl);
    }

    public Vector3f getColor() {
        return this.getAppearance().getMaterial().getColor();
    }

    public void setColor(Vector3f color) {
        this.getAppearance().setMaterial(new Material(color, 0.f));
    }

    public Item getBoundingBox() {
        return this.mBoundingBox;
    }

    public boolean isBoundingBox() {
        return mIsBoundingBox;
    }

    public void setBoundingBox(Item item) {
        this.mBoundingBox = item;
    }
}
