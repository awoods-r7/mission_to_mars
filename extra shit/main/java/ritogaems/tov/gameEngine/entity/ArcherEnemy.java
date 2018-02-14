package ritogaems.tov.gameEngine.entity;

import android.graphics.Bitmap;

import java.util.Random;

import ritogaems.tov.gameEngine.graphics.animation.DirectedAnimation;
import ritogaems.tov.gameEngine.graphics.animation.Frame;
import ritogaems.tov.gameEngine.graphics.particles.generation.ParticleEmitter;
import ritogaems.tov.gameEngine.graphics.tilemapEngine.TileMap;
import ritogaems.tov.gameEngine.items.AmmoDrop;
import ritogaems.tov.gameEngine.items.Arrow;
import ritogaems.tov.gameEngine.items.Inventory;
import ritogaems.tov.util.Vector2;
import ritogaems.tov.world.screens.MapScreen;

/**
 * @author Kevin Martin
 *         <p/>
 *         An enemy that shoots arrows at the player
 */
public class ArcherEnemy extends Enemy {

    /**
     * The cooldown between being able to fire more arrows
     */
    private int arrowCooldown = 0;

    /**
     * The direction the enemy is strafing in
     */
    private Direction strafeDirection = Direction.CENTER;
    /**
     * The timer before the enemy picks a new direction to strafe in
     */
    private int strafeTimer = 0;

    /**
     * Constructor for an Archer Enemy with a base health amount
     *
     * @param xPos      The xPos of the Archer Enemy
     * @param yPos      The yPos of the Archer Enemy
     * @param tileMap   The tilemap the Archer Enemy belongs to
     * @param mapScreen The mapScreen the Archer Enemy belongs to
     */
    public ArcherEnemy(float xPos, float yPos, TileMap tileMap, MapScreen mapScreen) {
        this(xPos, yPos, 40, tileMap, mapScreen);
    }

    /**
     * Constructor for an Archer Enemy with a base health amount
     *
     * @param xPos          The xPos of the Archer Enemy
     * @param yPos          The yPos of the Archer Enemy
     * @param currentHealth The current and max health of the enemy
     * @param tileMap       The tilemap the Archer Enemy belongs to
     * @param mapScreen     The mapScreen the Archer Enemy belongs to
     */
    private ArcherEnemy(float xPos, float yPos, int currentHealth, TileMap tileMap, MapScreen mapScreen) {
        super(xPos, yPos, 10, 15, 10, 15, currentHealth, currentHealth, 10, tileMap, mapScreen);

        // animations
        String name = "WalkingArcher";
        int noOfFrames = 6;
        Bitmap walkingSheet = mapScreen.getGame().getAssetStore().getBitmap(name, "img/Enemies/ArcherWalking.png");
        movementAnimation = new DirectedAnimation(mapScreen.getGame().getAssetStore(), name, walkingSheet, noOfFrames, 12);

        name = "ShootingArcher";
        noOfFrames = 4;
        Bitmap shootingSheet = mapScreen.getGame().getAssetStore().getBitmap(name, "img/Enemies/ArcherShooting.png");
        attackAnimation = new DirectedAnimation(mapScreen.getGame().getAssetStore(), name, shootingSheet, noOfFrames, 12);

        // starting bitmap
        Frame startFrame = movementAnimation.getFrame(Direction.CENTER);
        this.bitmap = startFrame.getFrameBitmap();
        this.collisionBox = startFrame.getFrameDrawBox();

        roamCenter = new Vector2(xPos, yPos);
        roamRadius = 40;
        chaseRadius = 60;
        attackRadius = 80;
        moveSpeed = 0.5f;
    }

    /**
     * Move the archer
     *
     * @param vector by which to adjust the archers position
     */
    public void move(Vector2 vector) {
        updatePosition(vector.x, vector.y);
    }

    /**
     * Checks if the arrow is off cooldown for shooting at the player if in range
     */
    public void attack() {
        if (arrowCooldown == 0) {
            movementVector.set(0, 0);
            arrowCooldown = 60;
            fireArrow(calculateArrowDirection());
        } else {
            arrowCooldown--;
            if (strafeTimer == 0) {
                strafeTimer = 10;
                strafe();
            } else {
                strafeTimer--;
            }
        }
        updatePosition(movementVector.x, movementVector.y);
    }

    /**
     * Chance to drop items when the archer dies
     */
    public void death() {
        if (ParticleEmitter.random.nextFloat() > 0.3) {
            mapScreen.addConsumable(new AmmoDrop((int) position.x, (int) position.y, 10, 10,
                    (int) ParticleEmitter.randomBetween(5, 20),
                    Inventory.AmmoType.ARROW, true, mapScreen));
        }
    }

    /**
     * Method to calculate which direction for the archer to strafe in
     */
    private void strafe() {
        Random random = new Random();

        if (random.nextInt(2) == 1) {
            switch (strafeDirection) {
                case UP:
                case DOWN:
                    movementVector.set(0.5f, 0);
                    break;
                case RIGHT:
                case LEFT:
                    movementVector.set(0, 0.5f);
                    break;
                case CENTER:
                    break;
            }
        } else {
            switch (strafeDirection) {
                case UP:
                case DOWN:
                    movementVector.set(-0.5f, 0);
                    break;
                case RIGHT:
                case LEFT:
                    movementVector.set(0, -0.5f);
                    break;
                case CENTER:
                    break;
            }
        }
    }

    /**
     * Fires an arrow in the calculated direction
     *
     * @param direction the direction to fire the arrow in
     */
    private void fireArrow(Direction direction) {
        switch (direction) {
            case UP:
            case DOWN:
                mapScreen.items.add(new Arrow(position.x, position.y, 3, 12, direction, Arrow.EnemyOwner, mapScreen));
                strafeDirection = Direction.UP;
                break;
            case LEFT:
            case RIGHT:
                mapScreen.items.add(new Arrow(position.x, position.y, 12, 3, direction, Arrow.EnemyOwner, mapScreen));
                strafeDirection = Direction.RIGHT;
                break;
            case CENTER:
                break;
        }
    }

    /**
     * Calculates the direction to shoot an arrow in
     *
     * @return a direction for use in the fireArrow method
     */
    private Direction calculateArrowDirection() {
        double angle = position.getAngle(mapScreen.getPlayer().position);

        double rightUpper = (Math.PI) / 6; // 0.52
        double rightLower = -(Math.PI) / 6; // -0.52
        double upLeft = (2 * Math.PI) / 3; // 2.09
        double upRight = (Math.PI) / 3; // 1.04
        double leftUpper = (5 * Math.PI) / 6; // 2.61
        double leftLower = -(5 * Math.PI) / 6; // - 2.61
        double downLeft = -(2 * Math.PI) / 3; // -2.09
        double downRight = -(Math.PI) / 3; // -1.04

        if (angle > rightLower && angle < rightUpper) {
            return Direction.LEFT;
        } else if (angle > upRight && angle < upLeft) {
            return Direction.UP;
        } else if (angle > leftUpper || angle < leftLower) {
            return Direction.RIGHT;
        } else if (angle < downRight && angle > downLeft) {
            return Direction.DOWN;
        } else {
            return Direction.CENTER;
        }
    }
}