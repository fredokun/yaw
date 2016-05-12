package gameEngine.meshs;

public class TetraedreMesh extends Mesh {
	public TetraedreMesh(float[] vertices, Material material,float[] normales, int[] indices) {
		super(vertices, material, normales, indices, 4);
	}
	
	public TetraedreMesh(float[] vertices, float cx, float cy, float cz, float reflectance,float[] normales, int[] indices) {
		super( vertices,  cx,  cy,  cz,  reflectance, normales,  indices,4);
	}
	
}
