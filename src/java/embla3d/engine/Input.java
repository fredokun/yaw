package embla3d.engine;

import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Class that contains keyboard entries.
 */
public class Input extends GLFWKeyCallback {

    /* A boolean array of all our keys.*/
    public static boolean[] keys = new boolean[65535];

    /**
     * Allows to return the key corresponding to the event to be stored (event keyboard)
     *
     * @param keycode keycode
     * @return keys[keycode]
     */
    public static boolean isKeyDown(int keycode) {
        return keys[keycode];
    }

    public static boolean isKeyUp(int keycode) {
        return keys[keycode];
    }

    /**
     * Overrides GLFW's own implementation of the Invoke method
     * This gets called every time a key is pressed.
     * If the escape key is pressed  then the window closes.
     * Or if it presses on the closing cross of the window.
     *
     * @param window   window
     * @param key      key
     * @param scancode scancode
     * @param action   action
     * @param mods     mods
     */
    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
            glfwSetWindowShouldClose(window, true); /* Allows to say that the window must be closed. */
        keys[key] = action != GLFW_RELEASE;
    }
}