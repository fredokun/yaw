package gameEngine;

import gameEngine.light.SceneLight;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class Renderer {
	protected ShaderProgram sh;

	protected Camera cam;

	public Renderer(Camera cam) throws Exception{
		this.cam = cam;
		
		//Initialisation of the shader program
		sh = new ShaderProgram();
		sh.createVertexShader(new String(Files.readAllBytes(Paths.get("./yaw/java/src/gameEngine/vertShader.vs"))));
		sh.createFragmentShader(new String(Files.readAllBytes(Paths.get("./yaw/java/src/gameEngine/fragShader.fs"))));
		sh.link();
		//Initialisation of the camera's uniform
		sh.createUniform("projectionMatrice");
		//Initialisation of the mesh's uniform
		sh.createUniform("modelViewMatrix");

		// Create uniform for material
        sh.createMaterialUniform("material");

		//Initialisation of the light's uniform
        sh.createUniform("camera_pos");
        sh.createUniform("specularPower");
        sh.createUniform("ambientLight");
        sh.createPointLightListUniform("pointLights",SceneLight.maxPointlight);
        sh.createSpotLightListUniform("spotLights", SceneLight.maxSpotlight);
        sh.createDirectionalLightUniform("directionalLight");
        
	}

	
	public void cleanUp(){
		//on desaloue le shaderProgramme
		sh.cleanup();
	}

	public void render(SceneVertex sc, SceneLight sl, boolean isResized){
		sh.bind();
		//Preparation of the camera
		if(isResized || SceneVertex.itemAdded){
			cam.updateCameraMat();
		}
		
//		 int err = GL11.GL_NO_ERROR;
//		if((err = GL11.glGetError()) != GL11.GL_NO_ERROR)
//		{
//			
//		  System.out.println(err);
//		}
		
		sh.setUniform("projectionMatrice", cam.getCameraMat());
		sh.setUniform("camera_pos", cam.position);
		Matrix4f mvm = cam.setupViewMatrix();

		//Rendering of the light
		sl.render(sh,mvm);
		
		//Init Objects
		sc.initMesh();
		
		//Update objects
		sc.update();
		
		
		//Rendering of the object
		sc.draw(sh,mvm);

		sh.unbind();
	}
}
