
// This file is generated from `skyboxFragmentShader.fs` shader program
// Please do not edit directly
package yaw.engine.skybox;

public class skyboxFragmentShader {
    public final static String SHADER_STRING = "#version 330\n\nout vec4 fragColor;\n\nuniform vec3 color;\n\nvoid main()\n{\n    fragColor = vec4(color,1.0);\n}";
}
