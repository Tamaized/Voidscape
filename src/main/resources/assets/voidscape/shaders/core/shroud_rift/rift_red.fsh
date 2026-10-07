#version 330

#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:globals.glsl>
#moj_import <voidscape:rift.glsl>

in vec2 texCoord0;
in vec4 vertexColor;

out vec4 fragColor;

const vec3 DEEP_COLOR = vec3(0.20, 0.02, 0.03);
const vec3 BODY_COLOR = vec3(0.70, 0.08, 0.09);
const vec3 RIM_COLOR = vec3(0.92, 0.22, 0.16);
const vec3 EDGE_COLOR = vec3(1.00, 0.80, 0.68);
const float BODY_ALPHA = 0.6;

void main() {
  fragColor = draw(GameTime, texCoord0, vertexColor, DEEP_COLOR, BODY_COLOR, RIM_COLOR, EDGE_COLOR, BODY_ALPHA, ColorModulator);
}
