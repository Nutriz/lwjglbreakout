package game;

import engine.GameLogic;
import engine.Window;

public class BreakoutGame implements GameLogic {

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
    }

    @Override
    public void input(Window window) {

    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void render(Window window) {
        renderer.render();
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
    }
}
