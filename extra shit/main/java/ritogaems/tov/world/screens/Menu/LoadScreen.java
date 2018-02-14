package ritogaems.tov.world.screens.Menu;

import ritogaems.tov.GameFragment;
import ritogaems.tov.gameEngine.ScreenManager;
import ritogaems.tov.gameEngine.io.SaveProperties;
import ritogaems.tov.world.screens.MapScreen;

/**
 * @author Michael Purdy
 *         Screen for loading the game
 */
public class LoadScreen extends SavesDisplayScreen {

    /**
     * Default constructor for a Menu Screen
     * sets the background image for the menu and the
     * default menu background music
     *
     * @param game The GameFragment that the menu screen is shown on
     * @param name The name of the menu screen
     */
    LoadScreen(GameFragment game, String name) {
        super(game, name);
        this.message = "Select a save file to load.";
    }

    /**
     * Load the save selected
     *
     * @param name The name of the button pressed
     */
    @Override
    public void doMenuAction(String name) {
        super.doMenuAction(name);
        if (name.substring(0, 8).equals("SaveIcon")) {
            ScreenManager screenManager = game.getScreenManager();
            int saveNumber = Integer.parseInt(name.substring(8));
            SaveProperties properties = getFileHandler().load(saveNumber);
            if (properties != null) {
                screenManager.setAsCurrentScreen("loadingScreen");
                StartScreen.createGameScreens(game);
                game.difficulty = properties.getDifficulty();
                MapScreen currentMapScreen = (MapScreen) (screenManager.getScreen(properties.getScreenName()));
                currentMapScreen.replacePlayer(properties.getPlayer(currentMapScreen));
                game.getScreenManager().setAsCurrentScreen(properties.getScreenName());
            } else {
                this.message = "Save " + (saveNumber + 1) + " is empty";
            }
        }
    }

}
