package ritogaems.tov.gameEngine.items;

import android.graphics.Bitmap;

import ritogaems.tov.world.screens.MapScreen;

/**
 * @auther Zoey Longridge
 * <p/>
 * Class for creating the consumable Health Potion
 */
public class HealthPotion extends Consumable {

    /**
     * int for amount of health to be returned
     */
    private int health;

    /**
     * Constructor for Health Potion
     *
     * @param xPos           X position
     * @param yPos           Y position
     * @param health         amount of health
     * @param checkCollision boolean to see if collision is needed
     * @param mapScreen      Get access to other game objects in map
     */
    public HealthPotion(float xPos, float yPos, int health, boolean checkCollision, MapScreen mapScreen) {
        super(xPos, yPos, 7.5f, 7.5f,
                mapScreen.getGame().getAssetStore().getBitmap("Health Potion", "img/Items/HealthPotion.png"),
                ConsumableType.HEALTHPOTION, checkCollision, mapScreen);
        this.health = health;
    }

    /**
     * Getter
     *
     * @return health
     */
    public int getHealth() {
        return health;
    }
}