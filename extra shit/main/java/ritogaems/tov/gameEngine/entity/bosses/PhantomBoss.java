package ritogaems.tov.gameEngine.entity.bosses;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import ritogaems.tov.ai.AIUtil;
import ritogaems.tov.gameEngine.AssetStore;
import ritogaems.tov.gameEngine.audio.SoundEffect;
import ritogaems.tov.gameEngine.entity.Direction;
import ritogaems.tov.gameEngine.entity.Entity;
import ritogaems.tov.gameEngine.entity.Player;
import ritogaems.tov.gameEngine.graphics.animation.DirectedAnimation;
import ritogaems.tov.gameEngine.graphics.animation.Frame;
import ritogaems.tov.gameEngine.items.Arrow;
import ritogaems.tov.gameEngine.items.Inventory;
import ritogaems.tov.gameEngine.items.Item;
import ritogaems.tov.util.BoundingBox;
import ritogaems.tov.util.CollisionDetector;
import ritogaems.tov.util.SteeringUtil;
import ritogaems.tov.util.Vector2;
import ritogaems.tov.world.screens.MapScreen;
import ritogaems.tov.world.viewports.LayerViewport;
import ritogaems.tov.world.viewports.ScreenViewport;

/**
 * @author Kevin Martin
 *         <p/>
 *         A Phantom Boss
 *         - Could be split into 3 smaller classes if time permitted
 */
public class PhantomBoss extends Entity {

    ////////////////
    // STATIC VARIABLES
    ////////////////

    /**
     * The maximum amount for the Pause Timer
     */
    private final static int pauseLimit = 60;

    /**
     * The maximum amount for the Wait Timer
     */
    private final static int waitLimit = 90;

    /**
     * The maximum amount for the Direction Timer
     */
    private final static int directionLimit = 30;

    /**
     * The maximum amount for the Tennis Timer
     */
    public final static int tennisLimit = 120;

    /**
     * The maximum amount for the Stun Timer
     */
    private final static int stunLimit = 90;

    /**
     * The maximum amount for the Death Timer
     */
    private final static int deathLimit = 180;

    ////////////////
    // GENERAL VARIABLES
    ////////////////

    /**
     * The different stages available for the Phantom Boss
     */
    public enum bossStages {
        IDLE, SPAWN, PHANTOM, FLINCHING, TENNIS, STUNNED, DEAD
    }

    /**
     * The movement vector of the Phantom Boss
     */
    Vector2 movementVector = new Vector2();

    /**
     * The current stage of the Phantom Boss
     */
    public bossStages stage;

    /**
     * The area that the Phantom Boss is confined to
     */
    private BoundingBox bossArea;

    /**
     * The Paint for adjusting the alpha value during drawing
     */
    Paint paint = new Paint();

    /**
     * The fence that locks the player in and triggers the fight
     */
    private Fence lockFence;

    /**
     * The fence to seperate the boss area during the Tennis Phase
     */
    private Fence tennisFence;

    ////////////////
    // PHANTOM PHASE VARIABLES
    ////////////////

    /**
     * The idle Phantom Spawns currently available
     */
    private ArrayList<PhantomSpawn> idleSpawns = new ArrayList<>();

    /**
     * The Phantom Spawns currently in use
     */
    private ArrayList<PhantomSpawn> activeSpawns = new ArrayList<>();

    /**
     * The list of Phantoms
     */
    private ArrayList<Phantom> phantoms = new ArrayList<>();

    /**
     * The current spawn assigned to the Phantom Boss
     */
    public PhantomSpawn spawn;

    /**
     * The pause between each round of the Phantom phase
     */
    private int pauseTimer = pauseLimit;

    /**
     * The time waiting to charge up during the Phantom phase
     */
    protected int waitTimer = waitLimit;

    ////////////////
    // TENNIS PHASE VARIABLES
    ////////////////

    /**
     * List of Tennis Balls currently on the screen
     */
    private ArrayList<TennisBall> tennisBalls = new ArrayList<>();

