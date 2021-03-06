package com.fuzzyrock.flappy.level;

import com.fuzzyrock.flappy.graphics.Shader;
import com.fuzzyrock.flappy.graphics.Texture;
import com.fuzzyrock.flappy.graphics.VertexArray;
import com.fuzzyrock.flappy.input.Input;
import com.fuzzyrock.flappy.math.Matrix4f;
import com.fuzzyrock.flappy.math.Vector3f;
import com.fuzzyrock.flappy.utils.Constants;

import static org.lwjgl.glfw.GLFW.*;

public class Bird {
    private float SIZE = 1.0f;
    private VertexArray mesh;
    private Texture texture;

    private Vector3f position = new Vector3f();
    private float rot;
    private float yDelta;

    private boolean control = true;

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
        if (position.y >= Constants.SCREEN_TOP - SIZE / 2) {
            position.y = Constants.SCREEN_TOP - SIZE / 2;
        }

        
        if (Input.isKeyDown(GLFW_KEY_SPACE)) {
            if (control) {
                // yDelta = -0.1f;  // easy
                yDelta = -0.15f;  // hard
            } 
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

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

	public float getSize() {
		return SIZE;
    }
    
    public void fall() {
        yDelta = -0.15f;
    }

    public void setControl(boolean enabled) {
        control = enabled;
    }

    public boolean getControl() {
        return control;
    }
}
