package net.virtuallyabstract.demo1

import com.jogamp.opengl.util.glsl.ShaderCode
import javax.media.opengl.GL2ES2
import com.jogamp.opengl.util.glsl.ShaderProgram
import javax.media.opengl.GL4
import javax.media.opengl.GL3ES3
import javax.media.opengl.GL3

class HeightMapProgram(gl: GL4) extends GLProgram {
  
    init();
    
	private def init(): Unit = {
		targetGL = gl;
		
	  	val createVertexShader = ShaderLoader.createShader(gl, GL2ES2.GL_VERTEX_SHADER, this.getClass())_;
		val createFragmentShader = ShaderLoader.createShader(gl, GL2ES2.GL_FRAGMENT_SHADER, this.getClass())_;
		val createGeometryShader = ShaderLoader.createShader(gl, GL3.GL_GEOMETRY_SHADER, this.getClass())_;
		
		val vShader: ShaderCode = createVertexShader("shaders/heightvshader.glsl");
		if(vShader.compile(gl, System.out) == false)
			throw new Exception("Failed to compile vertex shader");
		
		val fShader: ShaderCode = createFragmentShader("shaders/heightfshader.glsl");
		if(fShader.compile(gl, System.out) == false)
			throw new Exception("Failed to compile fragment shader");
		
		val gShader: ShaderCode = createGeometryShader("shaders/heightgshader.glsl");
		if(gShader.compile(gl, System.out) == false)
			throw new Exception("Failed to compile geometry shader");
		
		program = ShaderLoader.createProgram(gl, vShader, gShader, fShader);
		if(program.link(gl, System.out) == false)
			throw new Exception("Failed to link program");
		
		//No longer needed
		vShader.destroy(gl);
		fShader.destroy(gl);	
	}
	
	override def getVerticesIndex() = 0;
	override def getTexturesIndex() = 1;
	override def getNormalsIndex() = -1;
	override def getColorsIndex() = 2;
	override def getModelViewIndex() = 3;
	override def getITModelViewIndex() = 4;
	override def getProjectionIndex() = 5;
}