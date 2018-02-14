package ritogaems.tov.world.screens;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import ritogaems.tov.DrawLoop;
import ritogaems.tov.UpdateLoop;
import ritogaems.tov.gameEngine.Input.HudGroup;
import ritogaems.tov.gameEngine.entity.BlockPuzzle.Block;
import ritogaems.tov.gameEngine.entity.BlockPuzzle.Chest;
import ritogaems.tov.gameEngine.entity.BlockPuzzle.Door;
import ritogaems.tov.gameEngine.entity.BlockPuzzle.PressurePoint;
import ritogaems.tov.gameEngine.entity.BlockPuzzle.ResetBlocks;
import ritogaems.tov.gameEngine.entity.Enemy;
import ritogaems.tov.gameEngine.entity.Entity;
import ritogaems.tov.gameEngine.entity.Player;
import ritogaems.tov.GameFragment;
import ritogaems.tov.gameEngine.ElapsedTime;
import ritogaems.tov.gameEngine.audio.BackgroundMusic;
import ritogaems.tov.gameEngine.entity.bosses.Dodongo;
import ritogaems.tov.gameEngine.entity.bosses.PhantomBoss;
import ritogaems.tov.gameEngine.entity.BlockPuzzle.PressurePointCollection;
import ritogaems.tov.gameEngine.graphics.StatusBar;
import ritogaems.tov.gameEngine.graphics.particles.IParticleEffect;
import ritogaems.tov.gameEngine.graphics.tilemapEngine.TileMap;
import ritogaems.tov.gameEngine.items.Consumable;
import ritogaems.tov.gameEngine.items.Item;
import ritogaems.tov.util.GraphicsUtil;
import ritogaems.tov.world.MessageMetrics;
import ritogaems.tov.world.Portal;
import ritogaems.tov.world.Sign;
import ritogaems.tov.world.viewports.LayerViewport;
import ritogaems.tov.world.viewports.ScreenViewport;

/**
 * @author Andrew Woods
 * @author Michael Purdy
 * @author Zoey Longridge
 * @author Darren McGarry
 * @author Kevin Martin
 *         A map screen which the player navigates and interacts with
 *         Michael
 *         - Iterator method from particle effects
 *         - Changing music
 *         - Most add properties methods
 *         Everyone implemented their functionality
 */
public class MapScreen extends GameScreen {

    ////////////////
    // PROPERTIES
    ////////////////

    /**
     * Not currently used:
     * Used to display update and draw fps
     */
    private DrawLoop drawLoop;
    private UpdateLoop updateLoop;

    /**
     * The activity the map screen belongs to
     */
    public final Activity activity;

    /**
     * The paint used for drawing
     */
    private Paint paint;

    /**
     * Layer and screen viewports
     */
    private ScreenViewport screenViewport;
    private static int layerViewportWidth = 180;
    private static int layerViewportHeight = 120;
    public LayerViewport layerViewport;

    /**
     * Hud controls
     */
    private boolean drawHUD;
    private HudGroup hudGroup;

    /**
     * list of particle effects on the map
     */
    public ArrayList<IParticleEffect> particleEffects;

    /**
     * list of portals on the map
     */
    private ArrayList<Portal> portals;

    /**
     * Wrapper for message properties
     */
    private MessageMetrics messageMetrics;

    /**
     * list of signs on the map
     */
    private ArrayList<Sign> signs;

    /**
     * list of items on the map
     */
    public ArrayList<Item> items;

    //Chest
    public ArrayList<Chest> chests;

    /**
     * list of consumables on the map
     */
    public ArrayList<Consumable> consumables;

    /*
     * list of blocks on the map
     */
    public ArrayList<Block> blocks;

    /**
     * instance of phantomBoss on the map
     */
    public PhantomBoss phantomBoss;

    /**
     * Instance of Dodongo on the map
     */
    public Dodongo dodongo;

    //PressurePointCollection
    public ArrayList<PressurePointCollection> pressurePointCollections;

    //pressure point
    public ArrayList<PressurePoint> pressurePoints;

    //doors
    public ArrayList<Door> doors;
    /**
     * The list of reset blocks is on the map
     */
    public ArrayList<ResetBlocks> resetBlocks;

