package embla3d.engine.meshs;

public class HalfBlockMesh extends Mesh {
    public final float xLength;
    public final float yLength;
    public final float zLength;
	
    public HalfBlockMesh(float[] vertices, Material material,float[] normals, int[] indices,float xLength,float yLength,float zLength) {
	super(vertices, material, normals, indices, 6);
	this.xLength = xLength;
	this.yLength = yLength;
	this.zLength = zLength;
    }
	
    public HalfBlockMesh(float[] vertices, float cx, float cy, float cz, float reflectance,float[] normals, int[] indices,float xLength,float yLength,float zLength) {
	super( vertices,  cx,  cy,  cz,  reflectance, normals,  indices,6);
	this.xLength = xLength;
	this.yLength = yLength;
	this.zLength = zLength;
    }
	
}
