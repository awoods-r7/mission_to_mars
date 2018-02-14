package ritogaems.tov.util;

import java.io.Serializable;

public class Vector2 implements Serializable {

    // zero (0, 0) vector
    public final static Vector2 Zero = new Vector2(0, 0);

    /*
     * PROPERTIES
     */

    // x and y of vector
    public float x;
    public float y;

    /*
     * CONSTRUCTORS
     */

    // blank constructor
    public Vector2() { }

    // constructor that takes x and y
    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    // constructor from another vector
    public Vector2(Vector2 other) {
        this.x = other.x;
        this.y = other.y;
    }

    /*
     * METHODS
     */

    // determines if vector = (0, 0)
    public boolean isZero() {
        return x == 0.0f && y == 0.0f;
    }

    // calculate the Euclidean length of the vector
    private float length() {
        return (float) Math.sqrt(x * x + y * y);
    }
    public float lengthSquared() {
        return x * x + y * y;
    }

    // set the x and y values
    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    // set x and y values based on another vector
    public void set(Vector2 other) {
        this.x = other.x;
        this.y = other.y;
    }

    // increases the x and y value
    public void add(float x, float y) {
        this.x += x;
        this.y += y;
    }

    // increases the x and y value based on another vector
    public void add(Vector2 other) {
        this.x += other.x;
        this.y += other.y;
    }

    // decreases the x and y value by another vector
    public void subtract(Vector2 other) {
        this.x -= other.x;
        this.y -= other.y;
    }

    // multiple x and y components by a scalar
    public void multiply(float scalar) {
        x *= scalar;
        y *= scalar;
    }

    // divide x and y components by a scalar
    public void divide(float scalar) {
        x /= scalar;
        y /= scalar;
    }

    public float getDistance(Vector2 other) {
        double pythagA, pythagB, pythagC;
        pythagA = Math.pow(other.x - this.x, 2);
        pythagB = Math.pow(other.y - this.y, 2);
        pythagC = Math.sqrt(pythagA + pythagB);

        return (float) pythagC;
    }

    public double getAngle(Vector2 other) {
        return Math.atan2(this.y - other.y, this.x - other.x);
    }

    // normalise the scalar
    public void normalise() {
        float length = length();
        if (length != 0) {
            x /= length;
            y /= length;
        }
    }
}