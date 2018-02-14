package ritogaems.tov.gameEngine;

import java.util.HashMap;
import java.util.Map;

import ritogaems.tov.gameEngine.entity.Player;
import ritogaems.tov.world.screens.GameScreen;
import ritogaems.tov.world.screens.MapScreen;

/**
 * @author Michael Purdy
 *         Manages the current screen and navigating screens
 *         <p/>
 *         Edited from Gage
 *         - added last play screen code
 *         - added changing background music
 */
public class ScreenManager {

    /**
     * Map of available game screens
     */
    private Map<String, GameScreen> gameScreens;

    /**
     * current game screen
     */
    private GameScreen currentScreen;

    /**
     * The last play screen (used for returning from
     * in game menu)
     */
    private GameScreen lastPlayScreen;

    /**
     * Constructor
     */
    public ScreenManager() {
        gameScreens = new HashMap<>();
        currentScreen = null;
    }

    /**
     * adds the specified game screen to the manager - first game screen automatically becomes current screen
     *
     * @param screen The screen to add
     */
    public void addScreen(GameScreen screen) {
        // add the game screen if the name isn't already added
        if (gameScreens.containsKey(screen.getName())) {
            return;
        }
        gameScreens.put(screen.getName(), screen);

        // if this is the first screen, set it as default game screen
        if (gameScreens.size() == 1) {
            currentScreen = screen;
        }
    }

    /**
     * replace any screen with the same name with a new screen
     *
     * @param screen The new screen to add
     */
    public void replaceScreen(GameScreen screen) {
        if (gameScreens.containsKey(screen.getName())) {
            gameScreens.remove(screen.getName());
        }
        gameScreens.put(screen.getName(), screen);
    }

    /**
     * set the specified screen as the current screen
     * (pause the music from the previous screen and play
     * the new music)
     *
     * @param name The name of the new screen
     */
    public void setAsCurrentScreen(String name) {
        currentScreen.pauseMusic();
        GameScreen currentScreen = gameScreens.get(name);
        if (currentScreen != null) {
            this.currentScreen = currentScreen;
            this.currentScreen.playMusic();
            if (currentScreen.getType() == GameScreen.ScreenType.PLAY) {
                lastPlayScreen = currentScreen;
            } else {

                // if the player returns to the main menu then clear the last play screen
                if (currentScreen.getName().equals("startMenu")) {
                    lastPlayScreen = null;
                }
            }
        }
    }

    /**
     * Return the the previous play screen
     * (used when exiting the in game menu)
     */
    public void returnToLastPlayScreen() {
        if (lastPlayScreen != null) {
            setAsCurrentScreen(lastPlayScreen.getName());
        }
    }

    /**
     * get the current game screen
     *
     * @return the current game screen
     */
    public GameScreen getCurrentScreen() {
        return currentScreen;
    }

    /**
     * get the specified screen
     *
     * @param name The name of the screen
     * @return The screen specified
     */
    public GameScreen getScreen(String name) {
        return gameScreens.get(name);
    }

    /**
     * Getter
     *
     * @return the last play screen
     */
    public MapScreen getLastPlayScreen() {
        return (MapScreen) lastPlayScreen;
    }

    /**
     * remove the specified screen
     *
     * @param name The screen to remove
     * @return if the game screen existed
     */
    public boolean removeScreen(String name) {
        GameScreen gameScreen = gameScreens.remove(name);
        return (gameScreen != null);
    }

    /**
     * dispose of this manager and all game screens
     */
    public void dispose() {
        for (GameScreen gameScreen : gameScreens.values()) {
            gameScreen.dispose();
        }
    }

    /**
     * Getter
     *
     * @return the player from the last play screen
     */
    public Player getPlayer() {
        return lastPlayScreen.getPlayer();
    }

    /**
     * Getter
     *
     * @return The name of the last play screen
     */
    public String getLastPlayScreenName() {
        if (lastPlayScreen == null) return "";
        return lastPlayScreen.getName();
    }
}