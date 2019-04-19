package yaw.engine;

import org.joml.Quaternionf;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import yaw.engine.camera.Camera;
import yaw.engine.items.*;
import yaw.engine.light.SceneLight;
import yaw.engine.meshs.*;
import yaw.engine.meshs.strategy.DefaultDrawingStrategy;
import yaw.engine.skybox.Skybox;
import yaw.engine.InputCallback;
import org.joml.Vector3f;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;

/**
 * Allows the creation of the world and manages through a thread the events, the updates and the rendering to the screen at constant rate.
 */
public class World implements Runnable {
    private final SceneVertex mSceneVertex;
    private final Vector<Skybox> mSkyboxToBeRemoved;
    private Camera mCamera;
    private Vector<Camera> mCamerasList;
    private Renderer mRenderer;
    private SceneLight mSceneLight;
    private KeyCallback keyCallback;
    private Vector<ItemGroup> mItemGroupArrayList;
    private Skybox mSkybox = null;
    private ConcurrentHashMap<String, Texture> mStringTextureConcurrentHashMap;
    private boolean mLoop;
    private int initX, initY, initWidth, initHeight;
    private boolean initVSYNC;

    private UpdateCallback updateCallback;
    private InputCallback inputCallback;
    private GLFWKeyCallbackI glfwKeyCallback;
    private volatile boolean initialized;


    /**
     * Initializes the elements to create the window
     *
     * @param pInitX      initX
     * @param pInitY      initY
     * @param pInitWidth  initWidth
     * @param pInitHeight initHeight
     * @param pInitVSYNC  initVSYNC
     */
    public World(int pInitX, int pInitY, int pInitWidth, int pInitHeight, boolean pInitVSYNC) {
        this(pInitX, pInitY, pInitWidth, pInitHeight);
        this.initVSYNC = pInitVSYNC;
        this.initialized = false;
    }

    /**
     * Initializes the elements to create the window
     *
     * @param pInitX      initX
     * @param pInitY      initY
     * @param pInitWidth  initWidth
     * @param pInitHeight initHeight
     */
    public World(int pInitX, int pInitY, int pInitWidth, int pInitHeight) {
        this();
        this.initX = pInitX;
        this.initY = pInitY;
        this.initWidth = pInitWidth;
        this.initHeight = pInitHeight;
    }

    public World() {
        this.mRenderer = new Renderer();
        this.mCamerasList = new Vector<>();
        this.mCamera = new Camera();
        this.mSceneVertex = new SceneVertex();
        this.mSceneLight = new SceneLight();
        this.mItemGroupArrayList = new Vector<>();
        this.mSkyboxToBeRemoved = new Vector<>();
        this.mLoop = true;
        this.initVSYNC = true;
        this.mStringTextureConcurrentHashMap = new ConcurrentHashMap<>();
        this.updateCallback = null;
        this.inputCallback = null;
        // TODO : this old keyCallback mechanism should be removed
        this.keyCallback = new KeyCallback();
    }

    /**
     * Function managed by a Thread, which creates the world and which manages our game loop.
     */
    public void run() {
        try {
            this.init();
            this.UFRV();
        } catch (Exception pE) {
            pE.printStackTrace();
        } finally {
            cleanup();
        }
    }

    /**
     * Input of critical section, allows to protect the resource share loop.
     * Stop the game loop and stop the thread that manage the world.
     */
    public synchronized void close() throws InterruptedException {
        mLoop = false;
        this.wait();
    }

    /**
     * Input of critical section, allows to protect the synchronization;
     * Allows synchronization between Threads (releases the lock (caused by the wait ()) and allows other threads waiting to execute).
     */
    private synchronized void notifyFinished() {
        this.notify();
    }

    /**
     * Create an item with the specified parameters and add it to the  world
     *
     * @param id        id
     * @param x         x coordinate
     * @param y         y coordinate
     * @param z         z coordinate
     * @param pScale    scale
     * @param pMesh     mesh
     * @return the item
     */
    public ItemObject createItemObject(String id, float x, float y, float z, float pScale, Mesh pMesh) {
        ItemObject lItem = new ItemObject(id, new Vector3f(x, y, z)
                , new Quaternionf(), pScale, pMesh);

        mSceneVertex.add(lItem);
        return lItem;
    }

