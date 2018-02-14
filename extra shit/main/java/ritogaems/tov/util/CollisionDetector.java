package ritogaems.tov.util;
import ritogaems.tov.world.GameObject;
/**
 * @author Zoey Longridge
 *
 * Methods taken and edited from Gage
 */
public class CollisionDetector {
    /**
     * Types of collision
     */
    public enum CollisionType {
        None, Top, Bottom, Left, Right
    }

    /**
     * Determine the type of collision between the two game objects. If the two
     * objects overlap, then they are separated. The first game object will be
     * repositioned. The second game object will not be moved following a
     * collision.
     *
     * CollisionType.None is returned if there are no collisions.
     *
     * @param gameObjectOne
     *            First game object box
     * @param gameObjectTwo
     *            Second game object box
     * @return Collision type
     *
     *
     */
    public static CollisionType determineAndResolveCollision(
            GameObject gameObjectOne, GameObject gameObjectTwo, boolean topOverlap) {
        CollisionType collisionType = CollisionType.None;

        BoundingBox one;
        BoundingBox two = gameObjectTwo.getCollisionBox();

        if(topOverlap){
            BoundingBox temp = gameObjectOne.getCollisionBox();
            float newTop = temp.getTop() + temp.halfHeight;
            float newHalfHeight = (temp.halfHeight + (temp.y - newTop)) /2;
            float newY = temp.getBottom() - newHalfHeight;

            one = new BoundingBox(temp.x, newY, temp.halfWidth, newHalfHeight);
        }
        else{
            one = gameObjectOne.getCollisionBox();
        }

        if (one.intersects(two)) {
            // Determine the side of *least intersection*
            float collisionDepth = Float.MAX_VALUE;

            // Check the top side
            float tOverlap = (two.y + two.halfHeight)
                    - (one.y - one.halfHeight);
            if (tOverlap > 0.0f && tOverlap < collisionDepth) {
                collisionType = CollisionType.Top;
                collisionDepth = tOverlap;
            }

            // Check the bottom side
            float bOverlap = (one.y + one.halfHeight)
                    - (two.y - two.halfHeight);
            if (bOverlap > 0.0f && bOverlap < collisionDepth) {
                collisionType = CollisionType.Bottom;
                collisionDepth = bOverlap;
            }

            // Check the right overlap
            float rOverlap = (one.x + one.halfWidth) - (two.x - two.halfWidth);
            if (rOverlap > 0.0f && rOverlap < collisionDepth) {
                collisionType = CollisionType.Right;
                collisionDepth = rOverlap;
            }

            // Check the left overlap
            float lOverlap = (two.x + two.halfWidth) - (one.x - one.halfWidth);
            if (lOverlap > 0.0f && lOverlap < collisionDepth) {
                collisionType = CollisionType.Left;
                collisionDepth = lOverlap;
            }

            // Separate if needed
            switch (collisionType) {
                case Top:
                    gameObjectOne.position.y +=collisionDepth;
                    break;
                case Bottom:
                    gameObjectOne.position.y -= collisionDepth;
                    break;
                case Right:
                    gameObjectOne.position.x -= collisionDepth;
                    break;
                case Left:
                    gameObjectOne.position.x += collisionDepth;
                    break;
                case None:
                    break;
            }
        }

        return collisionType;
    }

    /**
     * Determine the side that collision is occuring and  return it
     *
     * @param gameObjectOne
     * @param gameObjectTwo
     * @param topOverlap
     * @return collisionType
     */
    public static CollisionType determineCollisionType(
            GameObject gameObjectOne, GameObject gameObjectTwo, boolean topOverlap) {
        CollisionType collisionType = CollisionType.None;

        BoundingBox one;
        BoundingBox two = gameObjectTwo.getCollisionBox();

        if(topOverlap){
            //Maths to allow top overlapping of collision boxes
            //Maths is only applied if topOverlap boolean is true
            BoundingBox temp = gameObjectOne.getCollisionBox();
            float newTop = temp.getTop() + temp.halfHeight;
            float newHalfHeight = (temp.halfHeight + (temp.y - newTop)) /2;
            float newY = temp.getBottom() - newHalfHeight;

            one = new BoundingBox(temp.x, newY, temp.halfWidth, newHalfHeight);
        }
        else{
            one = gameObjectOne.getCollisionBox();
        }

        if (one.intersects(two)) {
            // Determine the side of *least intersection*
            float collisionDepth = Float.MAX_VALUE;

            // Check the top side
            float tOverlap = (two.y + two.halfHeight)
                    - (one.y - one.halfHeight);
            if (tOverlap > 0.0f && tOverlap < collisionDepth) {
                collisionType = CollisionType.Top;
                collisionDepth = tOverlap;
            }
            // Check the bottom side
            float bOverlap = (one.y + one.halfHeight)
                    - (two.y - two.halfHeight);
            if (bOverlap > 0.0f && bOverlap < collisionDepth) {
                collisionType = CollisionType.Bottom;
                collisionDepth = bOverlap;
            }
            // Check the right overlap
            float rOverlap = (one.x + one.halfWidth) - (two.x - two.halfWidth);
            if (rOverlap > 0.0f && rOverlap < collisionDepth) {
                collisionType = CollisionType.Right;
                collisionDepth = rOverlap;
            }
            // Check the left overlap
            float lOverlap = (two.x + two.halfWidth) - (one.x - one.halfWidth);
            if (lOverlap > 0.0f && lOverlap < collisionDepth) {
                collisionType = CollisionType.Left;
            }
        }
        return collisionType;
    }
}
