package testArchitecture;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;

public class Mesh {
	private int vaoId;
	
	private int vboVertex;
	private int vboColor;
	private int vboIndices;
	
	private FloatBuffer verticeBuffer;
	private FloatBuffer colorBuffer;
	private IntBuffer indicesBuffer;
	
	public Mesh(float[] vertices, float[] couleur, int[] indices) {
		super();
		
		vaoId = glGenVertexArrays();
		glBindVertexArray(vaoId);
		
		//on initialise le VBO
				vboVertex = glGenBuffers();
				glBindBuffer(GL_ARRAY_BUFFER, vboVertex);

			//VBO des vertex
				verticeBuffer = BufferUtils.createFloatBuffer(vertices.length);
				
				verticeBuffer.put(vertices).flip();
				glBufferData(GL_ARRAY_BUFFER, verticeBuffer, GL_STATIC_DRAW);

				//on explique a opengl comment lire les valeurs.
				glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
			
			//VBO des couleurs
				vboColor = glGenBuffers();
				glBindBuffer(GL_ARRAY_BUFFER, vboColor);
				
				colorBuffer = BufferUtils.createFloatBuffer(couleur.length);
				colorBuffer.put(couleur).flip();
				glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
				
				//on explique a opengl comment lire les valeurs.
				glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);
				
			//VBO des indices
				vboIndices = glGenBuffers();
				glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboIndices);
				indicesBuffer = BufferUtils.createIntBuffer(indices.length);
				indicesBuffer.put(indices).flip();
				glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
				
				glBindBuffer(GL_ARRAY_BUFFER,0);
				glBindVertexArray(0);
	}
	
	public void draw(MyItem item){
		// Bind to the VAO
				 glBindVertexArray(vaoId);
				 glEnableVertexAttribArray(0);
				 glEnableVertexAttribArray(1);
				 
				 // Draw the vertices (les deux dernier parametre indique respectivement first et length)
				 glDrawElements(GL_TRIANGLES, 6,GL_UNSIGNED_INT, 0);

				 // Restore state
				 glDisableVertexAttribArray(0);
				 glDisableVertexAttribArray(1);
				 glBindVertexArray(0);
	}
	
	public void draw(ArrayList<MyItem> items, ShaderProgram sh){
		// Bind to the VAO
		glBindVertexArray(vaoId);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		for(MyItem i: items){
			sh.setUniform("worldMatrice", i.getWorldMatrix());
			// Draw the vertices (les deux dernier parametre indique respectivement first et length)
			glDrawElements(GL_TRIANGLES, 6,GL_UNSIGNED_INT, 0);
		}
		// Restore state
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
	}

	public void cleanUp(){
		//on desaloue les VAO et VBO
		glDisableVertexAttribArray(0);

		// Delete the VBO
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glDeleteBuffers(vboVertex);
		glDeleteBuffers(vboColor);
		glDeleteBuffers(vboIndices);

		// Delete the VAO
		glBindVertexArray(0);
		glDeleteVertexArrays(vaoId);
	}
}
