package net.virtuallyabstract.demo1

import javax.media.opengl.GL4
import java.nio.IntBuffer
import java.nio.FloatBuffer
import javax.media.opengl.GL

class Grid(gl: GL4, xwidth: Float, xcount: Int, zwidth: Float, zcount: Int) extends Matrices with Movable with Rotatable {
  
    var vertexCount: Int = -1;
	var verticesBuffer: Int = -1;
	var textureBuffer: Int = -1;
	
	var vaoID: Int = -1;
	
	init();
	
	def init(): Unit = {
			val buffers: IntBuffer = IntBuffer.allocate(2);
			gl.glGenBuffers(2, buffers);
			
			verticesBuffer = buffers.get();
			textureBuffer = buffers.get();
			
			//Count of the number of squares in the grid and multiply by the number of vertices per square (2 triangles per square = 6 vertices)
			vertexCount = 4 * 6 * (xcount) * (zcount);
			
			var vertices: FloatBuffer = FloatBuffer.allocate(vertexCount);
			var textures: FloatBuffer = FloatBuffer.allocate(vertexCount);
			val addVertex = BufferUtils.addVertex(vertices)_;
			val addTexture = BufferUtils.addVertex(textures)_;
			
			vertices.clear();
			textures.clear();
			
			val maxWidth = xwidth * xcount;
			val maxLen = zwidth * zcount * -1;
			for(x <- 0 until xcount) {
				for(z <- 0 until zcount) {
					val xpos: Float = x * xwidth;
					val zpos: Float = z * zwidth * -1;  //Negative -1 to render it down the z-axis
					
					//println(xpos / maxWidth + ", " + zpos / maxLen);
					//println((xpos + xwidth) / maxWidth + ", " + (zpos - zwidth) / maxLen);
					//println(xpos / maxWidth + ", " + (zpos - zwidth) / maxLen);
									
					addVertex(xpos, 0.0f, zpos);
					addTexture(xpos / maxWidth, zpos / maxLen, 0.0f);
					addVertex(xpos + xwidth, 0.0f, zpos - zwidth);
					addTexture((xpos + xwidth) / maxWidth, (zpos - zwidth) / maxLen, 0.0f);
					addVertex(xpos, 0.0f, zpos - zwidth);				
					addTexture(xpos / maxWidth, (zpos - zwidth) / maxLen, 0.0f);
					
					//println(xpos / maxWidth + ", " + zpos / maxLen);
					//println((xpos + xwidth) / maxWidth + ", " + zpos / maxLen);
					//println((xpos + xwidth) / maxWidth + ", " + (zpos - zwidth) / maxLen);
					
					addVertex(xpos, 0.0f, zpos);
					addTexture(xpos / maxWidth, zpos / maxLen, 0.0f);
					addVertex(xpos + xwidth, 0.0f, zpos);
					addTexture((xpos + xwidth) / maxWidth, zpos / maxLen, 0.0f);
					addVertex(xpos + xwidth, 0.0f, zpos - zwidth);				
					addTexture((xpos + xwidth) / maxWidth, (zpos - zwidth) / maxLen, 0.0f);
					
				}
			}
			
			vertices.flip();
			textures.flip();
			
			vaoID = BufferUtils.createVAO(gl);
			
			BufferUtils.setBufferData(gl, verticesBuffer, vertices);
			BufferUtils.setBufferData(gl, textureBuffer, textures)
	}
	
	def enableVertexBuffer = enableBuffer(verticesBuffer)_;
	def enableTextureBuffer = enableBuffer(textureBuffer)_;
	
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
		enableTextureBuffer(program.getTexturesIndex());
		
		//Normal is always pointing up for the grid
		gl.glUniform4f(program.getNormalsIndex(), 0.0f, 1.0f, 0.0f, 1.0f);
		
		//Modelview matrix
		gl.glUniformMatrix4fv(program.getModelViewIndex(), 1, false, getModelView());

		//Normal matrix (transpose of the inverse modelview matrix)
		gl.glUniformMatrix4fv(program.getITModelViewIndex(), 1, false, getITModelView());	
		
		gl.glBindVertexArray(vaoID);
				
		gl.glDrawArrays(GL.GL_TRIANGLES, 0, vertexCount);		
	}
}