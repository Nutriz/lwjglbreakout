package engine.utils;

import engine.ShaderProgram;
import engine.Texture2D;
import engine.TextureLoader;
import org.lwjgl.opengl.GL20;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Scanner;

public class ResourceManager {

    private static ResourceManager instance;

    private HashMap<String, ShaderProgram> shaders = new HashMap<>();
    private HashMap<String, Texture2D> textures = new HashMap<>();

    public static ResourceManager getInstance() {
        if (instance == null) {
            instance = new ResourceManager();
        }
        return instance;
    }

    public String loadStringFromFile(String fileName) {
        String result = null;
        try (InputStream in = ResourceManager.class.getResourceAsStream(fileName)) {
            Scanner scanner = new Scanner(in, "UTF-8");
            result = scanner.useDelimiter("\\A").next();
            System.out.println("File loaded: " + fileName);
        } catch (Exception e) {
            System.err.println("Can't load file from : " + fileName);
        }

        return result;
    }

    private ByteBuffer loadTextureFromFile(String fileName) {
        BufferedImage image = TextureLoader.loadImage(fileName);
        ByteBuffer byteBuffer = TextureLoader.loadTexture(image);
        return byteBuffer;
    }
}
