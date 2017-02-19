package embla3d.engine;

import embla3d.engine.camera.Camera;
import embla3d.engine.light.SceneLight;
import embla3d.engine.skybox.Skybox;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.*;

/**
 * This class allows to manage the rendering logic of our game.
 * The shader is used to configure a part of the rendering process performed by a graphics card.
 * The shader allows to describe the absorption, the diffusion of the light, the texture to be used, the reflections of the objects, the shading, etc ...
 */
public class Renderer {
    protected ShaderProgram sh;


    /**
     * Basic rendering.
     *
     * @throws Exception Exception
     */
    public Renderer() throws Exception {

        /* Initialization of the shader program. */
        sh = new ShaderProgram();
        sh.createVertexShader(vertShader.SHADER_STRING);
        sh.createFragmentShader(fragShader.SHADER_STRING);


       /* Binds the code and checks that everything has been done correctly. */
        sh.link();
        /* Initialization of the camera's uniform. */
        sh.createUniform("projectionMatrix");
        /* Initialization of the mesh's uniform. */
        sh.createUniform("modelViewMatrix");

        /* Create uniform for material. */
        sh.createMaterialUniform("material");

        /* Initialization of the light's uniform. */
        sh.createUniform("camera_pos");
        sh.createUniform("specularPower");
        sh.createUniform("ambientLight");
        sh.createPointLightListUniform("pointLights", SceneLight.MAX_POINTLIGHT);
        sh.createSpotLightUniformList("spotLights", SceneLight.MAX_SPOTLIGHT);
        sh.createDirectionalLightUniform("directionalLight");

    }

    /**
     * The Shader Program is deallocated
     */
    public void cleanUp() {
        sh.cleanup();
    }

    /**
     * Specific rendering.
     * Configuring rendering with the absorption, the diffusion of the light, the texture to be used, the reflections of the objects, the shading,
     * Which are passed by arguments
     *
     * @param sc        sceneVertex
     * @param sl        sceneLight
     * @param isResized isResized
     * @param cam       camera
     * @param sk        skybox
     */
    public void render(SceneVertex sc, SceneLight sl, boolean isResized, Camera cam, Skybox sk) {
        sh.bind();
        //Preparation of the camera
        if (isResized || SceneVertex.itemAdded) {
            cam.updateCameraMat();
        }

        //Debug
        //		 int err = GL11.GL_NO_ERROR;
        //		if((err = GL11.glGetError()) != GL11.GL_NO_ERROR)
        //		{
        //
        //		  System.out.println(err);
        //		}

        /* Set the camera to render. */
        sh.setUniform("projectionMatrix", cam.getCameraMat());
        sh.setUniform("camera_pos", cam.position);
        Matrix4f viewMat = cam.setupViewMatrix();

        /* Enable the option needed to render.*/
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);

        glDepthMask(true);        /* Enable or disable writing to the depth buffer. */
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_GREATER);       /* Specify the value used for depth buffer comparisons. */
        glClearDepth(cam.getzFar() * -1); /* GlClearDepth specifies the depth value used by glClear to clear the depth buffer.
                                                   The values ​​specified by glClearDepth are set to range [0.1].*/
        glDisable(GL_BLEND);

        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);

        /* Rendering of the light. */
        sl.render(sh, viewMat);

       /* Init Objects. */
        sc.initMesh();

        /* Update objects
        XXX useless?  sc.update(); */


        /* Rendering of the object. */
        sc.draw(sh, viewMat);
        /* Cleans all services. */
        sh.unbind();
        if (sk != null) {
            if (sk.init == false) {
                SceneVertex.itemAdded = true;
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