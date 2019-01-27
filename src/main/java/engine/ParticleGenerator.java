package engine;

import game.BallObject;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class ParticleGenerator {

    private ArrayList<Particle> particles = new ArrayList<>(500);
    private int amount;
    private Texture texture;
    private int vaoId;
    private int vboId;

    public ParticleGenerator(Texture texture, int amount) {
        this.amount = amount;
        this.texture = texture;

        this.init();
    }

    private void init() {

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

        MemoryUtil.memFree(verticesBuffer);
    }

    public void update(float deltaTime, BallObject ball, int count, Vector2f offset) {
        for (int i = 0; i < count; i++) {
            this.spawnNewParticle(ball, offset);
        }

        for (Particle particle : particles) {
            particle.life -= deltaTime;
            if (particle.life > 0) {
                particle.setPosition(new Vector2f(particle.getPosition()).sub(particle.velocity.x * deltaTime, particle.velocity.y * deltaTime));
                particle.getColor().w -= deltaTime * 5f;
            }
        }
    }

    private void spawnNewParticle(BallObject ball, Vector2f offset) {
//        GLfloat random = ((rand() % 100) - 50) / 10.0f;
//        GLfloat rColor = 0.5 + ((rand() % 100) / 100.0f);
//        particle.Position = object.Position + random + offset;
//        particle.Color = glm::vec4(rColor, rColor, rColor, 1.0f);
//        particle.Life = 1.0f;
//        particle.Velocity = object.Velocity * 0.1f;

        float random = (float) Math.random();
        Vector2f position = new Vector2f(ball.getPosition()).add(random*10, random*10);
        Vector4f color = new Vector4f (random, random, random, 1);
        Vector2f velocity = new Vector2f(ball.velocity).mul(0.1f);
        Particle particle = new Particle(position, velocity, color, 2);
        particles.add(particle);
    }

    public void draw() {
        if (texture != null) {
            // Activate firs texture bank
            glActiveTexture(GL_TEXTURE0);
            // Bind the texture
            glBindTexture(GL_TEXTURE_2D, texture.getId());
        }

        // Bind to the VAO
        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);

        // Draw the vertices
        glDrawArrays(GL_TRIANGLES, 0, 6);

        // Restore state
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public ArrayList<Particle> getParticles() {
        return particles;
    }

    public class Particle {
        private Vector2f position;
        private Vector2f velocity;
        private Vector4f color;
        private float life;

        public Particle(Vector2f position, Vector2f velocity, Vector4f color, float life) {
            this.position = position;
            this.velocity = velocity;
            this.color = color;
            this.life = life;
        }

        public Vector2f getPosition() {
            return position;
        }

        public void setPosition(Vector2f position) {
            this.position = position;
        }

        public Vector4f getColor() {
            return color;
        }

        public float getLife() {
            return life;
        }
    }
}
