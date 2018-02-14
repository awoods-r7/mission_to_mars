package ritogaems.tov.gameEngine.items;

import android.graphics.Bitmap;

import java.util.ArrayList;

import ritogaems.tov.ai.AIUtil;
import ritogaems.tov.gameEngine.AssetStore;
import ritogaems.tov.gameEngine.ElapsedTime;
import ritogaems.tov.gameEngine.audio.SoundEffect;
import ritogaems.tov.gameEngine.entity.Direction;
import ritogaems.tov.gameEngine.entity.Enemy;
import ritogaems.tov.gameEngine.entity.Entity;
import ritogaems.tov.gameEngine.entity.Player;
import ritogaems.tov.gameEngine.graphics.animation.Animation;
import ritogaems.tov.gameEngine.graphics.animation.Frame;
import ritogaems.tov.util.BoundingBox;
import ritogaems.tov.util.CollisionDetector;
import ritogaems.tov.util.SteeringUtil;
import ritogaems.tov.util.Vector2;
import ritogaems.tov.world.screens.MapScreen;

/**
 * This class creates a boomerang, adds it to the map and specifies its properties
 *
 * @author Andrew Woods
 */
public class Boomerang extends Item {

    /**
     * Variables needed for boomerang
     */
    private SoundEffect boomerangSound;
    public int damage = 25;
    private Vector2 movementVector;
    private double timeToReturn = 1;
    private boolean returning = false;
    private ArrayList<Vector2> path = new ArrayList<>();
    Animation boomerang;
    private int streamID = 0;

    /**
     * This constructor creates a boomerang, and specifies its properties
     *
     * @param xPos      This is the y position that the bitmap will be drawn to
     * @param yPos      This is the y position that the bitmap will be drawn to
     * @param width     This is the width of the bitmap in game units
     * @param height    This is the height of the bitmap in game units
     * @param direction This gives information about which direction the boomerang is fired
     * @param mapScreen This specifies the mapScreen to associate the boomerang with
     */
    public Boomerang(float xPos, float yPos, float width, float height, Direction direction, MapScreen mapScreen) {
        super(xPos, yPos, width, height, width * 0.9f, height * 0.9f, mapScreen);

        //creating a reference to the assetStore to be used to get/create assets
        AssetStore assetStore = mapScreen.getGame().getAssetStore();

        //specifying the items name
        itemName = Inventory.AmmoType.BOOMERANG.toString();

        //assigning a bitmap for each frame of animation to boomerang
        Bitmap boomerangBitmap = assetStore.getBitmap("Boomerang", "img/Items/Boomerang.png");
        boomerang = new Animation(assetStore, "Boomerang", boomerangBitmap, 8, 8, 1, 12);
        Frame startFrame = boomerang.getCurrentFrame();
        bitmap = startFrame.getFrameBitmap();

        //depending on Direction the movement vector is adjusted accordingly
        switch (direction) {
            case UP:
                movementVector = new Vector2(0, -5);
                break;
            case DOWN:
                movementVector = new Vector2(0, 5);
                break;
            case LEFT:
                movementVector = new Vector2(-5, 0);
                break;
            case RIGHT:
                movementVector = new Vector2(5, 0);
                break;
        }
        boomerangSound = assetStore.getSfx("boomerang", "audio/sfx/Boomerang.wav");
        streamID = boomerangSound.play(false);

    }

    /**
     * This method checks if the boomerang has been thrown to the point of return
     * then makes it come back if it has and continues moving if it hasn't.
     * It also updates the boomerang frame and checks if the boomerang has collided with a wall/enemy
     * Variables needed for boomerang
     *
     * @param elapsedTime This decreases the boomerangReturn time and acts as a timer
     */
    @Override
    public void update(ElapsedTime elapsedTime) {
        timeToReturn -= elapsedTime.stepTime;

        if (timeToReturn <= 0) {
            returnToPlayer(mapScreen.getPlayer());
            checkPlayerCollision();
        } else {
            updatePosition(movementVector.x, movementVector.y);
        }
        bitmap = boomerang.getCurrentFrame().getFrameBitmap();
        checkEntityCollision();
        if (checkTileCollision()) {
            timeToReturn = 0;
        }
        checkOutOfMap();
    }

    /**
     * This method checks if the boomerang has collided with an Entity
     */
    private void checkEntityCollision() {
        for (Enemy enemy : mapScreen.enemies) {
            CollisionDetector.CollisionType collisionType = CollisionDetector.determineAndResolveCollision(enemy, this, false);
            switch (collisionType) {
                case Top:
                    enemy.state = Entity.entityState.FLINCHING;
                    enemy.bounceBack.set(0, 2);
                    returning = true;
                    enemy.takeDamage(damage);
                    break;
                case Bottom:
                    enemy.state = Entity.entityState.FLINCHING;
                    enemy.bounceBack.set(0, -2);
                    returning = true;
                    enemy.takeDamage(damage);
                    break;
                case Right:
                    enemy.state = Entity.entityState.FLINCHING;
                    enemy.bounceBack.set(-2, 0);
                    returning = true;
                    enemy.takeDamage(damage);
                    break;
                case Left:
                    enemy.state = Entity.entityState.FLINCHING;
                    enemy.bounceBack.set(2, 0);
                    returning = true;
                    enemy.takeDamage(damage);
                    break;
                case None:
                    break;
            }
        }
    }

    /**
     * This method checks if the boomerang has returned to the player destroys the bitmap (alive = false),
     * gives them the ability to throw the boomerang again
     */
    private void checkPlayerCollision() {
        Player player = mapScreen.getPlayer();
        CollisionDetector.CollisionType collisionType = CollisionDetector.determineCollisionType(player, this, false);
        if (collisionType != CollisionDetector.CollisionType.None) {
            alive = false;
            mapScreen.getPlayer().inventory.increaseItem(Inventory.AmmoType.BOOMERANG, 1);
            boomerangSound.stop(streamID);
        } else {
            alive = true;
        }
    }

    /**
     * This method is called when the boomerang starts to return to the player, AI pathfinding brings it back
     *
     * @param player This gives a player object for the boomerang to return to using AI pathfinding
     */
    private void returnToPlayer(Player player) {
        path = AIUtil.FindPath(position, player.position, mapScreen.tileMap.getPathGrid());
        updateMovement();
    }

    /**
     * Updates the vector for the boomerang to move by
     */
    private void updateMovement() {
        if (!path.isEmpty()) {
            BoundingBox bound = getCollisionBox();
            if (bound.contains(path.get(0).x, path.get(0).y)) {
                path.remove(0);
                if (!path.isEmpty()) {
                    movementVector = SteeringUtil.seek(position, path.get(0), 5);
                } else {
                    movementVector.set(0, 0);
                }
            }
            updatePosition(movementVector.x, movementVector.y);
        } else {
            movementVector.set(0, 0);
        }
    }

    /**
     * Checks if the boomerang is thrown out of bounds and returns it to your inventory if it is
     */
    public void checkOutOfMap() {
        if (position.x < 0 || position.x > mapScreen.tileMap.getMapWidth()) {
            alive = false;
            mapScreen.getPlayer().inventory.increaseItem(Inventory.AmmoType.BOOMERANG, 1);
            boomerangSound.stop(streamID);
        } else if (position.y < 0 || position.y > mapScreen.tileMap.getMapHeight()) {
            alive = false;
            mapScreen.getPlayer().inventory.increaseItem(Inventory.AmmoType.BOOMERANG, 1);
            boomerangSound.stop(streamID);
        }
    }
}
