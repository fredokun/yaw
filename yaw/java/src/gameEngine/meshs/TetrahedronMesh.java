package gameEngine.meshs;

public class TetrahedronMesh extends Mesh {
	public TetrahedronMesh(float[] vertices, Material material,float[] normales, int[] indices) {
		super(vertices, material, normales, indices, 4);
	}
	
	public TetrahedronMesh(float[] vertices, float cx, float cy, float cz, float reflectance,float[] normales, int[] indices) {
		super( vertices,  cx,  cy,  cz,  reflectance, normales,  indices,4);
	}
	
}
