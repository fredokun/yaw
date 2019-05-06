package yaw.engine.items;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;
import yaw.engine.meshs.MeshBuilder;

import java.util.ArrayList;
import java.util.Vector;

/**
 * HitBox is an ItemObject, but unlike a classical ItemObject we can check if there is a collision with other Hitboxes
 */
public class HitBox extends ItemObject {
    /**
     * @param id          Name of the HitBox
     * @param orientation Initial Rotation
     * @param position    Initial Position
     * @param scale       HitBox scale
     */
    public HitBox(String id, Vector3f position, Quaternionf orientation, float scale
            , float xLength, float yLength, float zLength) {
        super(id, position, orientation, scale, MeshBuilder.generateBoundingBox(xLength, yLength, zLength));
        getMesh().getMaterial().setColor(new Vector3f(0, 255, 0));
    }


    /**
     * Testing the collision with another HitBox
     *
     * @param item the second hitbox to test collision with
     * @return true if item is touching the hitbox which is calling the method
     */
    public boolean collidesWith(HitBox item) {
        ArrayList<Vector4f> listVertexboundingBox1 = tabToListVertex(this);
        ArrayList<Vector4f> listVertexboundingBox2 = tabToListVertex(item);

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
                if (isIntersectSegmentAndFace(listVertexboundingBox1.get(tabIndexEdges[i]), listVertexboundingBox1.get(tabIndexEdges[i + 1]),
                        listVertexboundingBox2.get(tabIndexFaces[j]), listVertexboundingBox2.get(tabIndexFaces[j + 1]),
                        listVertexboundingBox2.get(tabIndexFaces[j + 2]), listVertexboundingBox2.get(tabIndexFaces[j + 3])))
                    return true;
        return false;
    }

    /**
     * Testing the inclusion of a hitbox in another
     *
     * @param item the second Hitbox to test the inclusion
     * @return true if the calling Hitbox is Contained in the one placed as an argument
     */
    public boolean isIncludedIn(HitBox item) {

        ArrayList<Vector4f> listVertexboundingBox1 = tabToListVertex(this);
        ArrayList<Vector4f> listVertexboundingBox2 = tabToListVertex(item);

        int[] tabIndexFaces = {0, 1, 2, 3, 1, 2, 6, 5, 0, 3, 7, 4, 1, 5, 4, 0, 2, 3, 7, 6, 4, 5, 6, 7};
        int[] tabOppositeVertice = {4, 3, 2, 2, 1, 0 };
        //if the two hitboxes collide there is no containment
        if (!this.collidesWith(item)) {
            boolean isIn = true;
            int k = 0;
            for(int i =0; i<tabIndexFaces.length;i+=4){
                //Using 3 points to create a plane for the face
                Vector4f A = listVertexboundingBox2.get(tabIndexFaces[i]);
                Vector4f B = listVertexboundingBox2.get(tabIndexFaces[i+1]);
                Vector4f C = listVertexboundingBox2.get(tabIndexFaces[i+2]);
                // Creating two vectors with these points
                Vector3f AB = new Vector3f(B.x-A.x, B.y-A.y,B.z-A.z);
                Vector3f AC = new Vector3f(C.x-A.x, C.y-A.y,C.z-A.z);
                //Cross product of the two vectors
                Vector3f crossProductABC = new Vector3f((AB.y*AC.z)-(AB.z*AC.y),(AB.z*AC.x)-(AB.x*AC.z),(AB.x*AC.y)-(AB.y*AC.x));
                // Taking a point that we know it don't belongs to the plane
                Vector4f opposite = listVertexboundingBox2.get(tabOppositeVertice[k]);
                Vector3f opp = new Vector3f(opposite.x-A.x, opposite.y-A.y, opposite.z-A.z);
                // computing the dot product to get it's sign and to know what is "the right face" of the plane (negative dot product or positive)
                float dotProductABC_Op = crossProductABC.x*opp.x+crossProductABC.y*opp.y+crossProductABC.z*opp.z;


                //Taking one point of the contained HitBox
                Vector4f point = listVertexboundingBox1.get(0);
                Vector3f p = new Vector3f(point.x-A.x, point.y-A.y , point.z-A.z);
                // computing the dot product to get it's sign
                float  dotProductABC_P = crossProductABC.x*p.x+crossProductABC.y*p.y+crossProductABC.z*p.z;
                //if the two dot products have different signs, the point is not on the right face so it's not contained in the HitBox
                if(dotProductABC_Op*dotProductABC_P<0){
                    isIn=false;
                }
                k++;
            }
            //returns true if all the dotProducts have the right sign
            return isIn;
        }else{
            return false;
        }
    }



