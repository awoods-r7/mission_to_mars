package ritogaems.tov.gameEngine.entity;

import android.graphics.Bitmap;

import ritogaems.tov.gameEngine.graphics.animation.DirectedAnimation;
import ritogaems.tov.gameEngine.graphics.animation.Frame;
import ritogaems.tov.gameEngine.graphics.tilemapEngine.TileMap;
import ritogaems.tov.util.Vector2;
import ritogaems.tov.world.screens.MapScreen;

/**
 * Snake enemy that chases after the player
 */
public class SnakeEnemy extends Enemy {

    /**
     * Constructor for snake with full health
     *
     * @param xPos      The x co-ordinate of the enemy (in game units)
     * @param yPos      The y co-ordinate of the enemy (in game units)
     * @param tileMap
     * @param mapScreen The map screen the enemy belongs to
     */
    public SnakeEnemy(float xPos, float yPos,
                      TileMap tileMap, MapScreen mapScreen) {
        this(xPos, yPos, 60, tileMap, mapScreen);
    }

    /**
     * Constructor for snake with full health
     *
     * @param xPos          The x co-ordinate of the enemy (in game units)
     * @param yPos          The y co-ordinate of the enemy (in game units)
     * @param currentHealth The current & max health of the enemy
     * @param tileMap
     * @param mapScreen     The map screen the enemy belongs to
     */
    private SnakeEnemy(float xPos, float yPos, int currentHealth,
                       TileMap tileMap, MapScreen mapScreen) {
        super(xPos, yPos, 10, 15, 10, 15, currentHealth, currentHealth, 25, tileMap, mapScreen);

        // animations
        String name = "snake";
        int noOfFrames = 4;
        Bitmap snakeSheet = mapScreen.getGame().getAssetStore().getBitmap(name, "img/Enemies/Snake.png");
        movementAnimation = new DirectedAnimation(mapScreen.getGame().getAssetStore(), name, snakeSheet, noOfFrames, 9);

        // starting bitmap
        Frame startFrame = movementAnimation.getFrame(Direction.CENTER);
        this.bitmap = startFrame.getFrameBitmap();
        this.collisionBox = startFrame.getFrameDrawBox();

        roamCenter = new Vector2(xPos, yPos);
        roamRadius = 100;
        chaseRadius = 50;
        attackRadius = 0;
        moveSpeed = 0.5f;
    }

    /**
     * Move to a new position
     * @param vector the vector to update position by
     */
    public void move(Vector2 vector) {
        updatePosition(vector.x, vector.y);
    }

    /**
     * The attack method - needs inherited, not used
     */
    public void attack() {

    }

    /**
     * The death method - needs inherited, not used
     */
    public void death() {

    }
}
