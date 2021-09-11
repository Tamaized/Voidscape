#version 150

in vec4 vertexColor;

uniform vec4 ColorModulator;
uniform float Alpha;

out vec4 fragColor;

void main() {
    vec4 color = vertexColor;
    if (color.a >= Alpha) {
        discard;
    }
    fragColor = color * ColorModulator;
}
