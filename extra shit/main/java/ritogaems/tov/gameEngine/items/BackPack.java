package ritogaems.tov.gameEngine.items;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;

import java.io.Serializable;
import java.util.ArrayList;

import ritogaems.tov.gameEngine.AssetStore;
import ritogaems.tov.gameEngine.Input.Input;
import ritogaems.tov.gameEngine.Input.InventoryButton;
import ritogaems.tov.gameEngine.Input.TouchEvent;
import ritogaems.tov.gameEngine.entity.Player;
import ritogaems.tov.world.screens.MapScreen;

/**
 * @author Zoey Longridge
 *         <p/>
 *         Creates a backpack which stores consumable items
 */
public class BackPack {

    ////////////////
    // PROPERTIES
    ////////////////

    /**
     * Mapscreen that the chest belongs on
     */
    private MapScreen mapScreen;

    /**
     * ArrayList of InventoryButtons
     */
    private ArrayList<InventoryButton> inventoryButtons = new ArrayList<>();

    /**
     * Stored bitmap of backpack background
     */
    private Bitmap popUpBackground;

    /**
     * The left side of the backpack background
     */
    private float left;

    /**
     * The top side of the backpack background
     */
    private float top;

    /**
     * Boolean for if Backpack is open
     */
    public boolean isOpen = false;

    /**
     * Int for the id of  button held in
     */
    private int longPressID;

    /**
     * Instance of a touch event
     */
    private TouchEvent event = new TouchEvent();

    /**
     * A consumable that is lifted to be moved to another slot
     */
    private Consumable consumableLifted;

    /**
     * Bitmap of image that is lifted
     */
    private Bitmap consumableLiftedBitmap;

    /**
     * Index for accessing touch events to check for touch and drag
     */
    private int index = -1;


    /**
     * Constructor for BackPack
     *
     * @param xPos       X position
     * @param yPos       Y position
     * @param drawWidth  Width of the draw box
     * @param drawHeight Height of the draw box
     * @param mapScreen  Get access to other game objects in map
     */
    public BackPack(float xPos, float yPos, float drawWidth, float drawHeight, AssetStore assetStore, DisplayMetrics metrics, MapScreen mapScreen) {

        int size = (int) Math.min(metrics.heightPixels * 0.2f, metrics.widthPixels * 0.2f);
        this.mapScreen = mapScreen;

        addInventoryButton(new InventoryButton(metrics.widthPixels * 0.3f, metrics.heightPixels * 0.4f, size, size, mapScreen));
        addInventoryButton(new InventoryButton(metrics.widthPixels * 0.4f, metrics.heightPixels * 0.4f, size, size, mapScreen));
        addInventoryButton(new InventoryButton(metrics.widthPixels * 0.5f, metrics.heightPixels * 0.4f, size, size, mapScreen));
        addInventoryButton(new InventoryButton(metrics.widthPixels * 0.6f, metrics.heightPixels * 0.4f, size, size, mapScreen));
        addInventoryButton(new InventoryButton(metrics.widthPixels * 0.7f, metrics.heightPixels * 0.4f, size, size, mapScreen));

        addInventoryButton(new InventoryButton(metrics.widthPixels * 0.3f, metrics.heightPixels * 0.6f, size, size, mapScreen));
        addInventoryButton(new InventoryButton(metrics.widthPixels * 0.4f, metrics.heightPixels * 0.6f, size, size, mapScreen));
        addInventoryButton(new InventoryButton(metrics.widthPixels * 0.5f, metrics.heightPixels * 0.6f, size, size, mapScreen));
        addInventoryButton(new InventoryButton(metrics.widthPixels * 0.6f, metrics.heightPixels * 0.6f, size, size, mapScreen));
        addInventoryButton(new InventoryButton(metrics.widthPixels * 0.7f, metrics.heightPixels * 0.6f, size, size, mapScreen));

        Bitmap temp = assetStore.getBitmap("Inventory Screen", "img/Inventory/InventoryPopUp.png");
        popUpBackground = Bitmap.createScaledBitmap(temp, (int) drawWidth, (int) drawHeight, false);

        left = xPos - (popUpBackground.getWidth() / 2);
        top = yPos - (popUpBackground.getHeight() / 2);
    }

