package com.fuzzyrock.flappy.level;

import com.fuzzyrock.flappy.graphics.Texture;
import com.fuzzyrock.flappy.graphics.VertexArray;
import com.fuzzyrock.flappy.math.Vector3f;

public class Bird {
    private float SIZE = 1.0f;
    private VertexArray mesh;
    private Texture texture;

    private Vector3f position = new Vector3f();
    private float rot;
    private float yDelta;

    public Bird() {
        float[] vertices = new float[] {
            -SIZE/2.0f, -SIZE/2.0f,
            -SIZE/2.0f, SIZE/2.0f,
            SIZE/2.0f, SIZE/2.0f,
            SIZE/2.0f, -SIZE/2.0f
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

        mesh = new VertexArray(vertices, indices, tcs);
        texture = new Texture("res/bird.png");
    }

    public void update() {

    }

    public void render() {
        // Shader.BIRD.enable();
        texture.bind();
        mesh.render();
        // Shader.BIRD.disable();
    }
}
