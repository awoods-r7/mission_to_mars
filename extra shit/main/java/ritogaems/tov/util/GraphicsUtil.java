package ritogaems.tov.util;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.DisplayMetrics;

import ritogaems.tov.gameEngine.AssetStore;
import ritogaems.tov.gameEngine.graphics.animation.Sprite;
import ritogaems.tov.world.GameObject;
import ritogaems.tov.world.viewports.LayerViewport;
import ritogaems.tov.world.viewports.ScreenViewport;

/**
 * Utility for manipulating graphics in the game
 * <p/>
 * Created by Darren on 07/12/2015.
 */
public final class GraphicsUtil {

    //Reusable Rectangles
    public static Rect drawSourceRect = new Rect();
    public static Rect drawScreenRect = new Rect();

    // reuasable matrix
    public static Matrix drawMatrix = new Matrix();

    /*
    METHODS FOR GETTING THINGS INTO THE VIEWPORT PROPERLY
     */

    /**
     * Draw a sprite onto the screen, scaling from the layer to the screen viewport
     *
     * @param gameObject        Object to be scaled
     * @param layerViewport     Layer viewport to scale from
     * @param screenViewport    Screen viewport to scale to
     * @return
     *
     * @author Darren
     */
    public static boolean getScreenRect(GameObject gameObject,
                                        LayerViewport layerViewport,
                                        ScreenViewport screenViewport) {

        // Get the bounding box for the specified sprite
        BoundingBox spriteBound = gameObject.getDrawBox();

        // Determine if the sprite falls within the layer viewport
        if (layerViewport.containsAll(spriteBound)) {

            // Determine the x- and y-aspect rations between the layer and screen viewports
            float screenXScale = (float) screenViewport.width / layerViewport.getWidth();
            float screenYScale = (float) screenViewport.height / layerViewport.getHeight();

            float screenX = screenViewport.xLeft + screenXScale * (spriteBound.getLeft() - layerViewport.getLeft());
            float screenY = screenViewport.yTop + screenYScale * (spriteBound.getTop() - layerViewport.getTop());

            drawScreenRect.set((int) screenX, (int) screenY,
                    (int) (screenX + spriteBound.getWidth() * screenXScale),
                    (int) (screenY + spriteBound.getHeight() * screenYScale));

            return true;
        }

        // Not visible
        return false;
    }

    /**
     * Calculate what part of the sprite is visible within the layer viewport,
     * and map it onto the screen viewport
     *
     * @param sprite            The sprite to be drawn
     * @param layerViewport     The layer viewport to draw from
     * @param screenViewport    The screen viewport
     * @return                  Whether the sprite lies within the viewport
     *
     * @author Darren
     */
    public static boolean getSourceAndScreenRect(Sprite sprite,
                                                 LayerViewport layerViewport,
                                                 ScreenViewport screenViewport) {

        // Get the bounding box for the specified sprite
        BoundingBox drawBox = sprite.getCollisionBox();

        if (layerViewport.intersects(drawBox)) {
            if (layerViewport.containsAll(drawBox)) {
            }
        }

        //Determine whether the whole, part or none of the sprite is in the viewport
        if (layerViewport.containsAll(drawBox)) {

            //Scale the bitmap to the draw box
            float sourceScaleWidth = sprite.getBitmap().getWidth() / drawBox.getWidth();
            float sourceScaleHeight = sprite.getBitmap().getHeight() / drawBox.getHeight();

            drawSourceRect.set(0, 0,
                    (int) (drawBox.getWidth() * sourceScaleWidth), (int) (drawBox.getHeight() * sourceScaleHeight));

            // Determine which region of the screen viewport (relative to the
            // canvas) we will be drawing to.

            // Determine the x- and y-aspect rations between the layer and screen viewports
            float screenXScale = (float) screenViewport.width / layerViewport.getWidth();
            float screenYScale = (float) screenViewport.height / layerViewport.getHeight();

            float screenX = screenViewport.xLeft + screenXScale * Math.max(0.0f,
                    drawBox.getLeft() - layerViewport.getLeft());
            float screenY = screenViewport.yTop + screenYScale * Math.max(0.0f,
                    drawBox.getTop() - layerViewport.getTop());

            // Set the region to the canvas to which we will draw
            drawScreenRect.set((int) screenX,
                    (int) screenY,
                    (int) (screenX + drawBox.getWidth() * screenXScale),
                    (int) (screenY + drawBox.getHeight() * screenYScale));

            return true;

        } else if (layerViewport.intersects(drawBox)) {

            // Work out what region of the sprite is visible within the layer viewport,
            float sourceX = Math.max(0.0f,
                    layerViewport.getLeft() - drawBox.getLeft());
            float sourceY = Math.max(0.0f,
                    layerViewport.getTop() - drawBox.getTop());

            float sourceWidth = ((drawBox.getWidth() - sourceX) - Math.max(0.0f,
                    drawBox.getRight() - layerViewport.getRight()));
            float sourceHeight = ((drawBox.getHeight() - sourceY) - Math.max(0.0f,
                    drawBox.getBottom() - layerViewport.getBottom()));

            //Scale the bitmap to the draw box
            float sourceScaleWidth = sprite.getBitmap().getWidth() / drawBox.getWidth();
            float sourceScaleHeight = sprite.getBitmap().getHeight() / drawBox.getHeight();

            drawSourceRect.set((int) (sourceX * sourceScaleWidth), (int) (sourceY * sourceScaleHeight),
                    (int) ((sourceX + sourceWidth) * sourceScaleWidth), (int) ((sourceY + sourceHeight) * sourceScaleHeight));

            // Determine which region of the screen viewport (relative to the
            // canvas) we will be drawing to.

            // Determine the x- and y-aspect rations between the layer and screen viewports
            float screenXScale = (float) screenViewport.width / layerViewport.getWidth();
            float screenYScale = (float) screenViewport.height / layerViewport.getHeight();

            float screenX = screenViewport.xLeft + screenXScale * Math.max(0.0f,
                    drawBox.getLeft() - layerViewport.getLeft());
            float screenY = screenViewport.yTop + screenYScale * Math.max(0.0f,
                    drawBox.getTop() - layerViewport.getTop());

            // Set the region to the canvas to which we will draw
            drawScreenRect.set((int) screenX,
                    (int) screenY,
                    (int) (screenX + sourceWidth * screenXScale),
                    (int) (screenY + sourceHeight * screenYScale));

            return true;
        }

        return false;
    }

