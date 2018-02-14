package ritogaems.tov.gameEngine.graphics;

import android.view.View;

import ritogaems.tov.world.screens.GameScreen;

/**
 * @author Michael Purdy
 *         Interface for accessing render and getView methods
 */
public interface IRenderSurface {

    /**
     * render the game screen to the surface
     *
     * @param gameScreen The game screen to render
     */
    void render(GameScreen gameScreen);

    /**
     * Get the surface as a view
     *
     * @return View of the surface
     */
    View getAsView();

}
