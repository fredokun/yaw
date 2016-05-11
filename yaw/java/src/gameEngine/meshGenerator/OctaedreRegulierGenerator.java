package gameEngine.meshGenerator;

import gameEngine.Material;
import gameEngine.Mesh;

public class OctaedreRegulierGenerator {
	public static Mesh generate(Material m){
		float[] vertices= new float[]{
				//LTF
				-0.5f, 0f, 0f,
				0f, 0.5f, 0f,
				0f, 0f, 0.5f,
				//RTF
				0.5f, 0f, 0f,
				0f, 0.5f, 0f,
				0f, 0f, 0.5f,
				//LBF
				-0.5f, 0f, 0f,
				0f, -0.5f, 0f,
				0f, 0f, 0.5f,
				//RBF
				0.5f, 0f, 0f,
				0f, -0.5f, 0f,
				0f, 0f, 0.5f,
				//LTB
				-0.5f, 0f, 0f,
				0f, 0.5f, 0f,
				0f, 0f, -0.5f,
				//RTB
				0.5f, 0f, 0f,
				0f, 0.5f, 0f,
				0f, 0f, -0.5f,
				//LBB
				-0.5f, 0f, 0f,
				0f, -0.5f, 0f,
				0f, 0f, -0.5f,
				//RBB
				0.5f,0f,0f,
				0f,-0.5f,0f,
				0f,0f,-0.5f
		};

		float pos = (float)(1/ Math.sqrt(3));
		float neg = (float)(-1/ Math.sqrt(3));
		for(int i=0; i< vertices.length; i++)
		System.out.println(vertices[i]+ " ");
		
		float[] normales= new float[]{
			//LTF
			neg,pos,pos,
			neg,pos,pos,
			neg,pos,pos,
			//RTF
			pos,pos,pos,
			pos,pos,pos,
			pos,pos,pos,
			//LBF
			neg,neg,pos,
			neg,neg,pos,
			neg,neg,pos,
			//RBF
			pos,neg,pos,
			pos,neg,pos,
			pos,neg,pos,
			//LTB
			neg,pos,neg,
			neg,pos,neg,
			neg,pos,neg,
			//RTB
			pos,pos,neg,
			pos,pos,neg,
			pos,pos,neg,
			//LBB
			neg,neg,neg,
			neg,neg,neg,
			neg,neg,neg,
			//RBB
			pos,neg,neg,
			pos,neg,neg,
			pos,neg,neg
		};
		
		int[] indices=new int[]{
				//LTF
				0,2,1,
				//RTF
				3,4,5,
				//LBF
				6,7,8,
				//RBF
				9,11,10,
				//LTB
				12,13,14,
				//RTB
				15,17,16,
				//LBB
				18,20,19,
				//RBB
				21,22,23
			};
		
		return new Mesh(vertices, m, normales, indices,6);
	}
}
