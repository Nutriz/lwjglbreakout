package game;

import org.joml.Vector2f;

public class Collision {

    private boolean isColliding;
    private BreakoutGame.Direction direction;
    private Vector2f difference;

    public Collision (boolean isColliding, BreakoutGame.Direction direction, Vector2f difference) {
        this.isColliding = isColliding;
        this.direction = direction;
        this.difference = difference;
    }

    public boolean isColliding() {
        return isColliding;
    }

    public void setColliding(boolean colliding) {
        isColliding = colliding;
    }

    public BreakoutGame.Direction getDirection() {
        return direction;
    }

    public void setDirection(BreakoutGame.Direction direction) {
        this.direction = direction;
    }

    public Vector2f getDifference() {
        return difference;
    }

    public void setDifference(Vector2f difference) {
        this.difference = difference;
    }
}
