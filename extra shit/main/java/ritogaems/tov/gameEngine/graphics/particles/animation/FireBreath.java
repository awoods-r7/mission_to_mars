package ritogaems.tov.gameEngine.graphics.particles.animation;

import ritogaems.tov.gameEngine.ElapsedTime;
import ritogaems.tov.gameEngine.entity.Direction;
import ritogaems.tov.gameEngine.entity.Entity;
import ritogaems.tov.gameEngine.entity.Player;
import ritogaems.tov.gameEngine.items.Bomb;
import ritogaems.tov.gameEngine.items.Item;
import ritogaems.tov.util.CollisionDetector;
import ritogaems.tov.world.screens.MapScreen;

/**
 * @author Darren
 */
public class FireBreath extends ParticleAnimation {

    /**
     * Map screen in which this exists
     */
    private MapScreen mapScreen;

    /**
     * Time for the particle animation to stay active, and a counter
     */
    private float timeToLiveCounter;
    private float timeToLive;

    /**
     * Direction of the animation
     */
    Direction direction;

    /**
     * Standard constructor
     *
     * @param x             X position
     * @param y             Y postion
     * @param drawWidth     Drawing width
     * @param drawHeight    Drawing height
     * @param mapScreen     Map screen in which this exists
     * @param direction     Direction
     * @param name          Name of the particle animation
     * @param timeToLive    Time for the particle animation to stay active
     */
    public FireBreath(float x, float y, float drawWidth, float drawHeight, MapScreen mapScreen, Direction direction, String name, float timeToLive) {
        super(name,
                x,
                y,
                drawWidth,
                drawHeight,
                "img/Particles/FireBreath/" + name + ".png",
                11,
                11,
                1,
                mapScreen.getGame().getAssetStore());

        this.mapScreen = mapScreen;
        this.direction = direction;
        this.timeToLive = timeToLive;
    }

    /**
     * Updates the counter for time to live, and checks for bomb and player collision
     *
     * @param elapsedTime Time since the last update
     */
    @Override
    public void update(ElapsedTime elapsedTime) {
        timeToLiveCounter += elapsedTime.stepTime;
        checkPlayerCollision();
        checkBombCollision();

        if (timeToLiveCounter >= timeToLive) {
            setAlive(false);
        }
    }

    /**
     * Checks collision with the player, knocks them back and damages them
     */
    private void checkPlayerCollision () {
        Player player = mapScreen.getPlayer();
        CollisionDetector.CollisionType collisionType = CollisionDetector.determineCollisionType(this, player, false);

        if (collisionType != CollisionDetector.CollisionType.None) {
            player.state = Entity.entityState.FLINCHING;

            switch (direction) {
                case UP:
                    player.bounceBack.set(0, -4);
                    break;
                case DOWN:
                    player.bounceBack.set(0, 4);
                    break;
                case RIGHT:
                    player.bounceBack.set(4, 0);
                    break;
                case LEFT:
                    player.bounceBack.set(-4, 0);
                    break;
            }

            player.takeDamage(25);
        }
    }

    /**
     * Checks collision with bombs and detonates them if there is one
     */
    private void checkBombCollision() {
        //Collision for bombs
        for (Item item : mapScreen.items) {
            if (item instanceof Bomb) {
                CollisionDetector.CollisionType collisionType = CollisionDetector.determineCollisionType(item, this, false);

                if (collisionType != CollisionDetector.CollisionType.None) {
                    Bomb bomb = (Bomb) item;
                    bomb.explode();
                }
            }
        }
    }
}
