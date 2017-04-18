package embla3d.engine;

import clojure.core.Vec;
import embla3d.engine.items.Item;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;

/**
 * Class for collision
 */
public class Collision {

    public static boolean isAlreadyAdd(ArrayList<Vector4f> listVertex, Vector4f vec)
    {
        for(Vector4f vector: listVertex)
        {
            if(vector.x == vec.x && vector.y == vec.y && vector.z == vec.z)
                return true;
        }
        return false;
    }

    public static ArrayList<Vector4f>  tabToListVertex(Item item)
    {
        ArrayList<Vector4f> listVertex = new ArrayList<Vector4f>();
        for(int i=0; i<item.getAppearance().getVertices().length;i+=3)
        {
            Vector4f vec = new Vector4f(item.getAppearance().getVertices()[i], item.getAppearance().getVertices()[i+1]
                    , item.getAppearance().getVertices()[i+2], 1.0f);
            if(!isAlreadyAdd(listVertex, vec))
                listVertex.add(vec);
        }

        //get Real position of vertex after rotation or translation
        for(int i=0; i<listVertex.size();i++)
            listVertex.set(i, listVertex.get(i).mul(item.getWorldMatrix()));


        return listVertex;
    }

    public static boolean isInCollision(Item boundingBox1, Item boundingBox2)
    {
        ArrayList<Vector4f> listVertexboundingBox1 = tabToListVertex(boundingBox1);
        ArrayList<Vector4f> listVertexboundingBox2 = tabToListVertex(boundingBox2);

        int []tabIndexFaces = {0,1,2,3,1,2,6,5,0,3,7,4,1,5,4,0,2,3,7,6,4,5,6,7};
        int []tabIndexEdges = {0,1,1,2,2,3,3,0,1,5,5,4,4,0,5,6,4,7,6,7,6,2,3,7};

        // test if edge of boundingBox1 intersect face of boundingBox2
        for(int i=0; i<tabIndexEdges.length; i+=2)
            for(int j=0; j<tabIndexFaces.length; j+=4 )
                if(isIntersectSegmentAndFace(listVertexboundingBox1.get(tabIndexEdges[i]), listVertexboundingBox1.get(tabIndexEdges[i+1]),
                        listVertexboundingBox2.get(tabIndexFaces[j]), listVertexboundingBox2.get(tabIndexFaces[j+1]),
                        listVertexboundingBox2.get(tabIndexFaces[j+2]), listVertexboundingBox2.get(tabIndexFaces[j+3])))
                        return true;

        // test if edge of boundingBox2 intersect face of boundingBox1
        for(int i=0; i<tabIndexEdges.length; i+=2)
            for(int j=0; j<tabIndexFaces.length; j+=4 )
                if(isIntersectSegmentAndFace(listVertexboundingBox2.get(tabIndexEdges[i]), listVertexboundingBox2.get(tabIndexEdges[i+1]),
                        listVertexboundingBox1.get(tabIndexFaces[j]), listVertexboundingBox1.get(tabIndexFaces[j+1]),
                        listVertexboundingBox1.get(tabIndexFaces[j+2]), listVertexboundingBox1.get(tabIndexFaces[j+3])))
                    return true;


        return false;
    }

    public static boolean isIntersectSegmentAndFace(Vector4f segmentStart, Vector4f segmentEnd, Vector4f corner1Face, Vector4f corner2Face, Vector4f corner3Face, Vector4f corner4Face)
    {

        Vector3f vecSegment = new Vector3f(segmentEnd.x-segmentStart.x, segmentEnd.y-segmentStart.y, segmentEnd.z-segmentStart.z );

        Vector3f vecCD= new Vector3f(corner2Face.x - corner1Face.x, corner2Face.y - corner1Face.y, corner2Face.z - corner1Face.z);

        Vector3f vecCF= new Vector3f(corner4Face.x - corner1Face.x, corner4Face.y - corner1Face.y, corner4Face.z - corner1Face.z);

        float a1 = vecSegment.x;
        float a2 = vecSegment.y;
        float a3 = vecSegment.z;

        float b1 = - vecCD.x;
        float b2 = - vecCD.y;
        float b3 = - vecCD.z;

        float c1 = - vecCF.x;
        float c2 = - vecCF.y;
        float c3 = - vecCF.z;

        float d1 =  segmentStart.x - corner1Face.x;
        float d2 =  segmentStart.y - corner1Face.y;
        float d3 =  segmentStart.z - corner1Face.z;


        float det=-(a1*(b2*c3-c2*b3)-b1*(a2*c3-a3*c2)+c1*(a2*b3-a3*b2));
        if(det!=0)
        {
            float x=(d1*(b2*c3-c2*b3)-b1*(d2*c3-d3*c2)+c1*(d2*b3-d3*b2))/(det);
            float y=(a1*(d2*c3-c2*d3)-d1*(a2*c3-a3*c2)+c1*(a2*d3-a3*d2))/(det);
            float z=(a1*(b2*d3-d2*b3)-b1*(a2*d3-a3*d2)+d1*(a2*b3-a3*b2))/(det);


            if(x>=0 && x<=1 && y>=0 && y<=1 && z>=0 && z<=1)
                return true;
            else
                return false;

        }
        return false;
    }
}
