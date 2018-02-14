package ritogaems.tov.gameEngine.items;

import android.graphics.Bitmap;

import ritogaems.tov.world.screens.MapScreen;
/**
 * @auther Zoey Longridge
 *
 * Class for creating the consumable Damage Potion
 */
public class DamagePotion extends Consumable
{
    /**
     * int for amount of damage dealt
     */
    private int damage;

    /**
     *Constructor for DamagePotion
     *
     * @param xPos            X position
     * @param yPos            Y position
     * @param damage          amount of damage
     * @param checkCollision  boolean to see if collision is needed
     * @param mapScreen       Get access to other game objects in map
     */
    public DamagePotion(float xPos, float yPos, int damage, boolean checkCollision, MapScreen mapScreen) {
        super(xPos, yPos, 7.5f, 7.5f,
                mapScreen.getGame().getAssetStore().getBitmap("Damage Potion", "img/Items/DamagePotion.png"),
                ConsumableType.DAMAGEPOTION, checkCollision, mapScreen);
        this.damage = damage;
    }

    /**
     * Getter
     * @return damage
     */
    public int getDamage(){
        return damage;
    }

}