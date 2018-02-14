package ritogaems.tov.gameEngine.audio;

import android.media.SoundPool;

import ritogaems.tov.util.Vector2;
import ritogaems.tov.world.viewports.LayerViewport;

/**
 * @author Darren
 */
public class SoundEffect {

    /**
     * Local instance of the sound pool
     */
    private SoundPool soundPool;

    /**
     * ID of the specified sound effect in the sound pool
     */
    private int soundEffectID;

    /**
     * Maximum number of simultaneous sound effects
     */
    public static final int MAX_SIMUL_SFX = 10;

    /**
     * Standard constructor
     *
     * @param soundPool     Local instance of the sound pool
     * @param sfxID         ID of the specified sound effect in the sound pool
     */
    public SoundEffect(SoundPool soundPool, int sfxID) {
        this.soundPool = soundPool;
        soundEffectID = sfxID;
    }

    /**
     * Plays the sound at maximum volume, with the choice of looping
     *
     * @param loop  Boolean determining the above
     * @return      The streamID of this play
     */
    public int play(Boolean loop) {
        return soundPool.play(soundEffectID, 1.0f, 1.0f, 0, getLoopValue(loop), 1.0f);
    }

    /**
     * Plays the sound at given mono volume, with the choice of looping
     *
     * @param vol   The selected volume
     * @param loop  Boolean determining the above
     * @return      The streamID of this play
     */
    public int play(float vol, Boolean loop) {
        return soundPool.play(soundEffectID, vol, vol, 0, getLoopValue(loop), 1.0f);
    }

    /**
     * Plays the sound at given stereo volume, with the choice of looping
     *
     * @param lVol  The selected left channel volume
     * @param rVol  The selected right channel volume
     * @param loop  Boolean determining the above
     * @return      The streamID of this play
     */
    public int play(float lVol, float rVol, Boolean loop) {
        return soundPool.play(soundEffectID, lVol, rVol, 0, getLoopValue(loop), 1.0f);
    }

    /**
     * Plays the sound based on the sound's source in relation
     * to the viewport
     *
     * @param viewport  The viewport from which to judge the source
     * @param position  The source's position
     * @return          The streamID of this play
     */
    public int playWithRelativeVolume(LayerViewport viewport, Vector2 position) {
        if (viewport.contains(position.x, position.y)) {
            double lVol = 1.0f;
            double rVol = 1.0f;

            float distance = Math.abs(position.x - viewport.x);
            double percentPos = (double) distance / viewport.halfWidth;

            if (position.x > viewport.x) {
                lVol = getVolumeArea(percentPos);
            } else if (viewport.x > position.x) {
                rVol = getVolumeArea(percentPos);
            }
            return play((float) lVol, (float) rVol, false);
        }

        return 0;
    }

    /**
     * Sets the volume based on the sounds source in relation
     * to the viewport, on a specific stream
     *
     * @param streamID  The streamID of this play
     * @param viewport  The viewport from which to judge the source
     * @param position  The source's position
     */
    public void setRelativeVolume(int streamID, LayerViewport viewport, Vector2 position) {
        if (viewport.contains(position.x, position.y)) {
            double lVol = 1.0f;
            double rVol = 1.0f;

            float distance = Math.abs(position.x - viewport.x);
            double percentPos = (double) distance / viewport.halfWidth;

            if (position.x > viewport.x) {
                lVol = getVolumeArea(percentPos);
            } else if (viewport.x > position.x) {
                rVol = getVolumeArea(percentPos);
            }
            setStereoVolume(streamID, (float) lVol, (float) rVol);
        }
    }

    /**
     * Sets the volume of the specified stream to the speficied stereo volume
     *
     * @param streamID  The stream to apply the volume
     * @param lVol      Left channel volume
     * @param rVol      Right channel volume
     */
    public void setStereoVolume(int streamID, float lVol, float rVol) {
        soundPool.setVolume(streamID, lVol, rVol);
    }

    /**
     * Sets the volume of the specified stream to the speficied mono volume
     *
     * @param streamID  The stream to apply the volume
     * @param volume    The mono volume
     */
    public void setMonoVolume(int streamID, float volume) {
        soundPool.setVolume(streamID, volume, volume);
    }

    /**
     * Stops the sound playing on the specified stream
     *
     * @param streamID  The specified stream
     */
    public void stop(int streamID) {
        soundPool.stop(streamID);
    }

    /**
     * Dispose the specified sound effect
     */
    public void dispose() {
        soundPool.unload(soundEffectID);
    }

    /**
     * Gets the volume 'area' based on what percentage of the viewport
     * an object was
     *
     * @param percentPos The percentage
     * @return           Thea volume for that area
     */
    private float getVolumeArea(double percentPos) {
        if (percentPos <= 0.33) {
            return 1.0f;
        } else if (percentPos <= 0.67) {
            return 0.7f;
        } else if (percentPos <= 0.9) {
            return 0.5f;
        } else {
            return 0.3f;
        }
    }

    /**
     * Gets the loop value based on a boolean (used for soundPool)
     *
     * @param b Boolean as determined above
     * @return  The value for usage with sound pool
     */
    private int getLoopValue(Boolean b) {
        if (b) {
            return -1;
        } else {
            return 0;
        }
    }

}
