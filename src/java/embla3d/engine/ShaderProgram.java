package embla3d.engine;

import embla3d.engine.light.AmbientLight;
import embla3d.engine.light.DirectionalLight;
import embla3d.engine.light.PointLight;
import embla3d.engine.light.SpotLight;
import embla3d.engine.meshs.Material;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GLUtil;

import java.nio.FloatBuffer;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;


public class ShaderProgram {

    private final int mProgramId;
    private final HashMap<String, Integer> mUniformsList = new HashMap<>();
    private int mVertexShaderId;
    private int mFragmentShaderId;


    /**
     * Constructor throws exception if the program could not create the shader
     *
     * @throws Exception the exception
     */
    public ShaderProgram() throws Exception {
        mProgramId = glCreateProgram();
        if (!glIsProgram(mProgramId)) {
            throw new Exception("Could not create Shader");
        }
    }

    /**
     * Create a vertex type shader
     *
     * @param shaderCode source code for the shader
     * @throws Exception the exception
     */
    public void createVertexShader(String shaderCode) throws Exception {
        mVertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
    }

    /**
     * Create a shader attach the specified source to it compile it and attach the shader to the main program
     *
     * @param shaderCode source code for the shader
     * @param shaderType the type of shader to be created. One of:
     *                   VERTEX_SHADER	FRAGMENT_SHADER	GEOMETRY_SHADER	TESS_CONTROL_SHADER
     *                   TESS_EVALUATION_SHADER
     * @return the id of the shader
     * @throws Exception the exception o/
     */
    private int createShader(String shaderCode, int shaderType) throws Exception {
        /*the shader object whose source code is to be replaced*/
        int shaderId = glCreateShader(shaderType);
        System.out.println(glGetShaderInfoLog(shaderId));
        /*can't use glIsShader here because it alwayse return false and we don't know why */
        if (shaderId == GL_FALSE) {
            throw new Exception("Error creating shader. Code: " + shaderId);
        }
        /*Sets the source code in shader to the source code in the array of strings specified by strings.*/
        glShaderSource(shaderId, shaderCode);
        /*Compiles a shader object.*/
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }

        /*void function */
        glAttachShader(mProgramId, shaderId);

