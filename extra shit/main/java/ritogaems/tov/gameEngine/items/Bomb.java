package ritogaems.tov.gameEngine.items;

import ritogaems.tov.gameEngine.AssetStore;
import ritogaems.tov.gameEngine.ElapsedTime;
import ritogaems.tov.gameEngine.audio.LoopableSoundEffect;
import ritogaems.tov.gameEngine.graphics.animation.Animation;
import ritogaems.tov.gameEngine.graphics.animation.Frame;
import ritogaems.tov.gameEngine.graphics.particles.animation.Explosion;
import ritogaems.tov.world.screens.MapScreen;

/**
 * @author Darren
 */
public class Bomb extends Item {

    /**
     * Animation for the bomb
     */
    private Animation animation;

    /**
     * Variables for varying the frame change for the animation
     */
    private double frameChangeWaitTime = 1.0;
    private double frameChangeCurrentTime = 1.0;

    /**
     * Variables for detonating the bomb
     */
    private double detonationTime = 5.0;
    private double currentTime = 0;

    /**
     * Loopable sound effect
     */
    private LoopableSoundEffect loopableSoundEffect;

    /**
     * Standard Constructor
     *
     * @param xPos      X Position
     * @param yPos      Y Position
     * @param mapScreen Map screen which this bomb is in
     */
    public Bomb(float xPos, float yPos, MapScreen mapScreen) {
        super(xPos, yPos, 7.5f, 7.5f, 7.5f, 7.5f, mapScreen);

        itemName = Inventory.AmmoType.BOMB.toString();

        AssetStore assetStore = mapScreen.getGame().getAssetStore();

        // Initialise animation
        animation = new Animation(assetStore, "Bomb", assetStore.getBitmap("Bomb", "img/Items/Bomb.png"), 2, 2, 1, 7.5f);
        Frame startFrame = animation.getCurrentFrame();
        bitmap = startFrame.getFrameBitmap();
        collisionBox = startFrame.getFrameDrawBox();

        // Initialise sound effect
        loopableSoundEffect = new LoopableSoundEffect(assetStore.getSfx("bomb", "audio/sfx/bomb.wav"));
        loopableSoundEffect.play();
    }

    /**
     * Updates the animation frame by making swap more quickly as detonation
     * approached. Also updates the volume of the fuse sound.
     *
     * @param elapsedTime
     */
    @Override
    public void update(ElapsedTime elapsedTime) {
        currentTime += elapsedTime.stepTime;

        if (currentTime >= detonationTime) {
            explode();

            return;
        }

        if (currentTime >= 1.5) {
            if (frameChangeCurrentTime >= frameChangeWaitTime) {
                Frame changeFrame = animation.getCurrentFrame();
                bitmap = changeFrame.getFrameBitmap();
                frameChangeWaitTime *= ((detonationTime - currentTime) / 4);
                frameChangeCurrentTime = 0.0;
            }
            frameChangeCurrentTime += elapsedTime.stepTime;
        }

        loopableSoundEffect.setRelativeVolume(mapScreen.layerViewport, position);
        checkTileCollision();

    }

    /**
     * Detonates the bomb by creating an instance of explosion and removing it
     */
    public void explode() {
        alive = false;
        loopableSoundEffect.stop();
        mapScreen.particleEffects.add(new Explosion(position.x, position.y, 45, 45, mapScreen));
    }

    /**
     * Defuses the bomb, removes it with no explosion
     */
    public void defuse() {
        alive = false;
        loopableSoundEffect.stop();
    }

}
