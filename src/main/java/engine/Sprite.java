package engine;

import engine.utils.ResourceManager;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Sprite {

    private final int vaoId;
    private final int vboId;
    private final int vertexCount;
    private Texture texture;

    private Vector2f position;
    private Vector2f size;
    private float rotation;
    private Vector3f color;

    public Sprite(float x, float y, int width, int height, float rotation, Vector3f color) {

        position = new Vector2f(x, y);
        size = new Vector2f(width, height);
        this.rotation = rotation;
        this.color = color;

        try {
            texture = new Texture();
            ByteBuffer buffer = ResourceManager.getInstance().loadTextureFromFile("/textures/test.png");
            texture.generate(256, 256, buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }

        float[] vertices = new float[]{
                0.0f, 1.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 0.0f,

                0.0f, 1.0f, 0.0f, 1.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                1.0f, 0.0f, 1.0f, 0.0f
        };

        vertexCount = vertices.length;

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
    }

    public void draw() {

        glActiveTexture(GL_TEXTURE0);
        texture.bind();
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
        glDisableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboId);

        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getSize() {
        return size;
    }

    public float getRotation() {
        return rotation;
    }
    public Vector3f getColor() {
        return color;
    }
}
