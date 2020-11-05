package com.fuzzyrock.flappy.graphics;

import com.fuzzyrock.flappy.math.Matrix4f;
import com.fuzzyrock.flappy.math.Vector3f;
import com.fuzzyrock.flappy.utils.ShaderUtils;

import static org.lwjgl.opengl.GL20.*;

import java.util.HashMap;
import java.util.Map;

public class Shader {

    public static final int VERTEX_ATTRIB = 0;
    public static final int TCOORD_ATTRIB = 1;

    public static Shader BG;
    public static Shader BIRD;
    public static Shader PIPE;
    public static Shader FADE;

    private final int ID;
    private Map<String, Integer> locationCache = new HashMap<String, Integer>();

    private boolean isEnabled;

    public Shader(String vertex, String fragment) {
        ID = ShaderUtils.load(vertex, fragment);
        isEnabled = false;
    }

    public static void loadAll() {
        BG = new Shader("shaders/bg.vert", "shaders/bg.frag");
        BIRD = new Shader("shaders/bird.vert", "shaders/bird.frag");
        PIPE = new Shader("shaders/pipe.vert", "shaders/pipe.frag");
        FADE = new Shader("shaders/fade.vert", "shaders/fade.frag");
    }

    public int getUniform(String name) {
        if (locationCache.containsKey(name)) {
            return locationCache.get(name);
        }

        int result = glGetUniformLocation(ID, name);
        if (result == -1) {
            System.err.println("Could not find uniform variable '" + name + "'!");
            new Exception().printStackTrace();
        } else {
            locationCache.put(name, result);
        }

        return result;
    }

    public void setUniform1i(String name, int value) {
        if (!isEnabled) enable();
        glUniform1i(getUniform(name), value);
    }

    public void setUniform1f(String name, float value) {
        if (!isEnabled) enable();
        glUniform1f(getUniform(name), value);
    }

    public void setUniform2f(String name, Float value1, float value2) {
        if (!isEnabled) enable();
        glUniform2f(getUniform(name), value1, value2);
    }

    public void setUniform3f(String name, Vector3f vector) {
        if (!isEnabled) enable();
        glUniform3f(getUniform(name), vector.x, vector.y, vector.z);
    }

    public void setUniformMat4f(String name, Matrix4f matrix) {
        if (!isEnabled) enable();
        glUniformMatrix4fv(getUniform(name), false, matrix.toFloatBuffer());
    }

    public void enable() {
        glUseProgram(ID);
        isEnabled = true;
    }

    public void disable() {
        glUseProgram(0);
        isEnabled = false;
    }

}
