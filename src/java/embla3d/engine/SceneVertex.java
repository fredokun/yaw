package embla3d.engine;

import embla3d.engine.items.Item;
import embla3d.engine.meshs.Mesh;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class representing a scene
 * we manage the rendering efficiency by splitting the meshes in two different structure
 * the first one (notInit) represent the mesh that must not be rendered ( we remove them from the gpu) unless we want to
 * and the second is a map where each mesh has a list of items
 */
public class SceneVertex {
    //old code from a previous attempt to manage a group of scene vertex
    public static boolean itemAdded = false;
    private HashMap<Mesh, ArrayList<Item>> mMeshMap;
    private ArrayList<Mesh> notInit;

    public SceneVertex() {
        mMeshMap = new HashMap<>();
        notInit = new ArrayList<>();
    }

    /**
     * Remove the specified item from the mMeshMap
     *
     * @param pItem item to be removed
     */
    public void removeItem(Item pItem) {
        ArrayList<Item> lItems = mMeshMap.get(pItem.getAppearance());
        lItems.remove(pItem);
    }

    /**
     * Retrieve all the items of the scene
     *
     * @return the list of item
     */
    public ArrayList<Item> getItemsList() {
        ArrayList<Item> lItems = new ArrayList<>();
        mMeshMap.values().forEach(lItems::addAll);
        return lItems;
    }

    /**
     * Accessor
     */
    public HashMap<Mesh, ArrayList<Item>> getMeshMap() {
        return mMeshMap;
    }

    /**
     * Invoke the method cleanup on all the active mesh
     */
    public void cleanUp() {
        for (Mesh m : mMeshMap.keySet()) {
            m.cleanUp();
        }
    }

    /**
     * Invoke the method init on all the mesh in the non initialize mesh collection
     */
    public void initMesh() {
        for (Mesh m : notInit) {
            m.init();
        }
        notInit.clear();
    }

    /**
     * Invoke the method render on all mesh with associated items
     * otherwise clean then remove mesh which has an empty list of items
     *
     * @param pShaderProgram         Shader program that will render
     * @param pViewMatrix the View Matrix
     */

    public void draw(ShaderProgram pShaderProgram, Matrix4f pViewMatrix) {
        ArrayList<Mesh> lRmListe = new ArrayList<>();
        for (Mesh m : mMeshMap.keySet()) {
            ArrayList<Item> lItems = mMeshMap.get(m);
            if (lItems.isEmpty()) {
                lRmListe.add(m);
            } else {
                m.render(lItems, pShaderProgram, pViewMatrix);
            }
        }
        /*Clean then remove*/
        for (Mesh m : lRmListe) {
            m.cleanUp();
            mMeshMap.remove(m);
        }
    }

    // XXX: remove?
    /*Maybe
    public void update() {
        for (Mesh m : mMeshMap.keySet()) {
            for (Item i : mMeshMap.get(m))
                i.update();
        }
    }*/

    /**
     * Clone then add the item in the scene
     *
     * @param pItem the item
     */
    public void clone(Item pItem) {
        this.add(new Item(pItem));
    }

    /**
     * Add the item in the map if the associated mesh is already a key
     * otherwise the association is created and the mesh is added to the nonInit List
     *
     * @param pItem the item
     */
    public synchronized void add(Item pItem) {
        itemAdded = true;
            /*retrieve the stored mesh in the item*/
        Mesh lMesh = pItem.getAppearance();

        if (mMeshMap.keySet().contains(lMesh)) {
            mMeshMap.get(lMesh).add(pItem);
        } else {
            ArrayList<Item> l = new ArrayList<>();
            l.add(pItem);
            mMeshMap.put(lMesh, l);
            notInit.add(lMesh);
        }
    }
}
