package ritogaems.tov.gameEngine.graphics.tilemapEngine;

import android.graphics.Bitmap;

import java.util.HashMap;

/**
 * @author Michael Purdy
 * @author Darren McGarry
 *         Holds the bitmaps for a tile sheet
 */
public class TileSheet {

    /**
     * Size in game units of one tile
     */
    public static final int TILE_SIZE = 10;
    public static final int HALF_TILE_SIZE = 5;

    /**
     * Map of tiles
     */
    private HashMap<Integer, Bitmap> tiles;

    /**
     * The number of pixels along a tile's side in the tile sheet (assumed to be square)
     */
    private int tilePixels;

    /**
     * Constructor
     *
     * @param tileSheet  The tile sheet bitmap
     * @param tilePixels The number of pixels along a tile's side in the tile sheet (assumed to be square)
     */
    public TileSheet(Bitmap tileSheet, int tilePixels) {

        tiles = new HashMap<>();
        this.tilePixels = tilePixels;

        int tileColumns = tileSheet.getWidth() / tilePixels;
        int tileRows = tileSheet.getHeight() / tilePixels;

        // generate the hash map
        for (int i = 0; i < tileRows; i++) {
            for (int j = 0; j < tileColumns; j++) {
                int x = j * tilePixels;
                int y = i * tilePixels;
                Bitmap tile = Bitmap.createBitmap(tileSheet, x, y, tilePixels, tilePixels);
                int tileId = (i * tileColumns) + j + 1;
                tiles.put(tileId, tile);
            }
        }


    }

    /**
     * Getter
     *
     * @param tileId ID of the tile
     * @return Tile with the specified ID
     */
    public Bitmap getTileBitmap(int tileId) {
        return tiles.get(tileId);
    }

    /**
     * Getter
     *
     * @return The number of pixels along a tile's side in the tile sheet (assumed to be square)
     */
    public int getTileSizeInPixels() {
        return tilePixels;
    }

}
