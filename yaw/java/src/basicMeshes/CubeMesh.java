package basicMeshes;

import org.joml.Vector3f;

import gameEngine.Material;
import gameEngine.Mesh;

public class CubeMesh extends Mesh{
	static Material material = new Material( new Vector3f(0.4f,0,0.7f),0.f);
	 static float[] vertices = {
					    // VO
					    -0.5f,  0.5f,  0.5f,
					    // V1
					    -0.5f, -0.5f,  0.5f,
					    // V2
					    0.5f, -0.5f,  0.5f,
					    // V3
					     0.5f,  0.5f,  0.5f,
					    // V4
					    -0.5f,  0.5f, -0.5f,
					    // V5
					     0.5f,  0.5f, -0.5f,
					    // V6
					    -0.5f, -0.5f, -0.5f,
					    // V7
					     0.5f, -0.5f, -0.5f,
					};
		
		
	 static int[] indices = new int[] {
			    // Front face
			    0, 1, 3, 3, 1, 2,
			    // Top Face
			    4, 0, 3, 5, 4, 3,
			    // Right face
			    3, 2, 7, 5, 3, 7,
			    // Left face
			    0, 1, 6, 4, 0, 6,
			    // Bottom face
			    6, 1, 2, 7, 6, 2,
			    // Back face
			    4, 6, 7, 5, 4, 7,
			};

		
		 static float[] colors = new float[]{
				    0.5f, 0.0f, 0.0f,
				    0.0f, 0.5f, 0.0f,
				    0.0f, 0.0f, 0.5f,
				    0.0f, 0.5f, 0.5f,
				    0.5f, 0.0f, 0.0f,
				    0.0f, 0.5f, 0.0f,
				    0.0f, 0.0f, 0.5f,
				    0.0f, 0.5f, 0.5f,
				};
		
	public CubeMesh() {
		super(vertices, material, colors, indices);
	}


}
