package ritogaems.tov.world.screens.Menu;

import android.graphics.Canvas;

import java.util.ArrayList;

import ritogaems.tov.GameFragment;
import ritogaems.tov.gameEngine.ElapsedTime;
import ritogaems.tov.gameEngine.Input.MenuButton;
import ritogaems.tov.gameEngine.audio.BackgroundMusic;
import ritogaems.tov.gameEngine.io.FileHandler;
import ritogaems.tov.world.screens.GameScreen;

/**
 * @author Michae Purdy
 *         Blank menu screen that only has a background
 *         can be extended to include buttons that perform
 *         actions when clicked
 */
public class MenuScreen extends GameScreen {

    /**
     * The options available on the menu screen
     * (These can simply be images that have no actions eg a background)
     */
    private ArrayList<MenuButton> menuButtons = new ArrayList<>();

    /**
     * The music for the menu screen
     */
    private BackgroundMusic backgroundMusic;

    /**
     * Default constructor for a Menu Screen
     * sets the background image for the menu and the
     * default menu background music
     *
     * @param game        The GameFragment that the menu screen is shown on
     * @param name        The name of the menu screen
     */
    public MenuScreen(GameFragment game, String name) {
        super(game, name, ScreenType.MENU);

        menuButtons.add(new MenuButton("menuBackground", "img/Menu/menuBackground.png", this, 1, 1, 0, 0));

        this.backgroundMusic = game.getAssetStore().getBgm("GameMenu", "audio/bgm/GameMenu.mp3");
        this.backgroundMusic.setLooping(true);
    }

    /**
     * The update of the screen that checks if any buttons
     * have been pressed and performs their actions
     *
     * @param elapsedTime Time since last update
     */
    @Override
    public void update(ElapsedTime elapsedTime) {

        // check if any touch events are in the bounds of a menu object
        for (MenuButton menuButton : menuButtons) {
            if (menuButton.isClicked()) {
                // perform action of button if it is clicked
                doMenuAction(menuButton.getName());
            }
            menuButton.checkForReset();
        }

    }

    /**
     * Draw all of the menu buttons
     * (including image buttons that have no action
     * e.g. background)
     *
     * @param canvas Canvas to draw the buttons to
     */
    @Override
    public void draw(Canvas canvas) {
        for (MenuButton menuButton : menuButtons) {
            menuButton.drawHUD(canvas);
        }
    }

    /**
     * Empty method stub that other Menu Screens can override
     * to add functionality to any buttons added
     *
     * @param name Name of the button that was clicked
     */
    public void doMenuAction(String name) {
        // empty method - override in sub classes to add functionality to buttons
    }

    /**
     * Get the name of the menu screen
     *
     * @return return the name of the menu screen
     */
    public String getName() {
        return name;
    }

    /**
     * Pause the music (used when switching screens)
     */
    @Override
    public void pauseMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.pause();
        }
    }

    /**
     * Play the music (used when switching screens)
     */
    @Override
    public void playMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.play();
        }
    }

    /**
     * Add a menu button to the menu screen
     *
     * @param menuButton The menu button to add
     */
    public void addMenuButton(MenuButton menuButton) {
        menuButtons.add(menuButton);
    }

    /**
     * Get the file handler (used for saving)
     *
     * @return FileHandler
     */
    FileHandler getFileHandler() {
        return game.getFileIO();
    }

    /**
     * Set the background image of the menu
     *
     * @param menuButton The new background of the menu
     */
    public void setBackground(MenuButton menuButton) {
        menuButtons.remove(0);
        menuButtons.add(0, menuButton);
    }
}
