package ritogaems.tov.gameEngine.entity;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.Random;

import ritogaems.tov.ai.AIUtil;
import ritogaems.tov.ai.Node;
import ritogaems.tov.gameEngine.audio.SoundEffect;
import ritogaems.tov.gameEngine.graphics.animation.Frame;
import ritogaems.tov.gameEngine.graphics.tilemapEngine.TileMap;
import ritogaems.tov.util.BoundingBox;
import ritogaems.tov.util.SteeringUtil;
import ritogaems.tov.util.Vector2;
import ritogaems.tov.world.screens.MapScreen;
import ritogaems.tov.world.viewports.LayerViewport;
import ritogaems.tov.world.viewports.ScreenViewport;

/**
 * @author Andrew Woods
 * @author Kevin Martin
 *         Andrew
 *         - Update Direction
 *         - Draw method
 *         Kevin
 *         - All others
 */
public abstract class Enemy extends Entity {

    // /////////////////////////////
    // Artificial Intelligence
    // /////////////////////////////

    /**
     * The tilemap for calculating roaming
     */
    private TileMap tileMap;

    /**
     * The path for the enemy to seek along
     */
    protected ArrayList<Vector2> path = new ArrayList<>();

    /**
     * The movement vector for the enemy position to be updated by
     */
    Vector2 movementVector = new Vector2();

    /**
     * The timer before picking a new roam target
     */
    protected int roamTimer;

    /**
     * The position the enemy paths towards
     */
    private Vector2 roamTarget;
    private ArrayList<Node> availableNodes = new ArrayList<>();
    Vector2 roamCenter; // A fixed point from which the roaming radius is taken
    float roamRadius;   // Radius of the circular roam range from the roam center

    // Variables for CHASING behaviour
    protected int chaseTimer;
    float chaseRadius; //Radius of the circular chase range, from the enemies current position

    // Variables for ATTACKING behaviour
    float attackRadius; //Radius of the circular attack range, from the enemies current position

    private SoundEffect hurtSound;

    // /////////////////////////////
    // Constructors
    // /////////////////////////////

    /**
     * Creates an enemy
     *
     * @param xPos            X position
     * @param yPos            Y position
     * @param drawWidth       Width of the draw box
     * @param drawHeight      Height of the draw box
     * @param collisionWidth  Width of the collision box
     * @param collisionHeight Height of the collision box
     * @param currentHealth   The current health of the enemy
     * @param maxHealth       The maximum health of the enemy
     * @param damage          Damage the enemy deals
     * @param tileMap         TileMap to calculate paths
     * @param mapScreen       Map screen where this object resides
     *                        NOTE: Entities can only reside in Map Screens
     */
    Enemy(float xPos, float yPos, float drawWidth, float drawHeight,
          float collisionWidth, float collisionHeight,
          int currentHealth, int maxHealth, int damage, TileMap tileMap, MapScreen mapScreen) {
        super(xPos, yPos, drawWidth, drawHeight, collisionWidth, collisionHeight, currentHealth, maxHealth, damage, mapScreen);

        roamCenter = new Vector2(xPos, yPos);
        this.tileMap = tileMap;

        hurtSound = mapScreen.getGame().getAssetStore().getSfx("enemy hit", "audio/sfx/EnemyHit.wav");

        direction = Direction.DOWN;

    }

    /**
     * Updates the enemy
     *
     * @param player Passed in to updateState()
     */
    public void update(Player player) {
        updateState(player);
        // check state and call appropriate method
        switch (state) {
            case IDLE:
                // possibly not used by enemy (or not all enemies)
                break;
            case MOVING:
                // not used by enemies (use roaming or chasing)
                break;
            case ATTACKING:
                // only used by certain enemies with specific attacks
                attack();
                break;
            case FLINCHING:
                // used to provide invincibility period after being hit
                flinch();
                break;
            case ROAMING:
                // when the enemy is waiting around it will roam randomly
                roam();
                break;
            case CHASING:
                // when the player is close enough the enemy will chase
                chase(player);
                break;
            case DEAD:
                // remove the enemy from the game
                death();
                break;
        }

        if (invincibilityTimer != 0) {
            invincibilityTimer--;
        }

        updateDirection();
        checkTileCollision();
    }

    /**
     * Updates the state of the enemy
     *
     * @param player The player, who's position will help determine the state
     */
    protected void updateState(Player player) {
        float distance = position.getDistance(player.position);
        if (getCurrentHealth() == 0 || state == entityState.DEAD) {
            state = entityState.DEAD;
        } else if (state != entityState.FLINCHING) {
            if (distance < chaseRadius) {
                if (distance < attackRadius) {
                    state = entityState.ATTACKING;
                } else {
                    if (state != entityState.CHASING) {
                        chaseTimer = 10;
                    }
                    state = entityState.CHASING;
                }
            } else {
                if (state != entityState.ROAMING) {
                    roamTimer = 270;
                    path.clear();
                }
                state = entityState.ROAMING;
            }
        }
    }

    /**
     * Update the direction the enemy is facing based on its current movement vector
     *
     * @author Andrew
     */
    private void updateDirection() {
        double angle = Math.atan2(movementVector.y, movementVector.x);

        double northEast = (-(Math.PI) / 4);
        double northWest = (-(3 * Math.PI) / 4);
        double southEast = ((Math.PI) / 4);
        double southWest = (4 * Math.PI) / 6;

        if (angle < northEast && angle >= northWest) {
            direction = Direction.UP;
        } else if (angle < southWest && angle >= southEast) {
            direction = Direction.DOWN;
        } else if (angle < southEast && angle >= northEast) {
            direction = Direction.RIGHT;
        } else {
            direction = Direction.LEFT;
        }
    }

