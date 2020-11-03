package com.fuzzyrock.flappy.level;

import com.fuzzyrock.flappy.graphics.Shader;
import com.fuzzyrock.flappy.graphics.Texture;
import com.fuzzyrock.flappy.graphics.VertexArray;
import com.fuzzyrock.flappy.input.Input;
import com.fuzzyrock.flappy.math.Matrix4f;
import com.fuzzyrock.flappy.math.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Bird {
    private float SIZE = 1.0f;
    private VertexArray mesh;
    private Texture texture;

    private Vector3f position = new Vector3f();
    private float rot;
    private float yDelta;

    public Bird() {
        float[] vertices = new float[] {
            -SIZE/2.0f, -SIZE/2.0f, 0.2f,
            -SIZE/2.0f, SIZE/2.0f, 0.2f,
            SIZE/2.0f, SIZE/2.0f, 0.2f,
            SIZE/2.0f, -SIZE/2.0f, 0.2f
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
        position.y -= yDelta;

        if (Input.isKeyDown(GLFW_KEY_SPACE)) {
            yDelta = -0.15f;
        } else {
            yDelta += 0.01f;
        }

        rot = -yDelta * 90.0f;
    }

    public void render() {
        Shader.BIRD.enable();
        Shader.BIRD.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.rotate(rot)));
        texture.bind();
        mesh.render();
        Shader.BIRD.disable();
        texture.unbind();
    }
}
