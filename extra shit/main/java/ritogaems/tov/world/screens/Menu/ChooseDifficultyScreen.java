package ritogaems.tov.world.screens.Menu;

import ritogaems.tov.GameFragment;
import ritogaems.tov.gameEngine.Difficulty;
import ritogaems.tov.gameEngine.Input.MenuButton;
import ritogaems.tov.gameEngine.ScreenManager;
import ritogaems.tov.gameEngine.graphics.animation.Animation;
import ritogaems.tov.world.screens.MenuAnimation;

/**
 * @author Michael Purdy
 *         Screen for choosing the difficult of the game
 */
public class ChooseDifficultyScreen extends MenuScreen {

    /**
     * Constructor that adds difficulty buttons
     *
     * @param game The GameFragment that the menu screen is shown on
     * @param name The name of the menu screen
     */
    public ChooseDifficultyScreen(GameFragment game, String name) {
        super(game, name);

        for (int i = 0; i < Difficulty.values().length; i++) {

            // equal sized fraction of screen for each button
            float yFraction = (0.9f / Difficulty.values().length) * 0.8f;

            // offset including previous buttons and space between
            float yOffset = 0.1f + (yFraction * i) + (i * yFraction / 8) + ((i - 1) * yFraction / 8);

            addMenuButton(new MenuButton(Difficulty.values()[i].getButtonName(),
                    "img/Menu/" + Difficulty.values()[i].getButtonName() + ".png",
                    this,
                    0.4f,
                    yFraction,
                    0.1f,
                    yOffset
            ));
            addMenuButton(new MenuAnimation("testAnimation",
                    new Animation(game.getAssetStore(), "campFire", game.getAssetStore().getBitmap("campFire", "img/Menu/CampFire.png"), 5, 5, 1, 7.5f, 2),
                    this, 0.2f, 0.65f, 0.6f, 0.2f));
        }
    }

    /**
     * Performs the action of a button in the menu screen
     *
     * @param name Name of the button that was clicked
     */
    @Override
    public void doMenuAction(String name) {

        final String firstScreen = "tutorialTent";

        ScreenManager screenManager = game.getScreenManager();

        Difficulty difficulty = Difficulty.getDifficultyFromButtonName(name);

        if (difficulty != null) {
            game.difficulty = difficulty;
            String lastPlayScreen = screenManager.getLastPlayScreenName();
            if (lastPlayScreen.equals("")) {
                screenManager.setAsCurrentScreen(firstScreen);
            } else {
                screenManager.setAsCurrentScreen(lastPlayScreen);
            }
        }
    }
}
