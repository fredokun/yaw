package gameEngine.meshGenerator;

import org.joml.Vector3f;

import gameEngine.meshs.GroundMesh;
import gameEngine.meshs.Material;
import gameEngine.meshs.Mesh;

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
		
		float[] normales = new float[]{
			0,1,0,
			0,1,0,
			0,1,0,
			0,1,0
		};
		
		int[] indices = new int[]{
			0,1,2,
			1,3,2
		};
		
		return new GroundMesh(vertices,normales,indices,m,width,length,height );
	}
	
	public static Mesh generate(float width, float length,float height, float cx, float cy, float cz, float r){
		return generate(width, length, height, new Material(new Vector3f(cx, cy, cz), r));
	}
}
