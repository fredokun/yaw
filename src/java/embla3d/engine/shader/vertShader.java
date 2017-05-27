
// This file is generated from `vertShader.fs` shader program
// Please do not edit directly
package embla3d.engine;

public class vertShader {
    public final static String SHADER_STRING = "#version 330 core\n\nlayout(location = 0) in vec3 position;\nlayout(location = 1) in vec2 texCoord;\nlayout(location = 2) in vec3 normal;\n\nout vec3 vNorm;\nout vec3 vPos;\nout vec2 outTexCoord;\n\nuniform mat4 projectionMatrix;\nuniform mat4 modelViewMatrix;\n\nvoid main()\n{\n	vec4 mvPos = modelViewMatrix * vec4(position, 1.0);\n    gl_Position = projectionMatrix*mvPos;\n    vNorm = normalize(modelViewMatrix * vec4(normal, 0.0)).xyz;\n    vPos = mvPos.xyz;\n    outTexCoord=texCoord;\n}";
}
