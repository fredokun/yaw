package embla3d.engine.meshs;

import de.matthiasmann.twl.utils.PNGDecoder;

import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;


public class Texture {

    private final int id;

    private final int width;

    private final int height;

    private int numRows = 1;

    private int numCols = 1;

    /**
     * Creates an empty texture.
     *
     * @param width       Width of the texture
     * @param height      Height of the texture
     * @param pixelFormat Specifies the format of the pixel data (GL_RGBA, etc.)
     * @throws Exception
     */
    public Texture(int width, int height, int pixelFormat) throws Exception {
        this.id = glGenTextures();
        this.width = width;
        this.height = height;
        glBindTexture(GL_TEXTURE_2D, this.id);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, this.width, this.height, 0, pixelFormat, GL_FLOAT, (ByteBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
    }

    public Texture(String fileName, int numCols, int numRows) throws Exception {
        this(fileName);
        this.numCols = numCols;
        this.numRows = numRows;
    }

    /**
     * Creates a texture from a specified file
     * Only support png format
     *
     * @param fileName file name
     * @throws Exception
     */
    public Texture(String fileName) throws Exception {
        this(Texture.class.getResourceAsStream(fileName));
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
            PNGDecoder decoder = new PNGDecoder(pInputStream);

            this.width = decoder.getWidth();
            this.height = decoder.getHeight();

            // Load texture contents into a byte buffer
            ByteBuffer buf = ByteBuffer.allocateDirect(
                    4 * decoder.getWidth() * decoder.getHeight());
            decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
            buf.flip();

            // Create a new OpenGL texture
            this.id = glGenTextures();
            // Bind the texture
            glBindTexture(GL_TEXTURE_2D, this.id);

            // Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            // Upload the texture data
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
            // Generate Mip Map
            glGenerateMipmap(GL_TEXTURE_2D);

            pInputStream.close();
        } finally {
            if (pInputStream != null) {
                pInputStream.close();
            }
        }
    }

    public int getNumCols() {
        return numCols;
    }

    public int getNumRows() {
        return numRows;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public int getId() {
        return id;
    }

    public void cleanup() {
        glDeleteTextures(id);
    }
}
