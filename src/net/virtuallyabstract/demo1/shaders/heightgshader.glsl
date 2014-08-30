#version 430 core

layout (triangles) in;

layout (triangle_strip) out;
layout (max_vertices = 3) out;

in vec3 mvPosition[];
in vec3 lightPos[];
in vec4 intensity[];
//in vec4 texCoord[];

out vec4 gIntensity;
//out vec2 texCoords;

void main(void)
{
	int i;
	
	//Calculate the normal for the triangle, this will be the same for all vertices
	vec4 ab = normalize(gl_in[0].gl_Position - gl_in[1].gl_Position);
	vec4 bc = normalize(gl_in[1].gl_Position - gl_in[2].gl_Position);
	vec3 normal = normalize(cross(vec3(bc), vec3(ab)));

	for(i = 0; i < gl_in.length(); i++) {
		gl_Position = gl_in[i].gl_Position;
		
		//Calculate the intensity based off the light position and calculated normal
		vec3 vLight = normalize(lightPos[i] - mvPosition[i]);
		gIntensity = (max(dot(vLight, normal), 0.0) + 0.0) * intensity[i];
		
		//texCoords = texCoord[i].xy;
		
		EmitVertex();
	}
	
	EndPrimitive();
}