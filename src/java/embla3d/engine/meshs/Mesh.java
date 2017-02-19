package embla3d.engine.meshs;

import embla3d.engine.ShaderProgram;
import embla3d.engine.items.Item;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    private float[] mVertices;//mVertices
    private float[] mNormals;
    private int[] mIndices; //order into which  mVertices should be drawn by referring to their  position
    private int mWeight;  // the mWeight of an object in a group (e.g. a mass in a group planets)

    private Material mMaterial;

    private Map<String, String> mOptionalAttributes;


    /**
     * Construct a Mesh with the specified mMaterial (cx, cy, cz, reflectance), mVertices, mNormals, mIndices and mWeight
     * Material is dynamically created from cx cy cz and reflectance
     * Reflectance should be between 0 and 1
     *
     * @param vertices    Vertex array
     * @param cx          colour R
     * @param cy          colour G
     * @param cz          colour B
     * @param reflectance reflectance
     * @param normals     mNormals
     * @param indices     order into which  mVertices should be drawn by referring to their  position
     * @param weight      mWeight ??
     */
    public Mesh(float[] vertices, float cx, float cy, float cz, float reflectance, float[] normals, int[] indices, int weight) {
        super();
        this.mVertices = vertices;
        this.mNormals = normals;
        this.mIndices = indices;
        this.mMaterial = new Material(new Vector3f(cx, cy, cz), reflectance);
        this.mWeight = weight;
        this.mOptionalAttributes = new HashMap<>();
    }

    /**
     * Construct a Mesh with the specified mMaterial (cx, cy, cz, reflectance), mVertices, mNormals, mIndices
     * Material is dynamically created from cx cy cz and reflectance
     * Reflectance should be between 0 and 1
     *
     * @param vertices    Vertex array
     * @param cx          colour R
     * @param cy          colour G
     * @param cz          colour B
     * @param reflectance reflectance
     * @param normals     mNormals
     * @param indices     order into which  mVertices should be drawn by referring to their  position
     */
    public Mesh(float[] vertices, float cx, float cy, float cz, float reflectance, float[] normals, int[] indices) {
        super();
        this.mVertices = vertices;
        this.mNormals = normals;
        this.mIndices = indices;
        this.mMaterial = new Material(new Vector3f(cx, cy, cz), reflectance);
        this.mWeight = vertices.length;
        this.mOptionalAttributes = new HashMap<>();
    }

    /**
     * Construct a Mesh with the specified mMaterial , mVertices, mNormals, mIndices and mWeight
     *
     * @param vertices Vertex array
     * @param normals  mNormals
     * @param indices  order into which  mVertices should be drawn by referring to their  position
     * @param weight   mWeight ??
     */
    public Mesh(float[] vertices, Material material, float[] normals, int[] indices, int weight) {
        super();
        this.mVertices = vertices;
        this.mNormals = normals;
        this.mIndices = indices;
        this.mMaterial = material;
        this.mWeight = weight;
        this.mOptionalAttributes = new HashMap<>();
    }

    /**
     * Construct a Mesh with the specified mMaterial , mVertices, mNormals and mIndices.
     *
     * @param vertices Vertex array
     * @param normals  mNormals
     * @param indices  order into which  mVertices should be drawn by referring to their  position
     */
    public Mesh(float[] vertices, Material material, float[] normals, int[] indices) {
        super();
        this.mVertices = vertices;
        this.mNormals = normals;
        this.mIndices = indices;
        this.mMaterial = material;
        this.mWeight = vertices.length;
        this.mOptionalAttributes = new HashMap<>();
    }

    /**
     * Initialize  vertex, mNormals and mIndices buffer
     */
    public void init() {
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        //Initialization of VBO
        //VBO of vertex
        FloatBuffer verticeBuffer = BufferUtils.createFloatBuffer(mVertices.length);
        verticeBuffer.put(mVertices).flip();
        vboVertexId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboVertexId);
        glBufferData(GL_ARRAY_BUFFER, verticeBuffer, GL_STATIC_DRAW);

        //We explain to OpenGL how to read our Buffers.
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        //VBO of mNormals
        FloatBuffer normBuffer = BufferUtils.createFloatBuffer(mNormals.length);
        normBuffer.put(mNormals).flip();
        vboNormId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboNormId);
        glBufferData(GL_ARRAY_BUFFER, normBuffer, GL_STATIC_DRAW);

        //We explain to OpenGL how to read our Buffers.
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);

        //VBO of mIndices
        IntBuffer indicesBuffer = BufferUtils.createIntBuffer(mIndices.length);
        indicesBuffer.put(mIndices).flip();
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
    public void draw(Item item, ShaderProgram sh, Matrix4f viewMatrix) {
        // Bind to the VAO
        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        sh.setUniform("material", mMaterial);
        sh.setUniform("modelViewMatrix", item.getWorldMatrix());
        // Draw the mVertices
        glDrawElements(GL_TRIANGLES, mIndices.length, GL_UNSIGNED_INT, 0);

        // Restore state
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
    }

    public void draw(ArrayList<Item> items, ShaderProgram sh, Matrix4f viewMatrix) {
        // Bind to the VAO
        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        sh.setUniform("material", mMaterial);
        for (Item i : items) {
            Matrix4f modelViewMat = new Matrix4f(viewMatrix).mul(i.getWorldMatrix());
            sh.setUniform("modelViewMatrix", modelViewMat);
            // Draw the mVertices
            glDrawElements(GL_TRIANGLES, mIndices.length, GL_UNSIGNED_INT, 0);
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
        return mVertices;
    }

    public Material getMaterial() {
        return mMaterial;
    }

    public void setMaterial(Material material) {
        this.mMaterial = material;
    }

    public float[] getNormals() {
        return mNormals;
    }

    public int[] getIndices() {
        return mIndices;
    }

    public int getWeight() {
        return mWeight;
    }

    /**
     * Returns the value to which the specified @attributeName is mapped,
     * or null if this map contains no mapping for the key.
     *
     * @param attributeName name of the attribute (most of the time it will be clojure keywords)
     * @return the corresponding value if exist null otherwise
     */
    public Object getAttribute(String attributeName) {
        return this.mOptionalAttributes.get(attributeName);
    }

    /**
     * Copies all of the mappings from the specified map to this map (optional operation).
     * The effect of this call is equivalent to that of calling put(k, v) on this map once for each mapping
     * from key k to value v in the specified map.
     * The behavior of this operation is undefined if the specified map is modified while the operation is in progress.
     *
     * @param pOptionalAttributes mappings to be stored in this map
     */
    public void putOptionalAttributes(Map<String, String> pOptionalAttributes) {
        this.mOptionalAttributes.putAll(pOptionalAttributes);
    }


}
