#version 430

in vec2 texCoords;

out vec4 color;

uniform sampler2D scene;
// uniform vec2      offsets[9];
// uniform int       edge_kernel[9];
// uniform float     blur_kernel[9];

uniform bool chaos;
uniform bool confuse;
uniform bool shake;

const float offset = 1.0 / 300.0;

// TODO transfert to uniform
const vec2 offsets[9] = vec2[](
    vec2(-offset,  offset), // top-left
    vec2( 0.0f,    offset), // top-center
    vec2( offset,  offset), // top-right
    vec2(-offset,  0.0f),   // center-left
    vec2( 0.0f,    0.0f),   // center-center
    vec2( offset,  0.0f),   // center-right
    vec2(-offset, -offset), // bottom-left
    vec2( 0.0f,   -offset), // bottom-center
    vec2( offset, -offset)  // bottom-right
);

const float edge_kernel[9] = float[](
        -1, -1, -1,
        -1,  9, -1,
        -1, -1, -1
    );

const float blur_kernel[9] = float[](
        1.0 / 16, 2.0 / 16, 1.0 / 16,
        2.0 / 16, 4.0 / 16, 2.0 / 16,
        1.0 / 16, 2.0 / 16, 1.0 / 16
    );

void main()
{
    color = vec4(0.0f);
    vec3 samplev[9];
    // sample from texture offsets if using convolution matrix
    if(chaos || shake) {
        for(int i = 0; i < 9; i++) {
            samplev[i] = vec3(texture(scene, texCoords.st + offsets[i]));
        }
    }

    // process effects
    if(chaos)
    {
        for(int i = 0; i < 9; i++)
            color += vec4(samplev[i] * edge_kernel[i], 0.0f)/2;
        color.a = 1.0f;
    }
    else if(confuse)
    {
        color = vec4(1.0 - texture(scene, texCoords).rgb, 1.0);
    }
    else if(shake)
    {
        for(int i = 0; i < 9; i++)
            color += vec4(samplev[i] * blur_kernel[i], 0.0f);
        color.a = 1.0f;
    }
    else
    {
        color = texture(scene, texCoords);
    }
}