    /**
     * The path the Phantom Boss seeks along
     */
    private ArrayList<Vector2> path = new ArrayList<>();

    /**
     * The time waiting to change direction when walking randomly
     */
    private int directionTimer = directionLimit;

    /**
     * The time waiting to fire another Tennis Ball
     */
    public int tennisTimer = tennisLimit;

    /**
     * The chance of the Phantom Boss reflecting the Tennis Ball
     */
    private int returnChance = 30;

    /**
     * The speed at which the Tennis Ball will move
     */
    private float ballSpeed = 1.0f;

    ////////////////
    // STUNNED & DEAD VARIABLES
    ////////////////

    /**
     * The time waiting while the Phantom Boss is stunned
     */
    private int stunTimer = stunLimit;

    /**
     * Determines if the Phantom Boss has reached the center of its area
     */
    private boolean deadPathGenerated = false;

    /**
     * Stores the original Spawn Point
     */
    private Vector2 spawnPoint;

    /**
     * The time waiting for the death animation to play out
     */
    private int deathTimer = deathLimit;

    ////////////////
    // SOUND EFFECTS
    ////////////////

    /**
     * The sound played when the Phantom Boss dies
     */
    private SoundEffect finalDeath;

    /**
     * The sound played when the Phantom Boss is hit by an arrow
     */
    private SoundEffect hitByArrow;

    /**
     * The sound played when the Phantom Boss is hit by a sword
     */
    private SoundEffect hitBySword;

    /**
     * The sound played when the Phantom Boss has finished dying
     */
    private SoundEffect victorySound;

    ////////////////
    // Constructors
    ////////////////

    /**
     * Constructor used by the Phantoms
     *
     * @param xPos      X position (centre in game units)
     * @param yPos      Y position (centre in game units)
     * @param width     Width of the draw and collision boxes
     * @param height    Height of the draw and collision boxes
     * @param damage    Damage done by this entity
     * @param mapScreen The MapScreen this entity belongs to
     */
    PhantomBoss(float xPos, float yPos, float width, float height, int damage, MapScreen mapScreen) {
        super(xPos, yPos, width, height, width, height, 100, 100, damage, mapScreen);

        // set the limits for the phantoms
        bossArea = new BoundingBox(xPos, yPos, 125, 70);
        moveSpeed = 2.0f;

        // sound effects
        hitByArrow = mapScreen.getGame().getAssetStore().getSfx("PB hit by arrow", "audio/sfx/PhantomBoss/HitByArrow.wav");
    }

    /**
     * Constructor used by the PhantomBoss
     *
     * @param xPos      X position (centre in game units)
     * @param yPos      Y position (centre in game units)
     * @param width     Width of the draw and collision boxes
     * @param height    Height of the draw and collision boxes
     * @param mapScreen The MapScreen this entity belongs to
     */
    public PhantomBoss(int xPos, int yPos, float width, float height, MapScreen mapScreen) {
        super(xPos, yPos, width, height, width, height, 500, 500, 40, mapScreen);

        AssetStore assetStore = mapScreen.getGame().getAssetStore();

        // set the limits & spawn of the boss
        bossArea = new BoundingBox(xPos, yPos, 125, 70);
        spawnPoint = new Vector2(xPos, yPos);

        // animation
        String name = "PhantomBoss";
        int noOfFrames = 3;
        Bitmap walkingSheet = assetStore.getBitmap(name, "img/Enemies/Bosses/Phantom Boss.png");
        movementAnimation = new DirectedAnimation(assetStore, name, walkingSheet, noOfFrames, 30);

        // create the lock fence
        Bitmap fence = assetStore.getBitmap("Fence", "img/Enemies/Bosses/Fence.png");
        lockFence = new Fence(200, 145, 40, 17, fence, mapScreen);

        // create the tennis fence
        Bitmap abyss = assetStore.getBitmap("Abyss", "img/Enemies/Bosses/Abyss.png");
        tennisFence = new Fence(200, 75, 240, 10, abyss, mapScreen);

        // create the sound effects
        finalDeath = mapScreen.getGame().getAssetStore().getSfx("PB final death", "audio/sfx/PhantomBoss/FinalDeath.wav");

        hitByArrow = mapScreen.getGame().getAssetStore().getSfx("PB hit by arrow", "audio/sfx/PhantomBoss/HitByArrow.wav");

        hitBySword = mapScreen.getGame().getAssetStore().getSfx("PB hit by sword", "audio/sfx/PhantomBoss/HitBySword.wav");

        victorySound = mapScreen.getGame().getAssetStore().getSfx("victory", "audio/sfx/Success.wav");

        // initial settings
        Frame startFrame = movementAnimation.getFrame(Direction.CENTER);
        this.bitmap = startFrame.getFrameBitmap();
        this.collisionBox = startFrame.getFrameDrawBox();
        stage = bossStages.IDLE;
        moveSpeed = 2.0f;
    }

