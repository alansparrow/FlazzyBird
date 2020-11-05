package com.fuzzyrock.flappy.level;

import java.util.Random;

import com.fuzzyrock.flappy.graphics.Shader;
import com.fuzzyrock.flappy.graphics.Texture;
import com.fuzzyrock.flappy.graphics.VertexArray;
import com.fuzzyrock.flappy.math.Matrix4f;
import com.fuzzyrock.flappy.math.Vector3f;
import com.fuzzyrock.flappy.utils.Constants;

public class Level {
    private final float SCROLL_RATE = 0.03f;

    private VertexArray background;
    private VertexArray fade;

    private Texture bgTexture;

    private float xScroll = 0.0f;

    private Bird bird;

    private final int PIPE_COUNT = 5;

    private Pipe[] pipes = new Pipe[PIPE_COUNT * 2];

    private int index = 0;

    private  Random random = new Random();

    private float time = 0.0f;

    private final float SCREEN_OFFSET = Constants.SCREEN_RIGHT / 2.0f;

    private final float PIPE_GAP = 11.5f;

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
        fade = new VertexArray(6);

        bird = new Bird();
        createPipes();
    }

    private void createPipes() {
        Pipe.create();
        for (int i = 0; i < PIPE_COUNT * 2; i += 2) {
            pipes[i] = new Pipe(SCREEN_OFFSET + index * 3.0f, random.nextFloat() * 4.0f);
            pipes[i + 1] = new Pipe(pipes[i].getX(), pipes[i].getY() - PIPE_GAP);
            index += 2;
        }
    }

    private boolean collision() {
        float bx0 = bird.getX() - bird.getSize() / 2;
        float bx1 = bird.getX() + bird.getSize() / 2;
        float by0 = bird.getY() - bird.getSize() / 2;
        float by1 = bird.getY() + bird.getSize() / 2;

        for (int i = 0; i < PIPE_COUNT * 2; i++) {
            float px0 = pipes[i].getX();
            float px1 = pipes[i].getX() + Pipe.getWidth();
            float py0 = pipes[i].getY();
            float py1 = pipes[i].getY() + Pipe.getHeight();

            if (bx1 > px0 && bx0 < px1) {
                if (by1 > py0 && by0 < py1) {
                    return true;
                }
            }
        }

        return false;
    }

    private void updatePipes() {        
        for (int i = 0; i < PIPE_COUNT * 2; i += 2) {
            float xPos = pipes[i].getModelMatrix().elements[0 + 3 * 4];
            if (xPos < Constants.SCREEN_LEFT * 1.2f) {
                int lastPipeIdx = (i + 8) % 10;
                float lastPipeXPos = pipes[lastPipeIdx].getModelMatrix().elements[0 + 3 * 4];

                pipes[i] = new Pipe(lastPipeXPos + 2 * 3.0f, random.nextFloat() * 4.0f);
                pipes[i + 1] = new Pipe(pipes[i].getX(), pipes[i].getY() - PIPE_GAP);
            } else {
                pipes[i].setModelMatrix(pipes[i].getModelMatrix().multiply(Matrix4f.translate(new Vector3f(-SCROLL_RATE, 0.0f, 0.0f))));
                pipes[i + 1].setModelMatrix(pipes[i + 1].getModelMatrix().multiply(Matrix4f.translate(new Vector3f(-SCROLL_RATE, 0.0f, 0.0f))));
            }
        }
    }

    public void update() {
        if (bird.getControl()) {
            xScroll -= SCROLL_RATE;
            if (xScroll <= -10.f) {
                xScroll = 0.0f;
            }
            updatePipes();
        }

        bird.update();

        if (bird.getControl()) {
            float by1 = bird.getY() + bird.getSize() / 2;
            if (collision() || (by1 < Constants.SCREEN_BOTTOM * 1.2))
            {
                bird.fall();
                bird.setControl(false);
            }
        }

        time += 0.01f;
    }

    private void renderPipes() {
        Shader.PIPE.enable();
        Shader.PIPE.setUniform2f("bird", 0.0f, bird.getY());
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
        Shader.BG.setUniform2f("bird", 0.01f, bird.getY());

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

        Shader.FADE.enable();
        Shader.FADE.setUniform1f("time", time);
        fade.render();
        Shader.FADE.disable();
    }

    public boolean isGameOver() {
        return !bird.getControl();
    }
}
