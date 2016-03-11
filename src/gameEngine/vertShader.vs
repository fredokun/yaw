#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec3 normal;

out vec3 vNorm;
out vec3 vPos;

uniform mat4 projectionMatrice;
uniform mat4 modelViewMatrix;

void main()
{
	vec4 mvPos = modelViewMatrix * vec4(position, 1.0);
    gl_Position =projectionMatrice*mvPos;
    vNorm = normalize(modelViewMatrix * vec4(normal, 0.0)).xyz;
    vPos = mvPos.xyz;
}