package yaw.engine.collision;

import yaw.engine.items.HitBox;
import org.joml.Vector3f;
import org.joml.Vector4f;
import yaw.engine.items.ItemObject;

import java.util.ArrayList;

/**
 * Class used to detect collision between two boundingBox
 */
public class Collision {

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
