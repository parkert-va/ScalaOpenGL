#version 430 core

in vec4 gIntensity;
//in vec2 texCoords;
//uniform sampler2D s;

out vec4 color;

void main(void)
{
	color = gIntensity;
	//color = texture(s, texCoords);
}