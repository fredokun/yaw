package embla3d.engine;

import embla3d.engine.camera.Camera;
import embla3d.engine.items.Item;
import embla3d.engine.items.ItemGroup;
import embla3d.engine.items.ItemManagement;
import embla3d.engine.light.SceneLight;
import embla3d.engine.meshs.Material;
import embla3d.engine.meshs.Mesh;
import embla3d.engine.meshs.Texture;
import embla3d.engine.meshs.strategy.DefaultDrawingStrategy;
import embla3d.engine.skybox.Skybox;
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
