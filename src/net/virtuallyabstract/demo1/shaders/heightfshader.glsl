#version 430 core

in vec4 gIntensity;

out vec4 color;

void main(void)
{
	color = gIntensity;
}