#version 330

#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:globals.glsl>
#moj_import <voidscape:open_simplex2.glsl>

in vec2 texCoord0;
in vec4 vertexColor;

out vec4 fragColor;

const float TAU = 6.28318530718;
const float SECONDS_PER_GAME_DAY = 1200.0;
const float SEED_SPREAD = 64.0;
const float QUAD_ASPECT = 0.5;

const vec3 DEEP_COLOR = vec3(0.23, 0.06, 0.23);
const vec3 BODY_COLOR = vec3(0.69, 0.21, 0.62);
const vec3 RIM_COLOR = vec3(0.88, 0.35, 0.82);
const vec3 EDGE_COLOR = vec3(1.0, 0.85, 0.97);
const float BODY_ALPHA = 0.6;

const float BREATH_PERIOD = 8.0;
const float INHALE_FRACTION = 0.48;
const float BREATH_SWELL = 0.05;
const float EDGE_BREATH_SWELL = 0.03;
const float BREATH_GLOW_MIN = 0.9;
const float BREATH_BRIGHTEN = 0.2;

const float BLOB_SCALE = 2.5;
const float BLOB_WARP = 0.25;
const float BLOB_DRIFT = 0.08;
const float BLOB_THRESHOLD_EXHALED = 0.06;
const float BLOB_THRESHOLD_INHALED = 0.04;
const float BLOB_SOFTNESS = 0.12;
const float BLOB_DETAIL_WEIGHT = 0.35;
const float BLOB_GLOW = 0.6;

const float FUNNEL_ZOOM = 0.6;
const float CENTER_DARKEN = 0.45;

const int LAYER_COUNT = 4;
const float LAYER_DEPTH_RANGE = 2.0;
const float LAYER_FLOW_SPEED = -0.25;
const float LAYER_FADE = 0.25;
const float LAYER_SEED_OFFSET = 17.0;
const float LAYER_SEED_CYCLE = 12.0;

const float BLOB_OVERFLOW = 0.12;
const float OVERFLOW_LAYER_DEPTH = 0.35;
const float OVERFLOW_ERODE = 0.45;

const float LIP_SHADOW = 0.6;
const float LIP_SHADOW_START = 0.06;
const float LIP_SHADOW_WIDTH = 0.15;

const float EDGE_SHARDS = 24.0;
const float EDGE_JAG = 0.22;
const float EDGE_FINE_JAG = 0.05;
const float EDGE_CRAWL = 0.02;
const float RIM_WIDTH = 0.12;
const float RIM_OUTER_WIDTH = 0.04;
const float RIM_EDGE_ALPHA = 0.7;
const float EDGE_LINE_SHARPNESS = 6.0;

float noise(vec3 position) {
    return openSimplex2_ImproveXY(position).a;
}

float shardRadius(float index, vec3 seed, float time) {
    float angle = index / EDGE_SHARDS * TAU;
    return noise(vec3(cos(angle) * 1.5, sin(angle) * 1.5, time * EDGE_CRAWL) + seed);
}

float edgeRadius(float angle, vec3 seed, float time) {
    float shardPosition = angle / TAU * EDGE_SHARDS;
    float shard = floor(shardPosition);
    float jag = mix(shardRadius(shard, seed, time), shardRadius(shard + 1.0, seed, time), fract(shardPosition));
    float fine = noise(vec3(cos(angle) * 6.0, sin(angle) * 6.0, time * EDGE_CRAWL * 3.0) + seed);
    return 1.0 - EDGE_JAG * (0.5 + 0.5 * jag) - EDGE_FINE_JAG * (0.5 + 0.5 * fine);
}

float blobField(vec2 position, vec3 seed, float time) {
    vec2 warp = vec2(
        noise(vec3(position * 1.5, time * BLOB_DRIFT * 0.5) + seed),
        noise(vec3(position * 1.5 + 7.3, time * BLOB_DRIFT * 0.5) + seed)
    );
    vec2 warped = position + warp * BLOB_WARP;
    return (1.0 - BLOB_DETAIL_WEIGHT) * noise(vec3(warped * BLOB_SCALE, time * BLOB_DRIFT) + seed)
        + BLOB_DETAIL_WEIGHT * noise(vec3(warped * BLOB_SCALE * 2.0, time * BLOB_DRIFT * 1.5) + seed);
}

