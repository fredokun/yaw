package yaw.engine;

import org.lwjgl.glfw.GLFWKeyCallback;

import java.util.Map;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Class that contains keyboard entries.
 */
public class KeyInput extends GLFWKeyCallback {

    private Map<Integer, String> trackedKeys;
    private Set<String> keyDowns;
    private InputCallback inputCallback;

    public KeyInput() {
        this.inputCallback = null;
    }

    /**
     * Allows to return the key corresponding to the event to be stored (event keyboard)
     *
     * @param keyId
     * @return keys[keycode]
     */
    public boolean isKeyDown(String keyId) {
        return keyDowns.contains(keyId);
    }

    public boolean isKeyUp(String keyId) {
        return !isKeyDown(keyId);
    }

    public synchronized void registerInputCallback(InputCallback inputCallback) {
        if(this.inputCallback != null) {
            throw new Error("Input callback already registered");
        }
        this.inputCallback = inputCallback;
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
        if(inputCallback!=null) {
            inputCallback.sendKey(key, scancode, action, mods);
        }
    }
}
