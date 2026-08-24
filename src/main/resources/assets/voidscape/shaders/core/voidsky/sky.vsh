#version 330

#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:projection.glsl>

in vec3 Position;

out vec4 texProj0;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    texProj0 = projection_from_position(gl_Position);
}
