package ritogaems.tov.gameEngine.entity.bosses;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import ritogaems.tov.ai.AIUtil;
import ritogaems.tov.gameEngine.AssetStore;
import ritogaems.tov.gameEngine.ElapsedTime;
import ritogaems.tov.gameEngine.audio.SoundEffect;
import ritogaems.tov.gameEngine.entity.Direction;
import ritogaems.tov.gameEngine.entity.Entity;
import ritogaems.tov.gameEngine.entity.Player;
import ritogaems.tov.gameEngine.graphics.animation.DirectedAnimation;
import ritogaems.tov.gameEngine.graphics.animation.Frame;
import ritogaems.tov.gameEngine.graphics.particles.animation.AirInhale;
import ritogaems.tov.gameEngine.graphics.particles.animation.Explosion;
import ritogaems.tov.gameEngine.graphics.particles.animation.FireBreath;
import ritogaems.tov.gameEngine.graphics.tilemapEngine.Tile;
import ritogaems.tov.gameEngine.graphics.tilemapEngine.TileMap;
import ritogaems.tov.gameEngine.graphics.tilemapEngine.TileSheet;
import ritogaems.tov.gameEngine.items.Bomb;
import ritogaems.tov.gameEngine.items.Item;
import ritogaems.tov.util.BoundingBox;
import ritogaems.tov.util.CollisionDetector;
import ritogaems.tov.util.SteeringUtil;
import ritogaems.tov.util.Vector2;
import ritogaems.tov.world.screens.MapScreen;
import ritogaems.tov.world.viewports.LayerViewport;
import ritogaems.tov.world.viewports.ScreenViewport;

/**
 * @author Darren
 */
public class Dodongo extends Entity {

    //////////////////////////////////
    //  Properties
    //////////////////////////////////

    /**
     * Enum defining the states of
     */
    private enum DodongoState {
        SPAWN, WALK, INHALE_AIR, EXHALE_FIRE, EAT_BOMB, FLINCH, DEAD
    }

    /**
     * Enum defining what trajectory trough the beacons
     * Dodongo will take
     */
    private enum TrajectoryDirection {
        CLOCKWISE, COUNTERCLOCKWISE
    }

    /**
     * Dodongo Mouth Open animation
     */
    protected DirectedAnimation mouthOpen;

    /**
     * Counter determining how long to hold the mouth open animation
     */
    private int mouthOpenAnimationCounter = 0;

    /**
     * Animation for dodongo eating a bomb
     */
    protected DirectedAnimation bombEaten;

    /**
     * Map of sound effects available to Dodongo
     */
    private HashMap<String, SoundEffect> sfx = new HashMap<>();

    /**
     * Number of frames to skip before a stomp sound effect is played and a counter
     */
    private int stompTime = 8;
    private int stompTimeCounter = 0;

    /**
     * Stream ID for the inhale sound, so it can be stopped
     */
    private int inhaleStreamID = 0;

    /**
     * Instance of the inhaling particle effect, so it can be removed
     */
    AirInhale airInhale;

    /**
     * Dodongo's state and trajectory
     */
    private DodongoState dodongoState;
    private TrajectoryDirection trajectoryDirection;

    //-------------- SPAWN

    /**
     * Area the player must walk past to trigger the boss fight
     */
    private BoundingBox triggerArea = new BoundingBox(475, 235, 45, 5);

    //-------------- MOVING

    /**
     * Amount of time Dodongo will remain in the WALK state, and counter
     */
    private float walkTimerCap = 5.0f;
    private float walkTimer = 0;

    /**
     * AI path for Dodongo to move between beacons
     */
    private ArrayList<Vector2> path = new ArrayList<>();

    /**
     * Vector by which to move by
     */
    private Vector2 movementVector = new Vector2();

    /**
     * List of beacons which are used to WALK to
     */
    private ArrayList<Vector2> cornerBeacons = new ArrayList<>();

    /**
     * Index of the next beacon to travel to
     * Initialised as -1 for initial state change
     */
    private int beaconIndex = -1;

    /**
     * Whether the next becon has been rached or not
     */
    private boolean reachedBeacon = true;

    //-------------- INHALING

    /**
     * Delay time before beginning the inhaling action, with a counter
     */
    private float inhaleDelay = 2.0f;
    private float inhaleDelayCounter = 0.0f;

