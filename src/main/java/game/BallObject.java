package game;

import engine.GameObject;
import engine.Texture;
import org.joml.Vector2f;

public class BallObject extends GameObject {

    private float radius;
    private boolean stuck = true;


    public BallObject(Texture texture, Vector2f pos, float radius, Vector2f velocity) {
        super(texture, pos, new Vector2f(radius*2, radius*2), new Vector2f(velocity));

        this.radius = radius;
    }

    public void move(float deltaTime) {
        if (!stuck) {
            position.add(velocity.x * deltaTime, velocity.y * deltaTime);

            if (position.x < 0) {
                velocity.x *= -1;
            } else if (position.x + radius*2 > BreakoutGame.WIDTH) {
                velocity.x *= -1;
            } else if (position.y < 0) {
                velocity.y *= -1;
            }
        }
    }

    public void reset(Vector2f position, Vector2f velocity) {
        this.setPosition(position);
        this.setVelocity(new Vector2f(velocity));
        stuck = true;
    }

    public boolean isStuck() {
        return stuck;
    }

    public void setStuck(boolean stuck) {
        this.stuck = stuck;
    }
}
