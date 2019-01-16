package game;

import engine.*;
import engine.utils.ResourceManager;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11C.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.GL_DEPTH_BUFFER_BIT;

public class BreakoutGame implements GameLogic {

    private final ArrayList<GameObject> gameObjects = new ArrayList<>();
    private Renderer renderer;

    private GameLevel level;

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    enum GameState {
        GAME_ACTIVE,
        GAME_MENU,
        GAME_WIN
    }

    GameState state = GameState.GAME_ACTIVE;

    public BreakoutGame() {
        renderer = new Renderer();
    }

    @Override
    public void init() {
        renderer.init();

        ResourceManager.getInstance().loadTexture("/textures/block.png", "block");
        ResourceManager.getInstance().loadTexture("/textures/block_solid.png", "block_solid");
        ResourceManager.getInstance().loadTexture("/textures/paddle.png", "paddle");

        level = new GameLevel("/levels/level1.txt");
    }

    @Override
    public void input(Window window) {

    }

    @Override
    public void update(float deltaTime) {

        if (state == GameState.GAME_ACTIVE) {

        }
    }

    @Override
    public void render(Window window) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        renderer.render(level.getBricks());
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
    }
}
