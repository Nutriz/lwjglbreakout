package engine;

import org.joml.Vector2f;

public class GameObject {

    private Texture texture;
    private Vector2f position = new Vector2f(0, 0);
    private Vector2f size = new Vector2f(64, 64);
    private Vector2f velocity = new Vector2f(0, 0);
    private float scale = 1;
    private float rotation = 0;

    private boolean isSolid = false;
    private boolean isDestroyed = false;


    public GameObject(Texture sprite) {
        this.texture = sprite;
    }

    public GameObject(Texture texture, float x, float y, float w, float h) {
        this.texture = texture;
        this.position = new Vector2f(x, y);
        this.size = new Vector2f(w, h);
    }


    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public Vector2f getSize() {
        return size;
    }

    public void setSize(Vector2f size) {
        this.size = size;
    }

    public Vector2f getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2f velocity) {
        this.velocity = velocity;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public boolean isSolid() {
        return isSolid;
    }

    public void setSolid(boolean solid) {
        isSolid = solid;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public void setDestroyed(boolean destroyed) {
        isDestroyed = destroyed;
    }

    public Texture getTexture() {
        return texture;
    }
}
