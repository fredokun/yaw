package embla3d.engine.meshs;

import de.matthiasmann.twl.utils.PNGDecoder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;


public class Texture {

    private final String mFileName;
    private int mId;
    private int mWidth;
    private int mHeight;
    private int mNumRows = 1;
    private int mNumCols = 1;


    public Texture(String pFileName, int pNumCols, int pNumRows) {
        this(pFileName);
        this.mNumCols = pNumCols;
        this.mNumRows = pNumRows;
    }

    /**
     * Creates a texture from a specified file
     * Only support png format
     * Texture will not be load at the present moment but when it is needed
     *
     * @param pFileName file name
     */
    public Texture(String pFileName) {
        mId = -1;
        mFileName = pFileName;
    }


    /**
     * Load the texture from the disk and transfer it to the graphic card
     */
    public void init() {
        if (mId < 0) {
            InputStream lInputStream = null;
            try {
                lInputStream = Texture.class.getResourceAsStream(mFileName);
                // Load Texture file
                PNGDecoder mDecoder = new PNGDecoder(lInputStream);

                this.mWidth = mDecoder.getWidth();
                this.mHeight = mDecoder.getHeight();

                // Load texture contents into a byte buffer
                ByteBuffer lByteBuffer = ByteBuffer.allocateDirect(
                        4 * mDecoder.getWidth() * mDecoder.getHeight());
                mDecoder.decode(lByteBuffer, mDecoder.getWidth() * 4, PNGDecoder.Format.RGBA);
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
                lInputStream.close();
            } catch (IOException pE) {

                pE.printStackTrace();
            } finally {
                if (lInputStream != null) {
                    try {
                        lInputStream.close();
                    } catch (IOException pE) {
                        pE.printStackTrace();
                    }
                }
            }
        }
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, mId);
    }

    public void cleanup() {
        glDeleteTextures(mId);
    }

    public boolean isActivated() {
        return mId >= 0;
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

    public int getId() {
        return mId;
    }
}
