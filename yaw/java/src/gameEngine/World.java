package gameEngine;

import java.util.ArrayList;

import gameEngine.camera.Camera;
import gameEngine.items.GroupItem;
import gameEngine.light.SceneLight;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.*;


public class World implements Runnable{
	Camera c ;
	ArrayList<Camera> listCamera;
	Renderer renderer;
	SceneVertex sc ;
	SceneLight sl;
	Callback callback;
	ArrayList<GroupItem> listGroup;
	
	
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
	public ArrayList<GroupItem> getListGroup(){
		return listGroup;
	}
	public SceneLight getSceneLight(){
		return sl;
	}
	public Callback getCallback(){
		return callback;
	}
	
	public void init() throws Exception{
		this.listCamera=new ArrayList<Camera>();
		this.c= new Camera();
		this.sc= new SceneVertex();
		this.sl= new SceneLight();
		this.callback=new Callback();
		this.listGroup=new ArrayList<GroupItem>();
	}
	
	public void emptyListCamera() {
		listCamera=new ArrayList<Camera>();
	}
	
	public void setCamera(int index, Camera camera) {
		if (index == 0)
			c = camera;
		listCamera.add(index, camera);
	}
	
	public void run(){
		Window.init();
		try {
			this.renderer= new Renderer();
		} catch (Exception e1) {
			e1.printStackTrace();
			return;
		}

		try{
			//Initialisation of the window we currently use
			glViewport(0, 0, 500,500);
			while ( glfwWindowShouldClose(Window.window) == GLFW_FALSE ) {
				c.update();
				callback.update();
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

