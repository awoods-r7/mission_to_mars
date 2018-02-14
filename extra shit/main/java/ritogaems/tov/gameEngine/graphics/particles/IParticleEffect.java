package ritogaems.tov.gameEngine.graphics.particles;

import android.graphics.Canvas;

import ritogaems.tov.gameEngine.ElapsedTime;
import ritogaems.tov.world.viewports.LayerViewport;
import ritogaems.tov.world.viewports.ScreenViewport;

/**
 * @author Michael Purdy
 *         Interface for a particle effect in the game
 */
public interface IParticleEffect {

    /**
     * Update the particle effect
     *
     * @param elapsedTime Time since the last update
     */
    void update(ElapsedTime elapsedTime);

    /**
     * Draw the the particle effect
     *
     * @param canvas         The canvas to draw the effect to
     * @param layerViewport  The layer viewport of what is visible
     * @param screenViewport The screen viewport
     */
    void draw(Canvas canvas, LayerViewport layerViewport, ScreenViewport screenViewport);

    /**
     * Is the particle still to be drawn and updated
     *
     * @return alive
     */
    boolean isAlive();

}
