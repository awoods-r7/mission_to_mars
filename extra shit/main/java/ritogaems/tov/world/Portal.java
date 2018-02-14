package ritogaems.tov.world;

import android.graphics.Canvas;

import ritogaems.tov.gameEngine.ScreenManager;
import ritogaems.tov.gameEngine.entity.Player;
import ritogaems.tov.util.Vector2;
import ritogaems.tov.world.screens.MapScreen;
import ritogaems.tov.world.viewports.LayerViewport;
import ritogaems.tov.world.viewports.ScreenViewport;

/**
 * @author Michael Purdy
 *         A Portal is a way to travel to another area in the same map or a new map
 */
public class Portal extends GameObject {

    /**
     * The map the portal leads to and the position in that map
     */
    private String destinationMap;
    private Vector2 destinationPosition;

    /**
     * Creates a 1 tile portal
     *
     * @param sourceX            The middle of the portal x co-ordinate (game units)
     * @param sourceY            The middle of the portal y co-ordinate (game units)
     * @param destinationMapName The name of the map the portal goes to
     * @param destX              The x co-ordinate in the destination map the portal goes to (game units)
     * @param destY              The y co-ordinate in the destination map the portal goes to (game units)
     */
    public Portal(int sourceX, int sourceY, String destinationMapName, float destX, float destY) {
        super(sourceX, sourceY, 10, 10);

        this.destinationMap = destinationMapName;
        this.destinationPosition = new Vector2(destX, destY);
    }

    /**
     * Transport the player to the position in the new map
     *
     * @param screenManager The manager for the current screen
     * @param player        The player being transported
     */
    public void activatePortal(ScreenManager screenManager, Player player) {
        changePlayerScreen(screenManager, player, destinationMap, destinationPosition.x, destinationPosition.y);
    }

    /**
     * Draw the portal (blank)
     *
     * @param canvas         Canvas on which to draw
     * @param layerViewport  Layer Viewport to draw from
     * @param screenViewport Screen Viewport to translate the Layer Viewport
     */
    @Override
    public void draw(Canvas canvas, LayerViewport layerViewport, ScreenViewport screenViewport) {
    }

    /**
     * Move the player to a new screen
     *
     * @param screenManager The screen manager
     * @param player        The player to move
     * @param newScreenName The name of the new screen
     * @param x             The x position in the new screen (Game Units)
     * @param y             The y position in the new screen (Game Units)
     */
    public static void changePlayerScreen(ScreenManager screenManager, Player player, String newScreenName, float x, float y) {

        // get the new screen
        MapScreen newScreen = (MapScreen) screenManager.getScreen(newScreenName);

        // set the players position
        player.setPosition(x, y);

        // move the player
        newScreen.replacePlayer(player);

        // display the new screen
        screenManager.setAsCurrentScreen(newScreen.getName());
    }
}
