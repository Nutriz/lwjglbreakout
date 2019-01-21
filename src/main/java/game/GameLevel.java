package game;

import engine.GameObject;
import engine.Texture;
import engine.utils.ResourceManager;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class GameLevel {

    private final ArrayList<String> levelLines;
    private ArrayList<GameObject> bricks = new ArrayList<>();

    public GameLevel(String fileName) {
        levelLines = ResourceManager.getInstance().loadLinesFromFile(fileName);
        this.load(levelLines);
    }

    private void load(ArrayList<String> levelString) {
        List<List<Integer>> rows = this.getLevelRows(levelString);
        this.generateBricks(rows);
    }

    public void reload() {
        bricks.clear();
        this.load(levelLines);
    }

    private List<List<Integer>> getLevelRows(ArrayList<String> levelString) {
        List<List<Integer>> rows =  new ArrayList<>();
        for (String line : levelString) {
            rows.add(new ArrayList<>());
            for (String tile : line.split(" ")) {
                rows.get(rows.size()-1).add(Integer.valueOf(tile));
            }
        }
        return rows;
    }

    private void generateBricks(List<List<Integer>> rows) {

        Vector2f tileSize = new Vector2f(BreakoutGame.WIDTH / rows.get(0).size(), BreakoutGame.HEIGHT / 2 / rows.size());
        Vector2f brickSize = new Vector2f(tileSize.x, tileSize.y);

        for (int y = 0; y < rows.size(); y++) {
            List<Integer> row = rows.get(y);
            for (int x = 0; x < row.size(); x++) {
                Integer tile = row.get(x);
                if (tile > 0) {
                    Vector2f pos = new Vector2f(x * tileSize.x, y * tileSize.y);
                    Texture texture = this.getTexture(tile);
                    Vector3f color = this.getColor(tile);

                    GameObject brick = new GameObject(texture, pos, brickSize, color);
                    brick.setSolid(tile == 1);
                    bricks.add(brick);
                }
            }
        }
    }

    private Texture getTexture(Integer tile) {
        Texture texture;
        if (tile == 1) {
            texture = ResourceManager.getInstance().getTexture("block_solid");
        } else {
            texture = ResourceManager.getInstance().getTexture("block");
        }
        return texture;
    }

    private Vector3f getColor(Integer tile) {
        Vector3f color = new Vector3f(1, 1, 1);
        switch (tile) {
            case 2: color.set(0.2f, 0.6f, 1.0f); break;
            case 3: color.set(0.0f, 0.7f, 0.0f); break;
            case 4: color.set(0.8f, 0.8f, 0.4f); break;
            case 5: color.set(1.0f, 0.5f, 0.0f); break;
        }
        return color;
    }

    public ArrayList<GameObject> getBricks() {
        return bricks;
    }

    public boolean remainsBricksToDestroy() {

        for (GameObject brick : bricks) {
            if (!brick.isDestroyed() && !brick.isSolid()) {
                return true;
            }
        }

        return false;
    }
}
