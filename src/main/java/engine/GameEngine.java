package engine;

import engine.utils.Timer;

public class GameEngine implements Runnable {

    private static final float TARGET_UPS = 30;
    private Window window;
    private GameLogic game;
    private Thread gameLoopThread;

    public GameEngine(String title, int width, int height, GameLogic game) {
        this.game = game;
        window = new Window(title, width, height);
        gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
    }

    public void start() {
        String osName = System.getProperty("os.name");
        if (osName.contains("Mac")) {
            gameLoopThread.run();
        } else {
            gameLoopThread.start();
        }
    }

    @Override
    public void run() {
        try {
            this.init();
            this.gameLoop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.cleanup();
        }
    }

    protected void init() {
        window.init();
        game.init();
    }

    protected void gameLoop() {
        float elapsedTime;
        float accumulator = 0;
        float interval = 1f / TARGET_UPS; // 33 ms
        double lastLoopTime = Timer.getTime();

        boolean running = true;
        while (running && !window.windowShouldClose()) {
            elapsedTime = Timer.getElapsedTime(lastLoopTime);
            accumulator += elapsedTime;
            lastLoopTime = Timer.getTime();

            this.input();

            while (accumulator >= interval) {
                update(interval);
                accumulator -= interval;
            }

            this.render();
        }
    }

    private void input() {
        game.input(window);
    }

    protected void update(float deltaTime) {
        game.update(deltaTime);
    }

    protected void render() {
        game.render(window);
        window.update();
    }

    protected void cleanup() {
        game.cleanup();
    }
}
