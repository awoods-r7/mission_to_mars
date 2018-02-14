package ritogaems.tov.gameEngine.graphics.particles.animation;


import ritogaems.tov.gameEngine.ElapsedTime;
import ritogaems.tov.gameEngine.entity.Direction;
import ritogaems.tov.gameEngine.items.Item;
import ritogaems.tov.util.CollisionDetector;
import ritogaems.tov.world.screens.MapScreen;

/**
 * @author Darren
 */
public class AirInhale extends ParticleAnimation {

    /**
     * MapScreen to which this object belongs to
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
     * @param y             Y position
     * @param drawWidth     Drawing Width
     * @param drawHeight    Drawing Height
     * @param mapScreen     MapScreen to which this belongs
     * @param direction     Direction
     * @param name          Name of the particle animation
     * @param timeToLive    Time for the particle animation to stay active
     */
    public AirInhale(float x, float y, float drawWidth, float drawHeight, MapScreen mapScreen, Direction direction, String name, float timeToLive) {
        super(name,
                x,
                y,
                drawWidth,
                drawHeight,
                "img/Particles/AirInhale/" + name + ".png",
                5,
                5,
                1,
                mapScreen.getGame().getAssetStore());

        this.mapScreen = mapScreen;
        this.direction = direction;
        this.timeToLive = timeToLive;
    }

    /**
     * Updates the counter for time to live, and checks for item collision
     *
     * @param elapsedTime Time since the last update
     */
    @Override
    public void update(ElapsedTime elapsedTime) {
        timeToLiveCounter += elapsedTime.stepTime;
        checkItemCollision();

        if (timeToLiveCounter >= timeToLive) {
            setAlive(false);
        }
    }

    /**
     * Checks for collision with items, and draws them towards the base of the animation
     */
    private void checkItemCollision(){
        //Collision for items
        for (Item item : mapScreen.items) {
            CollisionDetector.CollisionType collisionType = CollisionDetector.determineCollisionType(item, this, false);

            if (collisionType != CollisionDetector.CollisionType.None) {
                switch (direction) {
                    case UP:
                        item.position.add(0, 2.0f);
                        break;
                    case DOWN:
                        item.position.add(0, -2.0f);
                        break;
                    case RIGHT:
                        item.position.add(-2.0f, 0);
                        break;
                    case LEFT:
                        item.position.add(2.0f, 0);
                        break;
                }
            }
        }
    }
}
