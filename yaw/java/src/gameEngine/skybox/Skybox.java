package gameEngine.skybox;

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
import java.nio.file.Files;
import java.nio.file.Paths;

import gameEngine.ShaderProgram;
import gameEngine.camera.Camera;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

public class Skybox {
	public Vector3f color;
	private ShaderProgram shaderProg;
	public boolean init=false;
	public final float width, length, height;
	
	private int vaoId,vboVertex,vboIndices;
	
	public Skybox(float width, float length, float height, Vector3f color) {
		this.width = width;
		this.height = height;
		this.length = length;
		this.color = color;
	}
	
	public Skybox(float width, float length, float height, float r,float g,float b){
		this.width = width;
		this.height = height;
		this.length = length;
		this.color = new Vector3f(r,g,b);
	}
	
	public void init() throws Exception{
		
		shaderProg = new ShaderProgram();
		try{
			shaderProg.createVertexShader(new String(Files.readAllBytes(Paths.get("./java/src/gameEngine/skybox/skyboxVertexShader.vs"))));
			shaderProg.createFragmentShader(new String(Files.readAllBytes(Paths.get("./java/src/gameEngine/skybox/skyboxFragmentShader.fs"))));
		}catch(Exception e){
			try{
				shaderProg.createVertexShader(new String(Files.readAllBytes(Paths.get("./src/gameEngine/skybox/skyboxVertexShader.vs"))));
				shaderProg.createFragmentShader(new String(Files.readAllBytes(Paths.get("./src/gameEngine/skybox/skyboxFragmentShader.fs"))));
			}catch(Exception x){
				shaderProg.createVertexShader(new String(Files.readAllBytes(Paths.get("./yaw/java/src/gameEngine/skybox/skyboxVertexShader.vs"))));
				shaderProg.createFragmentShader(new String(Files.readAllBytes(Paths.get("./yaw/java/src/gameEngine/skybox/skyboxFragmentShader.fs"))));
			}

		}
		shaderProg.link();
		//Initialization of the uniforms
		shaderProg.createUniform("projectionMatrix");
		shaderProg.createUniform("viewMatrix");
		shaderProg.createUniform("color");
		
		float Hwidth = width/2, Hheight = height/2, Hlength = length/2;
		
		float[] vertices = new float[]{
				Hwidth,Hheight,Hlength,
				-Hwidth,Hheight,Hlength,
				Hwidth,-Hheight,Hlength,
				-Hwidth,-Hheight,Hlength,
				Hwidth,Hheight,-Hlength,
				-Hwidth,Hheight,-Hlength,
				Hwidth,-Hheight,-Hlength,
				-Hwidth,-Hheight,-Hlength
		};
		
		int[] indices = new int[]{
			//Front
			0,2,3,
			0,3,1,
			//Back
			5,7,4,
			4,7,6,
			//top
			0,5,4,
			0,1,5,
			//Bottom
			2,6,7,
			2,7,3,
			//left
			3,7,1,
			7,5,1,
			//right
			0,4,2,
			4,6,2
		};
		
		vaoId = glGenVertexArrays();
		glBindVertexArray(vaoId);
		
		//Initialization of VBO
		//VBO of vertex
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
		verticesBuffer.put(vertices).flip();
		vboVertex = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboVertex);
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);

		//We explain to OpenGL how to read our Buffers.
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

		//VBO of indices
		IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
		indicesBuffer.put(indices).flip();
		vboIndices = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboIndices);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

		glBindBuffer(GL_ARRAY_BUFFER,0);
		glBindVertexArray(0);
		init=true;
	}
	
	public void draw(Camera cam){
		shaderProg.bind();
		// Bind to the VAO
		glBindVertexArray(vaoId);
		glEnableVertexAttribArray(0);
		
		shaderProg.setUniform("projectionMatrix", cam.getCameraMat());
		
		Matrix4f mat = new Matrix4f(cam.setupViewMatrix());
		mat.m30 = 0;
		mat.m31 = 0;
		mat.m32 = 0;

		shaderProg.setUniform("viewMatrix", mat);
		shaderProg.setUniform("color", color);

		glDrawElements(GL_TRIANGLES, 36,GL_UNSIGNED_INT, 0);

		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
		shaderProg.unbind();
	}
	
	public void cleanUp(){
		// Deallocation of VAO and VBO
		glDisableVertexAttribArray(0);

		// Delete the VBO
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glDeleteBuffers(vboVertex);
		glDeleteBuffers(vboIndices);

		// Delete the VAO
		glBindVertexArray(0);
		glDeleteVertexArrays(vaoId);
	}
}
