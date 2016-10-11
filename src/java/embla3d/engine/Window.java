package embla3d.engine;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

public class Window {
    public static long window;
    protected static int width;
    protected static int height;
    protected static boolean resized;
    // Protect from Garbage Collector errors
    private static GLFWKeyCallback keyCallback;
    private static GLFWWindowSizeCallback windowSizeCallback;

    // Capabilities
    public static GLCapabilities caps;

    // Initializes and opens a window.
    public static void init() {
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        width = 500;
        height = 500;
        resized = false;

        window = glfwCreateWindow(500, 500, "Yet Another World", NULL, NULL);

        glfwSetKeyCallback(window, keyCallback = new Input());

        // Setup resize callback
        glfwSetWindowSizeCallback(window, windowSizeCallback = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                Window.width = width;
                Window.height = height;
                Window.resized = true;
            }
        });

        glfwMakeContextCurrent(window);

        caps = GL.createCapabilities();

        glfwSwapInterval(1);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    }

    // Deallocates the resources used for the window
    public static void cleanUp() {
        glfwTerminate();
    }

    // To be called before updating the windows's content
    public static boolean clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        if (resized) {
            glViewport(0, 0, width, height);
            resized = false;
            return true;
        }
        return false;
    }

    // Draws the window's contents
    public static void update() {
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    public static double aspectRatio() {
        return width / (double) height;
    }
}