    /**
     * Updates the Phantom Boss for this frame of the Game Loop
     */
    public void update() {
        // check state and call appropriate method
        switch (stage) {
            case IDLE:
                idleSequence();
                break;
            case SPAWN:
                spawnSequence();
                move(movementVector);
                break;
            case PHANTOM:
                invincibilityTimer = 0;
                phantomSequence();
                break;
            case FLINCHING:
                flinch();
                if (flinchTimer == 0) {
                    stage = bossStages.PHANTOM;
                    emptyActiveSpawns();
                }
                for (Phantom phantom : phantoms) {
                    phantom.update();
                }
                break;
            case TENNIS:
                tennisSequence();
                break;
            case STUNNED:
                if (invincibilityTimer != 0) {
                    invincibilityTimer--;
                }
                stunnedSequence();
                break;
            case DEAD:
                deathSequence();
                break;
        }

        // update the fences to check for player collision
        lockFence.update();
        tennisFence.update();

        // update the bosses draw direction and check collision
        updateDirection();
    }

    /**
     * The Idle Stage of the boss fight
     * Checks for if the player has entered the boss area
     */
    private void idleSequence() {
        if (lockFence.checkPlayerCollision()) {
            mapScreen.setSave(false);
            lockFence.setExists(true);
            stage = bossStages.SPAWN;
            mapScreen.changeBackgroundMusic("shadow dungeon boss music", "audio/bgm/ShadowDungeonBoss.mp3", true);
        }
    }

    /**
     * Empties the active spawns for the next round of the Phantom Phase
     */
    private void emptyActiveSpawns() {
        for (PhantomSpawn spawn : activeSpawns) {
            idleSpawns.add(spawn);
        }
        activeSpawns.clear();
    }

    /**
     * The Spawn Stage of the boss fight
     * Creates phantoms and sets their movement vector
     */
    private void spawnSequence() {
        if (phantoms.isEmpty()) {
            setMovementVector(0, -2.0f);

            phantoms.add(new Phantom(position.x, position.y, getCollisionBox().getWidth(), getCollisionBox().getHeight(), mapScreen));
            phantoms.add(new Phantom(position.x, position.y, getCollisionBox().getWidth(), getCollisionBox().getHeight(), mapScreen));
            phantoms.add(new Phantom(position.x, position.y, getCollisionBox().getWidth(), getCollisionBox().getHeight(), mapScreen));
            phantoms.add(new Phantom(position.x, position.y, getCollisionBox().getWidth(), getCollisionBox().getHeight(), mapScreen));
            phantoms.add(new Phantom(position.x, position.y, getCollisionBox().getWidth(), getCollisionBox().getHeight(), mapScreen));

            for (Phantom phantom : phantoms) {
                phantom.stage = Phantom.phantomStages.SPAWN;
            }

            phantoms.get(0).setMovementVector(3.0f, -1.8f);
            phantoms.get(1).setMovementVector(-3.0f, -1.8f);
            phantoms.get(2).setMovementVector(0, 3.0f);
            phantoms.get(3).setMovementVector(3.0f, 2.2f);
            phantoms.get(4).setMovementVector(-3.0f, 2.2f);
        }

        if (phantomsIdle()) {
            stage = bossStages.PHANTOM;
        }
    }

