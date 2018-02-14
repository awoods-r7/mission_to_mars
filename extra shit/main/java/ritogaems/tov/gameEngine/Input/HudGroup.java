package ritogaems.tov.gameEngine.Input;

import android.graphics.Canvas;
import android.util.DisplayMetrics;

import java.util.ArrayList;

import ritogaems.tov.GameFragment;
import ritogaems.tov.gameEngine.graphics.StatusBar;
import ritogaems.tov.gameEngine.entity.Player;
import ritogaems.tov.gameEngine.items.Inventory;
import ritogaems.tov.world.screens.GameScreen;

/**
 * @author Michael Purdy
 * @author Kevin Martin
 *         Class to group all the HUD together
 *         <p/>
 *         Michael
 *         - Refactored code into group
 *         - Health bar code
 *         Kevin
 *         - wrote all other code
 */
public class HudGroup {

    /**
     * Checks if the wheel has been activated
     */
    private boolean wheelActivated;

    /**
     * The current item being held by the player
     */
    private Inventory.AmmoType currentItem;

    /**
     * The array list of all buttons in the hud group
     */
    public ArrayList<Button> buttonArrayList = new ArrayList<>();

    /**
     * Following buttons set the current item to the specified one
     */
    public Button swordButton;
    public Button bowButton;
    public Button bombButton;
    public Button boomerangButton;

    /**
     * Other HUD buttons each with a specific function
     */
    public Button menuButton;
    public Button itemButton;
    public Button noItemButton;
    public Button inventoryButton;
    public Joystick joystick;
    public StatusBar healthBar;

    /**
     * Constructor for the HUDGroup
     *
     * @param metrics for scaling objects to a % size of the screen
     * @param game    the fragment the buttons belong to
     * @param screen  the screen the buttons are drawn on
     */
    public HudGroup(DisplayMetrics metrics, GameFragment game, GameScreen screen) {
        // default size for a button
        int size = (int) Math.min(metrics.heightPixels * 0.15f, metrics.widthPixels * 0.15f);

        // create Item Wheel Buttons
        bombButton = new Button(metrics.widthPixels * 0.72f, metrics.heightPixels * 0.9f, size, size, game.getAssetStore().getBitmap("Bomb Icon", "img/HUD/Bomb Icon.png"), true, screen);
        boomerangButton = new Button(metrics.widthPixels * 0.75f, metrics.heightPixels * 0.75f, size, size, game.getAssetStore().getBitmap("Boomerang Icon", "img/HUD/Boomerang Icon.png"), true, screen);
        bowButton = new Button(metrics.widthPixels * 0.83f, metrics.heightPixels * 0.63f, size, size, game.getAssetStore().getBitmap("Bow Icon", "img/HUD/Bow Icon.png"), true, screen);
        noItemButton = new Button(metrics.widthPixels * 0.92f, metrics.heightPixels * 0.6f, size, size, null, true, screen);

        // create HUD Buttons
        swordButton = new Button(metrics.widthPixels * 0.92f, metrics.heightPixels * 0.80f, size, size, game.getAssetStore().getBitmap("Sword Icon", "img/HUD/Sword Icon.png"), true, screen);
        itemButton = new Button(metrics.widthPixels * 0.85f, metrics.heightPixels * 0.9f, size, size, null, true, screen);
        menuButton = new Button(metrics.widthPixels * 0.92f, metrics.heightPixels * 0.15f, size, size, game.getAssetStore().getBitmap("Menu Icon", "img/HUD/Menu Icon.png"), true, screen);
        inventoryButton = new Button(metrics.widthPixels * 0.92f, metrics.heightPixels * 0.32f, size, size, game.getAssetStore().getBitmap("Inventory Icon", "img/HUD/BackPack Icon.png"), true, screen);
        // create the joystick
        joystick = new Joystick(metrics.widthPixels * 0.15f, metrics.heightPixels * 0.75f, (size * 2), (size * 2), game.getAssetStore().getBitmap("JoystickImage", "img/HUD/HUD Circle.png"), screen.getGame().getInput());

        buttonArrayList.add(bombButton);
        buttonArrayList.add(boomerangButton);
        buttonArrayList.add(bowButton);
        buttonArrayList.add(noItemButton);
        buttonArrayList.add(swordButton);
        buttonArrayList.add(menuButton);
        buttonArrayList.add(itemButton);
        buttonArrayList.add(inventoryButton);
    }