    /**
     * Create a mesh with the specified parameters
     * MeshOld won't be load into the grahpic cards unless you bind it to an item
     *
     * @param pVertices    vetices
     * @param pTextCoords  texture coordonates
     * @param pNormals     normals
     * @param pIndices     indices
     * @param pWeight      weight
     * @param rgb          rgb array
     * @param pTextureName texture path
     * @return the mesh
     */
    public Mesh createMesh(float[] pVertices, float[] pTextCoords, float[] pNormals, int[] pIndices, int pWeight, float[] rgb, String pTextureName) {
        if (rgb.length != 3) {
            throw new RuntimeException("RGB must represent 3 colors");
        }
        Vector3f lMaterialColor = new Vector3f(rgb[0], rgb[1], rgb[2]);
        Material lMaterial = new Material(lMaterialColor);
        //Texture part
        if (pTextureName != null && !pTextureName.isEmpty()) {
            Texture lTexture = mStringTextureConcurrentHashMap.get(pTextureName);
            if (lTexture == null) {
                lTexture = new Texture(pTextureName);
            }
            lMaterial.setTexture(lTexture);
        }
        Mesh lMesh = new Mesh(pVertices, pTextCoords, pNormals, pIndices, pWeight);
        lMesh.setDrawingStrategy(new DefaultDrawingStrategy());
        lMesh.setMaterial(lMaterial);
        return lMesh;
    }

    /**
     * Create a mesh with the specified parameters
     * MeshOld won't be load into the grahpic cards unless you bind it to an item
     *
     * @param pVertices    vetices
     * @param pNormals     normals
     * @param pIndices     indices
     * @return the mesh
     */
    public Mesh createMesh(float[] pVertices, float[] pNormals, int[] pIndices, float[] rgb) {
        if (rgb.length != 3) {
            throw new RuntimeException("RGB must represent 3 colors");
        }
        Vector3f lMaterialColor = new Vector3f(rgb[0], rgb[1], rgb[2]);
        Material lMaterial = new Material(lMaterialColor);
        Mesh lMesh = new Mesh(pVertices, pNormals, pIndices);
        lMesh.setDrawingStrategy(new DefaultDrawingStrategy());
        lMesh.setMaterial(lMaterial);
        return lMesh;
    }



    /**
     * Create a bounding box with the specified parameters and add it to the  world
     *
     * @param id        id
     * @param x         x coordinate
     * @param y         y coordinate
     * @param z         z coordinate
     * @param pScale    scale
     * @return BoundingBox
     */
    public HitBox createHitBox(String id, float x, float y, float z, float pScale, float xLength, float yLength, float zLength){
        HitBox hb = new HitBox(id, new Vector3f(x, y, z), new Quaternionf(), pScale, xLength, yLength, zLength);
        mSceneVertex.add(hb);
        return hb;

    }


    public boolean isInCollision(HitBox hb1, HitBox hb2) {
        return hb1.collidesWith(hb2);
    }

    public synchronized void registerUpdateCallback(UpdateCallback cb) {
        updateCallback = cb;
    }

    public synchronized void registerInputCallback(InputCallback callback) {
        if(inputCallback != null) {
            throw new Error("Input callback already registered");
        }
        inputCallback = callback;

        if(initialized) {
            Window.getGLFWKeyCallback().registerInputCallback(callback);
        }
    }


    public synchronized void registerKeyCallback(KeyCallback key) { keyCallback = key;}


    /**
     * Allows to initialize the parameters of the class World.
     *
     * @throws Exception Exception
     */
    public synchronized void init() throws Exception {
        Window.init(initWidth, initHeight, initVSYNC);
        // Create the rendering logic of our game.
        mRenderer.init();

        if(inputCallback != null) {
            Window.getGLFWKeyCallback().registerInputCallback(inputCallback);
        }

        initialized = true;
    }

    /**
     * Create a skybox with all its parameters and replace the current skybox
     *
     * @param pWidth  width
     * @param pLength length
     * @param pHeight height
     * @param pR      r
     * @param pG      g
     * @param pB      b
     */
    public void setSkybox(float pWidth, float pLength, float pHeight, float pR, float pG, float pB) {
        Skybox lSkybox = new Skybox(pWidth, pLength, pHeight, new Vector3f(pR, pG, pB));
        setSkybox(lSkybox);
    }

    public void removeSkybox() {

        mSkyboxToBeRemoved.add(mSkybox);

        this.mSkybox = null;
    }

    /**
     * Gets an empty camera list
     */
    public void emptyListCamera() {
        mCamerasList = new Vector<>();
    }

    /**
     * Adds a camera and its index in the list of cameras (not in live)
     *
     * @param pIndex  index
     * @param pCamera camera
     */
    public void addCamera(int pIndex, Camera pCamera) {
        if (pIndex == 0) mCamera = pCamera;
        mCamerasList.add(pIndex, pCamera);
    }

    /**
     * Remove the specified item from the world
     *
     * @param pItem the item
     */
    public void removeItem(ItemObject pItem) {
        mSceneVertex.removeItem(pItem);
    }

    /**
     * Create a group of item
     *
     * @return group of item
     */
    public ItemGroup createGroup(String id) {
        ItemGroup group = new ItemGroup(id);
        mItemGroupArrayList.add(group);
        return group;
    }

