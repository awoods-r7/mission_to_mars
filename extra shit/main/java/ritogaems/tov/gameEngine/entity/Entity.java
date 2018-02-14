package ritogaems.tov.gameEngine.entity;

import ritogaems.tov.gameEngine.Difficulty;
import ritogaems.tov.gameEngine.graphics.animation.Animation;
import ritogaems.tov.gameEngine.graphics.animation.DirectedAnimation;
import ritogaems.tov.gameEngine.graphics.animation.Sprite;
import ritogaems.tov.gameEngine.graphics.particles.animation.BloodSplatter;
import ritogaems.tov.util.CollisionDetector;
import ritogaems.tov.util.Vector2;
import ritogaems.tov.world.GameObject;
import ritogaems.tov.world.screens.MapScreen;

public abstract class Entity extends Sprite {

    // /////////////////////////////
    // Properties
    // /////////////////////////////

    protected MapScreen mapScreen;

    /**
     * States for an Entity, which defines it's behaviour in-game
     */
    public enum entityState {
        IDLE, MOVING, ATTACKING, FLINCHING, ROAMING, CHASING, DEAD, FLEEING, SHOOTING, THROWING
    }

    /**
     * Reusable Animations with which to set the Bitmap for the current frame
     */
    protected DirectedAnimation movementAnimation;
    DirectedAnimation attackAnimation;
    Animation deathAnimation;

    /**
     * Current direction and state of the Entity
     */
    public Direction direction = Direction.DOWN;
    public entityState state = entityState.IDLE;

    /**
     * information
     */
    protected int currentHealth;
    private int maxHealth;
    public int damage;
    protected float moveSpeed;

    /**
     * Variable for FLINCHING behaviour
     */
    protected int flinchTimer = 0;
    protected int invincibilityTimer = 0;
    public Vector2 bounceBack = new Vector2(0, 0);

    // /////////////////////////////
    // Constructors
    // /////////////////////////////

    /**
     * Creates a new entity
     *
     * @param xPos            X position
     * @param yPos            Y position
     * @param drawWidth       Width of the draw box
     * @param drawHeight      Height of the draw box
     * @param collisionWidth  Width of the collision box
     * @param collisionHeight Height of the collision box
     * @param currentHealth   Initial value for current and maximum health
     * @param damage          Damage the entity deals
     * @param mapScreen       Map screen where this object resides
     *                        NOTE: Entities can only reside in Map Screens
     */
    protected Entity(float xPos, float yPos, float drawWidth, float drawHeight,
                     float collisionWidth, float collisionHeight,
                     int currentHealth, int maxHealth, int damage, MapScreen mapScreen) {
        super(xPos, yPos, drawWidth, drawHeight, collisionWidth, collisionHeight);
        this.currentHealth = currentHealth;
        this.maxHealth = maxHealth;
        this.damage = damage;
        this.mapScreen = mapScreen;
    }

    // /////////////////////////////
    // Methods
    // /////////////////////////////

    /**
     * If entity is in state flinching, set move to vector bounceBack untill timer is finished
     *
     * @author Zoey Longridge
     */
    protected void flinch() {
        move(bounceBack);
        if (flinchTimer > 10) {
            state = entityState.IDLE;
            flinchTimer = 0;
        } else {
            flinchTimer++;
        }
    }

    /**
     * Decrease currentHealth
     *
     * @param damage The base damage
     * @author Kevin Martin
     */
    public void takeDamage(int damage) {

        // Michael - added particle effect
        mapScreen.addParticleEffect(new BloodSplatter(position.x, position.y, 10, 10, mapScreen.getGame().getAssetStore()));

        currentHealth -= damage;
        invincibilityTimer = 20;
        if (currentHealth < 0) {
            currentHealth = 0;
        }
    }

    /**
     * increase currentHealth
     *
     * @param health
     * @author Kevin Martin
     */
    public void restoreHealth(int health) {
        currentHealth += health;
        if (currentHealth > maxHealth) {
            currentHealth = maxHealth;
        }
    }

    /**
     * Used to move the entity
     *
     * @param vector
     */
    protected abstract void move(Vector2 vector);

    /**
     * If this entity collides with a tile, then resolve the collision with the entity moving back
     *
     * @return collisionFound
     * @author Zoey Longridge
     */
    protected boolean checkTileCollision() {
        Boolean collisionFound = false;
        for (GameObject object : mapScreen.tileMap.getCollidableTiles().values()) {
            CollisionDetector.CollisionType collisionType = CollisionDetector.determineAndResolveCollision(this, object, true);

            if (collisionType != CollisionDetector.CollisionType.None) {
                collisionFound = true;
            }
        }
        return collisionFound;
    }

    /**
     * Getter
     *
     * @return currentHealth
     */
    public int getCurrentHealth() {
        return currentHealth;
    }

    /**
     * Getter
     *
     * @return maxHealth
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Getter
     *
     * @return damage
     */
    int getDamage() {
        return damage;
    }

    /**
     * Setter
     *
     * @param mapScreen
     */
    public void switchScreen(MapScreen mapScreen) {
        this.mapScreen = mapScreen;
    }

    /**
     * Setter
     *
     * @return
     */
    public Difficulty getDifficulty() {
        return mapScreen.getGame().difficulty;
    }

}