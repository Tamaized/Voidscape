#version 150

in vec4 vertexColor;

uniform vec4 ColorModulator;
uniform float Alpha;
uniform int Type;

out vec4 fragColor;

void main() {
    vec4 color = vertexColor;
    if (Type == 0 && color.a == Alpha) {
        discard;
    } else if (Type == 1 && color.a >= Alpha) {
        discard;
    } else if (Type == 2 && color.a <= Alpha) {
        discard;
    } else if (Type == 3 && color.a > Alpha) {
        discard;
    } else if (Type == 4 && color.a < Alpha) {
        discard;
    } else if (Type == 5 && color.a != Alpha) {
        discard;
    }
    fragColor = color * ColorModulator;
}
