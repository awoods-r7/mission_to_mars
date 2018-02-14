package ritogaems.tov.gameEngine.graphics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import ritogaems.tov.world.screens.GameScreen;

/**
 * @author Michael Purdy
 *         Generic status bar for showing a stat on the HUD
 *         e.g. Health (red) or mana (blue)
 */
public class StatusBar extends HudObject {

    /**
     * The maximum available stat value and
     * current stat value
     */
    private float maxStat;
    private float currentStat;

    /**
     * The number of pixels padding between the bar and the outline
     */
    private final int PADDING_PX = 5;

    /**
     * The outline of the status bar
     */
    private int outlineColor = Color.BLACK;

    /**
     * The colour of the status display
     */
    private int statusColor;

    /**
     * The paint used to draw the status bar
     */
    private Paint paint;

    /**
     * Constructor using fractions of the screen to determine
     * where the object is and it's size
     *
     * @param gameScreen      The game screen the hud object belongs to
     * @param xFraction       The fraction of the screen width that the object occupies
     * @param yFraction       The fraction of the screen height that the object occupies
     * @param xOffsetFraction The fraction of the screen width to the left side of the object
     * @param yOffsetFraction The fraction of the screen height to the top of the object
     * @param maxStat         The maximum value for the status
     * @param currentStat     The current value for the status
     * @param paint           The paint object used for drawing the bar
     */
    public StatusBar(GameScreen gameScreen, float xFraction, float yFraction,
                     float xOffsetFraction, float yOffsetFraction, float maxStat, float currentStat,
                     int statusColor, Paint paint) {
        super(gameScreen, xFraction, yFraction, xOffsetFraction, yOffsetFraction);
        this.maxStat = maxStat;
        this.currentStat = currentStat;
        this.statusColor = statusColor;
        this.paint = paint;
    }

    /**
     * Update the status bar
     *
     * @param currentStat The new status value
     */
    public void update(float currentStat) {
        this.currentStat = currentStat;
    }

    /**
     * Draw the status bar
     *
     * @param canvas The canvas to draw the Hud Object to
     */
    @Override
    public void drawHUD(Canvas canvas) {

        // draw the outline
        paint.setColor(outlineColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4.0f);

        canvas.drawRect(drawBox.getLeft(), drawBox.getTop(),
                drawBox.getRight(), drawBox.getBottom(),
                paint);

        // draw the status bar
        paint.setColor(statusColor);
        paint.setStyle(Paint.Style.FILL);

        float maxSize = (drawBox.getRight() - PADDING_PX) - (drawBox.getLeft() + PADDING_PX);
        float healthFraction = maxSize * (currentStat / maxStat);

        canvas.drawRect(drawBox.getLeft() + PADDING_PX, drawBox.getTop() + PADDING_PX,
                drawBox.getLeft() + PADDING_PX + healthFraction, drawBox.getBottom() - PADDING_PX,
                paint);
    }

}
