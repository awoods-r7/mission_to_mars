package ritogaems.tov.world.viewports;

import ritogaems.tov.util.BoundingBox;
import ritogaems.tov.util.Vector2;

public class LayerViewport {

    //Centre coordinates of the viewport
    public float x;
    private float y;

    //Half dimensions of the viewport
    public float halfWidth;
    private float halfHeight;

    //--- CONSTRUCTORS

    //Creates layer viewport with specified dimensions
    public LayerViewport(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.halfWidth = width / 2;
        this.halfHeight = height / 2;
    }

    //--- METHODS

    //Moves the viewport to the specified location, given the centre point,
    //checking after to see if it is still within the world bounds
    public void snap(Vector2 pos, int mapWidth, int mapHeight) {
        this.x = pos.x;
        this.y = pos.y;

        if (getLeft() < 0) {
            this.x = halfWidth;
        } else if (getRight() > mapWidth) {
            this.x = mapWidth - halfWidth;
        }

        if (getTop() < 0) {
            this.y = halfHeight;
        } else if (getBottom() > mapHeight) {
            this.y = mapHeight - halfHeight;
        }
    }

    //Offsets the viewport by the specified coordinates
    public void offset(float x, float y) {
        this.x += x;
        this.y += y;
    }

    //Sets the layer viewport to the specified dimensions
    public void set(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.halfWidth = width / 2;
        this.halfHeight = height / 2;
    }

    //Returns true if point is contained in viewport, false otherwise
    public boolean contains(float x, float y) {
        return (getLeft() < x &&
                getRight() > x &&
                getTop() < y &&
                getBottom() > y);
    }

    //Returns true if all of the BoundingBox is within the layer viewport
    public boolean containsAll(BoundingBox bound) {
        return (bound.getLeft() >= getLeft() &&
                bound.getRight() <= getRight() &&
                bound.getTop() >= getTop() &&
                bound.getBottom() <= getBottom());
    }

    //Returns true if any of the BoundingBox is within the layer viewport
    public boolean intersects(BoundingBox bound) {
        return (getLeft() < bound.getRight() &&
                getRight() > bound.getLeft() &&
                getTop() < bound.getBottom() &&
                getBottom() > bound.getTop());
    }

    //--- GETTERS
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

    public float getBottom() {
        return y + halfHeight;
    }

}
