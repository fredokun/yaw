package gameEngine.meshGenerator;

import gameEngine.meshs.Material;
import gameEngine.meshs.Mesh;
import gameEngine.meshs.TetraedreMesh;

public class TetraedreRegGenerator {
	public static Mesh generate(Material m){
		float[] vertices = new float[]{
				//First Face
				-0.5f,0.5f,0.5f,
				0.5f,0.5f,-0.5f,
				0.5f,-0.5f,0.5f,
				//Snd Face
				-0.5f,0.5f,0.5f,
				0.5f,0.5f,-0.5f,
				-0.5f,-0.5f,-0.5f,
				//Third Face
				-0.5f,0.5f,0.5f,
				-0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,0.5f,
				//Last Face
				0.5f,0.5f,-0.5f,
				0.5f,-0.5f,0.5f,
				-0.5f,-0.5f,-0.5f
		};

		float pos = (float)(1/ Math.sqrt(3));
		float neg = (float)(-1/ Math.sqrt(3));
		
		float[] normales = new float[]{
				//First Face
				pos,pos,pos,
				pos,pos,pos,
				pos,pos,pos,
				//Snd Face
				neg,pos,neg,
				neg,pos,neg,
				neg,pos,neg,
				//Third Face
				neg,neg,pos,
				neg,neg,pos,
				neg,neg,pos,
				//Last Face
				pos,neg,neg,
				pos,neg,neg,
				pos,neg,neg
		};

		int[] indices = new int[]{
				//First Face
				0,2,1,
				//Snd Face
				3,4,5,
				//Third Face
				6,7,8,
				//Last Face
				9,10,11
		};
		return new TetraedreMesh(vertices, m, normales, indices);
	}
}
