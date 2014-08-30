package net.virtuallyabstract.demo1

import java.nio.FloatBuffer
import java.nio.IntBuffer

import com.jogamp.opengl.util.GLBuffers

import javax.media.opengl.GL
import javax.media.opengl.GL4

class Cube(gl: GL4) extends Matrices with Movable with Scalable with Rotatable {
    val vertexCount: Int = 144;

    var vaoID: Int = -1;
    
	var verticesBuffer: Int = -1;
	var normalsBuffer: Int = -1;
	var colorBuffer: Int = -1;
	
	init();

	private def init(): Unit = {
			val buffers: IntBuffer = IntBuffer.allocate(3);
			gl.glGenBuffers(3, buffers);
			
			verticesBuffer = buffers.get();
			normalsBuffer = buffers.get();
			colorBuffer = buffers.get();			
			
			val vertices: FloatBuffer = FloatBuffer.allocate(vertexCount);
			val normals: FloatBuffer = FloatBuffer.allocate(vertexCount);
			val colors: FloatBuffer = FloatBuffer.allocate(vertexCount);
			val addVertex = BufferUtils.addVertex(vertices)_;
			val addNormal = BufferUtils.addVertex(normals)_;
			val addColor = BufferUtils.addVertex(colors)_;
				
			vertices.clear();
			normals.clear();
			colors.clear();
			
			//Normalized Device Space
			//The region defined by the following coordinate limits:
			//   -1.0 to 1.0 on the X, Y and Z axis (where the positive Z axis is coming out of the screen towards you).
			//Anything that falls within that region will be displayed
			//
			//Once vertices are mapped to those coordinates they will be translated into 'Window coordinates' based on
			//the values of glViewPort and glDepthRange
			
			addVertex(-0.5f, -0.5f, 0.5f);
			addVertex(0.5f, 0.5f, 0.5f);
			addVertex(-0.5f, 0.5f, 0.5f);		
			addVertex(-0.5f, -0.5f, 0.5f);
			addVertex(0.5f, -0.5f, 0.5f);
			addVertex(0.5f, 0.5f, 0.5f);
			for(n <- 0 to 5)
			{
				addNormal(0.0f, 0.0f, 1.0f);
				addColor(1.0f, 1.0f, 0.0f);
			}
			
			addVertex(-0.5f, -0.5f, -0.5f);
			addVertex(-0.5f, 0.5f, -0.5f);		
			addVertex(0.5f, 0.5f, -0.5f);
			addVertex(-0.5f, -0.5f, -0.5f);
			addVertex(0.5f, 0.5f, -0.5f);
			addVertex(0.5f, -0.5f, -0.5f);
			for(n <- 0 to 5)
			{
				addNormal(0.0f, 0.0f, -1.0f);
				addColor(0.0f, 1.0f, 0.0f);
			}
			
			addVertex(-0.5f, -0.5f, -0.5f);
			addVertex(-0.5f, 0.5f, 0.5f);
			addVertex(-0.5f, 0.5f, -0.5f);
			addVertex(-0.5f, -0.5f, -0.5f);
			addVertex(-0.5f, -0.5f, 0.5f);
			addVertex(-0.5f, 0.5f, 0.5f);
			for(n <- 0 to 5)
			{
				addNormal(-1.0f, 0.0f, 0.0f);
				addColor(0.0f, 0.0f, 1.0f);
			}
			
			addVertex(0.5f, -0.5f, -0.5f);
			addVertex(0.5f, 0.5f, -0.5f);
			addVertex(0.5f, 0.5f, 0.5f);
			addVertex(0.5f, -0.5f, -0.5f);
			addVertex(0.5f, 0.5f, 0.5f);
			addVertex(0.5f, -0.5f, 0.5f);
			for(n <- 0 to 5)
			{
				addNormal(1.0f, 0.0f, 0.0f);
				addColor(0.0f, 0.5f, 0.5f);
			}
			
			addVertex(-0.5f, -0.5f, -0.5f);
			addVertex(0.5f, -0.5f, -0.5f);
			addVertex(0.5f, -0.5f, 0.5f);
			addVertex(-0.5f, -0.5f, -0.5f);
			addVertex(0.5f, -0.5f, 0.5f);
			addVertex(-0.5f, -0.5f, 0.5f);
			for(n <- 0 to 5)
			{
				addNormal(0.0f, -1.0f, 0.0f);
				addColor(0.5f, 0.5f, 0.0f);
			}
			
			addVertex(-0.5f, 0.5f, -0.5f);
			addVertex(0.5f, 0.5f, 0.5f);
			addVertex(0.5f, 0.5f, -0.5f);
			addVertex(-0.5f, 0.5f, -0.5f);
			addVertex(-0.5f, 0.5f, 0.5f);
			addVertex(0.5f, 0.5f, 0.5f);
			for(n <- 0 to 5)
			{
				addNormal(0.0f, 1.0f, 0.0f);
				addColor(0.5f, 0.0f, 0.5f);
			}
						
			vertices.flip();
			normals.flip();
			colors.flip();
			
	  		vaoID = BufferUtils.createVAO(gl);
	  		
	  		BufferUtils.setBufferData(gl, verticesBuffer, vertices);
	  		BufferUtils.setBufferData(gl, normalsBuffer, normals);
	  		BufferUtils.setBufferData(gl, colorBuffer, colors);
	}
	
	def enableVertexBuffer = enableBuffer(verticesBuffer)_;
	def enableNormalBuffer = enableBuffer(normalsBuffer)_;
	def enableColorBuffer = enableBuffer(colorBuffer)_;
	
	private def enableBuffer(bufferID: Int)(index: Int): Unit = {
  		gl.glBindVertexArray(vaoID);
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, bufferID);
		
		//Associates the data in the currently bound VBO with the currently bound VAO at the specified index.
		//The arguments provided describe the format of the provided buffer which in this case contains
		//tightly packed 4-component float vectors.
  		gl.glVertexAttribPointer(index, 4, GL.GL_FLOAT, false, 0, 0);
  		gl.glEnableVertexAttribArray(index);
	}
	
	def draw(program: GLProgram): Unit = {
	  
		enableVertexBuffer(program.getVerticesIndex());
		enableNormalBuffer(program.getNormalsIndex());
		enableColorBuffer(program.getColorsIndex());
		
		//Modelview matrix
		gl.glUniformMatrix4fv(program.getModelViewIndex(), 1, false, getModelView());

		//Normal matrix (transpose of the inverse modelview matrix)
		gl.glUniformMatrix4fv(program.getITModelViewIndex(), 1, false, getITModelView());	
		
		gl.glBindVertexArray(vaoID);
				
		gl.glDrawArrays(GL.GL_TRIANGLES, 0, vertexCount);		
	}
}