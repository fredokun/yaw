package embla3d.engine;

import embla3d.engine.camera.Camera;
import embla3d.engine.items.ItemGroup;
import embla3d.engine.light.SceneLight;
import embla3d.engine.skybox.Skybox;
import org.joml.Vector3f;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.glViewport;

/**
 * Allows the creation of the world and manages through a thread the events, the updates and the rendering to the screen at constant rate.
 */
public class World implements Runnable {
    private Camera mCamera;
    private ArrayList<Camera> mCamerasList;
    private Renderer mRenderer;
    private SceneVertex mSceneVertex;
    private SceneLight mSceneLight;
    private Callback mCallback;
    private ArrayList<ItemGroup> mItemGroupArrayList;
    private ArrayList<Skybox> mSkyboxToBeRemoved;

    private Skybox mSkybox = null;
    private boolean mLoop;

    private int initX, initY, initWidth, initHeight;

    /**
     * Initializes the elements to create the window
     *
     * @param pInitX      initX
     * @param pInitY      initY
     * @param pInitWidth  initWidth
     * @param pInitHeight initHeight
     */
    public World(int pInitX, int pInitY, int pInitWidth, int pInitHeight) {
        this.initX = pInitX;
        this.initY = pInitY;
        this.initWidth = pInitWidth;
        this.initHeight = pInitHeight;
    }

    public World() {

    }


    public Camera getCamera() {
        return mCamera;
    }

    public void setCamera(Camera pCamera) {
        this.mCamera = pCamera;
    }

    public ArrayList<Camera> getmCamerasList() {
        return mCamerasList;
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
        synchronized (mSkyboxToBeRemoved) {
            mSkyboxToBeRemoved.add(mSkybox);
        }
        this.mSkybox = null;
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
            synchronized (mSkyboxToBeRemoved) {
                mSkyboxToBeRemoved.add(pSkybox);
            }
        }
        this.mSkybox = pSkybox;
    }

    public SceneVertex getSceneVertex() {
        return mSceneVertex;
    }

    public ArrayList<ItemGroup> getItemGroupArrayList() {
        return mItemGroupArrayList;
    }


    public SceneLight getSceneLight() {
        return mSceneLight;
    }

    public Callback getCallback() {
        return mCallback;
    }


    /**
     * Allows to initialize the parameters of the class World.
     *
     * @throws Exception Exception
     */
    public void init() throws Exception {
        this.mCamerasList = new ArrayList<>();
        this.mCamera = new Camera();
        this.mSceneVertex = new SceneVertex();
        this.mSceneLight = new SceneLight();
        this.mCallback = new Callback();
        this.mItemGroupArrayList = new ArrayList<>();
        this.mSkyboxToBeRemoved = new ArrayList<>();
        this.mLoop = true;
    }

    /**
     * Gets an empty camera list
     */
    public void emptyListCamera() {
        mCamerasList = new ArrayList<Camera>();
    }

    /**
     * Adds a camera and its index in the list of cameras (not in live)
     *
     * @param pIndex  index
     * @param pCamera camera
     */
    public void setCamera(int pIndex, Camera pCamera) {
        if (pIndex == 0) mCamera = pCamera;
        mCamerasList.add(pIndex, pCamera);
    }

    /**
     * Function managed by a Thread, which creates the world and which manages our game loop.
     */
    public void run() {
        Window.init(initWidth, initHeight);
        try {
            this.mRenderer = new Renderer(); // Create the rendering logic of our game.
        } catch (Exception e1) {
            e1.printStackTrace();
            return;
        }

        try {


            /* Initialization of the window we currently use. */
            glViewport(initX, initY, initWidth, initHeight);
            while (glfwWindowShouldClose(Window.window) == false && mLoop) { /* Check if the window has not been closed. */
                Thread.sleep(20); // XXX ? Why sleep ?
                mCamera.update();
                mCallback.update();

                /*Clean the window*/
                boolean isResized = Window.clear();

               /* Input of critical section, allows to protect the resource mSkyboxToBeRemoved .
                  Deallocation of VAO and VBO, Moreover Delete the buffers VBO and VAO. */
                synchronized (mSkyboxToBeRemoved) {
                    for (Skybox s : mSkyboxToBeRemoved) {
                        s.cleanUp();
                    }
                    mSkyboxToBeRemoved.clear();
                }

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
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
           /* Deallocations renderer, SceneVertex and Skybox. */
            mRenderer.cleanUp();
            mSceneVertex.cleanUp();
            if (mSkybox != null) mSkybox.cleanUp();
            /* Deallocation of the window's resources. */
            Window.cleanUp();
            this.notifyFinished();
        }
    }

    /**
     * Input of critical section, allows to protect the synchronization;
     * Allows synchronization between Threads (releases the lock (caused by the wait ()) and allows other threads waiting to execute).
     */
    private synchronized void notifyFinished() {
        this.notify();
    }

    /**
     * Input of critical section, allows to protect the resource share loop.
     * Stop the game loop and stop the thread that manage the world.
     */
    public synchronized void close() throws InterruptedException {
        mLoop = false;
        this.wait();
    }
}

