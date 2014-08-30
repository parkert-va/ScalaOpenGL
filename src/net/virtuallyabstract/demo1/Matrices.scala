package net.virtuallyabstract.demo1

import com.jogamp.opengl.util.PMVMatrix
import javax.media.opengl.fixedfunc.GLMatrixFunc
import scala.collection.mutable.ArrayBuffer
import java.nio.FloatBuffer

class Matrices {
	val pmv: PMVMatrix = new PMVMatrix();
	val transforms: ArrayBuffer[(PMVMatrix) => Unit] = new ArrayBuffer[(PMVMatrix) => Unit]();
	var dirty = true;
	
	//We actually want to play back the transforms in reverse order so we will prepend them to the current
	//transformation list. 
	def pushTransform(transform: (PMVMatrix) => Unit): Unit = {
		transform +=: transforms;
		dirty = true;
	}
	
	def reset(): Unit = {
		transforms.clear();
		dirty = true;
	}
	
	private def applyTransforms(): Unit = {
	    if(!dirty)
	    	return;
	    
		pmv.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
		pmv.glLoadIdentity();
		
		for(transform <- transforms)
		{
			transform(pmv);
		}
		
		dirty = false;
	}
	
	/**
	 * Obtain the default modelview matrix with all transforms applied.
	 */
	def getModelView(): FloatBuffer = {
		applyTransforms();
		
		return pmv.glGetMatrixf();
	}
	
	/**
	 * Obtain the intverse transpose modelview matrix with all transforms applied.  
	 * Useful for normal transformations.
	 */
	def getITModelView(): FloatBuffer = {
		applyTransforms();
		
		return pmv.glGetMvitMatrixf();
	}
}