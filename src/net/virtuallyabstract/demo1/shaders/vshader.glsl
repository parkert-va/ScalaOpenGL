#version 430 core

layout (location = 0) in vec4 position;
layout (location = 1) in vec4 normal;
layout (location = 2) in vec4 color;

layout (location = 3) uniform mat4 mvMatrix;
layout (location = 4) uniform mat4 normalMatrix;
layout (location = 5) uniform mat4 pMatrix;

out vec4 intensity;

void main(void)
{
	gl_Position = pMatrix * mvMatrix * position;
	
    //Need to transform our normal to match the transformation of the position
    //This is done with the normal matrix which is the transposed inverse of the modelview matrix
	vec3 vNormal = normalize(vec3(normalMatrix * normal));
	
	vec3 lightPos = vec3(0.0, 0.0, 1.0);
	vec3 vLight = normalize(lightPos - vec3(mvMatrix * position));	
	intensity = max(dot(vLight, vNormal), 0.0) * color;
}