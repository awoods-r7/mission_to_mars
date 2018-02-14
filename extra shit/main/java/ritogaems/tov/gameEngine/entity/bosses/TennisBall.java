package ritogaems.tov.gameEngine.entity.bosses;

import java.util.ArrayList;
import java.util.Random;

import ritogaems.tov.ai.AIUtil;
import ritogaems.tov.gameEngine.audio.SoundEffect;
import ritogaems.tov.gameEngine.entity.Direction;
import ritogaems.tov.gameEngine.entity.Entity;
import ritogaems.tov.gameEngine.entity.Player;
import ritogaems.tov.util.BoundingBox;
import ritogaems.tov.util.CollisionDetector;
import ritogaems.tov.util.SteeringUtil;
import ritogaems.tov.util.Vector2;
import ritogaems.tov.world.screens.MapScreen;

/**
 * @author Kevin Martin
 *         <p/>
 *         TennisBall object used by the Phantom Boss
 */
public class TennisBall extends Entity {

    ////////////////
    // PROPERTIES
    ////////////////

    /**
     * Stores the different owners of the tennis ball for collision purposes
     */
    public enum ballOwner {
        PLAYER, PHANTOM
    }

    /**
     * Current owner of the tennis ball
     */
    private ballOwner owner;

    /**
     * Path for the ball to seek along to reach the target
     */
    private ArrayList<Vector2> path = new ArrayList<>();

    /**
     * Movement vector to adjust the balls position each frame
     */
    private Vector2 movementVector = new Vector2();

    /**
     * Timer for when to calculate a new path
     */
    private int chaseTimer = 15;

    /**
     * Boolean to check if the ball is still alive or not
     */
    private boolean isAlive = true;

    /**
     * Sound effect for when the ball hits a target
     */
    private SoundEffect ballHit;

    /**
     * Sound effect for when the ball is reflected by a target
     */
    private SoundEffect ballReflect;

    /**
     * The constructor for the Tennis Ball
     *
     * @param xPos      X position (centre in game units)
     * @param yPos      Y position (centre in game units)
     * @param moveSpeed Factor by which to adjust the Tennis Balls moveSpeed
     * @param mapScreen MapScreen to which the Tennis Ball belongs
     */
    public TennisBall(float xPos, float yPos, float moveSpeed, MapScreen mapScreen) {
        super(xPos, yPos, 7.5f, 7.5f, 7.5f, 7.5f, 100, 100, 50, mapScreen);

        bitmap = mapScreen.getGame().getAssetStore().getBitmap("TennisBall", "img/Enemies/Bosses/PhantomTennisBall.png");

        this.moveSpeed = moveSpeed;

        ballHit = mapScreen.getGame().getAssetStore().getSfx("ball hit", "audio/sfx/PhantomBoss/BallHit.wav");

        ballReflect = mapScreen.getGame().getAssetStore().getSfx("ball reflect", "audio/sfx/PhantomBoss/BallReflect.wav");

        owner = ballOwner.PHANTOM;
    }

    /**
     * Update the Tennis Ball for this frame of the Game Loop
     *
     * @param phantomChance The chance of the phantom to return
     */
    public void update(int phantomChance) {
        switch (owner) {
            case PLAYER:
                if (chaseTimer >= 15) {
                    path = AIUtil.FindPath(position, mapScreen.phantomBoss.position, mapScreen.tileMap.getPathGrid());
                    chaseTimer = 0;
                } else {
                    chaseTimer++;
                }
                updateMovement();
                checkPhantomCollision(phantomChance);
                break;
            case PHANTOM:
                if (chaseTimer >= 15) {
                    path = AIUtil.FindPath(position, mapScreen.getPlayer().position, mapScreen.tileMap.getPathGrid());
                    chaseTimer = 0;
                } else {
                    chaseTimer++;
                }
                updateMovement();
                checkPlayerCollision();
                break;
        }
    }

    /**
     * Checks if the Tennis Ball is currently alive
     *
     * @return the isAlive boolean
     */
    public boolean isAlive() {
        return isAlive;
    }

    /**
     * Sets the owner of the Tennis ball
     *
     * @param newOwner the new owner of the ball
     */
    private void setOwner(ballOwner newOwner) {
        owner = newOwner;
    }

