#version 430

in vec2 texCoords;
in vec4 particleColor;

uniform sampler2D image;

out vec4 fragColor;

void main()
{
    vec4 pixelFromImage = texture(image, texCoords);
    fragColor = pixelFromImage * particleColor;
}