    /**
     * Amount of time dodongo will remain in INHALE, with a counter
     */
    private float inhaleTime = 3.0f;
    private float inhaleTimeCounter = 0.0f;

    //-------------- EXHALING

    /**
     * Amount of time dodongo will remain in EXHALE, with a counter
     */
    private float exhaleTime = 4.0f;
    private float exhaleTimeCounter = 0.0f;

    //-------------- BOMB EATEN

    /**
     * Time dodongo will remain in the BOMB EATEN state, with a counter
     */
    private float bombEatenTime = 3.0f;
    private float bombEatenTimeCounter = 0.0f;

    //-------------- DEATH

    /**
     * Delay before dodongo dies and is removed from the game, with a counter
     */
    private float deathTime = 5.0f;
    private float deathTimeCounter = 0.0f;


    //////////////////////////////////
    //  Constructors
    //////////////////////////////////

    /**
     * Creates an instance of Dodongo
     *
     * @param xPos      X position
     * @param yPos      Y position
     * @param mapScreen Map Screen to which Dodongo belongs
     */
    public Dodongo(float xPos, float yPos, MapScreen mapScreen) {
        super(xPos, yPos, 1, 1, 35, 35, 500, 500, 50, mapScreen);

        AssetStore mAssetStore = mapScreen.getGame().getAssetStore();

        // Initialise animations
        String name = "DodongoMovement";
        int noOfFrames = 3;
        Bitmap spriteSheet = mAssetStore.getBitmap(name, "img/Enemies/Bosses/Dodongo/DodongoMovement.png");
        movementAnimation = new DirectedAnimation(mAssetStore, name, spriteSheet, noOfFrames, 40, 2);

        name = "DodongoMouthOpen";
        noOfFrames = 2;
        spriteSheet = mAssetStore.getBitmap(name, "img/Enemies/Bosses/Dodongo/DodongoMouthOpen.png");
        mouthOpen = new DirectedAnimation(mAssetStore, name, spriteSheet, noOfFrames, 40);

        name = "DodongoBombEaten";
        noOfFrames = 1;
        spriteSheet = mAssetStore.getBitmap(name, "img/Enemies/Bosses/Dodongo/DodongoBombEaten.png");
        bombEaten = new DirectedAnimation(mAssetStore, name, spriteSheet, noOfFrames, 40);

        //Initial setup for animation
        Frame startFrame = movementAnimation.getFrame(Direction.DOWN);
        this.bitmap = startFrame.getFrameBitmap();
        this.collisionBox = startFrame.getFrameDrawBox();

        //Independent variable initialisation
        moveSpeed = 1.0f;
        direction = Direction.DOWN;
        dodongoState = DodongoState.SPAWN;
        trajectoryDirection = TrajectoryDirection.COUNTERCLOCKWISE;

        //Initialise beacons
        cornerBeacons.add(new Vector2(360, 210));
        cornerBeacons.add(new Vector2(580, 210));
        cornerBeacons.add(new Vector2(580, 50));
        cornerBeacons.add(new Vector2(360, 50));

        //Initialise and store Sound effects
        sfx.put("Dodongostomp", mapScreen.getGame().getAssetStore().getSfx("Dodongostomp", "audio/sfx/Dodongo/stomp.wav"));
        sfx.put("Dodongoinhale", mapScreen.getGame().getAssetStore().getSfx("Dodongoinhale", "audio/sfx/Dodongo/inhale.wav"));
        sfx.put("Dodongoexhale", mapScreen.getGame().getAssetStore().getSfx("Dodongoexhale", "audio/sfx/Dodongo/exhale.wav"));
        sfx.put("Dodongohit", mapScreen.getGame().getAssetStore().getSfx("Dodongohit", "audio/sfx/Dodongo/hit.wav"));
        sfx.put("Dodongodeath", mapScreen.getGame().getAssetStore().getSfx("Dodongodeath", "audio/sfx/Dodongo/death.wav"));
        sfx.put("Dodongoswallow", mapScreen.getGame().getAssetStore().getSfx("Dodongoswallow", "audio/sfx/Dodongo/swallow.wav"));
    }