    /**
     * Checks if all the phantoms are currently idle
     */
    private boolean phantomsIdle() {
        boolean phantomsIdle = true;
        for (Phantom phantom : phantoms) {
            phantom.update();
            if (phantom.stage != Phantom.phantomStages.PHANTOM_IDLE) {
                phantomsIdle = false;
            }
        }
        return phantomsIdle;
    }

    /**
     * The Phantom Stage of the boss fight
     * Assigns a random spawn to each Phantom/PhantomBoss
     * Phantoms charge down the screen and Boss is hurt by arrow
     */
    private void phantomSequence() {
        // create the list of spawns
        if (idleSpawns.isEmpty() && activeSpawns.isEmpty()) {
            idleSpawns.add(new PhantomSpawn(100, 30));
            idleSpawns.add(new PhantomSpawn(140, 30));
            idleSpawns.add(new PhantomSpawn(180, 30));
            idleSpawns.add(new PhantomSpawn(220, 30));
            idleSpawns.add(new PhantomSpawn(260, 30));
            idleSpawns.add(new PhantomSpawn(300, 30));
        }

        // check if health is low enough to move on to next stage
        if (getCurrentHealth() <= 400) {
            stage = bossStages.TENNIS;
            currentHealth = 400;
            for (Phantom phantom : phantoms) {
                phantom.stage = Phantom.phantomStages.PHANTOM_IDLE;
            }
            return;
        }

        // set movespeed + direction of boss + phantoms
        movementVector.set(0, 2);
        direction = Direction.DOWN;
        for (Phantom phantom : phantoms) {
            phantom.setMovementVector(0, 2);
            phantom.direction = Direction.DOWN;
        }

        // the actual phase
        if (pauseTimer == 0) {
            if (activeSpawns.isEmpty()) {
                if (getCurrentHealth() <= 500) {
                    // create actual boss
                    Random random = new Random();
                    int i = random.nextInt(idleSpawns.size());

                    // set the boss spawn to the randomly chosen one
                    setSpawn(idleSpawns.get(i));
                    idleSpawns.get(i).setSpawnUsed(true);

                    // move the boss to the spawn
                    position.x = idleSpawns.get(i).spawnPos.x;
                    position.y = idleSpawns.get(i).spawnPos.y;

                    // move the spawn to the active list
                    activeSpawns.add(idleSpawns.get(i));
                    idleSpawns.remove(i);

                    // create 2 phantoms
                    setupPhantom(0);
                    setupPhantom(1);
                }
                if (getCurrentHealth() <= 475) {
                    // create a phantom
                    setupPhantom(2);
                }
                if (getCurrentHealth() <= 450) {
                    // create a phantom
                    setupPhantom(3);
                }
                if (getCurrentHealth() <= 425) {
                    // create a phantom
                    setupPhantom(4);
                }
            } else {
                if (waitTimer == 0) {
                    if (checkOutOfBounds()) {
                        waitTimer = waitLimit;
                        pauseTimer = pauseLimit;
                        for (Phantom phantom : phantoms) {
                            phantom.waitTimer = waitLimit;
                        }
                        emptyActiveSpawns();
                    } else {
                        move(movementVector);
                        for (Phantom phantom : phantoms) {
                            phantom.update();
                        }
                        if (checkArrowCollision()) {
                            waitTimer = waitLimit;
                            pauseTimer = pauseLimit;
                            stage = bossStages.FLINCHING;
                            for (Phantom phantom : phantoms) {
                                phantom.stage = Phantom.phantomStages.PHANTOM_IDLE;
                                phantom.waitTimer = waitLimit;
                            }
                        }
                        checkPhantomPhasePlayerCollision();
                    }
                } else {
                    for (Phantom phantom : phantoms) {
                        phantom.waitTimer--;
                    }
                    waitTimer--;
                }
            }
        } else {
            pauseTimer--;
        }
    }

