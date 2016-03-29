package gameEngine;

import gameEngine.light.PointLight;
import gameEngine.light.SceneLight;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.*;
import org.joml.Vector3f;

import basicMeshes.CubeItem;

public class World {
	Camera c ;
	Renderer renderer;
	SceneVertex sc ;
	SceneLight sl;

	public void CreateCube(){
		Material material = new Material( new Vector3f(0.4f,0,0.7f),0.f);
		sc.add(new CubeItem(material, 1f, new Vector3f(), new Vector3f(0f,0f,-2f)));
	}
	public void init() throws Exception{
		Window.init();
		this.c= new Camera();
		this.renderer= new Renderer(c);
		this.sc= new SceneVertex();
		this.sl= new SceneLight();
		
		CreateCube();
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

