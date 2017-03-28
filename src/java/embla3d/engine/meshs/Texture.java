package embla3d.engine.meshs;

import de.matthiasmann.twl.utils.PNGDecoder;

import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;


public class Texture {

    private final int mId;

    private final int mWidth;

    private final int mHeight;

    private int mNumRows = 1;

    private int mNumCols = 1;

    /**
     * Creates an empty texture.
     *
     * @param pWidth       Width of the texture
     * @param pHeight      Height of the texture
     * @param pPixelFormat Specifies the format of the pixel data (GL_RGBA, etc.)
     * @throws Exception
     */
    public Texture(int pWidth, int pHeight, int pPixelFormat) throws Exception {
        this.mId = glGenTextures();
        this.mWidth = pWidth;
        this.mHeight = pHeight;
        glBindTexture(GL_TEXTURE_2D, this.mId);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, this.mWidth, this.mHeight, 0, pPixelFormat, GL_FLOAT, (ByteBuffer) null);
        //This parameter basically says that when a pixel is drawn with no direct one to one association to a texture coordinate it will pick the nearest texture coordinate point.
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        //We set the texture wrapping mode to GL_CLAMP_TO_EDGE since we do not want the texture to repeat in case we exceed the [0,1]range.
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
    }

    public Texture(String pFileName, int pNumCols, int pNumRows) throws Exception {
        this(pFileName);
        this.mNumCols = pNumCols;
        this.mNumRows = pNumRows;
    }

    /**
     * Creates a texture from a specified file
     * Only support png format
     *
     * @param pFileName file name
     * @throws Exception
     */
    public Texture(String pFileName) throws Exception {
        this(Texture.class.getResourceAsStream(pFileName));
    }

    /**
     * Creates a texture from an inputstream
     *
     * @param pInputStream inpustream
     * @throws Exception
     */
    public Texture(InputStream pInputStream) throws Exception {
        try {
            // Load Texture file
            PNGDecoder lDecoder = new PNGDecoder(pInputStream);

            this.mWidth = lDecoder.getWidth();
            this.mHeight = lDecoder.getHeight();

            // Load texture contents into a byte buffer
            ByteBuffer lByteBuffer = ByteBuffer.allocateDirect(
                    4 * lDecoder.getWidth() * lDecoder.getHeight());
            lDecoder.decode(lByteBuffer, lDecoder.getWidth() * 4, PNGDecoder.Format.RGBA);
            lByteBuffer.flip();

            // Create a new OpenGL texture
            this.mId = glGenTextures();
            // Bind the texture
            glBindTexture(GL_TEXTURE_2D, this.mId);

            // Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            // Upload the texture data
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.mWidth, this.mHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, lByteBuffer);
            // Generate Mip Map: A mipmap is a decreasing resolution set of images generated from a high detailed texture.
            glGenerateMipmap(GL_TEXTURE_2D);

            pInputStream.close();
        } finally {
            if (pInputStream != null) {
                pInputStream.close();
            }
        }
    }

    public int getNumCols() {
        return mNumCols;
    }

    public int getNumRows() {
        return mNumRows;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, mId);
    }

    public int getId() {
        return mId;
    }

    public void cleanup() {
        glDeleteTextures(mId);
    }
}