    /**
     * Setup the phantom with one of the idle spawns
     *
     * @param phantomNumber The number of the phantom in the ArrayList
     */
    private void setupPhantom(int phantomNumber) {
        Random random = new Random();
        int i = random.nextInt(idleSpawns.size());

        // set the phantom spawn to the randomly chosen one
        phantoms.get(phantomNumber).stage = Phantom.phantomStages.PHANTOM_ACTIVE;
        phantoms.get(phantomNumber).setSpawn(idleSpawns.get(i));
        idleSpawns.get(i).setSpawnUsed(true);

        // move the phantom to the spawn
        phantoms.get(phantomNumber).position.x = idleSpawns.get(i).spawnPos.x;
        phantoms.get(phantomNumber).position.y = idleSpawns.get(i).spawnPos.y;

        // move the spawn to the active list
        activeSpawns.add(idleSpawns.get(i));
        idleSpawns.remove(i);
    }

    /**
     * Sets the movement vector to a specific value
     *
     * @param xVec The x value of the vector
     * @param yVec The y value of the vector
     */
    void setMovementVector(float xVec, float yVec) {
        movementVector.set(xVec, yVec);
        movementVector.normalise();
        movementVector.multiply(moveSpeed);
    }

    /**
     * Sets the Phantom Spawn of the entity
     *
     * @param spawn is the spawn thats being assigned
     */
    void setSpawn(PhantomSpawn spawn) {
        this.spawn = spawn;
    }

    /**
     * The Tennis Stage of the boss fight
     * Boss moves left and right and generates tennis balls
     * Tennis balls are bounced back and forth until it hits either player or boss
     */
    private void tennisSequence() {
        // update return chance based on missing health
        if (getCurrentHealth() < 300) {
            returnChance = 50;
            ballSpeed = 1.5f;
        }
        if (getCurrentHealth() < 200) {
            returnChance = 75;
            ballSpeed = 2.0f;
        }
        if (getCurrentHealth() < 100) {
            returnChance = 85;
            ballSpeed = 2.5f;
        }

        if (!tennisFence.exists) {
            tennisFence.setExists(true);
            if (mapScreen.getPlayer().position.y > tennisFence.position.y) {
                Vector2 vector = new Vector2(200, 40);
                path = AIUtil.FindPath(position, vector, mapScreen.tileMap.getPathGrid());
            } else {
                Vector2 vector = new Vector2(200, 110);
                path = AIUtil.FindPath(position, vector, mapScreen.tileMap.getPathGrid());
            }
        } else {
            if (!path.isEmpty()) {
                updateMovement();
            } else {
                if (tennisTimer == 0 && tennisBalls.isEmpty()) {
                    tennisBalls.add(new TennisBall(position.x, position.y, ballSpeed, mapScreen));
                    movementVector.set(0, 0);
                } else if (tennisTimer != 0) {
                    if (directionTimer == 0) {
                        Random random = new Random();
                        if (random.nextInt(2) == 1) {
                            movementVector.set(1.5f, 0);
                        } else {
                            movementVector.set(-1.5f, 0);
                        }
                        directionTimer = directionLimit;
                    } else {
                        directionTimer--;
                    }
                    tennisTimer--;
                    move(movementVector);
                    checkTileCollision();
                }
            }
        }

        Iterator<TennisBall> tennisBallIterator = tennisBalls.iterator();
        while (tennisBallIterator.hasNext()) {
            TennisBall tennisBall = tennisBallIterator.next();
            if (tennisBall.isAlive()) {
                tennisBall.update(returnChance);
            } else {
                tennisBallIterator.remove();
            }
        }
    }

