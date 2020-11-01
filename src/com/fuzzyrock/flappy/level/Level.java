package com.fuzzyrock.flappy.level;

import com.fuzzyrock.flappy.graphics.Shader;
import com.fuzzyrock.flappy.graphics.Texture;
import com.fuzzyrock.flappy.graphics.VertexArray;
import com.fuzzyrock.flappy.math.Matrix4f;
import com.fuzzyrock.flappy.math.Vector3f;

public class Level {
    private VertexArray background;

    private Texture bgTexture;

    private int xScroll = 0;
    private int map = 0;

    // for render2
    private float xScroll2 = 0.0f;

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
    }

    public void update() {
        xScroll--;
        if (-xScroll % 335 == 0) {
            map++;
        }

        // System.out.println("xScroll: " + xScroll);

        // for render2
        xScroll2 += 0.03f;
        if (xScroll2 >= 10.f) {
            xScroll2 = 0.0f;
        }
    }

    private void render1() {
        bgTexture.bind();
        background.bind();
        Shader.BG.enable();
        
        for (int i = map; i < map + 4; i++) {
            Shader.BG.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(i * 10 + xScroll * 0.03f, 0.0f, 0.0f)));
            // System.out.println("X translate: " + (i * 10 + xScroll * 0.03f) + " map: " + map);
            System.out.format("X translate: %-10.5f	map: %d%n", (i * 10 + xScroll * 0.03f), map);
            background.draw();
        }
        
        // example
        // Shader.BG.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(0.f, 0.0f, 0.0f)));        
        // background.draw();
        // Shader.BG.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(10.f, 0.0f, 0.0f)));
        // background.draw();

        Shader.BG.disable();
        bgTexture.unbind();
    }

    private void render2() {
        bgTexture.bind();
        background.bind();
        Shader.BG.enable();
        
        // System.out.println("====");
        for (int i = 0; i < 3; i++) {
            Shader.BG.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(i * 10 - xScroll2, 0.0f, 0.0f)));
            // System.out.format("X translate: %-10.5f xScroll2 %-10.5f%n", (i * 10 - xScroll2), xScroll2);
            background.draw();
        }
        

        Shader.BG.disable();
        bgTexture.unbind();
    }

    public void render() {
        // render1();
        render2();
    }
}
