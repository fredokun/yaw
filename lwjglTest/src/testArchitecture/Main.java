package testArchitecture;

import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.*;

import org.joml.Vector3f;

public class Main {

	public static void main(String[] args) throws Exception {
		Window.init();
		Renderer renderer = new Renderer();
		Scene sc = new Scene();
		
		float[] vertices = new float[]{
				0.5f,  0.5f, 0.0f,
				-0.5f,  0.5f, 0.0f,
				-0.5f, -0.5f, 0.0f,
				0.5f, -0.5f, 0.0f
		};
		
		float[] colors = new float[]
				{
				    1.f, 0.f, 0.f, 1.f,  // Red color, for the first vertex
				    0.f, 1.f, 0.f, 1.f,  // Green color, for the second vertex
				    0.f, 0.f, 1.f, 1.f,   // Blue color, for the third vertex
				    0.f, 1.f, 1.f, 1.f
				};
		
		int[] indices = new int[]{0,1,2,0,2,3};
		
		sc.add(vertices, colors, indices, 1f, new Vector3f(), new Vector3f(0f,0f,-2f));
		
		try{
			//initialise quel et la fenetre dans laquel on ecrit, il faut le rappeller si la taille de la feneter est modifie
			//en changeant les deux dernier parametre
			glViewport(0, 0, 500,500);
			while ( glfwWindowShouldClose(Window.window) == GLFW_FALSE ) {
				//on nettois la fenetre
				boolean isResized = Window.clear();
				//MAJ monde 3D
				renderer.render(sc, isResized);
				//MAJ affichage de la fenetre
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