    /**
     * The Stunned Phase for the boss fight
     * The boss can be damaged by the players sword
     */
    private void stunnedSequence() {
        // remove the fence so player can reach
        tennisFence.setExists(false);

        // adjust the stun timer
        if (stunTimer == 0) {
            stunTimer = stunLimit;
            stage = bossStages.TENNIS;
        } else {
            stunTimer--;
        }

        if (checkTennisPhasePlayerCollision()) {
            takeDamage(mapScreen.getPlayer().damage);
        }

        Log.e("HEALTH", getCurrentHealth() + " ");

        if (getCurrentHealth() <= 0) {
            stage = bossStages.DEAD;
            finalDeath.play(false);
        }
    }

    /**
     * The Death Stage for the boss fight
     * The boss moves to the center of its area and dies
     */
    private void deathSequence() {
        if (!deadPathGenerated) {
            deadPathGenerated = true;
            path = AIUtil.FindPath(position, spawnPoint, mapScreen.tileMap.getPathGrid());
        }
        if (!path.isEmpty()) {
            updateMovement();
        } else {
            if (deathTimer == 0) {
                mapScreen.setSave(true);
                victorySound.play(false);
                state = entityState.DEAD;
            } else {
                deathTimer--;
            }
        }
    }

    /**
     * Moves the Phantom to a new location
     *
     * @param vector The vector by which to update the Phantoms position
     */
    protected void move(Vector2 vector) {
        updatePosition(vector.x, vector.y);
    }

    /**
     * Updates the Phantom position using the seek method to follow a path
     */
    private void updateMovement() {
        BoundingBox bound = getCollisionBox();
        if (bound.contains(path.get(0).x, path.get(0).y)) {
            path.remove(0);
            if (!path.isEmpty()) {
                movementVector = SteeringUtil.seek(position, path.get(0), moveSpeed);
            } else {
                movementVector.set(0, 0);
            }
        }
        move(movementVector);
    }

