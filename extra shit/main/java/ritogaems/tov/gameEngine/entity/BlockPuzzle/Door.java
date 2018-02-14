package ritogaems.tov.gameEngine.entity.BlockPuzzle;

import ritogaems.tov.gameEngine.audio.SoundEffect;
import ritogaems.tov.gameEngine.entity.Enemy;
import ritogaems.tov.gameEngine.graphics.animation.Sprite;
import ritogaems.tov.gameEngine.items.Consumable;
import ritogaems.tov.util.CollisionDetector;
import ritogaems.tov.world.screens.MapScreen;
/**
 * @author Zoey Longridge
 *
 * A door that the player can interact with
 */
public class Door extends Sprite {
    /**
     * Mapscreen that the chest belongs on
     */
    private MapScreen mapScreen;

    /**
     * Boolean to see if door is open
     */
    boolean isOpen;
    private SoundEffect keyUnlockSound;

    /**
     * Constructor for Door
     *
     * @param xPos            X position
     * @param yPos            Y position
     * @param drawWidth       Width of the draw box
     * @param drawHeight      Height of the draw box
     * @param collisionWidth  Width of the collision box
     * @param collisionHeight Height of the collision box
     * @param mapScreen       Get access to other game objects in map
     */
    public Door(float xPos, float yPos, float drawWidth, float drawHeight,
                      float collisionWidth, float collisionHeight, MapScreen mapScreen) {
        super(xPos, yPos, drawWidth, drawHeight, collisionWidth, collisionHeight);
        this.mapScreen = mapScreen;
        isOpen= false;
        bitmap = mapScreen.getGame().getAssetStore().getBitmap("Door", "img/Puzzle/Door.png");

        keyUnlockSound = mapScreen.getGame().getAssetStore().getSfx("unlock doors key", "audio/sfx/Puzzle/DoorKeyUnlock.wav");
    }

    /**
     * Method to update the player and enemy collision with door on this frame of the game loop
     */
    public void update(){
        checkPlayerCollision();
        checkEnemyCollision();
    }

    /**
     * Getter
     * @return isOpen
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     *If this Door collides with a enemy, then resolve the collision with the enemy moving back
     */
    private void checkEnemyCollision() {
        for(Enemy enemy: mapScreen.enemies) {
            CollisionDetector.determineAndResolveCollision(enemy,this,false);
        }
    }

    /**
     *If this door collides with the player, then resolve the collision with the player moving back
     * if player collides with its top on the door, check if they have an instance of key in their backpack
     * if there is an instance, remove the consumable from backpack and set isOpen to true
     */
    private void checkPlayerCollision() {
        CollisionDetector.CollisionType collisionType = CollisionDetector.determineAndResolveCollision(mapScreen.getPlayer(),this, true);
        switch (collisionType) {
            case Top:
                if(mapScreen.getPlayer().getBackPack().removeConsumable(Consumable.ConsumableType.KEY)) {
                    isOpen = true;
                    keyUnlockSound.play(false);
                }
            break;
        }
    }
}
