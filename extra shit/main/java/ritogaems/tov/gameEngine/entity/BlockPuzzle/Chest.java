package ritogaems.tov.gameEngine.entity.BlockPuzzle;

import android.util.Log;

import ritogaems.tov.gameEngine.audio.SoundEffect;
import ritogaems.tov.gameEngine.graphics.animation.Sprite;
import ritogaems.tov.gameEngine.items.AmmoDrop;
import ritogaems.tov.gameEngine.items.Consumable;
import ritogaems.tov.gameEngine.items.DamagePotion;
import ritogaems.tov.gameEngine.items.HealthPotion;
import ritogaems.tov.gameEngine.items.Inventory;
import ritogaems.tov.gameEngine.items.Key;
import ritogaems.tov.gameEngine.items.SpeedPotion;
import ritogaems.tov.util.CollisionDetector;
import ritogaems.tov.world.screens.MapScreen;

/**
 * @author Zoey Longridge
 *
 * A chest that the player can interact with
 */
public class Chest extends Sprite {

    /**
     * Mapscreen that the chest belongs on
     */
    MapScreen mapScreen;

    /**
     * boolean to get consumble from chest once
     */
    boolean getConsumableOnce;

    /**
     * ConsumbleType stored
     */
    Consumable.ConsumableType consumableType;
    private SoundEffect chestSound;

    ////////////////
    // METHODS
    ////////////////

    /**
     * Constructor for Chest
     *
     * @param xPos            X position
     * @param yPos            Y position
     * @param drawWidth       Width of the draw box
     * @param drawHeight      Height of the draw box
     * @param collisionWidth  Width of the collision box
     * @param collisionHeight Height of the collision box
     * @param mapScreen       Get access to other game objects in map
     * @param consumableType  Type of cosumable stored
     */
    public Chest (float xPos, float yPos, float drawWidth, float drawHeight,
                float collisionWidth, float collisionHeight, MapScreen mapScreen, Consumable.ConsumableType consumableType) {
        super(xPos, yPos, drawWidth, drawHeight, collisionWidth, collisionHeight);
        this.mapScreen = mapScreen;
        this.consumableType = consumableType;
        getConsumableOnce= false;
        bitmap = mapScreen.getGame().getAssetStore().getBitmap("ChestClosed", "img/Puzzle/ClosedChest.png");
    }

    /**
     * Method to update the player collision with chest on this frame of the game loop
     */
    public void update(){
        checkPlayerCollision();
    }

    /**
     *If this block collides with the player, then resolve the collision with the player moving back
     * if player collides with its top on the chest, change the bitmap of the chest
     * Player will receive consumable found in chest into their backpack
     * @return collisionFound
     */
    protected boolean checkPlayerCollision() {
        boolean collisionFound = false;
        CollisionDetector.CollisionType collisionType = CollisionDetector.determineAndResolveCollision(mapScreen.getPlayer(),this, true);
        switch (collisionType) {
            case Top:
                bitmap  = mapScreen.getGame().getAssetStore().getBitmap("ChestOpen", "img/Puzzle/OpenChest.png");
                if(!getConsumableOnce){
                    switch(consumableType){
                        case HEALTHPOTION:
                            HealthPotion healthPotion = new HealthPotion(0,0,10,false, mapScreen);
                            mapScreen.getPlayer().getBackPack().pickUpItem(healthPotion);
                            healthPotion.isAlive = false;
                            break;
                        case DAMAGEPOTION:
                            DamagePotion damagePotion = new DamagePotion(0,0,10,false, mapScreen);
                            mapScreen.getPlayer().getBackPack().pickUpItem(damagePotion);
                            damagePotion.isAlive = false;
                            break;
                        case SPEEDPOTION:
                            SpeedPotion speedPotion = new SpeedPotion(0,0,false, mapScreen);
                            mapScreen.getPlayer().getBackPack().pickUpItem(speedPotion);
                            speedPotion.isAlive = false;
                            break;
                        case KEY:
                            Key key = new Key(0,0,false, mapScreen);
                            mapScreen.getPlayer().getBackPack().pickUpItem(key);
                            key.isAlive = false;
                            break;
                        case AMMO:
                            AmmoDrop ammoDrop = new AmmoDrop(0,0,0,0,10, Inventory.AmmoType.ARROW, false, mapScreen);
                            mapScreen.getPlayer().getBackPack().pickUpItem(ammoDrop);
                            ammoDrop.isAlive = false;
                            break;
                    }
                    chestSound = mapScreen.getGame().getAssetStore().getSfx("get chest item", "audio/sfx/Consumables/GetItemFromChest.wav");
                    chestSound.play(false);
                    getConsumableOnce = true;
                }
                break;
        }
        if (collisionType != CollisionDetector.CollisionType.None) {
            collisionFound = true;
        }
        return collisionFound;
    }

}
