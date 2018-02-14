package ritogaems.tov.gameEngine.items;


import ritogaems.tov.gameEngine.ElapsedTime;
import ritogaems.tov.gameEngine.graphics.animation.Sprite;
import ritogaems.tov.util.CollisionDetector;
import ritogaems.tov.world.GameObject;
import ritogaems.tov.world.screens.MapScreen;

/**
 * @author Darren
 */
public abstract class Item extends Sprite {

    /**
     * Map Screen in which this item exists
     */
    MapScreen mapScreen;

    /**
     * Name of the item
     */
    public String itemName;

    /**
     * Determines whether the item should continue to exist
     */
    boolean alive;

    /**
     * Standard constructor
     *
     * @param xPos              X position
     * @param yPos              Y position
     * @param drawWidth         Drawing width
     * @param drawHeight        Drawing height
     * @param collisionWidth    Collision width
     * @param collisionHeight   Collision height
     * @param mapScreen         Map screen in which this object exists
     */
    Item(float xPos, float yPos, float drawWidth, float drawHeight,
         float collisionWidth, float collisionHeight, MapScreen mapScreen) {
        super(xPos, yPos, drawWidth, drawHeight, collisionWidth, collisionHeight);

        this.mapScreen = mapScreen;
        alive = true;
    }

    /**
     * Empty, to be override by subclasses, but not mandatorily
     *
     * @param elapsedTime Time that has passed since last game loop iteration
     */
    public void update(ElapsedTime elapsedTime) {

    }

    /**
     * Getter
     *
     * @return whether alive is true or false
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Setter
     *
     * @param alive the new boolean
     */
    public void setAlive(Boolean alive) {
        this.alive = alive;
    }

    /**
     * Checks collision with collidable tiles and resolves it
     *
     * @return
     */
    boolean checkTileCollision() {
        boolean collisionFound = false;
        // Consider each platform for a collision
        for (GameObject object : mapScreen.tileMap.getCollidableTiles().values()) {
            CollisionDetector.CollisionType collisionType = CollisionDetector.determineAndResolveCollision(this, object, true);

            if (collisionType != CollisionDetector.CollisionType.None) {
                collisionFound = true;
            }
        }

        return collisionFound;
    }
}