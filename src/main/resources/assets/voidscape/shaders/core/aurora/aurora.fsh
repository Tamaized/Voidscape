#version 150

#moj_import <fog.glsl>
#moj_import <voidscape:open_simplex2.glsl>

uniform vec4 ColorModulator;
uniform float GameTime;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;
uniform int SeedContext;
uniform vec3 PositionContext;

out vec4 fragColor;

in vec4 pixelPos;
in vec4 vertexColor;

const int STEPS = 16;
const float FSTEPS = 16.0;
const float PRECISION = 0.000001;

float genNoise(float x, float z, float speed) {
    float xx = x + PositionContext.x + (SeedContext / 360);
    float zz = z + PositionContext.z + (SeedContext % 360);
    return openSimplex2_ImproveXY(vec3(xx / 512.0, zz / 512.0, GameTime * speed)).a;
}

int floatToOneOrZero(float value) {
    return int(step(PRECISION, value));
}

float fixNoise(float noise) {
    float absNoise = abs(noise);
    // Avoids if (noise > -0.2 && noise < 0.2) else ...
    int swap = floatToOneOrZero(absNoise - 0.2);
    noise = (1 - swap) * (1.0 + absNoise * 5.0) + swap * -1.0;

    noise = clamp((noise + 1.0) / 2.0 - 0.5, 0.0, 1.0);
    noise = (1.0 - noise) * floatToOneOrZero(noise); // Avoids if (noise > 0)
    return noise;
}

// https://michaelwalczyk.com/blog-ray-marching.html
float rayMarch(vec3 origin, vec3 direction) {
    float noise = 0.0;
    float scale = 6;
    for (int i = 0; i < STEPS; ++i) {
        vec3 curPos = origin + ((i / FSTEPS) * 0.35) * direction;

        // Avoids if (i > 0)
        int swap = min(i, 1);
        float fade = swap * (((STEPS - i) / FSTEPS) * 0.65) + (1 - swap);
        noise += fixNoise(genNoise(curPos.x * scale, curPos.z * scale, 360)) * fade;
    }

    return noise;
}

void main() {
    // Normalize pixelPos to [-1.0, 1.0]
    vec2 uv = pixelPos.xz * 2.0 - 1.0;

    float noise = rayMarch(vec3(uv.x, 0.0, uv.y), vec3(uv.x, 1.0, uv.y));
    float colorNoise = genNoise(uv.x, uv.y, 450.0);

    colorNoise = ((colorNoise + 1.0) / 2.0) * 0.5;
    vec4 color = vec4(0.45 + colorNoise * 0.6, 0.55 - colorNoise * 0.75, 1.0 - colorNoise * 0.25, noise);
    float fogFade = linear_fog_fade(length(pixelPos.xz / 2.75), FogStart, FogEnd);
    fragColor = linear_fog(vec4(vertexColor.rgb * ColorModulator.rgb * color.rgb, vertexColor.a * ColorModulator.a * color.a * fogFade), length(pixelPos.xz / 2.5), FogStart, FogEnd, FogColor);
}