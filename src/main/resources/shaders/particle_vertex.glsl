#version 430

layout (location = 0) in vec4 vertex; // <vec2 position, vec2 texCoords>

uniform mat4 projectionMatrix;
uniform mat4 modelMatrix;
uniform vec2 offset;
uniform vec4 color;

out vec2 texCoords;
out vec4 particleColor;

void main()
{
    float scale = 10.0f;
    texCoords = vertex.zw;
    particleColor = color;
    gl_Position = projectionMatrix * modelMatrix * vec4((vertex.xy * scale) + offset, 0.0, 1.0);
}