    /**
     * Update the movement of the ball based on the path seeking
     */
    private void updateMovement() {
        BoundingBox bound = getCollisionBox();
        if (!path.isEmpty()) {
            if (bound.contains(path.get(0).x, path.get(0).y)) {
                path.remove(0);
                if (!path.isEmpty()) {
                    movementVector = SteeringUtil.seek(position, path.get(0), moveSpeed);
                } else {
                    movementVector.set(0, 0);
                }
            }
        } else {
            movementVector.set(0, 0);
        }
        move(movementVector);
    }

    /**
     * Check Phantom Collision if the player is the owner
     *
     * @param phantomChance The chance of the phantom reflecting the Tennis Ball
     */
    private void checkPhantomCollision(int phantomChance) {
        CollisionDetector.CollisionType collisionType = CollisionDetector.determineCollisionType(this, mapScreen.phantomBoss, false);

        if (collisionType != CollisionDetector.CollisionType.None) {
            Random random = new Random();
            if (random.nextInt(101) <= phantomChance) {
                setOwner(ballOwner.PHANTOM);
                chaseTimer = 15;
                ballReflect.play(false);
            } else {
                mapScreen.phantomBoss.stage = PhantomBoss.bossStages.STUNNED;
                mapScreen.phantomBoss.tennisTimer = PhantomBoss.tennisLimit;
                isAlive = false;
                ballHit.play(false);
            }
        }
    }

    /**
     * Check Player collision if the phantom is the owner
     */
    private void checkPlayerCollision() {
        Player player = mapScreen.getPlayer();
        CollisionDetector.CollisionType collisionType = CollisionDetector.determineCollisionType(this, player, false);

        if (collisionType != CollisionDetector.CollisionType.None) {
            if (player.state != entityState.ATTACKING) {
                player.state = entityState.FLINCHING;
                player.takeDamage(damage);
                mapScreen.phantomBoss.tennisTimer = PhantomBoss.tennisLimit;
                ballHit.play(false);
                isAlive = false;
            } else {
                switch (collisionType) {
                    case Top:
                        if (player.direction == Direction.DOWN) {
                            setOwner(ballOwner.PLAYER);
                            chaseTimer = 15;
                            ballReflect.play(false);
                        } else {
                            player.state = entityState.FLINCHING;
                            player.takeDamage(damage);
                            mapScreen.phantomBoss.tennisTimer = PhantomBoss.tennisLimit;
                            ballHit.play(false);
                            isAlive = false;
                        }
                        break;
                    case Bottom:
                        if (player.direction == Direction.UP) {
                            setOwner(ballOwner.PLAYER);
                            chaseTimer = 15;
                            ballReflect.play(false);
                        } else {
                            player.state = entityState.FLINCHING;
                            player.takeDamage(damage);
                            mapScreen.phantomBoss.tennisTimer = PhantomBoss.tennisLimit;
                            ballHit.play(false);
                            isAlive = false;
                        }
                        break;
                    case Left:
                        if (player.direction == Direction.RIGHT) {
                            setOwner(ballOwner.PLAYER);
                            chaseTimer = 15;
                            ballReflect.play(false);
                        } else {
                            player.state = entityState.FLINCHING;
                            player.takeDamage(damage);
                            mapScreen.phantomBoss.tennisTimer = PhantomBoss.tennisLimit;
                            ballHit.play(false);
                            isAlive = false;
                        }
                        break;
                    case Right:
                        if (player.direction == Direction.LEFT) {
                            setOwner(ballOwner.PLAYER);
                            chaseTimer = 15;
                            ballReflect.play(false);
                        } else {
                            player.state = entityState.FLINCHING;
                            player.takeDamage(damage);
                            mapScreen.phantomBoss.tennisTimer = PhantomBoss.tennisLimit;
                            ballHit.play(false);
                            isAlive = false;
                        }
                        break;
                }
            }
        }
    }

    /**
     * Move the Tennis Ball to a new position
     *
     * @param vector The vector to update its position by
     */
    protected void move(Vector2 vector) {
        updatePosition(vector.x, vector.y);
    }
}