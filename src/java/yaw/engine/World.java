package yaw.engine;

import yaw.engine.camera.Camera;
import yaw.engine.collision.Collision;
import yaw.engine.items.Item;
import yaw.engine.items.ItemGroup;
import yaw.engine.light.SceneLight;
import yaw.engine.meshs.Material;
import yaw.engine.meshs.Mesh;
import yaw.engine.meshs.Texture;
import yaw.engine.skybox.Skybox;
import org.joml.Vector3f;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.glfw.GLFW.glfwGetTime;

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
    private KeyCallback mCallback;
    private Vector<ItemGroup> mItemGroupArrayList;
    private Skybox mSkybox = null;
    private ConcurrentHashMap<String, Texture> mStringTextureConcurrentHashMap;
    private boolean mLoop;
    private int initX, initY, initWidth, initHeight;
    private WorldNucleus mNucleus;
    private UpdateCallback updateCallback;

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
        this.mNucleus = WorldNucleus.getInstance();
        this.mRenderer = mNucleus.createRenderer();
        this.mCamerasList = new Vector<>();
        this.mCamera = mNucleus.createCamera();
        this.mSceneVertex = mNucleus.createSceneVertex();
        this.mSceneLight = mNucleus.createSceneLight();
        this.mCallback = mNucleus.createCallback();
        this.mItemGroupArrayList = new Vector<>();
        this.mSkyboxToBeRemoved = new Vector<>();
        this.mLoop = true;
        this.mStringTextureConcurrentHashMap = new ConcurrentHashMap<>();
        updateCallback = null;
    }

    /**
     * Function managed by a Thread, which creates the world and which manages our game loop.
     */
    public void run() {
        try {
            this.init();
            this.worldLoop();
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
     * @param pPosition position
     * @param pScale    scale
     * @param pMesh     mesh
     * @return the item
     */
    public Item createItem(String id, float[] pPosition, float pScale, Mesh pMesh) {
        if (pPosition.length != 3) {
            throw new RuntimeException("Position must represent a 3D position because we live in a 3D world");
        }
        Item lItem = mNucleus.createItem(id, pPosition, pScale, pMesh);
        mSceneVertex.add(lItem);
        return lItem;
    }

    /**
     * Create a mesh with the specified parameters
     * Mesh won't be load into the grahpic cards unless you bind it to an item
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
        Material lMaterial = mNucleus.createMaterial(lMaterialColor);
        //Texture part
        if (pTextureName != null && !pTextureName.isEmpty()) {
            Texture lTexture = mStringTextureConcurrentHashMap.get(pTextureName);
            if (lTexture == null) {
                lTexture = mNucleus.createTexture(pTextureName);
            }
            lMaterial.setTexture(lTexture);
        }
        Mesh lMesh = mNucleus.createMesh(pVertices, pTextCoords, pNormals, pIndices, pWeight);
        lMesh.setMaterial(lMaterial);
        return lMesh;
    }

    /**
     * Create a bounding box with the specified parameters and add it to the  world
     *
     * @param id        id
     * @param pPosition position
     * @param pScale    scale
     * @return BoundingBox
     */
    public Item createBoundingBox(String id, float[] pPosition, float pScale, float[] pLength) {
        Item lItem = mNucleus.createBoundingBox(id, pPosition, pScale, pLength);
        mSceneVertex.add(lItem);
        return lItem;
    }

    public boolean isInCollision(Item item1, Item item2) {
        return Collision.isInCollision(item1, item2);
    }

    public void registerUpdateCallback(UpdateCallback cb) {
    	updateCallback = cb;
    }
    
    /**
     * Allows to initialize the parameters of the class World.
     *
     * @throws Exception Exception
     */
    public void init() throws Exception {
        Window.init(initWidth, initHeight);
        // Create the rendering logic of our game.
        mRenderer.init();

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
        Skybox lSkybox = mNucleus.createSkybox(pWidth, pLength, pHeight, new Vector3f(pR, pG, pB));
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
     * Clone the specified item and add itto the scene
     *
     * @param pItem item to be cloned
     * @return the cloned item
     */
    public Item clone(Item pItem) {
        Item item = mNucleus.createItem(pItem);
        mSceneVertex.add(item);
        return item;
    }

    /**
     * Remove the specified item from the world
     *
     * @param pItem the item
     */
    public void removeItem(Item pItem) {
        mSceneVertex.removeItem(pItem);
        for (ItemGroup g : pItem.getGroups()) {
            g.remove(pItem);
        }
    }

    /**
     * Create a group of item
     *
     * @return group of item
     */
    public ItemGroup createGroup() {
        ItemGroup group = mNucleus.createItemGroup();
        mItemGroupArrayList.add(group);
        return group;
    }

    /**
     * Remove a group of item from the world
     *
     * @param pGroup the specified group
     */
    public void removeGroup(ItemGroup pGroup) {
        mItemGroupArrayList.remove(pGroup);
        for (Item lItem : pGroup.getItems()) {
            lItem.removeFromGroup(pGroup);
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
            mCallback.update();

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

    public KeyCallback getCallback() {
        return mCallback;
    }
}

