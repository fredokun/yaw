package gameEngine;

import java.util.ArrayList;

import gameEngine.light.AmbiantLight;
import gameEngine.light.PointLight;
import gameEngine.light.SceneLight;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.*;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;

import basicMeshes.CubeItem;

public class World implements Runnable{
	Camera c ;
	ArrayList<Camera> listCamera;
	Renderer renderer;
	SceneVertex sc ;
	SceneLight sl;
	
	
	public Camera getCamera(){
		return c;
	}
	
	public void setCamera(Camera c){
		this.c=c;
	}
	public ArrayList<Camera> getListCamera(){
		return listCamera;
	}
	
	public SceneVertex getSceneVertex(){
		return sc;
	}
	
	public SceneLight getSceneLight(){
		return sl;
	}
	

	public CubeItem CreateCube(float red, float green, float blue){
		Material material = new Material( new Vector3f(red,green,blue),0.f);
		CubeItem cube = new CubeItem(material, 1f, new Vector3f(), new Vector3f(0f,0f,-2f));
		sc.add(cube);
		return cube;
	}
	
	public CubeItem CreateCube(){
		Material material = new Material( new Vector3f(1f,1f,1f),0.f);
		CubeItem cube = new CubeItem(material, 1f, new Vector3f(), new Vector3f(0f,0f,-2f));
		sc.add(cube);
		return cube;
	}
	public void init() throws Exception{
		this.listCamera=new ArrayList<Camera>();
		this.c= new Camera();
		listCamera.add(c);
		this.sc= new SceneVertex();
		this.sl= new SceneLight();
	}
	public void run(){
		Window.init();
		try {
			this.renderer= new Renderer();
		} catch (Exception e1) {
			e1.printStackTrace();
			return;
		}
		sl.setAmbiant(new AmbiantLight(new Vector3f(0,1f,1f),1f));
		sl.setPointLight(new PointLight(new Vector3f(1f,1f,0.0f), new Vector3f(), 1, 0, 0.1f, 0), 0);

		try{
			//Initialisation of the window we currently use
			glViewport(0, 0, 500,500);
			while ( glfwWindowShouldClose(Window.window) == GLFW_FALSE ) {
				c.update();
				//Clean the window
				boolean isResized = Window.clear();
				synchronized(sc){
			
				//Update the world
				renderer.render(sc, sl, isResized,c);
				}
				//Thread.sleep(1000);
				//Update the window's picture
				Window.update();
			}
		}catch(Exception e){System.out.println(e.getMessage());}
		finally{
			renderer.cleanUp();
			sc.cleanUp();
			//On desaloue le materiel de la fenetre
			Window.cleanUp();
		}
	}
}

