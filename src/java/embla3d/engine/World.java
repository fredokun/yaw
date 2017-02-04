package embla3d.engine;

import java.util.ArrayList;

import org.joml.Vector3f;

import embla3d.engine.camera.Camera;
import embla3d.engine.items.ItemGroup;
import embla3d.engine.light.SceneLight;
import embla3d.engine.skybox.Skybox;

import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.*;

/**
 * Allows the creation of the world and manages through a thread the events, the updates and the rendering to the screen at constant rate.
 */
public class World implements Runnable {
    Camera c;
    ArrayList<Camera> camerasList;
    Renderer renderer;
    SceneVertex sc;
    SceneLight sl;
    Callback callback;
    ArrayList<ItemGroup> groupsList;
    ArrayList<Skybox> skyboxToBeRemoved;

    private Skybox sk = null;
    private boolean loop;

    private int initX, initY, initWidth, initHeight;

    /**
     * Initializes the elements to create the window
     *
     * @param initX      initX
     * @param initY      initY
     * @param initWidth  initWidth
     * @param initHeight initHeight
     */
    public World(int initX, int initY, int initWidth, int initHeight) {
        this.initX = initX;
        this.initY = initY;
        this.initWidth = initWidth;
        this.initHeight = initHeight;
    }

    public World() {

    }


    public Camera getCamera() {
        return c;
    }

    public void setCamera(Camera c) {
        this.c = c;
    }

    public ArrayList<Camera> getCamerasList() {
        return camerasList;
    }

    /**
     * Create a skybox with all its parameters and replace the current skybox
     *
     * @param width
     * @param length
     * @param height
     * @param r
     * @param g
     * @param b
     */
    public void setSkybox(float width, float length, float height, float r, float g, float b) {
        Skybox sky = new Skybox(width, length, height, new Vector3f(r, g, b));
        setSkybox(sky);
    }

    /**
     * Allows to change the skybox by the new Skybox passed in arguments.
     * If a skybox already exists, it's added to the list of skyboxToBeRemoved
     *
     * @param sk
     */
    public void setSkybox(Skybox sk) {
        if (this.sk != null) {
            synchronized (skyboxToBeRemoved) {
                skyboxToBeRemoved.add(sk);
            }
        }
        this.sk = sk;
    }

    public void removeSkybox() {
        synchronized (skyboxToBeRemoved) {
            skyboxToBeRemoved.add(sk);
        }
        this.sk = null;
    }

    public Skybox getSkybox() {
        return sk;
    }

    public SceneVertex getSceneVertex() {
        return sc;
    }

    public ArrayList<ItemGroup> getGroupsList() {
        return groupsList;
    }


    public SceneLight getSceneLight() {
        return sl;
    }

    public Callback getCallback() {
        return callback;
    }

    /**
     * Allows to initialize the parameters of the class World.
     *
     * @throws Exception
     */
    public void init() throws Exception {
        this.camerasList = new ArrayList<Camera>();
        this.c = new Camera();
        this.sc = new SceneVertex();
        this.sl = new SceneLight();
        this.callback = new Callback();
        this.groupsList = new ArrayList<ItemGroup>();
        this.skyboxToBeRemoved = new ArrayList<Skybox>();
        loop = true;
    }

    /**
     * Gets an empty camera list
     */
    public void emptyListCamera() {
        camerasList = new ArrayList<Camera>();
    }

    /**
     * Adds a camera and its index in the list of cameras (not in live)
     *
     * @param index
     * @param camera
     */
    public void setCamera(int index, Camera camera) {
        if (index == 0)
            c = camera;
        camerasList.add(index, camera);
    }

    /**
     * Function managed by a Thread, which creates the world and which manages our game loop.
     */
    public void run() {
        Window.init(initWidth, initHeight);
        try {
            this.renderer = new Renderer(); // Create the rendering logic of our game.
        } catch (Exception e1) {
            e1.printStackTrace();
            return;
        }

        try {


            //Initialization of the window we currently use
            glViewport(initX, initY, initWidth, initHeight);
            while (glfwWindowShouldClose(Window.window) == false && loop) { // Check if the window has not been closed.
                Thread.sleep(20); // XXX ? Why sleep ?
                c.update();
                callback.update();

                //Clean the window
                boolean isResized = Window.clear();

                // Input of critical section, allows to protect the resource skyboxToBeRemoved .
                // Deallocation of VAO and VBO, Moreover Delete the buffers VBO and VAO
                synchronized (skyboxToBeRemoved) {
                    for (Skybox s : skyboxToBeRemoved) {
                        s.cleanUp();
                    }
                    // System.out.println("ici");
                    skyboxToBeRemoved.clear();
                }

                // Input of critical section, allows to protect the creation of our logic of Game
                // 1 Maximum thread in Synchronize -> mutual exclusion.
                synchronized (sc) {
                    //Update the world
                    renderer.render(sc, sl, isResized, c, sk);
                }

                // Rendered with vSync (vertical Synchronization)

                //Thread.sleep(1000);
                // Update the window's picture
                Window.update();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            // Deallocations renderer, SceneVertex and Skybox
            renderer.cleanUp();
            sc.cleanUp();
            if (sk != null)
                sk.cleanUp();
            // Deallocation of the window's resources
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
     *
     * @throws InterruptedException
     */
    public synchronized void close() throws InterruptedException {
        loop = false;
        this.wait();
    }
}

