package engine.utils;

import engine.ShaderProgram;
import engine.Texture;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class ResourceManager {

    private static ResourceManager instance;

    private HashMap<String, ShaderProgram> shaders = new HashMap<>();
    private HashMap<String, Texture> textures = new HashMap<>();

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
            System.out.println("Engine: File loaded: " + fileName);
        } catch (Exception e) {
            System.err.println("Engine: Can't load file from : " + fileName);
            e.printStackTrace();
        }

        return result;
    }

    public ArrayList<String> loadLinesFromFile(String fileName) {
        try {
            String path = "src/main/resources" + fileName;
            return (ArrayList<String>) Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Engine: Can't load file from : " + fileName);
            e.printStackTrace();
        }
        return null;
    }

    public void loadTexture(String fileName, String name) {
        Texture texture = null;
        try {
            texture = new Texture(fileName);
            textures.put(name, texture);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Texture getTexture(String name) {
        return textures.get(name);
    }
}
