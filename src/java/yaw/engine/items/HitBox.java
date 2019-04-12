package yaw.engine.items;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;
import yaw.engine.meshs.MeshBuilder;

import java.util.ArrayList;

/**
 * HitBox is an ItemObject, but unlike a classical ItemObject we can check if there is a collision with other Hitboxes
 */
public class HitBox extends ItemObject {
    /**
     *
     * @param id           Name of the HitBox
     * @param orientation     Initial Rotation
     * @param position     Initial Position
     * @param scale        HitBox scale

     */
    public HitBox(String id, Vector3f position, Quaternionf orientation, float scale
            , float xLength, float yLength, float zLength) {
        super(id, position, orientation, scale, MeshBuilder.generateBoundingBox(xLength, yLength, zLength));
        getMesh().getMaterial().setColor(new Vector3f(0,255,0));
    }


    /**    Testing the collision with another HitBox
     *
     * @param item      the second hitbox to test collision with
     * @return          true if item is touching the hitbox which is calling the method
     */
    public boolean intersect(HitBox item){

        // TODO: containment should be a collision

        ArrayList<Vector4f> listVertexHitBox1 = tabToListVertex(this);
        ArrayList<Vector4f> listVertexHitBox2 = tabToListVertex(item);

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
                if (isIntersectSegmentAndFace(listVertexHitBox1.get(tabIndexEdges[i]), listVertexHitBox1.get(tabIndexEdges[i + 1]),
                        listVertexHitBox2.get(tabIndexFaces[j]), listVertexHitBox2.get(tabIndexFaces[j + 1]),
                        listVertexHitBox2.get(tabIndexFaces[j + 2]), listVertexHitBox2.get(tabIndexFaces[j + 3])))
                    return true;


        return false;
    }

    /**This method compute the max and min coordinates of the two hitboxes then check if the calling hitbox coordinates are bounded by the second one
     *
     * @param item the item with which the containment is check
     * @return
     */
    public boolean isContainedIn(HitBox item){
        boolean isAPointIn = false;
        ArrayList<Vector4f> listVertexHitBox1 = tabToListVertex(this);
        ArrayList<Vector4f> listVertexHitBox2 = tabToListVertex(item);

        //int[] tabOppositeVertex = {0,6,1,7,2,4,3,5};
        int[] tabOppositeVertex = {6,7,4,5,2,3,0,1};

        if(!this.intersect(item)) {
            // TODO ca marche pas

            for (int i = 0; i < listVertexHitBox1.size(); i++) {
                for (int j = 0; j < listVertexHitBox2.size(); j++) {

                    if ((listVertexHitBox1.get(i).x > listVertexHitBox2.get(j).x && listVertexHitBox1.get(i).x < listVertexHitBox2.get(tabOppositeVertex[j]).x) ||
                            (listVertexHitBox1.get(i).x < listVertexHitBox2.get(j).x && listVertexHitBox1.get(i).x > listVertexHitBox2.get(tabOppositeVertex[j]).x)) {
                        System.out.println(j + " = " + listVertexHitBox2.get(j).y + " / " + tabOppositeVertex[j] + " = " + listVertexHitBox2.get(tabOppositeVertex[j]).y);
                        System.out.println("get(" + i + ").x = " + listVertexHitBox1.get(i).y);
                        if ((listVertexHitBox1.get(i).y > listVertexHitBox2.get(j).y && listVertexHitBox1.get(i).y < listVertexHitBox2.get(tabOppositeVertex[j]).y) ||
                                (listVertexHitBox1.get(i).y < listVertexHitBox2.get(j).y && listVertexHitBox1.get(i).y > listVertexHitBox2.get(tabOppositeVertex[j]).y)) {
                            System.out.println(i + " / " + j + " 2eme");

                            if ((listVertexHitBox1.get(i).z > listVertexHitBox2.get(j).z && listVertexHitBox1.get(i).z < listVertexHitBox2.get(tabOppositeVertex[j]).z) ||
                                    (listVertexHitBox1.get(i).z < listVertexHitBox2.get(j).z && listVertexHitBox1.get(i).z > listVertexHitBox2.get(tabOppositeVertex[j]).z)) {
                                isAPointIn = true;
                                break;
                            }
                        }
                    }
                }
                if (isAPointIn) {
                    break;
                }
            }
            return isAPointIn;
        }
        return false;




    }


