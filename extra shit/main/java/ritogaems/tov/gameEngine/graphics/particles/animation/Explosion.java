package ritogaems.tov.gameEngine.graphics.particles.animation;


import ritogaems.tov.gameEngine.audio.SoundEffect;
import ritogaems.tov.gameEngine.entity.Enemy;
import ritogaems.tov.gameEngine.entity.Entity;
import ritogaems.tov.gameEngine.entity.Player;
import ritogaems.tov.util.CollisionDetector;
import ritogaems.tov.world.screens.MapScreen;

public class Explosion extends ParticleAnimation {

    /**
     * Damage the explosion deals
     */
    private int damage = 40;

    /**
     * Map Screen in which this explosion exists
     */
    private MapScreen mapScreen;

    /**
     * Standard Constructors
     *
     * @param x             X position
     * @param y             Y position
     * @param drawWidth     Drawing width
     * @param drawHeight    Drawing height
     * @param mapScreen     Map Screen in which this explosion exists
     */
    public Explosion(float x, float y, float drawWidth, float drawHeight, MapScreen mapScreen) {
        super("explosion2", // name
                x,
                y,
                drawWidth,
                drawHeight,
                "img/Particles/Explosion2.png",
                23,  // number of frames
                5,   // row index
                5,   // number of rows
                mapScreen.getGame().getAssetStore()
        );
        // could change this to an arraylist of entities?

        this.mapScreen = mapScreen;

        checkEnemyCollision();
        checkPlayerCollision();

        // Initialise Sound effects
        SoundEffect soundEffect = mapScreen.getGame().getAssetStore().getSfx("explosion2", "audio/sfx/explosion2.wav");
        soundEffect.playWithRelativeVolume(mapScreen.layerViewport, position);
    }


    /**
     * @author Zoey Longridge
     * If explosion collides with a enemy, knock the enemy back and apply damage
     */
    private void checkEnemyCollision(){
        for (Enemy enemy : mapScreen.enemies) {
            CollisionDetector.CollisionType collisionType = CollisionDetector.determineCollisionType(enemy, this, true);
            switch (collisionType) {
                case Top:
                    enemy.state = Entity.entityState.FLINCHING;
                    enemy.bounceBack.set(0,1);
                    enemy.takeDamage(damage);
                    break;
                case Bottom:
                    enemy.state = Entity.entityState.FLINCHING;
                    enemy.bounceBack.set(0,-1);
                    enemy.takeDamage(damage);
                    break;
                case Right:
                    enemy.state = Entity.entityState.FLINCHING;
                    enemy.bounceBack.set(-1,0);
                    enemy.takeDamage(damage);
                    break;
                case Left:
                    enemy.state = Entity.entityState.FLINCHING;
                    enemy.bounceBack.set(1,0);
                    enemy.takeDamage(damage);
                    break;
                case None:
                    break;
            }}
    }

    /**
     * @author Zoey Longridge
     * If explosion collides with a player, knock the player back and apply damage
     */
    private void checkPlayerCollision(){
        Player player = mapScreen.getPlayer();
        CollisionDetector.CollisionType collisionType = CollisionDetector.determineCollisionType(player, this, true);
        switch (collisionType) {
            case Top:
                player.state = Entity.entityState.FLINCHING;
                player.bounceBack.set(0,2);
                player.takeDamage(damage);
                break;
            case Bottom:
                player.state = Entity.entityState.FLINCHING;
                player.bounceBack.set(0,-2);
                player.takeDamage(damage);
                break;
            case Right:
                player.state = Entity.entityState.FLINCHING;
                player.bounceBack.set(-2,0);
                player.takeDamage(damage);
                break;
            case Left:
                player.state = Entity.entityState.FLINCHING;
                player.bounceBack.set(2,0);
                player.takeDamage(damage);
                break;
            case None:
                break;
        }

    }
    
}
