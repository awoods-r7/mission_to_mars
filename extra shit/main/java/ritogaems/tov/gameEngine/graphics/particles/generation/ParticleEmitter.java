package ritogaems.tov.gameEngine.graphics.particles.generation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import ritogaems.tov.gameEngine.ElapsedTime;
import ritogaems.tov.gameEngine.graphics.particles.IParticleEffect;
import ritogaems.tov.gameEngine.graphics.particles.generation.settings.ParticleSettings;
import ritogaems.tov.util.GraphicsUtil;
import ritogaems.tov.util.Vector2;
import ritogaems.tov.world.viewports.LayerViewport;
import ritogaems.tov.world.viewports.ScreenViewport;

/**
 * @author Michael Purdy
 *         Emitter for a single particle system
 *         <p/>
 *         Adapted from demo
 *         - Added pickRandomInt
 *         - Added textures array to allow a random texture from a selection
 *         to be chosen for each particle
 *         - Fixed rotation code (demo didn't implement fully)
 *         - Added synchronisation for update and draw to be on different threads
 *         - Added check for if the particle is in the screen
 */
public class ParticleEmitter implements IParticleEffect {

    // /////////////////////////////////////////////////////////////////////////
    // Properties:
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Static random instance used by all particle effects to configure their particles
     */
    public static Random random = new Random();

    /**
     * position of the particle effect
     */
    private Vector2 effectLocation;

    /**
     * Return a random between between the specified min and max
     *
     * @param min Minimum value
     * @param max Maximum value
     * @return Value in the specified range
     */
    public static float randomBetween(float min, float max) {
        return min + random.nextFloat() * (max - min);
    }

    /**
     * Choose a random integer
     *
     * @param min Minimum value (inclusive)
     * @param max Maximum value (inclusive)
     * @return Value in the specificed range
     */
    public static int randomIntBetween(int min, int max) {
        return min + random.nextInt(max - min);
    }

    /**
     * Return a random direction between the specified min and max
     *
     * @param min          Minimum value
     * @param max          Maximum value
     * @param outputVector Vector within which the direction will be stored
     */
    private static void pickRandomDirection(float min, float max, Vector2 outputVector) {
        float angle = randomBetween(min, max);
        // our settings angles are in degrees, so we must convert to radians
        angle = (float) Math.toRadians(angle);
        outputVector.set((float) Math.cos(angle), (float) Math.sin(angle));
    }

    /**
     * Context to which this particle emitter belongs
     */
    private Context context;

    /**
     * Bitmap array used by this particle system
     */
    private Bitmap[] textures;

    /**
     * Boolean indicating if this particle system is in existence
     */
    private boolean alive;

    /**
     * Center point of the bitmap (used as a reference point when rotating the bitmap or
     * when drawing the bitmap centered on the particle location)
     */
    private Vector2 textureCenter;

    /**
     * Paint instance used when drawing the particle
     */
    private Paint paint;

    /**
     * Amount of time before the next batch of particles needs to be created
     */
    private double timeToBurst = 0.0;

    /**
     * Previous location at which the last batch of particles was created
     */
    private Vector2 lastLocation = new Vector2();

    /**
     * Linked list of active particles currently evolving
     */
    private final LinkedList<Particle> activeParticles;

    /**
     * Linked list of available inactive particles that an be used if needed
     */
    private LinkedList<Particle> freeParticles;