    /**
     * Method to update the ammo icon
     *
     * @param player the player object for updating its current ammo type
     * @param game   the fragment
     */
    public void updateAmmoIcon(Player player, GameFragment game) {
        if (player.inventory.getCurrentAmmoType() != currentItem) {
            currentItem = player.inventory.getCurrentAmmoType();

            switch (currentItem) {
                case ARROW:
                    itemButton.setIcon(game.getAssetStore().getBitmap("Bow Icon", "img/HUD/Bow Icon.png"));
                    break;
                case BOMB:
                    itemButton.setIcon(game.getAssetStore().getBitmap("Bomb Icon", "img/HUD/Bomb Icon.png"));
                    break;
                case BOOMERANG:
                    itemButton.setIcon(game.getAssetStore().getBitmap("Boomerang Icon", "img/HUD/Boomerang Icon.png"));
                    break;
                case NONE:
                    itemButton.setIcon(null);
            }
        }
    }

    /**
     * Draw the hud group
     *
     * @param canvas The canvas to draw to
     * @param player The player to draw
     * @param game   The game to draw
     */
    public void draw(Canvas canvas, Player player, GameFragment game) {
        menuButton.drawHUD(canvas);
        swordButton.drawHUD(canvas);
        joystick.drawHUD(canvas);
        inventoryButton.drawHUD(canvas);

        // check if the item has changed
        updateAmmoIcon(player, game);

        // draw the item button
        itemButton.drawHUD(canvas);

        // draw the health bar
        if (healthBar != null)
            healthBar.drawHUD(canvas);

        // draw the wheel HUD buttons
        if (wheelActivated) {
            bombButton.drawHUD(canvas);
            boomerangButton.drawHUD(canvas);
            bowButton.drawHUD(canvas);
            noItemButton.drawHUD(canvas);
        }
    }

    /**
     * Update the Hud Group
     *
     * @param game   The game to update
     * @param player The player to update
     */
    public void update(GameFragment game, Player player) {


        healthBar.update(player.getCurrentHealth());

        if (menuButton.isClicked()) {
            game.getScreenManager().setAsCurrentScreen("inGameMenuScreen");
        }

        if (inventoryButton.isClicked()) {
            player.getBackPack().isOpen = true;
        }

        if (itemButton.isLongPressed()) {
            wheelActivated = true;
        } else if (itemButton.isClicked()) {
            switch (player.inventory.getCurrentAmmoType()) {
                case BOMB:
                    player.placeBomb();
                    break;
                case ARROW:
                    player.fireArrow();
                    break;
                case BOOMERANG:
                    player.throwBoomerang();
                    break;
                case NONE:
                    break;
            }
            wheelActivated = false;
        }

        if (wheelActivated) {
            if (bowButton.isClicked()) {
                player.inventory.setCurrentAmmoType(Inventory.AmmoType.ARROW);
                wheelActivated = false;
            } else if (bombButton.isClicked()) {
                player.inventory.setCurrentAmmoType(Inventory.AmmoType.BOMB);
                wheelActivated = false;
            } else if (boomerangButton.isClicked()) {
                player.inventory.setCurrentAmmoType(Inventory.AmmoType.BOOMERANG);
                wheelActivated = false;
            } else if (noItemButton.isClicked()) {
                player.inventory.setCurrentAmmoType(Inventory.AmmoType.NONE);
                wheelActivated = false;
            }
        }

        for (Button button : buttonArrayList) {
            button.checkForReset();
        }
    }
}
