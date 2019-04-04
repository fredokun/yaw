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


    public Skybox createSkybox(float pWidth, float pLength, float pHeight, Vector3f pVector3f) {
        return new Skybox(pWidth, pLength, pHeight, pVector3f);
    }

}
