package gameEngine;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

import org.lwjgl.glfw.GLFWKeyCallback;

public class Input extends GLFWKeyCallback{

	// a boolean array of all our keys. 
	public static boolean[] keys = new boolean[65535];

	// Overrides GLFW's own implementation of the Invoke method
	// This gets called every time a key is pressed.
	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
            glfwSetWindowShouldClose(window, GLFW_TRUE);
		keys[key] = action != GLFW_RELEASE;
	}

	public static boolean isKeyDown(int keycode) {
		return keys[keycode];
	}

	public static boolean isKeyUp(int keycode){
		return keys[keycode];
	}
}