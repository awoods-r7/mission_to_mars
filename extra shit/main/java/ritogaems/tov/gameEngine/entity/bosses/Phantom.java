package ritogaems.tov.gameEngine.entity.bosses;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import ritogaems.tov.gameEngine.AssetStore;
import ritogaems.tov.gameEngine.entity.Direction;
import ritogaems.tov.gameEngine.graphics.animation.DirectedAnimation;
import ritogaems.tov.gameEngine.graphics.animation.Frame;
import ritogaems.tov.world.screens.MapScreen;
import ritogaems.tov.world.viewports.LayerViewport;
import ritogaems.tov.world.viewports.ScreenViewport;

/**
 * @author Kevin Martin
 *         <p/>
 *         A Phantom object used by the PhantomBoss
 */
public class Phantom extends PhantomBoss {

    ////////////////
    // PROPERTIES
    ////////////////

    /**
     * Stores the different stages of the phantoms
     */
    public enum phantomStages {
        SPAWN, PHANTOM_IDLE, PHANTOM_ACTIVE
    }

    /**
     * The current stage of the phantom
     */
    phantomStages stage;

    /**
     * Constructor for the Phantom class
     *
     * @param xPos      X position (centre in game units)
     * @param yPos      Y position (centre in game units)
     * @param width     Width of the draw and collision boxes
     * @param height    Height of the draw and collision boxes
     * @param mapScreen MapScreen the Phantom belongs to
     */
    public Phantom(float xPos, float yPos, float width, float height, MapScreen mapScreen) {
        super(xPos, yPos, width, height, 25, mapScreen);

        AssetStore assetStore = mapScreen.getGame().getAssetStore();

        // animation
        String name = "Phantom";
        int noOfFrames = 3;
        Bitmap walkingSheet = assetStore.getBitmap(name, "img/Enemies/Bosses/Phantom Boss Phantoms.png");
        movementAnimation = new DirectedAnimation(assetStore, name, walkingSheet, noOfFrames, 30);

        // initial settings
        Frame startFrame = movementAnimation.getFrame(Direction.CENTER);
        this.bitmap = startFrame.getFrameBitmap();
        this.collisionBox = startFrame.getFrameDrawBox();
    }

    /**
     * Update the phantom in this frame of the game loop
     */
    public void update() {
        switch (stage) {
            case SPAWN:
                move(movementVector);
                if (checkOutOfBounds()) {
                    stage = phantomStages.PHANTOM_IDLE;
                }
                break;
            case PHANTOM_IDLE:
                break;
            case PHANTOM_ACTIVE:
                move(movementVector);
                if (checkOutOfBounds()) {
                    stage = phantomStages.PHANTOM_IDLE;
                }
                if (checkArrowCollision()) {
                    stage = phantomStages.PHANTOM_IDLE;
                }
                checkPhantomPhasePlayerCollision();
                break;
        }
        updateDirection();
    }

    /**
     * Draw method that calls Sprite.draw if the conditions are met
     *
     * @param canvas         Canvas on which to draw
     * @param layerViewport  Layer Viewport to draw from
     * @param screenViewport Screen Viewport to translate the Layer Viewport
     */
    @Override
    public void draw(Canvas canvas, LayerViewport layerViewport, ScreenViewport screenViewport) {
        Frame currentFrame = movementAnimation.getFrame(direction);
        bitmap = currentFrame.getFrameBitmap();
        collisionBox = currentFrame.getFrameDrawBox();

        switch (stage) {
            case SPAWN:
                super.phantomDraw(canvas, layerViewport, screenViewport, null);
                break;
            case PHANTOM_ACTIVE:
                super.phantomDraw(canvas, layerViewport, screenViewport, paint);
                break;
            case PHANTOM_IDLE:
                break;
        }
    }
}