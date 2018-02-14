package ritogaems.tov.gameEngine.entity.BlockPuzzle;

import ritogaems.tov.gameEngine.audio.SoundEffect;
import ritogaems.tov.gameEngine.graphics.animation.Sprite;
import ritogaems.tov.world.screens.MapScreen;

/**
 * @author Zoey Longridge
 *
 * A pressurePoint that the player and blocks can interact with
 */
public class PressurePoint extends Sprite {

    /**
     * Mapscreen that the chest belongs on
     */
    private MapScreen mapScreen;

    /**
     * Door object
     */
    private Door door;

    /**
     * sound effects
     */
    private SoundEffect switchPressedSound;
    private boolean switchPressed = false;

    /**
     * Constructor for PressurePoint
     *
     * @param xPos            X position
     * @param yPos            Y position
     * @param drawWidth       Width of the draw box
     * @param drawHeight      Height of the draw box
     * @param collisionWidth  Width of the collision box
     * @param collisionHeight Height of the collision box
     * @param mapScreen       Get access to other game objects in map
     * @param door            instance of  door which pressure points are associated with
     */
    public PressurePoint(float xPos, float yPos, float drawWidth, float drawHeight,
                         float collisionWidth, float collisionHeight, MapScreen mapScreen, Door door) {
        super(xPos, yPos, drawWidth, drawHeight, collisionWidth, collisionHeight);
        this.door = door;
        this.mapScreen = mapScreen;
        bitmap = mapScreen.getGame().getAssetStore().getBitmap("Pressure Point", "img/Puzzle/PressurePoint.png");

        switchPressedSound = mapScreen.getGame().getAssetStore().getSfx("switch pressed", "audio/sfx/Puzzle/SwitchPressed.wav");
    }

    /**
     * If all pressure points have intersected with a block
     * then set doorOpen to true
     * else doorOpen is false
     * @return doorOpen
     */
    public boolean isPressed() {
        boolean doorOpen = false;
        for (Block block : mapScreen.blocks) {
            if (this.collisionBox.intersects(block.getCollisionBox())) {
                if (!switchPressed) {
                    switchPressedSound.play(false);
                    switchPressed = true;
                }
                doorOpen = true;
                break;
            }
            door.isOpen = false;
        }
        return doorOpen;
    }
}
