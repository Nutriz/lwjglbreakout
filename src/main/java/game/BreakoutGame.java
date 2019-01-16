package game;

import engine.*;
import engine.utils.ResourceManager;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class BreakoutGame implements GameLogic {

    private final ArrayList<GameObject> gameObjects = new ArrayList<>();
    private Renderer renderer;

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    enum GameState {
        GAME_ACTIVE,
        GAME_MENU,
        GAME_WIN
    }

    GameState state;

    public BreakoutGame() {
        renderer = new Renderer();
    }

    @Override
    public void init() {
        renderer.init();

        try {
            Texture texture = new Texture("/textures/test.png");
            GameObject gameObject = new GameObject(texture, 100, 100, 50, 50);
            gameObjects.add(gameObject);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void input(Window window) {

    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void render(Window window) {
        renderer.render(gameObjects);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
    }
}
