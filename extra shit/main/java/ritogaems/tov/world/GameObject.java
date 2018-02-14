package ritogaems.tov.world;

import android.graphics.Canvas;

import ritogaems.tov.util.Vector2;
import ritogaems.tov.util.BoundingBox;
import ritogaems.tov.world.screens.GameScreen;
import ritogaems.tov.world.viewports.LayerViewport;
import ritogaems.tov.world.viewports.ScreenViewport;

/**
 * Object in the game
 * Adapted from Gage
 * - separated collision and draw box
 * - fraction of screen constructor
 */
public abstract class GameObject {

    // /////////////////////////////
    // Properties
    // /////////////////////////////

    /**
     * Position of the object in the game space
     */
    public Vector2 position = new Vector2();

    /**
     * Boxes to define the collision and drawing bounds of the object
     */
    protected BoundingBox collisionBox = new BoundingBox();
    protected BoundingBox drawBox = new BoundingBox();

    // /////////////////////////////
    // Constructors
    // /////////////////////////////

    /**
     * Creates an empty game object (used by particles)
     */
    protected GameObject() {
    }

    /**
     * Creates a Game Object
     * Takes in separate values for the draw and collision boxes
     *
     * @param xPos            X position (centre in game units)
     * @param yPos            Y position (centre in game units)
     * @param drawWidth       Width of the draw box
     * @param drawHeight      Height of the draw box
     * @param collisionWidth  Width of the collision box
     * @param collisionHeight Height of the collision box
     */
    protected GameObject(float xPos, float yPos, float drawWidth, float drawHeight, float collisionWidth, float collisionHeight) {
        position.x = xPos;
        position.y = yPos;

        drawBox.x = xPos;
        drawBox.y = yPos;
        drawBox.halfWidth = drawWidth / 2.0f;
        drawBox.halfHeight = drawHeight / 2.0f;

        collisionBox.x = xPos;
        collisionBox.y = yPos;
        collisionBox.halfWidth = collisionWidth / 2.0f;
        collisionBox.halfHeight = collisionHeight / 2.0f;
    }

    /**
     * Creates a Game Object
     * Sets the draw and collision boxes to the same dimensions
     *
     * @param xPos   X position (centre in game units)
     * @param yPos   Y position (centre in game units)
     * @param width  Width of the boxes
     * @param height Height of the boxes
     */
    protected GameObject(float xPos, float yPos, float width, float height) {
        position.x = xPos;
        position.y = yPos;

        drawBox.x = xPos;
        drawBox.y = yPos;
        drawBox.halfWidth = width / 2.0f;
        drawBox.halfHeight = height / 2.0f;

        collisionBox.x = xPos;
        collisionBox.y = yPos;
        collisionBox.halfWidth = width / 2.0f;
        collisionBox.halfHeight = height / 2.0f;
    }

    /**
     * @param xFraction       The fraction of the width the object's width is
     * @param yFraction       The fraction of the height the object's height is
     * @param xOffsetFraction The indent from the left as a fraction of the width
     * @param yOffsetFraction The indent from the top as a fraction of the height
     * @param gameScreen      Game screen where this object resides
     * @author Michael Purdy
     * Creates a Game Object
     * Sets the position and size based on fractions of the screen
     */
    protected GameObject(GameScreen gameScreen, float xFraction, float yFraction,
                         float xOffsetFraction, float yOffsetFraction) {

        int screenWidth = gameScreen.getGame().getScreenWidth();
        int screenHeight = gameScreen.getGame().getScreenHeight();

        float halfWidth = screenWidth * xFraction / 2;
        float halfHeight = screenHeight * yFraction / 2;

        position.x = (xOffsetFraction * screenWidth) + halfWidth;
        position.y = (yOffsetFraction * screenHeight) + halfHeight;

        drawBox.reset(position.x, position.y, halfWidth, halfHeight);
        collisionBox.reset(position.x, position.y, drawBox.halfWidth, drawBox.halfHeight);
    }

    // /////////////////////////////
    // Methods
    // /////////////////////////////

    /**
     * Sets the bounds of the collision box
     *
     * @param xPos   X position
     * @param yPos   Y position
     * @param width  Width of the box
     * @param height Height of the box
     */
    protected void setCollisionBounds(float xPos, float yPos, float width, float height) {
        position.x = xPos;
        position.y = yPos;

        collisionBox.x = xPos;
        collisionBox.y = yPos;
        collisionBox.halfWidth = width / 2.0f;
        collisionBox.halfHeight = height / 2.0f;
    }


    /**
     * Getter
     *
     * @return Object's collision box
     */
    public BoundingBox getCollisionBox() {
        collisionBox.x = position.x;
        collisionBox.y = position.y;
        return collisionBox;
    }

    /**
     * Getter
     *
     * @return Object's draw box
     */
    public BoundingBox getDrawBox() {
        drawBox.x = position.x;
        drawBox.y = position.y;
        return drawBox;
    }

    /**
     * Sets the position of the Game Object
     *
     * @param xPos X position
     * @param yPos Y position
     */
    public void setPosition(float xPos, float yPos) {
        collisionBox.x = drawBox.x = position.x = xPos;
        collisionBox.y = drawBox.y = position.y = yPos;
    }

    /**
     * Updates the position of the game object
     *
     * @param xPos X increment/decrement
     * @param yPos Y increment/decrement
     */
    protected void updatePosition(float xPos, float yPos) {
        collisionBox.x += xPos;
        collisionBox.y += yPos;

        drawBox.x += xPos;
        drawBox.y += yPos;

        position.x += xPos;
        position.y += yPos;
    }


    /**
     * (Abstract) Draws the object
     *
     * @param canvas         Canvas on which to draw
     * @param layerViewport  Layer Viewport to draw from
     * @param screenViewport Screen Viewport to translate the Layer Viewport
     */
    public abstract void draw(Canvas canvas, LayerViewport layerViewport, ScreenViewport screenViewport);

}