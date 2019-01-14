#version 430

layout (location = 0) in vec4 vertex;

uniform mat4 projectionMatrix;
uniform mat4 modelMatrix;

void main()
{
    gl_Position = projectionMatrix * modelMatrix * vec4(vertex.x, vertex.y, 0, 1.0);
}