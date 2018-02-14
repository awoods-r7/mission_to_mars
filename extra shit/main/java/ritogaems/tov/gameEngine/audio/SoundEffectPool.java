package ritogaems.tov.gameEngine.audio;


import java.util.ArrayList;
import java.util.Random;

import ritogaems.tov.util.Vector2;
import ritogaems.tov.world.viewports.LayerViewport;

/**
 * @author Darren
 */
public class SoundEffectPool {

    /**
     * List of sound effetcs
     */
    private ArrayList<SoundEffect> pool;

    /**
     * Random object for choosing sound effect at random
     */
    private Random random = new Random();

    /**
     * Standard constructor
     */
    public SoundEffectPool() {
        pool = new ArrayList<>();

    }

    /**
     * Play one of the sound effects at random
     */
    public void play() {
        pool.get(random.nextInt(pool.size())).play(false);
    }

    /**
     * Plays the sound with volume based on the sounds source in relation
     * to the viewport
     *
     * @param viewport The viewport from which to judge the source
     * @param position The source's position
     */
    public void playWithRelativeVolume(LayerViewport viewport, Vector2 position) {
        pool.get(random.nextInt(pool.size())).playWithRelativeVolume(viewport, position);
    }

    /**
     * Adds a new sound effec to the pool
     *
     * @param newSoundEffect    The new sound effect
     */
    public void add(SoundEffect newSoundEffect) {
        pool.add(newSoundEffect);
    }
}
