package yaw.engine.items;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import yaw.engine.meshs.Material;
import yaw.engine.meshs.Mesh;

/**
 * Basic Item implementing all the Item methods. This is were all the moves are done for Items and Hitboxes
 */
public class ItemObject extends Item {

    protected Mesh mesh;



    public ItemObject(String pId, Vector3f pRotation, Vector3f pPosition, float pScale, Mesh mesh){
        super(pId,pRotation,pPosition,pScale);
        this.mesh= mesh;
    }




    public ItemObject(String pId, float[] pPosition, float pScale, Mesh mesh){
        super(pId,pPosition,pScale);
        this.mesh= mesh;
    }


    public ItemObject(ItemObject item){
        super(item);
    }






    @Override
    public Item clone() {
        return new ItemObject(this);
    }

    @Override
    public void rotate(float x, float y, float z) {
        this.setRotation(getRotation().add(x, y, z));
    }

    @Override
    public void rotateAround(float angle, float ax, float ay, float az, float cx, float cy, float cz) {

    }

    @Override
    public void translate(float x, float y, float z) {
        this.setPosition(this.getPosition().add(x,y,z));
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



    // MeshOld Gestion
    public Mesh getMesh(){ return mesh; }

    public float getReflectance() {
        return this.getMesh().getMaterial().getReflectance();
    }

    public void setReflectance(float refl) {
        this.getMesh().getMaterial().setReflectance(refl);
    }

    public Vector3f getColor() {
        return this.getMesh().getMaterial().getColor();
    }

    public void setColor(Vector3f color) {
        this.getMesh().setMaterial(new Material(color, 0.f));
    }


    // In case of input Feature
    @Override
    public void update() {

    }
}
