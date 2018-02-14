package ritogaems.tov.gameEngine.Input;

import android.graphics.Bitmap;

import ritogaems.tov.world.screens.GameScreen;

/**
 * A button that is used in a menu screen
 * Has a name that allows the menu screen to
 * determine which button was pressed
 * <p/>
 * Created by Michael Purdy on 24/02/16.
 */
public class MenuButton extends Button {

    /**
     * The name of the button
     */
    private String name;

    /**
     * Constructor using fractions of the screen to determine
     * where the object is and it's size
     *
     * @param name            The name of the menu button
     * @param bitmapName      The location in the assets folder of the button image
     * @param gameScreen      The game screen the hud object belongs to
     * @param xFraction       The fraction of the screen width that the object occupies
     * @param yFraction       The fraction of the screen height that the object occupies
     * @param xOffsetFraction The fraction of the screen width to the left side of the object
     * @param yOffsetFraction The fraction of the screen height to the top of the object
     */
    public MenuButton(String name, String bitmapName, GameScreen gameScreen,
                      float xFraction, float yFraction,
                      float xOffsetFraction, float yOffsetFraction) {
        super(gameScreen, xFraction, yFraction, xOffsetFraction, yOffsetFraction,
                gameScreen.getGame().getAssetStore().getBitmap(name, bitmapName), false);

        this.name = name;
    }

    /**
     * Constructor using fractions of the screen to determine
     * where the object is and it's size
     * Passes a bitmap not a bitmap location
     *
     * @param name            The name of the menu button
     * @param bitmap          The bitmap of the button
     * @param gameScreen      The game screen the hud object belongs to
     * @param xFraction       The fraction of the screen width that the object occupies
     * @param yFraction       The fraction of the screen height that the object occupies
     * @param xOffsetFraction The fraction of the screen width to the left side of the object
     * @param yOffsetFraction The fraction of the screen height to the top of the object
     */
    public MenuButton(String name, Bitmap bitmap, GameScreen gameScreen,
                      float xFraction, float yFraction,
                      float xOffsetFraction, float yOffsetFraction) {
        super(gameScreen, xFraction, yFraction, xOffsetFraction, yOffsetFraction,
                bitmap, false);

        this.name = name;
    }

    /**
     * Gets the name of the button
     *
     * @return The name of the button
     */
    public String getName() {
        return name;
    }
}
