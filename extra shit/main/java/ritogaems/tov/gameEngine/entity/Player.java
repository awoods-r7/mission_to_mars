package ritogaems.tov.gameEngine.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;

import ritogaems.tov.gameEngine.AssetStore;
import ritogaems.tov.gameEngine.Input.Button;

import ritogaems.tov.gameEngine.audio.SoundEffect;
import ritogaems.tov.gameEngine.graphics.animation.Animation;
import ritogaems.tov.gameEngine.graphics.animation.DirectedAnimation;
import ritogaems.tov.gameEngine.graphics.animation.Frame;
import ritogaems.tov.gameEngine.items.Arrow;
import ritogaems.tov.gameEngine.items.BackPack;
import ritogaems.tov.gameEngine.items.Bomb;
import ritogaems.tov.gameEngine.items.Boomerang;
import ritogaems.tov.gameEngine.items.Inventory;
import ritogaems.tov.util.CollisionDetector;
import ritogaems.tov.util.Vector2;
import ritogaems.tov.world.Portal;
import ritogaems.tov.world.screens.MapScreen;
import ritogaems.tov.world.viewports.LayerViewport;
import ritogaems.tov.world.viewports.ScreenViewport;

/**
 * @author Andrew Woods
 * @author Michael Purdy
 * @author Zoey Longridge
 * @author Darren McGarry
 * @author Kevin Martin
 *         Andrew
 *         - Framework
 *         Zoey
 *         - update
 *         - draw
 *         - collision
 *         - flinching
 *         Kevin
 *         - fire arrow
 *         - sound effects
 *         Darren
 *         - place bomb
 *         Michael
 *         - death
 */
public class Player extends Entity {

    /**
     * Instance of inventory
     */
    public Inventory inventory;

    /**
     * Instance of backpack
     */
    public BackPack backPack;

    /**
     * Time limit on time using items
     */
    private int itemCooldown = 0;

    /**
     * timer used on speedPotion
     */
    private int speedTimer;

    /**
     * timer used on damagePotion
     */
    private int damageTimer;

    /**
     * int of Standard Damage
     */
    private int standardDamage;

    /**
     * shootingAnimation
     */
    private DirectedAnimation shootingAnimation;

    /**
     * Animation for throwing boomerang
     */
    private DirectedAnimation throwingAnimation;

    /**
     * Sound Effects
     */
    private SoundEffect attackSound;
    public SoundEffect drinkSound;
    private SoundEffect hurtSound;
    private SoundEffect deathSound;
    private boolean attackSoundPlaying = false;

    /**
     * Max Health
     */
    private static final int MAX_HEALTH = 200;

    // /////////////////////////////
    // Constructors
    // /////////////////////////////

    /**
     * Creates a new player with full health
     * Initial animations and bitmaps are set here
     *
     * @param xPos      The x co-ordinate of the player (int game units)
     * @param yPos      The y co-ordinate of the player (in game units)
     * @param mapScreen The map screen the player belongs to
     */
    public Player(float xPos, float yPos, MapScreen mapScreen) {
        this(xPos, yPos, MAX_HEALTH, mapScreen);
    }

