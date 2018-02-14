package ritogaems.tov.gameEngine.Input;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import ritogaems.tov.gameEngine.graphics.HudObject;
import ritogaems.tov.util.BoundingBox;
import ritogaems.tov.util.GraphicsUtil;
import ritogaems.tov.world.screens.GameScreen;

/**
 * @author Kevin Martin
 * @author Michael Purdy
 *         A button which the user can click to perform an action
 */
public class Button extends HudObject {

    // /////////////////////////////
    // Properties
    // /////////////////////////////

    /**
     * The center co-ordinates of the button
     */
    private Bitmap buttonIcon;
    private Bitmap HUDBackground;

    private BoundingBox mIconDrawBox;

    // /////////////////////////////
    // Constructors
    // /////////////////////////////

    /**
     * Constructor to create the button from specific
     * co-ordinates and sizes in pixels
     * (Circle background)
     *
     * @param xPos       The x co-ordinate in pixels
     * @param yPos       The y co-ordinate in pixels
     * @param width      The width in pixels
     * @param height     The height in pixels
     * @param bitmap     The button image
     * @param gameScreen The game screen the object belongs to
     */
    public Button(float xPos, float yPos, int width, int height, Bitmap bitmap, boolean scaleIcon, GameScreen gameScreen) {
        super(xPos, yPos, width, height, gameScreen.getGame().getInput());

        mIconDrawBox = getIconDrawBox(scaleIcon);

        if (bitmap != null) {
            buttonIcon = bitmap;
        }
        HUDBackground = gameScreen.getGame().getAssetStore().getBitmap("HUD Circle", "img/HUD/HUD Circle.png");
    }

    /**
     * Constructor using fractions of the screen to determine
     * where the object is and it's size
     * (has no background)
     *
     * @param gameScreen      The game screen the hud object belongs to
     * @param xFraction       The fraction of the screen width that the object occupies
     * @param yFraction       The fraction of the screen height that the object occupies
     * @param xOffsetFraction The fraction of the screen width to the left side of the object
     * @param yOffsetFraction The fraction of the screen height to the top of the object
     * @param bitmap          The button image
     */
    public Button(GameScreen gameScreen, float xFraction, float yFraction,
                  float xOffsetFraction, float yOffsetFraction, Bitmap bitmap, boolean scaleIcon) {
        super(gameScreen, xFraction, yFraction, xOffsetFraction, yOffsetFraction);

        mIconDrawBox = getIconDrawBox(scaleIcon);

        buttonIcon = bitmap;
    }

    /**
     * Constructor to create the button from specific
     * co-ordinates and sizes in pixels
     * (square background)
     *
     * @param xPos
     * @param yPos
     * @param width
     * @param height
     * @param scaleIcon
     * @param gameScreen
     */
    public Button(float xPos, float yPos, int width, int height, boolean scaleIcon, GameScreen gameScreen) {
        super(xPos, yPos, width, height, gameScreen.getGame().getInput());

        this.mIconDrawBox = getIconDrawBox(scaleIcon);

        HUDBackground = gameScreen.getGame().getAssetStore().getBitmap("HUD Square", "img/Menu/SquareButton.png");
    }

    /**
     * Create a bounding box for the button icon
     *
     * @param scaleIcon If the button should be scaled down to 70%
     *                  (e.g. for fitting inside circle background)
     * @return Icon bounding box
     */
    private BoundingBox getIconDrawBox(boolean scaleIcon) {
        float scale;
        if (scaleIcon) {
            scale = 0.7f;
        } else {
            scale = 1.0f;
        }
        return new BoundingBox(drawBox.x, drawBox.y, drawBox.halfWidth * scale, drawBox.halfHeight * scale);
    }

    /**
     * Setter
     *
     * @param bitmap The new button icon
     */
    public void setIcon(Bitmap bitmap) {
        if (bitmap != null) {
            buttonIcon = bitmap;
        } else {
            buttonIcon = null;
        }
    }

    /**
     * Getter
     *
     * @return The current button icon
     */
    public Bitmap getIcon() {
        return buttonIcon;
    }

    // /////////////////////////////
    // Methods
    // /////////////////////////////

    /**
     * Draw the button to the canvas
     *
     * @param canvas The canvas to draw the Hud Object to
     */
    @Override
    public void drawHUD(Canvas canvas) {
        if (HUDBackground != null) {
            canvas.drawBitmap(HUDBackground, null, GraphicsUtil.getRectFromBoundingBox(drawBox), null);
        }
        if (buttonIcon != null) {
            canvas.drawBitmap(buttonIcon, null, GraphicsUtil.getRectFromBoundingBox(mIconDrawBox), null);
        }
    }
}