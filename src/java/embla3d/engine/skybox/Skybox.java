package embla3d.engine.skybox;

import embla3d.engine.ShaderProgram;
import embla3d.engine.camera.Camera;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Skybox {
    public final float width, length, height;
    public Vector3f color;
    public boolean init = false;
    private ShaderProgram shaderProg;
    private int vaoId, vboVertex, vboIndices;

    /**
     * Construct a skybox with the specified width length height and color(vector)
     *
     * @param width  width
     * @param length length
     * @param height height
     * @param color  color
     */
    public Skybox(float width, float length, float height, Vector3f color) {
        this.width = width;
        this.height = height;
        this.length = length;
        this.color = color;
    }

    /**
     * Construct a skybox with the specified width length height and color(r,g,b)
     *
     * @param width  width
     * @param length length
     * @param height height
     * @param r      red value of the color
     * @param g      greeb value of the color
     * @param b      blue value of the color
     */
    public Skybox(float width, float length, float height, float r, float g, float b) {
        this.width = width;
        this.height = height;
        this.length = length;
        this.color = new Vector3f(r, g, b);
    }

    /**
     * initialize the skybox shadders and vbo
     *
     * @throws Exception if the last compile operation on shader was unsuccessful or an
     *                   error occured during the shadder creation
     */
    public void init() throws Exception {
        //if the last compile operation on shader was unsuccessful then an exception is launched
        shaderProg = new ShaderProgram();

        shaderProg.createVertexShader(skyboxVertexShader.SHADER_STRING);
        shaderProg.createFragmentShader(skyboxFragmentShader.SHADER_STRING);

        shaderProg.link();
        //Initialization of the uniforms
        shaderProg.createUniform("projectionMatrix");
        shaderProg.createUniform("viewMatrix");
        shaderProg.createUniform("color");


        float lWidth = width / 2, lHeight = height / 2, lLength = length / 2;

        float[] vertices = new float[]{
                lWidth, lHeight, lLength,
                -lWidth, lHeight, lLength,
                lWidth, -lHeight, lLength,
                -lWidth, -lHeight, lLength,
                lWidth, lHeight, -lLength,
                -lWidth, lHeight, -lLength,
                lWidth, -lHeight, -lLength,
                -lWidth, -lHeight, -lLength
        };
        //Box mapping
        int[] indices = new int[]{
                //Front
                0, 2, 3,
                0, 3, 1,
                //Back
                5, 7, 4,
                4, 7, 6,
                //top
                0, 5, 4,
                0, 1, 5,
                //Bottom
                2, 6, 7,
                2, 7, 3,
                //left
                3, 7, 1,
                7, 5, 1,
                //right
                0, 4, 2,
                4, 6, 2
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

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
        init = true;
    }

    public void draw(Camera cam) {
        shaderProg.bind();
        // Bind to the VAO
        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);

        shaderProg.setUniform("projectionMatrix", cam.getCameraMat());

        Matrix4f mat = new Matrix4f(cam.setupViewMatrix());
        // we do not want translation to be applied to the sky box. so we fixed the value to 0
        mat.m30(0);
        mat.m31(0);
        mat.m32(0);

        shaderProg.setUniform("viewMatrix", mat);
        shaderProg.setUniform("color", color);

        glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        shaderProg.unbind();
    }

    /**
     * Deallocation and deletion of VAO and VBO
     */
    public void cleanUp() {
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
