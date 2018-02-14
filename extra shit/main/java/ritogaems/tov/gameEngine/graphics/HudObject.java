package ritogaems.tov.gameEngine.graphics;

import android.graphics.Canvas;

import ritogaems.tov.gameEngine.Input.Input;
import ritogaems.tov.gameEngine.Input.TouchHandler;
import ritogaems.tov.util.BoundingBox;
import ritogaems.tov.world.GameObject;
import ritogaems.tov.world.screens.GameScreen;
import ritogaems.tov.world.viewports.LayerViewport;
import ritogaems.tov.world.viewports.ScreenViewport;

/**
 * @author Kevin Martin
 * @author Michael Purdy
 *         An object draw to the screen that is not tied to
 *         the screen View port
 */
public abstract class HudObject extends GameObject {

    /**
     * The ID to keep track of a long press
     */
    private int longPressID = -1;

    /**
     * The ID to keep track of drag and drop in Backpack
     */
    private int prevID;

    /**
     * The timer for identifying when a touch qualifies as a long press
     */
    private int longPressTimer = 0;

    /**
     * The boolean to show a long press has been achieved
     */
    private boolean longPress = false;

    /**
     * The ID for tracking a click event
     */
    private int clickID = -1;

    /**
     * THe timer for identifying when a touch qualifies as a click event
     */
    private int clickTimer = 0;

    /**
     * variables for reset
     */
    int reset = 0;

    /**
     * The Input Manager
     */
    protected Input input;

    /**
     * CONSTRUCTORS
     */

    /**
     * Constructor using specific co-ordinates
     * and sizes in pixels
     *
     * @param x      The x co-ordinate in pixels
     * @param y      The y co-ordinate in pixels
     * @param width  The width in pixels
     * @param height The height in pixels
     * @param input  The input for the game
     */
    protected HudObject(float x, float y, float width, float height, Input input) {
        super(x, y, width, height);
        this.input = input;
    }

    /**
     * Constructor using fractions of the screen to determine
     * where the object is and it's size
     *
     * @param gameScreen      The game screen the hud object belongs to
     * @param xFraction       The fraction of the screen width that the object occupies
     * @param yFraction       The fraction of the screen height that the object occupies
     * @param xOffsetFraction The fraction of the screen width to the left side of the object
     * @param yOffsetFraction The fraction of the screen height to the top of the object
     */
    protected HudObject(GameScreen gameScreen, float xFraction, float yFraction,
                        float xOffsetFraction, float yOffsetFraction) {
        super(gameScreen, xFraction, yFraction, xOffsetFraction, yOffsetFraction);
        this.input = gameScreen.getGame().getInput();
    }

    /**
     * INHERITED METHODS
     */

    /**
     * How to draw the object
     * (generally not used - replaced by drawHUD
     * which does not use layer/screen viewport)
     *
     * @param canvas         Canvas on which to draw
     * @param layerViewport  Layer Viewport to draw from
     * @param screenViewport Screen Viewport to translate the Layer Viewport
     */
    @Override
    public void draw(Canvas canvas, LayerViewport layerViewport, ScreenViewport screenViewport) {
        // uses draw HUD to draw
    }

    /**
     * PUBLIC METHODS
     */

    /**
     * Determines if the object has been clicked
     *
     * @return true if the object has been touched
     */
    public boolean isClicked() {

        if (!longPress) {
            BoundingBox bound = getCollisionBox();
            if (clickID != -1) {
                // check the touch event has been lifted
                if (!input.existsTouch(clickID)) {
                    clickID = -1;
                    clickTimer = 0;
                    return true;
                } else if (clickTimer > 5) {
                    clickTimer = 0;
                    clickID = -1;
                    longPress = true;
                } else {
                    clickTimer++;
                }
            } else {
                // check if any of the touch events were in this control
                for (int i = 0; i < TouchHandler.MAX_TOUCH_POINTS; i++) {
                    if (input.existsTouch(i)) {
                        if (bound.contains(input.getTouchX(i), input.getTouchY(i))) {
                            clickID = i;
                            return false;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the object has been long pressed
     *
     * @return true if the object has been touched
     */
    public boolean isLongPressed() {

        if (longPressID != -1) {
            if (longPressTimer > 15 && input.existsTouch(longPressID)) {
                longPressTimer = 0;
                prevID = longPressID;
                longPressID = -1;
                longPress = false;
                return true;
            } else if (!input.existsTouch(longPressID)) {
                longPressTimer = 0;
                longPressID = -1;
                longPress = false;
            } else {
                longPressTimer++;
            }
        } else {
            // check for a touch event in this control and store it's ID
            BoundingBox bound = getCollisionBox();
            for (int i = 0; i < TouchHandler.MAX_TOUCH_POINTS; i++) {
                if (input.existsTouch(i)) {
                    if (bound.contains(input.getTouchX(i), input.getTouchY(i))) {
                        longPressID = i;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks if the buttons need reset to listen for new touch events after not being touched
     * for several frames
     */
    public void checkForReset() {
        BoundingBox bound = getCollisionBox();
        boolean touchFound = false;
        for (int i = 0; i < TouchHandler.MAX_TOUCH_POINTS; i++) {
            if (input.existsTouch(i)) {
                if (bound.contains(input.getTouchX(i), input.getTouchY(i))) {
                    touchFound = true;
                    break;
                }
            }
        }

        if (touchFound) {
            reset = 0;
        } else {
            if (reset > 3) {
                resetVariables();
            } else {
                reset++;
            }
        }
    }

    /**
     * Reset the variables for tracking long press and click events
     */
    public void resetVariables() {
        longPressID = -1;
        longPressTimer = 0;
        longPress = false;
        clickID = -1;
        clickTimer = 0;
    }

    /**
     * Get the previous ID
     * @return an int with the previous ID
     */
    public int getPrevID() {
        return prevID;
    }

    /**
     * How to draw the HudObject
     *
     * @param canvas The canvas to draw the Hud Object to
     */
    public abstract void drawHUD(Canvas canvas);
}
