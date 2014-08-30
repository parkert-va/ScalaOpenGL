package net.virtuallyabstract.demo1

import com.jogamp.opengl.util.glsl.ShaderProgram
import com.jogamp.opengl.util.glsl.ShaderCode
import javax.media.opengl.GL4

trait GLProgram {
  
    var program: ShaderProgram = null;
    var targetGL: GL4 = null;
  
	def getVerticesIndex(): Int;
	def getNormalsIndex(): Int;
	def getTexturesIndex(): Int;
	def getColorsIndex(): Int;
	def getModelViewIndex(): Int;
	def getITModelViewIndex(): Int;
	def getProjectionIndex(): Int;

	def activate(): Unit = {
		if(program == null || targetGL == null)
			return;
		
		program.useProgram(targetGL, true)
	}
	
	def deactivate(): Unit = {
	    if(program == null || targetGL == null)
	    	return;
	    
	    program.useProgram(targetGL, false);
	}
	
	def destroy(): Unit = {
	    if(program == null || targetGL == null)
	    	return;
	    
		program.destroy(targetGL);
	}
}

object ShaderLoader
{
	def createShader(gl: GL4, shaderType: Int, context: Class[_])(filename: String): ShaderCode = {
		ShaderCode.create(gl, shaderType, 1, context, Array(filename), false);
	}
	
	def createProgram(gl: GL4, shaders: ShaderCode*): ShaderProgram = {
		val retVal: ShaderProgram = new ShaderProgram();
		retVal.init(gl);
		
		for(shader <- shaders)
		{
			retVal.add(shader);
		}
		
		return retVal;
	}
}