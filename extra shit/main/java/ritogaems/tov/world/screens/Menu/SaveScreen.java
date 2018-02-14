package ritogaems.tov.world.screens.Menu;

import ritogaems.tov.GameFragment;
import ritogaems.tov.gameEngine.ScreenManager;

/**
 * @author Michael Purdy
 *         Screen for saving the game
 */
public class SaveScreen extends SavesDisplayScreen {

    /**
     * Default constructor for a Menu Screen
     * sets the background image for the menu and the
     * default menu background music
     *
     * @param game The GameFragment that the menu screen is shown on
     * @param name The name of the menu screen
     */
    SaveScreen(GameFragment game, String name) {
        super(game, name);
        this.message = "Select a save file to save.";
        setPreviousScreen("inGameMenuScreen");
    }

    /**
     * Save to the selected slot
     *
     * @param name The name of the button pressed
     */
    @Override
    public void doMenuAction(String name) {
        super.doMenuAction(name);
        if (name.substring(0, 8).equals("SaveIcon")) {
            int saveNumber = Integer.parseInt(name.substring(8));

            ScreenManager screenManager = game.getScreenManager();

            if (screenManager.getLastPlayScreen().canSave()) {
                getFileHandler().save(game.getScreenManager().getPlayer(), getGame().getScreenManager().getLastPlayScreenName(), saveNumber);

                message = "Saved game " + (saveNumber + 1);
            } else {
                message = "You cannot save now!";
            }
        }
    }
}
