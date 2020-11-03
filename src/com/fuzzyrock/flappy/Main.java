package com.fuzzyrock.flappy;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

import com.fuzzyrock.flappy.graphics.Shader;
import com.fuzzyrock.flappy.input.Input;
import com.fuzzyrock.flappy.level.Constants;
import com.fuzzyrock.flappy.level.Level;
import com.fuzzyrock.flappy.math.Matrix4f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

public class Main implements Runnable {

	private int width = 1280;
	private int height = 720;

	private Thread thread;
	private boolean running = false;

	private long window;

	private Level level;

	public void start() {
		running = true;
		thread = new Thread(this, "Game");
		thread.start();
	}

	private void init() {
		if (glfwInit() != true) {
			// TODO: handle it
			return;
		} 

		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
		window = glfwCreateWindow(width, height, "Flappy", NULL, NULL);
		if (window == NULL) {
			// TODO: handle it
			return;
		}

		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

		glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);
		glfwSetKeyCallback(window, new Input());
		glfwMakeContextCurrent(window);
		glfwShowWindow(window);
		
		GL.createCapabilities();

		glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		glEnable(GL_DEPTH_TEST);
		glActiveTexture(GL_TEXTURE1);
		System.out.println("OpenGL: " + glGetString(GL_VERSION));
		Shader.loadAll();

		Matrix4f pr_matrix = Matrix4f.orthographic(Constants.SCREEN_LEFT
												, Constants.SCREEN_RIGHT
												, Constants.SCREEN_BOTTOM
												, Constants.SCREEN_TOP
												, Constants.SCREEN_NEAR
												, Constants.SCREEN_FAR);
		
		// for BG
		Shader.BG.setUniformMat4f("pr_matrix", pr_matrix);
		Shader.BG.setUniform1i("tex", 1);

		// for BIRD
		Shader.BIRD.setUniformMat4f("pr_matrix", pr_matrix);
		Shader.BIRD.setUniform1i("tex", 1);

		// for PIPE
		Shader.PIPE.setUniformMat4f("pr_matrix", pr_matrix);
		Shader.PIPE.setUniform1i("tex", 1);

		level = new Level();
	}

	public void run() {
		init();

		long lastTime = System.nanoTime();
		double delta = 0.0;
		double ns = 1000000000.0 / 60.0; // how many nano seconds per frame (60 frames / s)
		long timer = System.currentTimeMillis();
		int updates = 0;
		int frames = 0;

		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			// System.out.format("delta %-10.5f	accDelta %-10.5f%n", (now - lastTime) / ns, delta);
			lastTime = now;
			if (delta >= 1.0) {
				update();
				updates++;
				// delta--;
				delta -= 1.01;
			}
			render();
			frames++;

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println(updates + " ups, " + frames + " fps");
				updates = 0;
				frames = 0;
			}

			if (glfwWindowShouldClose(window) == true) {
				running = false;
			}
		}

		glfwDestroyWindow(window);
		glfwTerminate();
	}

	private void update() {
		glfwPollEvents();
		if (Input.keys[GLFW_KEY_SPACE]) {
			System.out.println("FLAP!");
		}

		level.update();
	}

	private void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		level.render();

		int errorCode = glGetError();
		if (errorCode != GL_NO_ERROR) {
			System.err.println("OpenGL error: " + errorCode);
		}

		glfwSwapBuffers(window);		
	}

	public static void main(String[] args) {
		// Entry point
		new Main().start();
	}

}
