package embla3d.engine.meshs;

public class TetrahedronMesh extends Mesh {
    public TetrahedronMesh(float[] vertices, Material material,float[] normals, int[] indices) {
	super(vertices, material, normals, indices, 4);
    }
	
    public TetrahedronMesh(float[] vertices, float cx, float cy, float cz, float reflectance,float[] normals, int[] indices) {
	super( vertices,  cx,  cy,  cz,  reflectance, normals,  indices,4);
    }
	
}
