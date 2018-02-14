package ritogaems.tov.gameEngine.graphics.animation;

import android.graphics.Bitmap;

import java.util.HashMap;

import ritogaems.tov.gameEngine.entity.Direction;
import ritogaems.tov.gameEngine.AssetStore;
import ritogaems.tov.util.GraphicsUtil;

/**
 * Animation with 4 distinct directions
 * <p/>
 * Created by Andrew Woods on 10/2/2016.
 */
public class DirectedAnimation {

    private HashMap<Direction, Animation> animations = new HashMap<>();
    public Direction prevDirection;

    /**
     * Creates an array of animations for UP, DOWN, LEFT and RIGHT that can be accessed through the assetStore
     * It also stores the previous direction
     *
     * @param assetStore A reference to the assetStore
     * @param name       A name so that the animations can be saved in the asset Store
     * @param bitmap     A spritesheet bitmap with 4 different directions
     * @param noOfFrames The number of frames for each animation so it can be cut up
     * @param entitySize entity size specifies how the sprite will scale in size with game units
     */
    public DirectedAnimation(AssetStore assetStore, String name, Bitmap bitmap, int noOfFrames, float entitySize) {
        Bitmap[] strips = GraphicsUtil.cutDirectedBitmap(bitmap, 4);

        animations.put(Direction.UP, new Animation(assetStore, name + "_UP", strips[0], noOfFrames, noOfFrames, 1, entitySize));
        animations.put(Direction.DOWN, new Animation(assetStore, name + "_DOWN", strips[1], noOfFrames, noOfFrames, 1, entitySize));
        animations.put(Direction.RIGHT, new Animation(assetStore, name + "_RIGHT", strips[2], noOfFrames, noOfFrames, 1, entitySize));
        animations.put(Direction.LEFT, new Animation(assetStore, name + "_LEFT", strips[3], noOfFrames, noOfFrames, 1, entitySize));

        prevDirection = Direction.DOWN;
    }

    /**
     * Same function as constructor above except for frameDelay
     *
     * @param frameDelay this allows you to slow down the animation by increasing the time each frame appears for
     */
    public DirectedAnimation(AssetStore assetStore, String name, Bitmap bitmap, int noOfFrames, float entitySize, int frameDelay) {
        Bitmap[] strips = GraphicsUtil.cutDirectedBitmap(bitmap, 4);

        animations.put(Direction.UP, new Animation(assetStore, name + "_UP", strips[0], noOfFrames, noOfFrames, 1, entitySize, frameDelay));
        animations.put(Direction.DOWN, new Animation(assetStore, name + "_DOWN", strips[1], noOfFrames, noOfFrames, 1, entitySize, frameDelay));
        animations.put(Direction.RIGHT, new Animation(assetStore, name + "_RIGHT", strips[2], noOfFrames, noOfFrames, 1, entitySize, frameDelay));
        animations.put(Direction.LEFT, new Animation(assetStore, name + "_LEFT", strips[3], noOfFrames, noOfFrames, 1, entitySize, frameDelay));

        prevDirection = Direction.DOWN;
    }

    /**
     * get Frame that is currently representing animation
     *
     * @param direction
     * @return current frame from the animation
     */
    public Frame getFrame(Direction direction) {
        if (direction == Direction.CENTER) {
            return animations.get(prevDirection).getFirstFrame();
        } else {
            prevDirection = direction;
            return animations.get(direction).getCurrentFrame();
        }
    }

    /**
     * Draw animation in the direction its currently in once
     *
     * @param direction
     * @return animation direction
     */
    public boolean drawOnce(Direction direction) {
        if (direction == Direction.CENTER) {
            return animations.get(prevDirection).drawOnce();
        } else {
            prevDirection = direction;
            return animations.get(direction).drawOnce();
        }
    }

    /**
     * Resets the frames for each direction in the Directed Animation
     */
    public void resetFrames() {
        animations.get(Direction.UP).resetFrame();
        animations.get(Direction.DOWN).resetFrame();
        animations.get(Direction.RIGHT).resetFrame();
        animations.get(Direction.LEFT).resetFrame();
    }

    /**
     * Reduces/increases speed of animation, by changing the frequency that the frames are updated
     *
     * @param frameDelay
     */
    public void setAnimationSpeed(int frameDelay) {
        for (Animation animation : animations.values()) {
            animation.setAnimationSpeed(frameDelay);
        }
    }
}