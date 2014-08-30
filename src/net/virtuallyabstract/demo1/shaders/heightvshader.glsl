#version 430 core

layout (location = 0) in vec4 position;
layout (location = 1) in vec4 texCoords;
layout (location = 2) uniform vec4 color;

layout (location = 3) uniform mat4 mvMatrix;
layout (location = 4) uniform mat4 normalMatrix;
layout (location = 5) uniform mat4 pMatrix;

uniform sampler2D s;

out vec3 mvPosition;
out vec4 intensity;
out vec3 lightPos;
//out vec4 texCoord;

void main(void)
{
	vec4 texColor = texture(s, texCoords.xy);
	vec4 newPosition = vec4(position.x, position.y + texColor.y * 5.0, position.z, position.w);

	gl_Position = pMatrix * mvMatrix * newPosition;	
	
	//mvPosition will contain the vector position modified by the mvMatrix, we need this because the position the
	//geometry shader normally gets will also be modified by the projection matrix and therefore be unsuitable
	//for lighting calculations
	mvPosition = vec3(mvMatrix * position);
	lightPos = vec3(0.0, 5.0, 1.0);	
	
	//This will just pass through the color to the geometry shader where the intensity will be actually calculated
	intensity = color;
	//texCoord = texCoords;
}