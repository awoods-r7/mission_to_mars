package ritogaems.tov.gameEngine.graphics.animation;


import android.graphics.Bitmap;

import ritogaems.tov.util.BoundingBox;

/**
 * @author Darren
 */
public class Frame {

    /**
     * Bitmap of this frame
     */
    private Bitmap frameBitmap;

    /**
     * Drawbox of this frame
     */
    private BoundingBox frameDrawBox;

    /**
     * Standard constructor.
     * Scales the drawbox based on the entity size
     *
     * @param bitmap        Bitmap of this frame
     * @param entitySize    Size of the frame by which to scale the drawbox
     */
    public Frame(Bitmap bitmap, float entitySize) {
        frameBitmap = bitmap;

        if (entitySize != 0) {
            if (frameBitmap.getHeight() > frameBitmap.getWidth()) {
                float ratio = (float) frameBitmap.getHeight() / (float) frameBitmap.getWidth();
                float boxHeight = entitySize;
                float boxWidth = entitySize / ratio;

                frameDrawBox = new BoundingBox(0, 0, boxWidth / 2, boxHeight / 2);
            } else if (frameBitmap.getWidth() > frameBitmap.getHeight()) {
                float ratio = (float) frameBitmap.getWidth() / (float) frameBitmap.getHeight();
                float boxWidth = entitySize;
                float boxHeight = entitySize / ratio;

                frameDrawBox = new BoundingBox(0, 0, boxWidth / 2, boxHeight / 2);
            } else {
                frameDrawBox = new BoundingBox(0, 0, entitySize / 2, entitySize / 2);
            }
        } else {
            frameDrawBox = new BoundingBox();
        }
    }

    /**
     * Getter
     *
     * @return the frame's bitmap
     */
    public Bitmap getFrameBitmap() {
        return frameBitmap;
    }

    /**
     * Getter
     *
     * @return the frame's drawbox
     */
    public BoundingBox getFrameDrawBox() {
        return frameDrawBox;
    }
}
