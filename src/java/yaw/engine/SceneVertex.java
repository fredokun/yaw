package yaw.engine;

import yaw.engine.items.Item;
import yaw.engine.meshs.Mesh;
import yaw.engine.shader.ShaderProgram;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class representing a scene
 * we manage the rendering efficiency by splitting the meshes in two different structure
 * the first one (notInit) represent the mesh that must not be rendered ( we remove them from the gpu) unless we want to
 * and the second is a map where each mesh has a list of items
 */
public class SceneVertex {
    //old code from a previous attempt to manage a group of scene vertex
    private boolean itemAdded = false;
    private HashMap<Mesh, List<Item>> mMeshMap;
    private ArrayList<Mesh> notInit;


    public SceneVertex() {
        mMeshMap = new HashMap<>();
        notInit = new ArrayList<>();
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
        List<Item> lItems = mMeshMap.get(lMesh);
        if (lItems == null) {
            lItems = new ArrayList<>();
            mMeshMap.put(lMesh, lItems);
            notInit.add(lMesh);
        }
        lItems.add(pItem);

    }

    /**
     * Remove the specified item from the mMeshMap
     *
     * @param pItem item to be removed
     */
    public void removeItem(Item pItem) {
        List<Item> lItems = mMeshMap.get(pItem.getAppearance());
        lItems.remove(pItem);
    }

    /**
     * Invoke the method cleanup on all the active mesh
     */
    public void cleanUp() {
        for (Mesh lMesh : mMeshMap.keySet()) {
            lMesh.cleanUp();
        }
    }

    /**
     * Invoke the method init on all the mesh in the non initialize mesh collection
     */
    public void initMesh() {
        for (Mesh lMesh : notInit) {
            lMesh.init();
        }
        notInit.clear();
    }

    /**
     * Invoke the method render on all mesh with associated items
     * otherwise clean then remove mesh which has an empty list of items
     *
     * @param pShaderProgram Shader program that will render
     * @param pViewMatrix    the View Matrix
     */

    public void draw(ShaderProgram pShaderProgram, Matrix4f pViewMatrix) {
        List<Mesh> lRmListe = new ArrayList<>();
        for (Mesh lMesh : mMeshMap.keySet()) {
            List<Item> lItems = mMeshMap.get(lMesh);
            if (lItems.isEmpty()) {
                lRmListe.add(lMesh);
            } else {
                lMesh.render(lItems, pShaderProgram, pViewMatrix);
            }
        }
        /*Clean then remove*/
        for (Mesh lMesh : lRmListe) {
            lMesh.cleanUp();
            mMeshMap.remove(lMesh);
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
     * Retrieve all the items of the scene
     *
     * @return the list of item
     */
    public ArrayList<Item> getItemsList() {
        ArrayList<Item> lItems = new ArrayList<>();
        mMeshMap.values().forEach(lItems::addAll);
        return lItems;
    }

    public boolean isItemAdded() {
        return itemAdded;
    }
}
