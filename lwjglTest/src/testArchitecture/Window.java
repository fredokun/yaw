package testArchitecture;

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
	
	//Initialise et ouvre une fenêtre.
	public static void init(){
		if(glfwInit()== 0){
			throw new IllegalStateException("Unable to initialize GLFW");
		}
		
		glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		
		width = 500;
		height = 500;
		resized = false;
		
		window =  glfwCreateWindow(500,500,"Premier Triangle", NULL,NULL);
		
		
		
		glfwSetKeyCallback(window, keyCallback=new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                    glfwSetWindowShouldClose(window, GLFW_TRUE); // We will detect this in our rendering loop
            }
        });
		
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
	
	//Désalloue le materielle utilisé pour la fenêtre
	public static void cleanUp(){
		glfwTerminate();
	}
	
	//A appeller avant de mettre à jour le contenue de la fenêtre
	public static boolean clear(){
		 glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		 if(resized){
			 glViewport(0, 0, width, height);
			 resized = false;
			 return true;
		 }
		 return false;
	}
	//Dessine le contenue de la fenêtre
	public static void update(){
			 glfwSwapBuffers(window);
			 glfwPollEvents();		
	}
	
	public static double aspectRatio(){
		return width/height;
	}
}
