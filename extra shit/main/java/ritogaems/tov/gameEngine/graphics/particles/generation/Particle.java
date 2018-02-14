package ritogaems.tov.gameEngine.graphics.particles.generation;

import android.graphics.Canvas;

import ritogaems.tov.util.Vector2;
import ritogaems.tov.world.GameObject;
import ritogaems.tov.world.viewports.LayerViewport;
import ritogaems.tov.world.viewports.ScreenViewport;

/**
 * @author Michael Purdy
 *         Single particle instance
 *
 *         Taken from gage
 *         Extended Game Object
 *         Added collision updating
 */
public class Particle extends GameObject {

    // /////////////////////////////////////////////////////////////////////////
    // Properties: [[Declared public for speed of access]]
    // /////////////////////////////////////////////////////////////////////////

    /**
     * The id of the texture bitmap
     */
    public int textureId;

    /**
     * Velocity of the particle
     */
    public Vector2 velocity = new Vector2();

    /**
     * Acceleration of the particle
     */
    private Vector2 acceleration = new Vector2();

    /**
     * Orientation of the particle
     */
    public float orientation;

    /**
     * Angular velocity of the particle (assumed to be in degrees/second)
     */
    private float angularVelocity;

    /**
     * Size of bounding box before scaling
     */
    private float width;
    private float height;

    /**
     * Scaling factor to applied to the particle
     */
    public float scale;

    /**
     * Growth factor determining how the scale changes over time
     */
    private float scaleGrowth;

    /**
     * Length of time this particle will remain alive
     */
    public float lifeSpan;

    /**
     * Length of time since the birth of this particle
     */
    public float timeSinceBirth;
    public Vector2 rotate = new Vector2();

    public Particle() {
        super();
    }

    @Override
    public void draw(Canvas canvas, LayerViewport layerViewport, ScreenViewport screenViewport) {
        // drawn from particle emitter
    }


    // /////////////////////////////////////////////////////////////////////////
    // Methods:
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Determine if this particle is still alive
     *
     * @return Boolean true if alive, otherwise false
     */
    public boolean isAlive() {
        return timeSinceBirth < lifeSpan;
    }

    /**
     * Initialise the particle using the specified values
     *
     * @param position        Position
     * @param velocity        Velocity
     * @param acceleration    Acceleration
     * @param orientation     Orientation
     * @param angularVelocity Angular velocity
     * @param scale           Scale
     * @param scaleGrowth     Scale growth
     * @param lifeSpan        Life span
     */
    public void initialize(int textureId, Vector2 position, Vector2 velocity,
                           Vector2 acceleration, float orientation, Vector2 rotatePoint, float angularVelocity,
                           float width, float height, float scale, float scaleGrowth, float lifeSpan) {

        this.textureId = textureId;

        this.position.x = position.x;
        this.position.y = position.y;

        this.velocity.x = velocity.x;
        this.velocity.y = velocity.y;

        this.acceleration.x = acceleration.x;
        this.acceleration.y = acceleration.y;

        this.rotate.x = rotatePoint.x;
        this.rotate.y = rotatePoint.y;

        this.orientation = orientation;
        this.angularVelocity = angularVelocity;


        this.width = width;
        this.height = height;
        this.scale = scale;
        this.scaleGrowth = scaleGrowth;

        this.lifeSpan = lifeSpan;
        this.timeSinceBirth = 0.0f;

        updateBounds();
    }

    /**
     * Evolve the particle
     *
     * @param dt Amount of time elapsed (in seconds) from the last update call
     */
    public void update(double dt) {

        velocity.x += acceleration.x * dt;
        velocity.y += acceleration.y * dt;

        position.x += velocity.x * dt;
        position.y += velocity.y * dt;

        orientation += angularVelocity * dt;

        scale += scaleGrowth * dt;

        timeSinceBirth += dt;

        updateBounds();
    }

    /**
     * update the collision box of the particle (if you needed to check collision with a particle)
     */
    private void updateBounds() {
        this.setCollisionBounds(position.x, position.y, width * scale, height * scale);
    }
}