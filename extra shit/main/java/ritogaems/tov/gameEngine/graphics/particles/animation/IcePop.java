package ritogaems.tov.gameEngine.graphics.particles.animation;


import ritogaems.tov.gameEngine.audio.SoundEffect;
import ritogaems.tov.world.screens.MapScreen;

public class IcePop extends ParticleAnimation {

    /**
     * Map screen to which this belongs
     */
    private MapScreen mapScreen;

    /**
     *
     *
     * @param x             X position
     * @param y             Y position
     * @param drawWidth     Drawing width
     * @param drawHeight    Drawing height
     * @param mapScreen     Map screen to which this belongs
     */
    public IcePop(float x, float y, float drawWidth, float drawHeight, MapScreen mapScreen) {
        super("IcePop", // name
                x,
                y,
                drawWidth,
                drawHeight,
                "img/Particles/IceParticles/IcePop.png",
                6,  // number of frames
                6,   // row index
                1,   // number of rows
                mapScreen.getGame().getAssetStore()
        );

        this.mapScreen = mapScreen;

        // Initialise and play sound effect
        SoundEffect soundEffect = this.mapScreen.getGame().getAssetStore().getSfx("icePop", "audio/sfx/IcePop.wav");
        soundEffect.playWithRelativeVolume(mapScreen.layerViewport, position);
    }

}
