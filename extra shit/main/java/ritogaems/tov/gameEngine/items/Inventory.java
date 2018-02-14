package ritogaems.tov.gameEngine.items;


import java.io.Serializable;
import java.util.HashMap;

/**
 * The Inventory that stores the ammo of the different Items
 */
public class Inventory implements Serializable {

    /**
     * For adjusting ammo after death based on difficulty
     *
     * @param fractionOfAmmoRetainedOnDeath the ammount of current ammo retained
     */
    public void death(float fractionOfAmmoRetainedOnDeath) {
        for (InventoryItem item : inventory.values()) {
            item.currentAmmo = (int) (item.currentAmmo * fractionOfAmmoRetainedOnDeath);
        }
    }

    /**
     * enum of the different types of ammo type
     */
    public enum AmmoType {
        NONE(0), ARROW(30), BOMB(20), BOOMERANG(1);

        /**
         * The maximum amount of ammo for this type allowed
         */
        private int maxNumber;

        /**
         * Constructor for the enum
         *
         * @param maxNumber the max number of ammo for this type allowed
         */
        AmmoType(int maxNumber) {
            this.maxNumber = maxNumber;
        }

        /**
         * Returns the max value
         *
         * @return
         */
        public int getMaxNumber() {
            return maxNumber;
        }

        /**
         * Converts the enum to a lower case string for referencing
         *
         * @return enum name in lower case
         */
        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }

        /**
         * Get the name of the arrow drop
         *
         * @return a string with the name
         */
        public String getAmmoDropName() {
            return toString().substring(0, 1).toUpperCase() + toString().substring(1) + "Drop";
        }

        /**
         * Get the file path of the ammo that has been dropped
         *
         * @return a string with the file path
         */
        public String getAmmoDropFilePath() {
            return "img/Items/" + getAmmoDropName() + ".png";
        }
    }

    /**
     * The current ammo type the player is holding
     */
    private AmmoType currentAmmoType = AmmoType.NONE;

    /**
     * The InventoryItem class
     */
    private class InventoryItem implements Serializable {
        /**
         * The current and maximum ammo for the current ammo
         */
        public int currentAmmo, maxAmmo;

        /**
         * Constructor for Inventory Item
         *
         * @param currentAmmo the current ammo for this type
         * @param maxAmmo     the maximum ammo for this type
         */
        public InventoryItem(int currentAmmo, int maxAmmo) {
            this.currentAmmo = currentAmmo;
            this.maxAmmo = maxAmmo;
        }
    }

    /**
     * Hashmap for referencing the different types of Inventory Item
     */
    private HashMap<AmmoType, InventoryItem> inventory = new HashMap<>();

    /**
     * blank constructor
     */
    public Inventory() {
    }

    /**
     * Increases the amount of ammo of a specified type
     *
     * @param ammoType the type of ammo being increased
     * @param amount   the amount to increase it by
     */
    public void increaseItem(AmmoType ammoType, int amount) {
        if (!inventory.containsKey(ammoType)) {
            inventory.put(ammoType, new InventoryItem(amount, ammoType.getMaxNumber()));
        } else {
            inventory.get(ammoType).currentAmmo += amount;
            if (inventory.get(ammoType).currentAmmo > inventory.get(ammoType).maxAmmo) {
                inventory.get(ammoType).currentAmmo = inventory.get(ammoType).maxAmmo;
            }
        }
    }

    /**
     * Use one item of the specified type
     *
     * @param ammoType the type of ammo being used
     * @return true if there is ammo available
     */
    public boolean useItem(AmmoType ammoType) {
        if (inventory.get(ammoType) != null && inventory.get(ammoType).currentAmmo != 0) {
            inventory.get(ammoType).currentAmmo--;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get the ammo for a specific ammo type
     *
     * @param ammoType the typo of ammo to get
     * @return an int of the amount of ammo
     */
    public int getAmmo(AmmoType ammoType) {
        return inventory.get(ammoType).currentAmmo;
    }

    /**
     * Adjust the current ammo type that the player is using
     *
     * @param currentAmmoType the new current ammo type
     */
    public void setCurrentAmmoType(AmmoType currentAmmoType) {
        this.currentAmmoType = currentAmmoType;
    }

    /**
     * Get the current ammo type the player is using
     *
     * @return the type of ammo currently being held
     */
    public AmmoType getCurrentAmmoType() {
        return currentAmmoType;
    }

    /**
     * Get the current amount of ammo for the type the player is currently holding
     *
     * @return an int with the amount of ammo
     */
    public int getCurrentAmmoAmount() {
        InventoryItem currentItem = inventory.get(currentAmmoType);
        if (currentItem == null) return 0;
        return currentItem.currentAmmo;
    }
}