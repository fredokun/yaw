package embla3d.engine.meshs;

/**
 * Mesh representing the Ground : default weight 0
 * the additional attributes are not used
 * Useless for the moment will be removed during the upcoming refactoring
 */
public class GroundMesh extends Mesh {
    public final float width, length, height;

    public GroundMesh(float[] vertices, float[] normals, int[] indices, Material m, float width, float length, float height) {
        super(vertices, m, normals, indices);
        this.width = width;
        this.length = length;
        this.height = height;
    }
}
