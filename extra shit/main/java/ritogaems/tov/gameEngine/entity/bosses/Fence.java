package ritogaems.tov.gameEngine.entity.bosses;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import ritogaems.tov.gameEngine.graphics.animation.Sprite;
import ritogaems.tov.util.CollisionDetector;
import ritogaems.tov.world.screens.GameScreen;
import ritogaems.tov.world.viewports.LayerViewport;
import ritogaems.tov.world.viewports.ScreenViewport;

/**
 * @author Kevin Martin
 *         <p/>
 *         A Fence object used by the Phantom Boss
 */
public class Fence extends Sprite {

    ////////////////
    // PROPERTIES
    ////////////////

    /**
     * Boolean to check if the fence currently exists
     */
    boolean exists;

    /**
     * The GameScreen that the Fence belongs to
     */
    private GameScreen gameScreen;

    /**
     * Constructor for the Fence object
     *
     * @param xPos       X position (centre in game units)
     * @param yPos       Y position (centre in game units)
     * @param width      Width of the draw and collision boxes
     * @param height     Height of the draw and collision boxes
     * @param bitmap     Bitmap of the Sprite
     * @param gameScreen The GameScreen to which the Fence belongs
     */
    public Fence(float xPos, float yPos, float width, float height, Bitmap bitmap, GameScreen gameScreen) {
        super(xPos, yPos, width, height, width, height, bitmap);

        this.gameScreen = gameScreen;

        exists = false;
    }

    /**
     * Update the fence is this frame of the game loop
     */
    public void update() {
        if (exists) {
            resolvePlayerCollision();
        }
    }

    /**
     * Sets the exists boolean
     *
     * @param bool the boolean value to set exists to
     */
    public void setExists(Boolean bool) {
        exists = bool;
    }

    /**
     * Resolves player collision by moving them back to their original position
     */
    private void resolvePlayerCollision() {
        CollisionDetector.determineAndResolveCollision(gameScreen.getPlayer(), this, false);
    }

    /**
     * Checks the type of collision currently happening to see if it is the bottom of the player colliding
     *
     * @return The boolean value returned for the above condition
     */
    public boolean checkPlayerCollision() {
        CollisionDetector.CollisionType collisionType = CollisionDetector.determineCollisionType(gameScreen.getPlayer(), this, false);
        return collisionType == CollisionDetector.CollisionType.Bottom;
    }

    /**
     * Draw method that calls Sprite.draw if the fence currently exists
     *
     * @param canvas         Canvas on which to draw
     * @param layerViewport  Layer Viewport to draw from
     * @param screenViewport Screen Viewport to translate the Layer Viewport
     */
    @Override
    public void draw(Canvas canvas, LayerViewport layerViewport, ScreenViewport screenViewport) {
        if (exists) {
            super.draw(canvas, layerViewport, screenViewport);
        }
    }
}