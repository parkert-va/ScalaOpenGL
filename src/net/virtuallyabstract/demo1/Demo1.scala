package net.virtuallyabstract.demo1

import java.awt.BorderLayout
import java.awt.Frame
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.nio.FloatBuffer
import java.nio.IntBuffer
import com.jogamp.opengl.util.Animator
import com.jogamp.opengl.util.GLBuffers
import com.jogamp.opengl.util.PMVMatrix
import com.jogamp.opengl.util.texture.Texture
import com.jogamp.opengl.util.texture.TextureIO
import javax.media.opengl.GL
import javax.media.opengl.GL2ES3
import javax.media.opengl.GL4
import javax.media.opengl.GLAutoDrawable
import javax.media.opengl.GLEventListener
import javax.media.opengl.awt.GLCanvas
import javax.media.opengl.fixedfunc.GLMatrixFunc
import com.jogamp.opengl.util.texture.TextureData
import javax.media.opengl.GLCapabilities
import javax.media.opengl.GLProfile


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
	var heightProgram: GLProgram = null;
	var mapTexture: Texture = null;
	var cube: Cube = null;
	var grid: Grid = null;
	var mvp: PMVMatrix = null;
	
	override def init(drawable: GLAutoDrawable): Unit = {
		val gl: GL4 = drawable.getGL().getGL4();
		
		//Enable culling, by default only triangles with vertices ordered in a counter clockwise order will be rendered
		gl.glEnable(GL.GL_CULL_FACE);	
		gl.glEnable(GL.GL_DEPTH_TEST);
		//gl.glDepthFunc(GL.GL_LESS);
		
		try
		{
		    program = new FixedLightProgram(gl);
		    heightProgram = new HeightMapProgram(gl);
			
			cube = new Cube(gl);
			grid = new Grid(gl, 0.1f, 200, 0.1f, 200);
			//grid = new Grid(gl, 20.0f, 1, 20.0f, 1);
			
			mapTexture = TextureIO.newTexture(this.getClass().getResourceAsStream("resources/heightmap2.jpg"), false, TextureIO.JPG);
			
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
		gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
		
		program.activate();
						
		val time: Long = System.currentTimeMillis();
		val degrees: Float = ((time / 20) % 360);
		
		cube.reset();
		cube.rotateXYZ(degrees, 1.0f, 1.0f, 0.0f);
		cube.move(0.0f, 0.0f, -5.0f);
		
		mvp.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
		mvp.glLoadIdentity();
		//90 degree field of view, aspect ratio of 1.0 (since window width = height), zNear 1.0f, zFar 20.0f
		mvp.gluPerspective(90.0f, 1.0f, 1.0f, 30.0f);
		mvp.update();
		
		//Projection matrix
		gl.glUniformMatrix4fv(program.getProjectionIndex(), 1, false, mvp.glGetMatrixf());
			
		//cube.draw(program);
		
		cube.reset();
		cube.rotateXYZ(degrees, 0.0f, 1.0f, 0.0f);
		cube.move(2.0f, 0.0f, -5.0f);
		
		//cube.draw(program);
		
		program.deactivate();
		heightProgram.activate();
		
		grid.reset();
		grid.move(-10.0f, 0.0f, 10.0f); 
		grid.rotateY(degrees);
		grid.move(0.0f, -5.0f, -15.0f);
		
		gl.glUniformMatrix4fv(heightProgram.getProjectionIndex(), 1, false, mvp.glGetMatrixf());
		
		gl.glUniform4f(heightProgram.getColorsIndex(), 0.0f, 1.0f, 1.0f, 1.0f);
		mapTexture.bind(gl);
		
		grid.draw(heightProgram);
		
		heightProgram.deactivate();
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
		
		val config: GLCapabilities = new GLCapabilities(GLProfile.getDefault());
		config.setDepthBits(8);

		val canvas: GLCanvas = new GLCanvas(config);
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