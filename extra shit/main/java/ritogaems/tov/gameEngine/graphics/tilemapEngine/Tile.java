package ritogaems.tov.gameEngine.graphics.tilemapEngine;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import ritogaems.tov.gameEngine.graphics.animation.Sprite;
import ritogaems.tov.world.viewports.LayerViewport;
import ritogaems.tov.world.viewports.ScreenViewport;

/**
 * @author Darren
 */
public class Tile extends Sprite {

    /**
     * ID of the tile
     */
    private int ID;

    /**
     * X and Y coordinated of the tile
     */
    public int xCoord, yCoord;

    /**
     * Standard constructor
     *
     * @param id        ID of the tile
     * @param xPos      X position in the world
     * @param yPos      Y position in the world
     * @param xCoord    X Coordinate in the array
     * @param yCoord    Y Coordinate in the array
     * @param bitmap    The bitmap of the Tile
     */
    public Tile(int id, float xPos, float yPos, int xCoord, int yCoord, Bitmap bitmap) {
        super(xPos, yPos, TileSheet.TILE_SIZE, TileSheet.TILE_SIZE, TileSheet.TILE_SIZE, TileSheet.TILE_SIZE, bitmap);
        this.ID = id;

        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    // Draws the tile bit map to the canvas if it is within the viewport
    @Override
    public void draw(Canvas canvas, LayerViewport layerViewport, ScreenViewport screenViewport) {
        //At the moment, draw could be called straight from sprite,
        //overriding just in case there is anything extra to add
        super.draw(canvas, layerViewport, screenViewport);
    }

}
