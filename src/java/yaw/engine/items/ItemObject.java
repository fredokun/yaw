package yaw.engine.items;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;
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



}
