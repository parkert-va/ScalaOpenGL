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

void main(void)
{
    //Fetch the color value of the texture at the given coordinates
	vec4 texColor = texture(s, texCoords.xy);
	
	//Re-calculate the vertex position by increasing the y-location based on the color of the texture
	//The texture is greyscale so all three components in the vector should be the same.  We do however want
	//to multiplay the value by a scaling amount to increase the height of the terrain
	vec4 newPosition = vec4(position.x, position.y + texColor.y * 5.0, position.z, position.w);

	gl_Position = pMatrix * mvMatrix * newPosition;	
	
	//mvPosition will contain the vector position modified by the mvMatrix, we need this because the position the
	//geometry shader normally gets will also be modified by the projection matrix and therefore be unsuitable
	//for lighting calculations
	mvPosition = vec3(mvMatrix * newPosition);
	lightPos = vec3(0.0, 5.0, 1.0);	
	
	//This will just pass through the color to the geometry shader where the intensity will be actually calculated
	intensity = color;
}