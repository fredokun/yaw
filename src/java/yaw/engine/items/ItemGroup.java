package yaw.engine.items;

import org.joml.*;

import java.util.HashMap;
import java.util.Map;

/**
 *  A Group is a container for items.
 *  The natural position of a group is at the centroid of its contained items.
 */
public class ItemGroup extends Item {
    private Map<String, Item> items;

    public ItemGroup(String id, Vector3f position, Quaternionf orientation, float scale){
       super(id, position, orientation, scale);
       items = new HashMap<String, Item>();
    }

    /** this method adds an item to the group, updates the weight and the center.
     *
     * @param s name of the add item
     * @param i the Item
     */
    public void add(String s, Item i) {
        //center = new Vector3f((float) ((center.x * ratioGr) + (i.getPosition().x * ratioI)), (float) ((center.y * ratioGr) + (i.getPosition().y * ratioI)), (float) ((center.z * ratioGr) + (i.getPosition().z * ratioI)));
        items.put(s, i);
        positionAtCentroid();
    }


    /** remove the item i from the group, update the center and the weight
     *
     * @param itemId the item to remove
     */
    public void remove(String itemId) {
        items.remove(itemId);
        positionAtCentroid();
    }

    public void invalidate() {
        // do nothing for group
    }


    public Vector3f computeCentroid() {
        double x = 0, y = 0, z = 0;
        for (Item item  : items.values()) {
            x += item.getPosition().x ;
            y += item.getPosition().y ;
            z += item.getPosition().z ;

        }
        x /= (items.size()*1.f);
        y /= (items.size()*1.f);
        z /= (items.size()*1.f);
        return new Vector3f((float) x, (float) y, (float) z);
    }

    /**
     * Update the center of the group so that it is placed at the centroid
     * wrt. the contained items.
     * This must be called when the position of an item of the group also
     * changes.
     */
    public void positionAtCentroid(){
        setPosition(computeCentroid());
    }

    @Override
    public void translate(float tx, float ty, float tz) {
        super.translate(tx, ty, tz);
        for(Item item : items.values()) {
            item.translate(tx, ty, tz);
        }
    }

    protected Item fetchChild(String id) {
        Item child = items.get(id);
        if (child == null) {
            throw new Error("No such child in group: " + id);
        }
        return child;
    }

    @Override
    public void rotateX(float angle) {
        super.rotateX(angle);
        for(Item item : items.values()) {
            item.rotateXAround(angle, position);
        }
    }

    @Override
    public void rotateY(float angle) {
        super.rotateY(angle);
        for(Item item : items.values()) {
            item.rotateYAround(angle, position);
        }
    }

    @Override
    public void rotateZ(float angle) {
        super.rotateZ(angle);
        for(Item item : items.values()) {
            item.rotateZAround(angle, position);
        }
    }

    @Override
    public void rotateXYZ(float angleX, float angleY, float angleZ) {
        super.rotateXYZ(angleX, angleY, angleZ);
        for(Item item : items.values()) {
            item.rotateXYZAround(angleX, angleY, angleZ, position);
        }
    }

    @Override
    public void rotateAxis(float angle, Vector3f axis) {
        super.rotateAxis(angle, axis);
        for(Item item : items.values()) {
            item.rotateAxisAround(angle, axis, position);
        }
    }

    @Override
    public void rotateAxisAround(float angle, Vector3f axis, Vector3f center) {
        super.rotateAxisAround(angle, axis, center);
        for(Item item : items.values()) {
            item.rotateAxisAround(angle, axis, position);
        }
    }

}
