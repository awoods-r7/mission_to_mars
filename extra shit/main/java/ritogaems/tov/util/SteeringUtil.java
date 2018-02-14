package ritogaems.tov.util;

public class SteeringUtil {

    /**
     * Method to allow a game object to seek towards a target location
     * @param source the game objects starting position
     * @param target the target location the game object is seeking towards
     * @param maxAcceleration the maximum speed that the game object can move at
     * @return returns a vector to adjust the game objects current position by
     */
    public static Vector2 seek(Vector2 source, Vector2 target, float maxAcceleration) {
        Vector2 newVector = new Vector2(target.x, target.y);

        // calculations to perform seek
        newVector.subtract(source);
        newVector.normalise();
        newVector.multiply(maxAcceleration);

        return newVector;
    }

    /**
     * Method to allow a game object to flee from a target location
     * @param source the game objects starting position
     * @param target the target location the game object is fleeing from
     * @param maxAcceleration the maximum speed the game object can move at
     * @return returns a vector to adjust the game objects current position by
     */
    public static Vector2 flee(Vector2 source, Vector2 target, float maxAcceleration) {
        Vector2 newVector = new Vector2(source.x, source.y);

        // calculations to perform flee
        newVector.subtract(target);
        newVector.normalise();
        newVector.multiply(maxAcceleration);

        return newVector;
    }

}