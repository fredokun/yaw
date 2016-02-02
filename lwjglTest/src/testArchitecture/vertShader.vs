#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec4 color;

out vec4 vColor;

uniform mat4 projectionMatrice;
uniform mat4 worldMatrice;

void main()
{
    vColor = color;
    gl_Position =projectionMatrice*worldMatrice* vec4(position, 1.0);
}