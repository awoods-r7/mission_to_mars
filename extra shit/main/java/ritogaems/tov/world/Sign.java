package ritogaems.tov.world;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import ritogaems.tov.world.screens.MapScreen;
import ritogaems.tov.world.viewports.LayerViewport;
import ritogaems.tov.world.viewports.ScreenViewport;

/**
 * @author Michael Purdy
 *         An area which if the player enters will display a message
 */
public class Sign extends GameObject {

    /**
     * The length of time the message remains for
     */
    private long delayMillis;

    /**
     * The time the message will stop displaying
     */
    private long endMessageTime;

    /**
     * The metrics used to create a message
     */
    private MessageMetrics messageMetrics;

    /**
     * The image of the message text
     */
    private Bitmap messageBitmap;

    /**
     * Creates a 1 tile sign (multiple tile signs possible but not implemented)
     *
     * @param x         The middle of the sign x co-ordinate (game units)
     * @param y         The middle of the sign y co-ordinate (game units)
     * @param message   The name of the map the portal goes to
     * @param mapScreen The map screen the portal is on (only MapScreens can contain signs)
     */
    public Sign(int x, int y, String message, long delayMillis, MapScreen mapScreen) {
        super(x, y, 10, 10);

        this.delayMillis = delayMillis;
        this.messageMetrics = mapScreen.getmessageMetrics();

        this.messageBitmap = messageMetrics.getTextBitmap(mapScreen.activity, message);

    }

    /**
     * activate the sign (set the time it is to be displayed until)
     */
    public void activateSign() {
        endMessageTime = System.currentTimeMillis() + delayMillis;
    }

    /**
     * If the sign is to be displayed then draw the message board and
     * message text
     *
     * @param canvas         Canvas on which to draw
     * @param layerViewport  Layer Viewport to draw from
     * @param screenViewport Screen Viewport to translate the Layer Viewport
     */
    @Override
    public void draw(Canvas canvas, LayerViewport layerViewport, ScreenViewport screenViewport) {
        if (endMessageTime > System.currentTimeMillis()) {
            messageMetrics.drawMessage(canvas);
            canvas.drawBitmap(messageBitmap, null, messageMetrics.getMessageArea(), null);
        }
    }
}
