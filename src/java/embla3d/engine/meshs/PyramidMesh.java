package embla3d.engine.meshs;

/**
 * Mesh representing a Pyramide : default weight 5
 * the additional attributes are not used
 * Useless for the moment will be removed during the upcoming refactoring
 */
public class PyramidMesh extends Mesh {
    public final float xLength;
    public final float yLength;
    public final float zLength;

    public PyramidMesh(float[] vertices, Material material, float[] normals, int[] indices, float xLength, float yLength, float zLength) {
        super(vertices, material, normals, indices, 5);
        this.xLength = xLength;
        this.yLength = yLength;
        this.zLength = zLength;
    }

    public PyramidMesh(float[] vertices, float cx, float cy, float cz, float reflectance, float[] normals, int[] indices, float xLength, float yLength, float zLength) {
        super(vertices, cx, cy, cz, reflectance, normals, indices, 5);
        this.xLength = xLength;
        this.yLength = yLength;
        this.zLength = zLength;
    }
}
