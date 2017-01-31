package embla3d.engine.meshs;

/**
 * Mesh representing a Tetrahedron : default weight 4
 * Useless for the moment will be removed during the upcoming refactoring
 */
public class TetrahedronMesh extends Mesh {
    public TetrahedronMesh(float[] vertices, Material material, float[] normals, int[] indices) {
        super(vertices, material, normals, indices, 4);
    }

    public TetrahedronMesh(float[] vertices, float cx, float cy, float cz, float reflectance, float[] normals, int[] indices) {
        super(vertices, cx, cy, cz, reflectance, normals, indices, 4);
    }

}