    /**
     * Updates the direction the Phantom is currently facing for drawing purposes
     */
    void updateDirection() {
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
            direction = Direction.LEFT;
        } else {
            direction = Direction.RIGHT;
        }
    }

    /**
     * Checks if the Phantoms are out of bounds
     *
     * @return true if the Phantom is out of bounds
     */
    boolean checkOutOfBounds() {
        return !getCollisionBox().intersects(bossArea);
    }

    /**
     * Checks if the Phantom has been hit by an arrow
     *
     * @return true if the Phantom has been hit
     */
    boolean checkArrowCollision() {
        for (Item item : mapScreen.items) {
            if (item.itemName.equals(Inventory.AmmoType.ARROW.toString())) {
                Arrow arrow = (Arrow) item;
                CollisionDetector.CollisionType collisionType = CollisionDetector.determineCollisionType(this, arrow, false);

                if (collisionType != CollisionDetector.CollisionType.None) {
                    if (arrow.getDirection() == Direction.UP) {
                        arrow.setAlive(false);
                        bounceBack.set(0, -2);
                        takeDamage(arrow.damage);
                        hitByArrow.play(false);
                        return true;
                    }
                }

                if (collisionType != CollisionDetector.CollisionType.None && collisionType != CollisionDetector.CollisionType.Top) {
                    // message saying can only hurt him with an arrow to the face
                }
            }
        }
        return false;
    }

    /**
     * Checks for if the Phantom has collided with the player and deals damage
     */
    void checkPhantomPhasePlayerCollision() {
        Player player = mapScreen.getPlayer();
        CollisionDetector.CollisionType collisionType = CollisionDetector.determineCollisionType(this, player, false);

        if (collisionType != CollisionDetector.CollisionType.None) {
            Random random = new Random();
            int i = random.nextInt(2);
            player.state = entityState.FLINCHING;
            if (i == 1) {
                player.bounceBack.set(-2, 0);
            } else {
                player.bounceBack.set(2, 0);
            }
            player.takeDamage(damage);
        }
    }

    /**
     * Checks if the Phantom is being hit by the player sword
     *
     * @return true if the player has hit the Phantom
     */
    private boolean checkTennisPhasePlayerCollision() {
        Player player = mapScreen.getPlayer();
        CollisionDetector.CollisionType collisionType = CollisionDetector.determineCollisionType(this, player, false);

        if (collisionType != CollisionDetector.CollisionType.None) {
            if (player.state == entityState.ATTACKING) {
                hitBySword.play(false);
                return true;
            }
        }
        return false;
    }

    /**
     * The draw method for the Phantoms
     *
     * @param canvas         Canvas on which to draw
     * @param layerViewport  Layer Viewport to draw from
     * @param screenViewport Screen Viewport to translate the Layer Viewport
     * @param paint          Paint to apply to the bitmap
     */
    void phantomDraw(Canvas canvas, LayerViewport layerViewport, ScreenViewport screenViewport, Paint paint) {
        if (paint != null) {
            alphaPaint(waitLimit, waitTimer, false);
            super.draw(canvas, layerViewport, screenViewport, paint);
        } else {
            super.draw(canvas, layerViewport, screenViewport, null);
        }
    }

    /**
     * Draws the sprite to the canvas, clipping it if needed
     *
     * @param canvas         Canvas on which to draw
     * @param layerViewport  Layer Viewport to draw from
     * @param screenViewport Screen Viewport to translate the Layer Viewport
     */
    @Override
    public void draw(Canvas canvas, LayerViewport layerViewport, ScreenViewport screenViewport) {
        lockFence.draw(canvas, layerViewport, screenViewport);
        tennisFence.draw(canvas, layerViewport, screenViewport);

        Frame currentFrame = movementAnimation.getFrame(direction);
        bitmap = currentFrame.getFrameBitmap();
        collisionBox = currentFrame.getFrameDrawBox();
        switch (stage) {
            case IDLE:
            case TENNIS:
                super.draw(canvas, layerViewport, screenViewport);
                for (TennisBall tennisBall : tennisBalls) {
                    tennisBall.draw(canvas, layerViewport, screenViewport);
                }
                break;
            case SPAWN:
                super.draw(canvas, layerViewport, screenViewport);
                for (Phantom phantom : phantoms) {
                    phantom.draw(canvas, layerViewport, screenViewport);
                }
                break;
            case FLINCHING:
                long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
                if (elapsed / 100 % 3 == 0) {
                    return;
                }
                super.draw(canvas, layerViewport, screenViewport);
                break;
            case STUNNED:
                if (invincibilityTimer != 0) {
                    long elapsed2 = (System.nanoTime() - flinchTimer) / 1000000;
                    if (elapsed2 / 100 % 3 == 0) {
                        return;
                    }
                }
                super.draw(canvas, layerViewport, screenViewport);
                break;
            case PHANTOM:
                if (pauseTimer == 0) {
                    alphaPaint(waitLimit, waitTimer, false);
                    super.draw(canvas, layerViewport, screenViewport, paint);
                }
                for (Phantom phantom : phantoms) {
                    phantom.draw(canvas, layerViewport, screenViewport);
                }
                break;
            case DEAD:
                if (deathTimer == deathLimit) {
                    long elapsed2 = (System.nanoTime() - flinchTimer) / 1000000;
                    if (elapsed2 / 100 % 3 == 0) {
                        return;
                    }
                }

                alphaPaint(deathLimit, deathTimer, true);
                super.draw(canvas, layerViewport, screenViewport, paint);
                break;
        }
    }

    /**
     * Sets the alpha value of the paint based on how long is left on timers
     *
     * @param limit   The upper limit of the timer
     * @param timer   The time that remains on the timer
     * @param inverse Inverse to fade from solid to transparent
     */
    private void alphaPaint(int limit, int timer, boolean inverse) {
        int paintAlpha;
        int alphaIncrements = 3;
        if (!inverse) {
            paintAlpha = alphaIncrements * (limit - timer);
            if (paintAlpha > 255) {
                paintAlpha = 255;
            }
        } else {
            paintAlpha = 255 - (alphaIncrements * (limit - timer));
            if (paintAlpha < 0) {
                paintAlpha = 0;
            }
        }

        paint.setAlpha(paintAlpha);
    }
}