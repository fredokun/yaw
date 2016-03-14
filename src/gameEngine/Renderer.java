package gameEngine;

import gameEngine.light.SceneLight;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.joml.Matrix4f;

public class Renderer {
	protected ShaderProgram sh;

	protected Camera cam;

	public Renderer(Camera cam) throws Exception{
		this.cam = cam;
		
		//Initialisation of the shader program
		sh = new ShaderProgram();
		sh.createVertexShader(new String(Files.readAllBytes(Paths.get("src/gameEngine/vertShader.vs"))));
		sh.createFragmentShader(new String(Files.readAllBytes(Paths.get("src/gameEngine/fragShader.fs"))));
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
		if(isResized){
			cam.updateCameraMat();
		}
		sh.setUniform("projectionMatrice", cam.getCameraMat());
		sh.setUniform("camera_pos", cam.position);
		Matrix4f mvm = cam.setupViewMatrix();

		//Rendering of the light
		sl.render(sh,mvm);
		
		//Rendering of the object
		sc.draw(sh,mvm);

		sh.unbind();
	}
}
