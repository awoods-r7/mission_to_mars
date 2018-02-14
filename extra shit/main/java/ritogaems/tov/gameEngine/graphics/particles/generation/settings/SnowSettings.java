package ritogaems.tov.gameEngine.graphics.particles.generation.settings;

import android.graphics.Bitmap;

import ritogaems.tov.gameEngine.AssetStore;
import ritogaems.tov.util.GraphicsUtil;

/**
 * @author Michael Purdy
 *         Settings for a snowy area
 */
public class SnowSettings extends ParticleSettings {

    /**
     * Type of snow
     * (At the moment - does it have a border or not)
     */
    public enum SnowType {
        DARK, LIGHT;

        private String getName() {
            return toString().toLowerCase();
        }

        // get a snow type enum from a string (used when reading in properties from file)
        public static SnowType getFromString(String snowTypeString) {
            for (SnowType snowType : SnowType.values())
                if (snowTypeString.equalsIgnoreCase(snowType.toString())) {
                    return snowType;
                }
            return null;
        }
    }

    /**
     * Constructor for snow settings (includes a bitmap area for multiple
     * snow flake types
     *
     * @param halfSpawnWidth  Half the width of spawn area (game units)
     * @param halfSpawnHeight Half the height of spawn area (game units)
     * @param assetStore      The asset store for the game
     */
    public SnowSettings(float halfSpawnWidth, float halfSpawnHeight, SnowType snowType, AssetStore assetStore) {
        super(
                halfSpawnWidth,                             // halfSpawnWidth
                halfSpawnHeight,                             // halfSpawnHeight
                GraphicsUtil.getCutAssetStoreBitmap(assetStore, snowType.getName() + "SnowFlakes",
                        assetStore.getBitmap(snowType.getName() + "snowFlakes",
                                "img/Particles/" + snowType.getName() + "SnowFlakes.png"), 3, 3, 1),        // textures
                false,      // additiveBlend;
                EmissionMode.Burst,         // emissionMode
                0.2f,       // minBurstTime
                0.3f,       // maxBurstTime
                AccelerationMode.Aligned,   // accelerationMode;
                0.0f,       // minAccelerationDirection;
                0.0f,       // maxAccelerationDirection;
                0.0f,       // minAccelerationMagnitude;
                0.0f,       // maxAccelerationMagnitude;
                0.0f,       // gravityX;
                3.0f,       // gravityY;
                (int) ((halfSpawnWidth + halfSpawnHeight) / 20.0f),         // minNumParticles;
                (int) ((halfSpawnWidth + halfSpawnHeight) / 10.0f) + 2,     // maxNumParticles;
                0,          // minInitialSpeed;
                0,          // maxInitialSpeed;
                7.5f,       // rotateX
                27.0f,      // rotateY
                0.0f,       // minAngularVelocity;
                0.0f,       // maxAngularVelocity;
                -30,        // minOrientationAngle;
                30,         // maxOrientationAngle;
                0.7f,       // minLifespan;
                2.0f,       // maxLifespan;
                0.15f,      // minSize;
                0.3f,       // maxSize;
                0.1f        // minScaleGrowth
        );
    }
}
