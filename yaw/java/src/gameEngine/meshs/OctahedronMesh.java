package gameEngine.meshs;

public class OctahedronMesh extends Mesh {
	public OctahedronMesh(float[] vertices, Material material,float[] normales, int[] indices) {
		super(vertices, material, normales, indices, 6);
	}
	
	public OctahedronMesh(float[] vertices, float cx, float cy, float cz, float reflectance,float[] normales, int[] indices) {
		super( vertices,  cx,  cy,  cz,  reflectance, normales,  indices,6);
	}
}
