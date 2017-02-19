package embla3d.engine.items;

import embla3d.engine.meshs.Material;
import org.joml.Vector3f;

import java.util.ArrayList;

public class ItemGroup {
    /**
     * Class which will contain items with coordinates specific to the center of the group
     *
     * @param items           new ArrayList<Item>
     * @param center          Vector3f
     * @param weight          int
     */


    public Vector3f mCenter;
    private ArrayList<Item> mItems;
    private int mWeight;

    public ItemGroup() {
        mItems = new ArrayList<>();
        mCenter = new Vector3f();
        mWeight = 0;
    }

    public ItemGroup(ArrayList<Item> objs) {
        mItems = objs;
        mWeight = 0;
        double x = 0, y = 0, z = 0;
        for (Item i : objs) {
            int w = i.getAppearance().getWeight();
            x += i.getPosition().x * w;
            y += i.getPosition().y * w;
            z += i.getPosition().z * w;
            mWeight += w;
        }
        x /= mWeight;
        y /= mWeight;
        z /= mWeight;
        mCenter = new Vector3f((float) x, (float) y, (float) z);
    }

    /**
     * Add an item in group and update center
     */
    public void add(Item i) {
        int nWeight = mWeight + i.getAppearance().getWeight();
        double ratioGr = mWeight / (double) nWeight, ratioI = i.getAppearance().getWeight() / (double) nWeight;
        mWeight = nWeight;
        mCenter = new Vector3f((float) ((mCenter.x * ratioGr) + (i.getPosition().x * ratioI)), (float) ((mCenter.y * ratioGr) + (i.getPosition().y * ratioI)), (float) ((mCenter.z * ratioGr) + (i.getPosition().z * ratioI)));
        mItems.add(i);
        i.addToGroup(this);
    }

    /**
     * remove an item of the group and update center
     */
    public void remove(Item i) {
        mItems.remove(i);
        i.removeFromGroup(this);
        this.mWeight -= i.getAppearance().getWeight();
        updateCenter();
    }

    public void updateCenter() {
        double x = 0, y = 0, z = 0;
        for (Item i : mItems) {
            int w = i.getAppearance().getWeight();
            x += i.getPosition().x * w;
            y += i.getPosition().y * w;
            z += i.getPosition().z * w;
        }
        x /= mWeight;
        y /= mWeight;
        z /= mWeight;
        mCenter = new Vector3f((float) x, (float) y, (float) z);
    }

    /**
     * Apply translation for all items in the group
     */
    public void translate(Vector3f translation) {
        for (Item i : mItems)
            i.getPosition().add(translation);
    }

    /**
     * Apply translation for all items in the group
     */
    public void translate(float x, float y, float z) {
        Vector3f translation = new Vector3f(x, y, z);
        for (Item i : mItems)
            i.getPosition().add(translation);
    }

    /**
     * Rotate all items in the group
     */
    public void rotate(Vector3f rotation) {
        for (Item i : mItems) {
            i.getRotation().add(rotation);
            i.revolveAround(mCenter, rotation.x, rotation.y, rotation.z);
        }
    }

    /**
     * Rotate all items in the group
     */
    public void rotate(float x, float y, float z) {
        Vector3f rotation = new Vector3f(x, y, z);
        for (Item i : mItems) {
            i.getRotation().add(rotation);
            i.revolveAround(mCenter, rotation.x, rotation.y, rotation.z);
        }
    }

    public void setPosition(float x, float y, float z) {
        setPosition(new Vector3f(x, y, z));
    }

    public void setPosition(Vector3f pos) {
        float x = pos.x - mCenter.x, y = pos.y - mCenter.y, z = pos.z - mCenter.z;
        for (Item i : mItems) {
            i.translate(x, y, z, this);
        }
        mCenter = pos;
    }

    public void separate(float dist) {
        for (Item i : mItems)
            i.repelBy(mCenter, dist);
    }

    public void setColor(float r, float g, float b) {
        for (Item i : mItems)
            i.getAppearance().setMaterial(new Material(new Vector3f(r, g, b), 0.f));
    }

    public void setColor(Vector3f color) {
        for (Item i : mItems)
            i.getAppearance().setMaterial(new Material(color, 0.f));
    }

    public void multScale(float val) {
        for (Item i : mItems)
            i.setScale(i.getScale() * val);
    }

    public ArrayList<Item> getItems() {
        return mItems;
    }
}