void main() {
    vec2 position = texCoord0 * 2.0 - 1.0;
    float radius = length(position);
    float angle = atan(position.y, position.x);
    vec3 seed = vertexColor.rgb * SEED_SPREAD;
    float time = GameTime * SECONDS_PER_GAME_DAY;

    float phase = fract(time / BREATH_PERIOD + vertexColor.r);
    float inhale = smoothstep(0.0, 1.0, min(phase / INHALE_FRACTION, 1.0));
    float exhale = smoothstep(0.0, 1.0, max(phase - INHALE_FRACTION, 0.0) / (1.0 - INHALE_FRACTION));
    float breath = inhale - exhale;
    float swell = 1.0 - BREATH_SWELL * (1.0 - breath);

    float edge = edgeRadius(angle, seed, time) * (1.0 - EDGE_BREATH_SWELL * (1.0 - breath));
    float outside = radius - edge;
    if (outside > BLOB_OVERFLOW) {
        discard;
    }
    float inside = step(outside, 0.0);
    float spillProgress = clamp(outside / BLOB_OVERFLOW, 0.0, 1.0);
    float spill = 1.0 - smoothstep(0.0, 1.0, spillProgress);

    float radialPosition = radius / edge;

    vec2 blobPosition = position * vec2(QUAD_ASPECT, 1.0) / swell * (1.0 + FUNNEL_ZOOM * (1.0 - radialPosition));
    float threshold = mix(BLOB_THRESHOLD_EXHALED, BLOB_THRESHOLD_INHALED, breath) + OVERFLOW_ERODE * spillProgress;
    float centerFade = CENTER_DARKEN * (1.0 - radialPosition);
    float flow = time * LAYER_FLOW_SPEED;
    float flowStep = floor(flow);
    float flowFraction = flow - flowStep;

    vec3 color = DEEP_COLOR;
    float coverage = 0.0;
    for (int layer = LAYER_COUNT - 1; layer >= 0; --layer) {
        float layerDepth = (float(layer) + flowFraction) / float(LAYER_COUNT);
        float fade = smoothstep(0.0, LAYER_FADE, layerDepth) * (1.0 - smoothstep(1.0 - LAYER_FADE, 1.0, layerDepth));
        vec2 layerPosition = blobPosition * exp2(layerDepth * LAYER_DEPTH_RANGE);
        vec3 layerSeed = seed + vec3(mod(float(layer) - flowStep, LAYER_SEED_CYCLE) * LAYER_SEED_OFFSET);
        float shape = smoothstep(threshold - BLOB_SOFTNESS, threshold + BLOB_SOFTNESS, blobField(layerPosition, layerSeed, time));
        float blob = shape * spill * mix(fade, sqrt(fade) * (1.0 - smoothstep(0.0, OVERFLOW_LAYER_DEPTH, layerDepth)), spillProgress);
        vec3 layerColor = mix(mix(RIM_COLOR, BODY_COLOR, layerDepth), DEEP_COLOR, max(layerDepth * layerDepth, centerFade));
        layerColor += EDGE_COLOR * BLOB_GLOW * 4.0 * shape * (1.0 - shape) * (1.0 - layerDepth);
        layerColor = mix(layerColor, EDGE_COLOR, breath * BREATH_BRIGHTEN * (1.0 - layerDepth));
        color = mix(color, layerColor, mix(ceil(blob), blob, inside));
        coverage += (1.0 - coverage) * blob;
    }

    float lip = (1.0 - smoothstep(LIP_SHADOW_START, RIM_WIDTH + LIP_SHADOW_WIDTH, -outside)) * inside;
    color = mix(color, DEEP_COLOR, LIP_SHADOW * lip);

    float rim = mix(1.0 - smoothstep(0.0, RIM_OUTER_WIDTH, outside), 1.0 - smoothstep(0.0, RIM_WIDTH, -outside), inside);
    color = mix(color, RIM_COLOR, rim);
    color = mix(color, EDGE_COLOR, pow(rim, EDGE_LINE_SHARPNESS));
    float body = mix(coverage, mix(BODY_ALPHA, 1.0, coverage) * mix(BREATH_GLOW_MIN, 1.0, breath), inside);
    float alpha = max(body, rim) * mix(1.0, RIM_EDGE_ALPHA, pow(rim, 4.0));

    fragColor = vec4(color * ColorModulator.rgb, alpha * vertexColor.a * ColorModulator.a);
}
