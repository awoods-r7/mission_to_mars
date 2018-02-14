package ritogaems.tov.gameEngine.items;

import android.graphics.Bitmap;

import java.io.Serializable;

import ritogaems.tov.gameEngine.audio.SoundEffect;
import ritogaems.tov.gameEngine.graphics.animation.Sprite;
import ritogaems.tov.util.CollisionDetector;
import ritogaems.tov.world.GameObject;
import ritogaems.tov.world.screens.MapScreen;

/**
 * @author Zoey Longridge
 *         <p/>
 *         A consumable item that can be stored in backpack
 */
public class Consumable extends Sprite {

    /**
     * Enums created for different types of consumables
     */
    public enum ConsumableType {
        HEALTHPOTION, SPEEDPOTION, DAMAGEPOTION, KEY, AMMO
    }

    ////////////////
    // PROPERTIES
    ///////////////

    /**
     * Mapscreen that the blocks belongs on
     */
    private MapScreen mapScreen;

    /**
     * boolean for whether the consumable will be present on screen
     */
    public boolean isAlive;

    /**
     * instance of consumable sound
     */
    private SoundEffect consumableSound;

    /**
     * instance of consumable Type
     */
    ConsumableType consumableType;

    ////////////////
    // METHODS
    ////////////////

    /**
     * Constructor for consumable
     *
     * @param xPos           X position
     * @param yPos           Y position
     * @param drawWidth      Width of the draw box
     * @param drawHeight     Height of the draw box
     * @param bitmap         bitmap of consumable
     * @param consumableType Type of enum that consumable is
     * @param checkCollision boolean to check if collision is needed
     * @param mapScreen      Get access to other game objects in map
     */
    Consumable(float xPos, float yPos, float drawWidth, float drawHeight, Bitmap bitmap, ConsumableType consumableType, boolean checkCollision, MapScreen mapScreen) {
        super(xPos, yPos, drawWidth, drawHeight, bitmap);
        this.mapScreen = mapScreen;
        this.consumableType = consumableType;
        isAlive = true;
        // sometimes the collision shouldn't be checked - eg: when creating a map and
        // the collidable tiles haven't yet been created
        if (checkCollision) checkTileCollision();
    }

    /**
     * Getter
     *
     * @return consumableType
     */
    public ConsumableType getConsumableType() {
        return consumableType;
    }

    /**
     * Method to update the consumable on whether is will be present or not on this frame of the game loop
     *
     * @return
     */
    public boolean update() {
        if (checkPlayerCollision()) {
            isAlive = false;
            return true;
        }
        return false;
    }

    /**
     * Getter
     *
     * @return isAlive
     */
    public boolean isAlive() {
        return isAlive;
    }

    /**
     * If this consumable collides with a tile, then resolve the collision with the consumable moving back
     */
    private void checkTileCollision() {
        for (GameObject object : mapScreen.tileMap.getCollidableTiles().values()) {
            CollisionDetector.determineAndResolveCollision(this, object, true);
        }
    }

    /**
     * If this consumable collides with a player, then resolve the collision with the consumable moving back
     * if collision is found, then play sound
     *
     * @return collisionFound
     */
    private boolean checkPlayerCollision() {
        boolean collisionFound = false;
        CollisionDetector.CollisionType collisionType = CollisionDetector.determineCollisionType(this, mapScreen.getPlayer(), false);
        if (collisionType != CollisionDetector.CollisionType.None && mapScreen.getPlayer().getBackPack().inventoryFull()) {
            collisionFound = true;
            consumableSound = mapScreen.getGame().getAssetStore().getSfx("get item", "audio/sfx/Consumables/GetItem.wav");
            consumableSound.play(false);
        }
        return collisionFound;
    }
}