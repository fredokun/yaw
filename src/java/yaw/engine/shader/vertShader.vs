#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 texCoord;
layout(location = 2) in vec3 normal;

out vec3 vNorm;
out vec3 vPos;
out vec2 outTexCoord;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

void main()
{
	vec4 mvPos = modelViewMatrix * vec4(position, 1.0);
    gl_Position = projectionMatrix*mvPos;
    vNorm = normalize(modelViewMatrix * vec4(normal, 0.0)).xyz;
    vPos = mvPos.xyz;
    outTexCoord=texCoord;
}