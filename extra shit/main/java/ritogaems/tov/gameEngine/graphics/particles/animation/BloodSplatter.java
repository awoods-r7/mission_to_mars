package ritogaems.tov.gameEngine.graphics.particles.animation;

import ritogaems.tov.gameEngine.AssetStore;

/**
 * @author Michael Purdy
 *         Particle animation of a blood splatter
 */
public class BloodSplatter extends ParticleAnimation {

    /**
     * Constructor for single blood splatter effect
     *
     * @param x          X position (centre in game units)
     * @param y          Y position (centre in game units)
     * @param drawWidth  The width of the blood splatter
     * @param drawHeight The height of the blood splatter
     * @param assetStore The asset store used to get the images
     */
    public BloodSplatter(float x, float y, float drawWidth, float drawHeight, AssetStore assetStore) {
        super("bloodSplatter",  // name
                x,              // X position (centre in game units)
                y,              // Y position (centre in game units)
                drawWidth,      // draw width (game units)
                drawHeight,     // draw height (game units)
                "img/Particles/blood_hit_02.png",   // bitmap path
                16,             // number of frames
                4,              // row index
                4,              // number of rows
                assetStore      // asset store
        );
    }

}
