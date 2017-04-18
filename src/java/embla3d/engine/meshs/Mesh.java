package embla3d.engine.meshs;

import embla3d.engine.ShaderProgram;
import embla3d.engine.items.Item;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/**
 *
 */
public class Mesh {
    protected final List<Integer> vboIdList;
    //reference to the VAO(wrapper)
    private int mVaoId;
    //VBO's ID

    //VBO
    private float[] mVertices;//mVertices
    private float[] mNormals;
    private int[] mIndices; //order into which  mVertices should be drawn by referring to their  position
    private float[] mTextCoords;
    private int mWeight;  // the mWeight of an object in a group (e.g. a mass in a group planets)
    private Material mMaterial;


    private Map<String, String> mOptionalAttributes;


    /**
     * Construct a Mesh with the specified mMaterial , mVertices, mNormals , mTextureCoordinate and mIndices.
     *
     * @param pVertices   Vertex array
     * @param pTextCoords texture coodinate
     * @param pNormals    mNormals
     * @param pIndices    order into which  mVertices should be drawn by referring to their  position
     */
    public Mesh(float[] pVertices, float[] pTextCoords, float[] pNormals, int[] pIndices) {
        this(pVertices, pTextCoords, pNormals, pIndices, pVertices.length);
    }


    /**
     * Construct a Mesh with the specified  mVertices, mNormals, mIndices , mTextureCoordinate and mWeight
     *
     * @param pVertices   Vertex array
     * @param pTextCoords texture coodinate
     * @param pNormals    mNormals
     * @param pIndices    order into which  mVertices should be drawn by referring to their  position
     * @param pWeight     mWeight numbre of vertices
     */
    public Mesh(float[] pVertices, float[] pTextCoords, float[] pNormals, int[] pIndices, int pWeight) {
        this.mMaterial = new Material();
        this.mVertices = pVertices;
        this.mNormals = pNormals;
        this.mIndices = pIndices;
        this.mWeight = pWeight;
        this.mTextCoords = pTextCoords == null ? new float[1] : pTextCoords;
        this.mOptionalAttributes = new HashMap<>();
        this.vboIdList = new ArrayList<>();
    }

    /**
     * Initialize  vertex, mNormals, mIndices and mTextureCoordinate buffer
     */
    public void init() {
        //initialization order is important do not change unless you know what to do
        mVaoId = glGenVertexArrays();
        glBindVertexArray(mVaoId);

        //Initialization of VBO

        //VBO of vertex layout 0 in vertShader.vs
        FloatBuffer verticeBuffer = BufferUtils.createFloatBuffer(mVertices.length);
        verticeBuffer.put(mVertices).flip();
        int lVboVertexId = glGenBuffers();
        vboIdList.add(lVboVertexId);
        glBindBuffer(GL_ARRAY_BUFFER, lVboVertexId);
        glBufferData(GL_ARRAY_BUFFER, verticeBuffer, GL_STATIC_DRAW);

        //We explain to OpenGL how to read our Buffers.
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        // Texture coordinates VBO
        int lVboCoordTextureId = glGenBuffers();
        vboIdList.add(lVboCoordTextureId);
        FloatBuffer textCoordsBuffer = BufferUtils.createFloatBuffer(mTextCoords.length);
        textCoordsBuffer.put(mTextCoords).flip();
        glBindBuffer(GL_ARRAY_BUFFER, lVboCoordTextureId);
        glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

        //VBO of mNormals layout 2 in vertShader.vs
        FloatBuffer normBuffer = BufferUtils.createFloatBuffer(mNormals.length);
        normBuffer.put(mNormals).flip();
        int lVboNormId = glGenBuffers();
        vboIdList.add(lVboNormId);
        glBindBuffer(GL_ARRAY_BUFFER, lVboNormId);
        glBufferData(GL_ARRAY_BUFFER, normBuffer, GL_STATIC_DRAW);

        //We explain to OpenGL how to read our Buffers.
        glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);

        //VBO of mIndices
        IntBuffer indicesBuffer = BufferUtils.createIntBuffer(mIndices.length);
        indicesBuffer.put(mIndices).flip();
        int lVboIndicesId = glGenBuffers();
        vboIdList.add(lVboIndicesId);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, lVboIndicesId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    /**
     * Render the specified items
     *
     * @param pItems         item
     * @param pShaderProgram shaderProgram
     * @param pViewMatrix    viewMatrix
     */
    public void render(List<Item> pItems, ShaderProgram pShaderProgram, Matrix4f pViewMatrix) {

        //initRender
        initRender();

        // Bind to the VAO


        pShaderProgram.setUniform("material", mMaterial);
        for (Item lItem : pItems) {
            //can be moved to Item class
            Matrix4f modelViewMat = new Matrix4f(pViewMatrix).mul(lItem.getWorldMatrix());
            pShaderProgram.setUniform("modelViewMatrix", modelViewMat);


            // Draw the mVertices
            if (lItem.getIsBoundingBox())
                glDrawElements(GL_LINES, mIndices.length, GL_UNSIGNED_INT, 0);
            else
                glDrawElements(GL_TRIANGLES, mIndices.length, GL_UNSIGNED_INT, 0);
        }
        //end render

        endRender();
    }

    public void cleanUp() {
        //de-allocation of VAO and VBO
        glDisableVertexAttribArray(0);

        // Delete the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int vboId : vboIdList) {
            glDeleteBuffers(vboId);
        }
        Texture texture = mMaterial.getTexture();
        if (texture != null) {
            texture.cleanup();
        }
        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(mVaoId);


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

    protected void initRender() {

        Texture texture = mMaterial != null ? mMaterial.getTexture() : null;
        if (texture != null) {
            //load the texture if needed
            if (!texture.isActivated()) {
                texture.init();
            }
            // Activate first texture bank
            glActiveTexture(GL_TEXTURE0);


            // Bind the texture
            texture.bind();
        }

        // Draw the mesh
        glBindVertexArray(mVaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

    }

    protected void endRender() {
        // Restore state
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);

        glBindTexture(GL_TEXTURE_2D, 0);
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

    public void setTextCoords(float[] pTextCoord) {
        mTextCoords = pTextCoord;
    }
}
