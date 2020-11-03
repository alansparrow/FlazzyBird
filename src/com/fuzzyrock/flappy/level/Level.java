package com.fuzzyrock.flappy.level;

import com.fuzzyrock.flappy.graphics.Shader;
import com.fuzzyrock.flappy.graphics.Texture;
import com.fuzzyrock.flappy.graphics.VertexArray;
import com.fuzzyrock.flappy.math.Matrix4f;
import com.fuzzyrock.flappy.math.Vector3f;

public class Level {
    private VertexArray background;

    private Texture bgTexture;

    private int map = 0;

    // for render2
    private float xScroll = 0.0f;

    private Bird bird;

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
    }

    public void update() {
        xScroll += 0.03f;
        if (xScroll >= 10.f) {
            xScroll = 0.0f;
        }

        bird.update();
    }

    public void render() {
        bgTexture.bind();
        background.bind();
        Shader.BG.enable();
        // System.out.println("====");
        for (int i = 0; i < 3; i++) {
            Shader.BG.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(i * 10 - xScroll, 0.0f, 0.0f)));
            // System.out.format("X translate: %-10.5f xScroll %-10.5f%n", (i * 10 - xScroll), xScroll);
            background.draw();
        }
        Shader.BG.disable();
        bgTexture.unbind();

        bird.render();
    }
}
