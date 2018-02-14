package ritogaems.tov.gameEngine.entity.BlockPuzzle;

import ritogaems.tov.gameEngine.entity.Enemy;
import ritogaems.tov.gameEngine.graphics.animation.Sprite;
import ritogaems.tov.util.CollisionDetector;
import ritogaems.tov.util.Vector2;
import ritogaems.tov.world.GameObject;
import ritogaems.tov.world.screens.MapScreen;

/**
 * @author Zoey Longridge
 *
 * A block that the player can interact with
 */
public class Block extends Sprite {
    /**
     * Mapscreen that the blocks belongs on
     */
    private MapScreen mapScreen;

    /**
     * Vector used to set previous position
     */
    private Vector2 prevPosition = new Vector2(0, 0);

    /**
     * Vector used to set spawn position of block
     */
    private Vector2 spawnPosition;

    /**
     * Constructor for Block
     *
     * @param xPos            X position
     * @param yPos            Y position
     * @param drawWidth       Width of the draw box
     * @param drawHeight      Height of the draw box
     * @param collisionWidth  Width of the collision box
     * @param collisionHeight Height of the collision box
     * @param mapScreen       Get access to other game objects in map
     */
    public Block( float xPos, float yPos, float drawWidth, float drawHeight,
    float collisionWidth, float collisionHeight,MapScreen mapScreen) {
        super(xPos, yPos, drawWidth, drawHeight, collisionWidth, collisionHeight);
        this.mapScreen = mapScreen;
        spawnPosition = new Vector2(xPos, yPos);
        bitmap = mapScreen.getGame().getAssetStore().getBitmap("Block", "img/Puzzle/Block.png");
    }

    /**
     * Method to update the block on this frame of the game loop
     */
    public void update(){
        if (checkTileCollision() || checkChestCollision()) {
            checkPlayerCollisionWall();
        }
        checkPlayerCollision();
        checkEnemyCollisionWall();

        if (checkBlockCollisionExists()) {
            position.set(prevPosition.x, prevPosition.y);
            checkPlayerCollisionWall();
        }
        
        prevPosition.set(position.x, position.y);
    }

    /**
     * Set blocks using vector SqawnPosition to their starting position
     */
    public void reset(){
        position.set(spawnPosition);
    }

    /**
     *If this block collides with a tile, then resolve the collision with the block moving back
     * @return collisionFound
     */
    private boolean checkTileCollision() {
        Boolean collisionFound = false;
        for (GameObject object : mapScreen.tileMap.getCollidableTiles().values()) {
            CollisionDetector.CollisionType collisionType = CollisionDetector.determineAndResolveCollision(this, object, true);
            if (collisionType != CollisionDetector.CollisionType.None) {
                collisionFound = true;
            }
        }
        return collisionFound;
    }

    /**
     * If this block collides with a player, then resolve the collision with the block moving back
     */
    private void checkPlayerCollision() {
        CollisionDetector.determineAndResolveCollision(this, mapScreen.getPlayer(), false);
    }

    /**
     * Check if collision exists if this block collides with another block
     * @return collisionFound
     */
    private boolean checkBlockCollisionExists() {
        boolean collisionFound = false;
        for(Block block: mapScreen.blocks) {
            if (block != this) {
                CollisionDetector.CollisionType collisionType = CollisionDetector.determineCollisionType(this, block, false);

                if (collisionType != CollisionDetector.CollisionType.None) {
                    collisionFound = true;
                    break;
                }
            }
        }
      return collisionFound;
    }

    /**
     *If this block collides with the player, then resolve the collision with the player moving back
     */
    private void checkPlayerCollisionWall() {
        CollisionDetector.determineAndResolveCollision(mapScreen.getPlayer(), this, false);
    }

    /**
     *If this block collides with a enemy, then resolve the collision with the enemy moving back
     */
    private void checkEnemyCollisionWall() {
        for(Enemy enemy: mapScreen.enemies) {
            CollisionDetector.determineAndResolveCollision(enemy,this, false);
        }
    }

    /**
     * If this block collides with a chest, then resolve the collision with the block moving back
     * @return collisionFound
     */
    private boolean checkChestCollision() {
        boolean collisionFound = false;
        for(Chest chest : mapScreen.chests) {
            CollisionDetector.CollisionType collisionType = CollisionDetector.determineAndResolveCollision(this, chest, false);

            if (collisionType != CollisionDetector.CollisionType.None) {
                collisionFound = true;
            }
        }
        return collisionFound;
    }
}
