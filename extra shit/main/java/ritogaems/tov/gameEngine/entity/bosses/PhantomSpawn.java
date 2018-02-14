package ritogaems.tov.gameEngine.entity.bosses;

import ritogaems.tov.util.Vector2;

/**
 * @author Kevin Martin
 *         <p/>
 *         A Phantom Spawn used by the Phantom Boss
 */
class PhantomSpawn {

    ////////////////
    // PROPERTIES
    ////////////////

    /**
     * The position of this Phantom Spawn
     */
    Vector2 spawnPos;

    /**
     * Boolean to track if this Phantom Spawn is already being used
     */
    private boolean spawnUsed;

    /**
     * Constructor for a Phantom Spawn
     *
     * @param xPos X position (centre in game units)
     * @param yPos Y position (centre in game units)
     */
    public PhantomSpawn(int xPos, int yPos) {
        spawnPos = new Vector2(xPos, yPos);

        spawnUsed = false;
    }

    /**
     * Getter for the spawnUsed variable
     *
     * @return returns the spawnUsed boolean
     */
    public boolean getSpawnUsed() {
        return spawnUsed;
    }

    /**
     * Sets the spawnUsed variable
     *
     * @param state the boolean used to set spawnUsed
     */
    public void setSpawnUsed(boolean state) {
        spawnUsed = state;
    }
}