package embla3d.engine;

import embla3d.engine.items.Item;
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


        return listVertex;
    }
}
