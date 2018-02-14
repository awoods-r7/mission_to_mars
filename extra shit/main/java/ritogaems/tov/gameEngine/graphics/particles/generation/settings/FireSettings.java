package ritogaems.tov.gameEngine.graphics.particles.generation.settings;

import android.graphics.Bitmap;

import ritogaems.tov.gameEngine.AssetStore;

/**
 * @author Michael Purdy
 *         The settings for a fire particle animation
 */
public class FireSettings extends ParticleSettings {

    /**
     * Constructor that creates a new particle settings for a fire
     *
     * @param halfSpawnWidth  Half the width of spawn area (game units)
     * @param halfSpawnHeight Half the height of spawn area (game units)
     * @param assetStore      The asset store for the game
     */
    public FireSettings(float halfSpawnWidth, float halfSpawnHeight, AssetStore assetStore) {
        super(
                halfSpawnWidth,                         // halfSpawnWidth
                halfSpawnHeight,                        // halfSpawnHeight
                new Bitmap[]{assetStore.getBitmap("fire",
                        "img/Particles/fire.png")},     // textureFilename;
                false,                                  // additiveBlend;
                EmissionMode.Burst,                     // emissionMode
                0.2f,                                   // minBurstTime
                0.3f,                                   // maxBurstTime
                AccelerationMode.Aligned,               // accelerationMode;
                0.0f,                                   // minAccelerationDirection;
                0.0f,                                   // maxAccelerationDirection;
                0.0f,                                   // minAccelerationMagnitude;
                0.0f,                                   // maxAccelerationMagnitude;
                0.0f,                                   // gravityX;
                0.0f,                                   // gravityY;
                (int) ((halfSpawnWidth + halfSpawnHeight) / 3),         // minNumParticles;
                (int) ((halfSpawnWidth + halfSpawnHeight) / 1.5) + 2,   // maxNumParticles;
                0,      // minInitialSpeed;
                0,      // maxInitialSpeed;
                7.5f,   // rotateX
                27.0f,  // rotateY
                0.0f,   // minAngularVelocity;
                0.0f,   // maxAngularVelocity;
                -30,    // minOrientationAngle;
                30,     // maxOrientationAngle;
                0.7f,   // minLifespan;
                2.0f,   // maxLifespan;
                3.0f,   // minSize;
                4.0f,   // maxSize;
                0.1f    // minScaleGrowth
        );
    }
}
