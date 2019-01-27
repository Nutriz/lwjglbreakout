#version 430

layout (location = 0) in vec4 vertex;

out vec2 texCoords;

uniform bool  chaos;
uniform bool  confuse;
uniform bool  shake;
uniform float time;

void main()
{
    gl_Position = vec4(vertex.xy, 0.0, 1.0);
    vec2 texture = vertex.zw;

    if (chaos)
    {
        float strength = 0.3;
        vec2 pos = vec2(texture.x + sin(time) * strength, texture.y + cos(time) * strength);
        texCoords = pos;
    }
    else if (confuse)
    {
        texCoords = vec2(1-texture.x, 1-texture.y);
    }
    else
    {
        texCoords = texture;
    }

    if (shake)
    {
        float strength = 0.02;
        gl_Position.x += cos(time * 10) * strength;
        gl_Position.y += cos(time * 15) * strength;
    }
}