package ritogaems.tov.gameEngine;

/**
 * @author Michael Purdy
 *         The difficulty of the game
 */
public enum Difficulty {

    /**
     * Difficulty levels
     */
    EASY(1.0f, 0.5f, 2.0f),
    HARD(0.5f, 1.0f, 1.0f),
    GOD(1.0f, 0.0f, 5.0f);

    /**
     * The fraction of your ammo that you retain when you die (out of 1)
     */
    public float fractionOfAmmoRetainedOnDeath;

    /**
     * The amount the damage the player takes is multiplied by
     */
    public float damageTakenMultiplier;

    /**
     * The amount the damage the player deals is multiplied by
     */
    public float damageDealtMultiplier;

    /**
     * Constructor
     *
     * @param fractionOfAmmoRetainedOnDeath The fraction of your ammo that you retain when you die (out of 1)
     * @param damageTakenMultiplier         The amount the damage the player takes is multiplied by
     * @param damageDealtMultiplier         The amount the damage the player deals is multiplied by
     */
    Difficulty(float fractionOfAmmoRetainedOnDeath, float damageTakenMultiplier, float damageDealtMultiplier) {
        this.fractionOfAmmoRetainedOnDeath = fractionOfAmmoRetainedOnDeath;
        this.damageTakenMultiplier = damageTakenMultiplier;
        this.damageDealtMultiplier = damageDealtMultiplier;
    }

    /**
     * Get the name of a button based on difficulty
     *
     * @return difficulty button name
     */
    public String getButtonName() {
        return this.toString().toLowerCase() + "DifficultyButton";
    }

    /**
     * Get a difficulty form a button name
     *
     * @param buttonName The name of the button
     * @return The difficulty
     */
    public static Difficulty getDifficultyFromButtonName(String buttonName) {
        for (Difficulty difficulty : Difficulty.values()) {
            if (difficulty.getButtonName().equalsIgnoreCase(buttonName)) {
                return difficulty;
            }
        }
        return null;
    }
}
