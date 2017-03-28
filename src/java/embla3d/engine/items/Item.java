package embla3d.engine.items;

import embla3d.engine.meshs.Material;
import embla3d.engine.meshs.Mesh;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;

public class Item {
    /**
     * Construct a item with the specified mesh, scale, rotation, translation and
     * an item can be in several groups of items
     *
     * @param mAppearance     Vertex array
     * @param mScale          float
     * @param mRotation       Vector3f
     * @param mTranslation    Vector3f
     * @param mGroups         ArrayList<ItemGroup>
     */
    private Mesh mAppearance;
    private float mScale;
    private Vector3f mRotation;
    private Vector3f mTranslation;
    private ArrayList<ItemGroup> mGroups;

    public Item(Item source) {
        this.mAppearance = source.mAppearance;
        this.mScale = source.mScale;
        this.mRotation = new Vector3f(source.mRotation);
        this.mTranslation = new Vector3f(source.mTranslation);
        this.mGroups = new ArrayList<>();
    }

    public Item(Mesh m) {
        this.mAppearance = m;
        mScale = 1f;
        mRotation = new Vector3f();
        mTranslation = new Vector3f();
        this.mGroups = new ArrayList<>();
    }

    /**
     * Create an item
     *
     * @param pAppearance Mesh
     * @param pScale      scale
     * @param pPosition   position
     */
    public Item(Mesh pAppearance, float pScale, Vector3f pPosition) {
        this(pAppearance, pScale, new Vector3f(), pPosition);
    }

    //Constructor
    public Item(Mesh appearance, float scale, Vector3f rotation, Vector3f position) {
        super();
        this.mAppearance = appearance;
        this.mScale = scale;
        this.mRotation = rotation;
        this.mTranslation = position;
        this.mGroups = new ArrayList<>();
    }

    public Item clone() {

        return new Item(this);
    }

    // OpenGl function
    public Matrix4f getWorldMatrix() {
        return new Matrix4f().identity().translate(mTranslation).
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

    public void rotate(float x, float y, float z) {
        this.setRotation(getRotation().add(x, y, z));
    }

    //Rotation
    public Vector3f getRotation() {
        return mRotation;
    }

    public void setRotation(Vector3f rotation) {
        this.mRotation = rotation;
    }

    public void setPosition(float x, float y, float z) {
        setPosition(new Vector3f(x, y, z), null);
    }

    public void setPosition(Vector3f pos, ItemGroup g) {
        this.mTranslation = pos;
        for (ItemGroup gr : mGroups) {
            if (gr != g)
                gr.updateCenter();
        }
    }

    public void translate(float x, float y, float z) {
        translate(x, y, z, null);
    }

    public void translate(float x, float y, float z, ItemGroup g) {
        Vector3f old = getPosition(), vect = new Vector3f(x + old.x, y + old.y, z + old.z);
        this.setPosition(vect, g);
    }

    //Translation
    public Vector3f getPosition() {
        return mTranslation;
    }

    public void setPosition(Vector3f pos) {
        setPosition(pos, null);
    }

    //Group Moves
    public void revolveAround(Vector3f center, float degX, float degY, float degZ) {
        Vector4f pos = new Vector4f(mTranslation, 1f);
        pos.add(-center.x, -center.y, -center.z, 0);
        Matrix4f trans = new Matrix4f();
        trans.rotateX((float) Math.toRadians(degX));
        trans.rotateY((float) Math.toRadians(degY));
        trans.rotateZ((float) Math.toRadians(degZ));
        trans.transform(pos);
        pos.add(center.x, center.y, center.z, 0);
        mTranslation = new Vector3f(pos.x, pos.y, pos.z);
    }

    public void repelBy(Vector3f center, float dist) {
        Vector3f dif = new Vector3f(mTranslation.x - center.x, mTranslation.y - center.y, mTranslation.z - center.z);
        float norm = dif.length();
        if (norm != 0) {
            float move = (dist / norm) + 1;
            dif.mul(move);
            dif.add(center);
            mTranslation = dif;
        }
    }

    // Groups Management
    public ArrayList<ItemGroup> getGroups() {
        return mGroups;
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
}