    /**
     * Creates a new player with a specific health
     * Initial animations and bitmaps are set here
     *
     * @param xPos          The x co-ordinate of the player (int game units)
     * @param yPos          The y co-ordinate of the player (in game units)
     * @param currentHealth The current health of the player
     * @param mapScreen     The map screen the player belongs to
     */
    public Player(float xPos, float yPos, int currentHealth, MapScreen mapScreen) {
        super(xPos, yPos, 10, 15, 10, 15, currentHealth, MAX_HEALTH, 40, mapScreen);

        AssetStore assetStore = mapScreen.getGame().getAssetStore();

        // create movement animation
        String name = "linkWalking";
        int noOfMoveFrames = 6;
        Bitmap spriteSheet = mapScreen.getGame().getAssetStore().getBitmap(name, "img/Link/Walking No Sword.png");
        movementAnimation = new DirectedAnimation(assetStore, name, spriteSheet, noOfMoveFrames, 12);

        // create sword animation
        String linkSword = "linkSword";
        Bitmap swordSheet = assetStore.getBitmap(linkSword, "img/Link/Swinging Sword.png");
        int noOfSwordFrames = 7;
        attackAnimation = new DirectedAnimation(assetStore, linkSword, swordSheet, noOfSwordFrames, 16);

        // create death animation
        String linkDeath = "linkDeath";
        Bitmap deathSheet = assetStore.getBitmap(linkDeath, "img/Link/Death.png");
        int noOfDeathFrames = 6;
        deathAnimation = new Animation(assetStore, linkDeath, deathSheet, noOfDeathFrames, noOfDeathFrames, 1, 12);

        // create shooting animation
        String linkShooting = "linkShooting";
        Bitmap shootingSheet = assetStore.getBitmap(linkShooting, "img/Link/Shooting Arrow.png");
        int noOfShootingFrames = 6;
        shootingAnimation = new DirectedAnimation(assetStore, linkShooting, shootingSheet, noOfShootingFrames, 12);

        // create sound effects
        attackSound = mapScreen.getGame().getAssetStore().getSfx("player attack", "audio/sfx/Player/Attacking.wav");
        drinkSound = mapScreen.getGame().getAssetStore().getSfx("player drink", "audio/sfx/Player/DrinkPotion.wav");
        hurtSound = mapScreen.getGame().getAssetStore().getSfx("player hurt", "audio/sfx/Player/Hurt.wav");
        deathSound = mapScreen.getGame().getAssetStore().getSfx("player hurt", "audio/sfx/Player/Death.wav");

        // create throwing animation
        String linkThrowing = "linkThrowing";
        Bitmap throwingSheet = assetStore.getBitmap(linkThrowing, "img/Link/Throwing Boomerang.png");
        int noOfThrowingFrames = 8;
        throwingAnimation = new DirectedAnimation(assetStore, linkThrowing, throwingSheet, noOfThrowingFrames, 12);

        // create other variables
        moveSpeed = 1.5f;
        inventory = new Inventory();

        // limited number of bombs - could be dropped like arrows
        inventory.increaseItem(Inventory.AmmoType.BOMB, 20);

        //adding one boomerang which doesn't decrease in ammo - it comes back
        inventory.increaseItem(Inventory.AmmoType.BOOMERANG, 1);

        DisplayMetrics metrics = new DisplayMetrics();
        mapScreen.getGame().getActivity().getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        backPack = new BackPack(metrics.widthPixels * 0.5f, metrics.heightPixels * 0.5f, metrics.widthPixels * 0.7f, metrics.heightPixels * 0.7f, assetStore, metrics, mapScreen);

        // set default state for player
        Frame startFrame = movementAnimation.getFrame(Direction.CENTER);
        this.bitmap = startFrame.getFrameBitmap();
        this.collisionBox = startFrame.getFrameDrawBox();
        this.state = entityState.MOVING;

        //set standardDamage = damage;
        standardDamage = damage;
    }

    // /////////////////////////////
    // Methods
    // /////////////////////////////

    /**
     * Updates the player
     *
     * @param vector    Vector by which to update position
     * @param direction Direction the player is facing
     */
    public void update(Vector2 vector, Direction direction, Button mSword) {

        setState(vector, mSword);
        switch (state) {
            case IDLE:
                attackSoundPlaying = false;
                break;
            case MOVING:
                vector.normalise();
                vector.multiply(moveSpeed);
                move(vector);
                this.direction = direction;
                break;
            case ATTACKING:
                if (!attackSoundPlaying) {
                    attackSound.play(false);
                    attackSoundPlaying = true;
                }
                break;
            case FLINCHING:
                if (flinchTimer == 0) {
                    attackAnimation.resetFrames();
                    shootingAnimation.resetFrames();
                    throwingAnimation.resetFrames();
                }
                flinch();
                break;
            case ROAMING:
                break;
            case CHASING:
                break;
            case DEAD:
                death();
                break;
        }

        if (itemCooldown != 0) {
            itemCooldown--;
        }

        if (damageTimer != 0) {
            damageTimer--;
        } else {
            damage = standardDamage;
        }

        if (speedTimer != 0) {
            speedTimer--;
        } else {
            moveSpeed = 1.5f;
        }

        if (invincibilityTimer != 0) {
            invincibilityTimer--;
        }

        if (checkTileCollision()) {
            checkEnemyCollisionWall();
        }
        checkEnemyCollision();
    }

