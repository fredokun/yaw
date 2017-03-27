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
    private int mVaoId;

    //VBO's ID
    private int mVboVertexId;
    private int mVboNormId;
    private int mVboIndicesId;

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
     * @param pVertices   Vertex array
     * @param cx          colour R
     * @param cy          colour G
     * @param cz          colour B
     * @param reflectance reflectance
     * @param pNormals    mNormals
     * @param pIndices    order into which  mVertices should be drawn by referring to their  position
     * @param pWeight     mWeight ??
     */
    public Mesh(float[] pVertices, float cx, float cy, float cz, float reflectance, float[] pNormals, int[] pIndices, int pWeight) {
        super();
        this.mVertices = pVertices;
        this.mNormals = pNormals;
        this.mIndices = pIndices;
        this.mMaterial = new Material(new Vector3f(cx, cy, cz), reflectance);
        this.mWeight = pWeight;
        this.mOptionalAttributes = new HashMap<>();
    }

    /**
     * Construct a Mesh with the specified mMaterial (cx, cy, cz, reflectance), mVertices, mNormals, mIndices
     * Material is dynamically created from cx cy cz and reflectance
     * Reflectance should be between 0 and 1
     *
     * @param pVertices   Vertex array
     * @param cx          colour R
     * @param cy          colour G
     * @param cz          colour B
     * @param reflectance reflectance
     * @param pNormals    mNormals
     * @param pIndices    order into which  mVertices should be drawn by referring to their  position
     */
    public Mesh(float[] pVertices, float cx, float cy, float cz, float reflectance, float[] pNormals, int[] pIndices) {
        super();
        this.mVertices = pVertices;
        this.mNormals = pNormals;
        this.mIndices = pIndices;
        this.mMaterial = new Material(new Vector3f(cx, cy, cz), reflectance);
        this.mWeight = pVertices.length;
        this.mOptionalAttributes = new HashMap<>();
    }

    /**
     * Construct a Mesh with the specified mMaterial , mVertices, mNormals, mIndices and mWeight
     *
     * @param pVertices Vertex array
     * @param pNormals  mNormals
     * @param pIndices  order into which  mVertices should be drawn by referring to their  position
     * @param pWeight   mWeight ??
     */
    public Mesh(float[] pVertices, Material pMaterial, float[] pNormals, int[] pIndices, int pWeight) {
        super();
        this.mVertices = pVertices;
        this.mNormals = pNormals;
        this.mIndices = pIndices;
        this.mMaterial = pMaterial;
        this.mWeight = pWeight;
        this.mOptionalAttributes = new HashMap<>();
    }

    /**
     * Construct a Mesh with the specified mMaterial , mVertices, mNormals and mIndices.
     *
     * @param pVertices Vertex array
     * @param pNormals  mNormals
     * @param pIndices  order into which  mVertices should be drawn by referring to their  position
     */
    public Mesh(float[] pVertices, Material pMaterial, float[] pNormals, int[] pIndices) {
        super();
        this.mVertices = pVertices;
        this.mNormals = pNormals;
        this.mIndices = pIndices;
        this.mMaterial = pMaterial;
        this.mWeight = pVertices.length;
        this.mOptionalAttributes = new HashMap<>();
    }

    /**
     * Initialize  vertex, mNormals and mIndices buffer
     */
    public void init() {
        mVaoId = glGenVertexArrays();
        glBindVertexArray(mVaoId);

        //Initialization of VBO
        //VBO of vertex
        FloatBuffer verticeBuffer = BufferUtils.createFloatBuffer(mVertices.length);
        verticeBuffer.put(mVertices).flip();
        mVboVertexId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, mVboVertexId);
        glBufferData(GL_ARRAY_BUFFER, verticeBuffer, GL_STATIC_DRAW);

        //We explain to OpenGL how to read our Buffers.
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        //VBO of mNormals
        FloatBuffer normBuffer = BufferUtils.createFloatBuffer(mNormals.length);
        normBuffer.put(mNormals).flip();
        mVboNormId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, mVboNormId);
        glBufferData(GL_ARRAY_BUFFER, normBuffer, GL_STATIC_DRAW);

        //We explain to OpenGL how to read our Buffers.
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);

        //VBO of mIndices
        IntBuffer indicesBuffer = BufferUtils.createIntBuffer(mIndices.length);
        indicesBuffer.put(mIndices).flip();
        mVboIndicesId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, mVboIndicesId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    /**
     * Draw the specified item
     *
     * @param pItem          item
     * @param pShaderProgram shaderProgram
     * @param pViewMatrix    viewMatrix
     */
    public void draw(Item pItem, ShaderProgram pShaderProgram, Matrix4f pViewMatrix) {
        // Bind to the VAO
        glBindVertexArray(mVaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        pShaderProgram.setUniform("material", mMaterial);
        pShaderProgram.setUniform("modelViewMatrix", pItem.getWorldMatrix());
        // Draw the mVertices
        glDrawElements(GL_TRIANGLES, mIndices.length, GL_UNSIGNED_INT, 0);

        // Restore state
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
    }

    public void draw(ArrayList<Item> pItems, ShaderProgram pShaderProgram, Matrix4f pViewMatrix) {
        // Bind to the VAO
        glBindVertexArray(mVaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        pShaderProgram.setUniform("material", mMaterial);
        for (Item i : pItems) {
            Matrix4f modelViewMat = new Matrix4f(pViewMatrix).mul(i.getWorldMatrix());
            pShaderProgram.setUniform("modelViewMatrix", modelViewMat);
            // Draw the mVertices
            glDrawElements(GL_TRIANGLES, mIndices.length, GL_UNSIGNED_INT, 0);
        }
        // Restore state
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
    }

    public void cleanUp() {
        //de-allocation of VAO and VBO
        glDisableVertexAttribArray(0);

        // Delete the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(mVboVertexId);
        glDeleteBuffers(mVboIndicesId);
        glDeleteBuffers(mVboNormId);

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(mVaoId);
    }

    public float[] getVertices() {
        return mVertices;
    }

    public Material getMaterial() {
        return mMaterial;
    }

    public void setMaterial(Material pMaterial) {
        this.mMaterial = pMaterial;
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
     * @param pAttributeName name of the attribute (most of the time it will be clojure keywords)
     * @return the corresponding value if exist null otherwise
     */
    public Object getAttribute(String pAttributeName) {
        return this.mOptionalAttributes.get(pAttributeName);
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
