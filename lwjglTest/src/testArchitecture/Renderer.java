package testArchitecture;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Renderer {
	protected ShaderProgram sh;
	
	/*------ Hesitation sur l'architecture------*/
	//protected Scene sc;

	//angle du champs de vision
	protected float fieldOfView=(float) Math.toRadians(60.0f);;
	private  float zNear = 0.01f;
	private  float zFar = 1000.f;
	private Matrix4f projectionM;


	public Renderer() throws Exception{
		//on prépare le shader programme
		sh = new ShaderProgram();
		sh.createVertexShader(new String(Files.readAllBytes(Paths.get("src\\testTexturedCube\\vertShader.vs"))));
		sh.createFragmentShader(new String(Files.readAllBytes(Paths.get("src\\testTexturedCube\\fragShader.fs"))));
		sh.link();
		sh.createUniform("projectionMatrice");
		sh.createUniform("worldMatrice");
		//sc = new Scene();

		projectionM = new Matrix4f().perspective(fieldOfView, (float) Window.aspectRatio(), zNear, zFar);
	}

//	public Renderer(Scene sc) throws Exception{
//		this();
//		this.sc = sc;
//	}	
	public void cleanUp(){
		//on desaloue le shaderProgramme
		sh.cleanup();
		//On desaloue le materielle de la scene
		//sc.cleanUp();
	}

	public void render(Scene sc, boolean isResized){
		sh.bind();
		if(isResized){
			System.out.println("modifier");
			projectionM = new Matrix4f().perspective(fieldOfView, (float) Window.aspectRatio(), zNear, zFar);
			
		}
		sh.setUniform("projectionMatrice", projectionM);
		sc.draw(sh);

		sh.unbind();
	}
/*
	public void addItem(MyItem item){
		sc.add(item);
	}

	public void addItem(float[] vertices, float[] couleur, int[] indices, 
			float scale, Vector3f rotation,	Vector3f position){
		sc.add(vertices,couleur, indices,scale, rotation, position);
	}

	public void addItem(Mesh apparence,	float scale, Vector3f rotation,	Vector3f position){
		sc.add( apparence, scale, rotation, position);
	}*/
}
