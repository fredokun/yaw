package embla3d.engine.meshs;

import embla3d.engine.ShaderProgram;
import embla3d.engine.items.MyItem;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/**
 *
 */
public class Mesh {
    //reference to the VAO(wrapper)
    private int vaoId;

    //VBO's ID
    private int vboVertexId;
    private int vboNormId;
    private int vboIndicesId;

    //VBO
    private float[] vertices;//vertices
    private float[] normals;
    private int[] indices; //order into which  vertices should be drawn by referring to their  position
    private int weight;  // the weight of an object in a group (e.g. a mass in a group planets)

    private Material material;


    /**
     * Construct a Mesh with the specified material (cx, cy, cz, reflectance), vertices, normals, indices and weight
     * Material is dynamically created from cx cy cz and reflectance
     * Reflectance should be between 0 and 1
     *
     * @param vertices    Vertex array
     * @param cx          colour R
     * @param cy          colour G
     * @param cz          colour B
     * @param reflectance reflectance
     * @param normals     normals
     * @param indices     order into which  vertices should be drawn by referring to their  position
     * @param weight      weight ??
     */
    public Mesh(float[] vertices, float cx, float cy, float cz, float reflectance, float[] normals, int[] indices, int weight) {
        super();
        this.vertices = vertices;
        this.normals = normals;
        this.indices = indices;
        this.material = new Material(new Vector3f(cx, cy, cz), reflectance);
        this.weight = weight;
    }

    /**
     * Construct a Mesh with the specified material (cx, cy, cz, reflectance), vertices, normals, indices
     * Material is dynamically created from cx cy cz and reflectance
     * Reflectance should be between 0 and 1
     *
     * @param vertices    Vertex array
     * @param cx          colour R
     * @param cy          colour G
     * @param cz          colour B
     * @param reflectance reflectance
     * @param normals     normals
     * @param indices     order into which  vertices should be drawn by referring to their  position
     */
    public Mesh(float[] vertices, float cx, float cy, float cz, float reflectance, float[] normals, int[] indices) {
        super();
        this.vertices = vertices;
        this.normals = normals;
        this.indices = indices;
        this.material = new Material(new Vector3f(cx, cy, cz), reflectance);
        this.weight = vertices.length;
    }

    /**
     * Construct a Mesh with the specified material , vertices, normals, indices and weight
     *
     * @param vertices Vertex array
     * @param normals  normals
     * @param indices  order into which  vertices should be drawn by referring to their  position
     * @param weight   weight ??
     */
    public Mesh(float[] vertices, Material material, float[] normals, int[] indices, int weight) {
        super();
        this.vertices = vertices;
        this.normals = normals;
        this.indices = indices;
        this.material = material;
        this.weight = weight;
    }

    /**
     * Construct a Mesh with the specified material , vertices, normals and indices.
     *
     * @param vertices Vertex array
     * @param normals  normals
     * @param indices  order into which  vertices should be drawn by referring to their  position
     */
    public Mesh(float[] vertices, Material material, float[] normals, int[] indices) {
        super();
        this.vertices = vertices;
        this.normals = normals;
        this.indices = indices;
        this.material = material;
        this.weight = vertices.length;
    }

    /**
     * Initialize  vertex, normals and indices buffer
     */
    public void init() {
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        //Initialization of VBO
        //VBO of vertex
        FloatBuffer verticeBuffer = BufferUtils.createFloatBuffer(vertices.length);
        verticeBuffer.put(vertices).flip();
        vboVertexId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboVertexId);
        glBufferData(GL_ARRAY_BUFFER, verticeBuffer, GL_STATIC_DRAW);

        //We explain to OpenGL how to read our Buffers.
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        //VBO of normals
        FloatBuffer normBuffer = BufferUtils.createFloatBuffer(normals.length);
        normBuffer.put(normals).flip();
        vboNormId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboNormId);
        glBufferData(GL_ARRAY_BUFFER, normBuffer, GL_STATIC_DRAW);

        //We explain to OpenGL how to read our Buffers.
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);

        //VBO of indices
        IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
        indicesBuffer.put(indices).flip();
        vboIndicesId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboIndicesId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    /**
     * Draw the specified item
     *
     * @param item       item
     * @param sh         shaderProgram
     * @param viewMatrix viewMatrix
     */
    public void draw(MyItem item, ShaderProgram sh, Matrix4f viewMatrix) {
        // Bind to the VAO
        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        sh.setUniform("material", material);
        sh.setUniform("modelViewMatrix", item.getWorldMatrix());
        // Draw the vertices
        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);

        // Restore state
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
    }

    public void draw(ArrayList<MyItem> items, ShaderProgram sh, Matrix4f viewMatrix) {
        // Bind to the VAO
        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        sh.setUniform("material", material);
        for (MyItem i : items) {
            Matrix4f modelViewMat = new Matrix4f(viewMatrix).mul(i.getWorldMatrix());
            sh.setUniform("modelViewMatrix", modelViewMat);
            // Draw the vertices
            glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
        }
        // Restore state
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
    }

    public void cleanUp() {
        //deallocation of VAO and VBO
        glDisableVertexAttribArray(0);

        // Delete the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboVertexId);
        glDeleteBuffers(vboIndicesId);
        glDeleteBuffers(vboNormId);

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

    public float[] getVertices() {
        return vertices;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public float[] getNormals() {
        return normals;
    }

    public int[] getIndices() {
        return indices;
    }

    public int getWeight() {
        return weight;
    }
}
