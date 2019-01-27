package game;

import engine.GameObject;
import engine.ShaderProgram;
import engine.utils.ResourceManager;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Renderer {

    private ShaderProgram shaderProgram;

    private Matrix4f projection = new Matrix4f().ortho2D(0, BreakoutGame.WIDTH, BreakoutGame.HEIGHT, 0);

    public void init() {
        try {
            shaderProgram = new ShaderProgram();
            shaderProgram.createVertexShader(ResourceManager.getInstance().loadStringFromFile("/shaders/vertex.glsl"));
            shaderProgram.createFragmentShader(ResourceManager.getInstance().loadStringFromFile("/shaders/fragment.glsl"));
            shaderProgram.link();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void render(ArrayList<GameObject> gameObjects) {
        for (GameObject gameObject : gameObjects) {
            if (!gameObject.isDestroyed()) {
                shaderProgram.bind();
                try {
                    shaderProgram.createUniform("projectionMatrix");
                    shaderProgram.setUniform("projectionMatrix", projection);

                    Matrix4f modelMatrix = new Matrix4f()
                            .identity()
                            .translate(gameObject.getPosition().x, gameObject.getPosition().y, 0)
                            .rotate(gameObject.getRotation(), 0, 0, 1) // TODO fix rotation RAD/DEG
                            .scale(gameObject.getSize().x, gameObject.getSize().y, 0);

                    shaderProgram.createUniform("modelMatrix");
                    shaderProgram.setUniform("modelMatrix", modelMatrix);
                    shaderProgram.createUniform("spriteColor");
                    shaderProgram.setUniform("spriteColor", gameObject.getColor());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                gameObject.draw();

                shaderProgram.unbind();
            }
        }
    }

    public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }
    }
}
