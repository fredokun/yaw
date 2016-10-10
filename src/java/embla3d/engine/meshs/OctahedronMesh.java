package gameEngine.meshs;

public class OctahedronMesh extends Mesh {
	public OctahedronMesh(float[] vertices, Material material,float[] normals, int[] indices) {
		super(vertices, material, normals, indices, 6);
	}
	
	public OctahedronMesh(float[] vertices, float cx, float cy, float cz, float reflectance,float[] normals, int[] indices) {
		super( vertices,  cx,  cy,  cz,  reflectance, normals,  indices,6);
	}
}
