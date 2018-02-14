package ritogaems.tov.gameEngine.audio;

import ritogaems.tov.util.Vector2;
import ritogaems.tov.world.viewports.LayerViewport;

public class LoopableSoundEffect {

    /**
     * The sound effect to be looped
     */
    private SoundEffect soundEffect;

    /**
     * ID of the stream the sound effect is playing on
     */
    private int streamId;

    /**
     * Standard constructor
     *
     * @param soundEffect   The sound effect to be looped
     */
    public LoopableSoundEffect(SoundEffect soundEffect) {
        this.soundEffect = soundEffect;
        streamId = 0;
    }

    /**
     * Play the sound effect, looping it
     */
    public void play() {
        streamId = soundEffect.play(true);
    }

    /**
     * Play the sound effect, looping it, at a given mono volume
     *
     * @param vol   Single channel volume
     */
    public void play(float vol) {
        streamId = soundEffect.play(vol, true);
    }

    /**
     * Play the sound effect, looping it, at a given stereo volume
     *
     * @param lVol  Left channel volume
     * @param rVol  Right channel volume
     */
    public void play(float lVol, float rVol) {
        streamId = soundEffect.play(lVol, rVol, true);
    }

    /**
     * Sets the volume based on the sounds source in relation
     * to the viewport
     *
     * @param viewport  The viewport from which to judge the source
     * @param position  The source's position
     */
    public void setRelativeVolume(LayerViewport viewport, Vector2 position) {
        soundEffect.setRelativeVolume(streamId, viewport, position);
    }

    /**
     * Stops the sound effect based on it's stream
     */
    public void stop() {
        soundEffect.stop(streamId);
    }

}
