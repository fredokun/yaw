package gameEngine;

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

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;

public class Mesh {
	private int vaoId;
	
	private int vboVertex;
	private int vboNorm;
	private int vboIndices;
	
	private FloatBuffer verticeBuffer;
	private FloatBuffer normBuffer;
	private IntBuffer indicesBuffer;

	private Material material;
	
	public Mesh(float[] vertices, Material material,float[] normales, int[] indices) {
		super();
		this.material = material;
		//Initialisation of VBO
			//VBO of vertex
				verticeBuffer = BufferUtils.createFloatBuffer(vertices.length);
				verticeBuffer.put(vertices).flip();
			//VBO of normals
				normBuffer = BufferUtils.createFloatBuffer(normales.length);
				normBuffer.put(normales).flip();			
			//VBO of indices
				indicesBuffer = BufferUtils.createIntBuffer(indices.length);
				indicesBuffer.put(indices).flip();		
	}

	public void init(){
		vaoId = glGenVertexArrays();
		glBindVertexArray(vaoId);
		vboVertex = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboVertex);
		glBufferData(GL_ARRAY_BUFFER, verticeBuffer, GL_STATIC_DRAW);

		//We explain to OpenGL how to read our Buffers.
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		
		vboNorm = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboNorm);
		glBufferData(GL_ARRAY_BUFFER, normBuffer, GL_STATIC_DRAW);
		
		//We explain to OpenGL how to read our Buffers.
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
		
		vboIndices = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboIndices);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
		
		glBindBuffer(GL_ARRAY_BUFFER,0);
		glBindVertexArray(0);
	}
	
	public void draw(MyItem item, ShaderProgram sh,Matrix4f viewMatrix){
		// Bind to the VAO
		glBindVertexArray(vaoId);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		sh.setUniform("material", material);
		sh.setUniform("modelViewMatrix", item.getWorldMatrix());
		// Draw the vertices
		glDrawElements(GL_TRIANGLES, indicesBuffer.limit(),GL_UNSIGNED_INT, 0);

		// Restore state
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
	}

	public void draw(ArrayList<MyItem> items, ShaderProgram sh, Matrix4f viewMatrix){
		// Bind to the VAO
		glBindVertexArray(vaoId);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		sh.setUniform("material", material);
		for(MyItem i: items){
			Matrix4f modelViewMat = new Matrix4f(viewMatrix).mul( i.getWorldMatrix());
			sh.setUniform("modelViewMatrix", modelViewMat);
			// Draw the vertices
			glDrawElements(GL_TRIANGLES, indicesBuffer.limit(),GL_UNSIGNED_INT, 0);
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
		glDeleteBuffers(vboIndices);
		glDeleteBuffers(vboNorm);

		// Delete the VAO
		glBindVertexArray(0);
		glDeleteVertexArrays(vaoId);
	}
	
	public void setMaterial(Material material){
		this.material=material;
	}
}