        return shaderId;
    }

    /**
     * Create a fragment shader
     *
     * @param shaderCode source code for the shader
     * @throws Exception the exception \o
     */
    public void createFragmentShader(String shaderCode) throws Exception {
        mFragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    /**
     * Links the program object.
     *
     * @throws Exception the exception \o/
     */
    public void link() throws Exception {
        glLinkProgram(mProgramId);
        if (glGetProgrami(mProgramId, GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking Shader code: " + glGetShaderInfoLog(mProgramId, 1024));
        }

        glValidateProgram(mProgramId);
        if (glGetProgrami(mProgramId, GL_VALIDATE_STATUS) == 0) {
            throw new Exception("Error validating Shader code: " + glGetShaderInfoLog(mProgramId, 1024));
        }

    }

    /**
     * Installs the program object as part of current rendering state.
     */
    public void bind() {
        GLUtil.setupDebugMessageCallback();
        /*Specifies the handle of the program object whose executables are to be used as part of current rendering state.*/
        glUseProgram(mProgramId);

    }

    // Deallocate Shader Program

    /**
     *
     */
    public void cleanup() {
        unbind();
        if (mProgramId != 0) {
            if (mVertexShaderId != 0) {
             /*   Detaches a shader object from a program object to which it is attached*/
                glDetachShader(mProgramId, mVertexShaderId);
            }
            if (mFragmentShaderId != 0) {
                /*Detaches a shader object from a program object to which it is attached*/
                glDetachShader(mProgramId, mFragmentShaderId);
            }
           /*Deletes a program object*/
            glDeleteProgram(mProgramId);
        }
    }

    /**
     * unbind
     */
    public void unbind() {
        glUseProgram(0);
    }

    /**
     * Create  a list of n point light uniform
     *
     * @param uniformName uniform name
     * @param size        size
     * @throws Exception the exception
     */
    public void createPointLightListUniform(String uniformName, int size) throws Exception {
        for (int i = 0; i < size; i++) {
            createPointLightUniform(uniformName + "[" + i + "]");
        }
    }

    /**
     * Create uniform for each attribute of the point light
     *
     * @param uniformName uniform name
     * @throws Exception the exception
     */
    private void createPointLightUniform(String uniformName) throws Exception {
        createUniform(uniformName + ".colour");
        createUniform(uniformName + ".position");
        createUniform(uniformName + ".intensity");
        createUniform(uniformName + ".att_constant");
        createUniform(uniformName + ".att_linear");
        createUniform(uniformName + ".att_exponent");
    }

    /**
     * Returns the location of a uniform variable
     *
     * @param uniformName Points to a null terminated string containing the name of the uniform variable whose location is to be queried.
     * @return the location
     * @throws Exception the exception
     */
    public int createUniform(String uniformName) throws Exception {
        int res = glGetUniformLocation(mProgramId, uniformName);
        if (res < 0) {

            throw new Exception("Uniform creation error: " + uniformName);
        }
        mUniformsList.put(uniformName, res);
        return res;
    }

    /**
     * Create a list of n spotlight uniform
     *
     * @param uniformName uniform name
     * @param size        the size
     * @throws Exception the exception
     */
    public void createSpotLightUniformList(String uniformName, int size) throws Exception {
        for (int i = 0; i < size; i++) {
            createSpotLightUniform(uniformName + "[" + i + "]");
        }
    }

    /**
     * Create uniform for each attribute of the spot light
     *
     * @param uniformName uniform name
     * @throws Exception the exception
     */
    private void createSpotLightUniform(String uniformName) throws Exception {
        createPointLightUniform(uniformName + ".pl");
        createUniform(uniformName + ".conedir");
        createUniform(uniformName + ".cutoff");
    }

    /**
     * Create uniform for each attribute of the directional light
     *
     * @param uniformName uniform name
     * @throws Exception the exception
     */
    public void createDirectionalLightUniform(String uniformName) throws Exception {
        createUniform(uniformName + ".colour");
        createUniform(uniformName + ".direction");
        createUniform(uniformName + ".intensity");
    }

    /**
     * Create uniform for each attribute of the material
     *
     * @param uniformName uniform name
     * @throws Exception the exception
     */
    public void createMaterialUniform(String uniformName) throws Exception {
        createUniform(uniformName + ".color");
        createUniform(uniformName + ".hasTexture");
        createUniform(uniformName + ".reflectance");
    }

    /**
     * Retrieve a uniform location by name
     *
     * @param uniformName the uniform name
     * @return the location of the uniform
     */
    public int getUniform(String uniformName) {
        return mUniformsList.get(uniformName);
    }

    /**
     * Modifies the value of a uniform variable with the specified value
     *
     * @param uniformName the uniform name
     * @param value       the value
     */
    public void setUniform(String uniformName, Matrix4f value) {
        FloatBuffer fb = BufferUtils.createFloatBuffer(16);
        /*load the value in th floatbuffer*/
        value.get(fb);
        /*Warning can cause nullpointer exception*/
        glUniformMatrix4fv(mUniformsList.get(uniformName), false, fb);
    }

    /**
     * Modifies the value of a uniform material with the specified material
     *
     * @param uniformName the uniform name
     * @param material    the material
     */
    public void setUniform(String uniformName, Material material) {
        setUniform(uniformName + ".color", material.getColor());
        setUniform(uniformName + ".hasTexture", material.isTextured() ? 1 : 0);
        setUniform(uniformName + ".reflectance", material.getReflectance());
    }

    /**
     * Modifies the value of a uniform Vector3f with the specified Vector3f
     *
     * @param uniformName the uniform name
     * @param value       the Vector3f
     */
    public void setUniform(String uniformName, Vector3f value) {
        glUniform3f(mUniformsList.get(uniformName), value.x, value.y, value.z);
    }

    /**
     * Modifies the value of a uniform int with the specified float
     *
     * @param uniformName the uniform name
     * @param value       the float
     */
    public void setUniform(String uniformName, int value) {
        glUniform1i(mUniformsList.get(uniformName), value);
    }

    /**
     * Modifies the value of a uniform float with the specified float
     *
     * @param uniformName the uniform name
     * @param value       the float
     */
    public void setUniform(String uniformName, float value) {
        glUniform1f(mUniformsList.get(uniformName), value);
    }

    /**
     * Modifies the value of uniforms specified by the uniformName with the specified pointLights
     *
     * @param uniformName the uniform name
     * @param pointLights the pointlights
     */
    public void setUniform(String uniformName, PointLight[] pointLights) {
        int numLights = pointLights != null ? pointLights.length : 0;
        for (int i = 0; i < numLights; i++) {
            setUniform(uniformName, pointLights[i], i);
        }
    }

    /**
     * Modifies the value of uniform specified by the uniformName and the position with the specified value
     *
     * @param uniformName the uniform name
     * @param pointLight  the point light
     * @param pos         the position
     */
    public void setUniform(String uniformName, PointLight pointLight, int pos) {
        setUniform(uniformName + "[" + pos + "]", pointLight);
    }

    /**
     * Modifies the value of uniform specified by the uniformName with the specified value
     *
     * @param uniformName the uniform name
     * @param pointLight  the point light
     */
    private void setUniform(String uniformName, PointLight pointLight) {
        setUniform(uniformName + ".colour", pointLight.getColor());
        setUniform(uniformName + ".position", pointLight.getPosition());
        setUniform(uniformName + ".intensity", pointLight.getIntensity());
        setUniform(uniformName + ".att_constant", pointLight.getConstantAtt());
        setUniform(uniformName + ".att_linear", pointLight.getLinearAtt());
        setUniform(uniformName + ".att_exponent", pointLight.getQuadraticAtt());
    }

    /**
     * Modifies the value of uniforms specified by the uniformName with the specified spotLights
     *
     * @param uniformName the uniform name
     * @param spotLights  the spotLights
     */
    public void setUniform(String uniformName, SpotLight[] spotLights) {
        int numLights = spotLights != null ? spotLights.length : 0;
        for (int i = 0; i < numLights; i++) {
            setUniform(uniformName, spotLights[i], i);
        }
    }

    /**
     * Modifies the value of uniform specified by the uniformName and the position with the specified value
     *
     * @param uniformName the uniform name
     * @param spotLight   the spotlight
     * @param pos         the position
     */
    public void setUniform(String uniformName, SpotLight spotLight, int pos) {
        setUniform(uniformName + "[" + pos + "]", spotLight);
    }

    /**
     * Modifies the value of uniform specified by the uniformName with the specified value
     *
     * @param uniformName the uniform name
     * @param spotLight   the spotlight
     */
    private void setUniform(String uniformName, SpotLight spotLight) {
        setUniform(uniformName + ".pl", (PointLight) spotLight);
        setUniform(uniformName + ".conedir", spotLight.getConedir());
        setUniform(uniformName + ".cutoff", spotLight.getCutoffAngle());
    }

    /**
     * Modifies the value of uniform specified by the uniformName with the specified value
     *
     * @param uniformName the uniform name
     * @param dirLight    the directional Light
     */
    public void setUniform(String uniformName, DirectionalLight dirLight) {
        setUniform(uniformName + ".colour", dirLight.getColor());
        setUniform(uniformName + ".direction", dirLight.mDirection);
        setUniform(uniformName + ".intensity", dirLight.getIntensity());
    }

    /**
     * Modifies the value of uniform specified by the uniformName with the specified value
     *
     * @param uniformName the uniform name
     * @param ambient     the ambient light
     */
    public void setUniform(String uniformName, AmbientLight ambient) {
        setUniform(uniformName, ambient.getShaderValue());
    }

}
