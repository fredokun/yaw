package embla3d.engine;

import static org.lwjgl.opengl.GL11.*;

import embla3d.engine.fragShader;

import embla3d.engine.camera.Camera;
import embla3d.engine.light.SceneLight;
import embla3d.engine.skybox.Skybox;

import embla3d.engine.vertShader;
import embla3d.engine.fragShader;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.joml.Matrix4f;

public class Renderer {
    protected ShaderProgram sh;

    public Renderer() throws Exception{

	//Initialization of the shader program
	sh = new ShaderProgram();
	sh.createVertexShader(vertShader.SHADER_STRING);
	sh.createFragmentShader(fragShader.SHADER_STRING);

	sh.link();
	//Initialization of the camera's uniform
	sh.createUniform("projectionMatrix");
	//Initialization of the mesh's uniform
	sh.createUniform("modelViewMatrix");

	// Create uniform for material
	sh.createMaterialUniform("material");

	//Initialization of the light's uniform
	sh.createUniform("camera_pos");
	sh.createUniform("specularPower");
	sh.createUniform("ambientLight");
	sh.createPointLightListUniform("pointLights",SceneLight.maxPointlight);
	sh.createSpotLightUniformList("spotLights", SceneLight.maxSpotlight);
	sh.createDirectionalLightUniform("directionalLight");

    }


    public void cleanUp(){
	// The Shader Program is deallocated
	sh.cleanup();
    }

    public void render(SceneVertex sc, SceneLight sl, boolean isResized,Camera cam, Skybox sk){
	sh.bind();
	//Preparation of the camera
	if(isResized || SceneVertex.itemAdded){
	    cam.updateCameraMat();
	}

	//Debug
	//		 int err = GL11.GL_NO_ERROR;
	//		if((err = GL11.glGetError()) != GL11.GL_NO_ERROR)
	//		{
	//			
	//		  System.out.println(err);
	//		}

	//Set the camera to render.
	sh.setUniform("projectionMatrix", cam.getCameraMat());
	sh.setUniform("camera_pos", cam.position);
	Matrix4f viewMat = cam.setupViewMatrix();

	//Enable the option needed to render.
	glEnable(GL_CULL_FACE);
	glCullFace(GL_BACK);

	glDepthMask(true);
	glEnable(GL_DEPTH_TEST);
	glDepthFunc(GL_GREATER);
	glClearDepth(cam.getzFar()*-1);
	glDisable(GL_BLEND);

	glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);

	//Rendering of the light
	sl.render(sh,viewMat);

	//Init Objects
	sc.initMesh();

	//Update objects
	sc.update();


	//Rendering of the object
	sc.draw(sh,viewMat);

	sh.unbind();
	if(sk!= null){
	    if(sk.init==false){
		SceneVertex.itemAdded=true;
		try {
		    sk.init();
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	    sk.draw(cam);
	}
    }
}
