package ritogaems.tov.gameEngine.graphics.animation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import ritogaems.tov.util.GraphicsUtil;
import ritogaems.tov.world.GameObject;
import ritogaems.tov.world.viewports.LayerViewport;
import ritogaems.tov.world.viewports.ScreenViewport;

public abstract class Sprite extends GameObject {

    // /////////////////////////////
    // Properties
    // /////////////////////////////

    // Bitmap to hold the object for current frame
    protected Bitmap bitmap;

    // /////////////////////////////
    // Constructors
    // /////////////////////////////

    /**
     * Creates a sprite using the same draw and collision
     * Takes in a bitmap if the subclass does not set it
     *
     * @param xPos   X position (centre in game units)
     * @param yPos   Y position (centre in game units)
     * @param width  Width of the draw and collision boxes
     * @param height Height of the draw and collision boxes
     * @param bitmap Bitmap of the Sprite
     */
    protected Sprite(float xPos, float yPos, float width, float height,
                     Bitmap bitmap) {
        super(xPos, yPos, width, height);
        this.bitmap = bitmap;
    }

    /**
     * Creates a sprite
     * Takes in a bitmap if the subclass does not set it
     *
     * @param xPos            X position (centre in game units)
     * @param yPos            Y position (centre in game units)
     * @param drawWidth       Width of the draw box
     * @param drawHeight      Height of the draw box
     * @param collisionWidth  Width of the collision box
     * @param collisionHeight Height of the collision box
     * @param bitmap          Bitmap of the Sprite
     */
    protected Sprite(float xPos, float yPos, float drawWidth, float drawHeight,
                     float collisionWidth, float collisionHeight,
                     Bitmap bitmap) {
        super(xPos, yPos, drawWidth, drawHeight, collisionWidth, collisionHeight);
        this.bitmap = bitmap;
    }

    /**
     * Creates a sprite
     * NOTE: Bitmap must be set in subclass
     *
     * @param xPos            X position (centre in game units)
     * @param yPos            Y position (centre in game units)
     * @param drawWidth       Width of the draw box
     * @param drawHeight      Height of the draw box
     * @param collisionWidth  Width of the collision box
     * @param collisionHeight Height of the collision box
     */
    protected Sprite(float xPos, float yPos, float drawWidth, float drawHeight,
                     float collisionWidth, float collisionHeight) {
        super(xPos, yPos, drawWidth, drawHeight, collisionWidth, collisionHeight);
    }

    // /////////////////////////////
    // Methods
    // /////////////////////////////

    /**
     * Draws the sprite to the canvas, clipping it if needed
     *
     * @param canvas         Canvas on which to draw
     * @param layerViewport  Layer Viewport to draw from
     * @param screenViewport Screen Viewport to translate the Layer Viewport
     */
    @Override
    public void draw(Canvas canvas, LayerViewport layerViewport, ScreenViewport screenViewport) {
        if (GraphicsUtil.getSourceAndScreenRect(this, layerViewport, screenViewport)) {
            canvas.drawBitmap(bitmap, GraphicsUtil.drawSourceRect, GraphicsUtil.drawScreenRect, null);
        }
    }

    /**
     * Draws the sprite to the canvas, clipping it if needed
     *
     * @param canvas         Canvas on which to draw
     * @param layerViewport  Layer Viewport to draw from
     * @param screenViewport Screen Viewport to translate the Layer Viewport
     * @param paint          Paint to apply to the bitmap
     */
    protected void draw(Canvas canvas, LayerViewport layerViewport, ScreenViewport screenViewport, Paint paint) {
        if (GraphicsUtil.getSourceAndScreenRect(this, layerViewport, screenViewport)) {
            canvas.drawBitmap(bitmap, GraphicsUtil.drawSourceRect, GraphicsUtil.drawScreenRect, paint);
        }
    }

    /**
     * Getter
     *
     * @return The sprite's Bitmap
     */
    public Bitmap getBitmap() {
        return bitmap;
    }
}