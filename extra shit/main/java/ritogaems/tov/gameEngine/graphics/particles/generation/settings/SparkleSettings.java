package ritogaems.tov.gameEngine.graphics.particles.generation.settings;

import android.graphics.Bitmap;

import ritogaems.tov.gameEngine.AssetStore;

/**
 * @author Michael Purdy
 *         The settings for a fire particle animation
 *         Multicoloured particle animations could be created by having a sheet of different
 *         colour particles (like snow flakes)
 */
public class SparkleSettings extends ParticleSettings {

    /**
     * Enumeration of possible colour types
     */
    public enum SparkleColour {
        GREEN,
        PURPLE,
        RED,
        WHITE;

        // get a colour enum from a string (used when reading in properties from file)
        public static SparkleColour getFromString(String colourString) {
            for (SparkleColour colour : SparkleColour.values())
                if (colourString.equalsIgnoreCase(colour.toString())) {
                    return colour;
                }
            return null;
        }

        public String getName() {
            return toString().toLowerCase();
        }
    }

    /**
     * Constructor that creates a new particle settings for a sparkle emitter
     *
     * @param colour          The colour of the sparkles
     * @param halfSpawnWidth  Half the width of spawn area (game units)
     * @param halfSpawnHeight Half the width of spawn area (game units)
     * @param assetStore      The asset store of the game
     */
    public SparkleSettings(SparkleColour colour, float halfSpawnWidth, float halfSpawnHeight, AssetStore assetStore) {
        super(
                halfSpawnWidth,                 // halfSpawnWidth
                halfSpawnHeight,                // halfSpawnHeight
                new Bitmap[]{assetStore.getBitmap(colour.getName() + "Sparkle",
                        "img/Particles/" + colour.getName() + "Sparkle.png")},   // textureFilename;
                false,                          // additiveBlend;
                EmissionMode.Burst,             // emissionMode
                0.2f,                           // minBurstTime
                0.3f,                           // maxBurstTime
                AccelerationMode.Aligned,       // accelerationMode;
                0.0f,                           // minAccelerationDirection;
                0.0f,                           // maxAccelerationDirection;
                0.0f,                           // minAccelerationMagnitude;
                0.0f,                           // maxAccelerationMagnitude;
                0.0f,                           // gravityX;
                0.0f,                           // gravityY;
                0,                              // minNumParticles;
                (int) ((halfSpawnWidth + halfSpawnHeight) / 5) + 2,   // maxNumParticles;
                0,                              // minInitialSpeed;
                0,                              // maxInitialSpeed;
                7.5f,                           // rotateX
                27.0f,                          // rotateY
                0.0f,                           // minAngularVelocity;
                0.0f,                           // maxAngularVelocity;
                -30,                            // minOrientationAngle;
                30,                             // maxOrientationAngle;
                0.7f,                           // minLifespan;
                2.0f,                           // maxLifespan;
                0.2f,                           // minSize;
                0.8f,                           // maxSize;
                0.1f                            // minScaleGrowth
        );
    }
}
