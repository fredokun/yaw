package yaw.engine;

import yaw.engine.meshs.*;
import yaw.engine.camera.Camera;
import yaw.engine.items.*;
import yaw.engine.light.SceneLight;
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

    public KeyCallback createCallback() {
        return new KeyCallback();
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

    public Mesh createMesh(float[] pVertices, float[] pNormals, int[]pIndices) {
        Mesh lMesh = new Mesh(pVertices, pNormals, pIndices);
        lMesh.setDrawingStrategy(new DefaultDrawingStrategy());
        return lMesh;
    }

    public ItemObject createItemREF(ItemObject pItem) {
        return new ItemObject(pItem);
    }

    public Item createItemREF(Item pItem) {
        return pItem.clone();
    }

    public ItemGroup createItemGroupREF() {
        return new ItemGroup();
    }

    public Skybox createSkybox(float pWidth, float pLength, float pHeight, Vector3f pVector3f) {
        return new Skybox(pWidth, pLength, pHeight, pVector3f);
    }

    //REFACTORING TEST


    public ItemObject createItemREF(String pId, float[] pPosition, float pScale, Mesh pMesh) {
        ItemObject item = new ItemObject(pId, pPosition, pScale, pMesh);

        return item;
    }







    public HitBox createHitbox(String id, float[] pPosition, float pScale, float[] pLength) {
        Mesh appearance = MeshBuilder.generateBoundingBox(pLength[0], pLength[1], pLength[2]);
        appearance.getMaterial().setColor(new Vector3f(0,255,0));

        return new HitBox(id, new Vector3f(pPosition[0], pPosition[1], pPosition[2]), new Vector3f(), pScale, appearance);
    }

}
