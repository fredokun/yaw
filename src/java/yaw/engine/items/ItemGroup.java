package yaw.engine.items;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import yaw.engine.meshs.Material;

import java.util.HashMap;
import java.util.Map;

/**
 *  Group management
 */
public class ItemGroup extends Item {
    private Map<String, ItemObject> items;
    private float weight;


    public ItemGroup(String pId, Vector3f pRotation, Vector3f pPosition, float pScale){
       super(pId,pRotation,pPosition,pScale);
       items = new HashMap<String, ItemObject>();
       weight = 0;
    }



    public ItemGroup(ItemGroup ig){
        this(ig.mId,ig.mRotation,ig.mPosition,ig.mScale);
    }



    public ItemGroup() {
        this("Group", new Vector3f(), new Vector3f(),1);
    }

    @Override
    public Item clone() {
        return new ItemGroup(this);
    }

    /** this method adds an item to the group, updates the weight and the center.
     *
     * @param s name of the add item
     * @param i the Item
     */
    public void add(String s, ItemObject i) {
        float nWeight = weight + i.getMesh().getWeight();
        double ratioGr = weight / (double) nWeight, ratioI = i.getMesh().getWeight() / (double) nWeight;
        weight = nWeight;
        //center = new Vector3f((float) ((center.x * ratioGr) + (i.getPosition().x * ratioI)), (float) ((center.y * ratioGr) + (i.getPosition().y * ratioI)), (float) ((center.z * ratioGr) + (i.getPosition().z * ratioI)));
        items.put(s, i);
        updateCenter();
    }


    /** remove the item i from the group, update the center and the weight
     *
     * @param i the item to remove
     */
    public void remove(ItemObject i) {
        items.remove(i);
        this.weight -= i.getMesh().getWeight();
        updateCenter();
    }


    /**
     * Update the center of the group
     */
    public void updateCenter(){
        double x = 0, y = 0, z = 0;
        for (String s  : items.keySet()) {
            Item tmp = items.get(s);
            int w = items.get(s).getMesh().getWeight();
            x += tmp.getPosition().x ;
            y += tmp.getPosition().y ;
            z += tmp.getPosition().z ;

        }
        x /= (items.size()*1.f);
        y /= (items.size()*1.f);
        z /= (items.size()*1.f);
        mPosition = new Vector3f((float) x, (float) y, (float) z);
    }

    @Override
    public void rotate(float angle, float ax, float ay, float az) {

        for (Item item : items.values()){
            item.rotateAround(angle, ax, ay, az, mPosition.x, mPosition.y, mPosition.z);
        }

    }

    @Override
    public void rotateAround(float angle, float ax, float ay, float az, float cx, float cy, float cz) {
        for(Item item : items.values()) {
            item.rotateAround(angle, ax, ay, az, cx, cy, cz);
        }
        Vector3f center = new Vector3f(cx, cy, cz);
        new Matrix4f().translate(center)
                .rotate((float) Math.toRadians(angle), ax, ay, az)
                .translate(center.negate())
                .transformPosition(mPosition);
    }

    @Override
    public void translate(float x, float y, float z) {
        Vector3f translation = new Vector3f(x, y, z);
        for (String s  : items.keySet()){
            items.get(s).getPosition().add(translation);
        }
        updateCenter();

    }

    @Override
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

    @Override
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


    public Item fetchItem(String s ){
        try {
            return items.get(s);
        }catch(NullPointerException e){
            System.out.println("Item named s is not in the ItemOld Map");
            return null;
        }
    }

    @Override
    public void update() {

    }





    public void setPosition(float x, float y, float z) {
        setPosition(new Vector3f(x, y, z));
    }

    public void setPosition(Vector3f pos) {
        float x = pos.x - mPosition.x, y = pos.y - mPosition.y, z = pos.z - mPosition.z;
        for (String s : items.keySet()) {
            ItemObject i = items.get(s);
            i.translate(x, y, z);
            items.put(s,i);
        }
        mPosition = pos;
    }

    public void separate(float dist) {
        for (String s : items.keySet()) {
            ItemObject i = items.get(s);
            i.repelBy(mPosition, dist);
        }
    }

    public void setColor(float r, float g, float b) {
        for (String s : items.keySet()) {
            ItemObject i = items.get(s);
            i.getMesh().setMaterial(new Material(new Vector3f(r, g, b), 0.f));
            items.put(s,i);
        }
    }

    public void setColor(Vector3f color) {
        for (String s : items.keySet()) {
            ItemObject i = items.get(s);
            i.getMesh().setMaterial(new Material(color, 0.f));
            items.put(s,i);
        }
    }

    public void multScale(float val) {
        for (String s : items.keySet()) {
            ItemObject i = items.get(s);
            i.setScale(i.getScale() * val);
            items.put(s, i);
        }
    }

    public Map<String, ItemObject> getItems(){
            return items;

    }





}
