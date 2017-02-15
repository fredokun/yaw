package embla3d.engine;

import embla3d.engine.items.MyItem;
import embla3d.engine.meshs.Material;
import embla3d.engine.meshs.Mesh;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class representing a scene
 * we manage the rendering efficiency by splitting the meshes in two different structure
 * the first one (notInit) represent the mesh that must not be rendered ( we remove them from the gpu) unless we want to
 * and the second is a map where each mesh has a list of items
 */
public class SceneVertex {
    //why static ...
    public static boolean itemAdded = false;
    private HashMap<Mesh, ArrayList<MyItem>> meshMap;
    private ArrayList<Mesh> notInit;

    public SceneVertex() {
        meshMap = new HashMap<>();
        notInit = new ArrayList<>();
    }

    /**
     * Remove the specified item from the meshMap
     *
     * @param pItem item to be removed
     */
    public void removeItem(MyItem pItem) {
        ArrayList<MyItem> lItems = meshMap.get(pItem.getAppearance());
        lItems.remove(pItem);
    }

    /**
     * Retrieve all the items of the scene
     *
     * @return the list of item
     */
    public ArrayList<MyItem> getItemsList() {
        ArrayList<MyItem> lItems = new ArrayList<>();
        meshMap.values().forEach(lItems::addAll);
        return lItems;
    }

    /**
     * Accessor
     */
    public HashMap<Mesh, ArrayList<MyItem>> getMeshMap() {
        return meshMap;
    }

    /**
     * Invoke the method cleanup on all the active mesh
     */
    public void cleanUp() {
        for (Mesh m : meshMap.keySet()) {
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
     * Invoke the method draw on all mesh with associated items
     * otherwise clean then remove mesh which has an empty list of items
     *
     * @param pSh         Shader program that will draw
     * @param pViewMatrix the View Matrix
     */

    public void draw(ShaderProgram pSh, Matrix4f pViewMatrix) {
        ArrayList<Mesh> lRmListe = new ArrayList<>();
        for (Mesh m : meshMap.keySet()) {
            ArrayList<MyItem> lItems = meshMap.get(m);
            if (lItems.isEmpty()) {
                lRmListe.add(m);
            } else {
                m.draw(lItems, pSh, pViewMatrix);
            }
        }
        /*Clean then remove*/
        for (Mesh m : lRmListe) {
            m.cleanUp();
            meshMap.remove(m);
        }
    }

    // XXX: remove?
    /*Maybe
    public void update() {
        for (Mesh m : meshMap.keySet()) {
            for (MyItem i : meshMap.get(m))
                i.update();
        }
    }*/

    /**
     * Add the item in the map if the associated mesh is already a key
     * otherwise the association is created and the mesh is added to the nonInit List
     *
     * @param pItem the item
     */
    public synchronized void add(MyItem pItem) {
        itemAdded = true;
            /*retrieve the stored mesh in the item*/
        Mesh lMesh = pItem.getAppearance();

        if (meshMap.keySet().contains(lMesh)) {
            meshMap.get(lMesh).add(pItem);
        } else {
            ArrayList<MyItem> l = new ArrayList<>();
            l.add(pItem);
            meshMap.put(lMesh, l);
            notInit.add(lMesh);
        }
    }

    /**
     * Create a new item with the specified mesh then invoke the method add(Item)
     *
     * @param pMesh the mesh
     */
    public void add(Mesh pMesh) {
        MyItem lItem = new MyItem(pMesh);
        this.add(lItem);
    }

    /**
     * Create a new mesh with the specified arguments then invoke the method add(Mesh)
     *
     * @param pVertices the vertices
     * @param pMaterial the material
     * @param pNormals  the normals
     * @param pIndices  the indices
     */
    public void add(float[] pVertices, Material pMaterial, float[] pNormals, int[] pIndices) {
        Mesh lMesh = new Mesh(pVertices, pMaterial, pNormals, pIndices);
        add(lMesh);
    }

    /**
     * * Create a new mesh with the specified arguments then invoke the method add(Mesh scale rotation position)
     *
     * @param pVertices the vertices
     * @param pMaterial the material
     * @param pNormals  the normals
     * @param pIndices  the indices
     * @param pScale    the scale
     * @param pRotation the rotation
     * @param pPosition the position
     */
    public void add(float[] pVertices, Material pMaterial, float[] pNormals, int[] pIndices, float pScale, Vector3f pRotation, Vector3f pPosition) {
        Mesh lMesh = new Mesh(pVertices, pMaterial, pNormals, pIndices);
        this.add(lMesh, pScale, pRotation, pPosition);

    }

    /**
     * * Create a new mesh with the specified arguments then invoke the method add(Mesh)
     *
     * @param pVertices the vertices
     * @param pMaterial the material
     * @param pNormals  the normals
     * @param pIndices  the indices
     * @param pWeight   the weight
     */
    public void add(float[] pVertices, Material pMaterial, float[] pNormals, int[] pIndices, int pWeight) {
        Mesh lMesh = new Mesh(pVertices, pMaterial, pNormals, pIndices, pWeight);
        this.add(lMesh);

    }

    /**
     * * Create a new mesh with the specified arguments then invoke the method add(Mesh)
     *
     * @param pVertices the vertices
     * @param pMaterial the material
     * @param pNormals  the normals
     * @param pIndices  the indices
     * @param pWeight   the weight
     * @param pScale    the scale
     * @param pRotation the rotation
     * @param pPosition the position
     */
    public void add(float[] pVertices, Material pMaterial, float[] pNormals, int[] pIndices, int pWeight, float pScale, Vector3f pRotation, Vector3f pPosition) {
        Mesh lMesh = new Mesh(pVertices, pMaterial, pNormals, pIndices, pWeight);
        MyItem lItem = new MyItem(lMesh, pScale, pRotation, pPosition);
        this.add(lItem);
    }

    /**
     * * Create a new item with the specified arguments then invoke the method add(Item)
     *
     * @param pMesh     the mesh
     * @param pScale    the scale
     * @param pRotation the rotation
     * @param pPosition the position
     */
    public void add(Mesh pMesh, float pScale, Vector3f pRotation, Vector3f pPosition) {
        MyItem lItem = new MyItem(pMesh, pScale, pRotation, pPosition);
        this.add(lItem);
    }

    /**
     * Clone then add the item in the scene
     *
     * @param pItem the item
     */
    public void clone(MyItem pItem) {
        this.add(new MyItem(pItem));
    }
}