    /**
     * if backpack is open and a button is touched, check the type of touch
     *
     * @param player
     */
    public void update(Player player) {
        if (isOpen) {
            for (int i = 0; i < inventoryButtons.size(); i++) {
                InventoryButton inventoryButton = inventoryButtons.get(i);
                if (inventoryButton.getCurrentItem() != null) {
                    if (inventoryButton.isLongPressed()) {
                        if (longPressID == -1) {
                            longPressID = inventoryButton.getPrevID();
                            index = i;
                            this.consumableLifted = inventoryButton.removeCurrentItem();

                            DisplayMetrics metrics = new DisplayMetrics();
                            mapScreen.getGame().getActivity().getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
                            consumableLiftedBitmap = Bitmap.createScaledBitmap(consumableLifted.getBitmap(), (int) (metrics.widthPixels * 0.07f), (int) (metrics.heightPixels * 0.10f), false);
                        }
                    } else if (inventoryButton.isClicked()) {
                        switch (inventoryButton.getConsumableType()) {
                            case HEALTHPOTION:
                                HealthPotion healthPotion = (HealthPotion) inventoryButton.removeCurrentItem();
                                player.restoreHealth(healthPotion.getHealth());
                                player.drinkSound.play(false);
                                inventoryButton.removeCurrentItem();
                                break;
                            case SPEEDPOTION:
                                player.updateSpeed(SpeedPotion.Speed);
                                player.drinkSound.play(false);
                                inventoryButton.removeCurrentItem();
                                break;
                            case DAMAGEPOTION:
                                DamagePotion damagePotion = (DamagePotion) inventoryButton.removeCurrentItem();
                                player.updateDamage(damagePotion.getDamage());
                                player.drinkSound.play(false);
                                inventoryButton.removeCurrentItem();
                                break;
                            case KEY:
                                break;
                            case AMMO:
                                AmmoDrop ammoDrop = (AmmoDrop) inventoryButton.removeCurrentItem();
                                player.inventory.increaseItem(ammoDrop.getAmmoType(), ammoDrop.getAmount());
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
            touchInput(index);
        }
    }

    /**
     * Drag and drop consumbles
     *
     * @param index
     */
    private void touchInput(int index) {
        Input input = mapScreen.getGame().getInput();
        if (longPressID != -1) {

            if (!input.existsTouch(longPressID)) {
                event.type = TouchEvent.TOUCH_UP;
                longPressID = -1;

                boolean touchFound = false;
                for (InventoryButton inventoryButton : inventoryButtons) {
                    if (inventoryButton.getCollisionBox().contains(event.x, event.y)) {
                        if (inventoryButton.getCurrentItem() != null) {
                            inventoryButtons.get(index).setCurrentItem(inventoryButton.getCurrentItem());
                        }
                        touchFound = true;
                        inventoryButton.setCurrentItem(consumableLifted);
                        break;
                    }
                }
                if (!touchFound && index != -1) {
                    inventoryButtons.get(index).setCurrentItem(consumableLifted);
                }
                this.consumableLiftedBitmap = null;

            } else {
                event.x = input.getTouchX(longPressID);
                event.y = input.getTouchY(longPressID);
            }
        }
    }

    /**
     * Method to draw Backpack to screen
     *
     * @param canvas
     */
    public void draw(Canvas canvas) {
        if (isOpen) {
            canvas.drawBitmap(popUpBackground, left, top, null);
            for (InventoryButton inventoryButton : inventoryButtons) {
                inventoryButton.drawHUD(canvas);
            }
            if (consumableLiftedBitmap != null) {
                canvas.drawBitmap(consumableLiftedBitmap, event.x - (consumableLiftedBitmap.getWidth() / 2), event.y - (consumableLiftedBitmap.getHeight() / 2), null);
            }
        }
    }

    /**
     * Add inventory button to an arrayList
     *
     * @param inventoryButton
     */
    private void addInventoryButton(InventoryButton inventoryButton) {
        inventoryButtons.add(inventoryButton);
    }

    /**
     * remove first instance of the consumableType
     *
     * @param consumableType
     * @return boolean based on whether a consumable is removed
     */
    public boolean removeConsumable(Consumable.ConsumableType consumableType) {
        for (InventoryButton inventoryButton : inventoryButtons) {
            if (inventoryButton.currentItem != null) {
                if (inventoryButton.currentItem.consumableType == consumableType) {
                    inventoryButton.removeCurrentItem();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Pick up a consumable and place it in the first available button
     *
     * @param consumable
     */
    public void pickUpItem(Consumable consumable) {
        boolean isFound = false;
        for (InventoryButton inventoryButton : inventoryButtons) {
            if (!isFound) {
                if (inventoryButton.getCurrentItem() == null) {
                    inventoryButton.setCurrentItem(consumable);
                    isFound = true;
                }
            } else {
                break;
            }

        }
    }

    /**
     * Check if a backpack has an available button for an consumable
     *
     * @return spaceFound
     */
    public boolean inventoryFull() {
        boolean spaceFound = false;
        for (InventoryButton inventoryButton : inventoryButtons) {
            if (inventoryButton.getCurrentItem() == null) {
                spaceFound = true;
                break;
            }
        }
        return spaceFound;
    }

    /**
     * Gets the list of consumables the player has
     *
     * @return list of consumables
     */
    public ArrayList<ConsumableSave> getBackPackSave() {
        ArrayList<ConsumableSave> consumableSaves = new ArrayList<>();
        for (InventoryButton button : inventoryButtons) {
            consumableSaves.add(new ConsumableSave(button.getCurrentItem()));
        }
        return consumableSaves;
    }

    /**
     * Loads the player's backpack
     *
     * @param consumableSaves list of consumables
     */
    public void loadBackPack(ArrayList<ConsumableSave> consumableSaves, MapScreen mapScreen) {
        int i = 0;
        for (InventoryButton inventoryButton : inventoryButtons) {
            if (i < consumableSaves.size()) {
                Consumable consumable = consumableSaves.get(i).unPack(mapScreen);
                if (consumable != null) {
                    pickUpItem(consumable);
                } else {
                    inventoryButton.removeCurrentItem();
                }
                i++;
            } else {
                inventoryButton.removeCurrentItem();
            }
        }
    }
}