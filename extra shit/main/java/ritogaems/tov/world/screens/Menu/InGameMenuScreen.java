package ritogaems.tov.world.screens.Menu;

import ritogaems.tov.GameFragment;
import ritogaems.tov.gameEngine.Input.MenuButton;

/**
 * @author Michael Purdy
 *         The menu screen that the player is shown when they go to
 *         the menu from a map screen.
 *         They can save, quit or change the options of the game.
 */
public class InGameMenuScreen extends MenuScreen {

    /**
     * The constructor for the in game menu screen
     * Adds a save, back and main menu button
     *
     * @param gameFragment The game fragment that the menu screen belongs to
     * @param name         The name of the menu screen
     */
    public InGameMenuScreen(GameFragment gameFragment, String name) {
        super(gameFragment, name);

        addMenuButton(new MenuButton("menuSave", "img/Menu/menuSave.png", this, 0.3f, 0.2f, 0.1f, 0.1f));
        addMenuButton(new MenuButton("menuBack", "img/Menu/menuBack.png", this, 0.3f, 0.2f, 0.1f, 0.32f));
        addMenuButton(new MenuButton("menuStart", "img/Menu/menuStartMenu.png", this, 0.5f, 0.2f, 0.4f, 0.76f));
        addMenuButton(new MenuButton("menuChooseDifficulty", "img/Menu/menuChooseDifficulty.png", this, 0.5f, 0.2f, 0.4f, 0.1f));
    }

    /**
     * Performs the action of a button in the menu screen
     *
     * @param name Name of the button that was clicked
     */
    @Override
    public void doMenuAction(String name) {
        switch (name) {
            case "menuBack":
                game.getScreenManager().returnToLastPlayScreen();
                break;
            case "menuSave":
                game.getScreenManager().setAsCurrentScreen("saveScreen");
                break;
            case "menuStart":
                game.getScreenManager().setAsCurrentScreen("startMenu");
                break;
            case "menuChooseDifficulty":
                game.getScreenManager().setAsCurrentScreen("chooseDifficulty");
                break;
        }
    }

}
