#version 330 core

in vec3 passColor;
in vec2 passTextureCoord;
in int vertexID;
in vec3 reduceColor;
in vec3 gl_FragCoord;

out vec4 outColor;

uniform sampler2D tex;

void main() {
	outColor = texture(tex, passTextureCoord) - vec4(reduceColor, 0);
	if (outColor.a < 0.1)
		discard;
}