    /**
     *  create a viewport into the world that is a minimum of 120x80 "game units"
     *  when a screen is not a 3:2 ratio add extra game units to pad to physical screen ratio
     *
     * @param screenMetrics Metrics for the screen
     * @param width         Width of the base viewport
     * @param height        Height of the base viewport
     * @return
     *
     * @author Michael Purdy
     */
    public static LayerViewport createDynamicTileLayerVP(DisplayMetrics screenMetrics, int width, int height) {
        // initialise the minimum screen to the full size of the screen
        int minScreenWidth = screenMetrics.widthPixels;
        int minScreenHeight = screenMetrics.heightPixels;

        // get the relative number of pixels per 40 game units
        int pxWidthRatio = minScreenWidth / 3;
        int pxHeightRatio = minScreenHeight / 2;

        // initialise the offset (extra space beyond 3:2 ratio)
        int xOffset = 0;
        int yOffset = 0;

        // check whether height or width has more pixels per game unit
        // calculate the offset needed to pad the minScreen to screen size
        if ((pxWidthRatio) < (pxHeightRatio)) {
            minScreenHeight = pxWidthRatio * 2;
            yOffset = (screenMetrics.heightPixels - minScreenHeight) / 2;
        } else {
            minScreenWidth = pxHeightRatio * 3;
            xOffset = (screenMetrics.widthPixels - minScreenWidth) / 2;
        }

        // the ratio to convert game units to screen pixels
        float pxPerGameUnit = minScreenHeight / height;

        // calculate the offset in game units
        float xOffsetGU = xOffset / pxPerGameUnit;
        float yOffsetGU = yOffset / pxPerGameUnit;

        // calculate center of the layer viewport
        float x = (width + xOffsetGU) / 2;
        float y = (height + yOffsetGU) / 2;

        // calculate the width and height of the viewport
        float newWidth = width + xOffsetGU;
        float newHeight = height + yOffsetGU;

        return new LayerViewport(x, y, newWidth, newHeight);
    }

    /*
    METHODS FOR CUTTING BITMAPS
     */

    /**
     * @param bitmap       the bitmap to trim
     * @param noOfFrames   the maximum number of horizontal frames in the bitmap
     * @param framesPerRow the number of frames in that row (different from above as it allows different amounts of frames
     *                     in different rows)
     * @param noOfRows     This lets you cut bitmaps that have any number of rows
     * @return bitmapArray
     * @author Andrew Woods
     * Used for cutting bitmap strips into frames and returning an array of frames
     */
    public static Bitmap[] cutBitmap(Bitmap bitmap, int noOfFrames, int framesPerRow, int noOfRows) {
        Bitmap[] bitmapArray = new Bitmap[noOfFrames];
        int width = bitmap.getWidth() / framesPerRow;
        int height = bitmap.getHeight() / noOfRows;

        int i = 0;
        for (int c = 0; c < noOfRows; c++) {
            for (int d = 0; d < framesPerRow; d++) {
                int scrX = d * width;
                int scrY = c * height;
                if (i < noOfFrames) {
                    Bitmap frame = Bitmap.createBitmap(bitmap, scrX, scrY, width, height);
                    frame = trimBitmap(frame);
                    bitmapArray[i] = frame;
                    i++;
                }
            }
        }

        return bitmapArray;
    }