    /**
     * Draws Dodongo based on his state
     *
     * @param canvas         Canvas on which to draw
     * @param layerViewport  Layer Viewport to draw from
     * @param screenViewport Screen Viewport to translate the Layer Viewport
     */
    @Override
    public void draw(Canvas canvas, LayerViewport layerViewport, ScreenViewport screenViewport) {
        Frame frame;
        switch(dodongoState) {
            case WALK:
                frame = movementAnimation.getFrame(direction);
                bitmap = frame.getFrameBitmap();
                collisionBox = frame.getFrameDrawBox();
                break;
            case INHALE_AIR:
                if (mouthOpenAnimationCounter <= 1) {
                    frame = mouthOpen.getFrame(direction);
                    bitmap = frame.getFrameBitmap();
                    collisionBox = frame.getFrameDrawBox();
                    mouthOpenAnimationCounter++;
                }
                break;
            case EAT_BOMB:
                frame = bombEaten.getFrame(direction);
                bitmap = frame.getFrameBitmap();
                collisionBox = frame.getFrameDrawBox();
                break;
            case FLINCH:
                frame = movementAnimation.getFrame(Direction.CENTER);
                bitmap = frame.getFrameBitmap();
                collisionBox = frame.getFrameDrawBox();

                long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
                if (elapsed / 100 % 2 == 0) {
                    return;
                }
                break;
            case DEAD:
                if (mouthOpenAnimationCounter <= 1) {
                    frame = mouthOpen.getFrame(direction);
                    bitmap = frame.getFrameBitmap();
                    collisionBox = frame.getFrameDrawBox();
                    mouthOpenAnimationCounter++;
                }
                break;
        }

        super.draw(canvas, layerViewport, screenViewport);
    }

    /**
     * Updates dodongo based on his state
     *
     * @param elapsedTime   Used to time animations/updates
     */
    public void update(ElapsedTime elapsedTime) {
        switch(dodongoState) {
            case SPAWN:
                spawnSequence();
                break;
            case WALK:
                walk(elapsedTime);
                break;
            case INHALE_AIR:
                inhaleAir(elapsedTime);
                break;
            case EXHALE_FIRE:
                exhaleFire(elapsedTime);
                break;
            case EAT_BOMB:
                eatBomb(elapsedTime);
                break;
            case FLINCH:
                flinch();
                break;
            case DEAD:
                deathSequence(elapsedTime);
                break;
        }

        updateDirection();
        checkPlayerWalkInCollision();
    }

    /**
     * Initiates Dodongo's spawn sequence if the player crosses the trigger area.
     * The player is then enclosed in the boss area by new tiles being added. The
     * background music changes, and dodongo moves on to his WALK state.
     */
    private void spawnSequence() {
        if (triggerArea.contains(mapScreen.getPlayer().position.x, mapScreen.getPlayer().position.y)) {
            TileMap tileMap = mapScreen.tileMap;
            for (int x = 43; x < 52; x++) {
                tileMap.addTile(new Tile(-5, x * TileSheet.TILE_SIZE + 5, 240 + 5, x, 24, tileMap.getTileSheet().getTileBitmap(115)), 1);
            }

            mapScreen.changeBackgroundMusic("BossBattle","audio/bgm/BossFights/BossBattle.mp3", true);
            dodongoState = dodongoState.WALK;
        }
    }

    /**
     * Dodongo will traverse the area using four beacons in each corner by
     * using path finding. Once he reaches it, he moves on to the next. Depending
     * on his trajectory, he will either circle the area clockwise or counter-clockwise.
     * The stomp sound plays.
     * If the walkTimer reaches it's cap, Dodongo moves on to INHALE_AIR state.
     *
     * @param elapsedTime   used to increment the walkTimer
     */
    private void walk(ElapsedTime elapsedTime) {
        Random random = new Random();

        if (walkTimer >= walkTimerCap) {
            dodongoState = DodongoState.INHALE_AIR;
            walkTimer = 0;
            walkTimerCap = random.nextInt(5) + 5.0f;
        } else {
            walkTimer += elapsedTime.stepTime;

            if (reachedBeacon == true) {
                if (trajectoryDirection == TrajectoryDirection.CLOCKWISE) {
                    beaconIndex--;
                    if (beaconIndex < 0) beaconIndex = 3;
                } else {
                    beaconIndex++;
                    if (beaconIndex > 3) beaconIndex = 0;
                }

                path = AIUtil.FindPath(position, cornerBeacons.get(beaconIndex), mapScreen.tileMap.getPathGrid());

                reachedBeacon = false;
            }

            updateMovement();
        }

        if (stompTimeCounter >= stompTime) {
            sfx.get("Dodongostomp").playWithRelativeVolume(mapScreen.layerViewport, position);
            stompTimeCounter = 0;
        } else {
            stompTimeCounter++;
        }
    }

