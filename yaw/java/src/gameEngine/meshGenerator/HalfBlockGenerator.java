package gameEngine.meshGenerator;

import gameEngine.meshs.HalphBlockMesh;
import gameEngine.meshs.Material;
import gameEngine.meshs.Mesh;

public class HalfBlockGenerator {
	public static Mesh generate(float xLength, float yLength,float zLength, Material m){
		float x = xLength/2f;
		float y = yLength/2f;
		float z = zLength/2f;
		float[] vertices = new float[]{
				//Front face
				-x, -y, z,
				x, -y, z,
				x, y, -z,
				-x, y, -z,
				//Back face
				x,  y, -z,
				-x,  y, -z,
				-x, -y, -z,
				x, -y, -z,
				//Bottom face
				x, -y, z,
				-x, -y,z,
				x, -y, -z,
				-x, -y, -z,
				//Left face
				-x,  y, -z,
				-x, -y, z,
				-x, -y, -z,
				//Right face
				x,  y, -z,
				x, -y, z,
				x, -y, -z
		};
		
	float frontNormal = (float) Math.sqrt(yLength * yLength + zLength * zLength);
	if(frontNormal == 0)
		frontNormal = 0.00000000000001f;
	float yNormal = z/frontNormal;
	float zNormal = y/frontNormal;
	
	float[] normales = {
					//Front face
					0,yNormal,zNormal,
					0,yNormal,zNormal,
					0,yNormal,zNormal,
					0,yNormal,zNormal,
					//Back face
					0,0,-1f,
					0,0,-1f,
					0,0,-1f,
					0,0,-1f,
					//Bottom face
					0,-1f,0,
					0,-1f,0,
					0,-1f,0,
					0,-1f,0,
					//Left face
					-1,0,0,
					-1,0,0,
					-1,0,0,
					//Right face
					1,0,0,
					1,0,0,
					1,0,0
			};
	
			int[] indices = new int[]{
					//Front face
					0,1,2,0,2,3,
					//Back face
					4,7,6,4,6,5,
					//Bottom Face
					10,8,9,10,9,11,
					//Left face
					12,14,13,
					//Right face
					15,16,17
			};
			
			return new HalphBlockMesh(vertices, m,normales, indices, xLength,yLength,zLength);
	}
	
	public static Mesh generate(float xLength, float zLength,int angle, Material m){
		angle = angle%90;
		if( angle == 0)
			angle = 1;
		angle = Math.abs(angle);
		float yLength = (float) (Math.tan(angle)*zLength);
		return generate(xLength,yLength,zLength, m);
	}
}
