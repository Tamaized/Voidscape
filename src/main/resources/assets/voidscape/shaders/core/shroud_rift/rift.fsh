#version 330

#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:globals.glsl>
#moj_import <voidscape:rift.glsl>

in vec2 texCoord0;
in vec4 vertexColor;

out vec4 fragColor;

const vec3 DEEP_COLOR = vec3(0.23, 0.06, 0.23);
const vec3 BODY_COLOR = vec3(0.69, 0.21, 0.62);
const vec3 RIM_COLOR = vec3(0.88, 0.35, 0.82);
const vec3 EDGE_COLOR = vec3(1.0, 0.85, 0.97);
const float BODY_ALPHA = 0.6;

void main() {
    fragColor = draw(GameTime, texCoord0, vertexColor, DEEP_COLOR, BODY_COLOR, RIM_COLOR, EDGE_COLOR, BODY_ALPHA, ColorModulator);
}
