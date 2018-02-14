package ritogaems.tov.world.screens.Menu;

import ritogaems.tov.GameFragment;
import ritogaems.tov.gameEngine.Input.MenuButton;

/**
 * @author Michael Purdy
 *         VERY basic loading screen to show to the user that the game is loading
 *         No progress bar implemented because loading was so quick
 */
public class LoadingScreen extends MenuScreen {

    /**
     * Constructor for loading screen
     * Adds a loading screen background
     *
     * @param game        The GameFragment that the menu screen is shown on
     * @param name        The name of the menu screen
     */
    public LoadingScreen(GameFragment game, String name) {
        super(game, name);

        setBackground(new MenuButton("menuLoading", "img/Menu/menuLoading.gif", this, 1, 1, 0, 0));
    }

}
