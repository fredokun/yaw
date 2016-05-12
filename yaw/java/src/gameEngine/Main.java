package gameEngine;

import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.*;
import gameEngine.camera.Camera;
import gameEngine.light.AmbiantLight;
import gameEngine.light.PointLight;
import gameEngine.light.SceneLight;
import gameEngine.meshs.Material;

import org.joml.Vector3f;


public class Main {

	public static void main(String[] args) throws Exception {
		Window.init();
		Camera c = new Camera();
		Renderer renderer = new Renderer();
		SceneVertex sc = new SceneVertex();
		SceneLight sl= new SceneLight();
		
		
		//Generation of a cube
		Material material = new Material( new Vector3f(1f,0,1f),0.f);
		//sc.add(new CubeItem(PyramidGenerator.generate(1f,1f,1f,material), 1f, new Vector3f(), new Vector3f(0f,0f,-2f)));
		
		sl.setAmbiant(new AmbiantLight(new Vector3f(1f,1f,0f), 1));
		sl.setPointLight(new PointLight(new Vector3f(00f,0f,1f), new Vector3f(), 1, 0, 1, 0), 0);
		
		try{
			//Initialisation of the window we currently use
			glViewport(0, 0, 500,500);
			while ( glfwWindowShouldClose(Window.window) == GLFW_FALSE ) {
				c.update();
				//Clean the window
				boolean isResized = Window.clear();
				//Update the world
				renderer.render(sc, sl, isResized,c);
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
