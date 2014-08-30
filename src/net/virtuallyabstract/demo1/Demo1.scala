package net.virtuallyabstract.demo1

import javax.media.opengl._
import javax.media.opengl.awt._
import javax.media.opengl.fixedfunc._
import com.jogamp.newt.event.awt._
import com.jogamp.opengl.util._
import com.jogamp.opengl.util.glsl._;
import java.util._
import java.awt._
import java.awt.event._
import javax.swing._
import scala.io._
import java.nio._;


object BufferUtils
{
	def createBuffer(gl: GL4): Int = {
		val bufferArray: IntBuffer = IntBuffer.allocate(1);					
		
		gl.glGenBuffers(1, bufferArray);
		
		return bufferArray.get();
	}
	
	def createVAO(gl: GL4): Int = {
  		val vaoArray: IntBuffer = IntBuffer.allocate(1);							
  		
		gl.glGenVertexArrays(1, vaoArray);		
		
		return vaoArray.get();
	}
	
	def setBufferData(gl: GL4, bufferID: Int, data: FloatBuffer): Unit = {
		//glBindBuffer sets the active array buffer to be manipulated.
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, bufferID);
		
		//Using GLBuffers utility class to provide the size of the float primitive so I can calculate the size
		val size: Long = data.limit() * GLBuffers.sizeOfGLType(GL.GL_FLOAT);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, size, data, GL.GL_DYNAMIC_DRAW);
	}
	
	def addVertex(vertices: FloatBuffer)(x: Float, y: Float, z: Float): Unit = {
		vertices.put(x);
		vertices.put(y);
		vertices.put(z);	
		vertices.put(1.0f);
	}
}

class Demo1 extends GLEventListener
{
	var program: GLProgram = null;
	var cube: Cube = null;
	var mvp: PMVMatrix = null;
	
	override def init(drawable: GLAutoDrawable): Unit = {
		val gl: GL4 = drawable.getGL().getGL4();
		
		//Enable culling, by default only triangles with vertices ordered in a counter clockwise order will be rendered
		gl.glEnable(GL.GL_CULL_FACE);		
		
		try
		{
		    program = new FixedLightProgram(gl);
			
			cube = new Cube(gl);
			
			mvp = new PMVMatrix();
		}
		catch
		{
			case e: Exception => {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	override def display(drawable: GLAutoDrawable): Unit = {
		val gl: GL4 = drawable.getGL().getGL4();

		val blackArray: Array[Float] = Array(0.0f, 0.0f, 0.0f, 1.0f);
		gl.glClearBufferfv(GL2ES3.GL_COLOR, 0, blackArray, 0);
		
		program.activate();
						
		val time: Long = System.currentTimeMillis();
		val degrees: Float = ((time / 10) % 360);
		
		cube.reset();
		cube.rotateXYZ(degrees, 1.0f, 1.0f, 0.0f);
		cube.move(0.0f, 0.0f, -5.0f);
		
		mvp.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
		mvp.glLoadIdentity();
		//90 degree field of view, aspect ratio of 1.0 (since window width = height), zNear 1.0f, zFar 20.0f
		mvp.gluPerspective(90.0f, 1.0f, 1.0f, 20.0f);
		mvp.update();
		
		//Projection matrix
		gl.glUniformMatrix4fv(program.getProjectionIndex(), 1, false, mvp.glGetMatrixf());
			
		cube.draw(program);
	}

	override def reshape(drawable: GLAutoDrawable, x: Int, y: Int, width: Int, height: Int): Unit = {
	}

	override def dispose(drawable: GLAutoDrawable): Unit = {
  		val gl: GL4 = drawable.getGL().getGL4();
  		
		if(program != null)
		{
			program.destroy();
		}
	}
}

object EntryPoint {
	def main(args: Array[String]): Unit = {
		val frameMain: Frame = new Frame("Main Window");
		frameMain.setLayout(new BorderLayout());

		val animator: Animator = new Animator();
		frameMain.addWindowListener(new WindowAdapter() {
			override def windowClosing(e: WindowEvent) = {
				System.exit(0);
			}
		});

		val canvas: GLCanvas = new GLCanvas();
		animator.add(canvas);

		canvas.addGLEventListener(new Demo1());

		frameMain.setSize(600, 600);
		frameMain.setLocation(200, 200);
		frameMain.add(canvas, BorderLayout.CENTER);
		frameMain.validate();
		frameMain.setVisible(true);
		
		animator.start();
	}
}