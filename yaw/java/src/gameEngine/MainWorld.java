package gameEngine;

import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;

import org.lwjgl.opengl.GL;

public class MainWorld {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		World world=new World();
		world.init();
		(new Thread(world)).start();
		world.CreateCube();

	}

}
