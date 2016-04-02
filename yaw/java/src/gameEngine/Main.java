package gameEngine;

import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.*;
import gameEngine.light.PointLight;
import gameEngine.light.SceneLight;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL;

import basicMeshes.CubeItem;
import basicMeshes.CubeMesh;

public class Main {

	public static void main(String[] args) throws Exception {
		Window.init();
		Camera c = new Camera();
		Renderer renderer = new Renderer(c);
		SceneVertex sc = new SceneVertex();
		SceneLight sl= new SceneLight();
		
		
		//Generation of a cube
		Material material = new Material( new Vector3f(0.4f,0,0.7f),0.f);
		sc.add(new CubeItem(material, 1f, new Vector3f(), new Vector3f(0f,0f,-2f)));
		
		//sl.setPointLight(new PointLight(new Vector3f(0.2f,1f,0.3f), new Vector3f(), 1, 0, 0, 0), 0);
		
		try{
			//Initialisation of the window we currently use
			glViewport(0, 0, 500,500);
			while ( glfwWindowShouldClose(Window.window) == GLFW_FALSE ) {
				c.update();
				//Clean the window
				boolean isResized = Window.clear();
				//Update the world
				renderer.render(sc, sl, isResized);
				//Update the window's picture
				Window.update();
			}
		}finally{
			renderer.cleanUp();
			

			sc.cleanUp();
			
			//On desaloue le materiel de la fenetre
			Window.cleanUp();
		}
	}

}
