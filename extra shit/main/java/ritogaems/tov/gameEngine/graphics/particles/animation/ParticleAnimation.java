package ritogaems.tov.gameEngine.graphics.particles.animation;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import ritogaems.tov.gameEngine.AssetStore;
import ritogaems.tov.gameEngine.ElapsedTime;
import ritogaems.tov.gameEngine.graphics.animation.Animation;
import ritogaems.tov.gameEngine.graphics.animation.Frame;
import ritogaems.tov.gameEngine.graphics.animation.Sprite;
import ritogaems.tov.gameEngine.graphics.particles.IParticleEffect;
import ritogaems.tov.world.viewports.LayerViewport;
import ritogaems.tov.world.viewports.ScreenViewport;

/**
 * @author Michael Purdy
 *         A particle effect that uses a sprite sheet to create an animation
 *         Alternative to particle emitter because it was causing performance issues
 */
public abstract class ParticleAnimation extends Sprite implements IParticleEffect {

    /**
     * The animation of the particle effect
     */
    private Animation animation;

    /**
     * If the particle effect is still alive
     */
    private boolean alive;

    /**
     * Constructor
     *
     * @param name       Name of the particle animation
     * @param x          X position (centre in game units)
     * @param y          Y position (centre in game units)
     * @param width      Width of animation
     * @param height     Height of animation
     * @param bitmapPath Path to sprite sheet
     * @param numFrames  Number of frames in animation
     * @param rowIndex   Row index of the animation in the sprite sheet
     * @param numRows    The number of rows in the sprite sheet
     * @param assetStore The asset store of the game
     */
    ParticleAnimation(String name, float x, float y, float width, float height, String bitmapPath, int numFrames, int rowIndex, int numRows, AssetStore assetStore) {
        super(x, y, width, height, width, height);

        Bitmap spriteSheet = assetStore.getBitmap(name, bitmapPath);
        this.animation = new Animation(assetStore, name, spriteSheet, numFrames, rowIndex, numRows, 0);
        Frame startFrame = animation.getCurrentFrame();
        bitmap = startFrame.getFrameBitmap();
        alive = true;
    }

    /**
     * Update the animation
     *
     * @param elapsedTime Time since the last update
     */
    @Override
    public void update(ElapsedTime elapsedTime) {
        if (animation.getCurrentFrameIndex() == 0) {
            alive = false;
        }
    }

    /**
     * Draw the animation
     *
     * @param canvas         The canvas to draw the effect to
     * @param layerViewport  The layer viewport of what is visible
     * @param screenViewport The screen viewport
     */
    @Override
    public void draw(Canvas canvas, LayerViewport layerViewport, ScreenViewport screenViewport) {
        Frame currentFrame = animation.getCurrentFrame();
        bitmap = currentFrame.getFrameBitmap();
        super.draw(canvas, layerViewport, screenViewport);
    }

    /**
     * Check if the particle animation is alive
     *
     * @return alive
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Setter
     *
     * @param alive
     */
    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
