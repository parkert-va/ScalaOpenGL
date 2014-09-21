#version 430 core

layout (triangles) in;

layout (triangle_strip) out;
layout (max_vertices = 3) out;

in vec3 mvPosition[];
in vec3 lightPos[];
in vec4 intensity[];

out vec4 gIntensity;

void main(void)
{
	int i;
	
	//Calculate the normal for the triangle, this will be the same for all vertices
	vec4 ab = normalize(gl_in[0].gl_Position - gl_in[1].gl_Position);
	vec4 bc = normalize(gl_in[1].gl_Position - gl_in[2].gl_Position);

	//The cross product of two vectors will create a new vector that points in the direction
	//the plane created by the first two vectors is facing.  In other words a normal vector.
	vec3 normal = normalize(cross(vec3(bc), vec3(ab)));

	//I wish I could explain this better.  It appears that vertical surfaces generate a normal pointing
	//in the wrong direction.  Reflecting it across the xy-plane fixes this problem
	normal.z = -normal.z;

	for(i = 0; i < gl_in.length(); i++) {
		gl_Position = gl_in[i].gl_Position;
		
		//Calculate the intensity based off the light position and calculated normal
		vec3 vLight = normalize(lightPos[i] - mvPosition[i]);
		gIntensity = (max(dot(vLight, normal), 0.0) + 0.0) * intensity[i];		
		
		EmitVertex();
	}
	
	EndPrimitive();
}