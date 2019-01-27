package game;

import engine.GameObject;
import engine.ParticleGenerator;
import engine.ShaderProgram;
import engine.utils.ResourceManager;
import org.joml.Matrix4f;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {

    private ShaderProgram shaderProgram;
    private ShaderProgram particleShaderProgram;

    private Matrix4f projection = new Matrix4f().ortho2D(0, BreakoutGame.WIDTH, BreakoutGame.HEIGHT, 0);

    public void init() {
        try {
            shaderProgram = new ShaderProgram();
            shaderProgram.createVertexShader(ResourceManager.getInstance().loadStringFromFile("/shaders/vertex.glsl"));
            shaderProgram.createFragmentShader(ResourceManager.getInstance().loadStringFromFile("/shaders/fragment.glsl"));
            shaderProgram.link();

            particleShaderProgram = new ShaderProgram();
            particleShaderProgram.createVertexShader(ResourceManager.getInstance().loadStringFromFile("/shaders/particle_vertex.glsl"));
            particleShaderProgram.createFragmentShader(ResourceManager.getInstance().loadStringFromFile("/shaders/particle_fragment.glsl"));
            particleShaderProgram.link();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void render(ArrayList<GameObject> gameObjects) {
        for (GameObject gameObject : gameObjects) {
            if (!gameObject.isDestroyed()) {
                shaderProgram.bind();
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

                gameObject.draw();

                shaderProgram.unbind();
            }
        }
    }

    public void renderParticles(ParticleGenerator particles) {

        glBlendFunc(GL_SRC_ALPHA, GL_ONE);
        for (ParticleGenerator.Particle particle : particles.getParticles()) {

            if (particle.getLife() > 0) {
                particleShaderProgram.bind();
                try {
                    particleShaderProgram.createUniform("projectionMatrix");
                    particleShaderProgram.setUniform("projectionMatrix", projection);

                    Matrix4f modelMatrix = new Matrix4f()
                            .identity()
                            .translate(particle.getPosition().x, particle.getPosition().y, 0);

                    particleShaderProgram.createUniform("modelMatrix");
                    particleShaderProgram.setUniform("modelMatrix", modelMatrix);
                    // TODO fix offset problem
//                    particleShaderProgram.createUniform("offset");
//                    particleShaderProgram.setUniform("offset", particle.getPosition());
                    particleShaderProgram.createUniform("color");
                    particleShaderProgram.setUniform("color", particle.getColor());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                particles.draw();

                particleShaderProgram.unbind();
            }
        }
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }
    }
}