    /**
     * Implements roaming AI
     * Called when state = ROAMING
     *
     * @author Kevin
     */
    private void roam() {
        if (availableNodes.isEmpty()) {
            availableNodes = AIUtil.generateRoamNodes(roamCenter, roamRadius, tileMap.getPathGrid());
        }

        if (roamTimer >= 300) {
            // pick a new node from the available options
            Random random = new Random();
            int x = random.nextInt(availableNodes.size() + 1);
            if (x == availableNodes.size()) {
                x--;
            }
            roamTarget = availableNodes.get(x).nodePos;
            path = AIUtil.FindPath(position, roamTarget, tileMap.getPathGrid());
            if (path != null) {
                roamTimer = 0;
            }
        } else {
            roamTimer++;
        }
        updateMovement();
    }

    /**
     * Implements chasing AI
     * Called when state = CHASING
     *
     * @param player The player to chase
     * @author Kevin
     */
    private void chase(Player player) {
        if (chaseTimer >= 15) {
            path = AIUtil.FindPath(position, player.position, tileMap.getPathGrid());
            chaseTimer = 0;
        } else {
            chaseTimer++;
        }
        updateMovement();
    }

    /**
     * Updates the vector for the enemy to move by
     *
     * @author Kevin
     */
    private void updateMovement() {
        if (!path.isEmpty()) {
            BoundingBox bound = getCollisionBox();
            if (bound.contains(path.get(0).x, path.get(0).y)) {
                path.remove(0);
                if (!path.isEmpty()) {
                    movementVector = SteeringUtil.seek(position, path.get(0), moveSpeed);
                } else {
                    movementVector.set(0, 0);
                }
            }
            updatePosition(movementVector.x, movementVector.y);
        } else {
            movementVector.set(0, 0);
        }
    }

    /**
     * Draws the enemy with the current frame
     *
     * @param canvas         Canvas on which to draw
     * @param layerViewport  Layer Viewport to draw from
     * @param screenViewport Screen Viewport to translate the Layer Viewport
     * @author Andrew
     */
    @Override
    public void draw(Canvas canvas, LayerViewport layerViewport, ScreenViewport screenViewport) {
        Frame currentFrame;
        switch (state) {
            case IDLE:
                currentFrame = movementAnimation.getFrame(Direction.CENTER);
                bitmap = currentFrame.getFrameBitmap();
                collisionBox = currentFrame.getFrameDrawBox();
                break;
            case MOVING:
                currentFrame = movementAnimation.getFrame(direction);
                bitmap = currentFrame.getFrameBitmap();
                collisionBox = currentFrame.getFrameDrawBox();
                break;
            case ROAMING:
                currentFrame = movementAnimation.getFrame(direction);
                bitmap = currentFrame.getFrameBitmap();
                collisionBox = currentFrame.getFrameDrawBox();
                break;
            case CHASING:
                currentFrame = movementAnimation.getFrame(direction);
                bitmap = currentFrame.getFrameBitmap();
                collisionBox = currentFrame.getFrameDrawBox();
                break;
            case ATTACKING:
                currentFrame = attackAnimation.getFrame(direction);
                bitmap = currentFrame.getFrameBitmap();
                collisionBox = currentFrame.getFrameDrawBox();
                break;
            case FLINCHING:
                long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
                if (elapsed / 100 % 2 == 0) {
                    return;
                }

                currentFrame = movementAnimation.getFrame(Direction.CENTER);
                bitmap = currentFrame.getFrameBitmap();
                collisionBox = currentFrame.getFrameDrawBox();

                break;

        }
        super.draw(canvas, layerViewport, screenViewport);

    }

    /**
     * Move the enemy to a new position
     *
     * @param vector the vector to update the current position by
     */
    public abstract void move(Vector2 vector);

    /**
     * The attack method - needs inherited, not used
     */
    public abstract void attack();

    /**
     * The death method - needs inherited, not used
     */
    public abstract void death();

    /**
     * The enemy takes damage by calling Entity.takeDamage
     *
     * @param damage the damage value to take
     */
    @Override
    public void takeDamage(int damage) {
        hurtSound.play(false);
        super.takeDamage((int) (damage * mapScreen.getGame().difficulty.damageDealtMultiplier));
    }

    /**
     * BELOW COMMENTED OUT CODE DRAWS QUICK CIRCLES AT THE TOP LEFT OF THE SCREEN
     * USED FOR ROUGH REPRESENTATION OF THE ENEMY PATH CALCULATIONS
     */

    //    Kevin circle path code
//    Paint paint = new Paint();
//    paint.setStyle(Paint.Style.STROKE);
//    paint.setStrokeWidth(2.0f);
//    paint.setColor(Color.RED);
//    canvas.drawCircle(position.x, position.y, roamRadius * 2, paint);
//    paint.setColor(Color.BLACK);
//    canvas.drawCircle(position.x, position.y, chaseRadius * 2, paint);
//    paint.setColor(Color.BLUE);
//    for(int i = 0; i < path.size(); i++) {
//        canvas.drawCircle(path.get(i).x, path.get(i).y, 5, paint);
//    }
}