    /**
     * Settings that are used to drive the particle system
     */
    private ParticleSettings particleSettings;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create a new particle emitter using the specified settings
     *
     * @param context          Context to which this particle system belongs
     * @param particleSettings Settings used to drive this emitter
     */
    public ParticleEmitter(Context context, ParticleSettings particleSettings, Vector2 position) {
        this.context = context;
        this.particleSettings = particleSettings;
        alive = true;

        this.effectLocation = new Vector2();
        this.effectLocation.x = position.x;
        this.effectLocation.y = position.y;

        activeParticles = new LinkedList<>();
        freeParticles = new LinkedList<>();

        // Create an initial pool of inactive particles that can be used
        int initialSize = 100;
        for (int i = 0; i < initialSize; i++) {
            Particle particle = new Particle();
            freeParticles.add(particle);
        }

        // Based on the specified settings, configure the emitter
        configure();
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods: Configuration
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Change the particle settings used by this emitter
     *
     * @param particleSettings New particle settings to use
     */
    public void setParticleSettings(ParticleSettings particleSettings) {
        // Store the settings
        this.particleSettings = particleSettings;

        // Release all current active particles
        Iterator<Particle> iterator = activeParticles.iterator();
        while (iterator.hasNext()) {
            Particle particle = iterator.next();
            iterator.remove();
            freeParticles.add(particle);
        }

        // Configure the emitter based on the specified settings
        configure();
    }

    /**
     * Configure the emitter based on the specified settings
     */
    private void configure() {

        // load the Bitmap to be used (picks random from list of bitmaps)
        this.textures = particleSettings.textures;

        // Store the center points (to rotate, to offset when drawing)
        textureCenter = new Vector2(
                textures[0].getWidth() / 2.0f, textures[0].getHeight() / 2.0f);

        // Change the alpha blending (normal or additive) as specified in the settings
        paint = new Paint();
        paint.setAntiAlias(true);
        if (particleSettings.additiveBlend)
            paint.setXfermode(new PorterDuffXfermode(Mode.ADD));
    }

    /**
     * Vector2 object reused when adding particles
     */
    private Vector2 mParticlePosition = new Vector2();
    private Vector2 mParticleOffset = new Vector2();

    /**
     * Add a new batch of particles at the specified location (or spaced between
     * the last and current location, as appropriate depending upon the emitter settings).
     *
     * @param location     Current location
     * @param lastLocation Last location
     */
    private void addParticles(Vector2 location, Vector2 lastLocation) {

        // Determine the number of particles to be added
        int numParticles = randomIntBetween(
                particleSettings.minNumParticles, particleSettings.maxNumParticles);

        // Setup the location and offset depending upon the burst mode
        switch (particleSettings.emissionMode) {
            case Burst:
                mParticlePosition.set(location);
                mParticleOffset.set(Vector2.Zero);
                break;
            case Continuous:
                mParticlePosition.set(lastLocation);
                mParticleOffset.set((location.x - lastLocation.x) / numParticles,
                        (location.y - lastLocation.y) / numParticles);
                break;
        }

        synchronized (activeParticles) {
            // Initialise and add the particles
            for (int i = 0; i < numParticles; i++) {

                Particle p;
                if (freeParticles.size() == 0) {
                    p = new Particle();
                } else {
                    p = freeParticles.removeLast();
                }

                initialiseParticle(p, effectLocation);
                activeParticles.add(p);

                mParticlePosition.x += mParticleOffset.x;
                mParticlePosition.y += mParticleOffset.y;
            }
        }
    }

    /**
     * Vector2 objects used when initialising particles
     */
    private Vector2 mDirection = new Vector2();
    private Vector2 mVelocity = new Vector2();
    private Vector2 mAcceleration = new Vector2();

    /**
     * Initialise the particle
     *
     * @param particle Particle to initialise
     * @param position Location of the particle
     */
    private void initialiseParticle(Particle particle, Vector2 position) {

        // determine the particle's initial position
        float randX = position.x + randomBetween(-particleSettings.halfSpawnWidth, particleSettings.halfSpawnWidth);
        float randY = position.y + randomBetween(-particleSettings.halfSpawnHeight, particleSettings.halfSpawnHeight);

        mParticlePosition.x = randX;
        mParticlePosition.y = randY;

        // Determine the orientation and speed
        pickRandomDirection(
                particleSettings.minOrientationAngle,
                particleSettings.maxOrientationAngle, mDirection);
        float speed = randomBetween(particleSettings.minInitialSpeed,
                particleSettings.maxInitialSpeed);

        // Define the velocity
        mVelocity.x = mDirection.x * speed;
        mVelocity.y = mDirection.y * speed;

        // Define the life span
        float lifeSpan = randomBetween(
                particleSettings.minLifespan, particleSettings.maxLifespan);

        // Determine the orientation and angular velocity
        float orientation = randomBetween(particleSettings.minOrientationAngle,
                particleSettings.maxOrientationAngle);
        float angularVelocity = randomBetween(
                particleSettings.minAngularVelocity,
                particleSettings.maxAngularVelocity);

        // Determine the scale and scale growth
        float size = Math.max(textures[0].getHeight(), textures[0].getWidth());
        float scale = randomBetween(particleSettings.minScale,
                particleSettings.maxScale);
        float scaleGrowth = randomBetween(
                particleSettings.minScaleGrowth,
                particleSettings.minScaleGrowth);

        // Define the particle acceleration
        switch (particleSettings.accelerationMode) {
            case Aligned:
                // Randomly pick an acceleration using the direction and
                // the minAcceleration/maxAcceleration values
                float accelerationMagnitude = randomBetween(
                        particleSettings.minAccelerationMagnitude,
                        particleSettings.maxAccelerationMagnitude);
                mAcceleration.x = mDirection.x * accelerationMagnitude;
                mAcceleration.y = mDirection.y * accelerationMagnitude;
                break;
            case NonAligned:
                // Select an acceleration in a random direction and magnitude
                // using the defined min and max values
                pickRandomDirection(
                        particleSettings.minAccelerationDirection,
                        particleSettings.maxAccelerationDirection, mAcceleration);
                accelerationMagnitude = randomBetween(
                        particleSettings.minAccelerationMagnitude,
                        particleSettings.maxAccelerationMagnitude);
                mAcceleration.x = mDirection.x * accelerationMagnitude;
                mAcceleration.y = mDirection.y * accelerationMagnitude;
                break;
            default:
                break;
        }

        // Initialise the particle
        particle.initialize(randomIntBetween(0, textures.length),mParticlePosition, mVelocity, mAcceleration, orientation, particleSettings.rotatePoint,
                angularVelocity, textures[0].getWidth(), textures[0].getHeight(), scale, scaleGrowth, lifeSpan);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods: Update and Draw
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Update the emitter, creating a new burst of particles if needed and
     * updating all active particles, removing/storing those that have
     * become inactive.
     *
     * @param elapsedTime Elapsed time
     */
    public void update(ElapsedTime elapsedTime) {

        createParticles(elapsedTime);

        synchronized (activeParticles) {
            // Update all active particles and remove/store those that have become inactive
            Iterator<Particle> iterator = activeParticles.iterator();
            while (iterator.hasNext()) {
                Particle particle = iterator.next();

                // Add in gravity, if required
                particle.velocity.add(
                        particleSettings.gravityX,
                        particleSettings.gravityY);

                // Update the particle
                particle.update(elapsedTime.stepTime);

                // Remove and store if inactive
                if (!particle.isAlive()) {
                    iterator.remove();
                    freeParticles.add(particle);
                }
            }
        }
    }

    private void createParticles(ElapsedTime elapsedTime) {
        // Check and create a new burst of particles if needed
        if (timeToBurst > 0.0)
            timeToBurst -= elapsedTime.stepTime;
        else {
            timeToBurst = randomBetween(particleSettings.minBurstTime,
                    particleSettings.maxBurstTime);
            addParticles(effectLocation, lastLocation);
            lastLocation.set(effectLocation);
        }
    }

    /**
     * Matrix used to draw particles
     */
    private Matrix mMatrix = new Matrix();

    /**
     * Draw all active particles
     *
     * @param canvas Canvas on which to draw to
     */
    public void draw(Canvas canvas, LayerViewport layerViewport, ScreenViewport screenViewport) {

        synchronized (activeParticles) {
            for (Particle p : activeParticles) {
                // Avoid having particles pop in and out by using a fade in at
                // the start of the life span and a fade out at the end.
                // An alpha of 100% occurs mid-span and then fades out

                if (GraphicsUtil.getScreenRect(p, layerViewport, screenViewport)) {

                    float normalizedLifetime = p.timeSinceBirth / p.lifeSpan;
                    float alpha = 4.0f * normalizedLifetime * (1 - normalizedLifetime);
                    paint.setAlpha((int) (alpha * 255));

                    mMatrix.reset();
                    mMatrix.setScale(p.scale, p.scale);
                    mMatrix.preRotate(p.orientation, p.rotate.x, p.rotate.y);
                    mMatrix.postTranslate(GraphicsUtil.drawScreenRect.exactCenterX(), GraphicsUtil.drawScreenRect.exactCenterY());

                    canvas.drawBitmap(textures[p.textureId], mMatrix, paint);
                }

            }
        }
    }

    public int getNumberOfParticles() {
        return activeParticles.size();
    }

    public boolean isAlive() {
        return alive;
    }
}