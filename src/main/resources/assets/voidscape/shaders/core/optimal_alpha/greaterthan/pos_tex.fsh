#version 150

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float Alpha;

in vec2 texCoord0;

out vec4 fragColor;

void main() {
    vec4 color = texture(Sampler0, texCoord0);
    if (color.a > Alpha) {
        discard;
    }
    fragColor = color * ColorModulator;
}
