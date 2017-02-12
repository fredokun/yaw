package embla3d.engine;

import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * This Class allow to encapsulate all the GLFW Window initialization code and thus allowing some parameterization of its characteristics .
 */
public class Window {
    public static long window;
    /* Capabilities */
    public static GLCapabilities caps;
    protected static int width;
    protected static int height;
    protected static boolean resized;
    /* Protect from Garbage Collector errors. */
    private static GLFWKeyCallback keyCallback;
    private static GLFWWindowSizeCallback windowSizeCallback;

    /**
     * Initializes and opens a window.
     *
     * @param initWidth  Width of the window.
     * @param initHeight Height of the window.
     */
    public static void init(int initWidth, int initHeight) {
        if (!glfwInit()) { /* This function initializes the GLFW library. Before that GLFW functions can be used, GLFW must be initialized . */
            throw new IllegalStateException("Unable to initialize GLFW");
        }
       /* This function sets hints for the next call to glfwCreateWindow.
         In other words, they prepare the call to glfwCreateWindow. */
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3); /* Allow to specify the opengl version used, here opengl 3.3 (opengl MAJOR.MINOR). */
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);/* The window will stay visible after creation. */
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); /* The window will be resizable. */

        width = initWidth;
        height = initHeight;
        resized = false;

       /* Create the window and its associated OpenGL context.
          The window is created with its dimensions and its title.
          The second last element "monitor" allows to create windows in full screen.
          Finally, the last element share, allows to indicate to the new window that its context will possess
          The objects (textures, vertex buffers,... ) of the window pass as argument.*/
        window = glfwCreateWindow(width, width, "Yet Another World", NULL, NULL);

        /* This function sets the key callback of the specified window,
           which is called when a key is pressed, repeated or released.*/
        glfwSetKeyCallback(window, keyCallback = new Input());

        /* Setup resize callback
           This function sets the size callback of the specified window, which is called when the window is resized.
           The callback is provided with the size, in screen coordinates, of the client area of the window.*/
        glfwSetWindowSizeCallback(window, windowSizeCallback = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                Window.width = width;
                Window.height = height;
                Window.resized = true; /* When the resize attribute is false, it does not prevent the resizing of the window with the mouse
                                          but does not take into account the resize event in our program.*/
            }
        });

        glfwMakeContextCurrent(window); /* Update to context for the window. */

        caps = GL.createCapabilities(); /* Creates a new GLCapabilities instance for the OpenGL context that is current in the current thread. */

        glfwSwapInterval(1); /* Allows synchronization at a constant rate. */
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);/* Specifies the red, green, blue, and alpha values used by glClear to clear the color buffers. */

    }

    /**
     * Deallocates the resources used for the window
     */
    public static void cleanUp() {
        glfwTerminate();
    }

    /**
     * To be called before updating the windows's content
     *
     * @return true or false
     */
    public static boolean clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        if (resized) {
            glViewport(0, 0, width, height);
            resized = false;
            return true;
        }
        return false;
    }

    /**
     * Draws the window's contents
     */
    public static void update() {
        glfwSwapBuffers(window);/*  Waits the specified number of screen updates before exchanging the buffers for the new display. */
        glfwPollEvents(); /* Processes only those events that are already in the event queue.
        Processing events will cause the window and input callbacks associated with those events to be called.*/
    }

    /**
     * Verify the resizing of the window, useful especially for transformation (moving objects in the world) .
     * Define according to the size of the created window .
     *
     * @return aspectRatio
     */
    public static double aspectRatio() {
        return width / (double) height;
    }
}