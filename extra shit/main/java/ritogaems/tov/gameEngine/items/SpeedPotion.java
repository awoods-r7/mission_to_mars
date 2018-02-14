package ritogaems.tov.gameEngine.items;

import android.graphics.Bitmap;

import ritogaems.tov.world.screens.MapScreen;
/**
 * @auther Zoey Longridge
 *
 * Class for creating the consumable Speed Potion
 */
public class SpeedPotion extends Consumable
{
    /**
     * float for amount of speed
     */
    public static float Speed = 3.5f;

    /**
     *Constructor for SpeedPotion
     *
     * @param xPos            X position
     * @param yPos            Y position
     * @param checkCollision  boolean to see if collision is needed
     * @param mapScreen       Get access to other game objects in map
     */
    public SpeedPotion (float xPos, float yPos, boolean checkCollision, MapScreen mapScreen) {
        super(xPos, yPos, 7.5f, 7.5f,
                mapScreen.getGame().getAssetStore().getBitmap("Speed Potion", "img/Items/SpeedPotion.png"),
                ConsumableType.SPEEDPOTION, checkCollision, mapScreen);
    }
}