    // create the tilemap
    /**
     * The tilemap that the map screen uses
     */
    public TileMap tileMap;

    /**
     * The player on the map
     */
    private Player player;

    /**
     * The list of enemies on the map
     */
    public ArrayList<Enemy> enemies;

    /**
     * The background music of the map
     */
    BackgroundMusic backgroundMusic;

    /**
     * Can the game currently be saved
     */
    public boolean canSave;

    ////////////////
    // CONSTRUCTORS
    ////////////////

    /**
     * Constructor for map screen with a specific tile map
     *
     * @param game        The game fragment the map screen belongs to
     * @param name        The name of the map screen
     * @param tileMapPath The path to the tile map file in the assets folder
     * @throws IOException
     */
    public MapScreen(GameFragment game, String name, String tileMapPath) throws IOException {
        this(game, name);

        tileMap = new TileMap(game.getAssetStore().getTextFile(name, tileMapPath), this);
        replacePlayer(null);
    }

    /**
     * Constructor
     *
     * @param game The game fragment the screen belongs to
     * @param name The name of the game screen
     * @throws IOException
     */
    MapScreen(GameFragment game, String name) throws IOException {
        // map screen is always a play screen
        super(game, name, ScreenType.PLAY);

        this.canSave = true;

        activity = game.getActivity();

        // used for displaying draw and update fps (now removed)
        this.drawLoop = game.drawLoop;
        this.updateLoop = game.updateLoop;

        paint = new Paint();

        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);

        // create the viewports
        layerViewport = GraphicsUtil.createDynamicTileLayerVP(metrics, layerViewportWidth, layerViewportHeight);
        screenViewport = new ScreenViewport(0, 0, metrics.widthPixels, metrics.heightPixels);

        //add items
        items = new ArrayList<>();

        //add consumables
        consumables = new ArrayList<>();

        //add Blocks
        blocks = new ArrayList<>();

        //add Door
        doors = new ArrayList<>();

        // add enemies
        enemies = new ArrayList<>();

        //add Chest
        chests = new ArrayList<>();

        //add pressure points
        pressurePoints = new ArrayList<>();

        //add pressure point collection
        pressurePointCollections = new ArrayList<>();

        //add resetBlocks
        resetBlocks = new ArrayList<>();

        // add any particle effects
        particleEffects = new ArrayList<>();

        // initialise the portals
        portals = new ArrayList<>();

        // create the message metrics
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(100);
        this.messageMetrics = new MessageMetrics(metrics.widthPixels, metrics.heightPixels,
                game.getAssetStore().getBitmap("scroll", "img/HUD/scroll.png"));

        // initialise signs
        signs = new ArrayList<>();

