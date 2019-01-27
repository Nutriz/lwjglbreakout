package engine.graphics;

import game.BreakoutGame;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL30.*;

public class Fbo {

    private int frameBufferId;
    private int textureId;
    private int width;
    private int height;

    public Fbo(int width, int height) {
        this.width = width;
        this.height = height;
        this.createFrameBuffer();
        this.createTextureAttachment();
        this.unbindFrameBuffer();
    }

    private void createFrameBuffer() {
        frameBufferId = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, frameBufferId);
        glDrawBuffer(GL_COLOR_ATTACHMENT0);
    }

    public void bindFrameBuffer(){
        glBindTexture(GL_TEXTURE_2D, 0);//To make sure the texture isn't bound
        glBindFramebuffer(GL_FRAMEBUFFER, frameBufferId);
    }

    public void unbindFrameBuffer() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void createTextureAttachment() {
        textureId = glGenTextures();
        if (textureId == 0) {
            System.err.println("Could not create Texture");
        }
        glBindTexture(GL_TEXTURE_2D, textureId);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, (ByteBuffer) null);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, textureId, 0);
        this.checkErrors();

        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getTextureId() {
        return textureId;
    }

    public void cleanup() {
        glDeleteFramebuffers(frameBufferId);
        glDeleteTextures(textureId);
    }
    
    private void checkErrors() {
        int framebuffer = glCheckFramebufferStatus(GL_FRAMEBUFFER);
        switch ( framebuffer ) {
            case GL_FRAMEBUFFER_COMPLETE:
                break;
            case GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT:
                throw new RuntimeException( "FrameBuffer: " + frameBufferId
                        + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT exception" );
            case GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT:
                throw new RuntimeException( "FrameBuffer: " + frameBufferId
                        + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT exception" );
            case GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER:
                throw new RuntimeException( "FrameBuffer: " + frameBufferId
                        + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT exception" );
            case GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER:
                throw new RuntimeException( "FrameBuffer: " + frameBufferId
                        + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT exception" );
            default:
                throw new RuntimeException( "Unexpected reply from glCheckFramebufferStatusEXT: " + framebuffer );
        }
    }
}