    /**
     * After a delay, dodongo will inhale in front of him by creating an air inhale object.
     * (This object draws all items towards dodongo. If an item is a bomb, dodongo moves on
     * to the BOMB_EATEN state). If the timer runs out, dodongo will enter the EXHALE_FIRE state.
     *
     * @param elapsedTime   used to increment the timers
     */
    private void inhaleAir(ElapsedTime elapsedTime) {
        if (inhaleDelayCounter >= inhaleDelay) {
            if (inhaleTimeCounter == 0) {
                switch (direction) {
                    case UP:
                        airInhale = new AirInhale(position.x, position.y - 55, 20, 80, mapScreen, direction, "airInhaleUp", inhaleTime);
                        mapScreen.addParticleEffect(airInhale);
                        break;
                    case DOWN:
                        airInhale = new AirInhale(position.x, position.y + 55, 20, 80, mapScreen, direction, "airInhaleDown", inhaleTime);
                        mapScreen.addParticleEffect(airInhale);
                        break;
                    case RIGHT:
                        airInhale = new AirInhale(position.x + 55, position.y, 80, 20, mapScreen, direction, "airInhaleRight", inhaleTime);
                        mapScreen.addParticleEffect(airInhale);
                        break;
                    case LEFT:
                        airInhale = new AirInhale(position.x - 55, position.y, 80, 20, mapScreen, direction, "airInhaleLeft", inhaleTime);
                        mapScreen.addParticleEffect(airInhale);
                        break;
                }

                inhaleStreamID = sfx.get("Dodongoinhale").playWithRelativeVolume(mapScreen.layerViewport, position);
            }

            if (inhaleTimeCounter >= inhaleTime) {
                dodongoState = DodongoState.EXHALE_FIRE;
                inhaleTimeCounter = 0;
                inhaleDelayCounter = 0;
            } else {
                inhaleTimeCounter += elapsedTime.stepTime;
                checkBombCollision();
            }

        } else {
            inhaleDelayCounter += elapsedTime.stepTime;
        }
    }

    /**
     * Dodongo will exhale fire by creating a FireBreath instance in front of him. If it
     * collides with the player, they are knocked back and take damage. After the timer
     * runs out, dodongo returns to the WALK state.
     *
     * @param elapsedTime Used for the timer
     */
    private void exhaleFire(ElapsedTime elapsedTime) {
        if (exhaleTimeCounter == 0) {
            switch (direction) {
                case UP:
                    mapScreen.addParticleEffect(new FireBreath(position.x, position.y - 55, 20, 80, mapScreen, direction, "fireBreathUp", exhaleTime));
                    break;
                case DOWN:
                    mapScreen.addParticleEffect(new FireBreath(position.x, position.y + 55, 20, 80, mapScreen, direction, "fireBreathDown", exhaleTime));
                    break;
                case RIGHT:
                    mapScreen.addParticleEffect(new FireBreath(position.x + 55, position.y, 80, 20, mapScreen, direction, "fireBreathRight", exhaleTime));
                    break;
                case LEFT:
                    mapScreen.addParticleEffect(new FireBreath(position.x - 55, position.y, 80, 20, mapScreen, direction, "fireBreathLeft", exhaleTime));
                    break;
            }

            sfx.get("Dodongoinhale").stop(inhaleStreamID);
            sfx.get("Dodongoexhale").playWithRelativeVolume(mapScreen.layerViewport, position);
        }

        if (exhaleTimeCounter >= exhaleTime) {
            exhaleTimeCounter = 0;
            mouthOpenAnimationCounter = 0;
            dodongoState = DodongoState.WALK;
        } else {
            exhaleTimeCounter += elapsedTime.stepTime;
        }
    }

    /**
     * Dodongo will remain stationary, and after a delay he will create an instance of
     * explosion and take damage. If his health depletes to 0 or below, he initiates the
     * DEATH state. If not, he initiates the FLINCH state.
     *
     * @param elapsedTime
     */
    private void eatBomb(ElapsedTime elapsedTime) {
        if (bombEatenTimeCounter == 0) sfx.get("Dodongoswallow").playWithRelativeVolume(mapScreen.layerViewport, position);
        if (bombEatenTimeCounter >= bombEatenTime) {
            mapScreen.addParticleEffect(new Explosion(position.x, position.y, 45, 45, mapScreen));
            bombEatenTimeCounter = 0;
            takeDamage(100);

            if (currentHealth <= 0) {
                dodongoState = DodongoState.DEAD;
            } else {
                sfx.get("Dodongohit").playWithRelativeVolume(mapScreen.layerViewport, position);
                dodongoState = DodongoState.FLINCH;
                reachedBeacon = true;
            }
        } else {
            bombEatenTimeCounter += elapsedTime.stepTime;
        }
    }

