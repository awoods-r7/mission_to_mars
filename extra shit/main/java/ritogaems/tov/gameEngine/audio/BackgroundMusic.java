package ritogaems.tov.gameEngine.audio;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

import java.io.IOException;

/**
 * @author Darren
 */
public class BackgroundMusic implements OnCompletionListener {

    /**
     * Main media player
     */
    private MediaPlayer mediaPlayer;

    /**
     * Name of file
     */
    private String assetName;

    /**
     * Playback flag
     */
    private boolean playbackReady = false;

    /**
     * The time of the track when paused
     */
    private int currentTime;

    /**
     * Standard constructor, sets up the mediaPlayer
     *
     * @param descriptor    The file descripttor for the track
     */
    public BackgroundMusic(AssetFileDescriptor descriptor) {
        assetName = descriptor.getFileDescriptor().toString();

        mediaPlayer = new MediaPlayer();

        //Configure the media player
        try {
            mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());

            mediaPlayer.prepare();
            playbackReady = true;

            mediaPlayer.setOnCompletionListener(this);
        } catch (IOException e) {
            System.err.print("The audio file " + assetName + " could not be loaded.");
            e.printStackTrace();
        }
    }


    /**
     * Play the track if it is not being played
     */
    public void play() {
        if (isPlaying()) return;

        try {
            synchronized (this) {
                if (!playbackReady) {
                    //if not prepared, prepare it
                    mediaPlayer.prepare();
                    playbackReady = true;
                }

                mediaPlayer.start();
            }
        } catch (IOException e) {
            System.err.print("The audio file " + assetName + " could not be loaded.");
            e.printStackTrace();
        } catch (IllegalStateException e) {
            System.err.print("The audio file " + assetName + " was in an invalid state.");
            e.printStackTrace();
        }
    }

    /**
     * Stop the track
     */
    private void stop() {
        if (isPlaying()) mediaPlayer.stop();

        synchronized (this) {
            playbackReady = false;
        }
    }

    /**
     * Pauses the and saves the current time of the track
     */
    public void pause() {
        if (isPlaying()) {
            mediaPlayer.pause();

            currentTime = mediaPlayer.getCurrentPosition();
        }
    }

    /**
     * Resumes track from where it was paused
     */
    public void resume() {
        if (!isPlaying() && playbackReady) {
            mediaPlayer.seekTo(currentTime);

            mediaPlayer.start();
        }
    }

    /**
     * Whether the track is playing or not
     *
     * @return  boolean determining the above
     */
    private boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    /**
     * Sets volume on both channels simultaneously
     *
     * @param volume    Float value between 0.0 and 1.0
     */
    public void setMonoVolume(float volume) {
        mediaPlayer.setVolume(volume, volume);
    }

    /**
     * Sets volume on 2 channels individually, left and right. Float values between 0.0 and 1.0
     *
     * @param lVol  Left channel volume
     * @param rVol  Right channel volume
     */
    public void setStereoVolume(float lVol, float rVol) {
        mediaPlayer.setVolume(lVol, rVol);
    }

    //Returns true if clip is looping or false if not
    public boolean isLooping() {
        return mediaPlayer.isLooping();
    }

    /**
     * Set's the track to loop or not
     *
     * @param b boolean determining the above
     */
    public void setLooping(boolean b) {
        mediaPlayer.setLooping(b);
    }

    /**
     * Dispose of current track
     */
    public void dispose() {
        stop();
        mediaPlayer.release();
    }

    /**
     * //Method implemented by OnCompletionListener interface
     *
     * @param player    The media player
     */
    public void onCompletion(MediaPlayer player) {
        synchronized (this) {
            playbackReady = false;
        }
    }

}
