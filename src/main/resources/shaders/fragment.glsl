#version 430

in vec2 texCoords;

uniform sampler2D image;
uniform vec3 spriteColor;

out vec4 fragColor;

void main()
{
    vec4 pixelFromImage = texture(image, texCoords);
    fragColor = vec4(spriteColor, 1.0) * pixelFromImage;
}