package ritogaems.tov.util;

import java.io.Serializable;

public class BoundingBox implements Serializable {

    //Centre point of the bound
    public float x, y;

    //Half dimensions of the bound
    public float halfWidth, halfHeight;

    // blank constructor
    public BoundingBox() {
        reset();
    }

    // constructor with specified dimensions
    public BoundingBox(float x, float y, float halfWidth, float halfHeight) {
        reset(x,y,halfWidth,halfHeight);
    }

    // reset methods
    private void reset() {
        x = 0;
        y = 0;
        halfWidth = 1.0f;
        halfHeight = 1.0f;
    }

    public void reset(float x, float y, float halfWidth, float halfHeight) {
        this.x = x;
        this.y = y;
        this.halfWidth = halfWidth;
        this.halfHeight = halfHeight;
    }

    // methods to get bounds of box
    public float getWidth() {
        return halfWidth * 2.0f;
    }

    public float getHeight() {
        return halfHeight * 2.0f;
    }

    public float getLeft() {
        return x - halfWidth;
    }

    public float getRight() {
        return x + halfWidth;
    }

    public float getTop() {
        return y - halfHeight;
    }

    public float getBottom() { return y + halfHeight; }

    // check if specified point is within the bounding box
    public boolean contains(float x, float y) {
        return getLeft() < x &&
                getRight() > x &&
                getTop() < y &&
                getBottom() > y;
    }

    public boolean intersectsTest(BoundingBox other) {
        return getLeft() - halfWidth / 4 < other.getRight() &&
                getRight() - halfWidth / 4 > other.getLeft() &&
                getTop() - halfHeight / 4 < other.getBottom() &&
                getBottom() - halfHeight / 4 > other.getTop();
    }

    // check if the bounding box intersects another bounding box
    public boolean intersects(BoundingBox other) {
        return getLeft() < other.getRight() &&
                getRight() > other.getLeft() &&
                getTop() < other.getBottom() &&
                getBottom() > other.getTop();
    }
}