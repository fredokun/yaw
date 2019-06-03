package yaw.engine;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import yaw.engine.camera.Camera;
import yaw.engine.items.HitBox;
import yaw.engine.items.Item;
import yaw.engine.items.ItemGroup;
import yaw.engine.items.ItemObject;
import yaw.engine.light.SceneLight;
import yaw.engine.meshs.Material;
import yaw.engine.meshs.Mesh;
import yaw.engine.meshs.Texture;
import yaw.engine.meshs.strategy.DefaultDrawingStrategy;
import yaw.engine.skybox.Skybox;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL11.glViewport;

/**
 * This the main loop controlled by the World facade.
 *
 * */
public class MainLoop implements Runnable {
    private final SceneVertex mSceneVertex;
    private final Vector<Skybox> mSkyboxToBeRemoved;
    private Camera mCamera;
    private Vector<Camera> mCamerasList;
    private Renderer mRenderer;
    private SceneLight mSceneLight;
    private Vector<ItemGroup> mItemGroupArrayList;
    private Skybox mSkybox = null;
    private ConcurrentHashMap<String, Texture> mStringTextureConcurrentHashMap;
    private boolean mLoop;
    private int initX, initY, initWidth, initHeight;
    private boolean initVSYNC;


    //3D click

    private RayCaster rayCaster= new RayCaster();
    private Vector3f mousePosition = null;

    private UpdateCallback updateCallback;
    private InputCallback inputCallback;
    private Mouse3DClickCallBack mouseCallback;
    private volatile boolean initialized;

    /*
     GetCameraMat -> ProjectionMatrix
     camera.setupviewmatrix -> viewMAtrix

     */


    /**
     * Initializes the elements to create the window
     *
     * @param pInitX      initX
     * @param pInitY      initY
     * @param pInitWidth  initWidth
     * @param pInitHeight initHeight
     * @param pInitVSYNC  initVSYNC
     */
    public MainLoop(int pInitX, int pInitY, int pInitWidth, int pInitHeight, boolean pInitVSYNC) {
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
    public MainLoop(int pInitX, int pInitY, int pInitWidth, int pInitHeight) {
        this();
        this.initX = pInitX;
        this.initY = pInitY;
        this.initWidth = pInitWidth;
        this.initHeight = pInitHeight;
    }

    public MainLoop() {
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
    }

    /* package */ synchronized void addToScene(ItemObject itemObj) {
        mSceneVertex.add(itemObj);
    }

    /* package */ synchronized void removeFromScene(ItemObject pItem) {
        mSceneVertex.removeItem(pItem);
    }

    /* package */ synchronized ItemGroup createGroup(String id) {
        ItemGroup group = new ItemGroup(id);
        mItemGroupArrayList.add(group);
        return group;
    }

    /* package */ synchronized void removeGroup(ItemGroup pGroup) {
        pGroup.removeAll();
        mItemGroupArrayList.remove(pGroup);
    }

    /* package */ synchronized Vector<ItemGroup> getItemGroupArrayList() {
        return mItemGroupArrayList;
    }

    /* package */ synchronized SceneLight getSceneLight() {
        return mSceneLight;
    }

    /* package */ synchronized Texture fetchTexture(String textureName) {
        return mStringTextureConcurrentHashMap.get(textureName);
    }

    /* package */ synchronized void removeSkybox() {
        mSkyboxToBeRemoved.add(mSkybox);
        this.mSkybox = null;
    }

    /* package */ synchronized void clearCameras() {
        mCamerasList = new Vector<>();
    }

    /* package */ void addCamera(int pIndex, Camera pCamera) {
        if (pIndex == 0) mCamera = pCamera;
        mCamerasList.add(pIndex, pCamera);
    }

    /* package */ synchronized Camera getCamera() {
        return mCamera;
    }

    /* package */ synchronized void setCamera(Camera pCamera) {
        this.mCamera = pCamera;
    }

    /* package */ synchronized Vector<Camera> getCamerasList() {
        return mCamerasList;
    }

    /* package */ synchronized Skybox getSkybox() {
        return mSkybox;
    }

    /* package */ synchronized void setSkybox(Skybox pSkybox) {
        if (this.mSkybox != null) {
            mSkyboxToBeRemoved.add(pSkybox);
        }
        this.mSkybox = pSkybox;
    }

    /**
     * Function managed by a Thread, which creates the world and which manages our game loop.
     */
    public void run() {
        try {
            this.init();
            this.loop();
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

    //3D click

    public synchronized void registerMouse3DClickCallBack(Mouse3DClickCallBack mc){
        if(mouseCallback != null) {
            throw new Error("Input callback already registered");
        }
        mouseCallback = mc;

        if(initialized) {
            Window.getGLFWMouseCallback().registerMouseCallback(mc);
        }
    }






    // End 3D click

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

        if(mouseCallback != null) {
            Window.getGLFWMouseCallback().registerMouseCallback(mouseCallback);
        }

        initialized = true;
    }

    // UpdateRate: FIXED
    // FrameRate: VARIABLE
    private void loop() throws InterruptedException {
        double dt = 0.01; // Update Rate: 1 ~= 2 fps | 0.001 ~= 1000 fps
        /* Initialization of the window we currently use. */
        glViewport(initX, initY, initWidth, initHeight);
        double beforeTime = glfwGetTime();
        double lag = 0d;
        while (!Window.windowShouldClose() && mLoop) { /* Check if the window has not been closed. */
            double nowTime = glfwGetTime();
            double framet = nowTime - beforeTime;
            beforeTime = nowTime;
            lag += framet;
            //mousePosition=RayCaster.getWorldRay(Window.windowHandle, mCamera);

            //refresh rate ??
//            Thread.sleep(20); // XXX ? Why sleep ?
            mCamera.update();

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

}

