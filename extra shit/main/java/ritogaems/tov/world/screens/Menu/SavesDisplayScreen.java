package ritogaems.tov.world.screens.Menu;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import ritogaems.tov.GameFragment;
import ritogaems.tov.gameEngine.Input.MenuButton;
import ritogaems.tov.world.MessageMetrics;

/**
 * @author Michael Purdy
 *         Generic screen to show the available saves
 */
public abstract class SavesDisplayScreen extends MenuScreen {

    /**
     * The screen the back button returns to
     */
    private String previousScreenName = "startMenu";

    /**
     * The maximum number of save files
     */
    private final int maxNumSaves = 4;

    /**
     * The message metrics for writing a message to the screen
     */
    protected MessageMetrics messageMetrics;

    /**
     * The message to display
     */
    protected String message = null;

    /**
     * Default constructor for a Menu Screen
     * sets the background image for the menu and the
     * default menu background music
     *
     * @param game The GameFragment that the menu screen is shown on
     * @param name The name of the menu screen
     */
    SavesDisplayScreen(GameFragment game, String name) {
        super(game, name);

        this.messageMetrics = new MessageMetrics(game.getScreenWidth(), game.getScreenHeight(),
                game.getAssetStore().getBitmap("scroll", "img/HUD/scroll.png"));
        this.messageMetrics.setMessageBoardArea(0.5f, 0.35f, 0.4f, 0.55f);

        Bitmap saveIcon = game.getAssetStore().getBitmap("saveIcon", "img/Menu/saveIcon.png");

        for (int i = 0; i < maxNumSaves; i++) {

            // equal sized fraction of screen for each button
            float xFraction = (0.9f / maxNumSaves) * 0.8f;

            // offset including previous buttons and space between
            float xOffset = 0.1f + (xFraction * i) + (i * xFraction / 8) + ((i - 1) * xFraction / 8);
            addMenuButton(new MenuButton("SaveIcon" + i, saveIcon, this, xFraction, 0.3f, xOffset, 0.1f));
        }

        addMenuButton(new MenuButton("BackButton", "img/Menu/backArrow.png", this, 0.2f, 0.3f, 0.1f, 0.6f));
    }

    /**
     * Set the screen the back button goes to
     *
     * @param name The name of the screen to go back to
     */
    public void setPreviousScreen(String name) {
        this.previousScreenName = name;
    }

    /**
     * Do the action of menu buttons
     *
     * @param name Name of the button that was clicked
     */
    @Override
    public void doMenuAction(String name) {
        super.doMenuAction(name);
        switch (name) {
            case "BackButton":
                game.getScreenManager().setAsCurrentScreen(previousScreenName);
        }
    }

    /**
     * Draw the screen to the canvas
     *
     * @param canvas Canvas to draw the screen to
     */
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (message != null) {
            messageMetrics.drawMessageWithText(canvas, game.getActivity(), message);
        }
    }
}

