package gameEngine;

import gameEngine.light.PointLight;
import gameEngine.light.SceneLight;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.*;
import org.joml.Vector3f;

public class World {
	public void init() throws Exception{
		Window.init();
		Camera c = new Camera();
		Renderer renderer = new Renderer(c);
		SceneVertex sc = new SceneVertex();
		SceneLight sl= new SceneLight();

		//Generation of a cube
		Material material = new Material( new Vector3f(0.4f,0,0.7f),0.f);
		float[] vertices = new float[]{
				//Front face
				0.5f,  0.5f, 0.0f,
				-0.5f,  0.5f, 0.0f,
				-0.5f, -0.5f, 0.0f,
				0.5f, -0.5f, 0.0f,
				//Top face
				0.5f, 0.5f, 0.0f,
				-0.5f, 0.5f, 0.0f,
				0.5f, 0.5f, -1f,
				-0.5f, 0.5f, -1f,
				//Back face
				0.5f,  0.5f, -1f,
				-0.5f,  0.5f, -1f,
				-0.5f, -0.5f, -1f,
				0.5f, -0.5f, -1f,
				//Bottom face
				0.5f, -0.5f, 0.0f,
				-0.5f, -0.5f, 0.0f,
				0.5f, -0.5f, -1f,
				-0.5f, -0.5f, -1f,
				//Left face
				-0.5f,  0.5f, 0.0f,
				-0.5f,  0.5f, -1.0f,
				-0.5f, -0.5f, 0.0f,
				-0.5f, -0.5f, -1.0f,
				//Right face
				0.5f,  0.5f, 0.0f,
				0.5f,  0.5f, -1.0f,
				0.5f, -0.5f, 0.0f,
				0.5f, -0.5f, -1.0f
		};

		float[] normales = {
				//Front face
				0,0,1f,
				0,0,1f,
				0,0,1f,
				0,0,1f,
				//Top face
				0,1f,0,
				0,1f,0,
				0,1f,0,
				0,1f,0,
				//Back face
				0,0,-1f,
				0,0,-1f,
				0,0,-1f,
				0,0,-1f,
				//Bottom face
				0,-1f,0,
				0,-1f,0,
				0,-1f,0,
				0,-1f,0,
				//Left face
				-1,0,0,
				-1,0,0,
				-1,0,0,
				-1,0,0,
				//Right face
				1,0,0,
				1,0,0,
				1,0,0,
				1,0,0
		};

		int[] indices = new int[]{
				//Front face
				0,1,2,0,2,3,
				//Top face
				4,6,5,6,7,5,
				//Back face
				8,11,10,8,10,9,
				//Bottom Face
				14,12,13,14,13,15,
				//Left face
				16,19,18,16,17,19,
				//Right face
				20,22,21,22,23,21
		};

		sc.add(vertices, material, normales,indices, 1f, new Vector3f(), new Vector3f(0f,0f,-2f));

		sl.setPointLight(new PointLight(new Vector3f(0.2f,1f,0.3f), new Vector3f(), 1, 0, 0, 0), 0);

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