    /**
     * Remove a group of item from the world
     *
     * @param pGroup the specified group
     */
    public void removeGroup(ItemGroup pGroup) {
        pGroup.removeAll();
        mItemGroupArrayList.remove(pGroup);
    }

    // UpdateRate: FIXED
    // FrameRate: VARIABLE
    private void UFRV() throws InterruptedException {
        double dt = 0.01; // Update Rate: 1 ~= 2 fps | 0.001 ~= 1000 fps
        /* Initialization of the window we currently use. */
        int key;
        int scancode;
        int action;
        int mods;
        glViewport(initX, initY, initWidth, initHeight);
        double beforeTime = glfwGetTime();
        double lag = 0d;
        while (!Window.windowShouldClose() && mLoop) { /* Check if the window has not been closed. */
            double nowTime = glfwGetTime();
            double framet = nowTime - beforeTime;
            beforeTime = nowTime;
            lag += framet;

            //refressh rate ??
//            Thread.sleep(20); // XXX ? Why sleep ?
            mCamera.update();
            keyCallback.update();

            if(updateCallback != null) {
                while (lag >= dt) {
                    updateCallback.update(dt);
                    lag -= dt;
                }
            }


            /*Clean the window*/
            boolean isResized = Window.clear();

           /* Input of critical section, allows to protect the resource mSkyboxToBeRemoved .
              Deallocation of VAO and VBO, Moreover Delete the buffers VBO and VAO. */

            for (Skybox lSkybox : mSkyboxToBeRemoved) {
                lSkybox.cleanUp();
            }
            mSkyboxToBeRemoved.clear();


           /*  Input of critical section, allows to protect the creation of our logic of Game .
               1 Maximum thread in Synchronize -> mutual exclusion.*/
            synchronized (mSceneVertex) {
                //Update the world
                mRenderer.render(mSceneVertex, mSceneLight, isResized, mCamera, mSkybox);
            }

           /*  Rendered with vSync (vertical Synchronization)
               Update the window's picture */
            Window.update();

        }
    }

    /**
     * Start the game loop
     *
     * @throws InterruptedException Exception
     */
    private void worldLoop() throws InterruptedException {
        /* Initialization of the window we currently use. */
        glViewport(initX, initY, initWidth, initHeight);
        double beforeTime = glfwGetTime();
        while (!Window.windowShouldClose() && mLoop) { /* Check if the window has not been closed. */
            //refressh rate ??
            Thread.sleep(20); // XXX ? Why sleep ?
            mCamera.update();
            keyCallback.update();

            /*Clean the window*/
            boolean isResized = Window.clear();

           /* Input of critical section, allows to protect the resource mSkyboxToBeRemoved .
              Deallocation of VAO and VBO, Moreover Delete the buffers VBO and VAO. */

            for (Skybox lSkybox : mSkyboxToBeRemoved) {
                lSkybox.cleanUp();
            }
            mSkyboxToBeRemoved.clear();


           /*  Input of critical section, allows to protect the creation of our logic of Game .
               1 Maximum thread in Synchronize -> mutual exclusion.*/
            synchronized (mSceneVertex) {
                //Update the world
                mRenderer.render(mSceneVertex, mSceneLight, isResized, mCamera, mSkybox);
            }

           /*  Rendered with vSync (vertical Synchronization)
               Update the window's picture */
            Window.update();

            if(updateCallback != null) {
                double afterTime = glfwGetTime();
                updateCallback.update(afterTime - beforeTime);
                beforeTime = afterTime;
            }
        }
    }

    /**
     * Deallocates the resources used by the world
     */
    private void cleanup() {
        /* Deallocations renderer, SceneVertex and Skybox. */
        mRenderer.cleanUp();
        mSceneVertex.cleanUp();
        if (mSkybox != null) mSkybox.cleanUp();
        /* Deallocation of the window's resources. */
        Window.cleanUp();
        this.notifyFinished();
    }

    public Camera getCamera() {
        return mCamera;
    }

    public void setCamera(Camera pCamera) {
        this.mCamera = pCamera;
    }

    public Vector<Camera> getCamerasList() {
        return mCamerasList;
    }

    public Skybox getSkybox() {
        return mSkybox;
    }

    /**
     * Allows to change the skybox by the new Skybox passed in arguments.
     * If a skybox already exists, it's added to the list of mSkyboxToBeRemoved
     *
     * @param pSkybox skybox
     */
    public void setSkybox(Skybox pSkybox) {
        if (this.mSkybox != null) {
            mSkyboxToBeRemoved.add(pSkybox);
        }
        this.mSkybox = pSkybox;
    }

    public Vector<ItemGroup> getItemGroupArrayList() {
        return mItemGroupArrayList;
    }

    public SceneLight getSceneLight() {
        return mSceneLight;
    }

}

