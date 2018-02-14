package ritogaems.tov.world;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.widget.TextView;

/**
 * @author Michael Purdy
 *         Properties for how to draw a message to the screen
 *         Includes properties of a message board (Bitmap) and message
 *         text placement on the screen
 *         <p/>
 *         Currently used in signs but could be used for event driven messages
 */
public class MessageMetrics {

    /**
     * Paint used to draw message board and message text
     */
    private Paint boardPaint;
    private Paint textPaint;

    /**
     * Screen width and height
     */
    private int screenWidth;
    private int screenHeight;

    /**
     * Areas where the message text and board are drawn
     */
    private Rect messageArea;
    private Rect messageBoardArea;

    /**
     * The bitmap of the message board
     */
    private Bitmap messageBoardBitmap;

    /**
     * Padding between the outside of the screen and
     * the message board
     * (fraction of the screen)
     */
    private final float HORIZONTAL_SCREEN_PADDING = 0.1f;
    private final float VERTICAL_SCREEN_PADDING = 0.2f;

    /**
     * Padding between the outside of the message board
     * and the message text
     * (fraction of the message board)
     */
    private final float MESSAGEBOARD_VERTICAL_PADDING = 0.2f;
    private final float MESSAGEBOARD_HORIZONTAL_PADDING = 0.15f;

    /**
     * The alpha blend value for the message board (out of 255)
     */
    private final int MESSAGEBOARD_ALPHA = 200;

    /**
     * The colour of the message board (not variable for consistency)
     */
    private final int MESSAGEBOARD_COLOUR = Color.WHITE;

    /**
     * The text size of messages
     */
    private final int MESSAGE_TEXT_SIZE = 20;

    /**
     * Constructor for message metrics
     *
     * @param screenWidth  The width of the screen in px
     * @param screenHeight The height of the screen in px
     */
    public MessageMetrics(int screenWidth, int screenHeight, Bitmap messageBoardBitmap) {

        // set up the message board paint
        this.boardPaint = new Paint();
        this.boardPaint.setColor(MESSAGEBOARD_COLOUR);
        this.boardPaint.setAlpha(MESSAGEBOARD_ALPHA);

        // set up the text paint
        this.textPaint = new Paint();

        this.messageBoardBitmap = messageBoardBitmap;

        // store the screen width and height
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        // calculate the screen offset
        int horizontalOffset = (int) (this.screenWidth * HORIZONTAL_SCREEN_PADDING);
        int verticalOffset = (int) (this.screenHeight * VERTICAL_SCREEN_PADDING);

        // calculate the padding of the message board in pixels
        int hPadding = (int) ((screenWidth - horizontalOffset * 2) * MESSAGEBOARD_HORIZONTAL_PADDING);
        int vPadding = (int) ((screenHeight - verticalOffset * 2) * MESSAGEBOARD_VERTICAL_PADDING);

        // calculate the message text area
        this.messageArea = new Rect();
        this.messageArea.set(horizontalOffset + hPadding, verticalOffset + vPadding,
                this.screenWidth - horizontalOffset - hPadding, this.screenHeight - verticalOffset - vPadding);

        // calculate the message board area
        this.messageBoardArea = new Rect();
        this.messageBoardArea.set(horizontalOffset, verticalOffset,
                this.screenWidth - horizontalOffset, this.screenHeight - verticalOffset);
    }

    public void setMessageBoardArea(float xFraction, float yFraction, float xOffset, float yOffset) {
        this.messageBoardArea.set((int) (xOffset * screenWidth),
                (int) (yOffset * screenHeight),
                (int) ((xOffset * screenWidth) + (xFraction * screenWidth)),
                (int) ((yOffset * screenHeight) + (yFraction * screenHeight)));

        int hPadding = (int) (messageBoardArea.width() * MESSAGEBOARD_HORIZONTAL_PADDING);
        int vPadding = (int) (messageBoardArea.height() * MESSAGEBOARD_VERTICAL_PADDING);

        this.messageArea.set(messageBoardArea.left + hPadding, messageBoardArea.top + vPadding,
                messageBoardArea.right - hPadding, messageBoardArea.bottom - vPadding);
    }

    /**
     * Returns the area of the screen to write the message text to
     */
    public Rect getMessageArea() {
        return messageArea;
    }

    /**
     * Returns the area of the screen to write the message board to
     */
    public Rect getMessageBoardArea() {
        return messageBoardArea;
    }

    /**
     * Draw the message board to the screen
     * (used to also draw the message string but now
     * that is drawn as a bitmap to make word wrapping
     * easier)
     *
     * @param canvas The canvas to draw the message board to
     */
    public void drawMessage(Canvas canvas /*, String message*/) {

        // draw the message board
        canvas.drawBitmap(messageBoardBitmap, null, messageBoardArea, boardPaint);

        // no longer used draw text method
//        canvas.drawText(message, horizontalOffset + 20, verticalOffset + 50, mTextPaint);
    }

    /**
     * Draw the message board and the message text to the screen
     *
     * @param canvas  The canvas to draw to
     * @param context The context
     * @param message The message to draw
     */
    public void drawMessageWithText(Canvas canvas, Context context, String message) {
        // draw the message board
        canvas.drawBitmap(messageBoardBitmap, null, messageBoardArea, boardPaint);

        // draw the message text
        canvas.drawBitmap(getTextBitmap(context, message), null, messageArea, textPaint);
    }

    /**
     * Creates a bitmap of the word wrapped message text
     * adapted from
     * http://stackoverflow.com/questions/4259858/word-wraped-tex-on-android-canvas
     *
     * @param context The message context
     * @param message The message text
     * @return The bitmap of text
     */
    public Bitmap getTextBitmap(Context context, String message) {

        // create the text view with the text
        TextView tv = new TextView(context);
        tv.setMaxWidth(messageArea.width());
        tv.setMaxHeight(messageArea.height());
        tv.setText(message);
        tv.setTextSize(MESSAGE_TEXT_SIZE);

        // set the size of the text view
        int widthSpec = View.MeasureSpec.makeMeasureSpec(messageArea.width(), View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(messageArea.height(), View.MeasureSpec.EXACTLY);
        tv.measure(widthSpec, heightSpec);
        tv.layout(0, 0, tv.getMeasuredWidth(), tv.getMeasuredHeight());

        // create the bitmap
        Bitmap messageBitmap = Bitmap.createBitmap(tv.getWidth(), tv.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(messageBitmap);
        tv.draw(canvas);

        return messageBitmap;
    }

}
