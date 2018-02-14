package ritogaems.tov.world.screens;

import android.graphics.Canvas;

import ritogaems.tov.gameEngine.Input.MenuButton;
import ritogaems.tov.gameEngine.graphics.animation.Animation;

/**
 * @author Michael Purdy
 *         An animated button used in menu screens
 */
public class MenuAnimation extends MenuButton {

    /**
     * The animation that is played
     */
    private Animation animation;

    /**
     * Constructor using fractions of the screen to determine
     * where the object is and it's size
     *
     * @param name            The name of the menu button
     * @param animation       The animation that represents the button
     * @param gameScreen      The game screen the hud object belongs to
     * @param xFraction       The fraction of the screen width that the object occupies
     * @param yFraction       The fraction of the screen height that the object occupies
     * @param xOffsetFraction The fraction of the screen width to the left side of the object
     * @param yOffsetFraction The fraction of the screen height to the top of the object
     */
    public MenuAnimation(String name, Animation animation, GameScreen gameScreen, float xFraction, float yFraction, float xOffsetFraction, float yOffsetFraction) {
        super(name, animation.getCurrentFrame().getFrameBitmap(), gameScreen, xFraction, yFraction, xOffsetFraction, yOffsetFraction);

        this.animation = animation;
    }

    /**
     * Draw the menu animation
     *
     * @param canvas The canvas to draw the Hud Object to
     */
    @Override
    public void drawHUD(Canvas canvas) {
        super.drawHUD(canvas);
        super.setIcon(animation.getCurrentFrame().getFrameBitmap());
    }
}
