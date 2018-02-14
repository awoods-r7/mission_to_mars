package ritogaems.tov.gameEngine.entity;


import android.graphics.Bitmap;

import ritogaems.tov.gameEngine.audio.SoundEffect;
import ritogaems.tov.gameEngine.graphics.animation.DirectedAnimation;
import ritogaems.tov.gameEngine.graphics.animation.Frame;
import ritogaems.tov.gameEngine.graphics.particles.animation.IceBlast;
import ritogaems.tov.gameEngine.graphics.particles.animation.IcePop;
import ritogaems.tov.gameEngine.graphics.particles.generation.ParticleEmitter;
import ritogaems.tov.gameEngine.graphics.tilemapEngine.TileMap;
import ritogaems.tov.gameEngine.items.AmmoDrop;
import ritogaems.tov.gameEngine.items.HealthPotion;
import ritogaems.tov.gameEngine.items.Inventory;
import ritogaems.tov.util.Vector2;
import ritogaems.tov.world.screens.MapScreen;

/**
 * @author Darren
 */
public class Congelus extends Enemy {

    /**
     * Cooldown for Congelus to cast his ice spell, with a counter
     */
    private int spellCooldown = 80;
    private int spellCooldownTimer = 0;

    /**
     * Delay to allow the casting animation to be player, with a counter
     */
    private int castSpellDelay = 15;
    private int castSpellTimer = 0;

    /**
     * Sound effects for Congelus
     */
    private SoundEffect castingSfx;
    private SoundEffect dyingSfx;

    /**
     * Standard constructor
     *
     * @param xPos      X Position
     * @param yPos      Y Position
     * @param tileMap   Tilemap passed up to Enemy for AI
     * @param mapScreen Map Screen to which congelus belongs
     */
    public Congelus(float xPos, float yPos, TileMap tileMap, MapScreen mapScreen) {
        super(xPos, yPos, 10, 15, 10, 15, 200, 200, 0, tileMap, mapScreen);

        // Initialise Animations
        String name = "CongelusMovement";
        int noOfFrames = 3;
        Bitmap walkingSheet = mapScreen.getGame().getAssetStore().getBitmap(name, "img/Enemies/Congelus/CongelusMovement.png");
        movementAnimation = new DirectedAnimation(mapScreen.getGame().getAssetStore(), name, walkingSheet, noOfFrames, 15);

        name = "CongelusCasting";
        noOfFrames = 9;
        Bitmap shootingSheet = mapScreen.getGame().getAssetStore().getBitmap(name, "img/Enemies/Congelus/CongelusCasting.png");
        attackAnimation = new DirectedAnimation(mapScreen.getGame().getAssetStore(), name, shootingSheet, noOfFrames, 17);

        // Initial Setup for animations
        Frame startFrame = movementAnimation.getFrame(Direction.CENTER);
        this.bitmap = startFrame.getFrameBitmap();
        this.collisionBox = startFrame.getFrameDrawBox();

        // Initialise independant variables
        roamCenter = new Vector2(xPos, yPos);
        roamRadius = 30;
        chaseRadius = 40;
        attackRadius = 45;
        moveSpeed = 0.3f;

        // Initialise sound effects
        castingSfx = this.mapScreen.getGame().getAssetStore().getSfx("congelusEncounter", "audio/sfx/Congelus/CongelusEncounter.wav");
        dyingSfx = this.mapScreen.getGame().getAssetStore().getSfx("congelusDying", "audio/sfx/Congelus/CongelusDying.wav");

    }

    /**
     * Updates position
     *
     * @param vector Vector to update by
     */
    public void move(Vector2 vector) {
        updatePosition(vector.x, vector.y);
    }

    /**
     * If the cooldown is over and the animation has finished playing,
     * Congelus creates and IceBlast that follows the player
     */
    public void attack() {
        if (castSpellTimer >= castSpellDelay) {
            //castingSfx.playWithRelativeVolume(mapScreen.layerViewport, position);

            mapScreen.addParticleEffect(new IceBlast(position.x, position.y - 3, 10, 10, mapScreen));

            castSpellTimer = castSpellDelay;
            spellCooldownTimer = spellCooldown;
            attackAnimation.resetFrames();
        } else {
            castSpellTimer++;
        }
    }

    /**
     * Congelus dies, creating an instance of IcePop and dropping a health potion
     */
    public void death() {
        //dyingSfx.playWithRelativeVolume(mapScreen.layerViewport, position);

        mapScreen.addParticleEffect(new IcePop(position.x, position.y, 15, 15, mapScreen));
        mapScreen.addConsumable(new HealthPotion(position.x, position.y, 200, true, mapScreen));
    }

    /**
     * Update Congelus' state based on his position relative to the player, his cooldown and
     * his health.
     *
     * @param player The player, who's position will help determine the state
     */
    @Override
    protected void updateState(Player player) {
        float distance = position.getDistance(player.position);
        if (getCurrentHealth() == 0 || state == entityState.DEAD) {
            state = entityState.DEAD;
        } else if (state != entityState.FLINCHING) {
            if (distance < chaseRadius) {
                if (distance < attackRadius && spellCooldownTimer == 0) {
                    state = entityState.ATTACKING;
                } else {
                    if (state != entityState.CHASING) {
                        chaseTimer = 10;
                    }
                    state = entityState.CHASING;
                }
            } else {
                if (state != entityState.ROAMING) {
                    roamTimer = 270;
                    path.clear();
                }
                state = entityState.ROAMING;
            }
        }
        if (state != entityState.ATTACKING && spellCooldownTimer > 0) {
            spellCooldownTimer--;
        }
    }
}
