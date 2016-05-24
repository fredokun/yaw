package gameEngine.meshs;

public class PyramidMesh extends Mesh {
	public final float xLength;
	public final float yLength;
	public final float zLength;
	
	public PyramidMesh(float[] vertices, Material material,float[] normales, int[] indices,float xLength,float yLength,float zLength) {
		super(vertices, material, normales, indices, 5);
		this.xLength = xLength;
		this.yLength = yLength;
		this.zLength = zLength;
	}
	
	public PyramidMesh(float[] vertices, float cx, float cy, float cz, float reflectance,float[] normales, int[] indices,float xLength,float yLength,float zLength) {
		super( vertices,  cx,  cy,  cz,  reflectance, normales,  indices,5);
		this.xLength = xLength;
		this.yLength = yLength;
		this.zLength = zLength;
	}
}