    /**
     * Testing the inclusion of a hitbox in another
     *
     * @param item the second Hitbox to test the inclusion
     * @return true if the calling Hitbox contains the one placed as an argument
     */
    public boolean includes(HitBox item) {

        ArrayList<Vector4f> listVertexboundingBox1 = tabToListVertex(item);
        ArrayList<Vector4f> listVertexboundingBox2 = tabToListVertex(this);

        int[] tabIndexFaces = {0, 1, 2, 3, 1, 2, 6, 5, 0, 3, 7, 4, 1, 5, 4, 0, 2, 3, 7, 6, 4, 5, 6, 7};
        int[] tabOppositeVertice = {4, 3, 2, 2, 1, 0 };
        //if the two hitboxes collide there is no containment
        if (!this.collidesWith(item)) {
            boolean isIn = true;
            int k = 0;
            for(int i =0; i<tabIndexFaces.length;i+=4){
                //Using 3 points to create a plane for the face
                Vector4f A = listVertexboundingBox2.get(tabIndexFaces[i]);
                Vector4f B = listVertexboundingBox2.get(tabIndexFaces[i+1]);
                Vector4f C = listVertexboundingBox2.get(tabIndexFaces[i+2]);
                // Creating two vectors with these points
                Vector3f AB = new Vector3f(B.x-A.x, B.y-A.y,B.z-A.z);
                Vector3f AC = new Vector3f(C.x-A.x, C.y-A.y,C.z-A.z);
                //Cross product of the two vectors
                Vector3f crossProductABC = new Vector3f((AB.y*AC.z)-(AB.z*AC.y),(AB.z*AC.x)-(AB.x*AC.z),(AB.x*AC.y)-(AB.y*AC.x));
                // Taking a point that we know it don't belongs to the plane
                Vector4f opposite = listVertexboundingBox2.get(tabOppositeVertice[k]);
                Vector3f opp = new Vector3f(opposite.x-A.x, opposite.y-A.y, opposite.z-A.z);
                // computing the dot product to get it's sign and to know what is "the right face" of the plane (negative dot product or positive)
                float dotProductABC_Op = crossProductABC.x*opp.x+crossProductABC.y*opp.y+crossProductABC.z*opp.z;


                //Taking one point of the contained HitBox
                Vector4f point = listVertexboundingBox1.get(0);
                Vector3f p = new Vector3f(point.x-A.x, point.y-A.y , point.z-A.z);
                // computing the dot product to get it's sign
                float  dotProductABC_P = crossProductABC.x*p.x+crossProductABC.y*p.y+crossProductABC.z*p.z;
                //if the two dot products have different signs, the point is not on the right face so it's not contained in the HitBox
                if(dotProductABC_Op*dotProductABC_P<0){
                    isIn=false;
                }
                k++;
            }
            //returns true if all the dotProducts have the right sign
            return isIn;
        }else{
            return false;
        }
    }





    /**
     * Returns true if the segment passes through the face
     * Segment define by 2 points (segmentStart, segmentEnd)
     * face define by 4 points (corner1Face, corner2Face, corner3Face, corner4Face)
     *
     * @param segmentStart
     * @param segmentEnd
     * @param corner1Face
     * @param corner2Face
     * @param corner3Face
     * @param corner4Face
     * @return
     */
    public static boolean isIntersectSegmentAndFace(Vector4f segmentStart, Vector4f segmentEnd, Vector4f corner1Face, Vector4f corner2Face, Vector4f corner3Face, Vector4f corner4Face) {

        Vector3f vecSegment = new Vector3f(segmentEnd.x - segmentStart.x, segmentEnd.y - segmentStart.y, segmentEnd.z - segmentStart.z);

        Vector3f vecCD = new Vector3f(corner2Face.x - corner1Face.x, corner2Face.y - corner1Face.y, corner2Face.z - corner1Face.z);

        Vector3f vecCF = new Vector3f(corner4Face.x - corner1Face.x, corner4Face.y - corner1Face.y, corner4Face.z - corner1Face.z);

        float a1 = vecSegment.x;
        float a2 = vecSegment.y;
        float a3 = vecSegment.z;

        float b1 = -vecCD.x;
        float b2 = -vecCD.y;
        float b3 = -vecCD.z;

        float c1 = -vecCF.x;
        float c2 = -vecCF.y;
        float c3 = -vecCF.z;

        float d1 = segmentStart.x - corner1Face.x;
        float d2 = segmentStart.y - corner1Face.y;
        float d3 = segmentStart.z - corner1Face.z;


        float det = -(a1 * (b2 * c3 - c2 * b3) - b1 * (a2 * c3 - a3 * c2) + c1 * (a2 * b3 - a3 * b2));
        if (det != 0) {
            float x = (d1 * (b2 * c3 - c2 * b3) - b1 * (d2 * c3 - d3 * c2) + c1 * (d2 * b3 - d3 * b2)) / (det);
            float y = (a1 * (d2 * c3 - c2 * d3) - d1 * (a2 * c3 - a3 * c2) + c1 * (a2 * d3 - a3 * d2)) / (det);
            float z = (a1 * (b2 * d3 - d2 * b3) - b1 * (a2 * d3 - a3 * d2) + d1 * (a2 * b3 - a3 * b2)) / (det);


            if (x >= 0 && x <= 1 && y >= 0 && y <= 1 && z >= 0 && z <= 1)
                return true;
            else
                return false;

        }
        return false;
    }
    //Utility methods
    /**
     * Construct a list of vertex with the vertices of specified item
     *
     * @param item item
     * @return listVertex
     */
    public static ArrayList<Vector4f> tabToListVertex(HitBox item) {
        ArrayList<Vector4f> listVertex = new ArrayList<>();
        for (int i = 0; i < item.getMesh().getVertices().length; i += 3) {
            Vector4f vec = new Vector4f(item.getMesh().getVertices()[i], item.getMesh().getVertices()[i + 1]
                    , item.getMesh().getVertices()[i + 2], 1.0f);
            if (!isVecAlreadyAdd(listVertex, vec))
                listVertex.add(vec);
        }

        //get Real position of vertex after rotation or translation
        for (int i = 0; i < listVertex.size(); i++)
            listVertex.set(i, listVertex.get(i).mul(item.getWorldMatrix()));


        return listVertex;
    }

    public static boolean isVecAlreadyAdd(ArrayList<Vector4f> listVertex, Vector4f vec) {
        for (Vector4f vector : listVertex) {
            if (vector.x == vec.x && vector.y == vec.y && vector.z == vec.z)
                return true;
        }
        return false;
    }

}