    /**same as isContainedIn, but inverted.
     *
     * @param item
     * @return
     */
    public boolean contains(HitBox item){
        ArrayList<Vector4f> listVertexHitBox2 = tabToListVertex(this);
        ArrayList<Vector4f> listVertexHitBox1 = tabToListVertex(item);
        float max_x_1 = listVertexHitBox1.get(0).x;
        float max_y_1 = listVertexHitBox1.get(0).y;
        float max_z_1 = listVertexHitBox1.get(0).z;

        float min_x_1 = listVertexHitBox1.get(0).x;
        float min_y_1 = listVertexHitBox1.get(0).y;
        float min_z_1 = listVertexHitBox1.get(0).z;

        float max_x_2 = listVertexHitBox2.get(0).x;
        float max_y_2 = listVertexHitBox2.get(0).y;
        float max_z_2 = listVertexHitBox2.get(0).z;

        float min_x_2 = listVertexHitBox2.get(0).x;
        float min_y_2 = listVertexHitBox2.get(0).y;
        float min_z_2 = listVertexHitBox2.get(0).z;
        //Hitboxes are only parallelepiped, so they are supposed to have the same number of vertex.
        for(int i=0; i<listVertexHitBox2.size(); i++) {
            //Computation of the max/min coordinates for this hitbox
            if (max_x_1 < listVertexHitBox1.get(i).x) {
                max_x_1 = listVertexHitBox1.get(i).x;
            }
            if (min_x_1 > listVertexHitBox1.get(i).x) {
                min_x_1 = listVertexHitBox1.get(i).x;
            }

            if (max_y_1 < listVertexHitBox1.get(i).y) {
                max_y_1 = listVertexHitBox1.get(i).y;
            }
            if (min_y_1 > listVertexHitBox1.get(i).y) {
                min_y_1 = listVertexHitBox1.get(i).y;
            }

            if (max_z_1 < listVertexHitBox1.get(i).z) {
                max_z_1 = listVertexHitBox1.get(i).z;
            }
            if (min_z_1 > listVertexHitBox1.get(i).z) {
                min_z_1 = listVertexHitBox1.get(i).z;
            }


            // computation of the max/min coordinates for the second hitbox
            if (max_x_2 < listVertexHitBox2.get(i).x) {
                max_x_2 = listVertexHitBox2.get(i).x;
            }
            if (min_x_2 > listVertexHitBox2.get(i).x) {
                min_x_2 = listVertexHitBox2.get(i).x;
            }

            if (max_y_2 < listVertexHitBox2.get(i).y) {
                max_y_2 = listVertexHitBox2.get(i).y;
            }
            if (min_y_2 > listVertexHitBox2.get(i).y) {
                min_y_2 = listVertexHitBox2.get(i).y;
            }

            if (max_z_2 < listVertexHitBox2.get(i).z) {
                max_z_2 = listVertexHitBox2.get(i).z;
            }
            if (min_z_2 > listVertexHitBox2.get(i).z) {
                min_z_2 = listVertexHitBox2.get(i).z;
            }

        }
        return (max_x_1 <= max_x_2 && max_y_1 <= max_y_2 && max_z_1 <= max_z_2 && min_x_1 >= min_x_2 && min_y_1 >= min_y_2 && min_z_1 >= min_z_2 );

    }


    public boolean isInCollisionWith(HitBox item){
        return (this.isContainedIn(item) || this.contains(item) || this.intersect(item));
    }





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
