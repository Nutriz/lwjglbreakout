#version 430

layout (location = 0) in vec4 vertex;

uniform mat4 projectionMatrix;
uniform mat4 modelMatrix;

out vec2 texCoords;
void main()
{
    texCoords = vertex.zw;
    gl_Position = projectionMatrix * modelMatrix * vec4(vertex.x, vertex.y, 0.0, 1.0);
}