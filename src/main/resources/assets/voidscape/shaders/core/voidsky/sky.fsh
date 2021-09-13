#version 150

#moj_import <matrix.glsl>

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;

uniform float GameTime;

in vec4 texProj0;

const mat4 SCALE_TRANSLATE = mat4(
0.5, 0.0, 0.0, 0.25,
0.0, 0.5, 0.0, 0.25,
0.0, 0.0, 1.0, 0.0,
0.0, 0.0, 0.0, 1.0
);

mat4 end_portal_layer(float layer) {
    mat4 translate = mat4(
    1.0, 0.0, 0.0, 17.0 / layer,
    0.0, 1.0, 0.0, (2.0 + layer / 1.5) * (GameTime * 1.5),
    0.0, 0.0, 1.0, 0.0,
    0.0, 0.0, 0.0, 1.0
    );

    mat2 rotate = mat2_rotate_z(radians((layer * layer * 4321.0 + layer * 9.0) * 2.0));

    mat2 scale = mat2((4.5 - layer / 4.0) * 2.0);

    return mat4(scale * rotate) * translate * SCALE_TRANSLATE;
}

float mod(float a, float b) {
    return a - (b * floor(a/b));
}

out vec4 fragColor;

void main() {
    vec3 color = vec3(0);//textureProj(Sampler0, texProj0).rgb;
    float colorMod = 0.15F;
    float fade = 0.1F;

    float exactTick = GameTime * 24000.0F;
    float dur = 3200.0F;
    float phase = cos(radians((mod(exactTick, dur) / dur) * 360F)) * 0.5F + 0.5F;
    float maxPhases = 8.0F;
    int phases = int(ceil(phase * maxPhases));
    float lerp = mod(phase, (1F / maxPhases)) * maxPhases;
    for (int i = 0; i < phases + 1; ++i) {
        colorMod = 2.0F / (18 - i);
        fade = i == 0 ? 0.1F : i < phases ? 1.0F : lerp;

        float r = (/*RANDOM.nextFloat() * */0.5F + 0.1F) * colorMod * fade;
        float g = (/*RANDOM.nextFloat() * */0.5F + 0.4F) * colorMod * fade * 0.4F;
        float b = (/*RANDOM.nextFloat() * */0.5F + 0.5F) * colorMod * fade * 0.8F;

        color += textureProj(Sampler1, texProj0 * end_portal_layer(float(i + 1))).rgb * vec3(r, g, b);
    }

    fragColor = vec4(color, 1.0);
}