    /**
     * @param vector
     * @param mSword
     * @author Zoey Longridge
     * Set state of the player and check what the current state is
     */
    private void setState(Vector2 vector, Button mSword) {
        if (getCurrentHealth() == 0 || state == entityState.DEAD) {
            state = entityState.DEAD;
        } else if (state != entityState.FLINCHING) {
            if (state != entityState.ATTACKING && state != entityState.SHOOTING && state != entityState.THROWING) {
                if (mSword.isClicked()) {
                    state = entityState.ATTACKING;
                } else if (vector.isZero()) {
                    state = entityState.IDLE;
                } else {
                    state = entityState.MOVING;
                }
            }
        }
    }


    /**
     * Draws the player, setting the current bitmap to the
     * appropriate animation based on the direction it is facing
     * and it's state
     *
     * @param canvas         Canvas on which to draw
     * @param layerViewport  Layer Viewport to draw from
     * @param screenViewport Screen Viewport to translate the Layer Viewport
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
            case ATTACKING:
                currentFrame = attackAnimation.getFrame(direction);
                bitmap = currentFrame.getFrameBitmap();
                collisionBox = currentFrame.getFrameDrawBox();
                if (attackAnimation.drawOnce(direction)) {
                    state = entityState.IDLE;
                }
                break;
            case SHOOTING:
                currentFrame = shootingAnimation.getFrame(direction);
                bitmap = currentFrame.getFrameBitmap();
                collisionBox = currentFrame.getFrameDrawBox();
                if (shootingAnimation.drawOnce(direction)) {
                    state = entityState.IDLE;
                }
                break;
            case THROWING:
                currentFrame = throwingAnimation.getFrame(direction);
                bitmap = currentFrame.getFrameBitmap();
                collisionBox = currentFrame.getFrameDrawBox();
                if (throwingAnimation.drawOnce(direction)) {
                    state = entityState.IDLE;
                }
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
            case DEAD:
                currentFrame = deathAnimation.getCurrentFrame();
                bitmap = currentFrame.getFrameBitmap();
                collisionBox = currentFrame.getFrameDrawBox();
                if (deathAnimation.drawOnce()) {
                }
                break;
        }
        super.draw(canvas, layerViewport, screenViewport);
    }

    /**
     * Getter
     *
     * @return position
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * @author Darren McGarry
     * Places a bomb in front of the player in the direction he is facing
     */
    public void placeBomb() {
        float x = position.x;
        float y = position.y;

        switch (movementAnimation.prevDirection) {
            case UP:
                y -= 2.5;
                break;
            case DOWN:
                y += 10;
                break;
            case RIGHT:
                x += 5;
                y += 2.5;
                break;
            case LEFT:
                x -= 5;
                y += 2.5;
                break;
            default:
                y += 5;
                break;
        }

        if (itemCooldown == 0) {
            if (inventory.useItem(Inventory.AmmoType.BOMB)) {
                itemCooldown = 30;
                mapScreen.items.add(new Bomb(x, y, mapScreen));
            }
        }
    }

    /**
     * This method throws the boomerang and stops you from throwing again until the boomerang
     * has returned to the player
     *
     * @author Andrew Woods
     */
    public void throwBoomerang() {
        if (itemCooldown == 0) {
            if (inventory.useItem(Inventory.AmmoType.BOOMERANG)) {
                state = entityState.THROWING;
                itemCooldown = 30;
                mapScreen.items.add(new Boomerang(position.x, position.y, 9, 9, movementAnimation.prevDirection, mapScreen));
            }
        }
    }

