package engine;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;

public class Texture2D {

    private int textureId;
    private int width;
    private int height;

    public Texture2D() throws Exception {
        textureId = glGenTextures();
        if (textureId == 0) {
            throw new Exception("Could not create Texture");
        }
    }

    public void generate(int width, int height, ByteBuffer data) {
        this.width = width;
        this.height = height;

        glBindTexture(GL_TEXTURE_2D, textureId);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, data);
//        //Send texel data to OpenGL
//        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glBindTexture(GL_TEXTURE_2D, 0);
    }
    public void bind() {
        glBindTexture(GL_TEXTURE_2D, textureId);
    }

    public int getTextureId() {
        return textureId;
    }
}
