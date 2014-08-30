package net.virtuallyabstract.demo1

import com.jogamp.opengl.util.PMVMatrix

trait Scalable extends Matrices {
	def scale(x: Float, y: Float, z: Float): Unit = {
		super.pushTransform(
		    (pmv: PMVMatrix) => {
		    	pmv.glScalef(x, y, z);
		    }
		);
	}
}