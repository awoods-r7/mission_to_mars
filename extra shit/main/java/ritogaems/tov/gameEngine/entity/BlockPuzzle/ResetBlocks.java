package ritogaems.tov.gameEngine.entity.BlockPuzzle;

import ritogaems.tov.gameEngine.graphics.animation.Sprite;
import ritogaems.tov.world.screens.MapScreen;

/**
 * @author Zoey Longridge
 *
 * ResetBlocks is an interaction between player and pressure points
 */
public class ResetBlocks extends Sprite {

    ////////////////
    // PROPERTIES
    ////////////////

    /**
     * Mapscreen that the chest belongs on
     */
    private MapScreen mapScreen;

    /**
     * Constructor for ResetBlocks
     *
     * @param xPos            X position
     * @param yPos            Y position
     * @param drawWidth       Width of the draw box
     * @param drawHeight      Height of the draw box
     * @param collisionWidth  Width of the collision box
     * @param collisionHeight Height of the collision box
     * @param mapScreen       Get access to other game objects in map
     */
    public ResetBlocks(float xPos, float yPos, float drawWidth, float drawHeight,
                         float collisionWidth, float collisionHeight, MapScreen mapScreen) {
        super(xPos, yPos, drawWidth, drawHeight, collisionWidth, collisionHeight);
        this.mapScreen = mapScreen;
        bitmap = mapScreen.getGame().getAssetStore().getBitmap("Reset Pressure Point", "img/Puzzle/ResetPressurePoint.png");
    }

    /**
     * Method to update block position on this frame of the game loop
     */
    public void update() {
        if(isReset()){
            for(Block block : mapScreen.blocks){
                block.reset();
            }
        }
    }

    /**
     *
     * @return If player intersects with pressure point then return true
     */
    public boolean isReset() {
            if (this.collisionBox.intersects(mapScreen.getPlayer().getCollisionBox())){
                return true;
            }
        return false;
    }

}
