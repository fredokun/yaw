package gameEngine;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;

public class Window {
	public static long window;
	protected static int width;
	protected static int height;
	protected static boolean resized;
	
	//protege des erreurs du au garbage collector
	private static GLFWKeyCallback keyCallback;
	private static GLFWWindowSizeCallback windowSizeCallback;
	
	//Initialise et ouvre une fenetre.
	public static void init(){
		if(glfwInit()== 0){
			throw new IllegalStateException("Unable to initialize GLFW");
		}
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		
		width = 500;
		height = 500;
		resized = false;
		
		window =  glfwCreateWindow(500,500,"Lumiere", NULL,NULL);
		
		glfwSetKeyCallback(window, keyCallback = new Input());
		
		// Setup resize callback
        glfwSetWindowSizeCallback(window, windowSizeCallback=new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                Window.width = width;
                Window.height = height;
                Window.resized=true;
            }
        });
        
		glfwMakeContextCurrent(window);
		
		GL.createCapabilities();
		
		
		glfwSwapInterval(1);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
	}
	
	//Desalloue le materielle utilise pour la fenetre
	public static void cleanUp(){
		glfwTerminate();
	}
	
	//A appeller avant de mettre a jour le contenue de la fenetre
	public static boolean clear(){
		 glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		 if(resized){
			 glViewport(0, 0, width, height);
			 resized = false;
			 return true;
		 }
		 return false;
	}
	//Dessine le contenue de la fenetre
	public static void update(){
			 glfwSwapBuffers(window);
			 glfwPollEvents();		
	}
	
	public static double aspectRatio(){
		return width/(double)height;
	}
}
