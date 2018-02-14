package ritogaems.tov.gameEngine.graphics.particles.generation.settings;

import android.graphics.Bitmap;

import ritogaems.tov.gameEngine.AssetStore;

/**
 * @author Michael Purdy
 *         Adapted from gage (no major changes)
 */
public class SmokeSettings extends ParticleSettings {

    /**
     * Constructor with a spawn area
     *
     * @param halfSpawnWidth  Half the width of the area in which particles can spawn
     * @param halfSpawnHeight Half the height of the area in which particles can spawn
     * @param assetStore      The asset store for the game
     */
    public SmokeSettings(float halfSpawnWidth, float halfSpawnHeight, AssetStore assetStore) {
        super(
                halfSpawnWidth,             // halfSpawnWidth
                halfSpawnHeight,            // halfSpawnHeight
                new Bitmap[]{assetStore.getBitmap("smoke",
                        "img/Particles/Smoke.png")},  // textureFilename;
                false,                      // additiveBlend;
                EmissionMode.Continuous,    // emissionMode
                0.2f,                       // minBurstTime
                0.3f,                       // maxBurstTime
                AccelerationMode.Aligned,   // accelerationMode;
                0.0f,                       // minAccelerationDirection;
                0.0f,                       // maxAccelerationDirection;
                0.0f,                       // minAccelerationMagnitude;
                0.0f,                       // maxAccelerationMagnitude;
                0.0f,                       // gravityX;
                0.0f,                       // gravityY;
                2 + (int) ((halfSpawnWidth * 0.5) + (halfSpawnWidth * 0.5)),    // minNumParticles;
                2 + (int) ((halfSpawnWidth * 0.7) + (halfSpawnWidth * 0.7)),    // maxNumParticles;
                0,                          // minInitialSpeed;
                0,                          // maxInitialSpeed;
                7.5f,                       // rotateX
                27.0f,                      // rotateY
                0.0f,                       // minAngularVelocity;
                0.0f,                       // maxAngularVelocity;
                -30,                        // minOrientationAngle;
                30,                         // maxOrientationAngle;
                0.7f,                       // minLifespan;
                2.0f,                       // maxLifespan;
                0.05f,                      // minSize;
                0.1f,                       // maxSize;
                0.1f                        // minScaleGrowth
        );
    }
}
