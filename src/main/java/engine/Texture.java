package engine;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;

public class Texture {

    private int id;
    private int width;
    private int height;

    public Texture(String fileName) throws Exception {

        id = glGenTextures();
        if (id == 0) {
            throw new Exception("Could not create Texture");
        }

        BufferedImage image = TextureLoader.loadImage(fileName);
        ByteBuffer byteBuffer = TextureLoader.loadTexture(image);

        this.generate(image.getWidth(), image.getHeight(), byteBuffer);
    }


    public Texture(int width, int height) throws Exception {

        id = glGenTextures();
        if (id == 0) {
            throw new Exception("Could not create Texture");
        }

        this.generate(width, height, null);
    }


    private void generate(int width, int height, ByteBuffer data) {
        this.width = width;
        this.height = height;

        glBindTexture(GL_TEXTURE_2D, id);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glEnable(GL_BLEND);// you enable blending function
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void cleanup() {
        glDeleteTextures(id);
    }

    public int getId() {
        return id;
    }
}