    /**
     * @param assetStore   passing in this parameter allows this class to access the assetStore to check for a BitmapArray
     * @param name         this is used to add/get the bitmapArray from the assetStore
     * @param noOfFrames   the maximum number of horizontal frames in the bitmap
     * @param framesPerRow the number of frames in that row (different from above as it allows different amounts of frames
     *                     in different rows)
     * @param noOfRows     This lets you cut bitmaps that have any number of rows
     * @return bitmapArray
     * @author Michael Purdy
     * Used for cutting multiDirectional bitmaps into single strips that represent each direction
     */
    public static Bitmap[] getCutAssetStoreBitmap(AssetStore assetStore, String name, Bitmap bitmap, int noOfFrames, int framesPerRow, int noOfRows) {
        Bitmap[] bitmapArray = assetStore.getBitmapArray(name);
        if (bitmapArray == null) {
            bitmapArray = cutBitmap(bitmap, noOfFrames, framesPerRow, noOfRows);
            assetStore.add(name, bitmapArray);
        }
        return bitmapArray;
    }

    /**
     * @param bitmap       the bitmap to trim
     * @param numberOfRows the number of Rows to
     * @return bitmapArray
     * @author Andrew Woods
     * Used for cutting multiDirectional bitmaps into single strips that represent each direction
     */
    public static Bitmap[] cutDirectedBitmap(Bitmap bitmap, int numberOfRows) {
        Bitmap[] bitmapArray = new Bitmap[numberOfRows];
        int height = bitmap.getHeight() / numberOfRows;
        int scrY;
        for (int i = 0; i < numberOfRows; i++) {
            scrY = i * height;
            Bitmap frame = Bitmap.createBitmap(bitmap, 0, scrY, bitmap.getWidth(), height);
            bitmapArray[i] = frame;
        }
        return bitmapArray;
    }

    /**
     * @param bitmap the bitmap to trimg
     * @return nothing
     * @author Kevin Martin
     * Used for trimming bitmaps down to a specific size by removing transparent space
     * <p/>
     * SLIGHTLY MODIFIED, MOSTLY 3RD PARTY FROM:
     * http://stackoverflow.com/questions/32797295/how-to-remove-blank-space-around-an-image-in-android
     */
    private static Bitmap trimBitmap(Bitmap bitmap) {
        int imgHeight = bitmap.getHeight();
        int imgWidth = bitmap.getWidth();

        // the new width and height for the bitmap
        int startHeight = 0;
        int endHeight = 0;
        int startWidth = 0;
        int endWidth = 0;

        // trim the left
        for (int x = 0; x < imgWidth; x++) {
            if (startWidth == 0) {
                for (int y = 0; y < imgHeight; y++) {
                    if (bitmap.getPixel(x, y) != Color.TRANSPARENT) {
                        startWidth = x;
                        break;
                    }
                }
            } else {
                break;
            }
        }

        // trim the top
        for (int y = 0; y < imgHeight; y++) {
            if (startHeight == 0) {
                for (int x = 0; x < imgWidth; x++) {
                    if (bitmap.getPixel(x, y) != Color.TRANSPARENT) {
                        startHeight = y;
                        break;
                    }
                }
            } else {
                break;
            }
        }

        // trim the right
        for (int x = imgWidth - 1; x > -1; x--) {
            if (endWidth == 0) {
                for (int y = 0; y < imgHeight; y++) {
                    if (bitmap.getPixel(x, y) != Color.TRANSPARENT) {
                        endWidth = x + 1;
                        break;
                    }
                }
            } else {
                break;
            }
        }

        // trim the bottom
        for (int y = imgHeight - 1; y > -1; y--) {
            if (endHeight == 0) {
                for (int x = 0; x < imgWidth; x++) {
                    if (bitmap.getPixel(x, y) != Color.TRANSPARENT) {
                        endHeight = y + 1;
                        break;
                    }
                }
            } else {
                break;
            }
        }

        if (startHeight == 0 && startWidth == 0 && endHeight == 0 && endWidth == 0) {
            return bitmap;
        } else {
            return Bitmap.createBitmap(bitmap, startWidth, startHeight, endWidth - startWidth, endHeight - startHeight);
        }
    }

    /**
     * Get a rectangle from a bounding box
     *
     * @param boundingBox The bounding box
     * @return rectangle
     */
    public static Rect getRectFromBoundingBox(BoundingBox boundingBox) {
        drawScreenRect.set((int) boundingBox.getLeft(), (int) boundingBox.getTop(),
                (int) boundingBox.getRight(), (int) boundingBox.getBottom());
        return drawScreenRect;
    }
}