    /**
     * Dodongo flinches for a brief delay, and moves on to the WALK state
     */
    @Override
    public void flinch() {
        if (flinchTimer > 30) {
            dodongoState = DodongoState.WALK;
            flinchTimer = 0;
        } else {
            flinchTimer++;
        }
    }

    /**
     * Dodongo will create an explosion and disappear after a delay. The map will
     * then revert to normal, the background music will change, and Dodongo will
     * be garbage collected.
     *
     * @param elapsedTime Used for the timer
     */
    private void deathSequence(ElapsedTime elapsedTime) {
        if (deathTimeCounter == 0) sfx.get("Dodongodeath").playWithRelativeVolume(mapScreen.layerViewport, position);

        if (deathTimeCounter >= deathTime) {
            mapScreen.addParticleEffect(new Explosion(position.x, position.y, 45, 45, mapScreen));

            TileMap tileMap = mapScreen.tileMap;
            for (int x = 43; x < 52; x++) {
                tileMap.removeTile(x * TileSheet.TILE_SIZE + 5, 240 + 5, 1);
            }

            mapScreen.changeBackgroundMusic("BossClear","audio/bgm/BossFights/BossClear.mp3", true);

            state = entityState.DEAD;

        } else {
            deathTimeCounter += elapsedTime.stepTime;
        }
    }

    /**
     * Decrements Dodongo's health by the damage taken, and reverses his trajectory direction
     *
     * @param damage    The damage by which to decrement
     */
    @Override
    public void takeDamage(int damage) {
        if (trajectoryDirection == TrajectoryDirection.COUNTERCLOCKWISE) {
            trajectoryDirection = TrajectoryDirection.CLOCKWISE;
        } else {
            trajectoryDirection = TrajectoryDirection.COUNTERCLOCKWISE;
        }

        currentHealth -= damage;
        if (currentHealth < 0) {
            currentHealth = 0;
        }
    }

    /**
     * Updates dodongos position
     *
     * @param vector    Vector by which to update
     */
    public void move(Vector2 vector) {
        updatePosition(vector.x, vector.y);
    }

    /**
     * Updates Dodongos position by traversing the AI path.
     * If dodongo's collision box intersects with the target point,
     * he has reached the beacon
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
            reachedBeacon = true;
        }
    }

    /**
     * Updates dodongo's direction based on his movement vector
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
     * Checks for collision if the player walks into Dodongo.
     * The player will take damage and be knocked back.
     */
    private void checkPlayerWalkInCollision() {
        Player player = mapScreen.getPlayer();
        CollisionDetector.CollisionType collisionType = CollisionDetector.determineCollisionType(this, player, false);

        if (collisionType != CollisionDetector.CollisionType.None) {
            player.state = entityState.FLINCHING;
            if (this.direction == Direction.DOWN || this.direction == direction.UP) {
                player.bounceBack.set(-2, 0);
            } else {
                player.bounceBack.set(0, -2);
            }

            player.takeDamage(15);
        }
    }

    /**
     * Checks for collision with a bomb. If there is one,
     * it gets defused and dodongo is updated to the EAT_BOMB state.
     */
    private void checkBombCollision() {
        //Collision for bombs
        for (Item item : mapScreen.items) {
            if (item instanceof Bomb) {
                CollisionDetector.CollisionType collisionType = CollisionDetector.determineCollisionType(item, this, false);

                if (collisionType != CollisionDetector.CollisionType.None) {
                    Bomb bomb = (Bomb) item;
                    bomb.defuse();
                    airInhale.setAlive(false);
                    dodongoState = DodongoState.EAT_BOMB;
                    sfx.get("Dodongoinhale").stop(inhaleStreamID);
                    inhaleTimeCounter = 0;
                    inhaleDelayCounter = 0;
                    mouthOpenAnimationCounter = 0;
                }
            }
        }
    }

}
