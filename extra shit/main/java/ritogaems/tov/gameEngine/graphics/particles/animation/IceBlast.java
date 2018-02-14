package ritogaems.tov.gameEngine.graphics.particles.animation;


import java.util.ArrayList;

import ritogaems.tov.ai.AIUtil;
import ritogaems.tov.gameEngine.ElapsedTime;
import ritogaems.tov.gameEngine.audio.SoundEffect;
import ritogaems.tov.gameEngine.entity.Entity;
import ritogaems.tov.gameEngine.entity.Player;
import ritogaems.tov.util.BoundingBox;
import ritogaems.tov.util.CollisionDetector;
import ritogaems.tov.util.SteeringUtil;
import ritogaems.tov.util.Vector2;
import ritogaems.tov.world.screens.MapScreen;

/**
 * @author Darren
 */
public class IceBlast extends ParticleAnimation {

    /**
     * Map screen in which this exists
     */
    private MapScreen mapScreen;

    /**
     * Position of the player to seek to
     */
    private Vector2 playerPos;

    /**
     * Movement speed of the blast
     */
    private float moveSpeed = 1.75f;

    /**
     * Time for the particle animation to stay active, and a counter
     */
    private float timeToLive = 3.0f;
    private float timeToLiveCounter = 0;

    /**
     * AI path for Dodongo to move between beacons
     */
    private ArrayList<Vector2> path = new ArrayList<>();

    /**
     * Vector by which to move by
     */
    private Vector2 movementVector = new Vector2();

    /**
     * Standard constructor
     *
     * @param x             X position
     * @param y             Y position
     * @param drawWidth     Drawing width
     * @param drawHeight    Drawing height
     * @param mapScreen     Map screen in which this exists
     */
    public IceBlast(float x, float y, float drawWidth, float drawHeight, MapScreen mapScreen) {
        super("IceBlast", // name
                x,
                y,
                drawWidth,
                drawHeight,
                "img/Particles/IceParticles/IceBlast.png",
                5,  // number of frames
                5,   // row index
                1,   // number of rows
                mapScreen.getGame().getAssetStore()
        );

        this.mapScreen = mapScreen;
        this.playerPos = mapScreen.getPlayer().getPosition();

        // Initialise sound effects
        SoundEffect soundEffect = this.mapScreen.getGame().getAssetStore().getSfx("iceBlast", "audio/sfx/IceBlast.wav");
        soundEffect.playWithRelativeVolume(mapScreen.layerViewport, position);
    }

    /**
     * Generates a path, then finds it's way to the target. If it expires, collides
     * with it's target, or collides with the player, it is remove and an instance of
     * IcePop is created
     *
     * @param elapsedTime Time since the last update
     */
    @Override
    public void update(ElapsedTime elapsedTime) {
        if (timeToLiveCounter == 0) path = AIUtil.FindPath(position, playerPos, mapScreen.tileMap.getPathGrid());

        if (timeToLiveCounter >= timeToLive) {
            setAlive(false);
            mapScreen.addParticleEffect(new IcePop(position.x, position.y, 13, 13, mapScreen));
        } else {
            checkPlayerCollision();
            timeToLiveCounter += elapsedTime.stepTime;
            updateMovement();
        }

    }

    /**
     * Updates the vector for the enemy to move by
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
            setAlive(false);
            mapScreen.addParticleEffect(new IcePop(position.x, position.y, 13, 13, mapScreen));
            movementVector.set(0, 0);
        }
    }

    /**
     * Checks collision with the player, and if so, damages them and creates a new
     * instance of IcePop
     */
    private void checkPlayerCollision () {
        Player player = mapScreen.getPlayer();
        CollisionDetector.CollisionType collisionType = CollisionDetector.determineCollisionType(this, player, false);

        if (collisionType != CollisionDetector.CollisionType.None) {
            player.state = Entity.entityState.FLINCHING;
            player.takeDamage(35);

            setAlive(false);
            mapScreen.addParticleEffect(new IcePop(position.x, position.y, 13, 13, mapScreen));
        }
    }
}
