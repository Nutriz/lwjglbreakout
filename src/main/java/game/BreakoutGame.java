package game;

import engine.*;
import engine.utils.ResourceManager;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11C.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.GL_DEPTH_BUFFER_BIT;

public class BreakoutGame implements GameLogic {

    private final ArrayList<GameObject> gameObjects = new ArrayList<>();
    private Renderer renderer;


    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private ArrayList<GameLevel> levels = new ArrayList<>();
    private int currentLevelIndex;

    private int direction = 0;
    private GameObject background;

    enum GameState {
        GAME_ACTIVE,
        GAME_MENU,
        GAME_WIN
    }

    GameState state = GameState.GAME_ACTIVE;

    private GameObject player;
    private static final Vector2f PLAYER_SIZE = new Vector2f(100, 20);
    private static final float PLAYER_VELOCITY = 500;

    private BallObject ball;
    private static final float BALL_RADIUS = 12.5f;
    private static final Vector2f INITIAL_BALL_VELOCITY = new Vector2f(100, -350);


    public BreakoutGame() {
        renderer = new Renderer();
    }

    @Override
    public void init() {
        renderer.init();

        ResourceManager.getInstance().loadTexture("/textures/background.jpg", "background");
        ResourceManager.getInstance().loadTexture("/textures/block.png", "block");
        ResourceManager.getInstance().loadTexture("/textures/block_solid.png", "block_solid");
        ResourceManager.getInstance().loadTexture("/textures/paddle.png", "paddle");
        ResourceManager.getInstance().loadTexture("/textures/awesomeface.png", "ball");

        // loads levels from 1 to 4
        for (int i = 1; i <= 4; i++) {
            GameLevel level = new GameLevel("/levels/level" + i + ".txt");
            levels.add(level);
        }

        currentLevelIndex = 1;

        background = new GameObject(ResourceManager.getInstance().getTexture("background"), 0, 0, WIDTH, HEIGHT);

        Vector2f playerPos = new Vector2f(WIDTH/2 - PLAYER_SIZE.x/2, HEIGHT - PLAYER_SIZE.y);
        player = new GameObject(ResourceManager.getInstance().getTexture("paddle"), playerPos.x, playerPos.y, PLAYER_SIZE.x, PLAYER_SIZE.y);
        gameObjects.add(player);

        Vector2f ballPos = new Vector2f(playerPos.x + PLAYER_SIZE.x/2 - BALL_RADIUS, HEIGHT - PLAYER_SIZE.y - BALL_RADIUS*2);
        ball = new BallObject(ResourceManager.getInstance().getTexture("ball"), ballPos, BALL_RADIUS, INITIAL_BALL_VELOCITY);
        gameObjects.add(ball);
    }

    @Override
    public void input(Window window) {
        if (state == GameState.GAME_ACTIVE) {
            if (window.isKeyPressed(GLFW.GLFW_KEY_LEFT)) {
                direction = -1;
            } else if (window.isKeyPressed(GLFW.GLFW_KEY_RIGHT)) {
                direction = 1;
            } else {
                direction = 0;
            }

            if (window.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
                ball.setStuck(false);
            }

            if (window.isKeyPressed(GLFW.GLFW_KEY_R)) {
                Vector2f ballPos = new Vector2f(player.getPosition().x + PLAYER_SIZE.x/2 - BALL_RADIUS, HEIGHT - PLAYER_SIZE.y - BALL_RADIUS*2);
                ball.reset(ballPos, INITIAL_BALL_VELOCITY);
                ball.setStuck(true);
            }
        }
    }

    @Override
    public void update(float deltaTime) {
        float velocity = PLAYER_VELOCITY * direction * deltaTime;

        if (direction == -1 && player.getPosition().x >= 0
        || direction == 1 && player.getPosition().x <= WIDTH - player.getSize().x ) {
            player.getPosition().add(velocity, 0);

            if (ball.isStuck()) {
                ball.getPosition().add(velocity, 0);
            }
        }

        ball.move(deltaTime);
    }

    @Override
    public void render(Window window) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        if (state == GameState.GAME_ACTIVE) {
            renderer.render(background);
            renderer.render(levels.get(currentLevelIndex - 1).getBricks());
            renderer.render(gameObjects);
        }
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
    }
}
