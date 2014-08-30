#version 430 core

in vec4 intensity;

out vec4 color;

void main(void)
{
	color = intensity;
}