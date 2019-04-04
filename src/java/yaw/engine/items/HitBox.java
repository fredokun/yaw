package yaw.engine.items;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;
import yaw.engine.collision.Collision;
import yaw.engine.meshs.Mesh;
import yaw.engine.meshs.MeshBuilder;

import java.util.ArrayList;

/**
 * HitBox is an ItemObject, but unlike a classical ItemObject we can check if there is a collision with other Hitboxes
 */
public class HitBox extends ItemObject {
    /**
     *
     * @param pId           Name of the HitBox
     * @param pRotation     Initial Rotation
     * @param pPosition     Initial Position
     * @param pScale        HitBox scale
     * @param mesh          Mesh associated to the HitBox
     */
    public HitBox(String id, Vector3f position, Quaternionf orientation, float scale
            , float xLength, float yLength, float zLength) {
        super(id, position, orientation, scale, MeshBuilder.generateBoundingBox(xLength, yLength, zLength));
    }


    /**    Testing the collision with another HitBox
     *
     * @param item      the second hitbox to test collision with
     * @return          true if item is touching the hitbox which is calling the method
     */
    public boolean isIsCollisionWith(HitBox item){

        // TODO: containment should be a collision

        ArrayList<Vector4f> listVertexboundingBox1 = Collision.tabToListVertex(this);
        ArrayList<Vector4f> listVertexboundingBox2 = Collision.tabToListVertex(item);

        // index of faces
        // implication of vertex in faces
        // example: face(0,1,2,3) / face(1,2,6,5) / ...
        int[] tabIndexFaces = {0, 1, 2, 3, 1, 2, 6, 5, 0, 3, 7, 4, 1, 5, 4, 0, 2, 3, 7, 6, 4, 5, 6, 7};

        // index of edges
        // implication of vertex in edges
        // example: edge(0,1) / edge(1,2) / edge(2,3) / ...
        int[] tabIndexEdges = {0, 1, 1, 2, 2, 3, 3, 0, 1, 5, 5, 4, 4, 0, 5, 6, 4, 7, 6, 7, 6, 2, 3, 7};

        // test if edges of boundingBox1 intersect faces of boundingBox2
        for (int i = 0; i < tabIndexEdges.length; i += 2)
            for (int j = 0; j < tabIndexFaces.length; j += 4)
                if (Collision.isIntersectSegmentAndFace(listVertexboundingBox1.get(tabIndexEdges[i]), listVertexboundingBox1.get(tabIndexEdges[i + 1]),
                        listVertexboundingBox2.get(tabIndexFaces[j]), listVertexboundingBox2.get(tabIndexFaces[j + 1]),
                        listVertexboundingBox2.get(tabIndexFaces[j + 2]), listVertexboundingBox2.get(tabIndexFaces[j + 3])))
                    return true;

        // XXX: the rest is superfluous ?

        // test if edges of boundingBox2 intersect faces of boundingBox1
        for (int i = 0; i < tabIndexEdges.length; i += 2)
            for (int j = 0; j < tabIndexFaces.length; j += 4)
                if (Collision.isIntersectSegmentAndFace(listVertexboundingBox2.get(tabIndexEdges[i]), listVertexboundingBox2.get(tabIndexEdges[i + 1]),
                        listVertexboundingBox1.get(tabIndexFaces[j]), listVertexboundingBox1.get(tabIndexFaces[j + 1]),
                        listVertexboundingBox1.get(tabIndexFaces[j + 2]), listVertexboundingBox1.get(tabIndexFaces[j + 3])))
                    return true;


        return false;
    }
}
