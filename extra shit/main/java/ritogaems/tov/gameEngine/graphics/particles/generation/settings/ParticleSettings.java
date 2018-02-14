package ritogaems.tov.gameEngine.graphics.particles.generation.settings;

import android.graphics.Bitmap;

import ritogaems.tov.util.Vector2;

/**
 * @author Michael Purdy
 *         Particle settings - adapted from gage
 *         Added spawn area
 *         Changed texture to array of textures to allow one from a selection to be chosen
 */
public class ParticleSettings {

    /**
     * The mode of the acceleration
     */
    public enum AccelerationMode {
        Aligned, NonAligned
    }

    /**
     * How the particles are emmitted
     */
    public enum EmissionMode {
        Burst, Continuous
    }

    // /////////////////////////////////////////////////////////////////////////
    // Particle Settings
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Texture to be used when drawing this particle system
     */
    public final Bitmap[] textures;

    /**
     * Define if particles are to be combined using normal alpha blending or
     * additive blending
     */
    public final boolean additiveBlend;

    /**
     * Define how many particles are emitted - are they emitted in a continuous
     * manner over time, or in a sudden burst.
     */
    public final EmissionMode emissionMode;

    /**
     * Min and max time between bursts of particles
     */
    public final float minBurstTime;
    public final float maxBurstTime;

    /**
     * Min and max number of particles
     */
    public final int minNumParticles;
    public final int maxNumParticles;

    /**
     * Area in which the particles can spawn in
     */
    public final float halfSpawnWidth;
    public final float halfSpawnHeight;

    /**
     * Define how the particles are subject to acceleration. If the acceleration
     * mode is aligned, then particle accelerate in the same direction as their
     * velocity. If non aligned then the acceleration direction is randomly
     * selected within the define extents. A constant x and y gravitational
     * acceleration can be defined.
     */
    public final AccelerationMode accelerationMode;

    public final float minAccelerationDirection;
    public final float maxAccelerationDirection;

    public final float minAccelerationMagnitude;
    public final float maxAccelerationMagnitude;

    public final float gravityX;
    public final float gravityY;

    /**
     * Define the linear and rotational speed of the particle, alongside its
     * starting direction.
     */
    public final float minInitialSpeed;
    public final float maxInitialSpeed;

    public final Vector2 rotatePoint;

    public final float minAngularVelocity;
    public final float maxAngularVelocity;

    public final float minOrientationAngle;
    public final float maxOrientationAngle;

    /**
     * Define how long each particle will live
     */
    public final float minLifespan;
    public final float maxLifespan;

    /**
     * Define how each particle will scale over time.
     */
    public final float minScale;
    public final float maxScale;

    public final float minScaleGrowth;
    private final float maxScaleGrowth;

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create a new set of particle settings
     *
     * @param halfSpawnWidth           Half the width of spawn area (game units)
     * @param halfSpawnHeight          Half the height of spawn area (game units)
     * @param textures                 The possible particle textures
     * @param additiveBlend            Use additive blending
     * @param emissionMode             How are the particle emitted
     * @param minBurstTime             Minimum time between bursts
     * @param maxBurstTime             Maximum time between bursts
     * @param accelerationMode         Acceleration mode for the particles
     * @param minAccelerationDirection Min acceleration direction
     * @param maxAccelerationDirection Max acceleration direction
     * @param minAccelerationMagnitude Min acceleration magnitude
     * @param maxAccelerationMagnitude Max acceleration magnitude
     * @param gravityX                 Constant x gravity value
     * @param gravityY                 Constant y gravity value
     * @param minNumParticles          Minimum number of particles per burst/period
     * @param maxNumParticles          Maximum number of particles per burst/period
     * @param minInitialSpeed          Min initial speed of each particle
     * @param maxInitialSpeed          Max initial speed of each particle
     * @param rotateX                  The X co-ordinate of the rotation point
     * @param rotateY                  The Y co-ordinate of the rotation point
     * @param minAngularVelocity       Min initial rotational speed of each particle
     * @param maxAngularVelocity       Max initial rotational speed of each particle
     * @param minOrientationAngle      Min initial direction of each particle
     * @param maxOrientationAngle      Max initial direction of each particle
     * @param minLifespan              Min lifespan for each particle
     * @param maxLifespan              Max lifespan for each particle
     * @param minScale                 Min scale
     * @param maxScale                 Max scale
     * @param minScaleGrowth           Min growth factor applied to the scale value
     */
    ParticleSettings(float halfSpawnWidth, float halfSpawnHeight, Bitmap[] textures, boolean additiveBlend,
                     EmissionMode emissionMode, float minBurstTime, float maxBurstTime,
                     AccelerationMode accelerationMode, float minAccelerationDirection,
                     float maxAccelerationDirection, float minAccelerationMagnitude,
                     float maxAccelerationMagnitude, float gravityX, float gravityY,
                     int minNumParticles, int maxNumParticles, float minInitialSpeed,
                     float maxInitialSpeed, float rotateX, float rotateY, float minAngularVelocity,
                     float maxAngularVelocity, float minOrientationAngle,
                     float maxOrientationAngle, float minLifespan, float maxLifespan,
                     float minScale, float maxScale, float minScaleGrowth) {

        // Store the passed parameters

        this.halfSpawnWidth = halfSpawnWidth;
        this.halfSpawnHeight = halfSpawnHeight;

        this.textures = textures;

        this.additiveBlend = additiveBlend;
        this.emissionMode = emissionMode;

        this.minBurstTime = minBurstTime;
        this.maxBurstTime = maxBurstTime;

        this.accelerationMode = accelerationMode;

        this.minAccelerationDirection = minAccelerationDirection;
        this.maxAccelerationDirection = maxAccelerationDirection;
        this.minAccelerationMagnitude = minAccelerationMagnitude;
        this.maxAccelerationMagnitude = maxAccelerationMagnitude;

        this.gravityX = gravityX;
        this.gravityY = gravityY;

        this.rotatePoint = new Vector2(rotateX, rotateY);

        this.minNumParticles = minNumParticles;
        this.maxNumParticles = maxNumParticles;

        this.minInitialSpeed = minInitialSpeed;
        this.maxInitialSpeed = maxInitialSpeed;

        this.minAngularVelocity = minAngularVelocity;
        this.maxAngularVelocity = maxAngularVelocity;

        this.minOrientationAngle = minOrientationAngle;
        this.maxOrientationAngle = maxOrientationAngle;

        this.minLifespan = minLifespan;
        this.maxLifespan = maxLifespan;

        this.minScale = minScale;
        this.maxScale = maxScale;

        this.minScaleGrowth = minScaleGrowth;
        this.maxScaleGrowth = minScaleGrowth; // possible error here but not used so left alone
    }
}