    /**
     * @author Kevin Martin
     * Fires an arrow in the players current direction
     */
    public void fireArrow() {
        if (itemCooldown == 0) {
            if (inventory.useItem(Inventory.AmmoType.ARROW)) {
                state = entityState.SHOOTING;
                itemCooldown = 30;
                switch (movementAnimation.prevDirection) {
                    case UP:
                    case DOWN:
                        mapScreen.items.add(new Arrow(position.x, position.y, 3, 12, movementAnimation.prevDirection, Arrow.PlayerOwner, mapScreen));
                        break;
                    case LEFT:
                    case RIGHT:
                        mapScreen.items.add(new Arrow(position.x, position.y, 12, 3, movementAnimation.prevDirection, Arrow.PlayerOwner, mapScreen));
                }

            }
        }
    }

    /**
     * Getter
     *
     * @return currentAmmoAmount
     */
    public String getCurrentAmmo() {
        return Integer.toString(inventory.getCurrentAmmoAmount());
    }

    /**
     * Getter
     *
     * @return backPack
     */
    public BackPack getBackPack() {
        return backPack;
    }

    /**
     * Updates the player's position
     *
     * @param vector Vector by which to update position
     */
    protected void move(Vector2 vector) {
        updatePosition(vector.x, vector.y);
    }

    /*
     * Player is spawned back in tent
     * currentHealth is set back to maxHealth
     * Depending on diffuculty level, ammo is restored
     */
    private void death() {
        deathSound.play(false);

        // reset health
        this.currentHealth = getMaxHealth();

        // reduce the player's ammo
        this.inventory.death(mapScreen.getGame().difficulty.fractionOfAmmoRetainedOnDeath);

        // move player to respawn position
        String playerRespawnScreen = "tutorialTent";
        float playerRespawnX = 95;
        float playerRespawnY = 105;

        Portal.changePlayerScreen(mapScreen.getGame().getScreenManager(), this,
                playerRespawnScreen, playerRespawnX, playerRespawnY);

        // set the player to alive
        state = entityState.IDLE;
    }

    /**
     * @param newSpeed
     * @author Zoey Longridge
     * Updated speedTimer
     */
    public void updateSpeed(float newSpeed) {
        speedTimer += 200;
        moveSpeed = newSpeed;
    }

    /**
     * @param newDamage
     * @author Zoey Longridge
     * Update damage and damageTimer
     */
    public void updateDamage(int newDamage) {
        damageTimer += 100;
        damage = newDamage + standardDamage;
    }

    /**
     * @author Zoey Longridge
     * Checks for collisions with enemies
     * If player is hit by enemy, player will be knocked back and take damage
     * If enemy is hit by player, enemy will be knocked back ans take damage
     */
    private void checkEnemyCollision() {
        for (Enemy enemy : mapScreen.enemies) {
            CollisionDetector.CollisionType collisionType = CollisionDetector.determineAndResolveCollision(this, enemy, false);

            switch (collisionType) {
                case Top:
                    if (direction == Direction.UP && state == entityState.ATTACKING) {
                        enemy.state = entityState.FLINCHING;
                        enemy.bounceBack.set(0, -2);
                        enemy.takeDamage(damage);
                    } else {
                        if (invincibilityTimer == 0) {
                            state = entityState.FLINCHING;
                            this.bounceBack.set(0, 2);
                            takeDamage(enemy.getDamage());
                            hurtSound.play(false);
                        }
                    }
                    break;
                case Bottom:
                    if (direction == Direction.DOWN && state == entityState.ATTACKING) {
                        enemy.state = entityState.FLINCHING;
                        enemy.bounceBack.set(0, 2);
                        enemy.takeDamage(damage);
                    } else {
                        if (invincibilityTimer == 0) {
                            state = entityState.FLINCHING;
                            this.bounceBack.set(0, -2);
                            takeDamage(enemy.getDamage());
                            hurtSound.play(false);
                        }
                    }
                    break;
                case Right:
                    if (direction == Direction.RIGHT && state == entityState.ATTACKING) {
                        enemy.state = entityState.FLINCHING;
                        enemy.bounceBack.set(2, 0);
                        enemy.takeDamage(damage);
                    } else {
                        if (invincibilityTimer == 0) {
                            state = entityState.FLINCHING;
                            this.bounceBack.set(-2, 0);
                            takeDamage(enemy.getDamage());
                            hurtSound.play(false);
                        }
                    }
                    break;
                case Left:
                    if (direction == Direction.LEFT && state == entityState.ATTACKING) {
                        enemy.state = entityState.FLINCHING;
                        enemy.bounceBack.set(-2, 0);
                        enemy.takeDamage(damage);
                    } else {
                        if (invincibilityTimer == 0) {
                            state = entityState.FLINCHING;
                            this.bounceBack.set(2, 0);
                            takeDamage(enemy.getDamage());
                            hurtSound.play(false);
                        }
                    }
                    break;
                case None:
                    break;
            }
        }
    }

