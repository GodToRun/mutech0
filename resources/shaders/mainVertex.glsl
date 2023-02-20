#version 330 core

in vec3 position;
in vec3 color;
in vec2 textureCoord;

out vec3 passColor;
out vec2 passTextureCoord;
out vec3 reduceColor;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main() {
	reduceColor = vec3(0, 0, 0);
	gl_Position = projection * view * model * vec4(position, 1.0);
	if (gl_VertexID % 24 == 0) reduceColor = vec3(0.2, 0.2, 0.2);
	passColor = color - reduceColor;
	passTextureCoord = textureCoord;
}