        hudGroup = new HudGroup(metrics, game, this);
        drawHUD = true;

    }

    ////////////////
    // METHODS
    ////////////////

    /**
     * Get the player from the screen
     *
     * @return player
     */
    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * updates the map and all the components of the map
     *
     * @param elapsedTime The time since the last update
     */
    @Override
    public void update(ElapsedTime elapsedTime) {

        synchronized (player) {
            if (player.getBackPack().isOpen) {
                player.getBackPack().update(player);
                if (hudGroup.inventoryButton.isClicked()) {
                    player.getBackPack().isOpen = false;
                }
            } else {
                player.update(hudGroup.joystick.calculate(), hudGroup.joystick.checkDirection(), hudGroup.swordButton);

                if (phantomBoss != null) {
                    phantomBoss.update();
                    if (phantomBoss.state == Entity.entityState.DEAD) {
                        phantomBoss = null;
                    }
                }

                if (dodongo != null) {
                    dodongo.update(elapsedTime);
                    if (dodongo.state == Entity.entityState.DEAD) {
                        dodongo = null;
                    }
                }

                Iterator<Enemy> enemyIterator = enemies.iterator();
                while (enemyIterator.hasNext()) {
                    Enemy enemy = enemyIterator.next();
                    if (layerViewport.intersects(enemy.getCollisionBox())) enemy.update(player);
                    if (enemy.state == Entity.entityState.DEAD) {
                        enemyIterator.remove();
                    }
                }

                Iterator<Item> itemIterator = items.iterator();
                while (itemIterator.hasNext()) {
                    Item item = itemIterator.next();
                    if (item.isAlive()) {
                        item.update(elapsedTime);
                    } else {
                        itemIterator.remove();
                    }
                }

                Iterator<Door> doorIterator = doors.iterator();
                while (doorIterator.hasNext()) {
                    Door door = doorIterator.next();
                    if (!door.isOpen()) {
                        door.update();
                    } else {
                        doorIterator.remove();
                    }
                }


                for (Block block : this.blocks) {
                    block.update();
                }

                for (Chest chest : this.chests) {
                    chest.update();
                }

//                for (PressurePoint pressurePoint : this.pressurePoints) {
//                    pressurePoint.isPressed();
//                }

                for (PressurePointCollection pressurePointCollection : this.pressurePointCollections) {
                    pressurePointCollection.update();
                }

                for (ResetBlocks resetBlocks : this.resetBlocks) {
                    resetBlocks.update();
                }

                Iterator<Consumable> consumableIterator = consumables.iterator();
                while (consumableIterator.hasNext()) {
                    Consumable consumable = consumableIterator.next();
                    if (consumable.isAlive()) {
                        if (consumable.update()) {
                            player.getBackPack().pickUpItem(consumable);
                        }

                    } else {
                        consumableIterator.remove();
                    }
                }

                Iterator<IParticleEffect> particleIterator = particleEffects.iterator();
                while (particleIterator.hasNext()) {
                    IParticleEffect effect = particleIterator.next();
                    if (effect.isAlive()) {
                        effect.update(elapsedTime);
                    } else {
                        particleIterator.remove();
                    }
                }

                for (Sign sign : signs) {
                    if (sign.getCollisionBox().intersects(player.getCollisionBox())) {
                        sign.activateSign();
                    }
                }

                layerViewport.snap(player.position, tileMap.getMapWidth(), tileMap.getMapHeight());
                for (Portal portal : portals) {
                    if (portal.getCollisionBox().intersects(player.getCollisionBox())) {
                        portal.activatePortal(game.getScreenManager(), player);
                    }
                }

                layerViewport.snap(player.position, tileMap.getMapWidth(), tileMap.getMapHeight());

                hudGroup.update(game, player);

            }
        }
    }

    /**
     * Change the player of the map
     *
     * @param player
     */
    public void replacePlayer(Player player) {

        if (player == null) {
            player = new Player(tileMap.playerSpawn.x, tileMap.playerSpawn.y, this);
        } else {
            player.switchScreen(this);
        }

        this.player = player;

        // create the health bar
        hudGroup.healthBar = new StatusBar(this, 0.3f, 0.03f, 0.1f, 0.1f,
                player.getMaxHealth(), player.getCurrentHealth(), Color.RED, paint);
    }

    /**
     * Draws the map and all the components of the map
     *
     * @param canvas The canvas to draw the map to
     */
    @Override
    public void draw(Canvas canvas) {
        synchronized (player) {
            // metrics
            DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);

            tileMap.drawGroundLayers(canvas, layerViewport, screenViewport);

            for (Item item : items) {
                item.draw(canvas, layerViewport, screenViewport);
            }

            for (Consumable consumable : consumables) {
                consumable.draw(canvas, layerViewport, screenViewport);
            }

            for (PressurePoint pressurePoint : pressurePoints) {
                pressurePoint.draw(canvas, layerViewport, screenViewport);
            }

            for (ResetBlocks resetBlock : resetBlocks) {
                resetBlock.draw(canvas, layerViewport, screenViewport);
            }

            for (Block block : blocks) {
                block.draw(canvas, layerViewport, screenViewport);
            }

            for (Door door : doors) {
                door.draw(canvas, layerViewport, screenViewport);
            }

            for (Chest chest : chests) {
                chest.draw(canvas, layerViewport, screenViewport);
            }

            // draw enemies
            for (int i = 0; i < enemies.size(); i++) {
                enemies.get(i).draw(canvas, layerViewport, screenViewport);
            }

            if (phantomBoss != null) {
                phantomBoss.draw(canvas, layerViewport, screenViewport);
            }

            if (dodongo != null) {
                dodongo.draw(canvas, layerViewport, screenViewport);
            }

            player.draw(canvas, layerViewport, screenViewport);

            tileMap.drawLastLayer(canvas, layerViewport, screenViewport);

            for (IParticleEffect effect : particleEffects) {
                effect.draw(canvas, layerViewport, screenViewport);
            }

            for (Sign sign : signs) {
                sign.draw(canvas, layerViewport, screenViewport);
            }

            if (drawHUD) hudGroup.draw(canvas, player, game);

            player.getBackPack().draw(canvas);

            paint.setTextSize(60);
            canvas.drawText(player.getCurrentAmmo(), metrics.widthPixels * 0.9f, metrics.heightPixels * 0.92f, paint);

            // draw FPS
//            paint.setTextSize(100);
//            paint.setColor(Color.BLACK);
//            canvas.drawText("Draw FPS: " + drawLoop.getAverageFPS(), 100, 100, paint);
//            canvas.drawText("Update FPS: " + updateLoop.getAverageFPS(), 100, 200, paint);
        }
    }


    /**
     * Pause the map
     */
    @Override
    public void pause() {
        pauseMusic();
    }

    /**
     * resume the map
     */
    @Override
    public void resume() {
        playMusic();
    }

    /**
     * remove the map
     */
    @Override
    public void dispose() {
        backgroundMusic.dispose();
    }


    /**
     * Pause the music (used when switching screens)
     */
    @Override
    public void pauseMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.pause();
        }
    }

    /**
     * Play the music (used when switching screens)
     */
    @Override
    public void playMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.play();
        }
    }

    public boolean canSave() {
        return canSave;
    }

    public void setSave(boolean save) {
        this.canSave = save;
    }

    /**
     * change the background music of the map
     *
     * @param name The name of the background music
     * @param path The path to the background music in the assets folder
     * @param play Play the music after switching
     */
    public void changeBackgroundMusic(String name, String path, boolean play) {
        pauseMusic();
        this.backgroundMusic = game.getAssetStore().getBgm(name, path);
        if (this.backgroundMusic != null) {
            this.backgroundMusic.setLooping(true);
            backgroundMusic.setMonoVolume(0.5f);
        }
        if (play) playMusic();
    }

    /**
     * Add an enemy to the map
     *
     * @param enemy The enemy to add
     */
    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }

    /**
     * Add a portal to the map
     *
     * @param portal The portal to add
     */
    public void addPortal(Portal portal) {
        portals.add(portal);
    }

    /**
     * Add a sign to the map
     *
     * @param sign The sign to add
     */
    public void addSign(Sign sign) {
        signs.add(sign);
    }

    /**
     * Add a particle effect to the map
     *
     * @param particleEffect The particle effect to add
     */
    public void addParticleEffect(IParticleEffect particleEffect) {
        particleEffects.add(particleEffect);
    }

    /**
     * Add a consumable to the map
     *
     * @param consumable The consumable to add
     */
    public void addConsumable(Consumable consumable) {
        consumables.add(consumable);
    }

    /**
     * Get the message metrics of the map
     *
     * @return message metrics
     */
    public MessageMetrics getmessageMetrics() {
        return messageMetrics;
    }

    /**
     * Add a block to the map
     *
     * @param block The block to add
     */
    public void addBlock(Block block) {
        blocks.add(block);
    }

    /**
     * Add a door to the map
     *
     * @param door The door to add
     */
    public void addDoor(Door door) {
        this.doors.add(door);
    }

    /**
     * Add a pressure point to the map
     *
     * @param pressurePoint The pressure point to add
     */
    public void addPressurePoint(PressurePoint pressurePoint) {
        pressurePoints.add(pressurePoint);
    }

    /**
     * Add a reset blocks pressure point to the map
     *
     * @param resetBlock The reset blocks pressure point to add
     */
    public void addResetBlocks(ResetBlocks resetBlock) {
        resetBlocks.add(resetBlock);
    }

    /**
     * Add a pressure point collection to the map
     *
     * @param pressurePointCollection The portal to add
     */
    public void addPressurePointCollection(PressurePointCollection pressurePointCollection) {
        pressurePointCollections.add(pressurePointCollection);
    }

    /**
     * Add a chest to the map
     *
     * @param chest The portal to add
     */
    public void addChest(Chest chest) {
        chests.add(chest);
    }
}