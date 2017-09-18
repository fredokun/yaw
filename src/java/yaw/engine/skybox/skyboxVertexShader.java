
// This file is generated from `skyboxVertexShader.fs` shader program
// Please do not edit directly
package yaw.engine.skybox;

public class skyboxVertexShader {
    public final static String SHADER_STRING = "#version 330\n\nlayout (location=0) in vec3 position;\n\n\nuniform mat4 viewMatrix;\nuniform mat4 projectionMatrix;\n\nvoid main()\n{\n    gl_Position = projectionMatrix * viewMatrix * vec4(position, 1);\n}";
}
