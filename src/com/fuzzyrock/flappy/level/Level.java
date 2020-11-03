package com.fuzzyrock.flappy.level;

import java.util.Random;

import com.fuzzyrock.flappy.graphics.Shader;
import com.fuzzyrock.flappy.graphics.Texture;
import com.fuzzyrock.flappy.graphics.VertexArray;
import com.fuzzyrock.flappy.math.Matrix4f;
import com.fuzzyrock.flappy.math.Vector3f;

public class Level {
    private VertexArray background;

    private Texture bgTexture;

    private int map = 0;

    private float xScroll = 0.0f;

    private Bird bird;

    private final int PIPE_COUNT = 5;

    private Pipe[] pipes = new Pipe[PIPE_COUNT * 2];

    private int index = 0;

    private  Random random = new Random();

    public Level() {
        float[] vertices = new float[] {
            -10.0f, -10.0f * 9.0f / 16.0f, 0.0f,
            -10.0f, 10.0f * 9.0f / 16.0f, 0.0f,
            0.0f, 10.0f * 9.0f / 16.0f, 0.0f,
            0.0f, -10.0f * 9.0f / 16.0f, 0.0f
        };

        byte[] indices = new byte[] {
            0, 1, 2,
            2, 3, 0
        };

        float[] tcs = new float[] {
            0, 1,
            0, 0,
            1, 0,
            1, 1
        };

        background = new VertexArray(vertices, indices, tcs);
        bgTexture = new Texture("res/bg.jpeg");

        bird = new Bird();
        createPipes();
    }

    private void createPipes() {
        Pipe.create();
        for (int i = 0; i < PIPE_COUNT * 2; i += 2) {
            pipes[i] = new Pipe(index * 3.0f, random.nextFloat() * 4.0f);
            pipes[i + 1] = new Pipe(pipes[i].getX(), pipes[i].getY() - 11.0f);
            index += 2;
        }
    }

    private void updatePipes() {

    }

    public void update() {
        xScroll -= 0.03f;
        if (xScroll <= -10.f) {
            xScroll = 0.0f;
        }

        bird.update();
    }

    private void renderPipes() {
        Shader.PIPE.enable();
        Shader.PIPE.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(xScroll, 0.0f, 0.0f)));
        Pipe.getTexture().bind();
        Pipe.getMesh().bind();

        for (int i = 0; i < PIPE_COUNT * 2; i++) {
            Shader.PIPE.setUniformMat4f("ml_matrix", pipes[i].getModelMatrix());
            Shader.PIPE.setUniform1i("top", i % 2 == 0 ? 1 : 0);
            Pipe.getMesh().draw();
        }

        Pipe.getTexture().unbind();
        Pipe.getMesh().unbind();
        Shader.PIPE.disable();
    }

    public void render() {
        bgTexture.bind();
        background.bind();
        Shader.BG.enable();
        // System.out.println("====");
        for (int i = 0; i < 3; i++) {
            Shader.BG.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(i * 10 + xScroll, 0.0f, 0.0f)));
            // System.out.format("X translate: %-10.5f xScroll %-10.5f%n", (i * 10 - xScroll), xScroll);
            background.draw();
        }
        Shader.BG.disable();
        bgTexture.unbind();

        renderPipes();  
        bird.render();
    }
}
