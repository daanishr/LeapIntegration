package prototype;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.util.glu.GLU.*;

import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import com.leapmotion.leap.Controller;
/**
 * All OpenGL tasks relating to creating, translating and rendering handled here.
 * 
 *
 */
public class PrototypeGLContext extends Application {
	int vertexHandle;
	int colorHandle;
	Camera camera;
	public void Initialise()

	{


		camera = new Camera(70, (float)Display.getWidth()/ (float)Display.getHeight(), 0.001f, 800);
		//glMatrixMode(GL_PROJECTION);
		// 3D shape has a more realistic look with Perspective mode rather than Orthographic
		//gluPerspective(70f, 800f/600f, 1, 10);
		// render to the full size of the window
		//glViewport(0,0,Display.getWidth(), Display.getHeight());
		//glMatrixMode(GL_MODELVIEW);
		//glLoadIdentity();
		glEnable(GL_DEPTH_TEST);
		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_COLOR_ARRAY);
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(3*24);
		vertexBuffer.put(new float[]
				{
				-0.5f,  0.5f,  0.5f,
				0.5f,  0.5f,  0.5f,
				-0.5f, -0.5f,  0.5f,
				0.5f, -0.5f,  0.5f,
				0.5f,  0.5f,  0.5f,
				0.5f,  0.5f, -0.5f,
				0.5f, -0.5f,  0.5f,
				0.5f, -0.5f, -0.5f,
				0.5f,  0.5f, -0.5f,
				-0.5f,  0.5f, -0.5f,
				0.5f, -0.5f, -0.5f,
				-0.5f, -0.5f, -0.5f,
				-0.5f,  0.5f, -0.5f,
				-0.5f,  0.5f,  0.5f,
				-0.5f, -0.5f, -0.5f,
				-0.5f, -0.5f,  0.5f,
				-0.5f,  0.5f,  0.5f,
				0.5f,  0.5f,  0.5f,
				-0.5f,  0.5f, -0.5f,
				0.5f,  0.5f, -0.5f,
				-0.5f, -0.5f,  0.5f,
				0.5f, -0.5f,  0.5f,
				-0.5f, -0.5f, -0.5f,
				0.5f, -0.5f, -0.5f,
				});
		// set the pointer back to the start of buffer
		vertexBuffer.rewind();  // set the pointer back to the start of buffer
		FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(3*24);
		colorBuffer.put(new float[]
				{
				// Front face
				1, 0, 0,
				1, 1, 0,
				1, 0, 1,
				1, 0, 0,
				// Right face
				1, 1, 1,
				1, 1, 1,
				1, 1, 1,
				1, 1, 1,
				// Back face
				0, 1, 1,
				1, 0, 0,
				0, 1, 0,
				0, 1, 1,
				// Left face
				1, 0, 0,
				0, 1, 0,
				0, 0, 1,
				1, 0, 0,
				// Top face
				0, 1, 0,
				1, 0, 1,
				1, 0, 1,
				0, 1, 0,
				// Bottom face
				0, 0, 0,
				1, 0, 0,
				0, 0, 0,
				0, 0, 0
				});
		colorBuffer.rewind();
		// Allocate buffer space on the GPU and bind to the vertex and colour buffers created
		vertexHandle = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vertexHandle);
		glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		colorHandle = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, colorHandle);
		glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		// move the camera back in the z axis ( cube centre is drawn around origin)
		//glTranslatef(0, 0, -3); 
	}
	// method called 60 times per second
	public void render()
	{


		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		//rotation controlled by motion control device
		//glRotatef(1, leap.getAxis('z'), leap.getAxis('x'), 0);
		glBindBuffer(GL_ARRAY_BUFFER, vertexHandle);
		glVertexPointer(3, GL_FLOAT, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, colorHandle);
		glColorPointer(3, GL_FLOAT, 0, 0);
		glDrawArrays(GL_TRIANGLE_STRIP, 0, 24);
	}
	public void update(){

		SampleListener leap = new SampleListener();
		
		boolean forward = Keyboard.isKeyDown(Keyboard.KEY_W);
		boolean backward = Keyboard.isKeyDown(Keyboard.KEY_S); 
		boolean	left = Keyboard.isKeyDown(Keyboard.KEY_A);
		boolean right = Keyboard.isKeyDown(Keyboard.KEY_D);
		if(forward)
			camera.move(0.1f,1);
		if(backward)	
			camera.move(-0.1f,1);
		if(left)
			camera.move(-0.1f,0);//cam.rotateY(-0.1f);
		if(right)
			camera.move(0.1f,0);//cam.rotateY(0.1f);
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT))
			camera.rotateY(1f);
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
			camera.rotateY(-1f);
		if(Keyboard.isKeyDown(Keyboard.KEY_UP))
			camera.rotateX(1f);
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN))
			camera.rotateX(-1f);
		glClear(GL_COLOR_BUFFER_BIT);
		glLoadIdentity();
		camera.useView();
	}
	public void destroy()
	{
		// free memory taken up in the GPU
		glDeleteBuffers(vertexHandle);
		glDeleteBuffers(colorHandle);
		glDisableClientState(GL_VERTEX_ARRAY);
		glDisableClientState(GL_COLOR_ARRAY);
	}



	private static class Threads1 implements Runnable {

		Threads1() {
		}

		public void Output1() {
			new PrototypeGLContext();
		}

		public void run() {
			Output1();
		}
	}
	
	private static class Threads2 implements Runnable {

		Threads2() {
		}

		public void Output2() {
			SampleListener listener = new SampleListener();
			Controller controller = new Controller();
			controller.addListener(listener);
			System.out.println("Press Enter to quit...");
			try {
				System.in.read();
			} catch (IOException e) {
				e.printStackTrace();
			}
			controller.removeListener(listener);
		}

		public void run() {
			Output2();
		}
	}


	public static void main(String[] args) {
		Thread ob1 = new Thread(new Threads1());
		Thread ob2 = new Thread(new Threads2());
		ob1.start();
		ob2.start();

		System.out.println("Thread " + ob1.getName() + " is alive: " + ob1.isAlive());
		System.out.println("Thread " + ob2.getName() + " is alive: " + ob2.isAlive());

		try {
			System.out.println("Waiting for Threads to finish");
			ob1.join();
			ob2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Thread " + ob1.getName() + " is alive: " + ob1.isAlive());
		System.out.println("Thread " + ob2.getName() + " is alive: " + ob2.isAlive());

		System.out.println("Main Thread Exiting");
	}



}