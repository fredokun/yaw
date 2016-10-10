package embla3d.engine.meshGenerator;

import org.joml.Vector3f;

import embla3d.engine.meshs.GroundMesh;
import embla3d.engine.meshs.Material;
import embla3d.engine.meshs.Mesh;

public class GroundGenerator {
    public static Mesh generate(float width, float length,float height, Material m){
	float halfW = width / 2;
	float halfL = length / 2;
		
	float[] vertices = new float[]{
	    -halfW, height, -halfL,
	    -halfW, height, halfL,
	    halfW, height, -halfL,
	    halfW, height, halfL	
	};
		
	float[] normals = new float[]{
	    0,1,0,
	    0,1,0,
	    0,1,0,
	    0,1,0
	};
		
	int[] indices = new int[]{
	    0,1,2,
	    1,3,2
	};
		
	return new GroundMesh(vertices,normals,indices,m,width,length,height );
    }
	
    public static Mesh generate(float width, float length,float height, float cx, float cy, float cz, float r){
	return generate(width, length, height, new Material(new Vector3f(cx, cy, cz), r));
    }
}
