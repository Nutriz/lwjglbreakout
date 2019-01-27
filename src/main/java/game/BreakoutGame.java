package game;

import engine.*;
import engine.utils.ResourceManager;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11C.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.GL_DEPTH_BUFFER_BIT;

public class BreakoutGame implements GameLogic {

    private final ArrayList<GameObject> gameObjects = new ArrayList<>();
    private final HashMap<Direction, Vector2f> compass = new HashMap<>(4);
    private Renderer renderer;

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private ArrayList<GameLevel> levels = new ArrayList<>();
    private int currentLevelIndex = 0;

    private int direction = 0;

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
    private static final Vector2f INITIAL_BALL_VELOCITY = new Vector2f(300, -350);

    enum Direction {
        UP,
        RIGHT,
        DOWN,
        LEFT
    }

    public BreakoutGame() {
        renderer = new Renderer();
        compass.put(Direction.UP, new Vector2f(0, 1));
        compass.put(Direction.RIGHT, new Vector2f(1, 0));
        compass.put(Direction.DOWN, new Vector2f(0, -1));
        compass.put(Direction.LEFT, new Vector2f(-1, 0));
    }

    @Override
    public void init() {
        renderer.init();

        ResourceManager.getInstance().loadTexture("/textures/background.jpg", "background");
        ResourceManager.getInstance().loadTexture("/textures/block.png", "block");
        ResourceManager.getInstance().loadTexture("/textures/block_solid.png", "block_solid");
        ResourceManager.getInstance().loadTexture("/textures/paddle.png", "paddle");
        ResourceManager.getInstance().loadTexture("/textures/awesomeface.png", "ball");

        GameObject background = new GameObject(0, 0, WIDTH, HEIGHT);
        background.setTexture(ResourceManager.getInstance().getTexture("background"));
        gameObjects.add(background);

        GameLevel levelDebug = new GameLevel("/levels/debug_level.txt");
        levels.add(levelDebug);
        gameObjects.addAll(levels.get(currentLevelIndex).getBricks());
        // loads levels from 1 to 4
        for (int i = 1; i <= 4; i++) {
            GameLevel level = new GameLevel("/levels/level" + i + ".txt");
            levels.add(level);
        }

        Vector2f playerPos = new Vector2f(WIDTH/2 - PLAYER_SIZE.x/2, HEIGHT - PLAYER_SIZE.y);
        player = new GameObject(playerPos.x, playerPos.y, PLAYER_SIZE.x, PLAYER_SIZE.y);
        player.setTexture(ResourceManager.getInstance().getTexture("paddle"));
        gameObjects.add(player);

        Vector2f ballPos = new Vector2f(playerPos.x + PLAYER_SIZE.x/2 - BALL_RADIUS, HEIGHT - PLAYER_SIZE.y - BALL_RADIUS*2);
        ball = new BallObject(ballPos, BALL_RADIUS, INITIAL_BALL_VELOCITY);
        ball.setTexture(ResourceManager.getInstance().getTexture("ball"));
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
                this.resetLevel();
                this.resetBall();
            }
        }
    }

    private void resetBall() {
        Vector2f ballPos = new Vector2f(player.getPosition().x + PLAYER_SIZE.x/2 - BALL_RADIUS, HEIGHT - PLAYER_SIZE.y - BALL_RADIUS*2);
        ball.reset(ballPos, INITIAL_BALL_VELOCITY);
        ball.setStuck(true);
    }

    private void resetLevel() {
        levels.get(currentLevelIndex).reload();
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

        if (ball.getPosition().y > HEIGHT) {
            this.resetLevel();
            this.resetBall();
        }

        if (!levels.get(currentLevelIndex).remainsBricksToDestroy()) {
            this.resetBall();
            currentLevelIndex++;
            gameObjects.addAll(levels.get(currentLevelIndex).getBricks());
            System.out.println("Go to next level: " + currentLevelIndex);
        }

        ball.move(deltaTime);

        this.doCollision();
    }

    private void doCollision() {
        for (GameObject brick : levels.get(currentLevelIndex).getBricks()) {
            if (!brick.isDestroyed()) {
                Collision collision = this.checkCollision(ball, brick);
                if (collision.isColliding()) {
                    brick.setDestroyed(!brick.isSolid());

                    // collision resolution
                    Direction dir = collision.getDirection();
                    System.out.println("dir: " + dir);

                    if (dir == null) {
                        dir = Direction.DOWN;
                    }

                    if (dir == Direction.LEFT || dir == Direction.RIGHT) {
                        ball.getVelocity().x = -ball.getVelocity().x;

                        float penetration = ball.getSize().x/2 - Math.abs(collision.getDifference().x);
                        if (dir == Direction.LEFT) {
                            ball.getPosition().x += penetration;
                        } else {
                            ball.getPosition().x -= penetration;
                        }
                    } else {
                        ball.getVelocity().y = -ball.getVelocity().y;

                        float penetration = ball.getSize().y/2 - Math.abs(collision.getDifference().y);
                        if (dir == Direction.UP) {
                            ball.getPosition().y -= penetration;
                        } else {
                            ball.getPosition().y += penetration;
                        }
                    }
                }
            }
        }

        // Also check collisions for player pad (unless stuck)
        Collision result = this.checkCollision(ball, player);
        if (!ball.isStuck() && result.isColliding()) {
            // Check where it hit the board, and change velocity based on where it hit the board
            // TODO refactor GameObject to return CenterX centerY
            float halfBoardSize = player.getSize().x/2;
            float centerBoard = player.getPosition().x + halfBoardSize;
            float distance = (ball.getPosition().x + ball.getSize().x/2) - centerBoard;
            float percentage = distance / halfBoardSize;
            // Then move accordingly
            float strength = 2.0f;
            Vector2f oldVelocity = new Vector2f(ball.getVelocity());
            ball.getVelocity().x = INITIAL_BALL_VELOCITY.x * percentage * strength;
            ball.getVelocity().y = -1 * Math.abs(ball.getVelocity().y);
            ball.setVelocity(new Vector2f(ball.getVelocity()).normalize().mul(oldVelocity.length()));
        }
    }

    private boolean checkCollision(GameObject ball, GameObject brick) {
        boolean collisionX = ball.getPosition().x + ball.getSize().x >= brick.getPosition().x && ball.getPosition().x <= brick.getPosition().x + brick.getSize().x;
        boolean collisionY = ball.getPosition().y + ball.getSize().y >= brick.getPosition().y && ball.getPosition().y <= brick.getPosition().y + brick.getSize().y;
        return collisionX && collisionY;
    }

    // TODO better manager vector math
    private Collision checkCollision(BallObject ball, GameObject brick) {
        Vector2f center = new Vector2f(ball.getPosition().x + ball.getSize().x/2, ball.getPosition().y + ball.getSize().y/2);
        Vector2f aabb_half_extents = new Vector2f(brick.getSize().x / 2, brick.getSize().y / 2);
        Vector2f aabb_center = new Vector2f(brick.getPosition().x + aabb_half_extents.x, brick.getPosition().y + aabb_half_extents.y);

        Vector2f difference = new Vector2f(center.x - aabb_center.x, center.y - aabb_center.y);

        Vector2f clamped = this.clamp(difference, aabb_half_extents, aabb_half_extents);
        Vector2f closest = new Vector2f(clamped.x + aabb_center.x, clamped.y + aabb_center.y);

        difference.set(closest.x - center.x, closest.y - center.y);

        if (difference.length() < ball.getSize().x/2) {
            return new Collision(true, this.vectorDirection(difference), difference);
        } else {
            return new Collision(false, null, new Vector2f(0, 0));
        }
    }

    private Direction vectorDirection(Vector2f target) {

        float max = 0;
        Direction direction = null;

        for (Direction dir : compass.keySet()) {
            float dotProduct = target.normalize().dot(compass.get(dir));
            //System.out.println("dir name: " + dir + " target: " + target + " dir: " + compass.get(dir));
            if (dotProduct > max) {
                max = dotProduct;
                direction = dir;
            }
        }

        return direction;
    }

    private Vector2f clamp(Vector2f value, Vector2f min, Vector2f max) {
        float x = Math.max(-min.x, Math.min(max.x, value.x));
        float y =  Math.max(-min.y, Math.min(max.y, value.y));
        return new Vector2f(x, y);
    }

    @Override
    public void render(Window window) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        if (state == GameState.GAME_ACTIVE) {
            renderer.render(gameObjects);
        }
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
    }
}
