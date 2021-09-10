#version 150

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float Alpha;
uniform int Type;

in vec2 texCoord0;
in vec4 vertexColor;

out vec4 fragColor;

void main() {
    vec4 color = texture(Sampler0, texCoord0) * vertexColor;
    if (Type == 0 && color.a == Alpha) {
        discard;
    } else if (Type == 1 && color.a < Alpha) {
        discard;
    } else if (Type == 2 && color.a > Alpha) {
        discard;
    } else if (Type == 3 && color.a <= Alpha) {
        discard;
    } else if (Type == 4 && color.a >= Alpha) {
        discard;
    } else if (Type == 5 && color.a != Alpha) {
        discard;
    }
    fragColor = color * ColorModulator;
}
