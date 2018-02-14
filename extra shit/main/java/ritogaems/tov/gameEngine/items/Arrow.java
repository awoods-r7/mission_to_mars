package ritogaems.tov.gameEngine.items;

import ritogaems.tov.gameEngine.AssetStore;
import ritogaems.tov.gameEngine.ElapsedTime;
import ritogaems.tov.gameEngine.audio.SoundEffect;
import ritogaems.tov.gameEngine.entity.Direction;
import ritogaems.tov.gameEngine.entity.Enemy;
import ritogaems.tov.gameEngine.entity.Entity;
import ritogaems.tov.gameEngine.entity.Player;
import ritogaems.tov.util.CollisionDetector;
import ritogaems.tov.util.Vector2;
import ritogaems.tov.world.screens.MapScreen;

/**
 * Creates an Arrow
 */
public class Arrow extends Item {

    /**
     * Shows that the player is the owner of the arrow for checking collision
     */
    public static final int PlayerOwner = 1;

    /**
     * Shows that the enemy is the owner of the arrow for checking collision
     */
    public static final int EnemyOwner = 2;

    /**
     * The time the arrow has left to fly
     */
    private double timeToFly = 3;

    /**
     * The sound played when an arrow is fired
     */
    private SoundEffect arrowSound;

    /**
     * The direction the arrow is fired in
     */
    private Direction direction;

    /**
     * The movement vector of the arrow
     */
    private Vector2 movementVector;

    /**
     * The owner of the arrow
     */
    private int owner;

    /**
     * The damage the arrow deals on collision
     */
    public int damage = 25;

    /**
     * Constructor for the arrow
     *
     * @param xPos      The starting x Position of the Arrow
     * @param yPos      The starting y Position of the ARrow
     * @param width     The width of the arrow
     * @param height    The Height of the arrow
     * @param direction The direction the arrow is fired in
     * @param owner     The owner of the arrow
     * @param mapScreen The MapScreen the arrow is fired on
     */
    public Arrow(float xPos, float yPos, float width, float height, Direction direction, int owner, MapScreen mapScreen) {
        super(xPos, yPos, width, height, width * 0.9f, height * 0.9f, mapScreen);
        this.direction = direction;
        this.owner = owner;

        itemName = Inventory.AmmoType.ARROW.toString();

        AssetStore assetStore = mapScreen.getGame().getAssetStore();

        switch (direction) {
            case UP:
                bitmap = assetStore.getBitmap("ArrowUp", "img/Items/ArrowUp.png");
                movementVector = new Vector2(0, -2);
                break;
            case DOWN:
                bitmap = assetStore.getBitmap("ArrowDown", "img/Items/ArrowDown.png");
                movementVector = new Vector2(0, 2);
                break;
            case LEFT:
                bitmap = assetStore.getBitmap("ArrowLeft", "img/Items/ArrowLeft.png");
                movementVector = new Vector2(-2, 0);
                break;
            case RIGHT:
                bitmap = assetStore.getBitmap("ArrowRight", "img/Items/ArrowRight.png");
                movementVector = new Vector2(2, 0);
                break;
        }
        arrowSound = assetStore.getSfx("arrow", "audio/sfx/arrow.wav");
        arrowSound.play(false);
    }

    /**
     * Updates the arrow for this frame of the Game Loop
     *
     * @param elapsedTime The time that has elapsed since the last frame
     */
    @Override
    public void update(ElapsedTime elapsedTime) {
        timeToFly -= elapsedTime.stepTime;
        if (timeToFly <= 0) {
            alive = false;
        } else {
            updatePosition(movementVector.x, movementVector.y);
        }

        if (checkTileCollision()) {
            alive = false;
        }
        switch (owner) {
            case PlayerOwner:
                checkEnemyCollision();
                break;
            case EnemyOwner:
                checkPlayerCollision();
                break;
        }
    }

    /**
     * Getter for the direction
     *
     * @return the direction of the arrow
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     *Checks for collision with the enemy and deals damage if its the correct side of the arrow
     */
    private void checkEnemyCollision() {
        for (Enemy enemy : mapScreen.enemies) {
            CollisionDetector.CollisionType collisionType = CollisionDetector.determineAndResolveCollision(enemy, this, false);
            switch (collisionType) {
                case Top:
                    if (direction == Direction.DOWN) {
                        enemy.state = Entity.entityState.FLINCHING;
                        enemy.bounceBack.set(0, 2);
                        alive = false;
                        enemy.takeDamage(damage);
                    }
                    break;
                case Bottom:
                    if (direction == Direction.UP) {
                        enemy.state = Entity.entityState.FLINCHING;
                        enemy.bounceBack.set(0, -2);
                        alive = false;
                        enemy.takeDamage(damage);
                    }
                    break;
                case Right:
                    if (direction == Direction.LEFT) {
                        enemy.state = Entity.entityState.FLINCHING;
                        enemy.bounceBack.set(-2, 0);
                        alive = false;
                        enemy.takeDamage(damage);
                    }
                    break;
                case Left:
                    if (direction == Direction.RIGHT) {
                        enemy.state = Entity.entityState.FLINCHING;
                        enemy.bounceBack.set(2, 0);
                        alive = false;
                        enemy.takeDamage(damage);
                    }
                    break;
                case None:
                    break;
            }
        }
    }

    /**
     * Checks for collision with the player and deals damage if its the correct side of the arrow
     */
    private void checkPlayerCollision() {
        Player player = mapScreen.getPlayer();
        CollisionDetector.CollisionType collisionType = CollisionDetector.determineAndResolveCollision(player, this, false);
        switch (collisionType) {
            case Top:
                if (direction == Direction.DOWN) {
                    player.state = Entity.entityState.FLINCHING;
                    player.bounceBack.set(0, 2);
                    alive = false;
                    player.takeDamage(damage);
                }
                break;
            case Bottom:
                if (direction == Direction.UP) {
                    player.state = Entity.entityState.FLINCHING;
                    player.bounceBack.set(0, -2);
                    alive = false;
                    player.takeDamage(damage);
                }
                break;
            case Right:
                if (direction == Direction.LEFT) {
                    player.state = Entity.entityState.FLINCHING;
                    player.bounceBack.set(-2, 0);
                    alive = false;
                    player.takeDamage(damage);
                }
                break;
            case Left:
                if (direction == Direction.RIGHT) {
                    player.state = Entity.entityState.FLINCHING;
                    player.bounceBack.set(2, 0);
                    alive = false;
                    player.takeDamage(damage);
                }
                break;
            case None:
                break;
        }
    }
}