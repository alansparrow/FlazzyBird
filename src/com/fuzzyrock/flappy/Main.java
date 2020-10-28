package com.fuzzyrock.flappy;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

import com.fuzzyrock.flappy.input.Input;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

public class Main implements Runnable {

	private int width = 1280;
	private int height = 720;

	private Thread thread;
	private boolean running = false;

	private long window;

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
		System.out.println("OpenGL: " + glGetString(GL_VERSION));

		
	}

	public void run() {
		init();
		while (running) {
			update();
			render();

			if (glfwWindowShouldClose(window) == true) {
				running = false;
			}
		}
	}

	private void update() {
		glfwPollEvents();
		if (Input.keys[GLFW_KEY_SPACE]) {
			System.out.println("FLAP!");
		}
	}

	private void render() {
		glClear(GL_COLOR_BUFFER_BIT);
		glfwSwapBuffers(window);		
	}

	public static void main(String[] args) {
		// Entry point
		new Main().start();
	}

}
