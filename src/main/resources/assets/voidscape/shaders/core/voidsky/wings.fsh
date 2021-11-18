#version 150

#moj_import <matrix.glsl>

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;

uniform float GameTime;
uniform vec4 ColorModulator;

in vec4 texProj0;
in vec2 texCoord0;
in vec4 vertexColor;

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

    return mat4((scale * 0.8) * rotate) * translate * SCALE_TRANSLATE;
}

float mod(float a, float b) {
    return a - (b * floor(a/b));
}

out vec4 fragColor;

void main() {
    vec3 color = vec3(0);
    float colorMod = 0.15;
    float fade = 0.1;

    float exactTick = 0.5 * 24000.0;
    float dur = 3200.0;
    float phase = cos(radians((mod(exactTick, dur) / dur) * 360.0)) * 0.5 + 0.5;
    float maxPhases = 8.0;
    int phases = int(ceil(phase * maxPhases));
    float lerp = mod(phase, (1.0 / maxPhases)) * maxPhases;
    for (int i = 0; i < phases + 1; ++i) {
        colorMod = 2.0 / (18 - i);
        fade = i == 0 ? 0.1 : i < phases ? 1.0 : lerp;

        color += textureProj(Sampler1, texProj0 * end_portal_layer(float(i + 1))).rgb * vec3(fade);
    }
    vec3 alpha = vec3(texture(Sampler0, texCoord0).a);
    fragColor = vec4(color * alpha, 1) * vertexColor * ColorModulator;
}
