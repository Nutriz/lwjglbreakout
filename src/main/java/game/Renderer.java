package game;

import engine.GameObject;
import engine.ShaderProgram;
import engine.utils.ResourceManager;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11C.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Renderer {

    private ShaderProgram shaderProgram;

    private Matrix4f projection = new Matrix4f().ortho2D(0, BreakoutGame.WIDTH, BreakoutGame.HEIGHT, 0);
    private int vaoId;
    private int vboId;

    public void init() {

        float[] vertices = new float[]{
                0.0f, 1.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 0.0f,

                0.0f, 1.0f, 0.0f, 1.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                1.0f, 0.0f, 1.0f, 0.0f
        };

        FloatBuffer verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
        verticesBuffer.put(vertices).flip();

        // VAO
        vaoId = glGenVertexArrays(); // creates  vertex array object name (int)
        glBindVertexArray(vaoId); // activates this vertex array object

        // VBO
        vboId = glGenBuffers(); // creates buffer name (int)
        glBindBuffer(GL_ARRAY_BUFFER, vboId); // activates this buffer on GPU and say it's a Vertex attributes
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW); // initializes a buffer object's data store

        glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0); // define an array of generic vertex attribute data

        // Unbind the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        // Unbind the VAO
        glBindVertexArray(0);

        if (verticesBuffer != null) {
            MemoryUtil.memFree(verticesBuffer);
        }


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
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        shaderProgram.bind();

//        sprite.draw();
        for (GameObject gameObject : gameObjects) {

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
//                shaderProgram.createUniform("spriteColor");
//                shaderProgram.setUniform("spriteColor", gameObject.getColor());
            } catch (Exception e) {
                e.printStackTrace();
            }

            this.draw(gameObject);
        }
        shaderProgram.unbind();
    }

    public void draw(GameObject gameObject) {

        glActiveTexture(GL_TEXTURE0);
        gameObject.getTexture().bind();
        // Bind to the VAO
        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);
        // Draw the vertices
        glDrawArrays(GL_TRIANGLES, 0, 6);

        // Restore state
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
    }


    public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }

        glDisableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboId);

        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }
}
