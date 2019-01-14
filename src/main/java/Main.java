import engine.GameEngine;
import game.BreakoutGame;

public class Main {
    public static void main(String[] args) {
        try {
            BreakoutGame breakoutGame = new BreakoutGame();
            GameEngine engine = new GameEngine("BreakOut", BreakoutGame.WIDTH, BreakoutGame.HEIGHT, breakoutGame);
            engine.start();
            System.out.println("Engine launched");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
