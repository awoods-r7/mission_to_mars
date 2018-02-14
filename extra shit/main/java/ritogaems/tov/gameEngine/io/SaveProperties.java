package ritogaems.tov.gameEngine.io;

import java.io.Serializable;
import java.util.ArrayList;

import ritogaems.tov.gameEngine.Difficulty;
import ritogaems.tov.gameEngine.entity.Player;
import ritogaems.tov.gameEngine.items.BackPack;
import ritogaems.tov.gameEngine.items.Consumable;
import ritogaems.tov.gameEngine.items.ConsumableSave;
import ritogaems.tov.gameEngine.items.Inventory;
import ritogaems.tov.util.Vector2;
import ritogaems.tov.world.screens.MapScreen;

/**
 * @author Michael Purdy
 *         <p/>
 *         Properties of TOV that can be saved and loaded
 */
public class SaveProperties implements Serializable {

    // /////////////////////////////
    // Player Properties
    // /////////////////////////////

    /**
     * The position of the player in the map
     */
    private Vector2 playerPosition = new Vector2();

    /**
     * The current health of the player
     */
    private int currentHealth;

    /**
     * The player's inventory
     */
    private Inventory inventory;

    /**
     * The items the player is holding
     */
    private ArrayList<ConsumableSave> consumables;

    // /////////////////////////////
    // Game Properties
    // /////////////////////////////

    /**
     * The name of the map the player is currently in
     */
    private String mapName;

    /**
     * The difficulty level of the game
     */
    private Difficulty difficulty;

    /**
     * Constructor to take relevant properties from player
     *
     * @param player Player object
     */
    public SaveProperties(Player player, String mapName) {

        // store the player's properties
        this.playerPosition = player.getPosition();
        this.currentHealth = player.getCurrentHealth();
        this.inventory = player.inventory;
        this.consumables = player.backPack.getBackPackSave();

        this.mapName = mapName;
        this.difficulty = player.getDifficulty();

    }

    /**
     * Getter
     *
     * @return saved current screen name
     */
    public String getScreenName() {
        return mapName;
    }

    /**
     * Getter
     *
     * @return saved game difficulty
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Get player from properties that are saved
     *
     * @param screen The screen to add the player to
     * @return The player loaded from the properties
     */
    public Player getPlayer(MapScreen screen) {
        Player player = new Player(playerPosition.x, playerPosition.y,
                currentHealth, screen);

        player.inventory = this.inventory;
        player.backPack.loadBackPack(consumables, screen);

        return player;
    }
}
