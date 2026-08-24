#version 330

#moj_import <minecraft:dynamictransforms.glsl>

layout(std140) uniform VoidscapeAlpha {
    float Alpha;
};

uniform sampler2D Sampler0;

in vec2 texCoord0;
in vec4 vertexColor;

out vec4 fragColor;

void main() {
    vec4 color = texture(Sampler0, texCoord0) * vertexColor;
    if (color.a >= Alpha) {
        discard;
    }
    fragColor = color * ColorModulator;
}
