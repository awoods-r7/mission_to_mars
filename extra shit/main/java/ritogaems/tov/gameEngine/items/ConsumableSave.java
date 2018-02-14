package ritogaems.tov.gameEngine.items;

import java.io.Serializable;

import ritogaems.tov.world.screens.MapScreen;

/**
 * @@author Michael Purdy
 */
public class ConsumableSave implements Serializable {

    /**
     * Is the backpack slot empty (consumable == null)
     */
    private boolean empty;

    /**
     * Stored consumable properties
     */
    private float x, y;
    private float width, height;
    private int value;
    private Consumable.ConsumableType type;
    private Inventory.AmmoType ammoType;

    /**
     * Constructor
     *
     * @param consumable The consumable to save
     */
    public ConsumableSave(Consumable consumable) {
        if (consumable != null) {
            this.empty = false;
            this.x = consumable.position.x;
            this.y = consumable.position.y;
            this.width = consumable.getDrawBox().getWidth();
            this.height = consumable.getDrawBox().getHeight();
            this.type = consumable.getConsumableType();

            switch (consumable.getConsumableType()) {
                case AMMO:
                    this.value = ((AmmoDrop) consumable).getAmount();
                    this.ammoType = ((AmmoDrop) consumable).getAmmoType();
                    break;
                case HEALTHPOTION:
                    this.value = ((HealthPotion) consumable).getHealth();
                    break;
                case DAMAGEPOTION:
                    this.value = ((DamagePotion) consumable).getDamage();
                    break;
            }
        } else {
            this.empty = true;
        }
    }

    /**
     * Create a consumable object from the consumable save
     *
     * @param mapScreen The map screen the object belongs to
     * @return Consumable with properties saved
     */
    public Consumable unPack(MapScreen mapScreen) {
        if (empty) return null;
        switch (type) {
            case AMMO:
                return new AmmoDrop(x, y, width, height, value, ammoType, false, mapScreen);
            case KEY:
                return new Key(x, y, false, mapScreen);
            case DAMAGEPOTION:
                return new DamagePotion(x, y, value, false, mapScreen);
            case SPEEDPOTION:
                return new SpeedPotion(x, y, false, mapScreen);
            case HEALTHPOTION:
                return new HealthPotion(x, y, value, false, mapScreen);

        }
        return null;
    }
}
