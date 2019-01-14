package engine;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.system.MemoryUtil;

public class TextureLoader {

    public static BufferedImage loadImage(String loc) {
        try {
            return ImageIO.read(TextureLoader.class.getResource(loc));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ByteBuffer loadTexture(BufferedImage image) {

        int imgWidth = image.getWidth();
        int imgHeight = image.getHeight();
        int bytesPerPixel = image.getColorModel().getPixelSize(); //4 for RGBA, 3 for RGB

        int[] pixels = new int[imgWidth * imgHeight];
        image.getRGB(0, 0, imgWidth, imgHeight, pixels, 0, imgWidth);

//        ByteBuffer buffer = BufferUtils.createByteBuffer(imgWidth * imgHeight * bytesPerPixel);
        ByteBuffer buffer = MemoryUtil.memAlloc(imgWidth * imgHeight * bytesPerPixel);
        for(int y = 0; y < imgHeight; y++){
            for(int x = 0; x < imgWidth; x++){
                int pixel = pixels[y * imgWidth + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red component
                buffer.put((byte) ((pixel >> 8) & 0xFF));  // Green component
                buffer.put((byte) (pixel & 0xFF));         // Blue component
                buffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha component. Only for RGBA
            }
        }

        buffer.flip(); //FOR THE LOVE OF GOD DO NOT FORGET THIS

        return buffer;
    }
}