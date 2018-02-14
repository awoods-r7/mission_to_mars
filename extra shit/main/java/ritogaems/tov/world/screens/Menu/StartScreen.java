package ritogaems.tov.world.screens.Menu;

import java.io.IOException;

import ritogaems.tov.GameFragment;
import ritogaems.tov.gameEngine.Input.MenuButton;
import ritogaems.tov.gameEngine.ScreenManager;
import ritogaems.tov.world.screens.GameScreen;
import ritogaems.tov.world.screens.MapScreen;

/**
 * @author Michael Purdy
 *         The first screen shown to the user where they
 *         can choose to start a new game or load a previous
 *         save.
 */
public class StartScreen extends MenuScreen {

    /**
     * Constructor to create the menu screen
     *
     * @param gameFragment The game fragment that the menu screen belongs to
     * @param name         The name of the menu screen
     */
    public StartScreen(GameFragment gameFragment, String name) {
        super(gameFragment, name);

        addMenuButton(new MenuButton("menuTitle", "img/Menu/menuTitle.png", this, 0.7f, 0.15f, 0.15f, 0.2f));
        addMenuButton(new MenuButton("menuNew", "img/Menu/menuNew.png", this, 0.3f, 0.2f, 0.15f, 0.5f));
        addMenuButton(new MenuButton("menuLoad", "img/Menu/menuLoad.png", this, 0.3f, 0.2f, 0.55f, 0.5f));
    }

    /**
     * Performs the action of a button in the menu screen
     *
     * @param name Name of the button that was clicked
     */
    @Override
    public void doMenuAction(String name) {

        ScreenManager screenManager = game.getScreenManager();
        switch (name) {
            case "menuNew":
                screenManager.setAsCurrentScreen("loadingScreen");
                createGameScreens(game);
                screenManager.setAsCurrentScreen("chooseDifficulty");
                break;
            case "menuLoad":
                screenManager.setAsCurrentScreen("loadingScreen");
                screenManager.replaceScreen(new LoadScreen(game, "selectSaveScreen"));
                screenManager.setAsCurrentScreen("selectSaveScreen");
                break;
        }
    }

    /**
     * Create (or replace the existing) game screens
     *
     * @param game The game the screens are loaded into
     */
    public static void createGameScreens(GameFragment game) {

        ScreenManager screenManager = game.getScreenManager();

        // create inGameMenuScreen
        InGameMenuScreen inGameMenuScreen = new InGameMenuScreen(game, "inGameMenuScreen");
        screenManager.replaceScreen(inGameMenuScreen);

        // add save screen
        screenManager.replaceScreen(new SaveScreen(game, "saveScreen"));

        // add choose difficulty screen
        screenManager.replaceScreen(new ChooseDifficultyScreen(game, "chooseDifficulty"));

        // create map screens
        try {
            screenManager.replaceScreen(new MapScreen(game, "tutorialTent", "img/TileMaps/TutorialTent.txt"));

            GameScreen overWorldScreen = new MapScreen(game, "overWorld", "img/TileMaps/OverWorld.txt");
            screenManager.replaceScreen(overWorldScreen);

            screenManager.replaceScreen(new MapScreen(game, "puzzleScreen", "img/TileMaps/Puzzle.txt"));
            screenManager.replaceScreen(new MapScreen(game, "shadowDungeon", "img/TileMaps/ShadowDungeon.txt"));

            screenManager.replaceScreen(new MapScreen(game, "snowVillage", "img/TileMaps/SnowVillage.txt"));
            screenManager.replaceScreen(new MapScreen(game, "snowVillageCave", "img/TileMaps/SnowVillageCave.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}