package ritogaems.tov.gameEngine.Input;

import ritogaems.tov.gameEngine.items.Consumable;
import ritogaems.tov.world.screens.GameScreen;

/**
 * @author Zoey Longridge
 *         <p/>
 *         Create Inventory Buttons
 */
public class InventoryButton extends Button {

    /**
     * set the current item of the consumable
     */
    public Consumable currentItem = null;

    /**
     * Constructor to create the button from specific
     * co-ordinates and sizes in pixels
     *
     * @param xPos       The x co-ordinate in pixels
     * @param yPos       The y co-ordinate in pixels
     * @param width      The width in pixels
     * @param height     The height in pixels
     * @param gameScreen The game screen the object belongs to
     */
    public InventoryButton(float xPos, float yPos, int width, int height, GameScreen gameScreen) {
        super(xPos, yPos, width, height, true, gameScreen);
    }

    /**
     * Getter
     *
     * @return the consumable type of the current item
     */
    public Consumable.ConsumableType getConsumableType() {
        return currentItem.getConsumableType();
    }

    public Consumable getCurrentItem() {
        return currentItem;
    }

    /**
     * Set the current item as the parameter and set the icon
     *
     * @param currentItem
     */
    public void setCurrentItem(Consumable currentItem) {
        this.currentItem = currentItem;
        this.setIcon(currentItem.getBitmap());
    }

    /**
     * remove the current item
     *
     * @return consumable
     */
    public Consumable removeCurrentItem() {
        Consumable consumable = this.currentItem;
        this.currentItem = null;
        this.setIcon(null);
        return consumable;
    }

}