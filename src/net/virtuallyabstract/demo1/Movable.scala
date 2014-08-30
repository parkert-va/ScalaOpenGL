package net.virtuallyabstract.demo1

import com.jogamp.opengl.util.PMVMatrix

trait Movable extends Matrices {
	def move(x: Float, y: Float, z: Float): Unit = {
		super.pushTransform(
		    (pmv: PMVMatrix) => {
		    	pmv.glTranslatef(x, y, z);
		    }
		);
	}
}