package net.virtuallyabstract.demo1

import com.jogamp.opengl.util.PMVMatrix

trait Rotatable extends Matrices {
	def rotateX(degrees: Float): Unit = {
		super.pushTransform(
		    (pmv: PMVMatrix) => {
		    	pmv.glRotatef(degrees, 1.0f, 0.0f, 0.0f);
		    }
		);
	}
	
	def rotateY(degrees: Float): Unit = {
		super.pushTransform(
		    (pmv: PMVMatrix) => {
		    	pmv.glRotatef(degrees, 0.0f, 1.0f, 0.0f);
		    }
		);
	}
	
	def rotateZ(degrees: Float): Unit = {
		super.pushTransform(
		    (pmv: PMVMatrix) => {
		    	pmv.glRotatef(degrees, 0.0f, 0.0f, 1.0f);
		    }
		);
	}
	
	def rotateXYZ(degrees: Float, x: Float, y: Float, z: Float): Unit = {
		super.pushTransform(
		    (pmv: PMVMatrix) => {
		    	pmv.glRotatef(degrees, x, y, z);
		    }
		);
	}
}