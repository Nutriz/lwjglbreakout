package engine;

import engine.graphics.Fbo;
import engine.utils.ResourceManager;
import game.BreakoutGame;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class PostProcessor {

    private int vaoId;
    private Fbo frameBuffer;
    private ShaderProgram shader;

    private boolean confuse;
    private boolean chaos;
    private boolean shake;

    public PostProcessor() {

        frameBuffer = new Fbo(BreakoutGame.WIDTH, BreakoutGame.HEIGHT);

        this.initRenderData();

        try {
            shader = new ShaderProgram();
            shader.createVertexShader(ResourceManager.getInstance().loadStringFromFile("/shaders/postprocessing_vertex.glsl"));
            shader.createFragmentShader(ResourceManager.getInstance().loadStringFromFile("/shaders/postprocessing_fragment.glsl"));
            shader.link();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initRenderData() {

        float[] vertices = new float[]{
                // Pos        // Tex
                -1.0f, -1.0f, 0.0f, 0.0f,
                1.0f,  1.0f, 1.0f, 1.0f,
                -1.0f,  1.0f, 0.0f, 1.0f,

                -1.0f, -1.0f, 0.0f, 0.0f,
                1.0f, -1.0f, 1.0f, 0.0f,
                1.0f,  1.0f, 1.0f, 1.0f
        };

        FloatBuffer buffer = MemoryUtil.memAllocFloat(vertices.length);
        buffer.put(vertices).flip();

        vaoId = glGenVertexArrays();
        int vboId = glGenBuffers();

        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

        glBindVertexArray(vaoId);

        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);
        BreakoutGame.checkError(false);

        // unbind
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void startCapture() {
        frameBuffer.bindFrameBuffer();

    }

    public void endCapture() {
        frameBuffer.unbindFrameBuffer();
    }

    public void doPostProcessing(double time) {

        shader.bind();
        shader.createUniform("time");
        shader.setUniform("time", (float) time);
        shader.createUniform("confuse");
        shader.setUniform("confuse", confuse);
        shader.createUniform("chaos");
        shader.setUniform("chaos", chaos);
        shader.createUniform("shake");
        shader.setUniform("shake", shake);

        // Activate firs texture bank
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, frameBuffer.getTextureId());

        // Bind to the VAO
        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);

        // Draw the vertices
        glDrawArrays(GL_TRIANGLES, 0, 6);

        // Restore state
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
        glBindTexture(GL_TEXTURE_2D, 0);

        shader.unbind();
    }

    public void cleanup() {
        if (shader != null) {
            shader.cleanup();
        }
        frameBuffer.cleanup();
    }

    public void setConfuse(boolean confuse) {
        this.confuse = confuse;
    }

    public void setChaos(boolean chaos) {
        this.chaos = chaos;
    }

    public void setShake(boolean shake) {
        this.shake = shake;
    }
}
