package ritogaems.tov.gameEngine.graphics.animation;

import android.graphics.Bitmap;

import ritogaems.tov.gameEngine.AssetStore;
import ritogaems.tov.util.GraphicsUtil;

/**
 * Animation of a list of images
 * <p/>
 * Created by Andrew Woods on 10/2/2016.
 */
public class Animation {

    // /////////////////////////////
    // Properties
    // /////////////////////////////

    private int frameIndex = 0;
    private int numberOfFrames;
    private Frame[] frames;

    private int frameDelay = 0;
    private int delayCounter = 0;

    // /////////////////////////////
    // Constructors
    // /////////////////////////////

    /**
     * Creates a new animation
     *
     * @param assetStore     To get/add animation to
     * @param spriteSheet    Spritesheet to be cut
     * @param numberOfFrames Number of frames of this animation
     * @param name           Asset store name of this animation
     */
    public Animation(AssetStore assetStore, String name, Bitmap spriteSheet, int numberOfFrames, int framesPerRow, int noOfRows, float entitySize) {
        Bitmap[] frameBitmaps = assetStore.getBitmapArray(name);

        if (frameBitmaps == null) {
            frameBitmaps = GraphicsUtil.cutBitmap(spriteSheet, numberOfFrames, framesPerRow, noOfRows);
            this.numberOfFrames = numberOfFrames;
            assetStore.add(name, frameBitmaps);
        } else {
            this.numberOfFrames = frameBitmaps.length;
        }

        frames = new Frame[frameBitmaps.length];

        for (int i = 0; i < frameBitmaps.length; i++) {
            frames[i] = new Frame(frameBitmaps[i], entitySize);
        }

    }

    /**
     * Creates a new Animation with a frame delay
     *
     * @param assetStore     To get/add animation to
     * @param name           Name of the animation
     * @param spriteSheet    Sprite sheet to be cut
     * @param numberOfFrames The number of frames in the animation
     * @param framesPerRow   The number of frames per row
     * @param noOfRows       The number of rows in the sprite sheet
     * @param entitySize     The size of the entity
     * @param frameDelay     Skip this number of interations before incrementing frame count
     */
    public Animation(AssetStore assetStore, String name, Bitmap spriteSheet, int numberOfFrames, int framesPerRow, int noOfRows, float entitySize, int frameDelay) {
        this(assetStore, name, spriteSheet, numberOfFrames, framesPerRow, noOfRows, entitySize);

        this.frameDelay = frameDelay;

    }

    // /////////////////////////////
    // Methods
    // /////////////////////////////

    /**
     * Gets the current frame, and increments to the next one
     *
     * @return The current frame
     */
    public Frame getCurrentFrame() {
        Frame currentFrame = frames[frameIndex];

        if (delayCounter >= frameDelay) {
            frameIndex = (frameIndex + 1) % numberOfFrames;
            delayCounter = 0;
        } else {
            delayCounter++;
        }

        return currentFrame;
    }

    /**
     * @return a boolean depending on frameIndex
     * @author Zoey Longridge
     * Method will draw frames start to finish once
     */
    public boolean drawOnce() {
        if (frameIndex % numberOfFrames > 0) {
            return false;
        } else {
            frameIndex = 0;
            return true;
        }
    }

    /**
     * @return The first frame
     */
    public Frame getFirstFrame() {
        return frames[0];
    }

    /**
     * @return The current frame index integer value
     */
    public int getCurrentFrameIndex() {
        return frameIndex;
    }

    /**
     * Resets frame Index to 0
     */
    public void resetFrame() {
        frameIndex = 0;
    }

    /**
     * Sets the Animation speed to the desired speed by changing the delay between frames
     */
    public void setAnimationSpeed(int frameDelay) {
        this.frameDelay = frameDelay;
    }

}