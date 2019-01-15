package game;

import engine.ShaderProgram;
import engine.Sprite;
import engine.utils.ResourceManager;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11C.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL15.glClear;

public class Renderer {

    private ShaderProgram shaderProgram;

    private Matrix4f projection = new Matrix4f().ortho2D(0, BreakoutGame.WIDTH, BreakoutGame.HEIGHT, 0);
    private Sprite sprite;

    public void init() {

        sprite = new Sprite(200, 200, 100, 100, 0, new Vector3f(0, 0, 1));

        try {
            shaderProgram = new ShaderProgram();
            shaderProgram.createVertexShader(ResourceManager.getInstance().loadStringFromFile("/shaders/vertex.glsl"));
            shaderProgram.createFragmentShader(ResourceManager.getInstance().loadStringFromFile("/shaders/fragment.glsl"));
            shaderProgram.link();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        shaderProgram.bind();

        try {

            shaderProgram.createUniform("projectionMatrix");
            shaderProgram.setUniform("projectionMatrix", projection);

            Matrix4f modelMatrix = new Matrix4f()
                    .identity()
                    .translate(sprite.getPosition().x, sprite.getPosition().y, 0)
                    .rotate(sprite.getRotation(), 0, 0, 1) // TODO fix rotation RAD/DEG
                    .scale(sprite.getSize().x, sprite.getSize().y, 0);

            shaderProgram.createUniform("modelMatrix");
            shaderProgram.setUniform("modelMatrix", modelMatrix);
            shaderProgram.createUniform("spriteColor");
            shaderProgram.setUniform("spriteColor", sprite.getColor());
        } catch (Exception e) {
            e.printStackTrace();
        }

        sprite.draw();
        shaderProgram.unbind();
    }


    public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }

        sprite.cleanup();

    }
}
