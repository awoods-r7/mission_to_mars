package ritogaems.tov.gameEngine.entity;

/**
 * @author Andrew Woods
 *         Direction Enum returns an integer or Direction for animations
 */

public enum Direction {

    /**
     * enum values for Direction
     */
    UP(0),
    DOWN(1),
    RIGHT(2),
    LEFT(3),
    CENTER(4);

    /**
     * integer representation of direction
     */
    int direction;

    /**
     * @return The current direction
     */
    Direction(int direction) {
        this.direction = direction;
    }

    /**
     * @return The integer represented by the direction
     */
    public int getDirection() {
        return direction;
    }
}