package ritogaems.tov.gameEngine.items;

import ritogaems.tov.world.screens.MapScreen;

/**
 * @auther Zoey Longridge
 *
 * Class for creating the consumable Key
 */
public class Key extends Consumable {

    /**
     *Constructor for Key
     *
     * @param xPos            X position
     * @param yPos            Y position
     * @param checkCollision  boolean to see if collision is needed
     * @param mapScreen       Get access to other game objects in map
     */
    public Key (float xPos, float yPos, boolean checkCollision, MapScreen mapScreen) {
        super(xPos, yPos, 7.5f, 7.5f,
                mapScreen.getGame().getAssetStore().getBitmap("Key", "img/Items/Key.png"),
                ConsumableType.KEY, checkCollision, mapScreen);
    }
}