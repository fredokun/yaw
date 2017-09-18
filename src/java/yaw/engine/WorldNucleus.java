package yaw.engine;

import yaw.engine.camera.Camera;
import yaw.engine.items.Item;
import yaw.engine.items.ItemGroup;
import yaw.engine.items.ItemManagement;
import yaw.engine.light.SceneLight;
import yaw.engine.meshs.Material;
import yaw.engine.meshs.Mesh;
import yaw.engine.meshs.Texture;
import yaw.engine.meshs.strategy.DefaultDrawingStrategy;
import yaw.engine.skybox.Skybox;
import org.joml.Vector3f;

/**
 * Singleton
 * All new instance must be handle by the nucleus
 * TODO USE intermediate factory/management tools
 */
public class WorldNucleus {
    private static WorldNucleus ourInstance = new WorldNucleus();

    public static WorldNucleus getInstance() {
        return ourInstance;
    }

    private WorldNucleus() {
    }


    public Renderer createRenderer() {
        return new Renderer();
    }

    public Camera createCamera() {
        return new Camera();
    }

    public SceneVertex createSceneVertex() {
        return new SceneVertex();
    }

    public SceneLight createSceneLight() {
        return new SceneLight();
    }

    public Callback createCallback() {
        return new Callback();
    }

    public Material createMaterial(Vector3f pMaterialColor) {
        return new Material(pMaterialColor);
    }

    public Texture createTexture(String pFileName) {
        return new Texture(pFileName);
    }

    public Mesh createMesh(float[] pVertices, float[] pTextCoords, float[] pNormals, int[] pIndices, int pWeight) {
        Mesh lMesh = new Mesh(pVertices, pTextCoords, pNormals, pIndices, pWeight);
        lMesh.setDrawingStrategy(new DefaultDrawingStrategy());
        return lMesh;
    }

    public Item createItem(Item pItem) {
        return new Item(pItem);
    }

    public ItemGroup createItemGroup() {
        return new ItemGroup();
    }

    public Skybox createSkybox(float pWidth, float pLength, float pHeight, Vector3f pVector3f) {
        return new Skybox(pWidth, pLength, pHeight, pVector3f);
    }

    public Item createItem(String pId, float[] pPosition, float pScale, Mesh pMesh) {
        return ItemManagement.createItem(pId, pPosition, pScale, pMesh);
    }

    public Item createBoundingBox(String id, float[] pPosition, float pScale, float[] pLenght) {
        return ItemManagement.createBoundingBox(id, pPosition, pScale, pLenght);

    }
}
