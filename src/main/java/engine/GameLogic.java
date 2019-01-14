package engine;

public interface GameLogic {

    void init();

    void input(Window window);

    void update(float deltaTime);

    void render(Window window);

    void cleanup();
}