    /**
     * @author Zoey Longridge
     * If player attacks enemy, enemy will be knocked back and take damage
     * If enemy attacks player, player will be knocked back and take damage
     */
    private void checkEnemyCollisionWall() {
        for (Enemy enemy : mapScreen.enemies) {
            CollisionDetector.CollisionType collisionType = CollisionDetector.determineAndResolveCollision(enemy, this, false);
            switch (collisionType) {
                case Top:
                    if (direction == Direction.UP && state == entityState.ATTACKING) {
                        enemy.state = entityState.FLINCHING;
                        enemy.bounceBack.set(0, 2);
                        enemy.takeDamage(damage);
                    } else {
                        if (invincibilityTimer == 0) {
                            state = entityState.FLINCHING;
                            this.bounceBack.set(0, -2);
                            takeDamage(enemy.getDamage());
                            hurtSound.play(false);
                        }
                    }
                    break;
                case Bottom:
                    if (direction == Direction.DOWN && state == entityState.ATTACKING) {
                        enemy.state = entityState.FLINCHING;
                        enemy.bounceBack.set(0, -2);
                        enemy.takeDamage(damage);
                    } else {
                        if (invincibilityTimer == 0) {
                            state = entityState.FLINCHING;
                            this.bounceBack.set(0, 2);
                            takeDamage(enemy.getDamage());
                            hurtSound.play(false);
                        }
                    }
                    break;
                case Right:
                    if (direction == Direction.RIGHT && state == entityState.ATTACKING) {
                        enemy.state = entityState.FLINCHING;
                        enemy.bounceBack.set(-2, 0);
                        enemy.takeDamage(damage);
                    } else {
                        if (invincibilityTimer == 0) {
                            state = entityState.FLINCHING;
                            this.bounceBack.set(2, 0);
                            takeDamage(enemy.getDamage());
                            hurtSound.play(false);
                        }
                    }
                    break;
                case Left:
                    if (direction == Direction.LEFT && state == entityState.ATTACKING) {
                        enemy.state = entityState.FLINCHING;
                        enemy.bounceBack.set(2, 0);
                        enemy.takeDamage(damage);
                    } else {
                        if (invincibilityTimer == 0) {
                            state = entityState.FLINCHING;
                            this.bounceBack.set(-2, 0);
                            takeDamage(enemy.getDamage());
                            hurtSound.play(false);
                        }
                    }
                    break;
                case None:
                    break;
            }
        }
    }

    /**
     * update damage when choosing difficulty
     *
     * @param damage
     */
    @Override
    public void takeDamage(int damage) {
        if (invincibilityTimer == 0) {
            super.takeDamage((int) (damage * mapScreen.getGame().difficulty.damageTakenMultiplier));
        }
    }
}