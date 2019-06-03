package yaw.engine;

import org.lwjgl.glfw.GLFWCursorEnterCallback;

public interface Mouse3DClickCallBack {
    public void mouse_click_callback(long window, int button, int action, int mods);

}
