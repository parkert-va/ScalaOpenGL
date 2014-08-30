package net.virtuallyabstract.demo1

import com.jogamp.opengl.util.glsl.ShaderCode
import javax.media.opengl.GL2ES2
import com.jogamp.opengl.util.glsl.ShaderProgram
import javax.media.opengl.GL4

class FixedLightProgram(gl: GL4) extends GLProgram {
  
    init();
    
	private def init(): Unit = {
		targetGL = gl;
		
	  	val createVertexShader = ShaderLoader.createShader(gl, GL2ES2.GL_VERTEX_SHADER, this.getClass())_;
		val createFragmentShader = ShaderLoader.createShader(gl, GL2ES2.GL_FRAGMENT_SHADER, this.getClass())_;
		
		val vShader: ShaderCode = createVertexShader("shaders/vshader.glsl");
		if(vShader.compile(gl, System.out) == false)
			throw new Exception("Failed to compile vertex shader");
		
		val fShader: ShaderCode = createFragmentShader("shaders/fshader.glsl");
		if(fShader.compile(gl, System.out) == false)
			throw new Exception("Failed to compile fragment shader");
		
		program = ShaderLoader.createProgram(gl, vShader, fShader);
		if(program.link(gl, System.out) == false)
			throw new Exception("Failed to link program");
		
		//No longer needed
		vShader.destroy(gl);
		fShader.destroy(gl);	
	}
	
	override def getVerticesIndex() = 0;
	override def getNormalsIndex() = 1;
	override def getColorsIndex() = 2;
	override def getModelViewIndex() = 3;
	override def getITModelViewIndex() = 4;
	override def getProjectionIndex() = 5;
}