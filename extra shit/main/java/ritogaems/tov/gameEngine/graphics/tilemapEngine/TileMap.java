package ritogaems.tov.gameEngine.graphics.tilemapEngine;

import android.graphics.Canvas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import ritogaems.tov.ai.PathGrid;
import ritogaems.tov.gameEngine.AssetStore;
import ritogaems.tov.gameEngine.entity.ArcherEnemy;
import ritogaems.tov.gameEngine.entity.BlockPuzzle.Block;
import ritogaems.tov.gameEngine.entity.BlockPuzzle.Chest;
import ritogaems.tov.gameEngine.entity.BlockPuzzle.Door;
import ritogaems.tov.gameEngine.entity.BlockPuzzle.PressurePoint;
import ritogaems.tov.gameEngine.entity.BlockPuzzle.PressurePointCollection;
import ritogaems.tov.gameEngine.entity.BlockPuzzle.ResetBlocks;
import ritogaems.tov.gameEngine.entity.Congelus;
import ritogaems.tov.gameEngine.entity.SnakeEnemy;
import ritogaems.tov.gameEngine.entity.bosses.PhantomBoss;
import ritogaems.tov.gameEngine.entity.bosses.Dodongo;
import ritogaems.tov.gameEngine.graphics.particles.generation.settings.FireSettings;
import ritogaems.tov.gameEngine.graphics.particles.generation.ParticleEmitter;
import ritogaems.tov.gameEngine.graphics.particles.generation.settings.ParticleSettings;
import ritogaems.tov.gameEngine.graphics.particles.generation.settings.SmokeSettings;
import ritogaems.tov.gameEngine.graphics.particles.generation.settings.SnowSettings;
import ritogaems.tov.gameEngine.graphics.particles.generation.settings.SparkleSettings;
import ritogaems.tov.gameEngine.items.AmmoDrop;
import ritogaems.tov.gameEngine.items.Consumable;
import ritogaems.tov.gameEngine.items.DamagePotion;
import ritogaems.tov.gameEngine.items.HealthPotion;
import ritogaems.tov.gameEngine.items.Inventory;
import ritogaems.tov.gameEngine.items.SpeedPotion;
import ritogaems.tov.util.Vector2;
import ritogaems.tov.world.Portal;
import ritogaems.tov.world.Sign;
import ritogaems.tov.world.screens.MapScreen;
import ritogaems.tov.world.viewports.LayerViewport;
import ritogaems.tov.world.viewports.ScreenViewport;

/**
 * @author Michael Purdy
 * @author Darren McGarry
 *         Tile map that for holding the tiles
 *         Constructor reads map properties and adds them to the map screen
 *         <p>
 *         Darren
 *         - File layout convention
 *         - Layers code
 *         - Current code for reading file and creating tiles maths based on Michael’s initial code
 *         <p>
 *         Michael
 *         - Properties convention
 *         - Properties code
 *         - Initial maths for tiles
 */
public class TileMap {

    /**
     * The tile sheet used for the tile map
     */
    private TileSheet tileSheet;

    /**
     * The list of tile layers
     */
    private ArrayList<HashMap<String, Tile>> tileMap = new ArrayList<>();

    /**
     * The path grid
     */
    private PathGrid pathGrid;

    /**
     * Tilemap design convention layer assignments
     * (Darren McGarry's convention)
     */
    private static final int GROUND_LAYER = 0;
    private static final int COLLISION_LAYER = 1;
    private static final int DRAW_OVER_LAYER = 2;

    private int posFactor, posOffset;

    private int mapWidth = 0;
    private int mapHeight = 0;

    /**
     * The default spawn position of the player
     */
    public Vector2 playerSpawn = new Vector2();

    /**
     * Constructor to read in the tile map from a file
     *
     * @param rawMapText The raw text of the map
     * @param mapScreen  The map screen that the tile map is in
     * @throws IOException
     */
    public TileMap(String rawMapText, MapScreen mapScreen) throws IOException {

        // layer code - Darren's

        BufferedReader br = new BufferedReader(new StringReader(rawMapText));
        String line = br.readLine();
        String[] tilesheetProperties = line.split(",");

        tileSheet = new TileSheet(mapScreen.getGame().getAssetStore().getBitmap(tilesheetProperties[0], tilesheetProperties[1]), Integer.parseInt(tilesheetProperties[2]));

        line = br.readLine();

        posFactor = this.tileSheet.getTileSizeInPixels();
        posOffset = this.tileSheet.getTileSizeInPixels() / 2;

        HashMap<String, Tile> layerMap;

        int x = 0;
        int y = 0;
        int i = 0;

        // for all the layers in the map
        while (!line.equals("END MAP")) {
            layerMap = new HashMap<>();

            // for all the rows in the layer
            while (!line.equals("END LAYER")) {
                ArrayList<String> mapLine = new ArrayList<>();
                Collections.addAll(mapLine, line.split(","));
                for (String nextTile : mapLine) {
                    int tileID = Integer.parseInt(nextTile);
                    if (tileID != 0) {
                        // set the position of the tile
                        float xPos = (x * TileSheet.TILE_SIZE) + TileSheet.HALF_TILE_SIZE; // uses half tile size to not have to do /2 operation
                        float yPos = (y * TileSheet.TILE_SIZE) + TileSheet.HALF_TILE_SIZE;

                        // create the tile using the id of the tile bitmap
                        Tile tmpTile = new Tile(tileID, xPos, yPos, x, y, this.tileSheet.getTileBitmap(tileID));
                        layerMap.put(xPos + "," + yPos, tmpTile);
                    }

                    x++;
                }
                y++;
                if (mapWidth < x) mapWidth = x;
                x = 0;
                line = br.readLine();
            }
            tileMap.add(layerMap);
            if (mapHeight < y) mapHeight = y;
            y = 0;
            i++;
            line = br.readLine();
        }

        // property code - michaels

        // read in the map properties
        line = br.readLine();

        // if there are any properties
        if (line != null && line.equals("START PROPERTIES")) {

            // for creating puzzles
            ArrayList<Door> tempDoors = new ArrayList<>();
            ArrayList<PressurePoint> tempPP = new ArrayList<>();

            // For each property
            while (!line.equals("END PROPERTIES")) {
                line = br.readLine();
                String[] propertyValues = line.split(",");

                // switch the type of property
                switch (propertyValues[0]) {
                    case "Player":
                        // set the default spawn position
                        playerSpawn.x = Integer.parseInt(propertyValues[1]);
                        playerSpawn.y = Integer.parseInt(propertyValues[2]);
                        break;
                    case "Enemy":
                        // create an enemy on the map
                        // e.g. Enemy,SnakeEnemy,120,100
                        int xPos = Integer.parseInt(propertyValues[2]);
                        int yPos = Integer.parseInt(propertyValues[3]);
                        switch (propertyValues[1]) {
                            case "SnakeEnemy":
                                mapScreen.addEnemy(new SnakeEnemy(xPos, yPos, this, mapScreen));
                                break;
                            case "ArcherEnemy":
                                mapScreen.addEnemy(new ArcherEnemy(xPos, yPos, this, mapScreen));
                                break;
                            case "Congelus":
                                mapScreen.addEnemy(new Congelus(xPos, yPos, this, mapScreen));
                                break;
                        }
                        break;
                    case "Portal":
                        // create a portal
                        Portal newPortal = new Portal(Integer.parseInt(propertyValues[1]), Integer.parseInt(propertyValues[2]),
                                propertyValues[3],
                                Integer.parseInt(propertyValues[4]), Integer.parseInt(propertyValues[5]));
                        mapScreen.addPortal(newPortal);
                        break;
                    case "Sign":
                        // create a sign
                        Sign newSign = new Sign(Integer.parseInt(propertyValues[1]), Integer.parseInt(propertyValues[2]),
                                propertyValues[3], Integer.parseInt(propertyValues[4]), mapScreen);
                        mapScreen.addSign(newSign);
                        break;
                    case "ParticleEmitter":
                        // create a particle emitter
                        addParticleEmitter(mapScreen, propertyValues);
                        break;
                    case "BackgroundMusic":
                        // add background music
                        // e.g. BackgroundMusic,testBgm,audio/testBgm.mp3
                        mapScreen.changeBackgroundMusic(propertyValues[1], propertyValues[2], false);
                        break;
                    case "Consumable":
                        // add a consumable item
                        addConsumable(mapScreen, propertyValues);
                        break;
                    case "Dodongo":
                        //Add a Dodongo to the map, placed in the left size of the lava chamber
                        mapScreen.dodongo = new Dodongo(Integer.parseInt(propertyValues[1]), Integer.parseInt(propertyValues[2]), mapScreen);
                        break;
                    case "PhantomBoss":
                        // add a phantom boss to this map - must be placed in the center of a 25x14 tile room
                        // e.g. PhantomBoss,200,80,15,25
                        int PBXPos = Integer.parseInt(propertyValues[1]);
                        int PBYPos = Integer.parseInt(propertyValues[2]);
                        int PBWidth = Integer.parseInt(propertyValues[3]);
                        int PBHeight = Integer.parseInt(propertyValues[4]);
                        mapScreen.phantomBoss = new PhantomBoss(PBXPos, PBYPos, PBWidth, PBHeight, mapScreen);
                        break;
                    case "PressurePointCollection":
                        // e.g. PressurePointCollection,doorID
                        // doorID is same as the PressurePoint one
                        // adds all pressure points since the last collection was made
                        // clears arraylist of pressure points for next collection
                        int PPCDoorID = Integer.parseInt(propertyValues[1]);
                        mapScreen.addPressurePointCollection(new PressurePointCollection(tempPP, tempDoors.get(PPCDoorID)));
                        break;
                    case "Puzzle":
                        addPuzzle(mapScreen, propertyValues, tempDoors, tempPP);
                        break;
                }
            }
        }

        mapWidth *= TileSheet.TILE_SIZE;
        mapHeight *= TileSheet.TILE_SIZE;

        // Darren's for AI
        pathGrid = new PathGrid(this);
    }

    /**
     * Add puzzles to the map screen
     *
     * @param mapScreen
     * @param propertyValues
     * @param tempDoors
     * @param tempPP
     */
    private void addPuzzle(MapScreen mapScreen, String[] propertyValues,
                           ArrayList<Door> tempDoors,
                           ArrayList<PressurePoint> tempPP) {
        // temp locations of puzzle objects to use as parameters in others
        // create a puzzle object on the map
        // e.g. Puzzle,[PuzzleType],50,75,7.5f,7.5f,[PuzzleSpecificThings]
        int puzzleXPos = Integer.parseInt(propertyValues[2]);
        int puzzleYPos = Integer.parseInt(propertyValues[3]);
        float puzzleWidth = Float.parseFloat(propertyValues[4]);
        float puzzleHeight = Float.parseFloat(propertyValues[5]);
        switch (propertyValues[1]) {
            case "Block":
                // e.g. Puzzle,Block,50,75,7.5,7.5
                mapScreen.addBlock(new Block(puzzleXPos, puzzleYPos, puzzleWidth, puzzleHeight, puzzleWidth, puzzleHeight, mapScreen));
                break;
            case "Door":
                // same as block
                Door door = new Door(puzzleXPos, puzzleYPos, puzzleWidth, puzzleHeight, puzzleWidth, puzzleHeight, mapScreen);
                mapScreen.addDoor(door);
                tempDoors.add(door);
                break;
            case "PressurePoint":
                // e.g. Puzzle,PressurePoint,50,75,7.5,7.5,0
                // the 0 at the end is the index of the tempDoors array list
                // to access (if it was the 4th door in the list added, use 3)
                int doorID = Integer.parseInt(propertyValues[6]);
                PressurePoint pressurePoint = new PressurePoint(puzzleXPos, puzzleYPos, puzzleWidth, puzzleHeight, puzzleWidth, puzzleHeight, mapScreen, tempDoors.get(doorID));
                mapScreen.addPressurePoint(pressurePoint);
                tempPP.add(pressurePoint);
                break;
            case "ResetBlocks":
                // same as block
                mapScreen.addResetBlocks(new ResetBlocks(puzzleXPos, puzzleYPos, puzzleWidth, puzzleHeight, puzzleWidth, puzzleHeight, mapScreen));
                break;
            case "Chest":
                // e.g. Puzzle,Chest,50,75,10,10,HealthPotion/SpeedPotion/DamagePotion/Key/ETC
                Consumable.ConsumableType consumableType = Consumable.ConsumableType.HEALTHPOTION;
                switch (propertyValues[6]) {
                    case "HealthPotion":
                        consumableType = Consumable.ConsumableType.HEALTHPOTION;
                        break;
                    case "DamagePotion":
                        consumableType = Consumable.ConsumableType.DAMAGEPOTION;
                        break;
                    case "SpeedPotion":
                        consumableType = Consumable.ConsumableType.SPEEDPOTION;
                        break;
                    case "Key":
                        consumableType = Consumable.ConsumableType.KEY;
                        break;
                    case "Ammo":
                        consumableType = Consumable.ConsumableType.AMMO;
                        break;
                    default:
                        break;
                }
                mapScreen.addChest(new Chest(puzzleXPos, puzzleYPos, puzzleWidth, puzzleHeight, puzzleWidth, puzzleHeight, mapScreen, consumableType));
        }
    }

    /**
     * Add a particle emitter to a map screen using an array of properties
     *
     * @param mapScreen      The map screen to add the emitter to
     * @param propertyValues The values that describe the effect
     * @author Michael Purdy
     */
    private void addParticleEmitter(MapScreen mapScreen, String[] propertyValues) {
        // eg. ParticleEmitter,Sparkle,100,100,30,20,purple
        AssetStore assetStore = mapScreen.getGame().getAssetStore();
        ParticleEmitter particleEmitter;
        Vector2 effectPosition = new Vector2(Float.parseFloat(propertyValues[2]), Float.parseFloat(propertyValues[3]));
        Vector2 spawnArea = new Vector2(Float.parseFloat(propertyValues[4]), Float.parseFloat(propertyValues[5]));
        ParticleSettings effectSettings = null;

        // choose settings based on property value
        switch (propertyValues[1]) {
            case "Fire":
                effectSettings = new FireSettings(spawnArea.x, spawnArea.y, assetStore);
                break;
            case "Smoke":
                effectSettings = new SmokeSettings(spawnArea.x, spawnArea.y, assetStore);
                break;
            case "Snow":
                effectSettings = new SnowSettings(spawnArea.x, spawnArea.y, SnowSettings.SnowType.getFromString(propertyValues[6]), assetStore);
                break;
            case "Sparkle":
                SparkleSettings.SparkleColour colour = SparkleSettings.SparkleColour.getFromString(propertyValues[6]);
                if (colour != null) {
                    effectSettings = new SparkleSettings(colour, spawnArea.x, spawnArea.y, assetStore);
                }
        }
        if (effectSettings != null) {
            particleEmitter = new ParticleEmitter(mapScreen.activity, effectSettings, effectPosition);
            mapScreen.addParticleEffect(particleEmitter);
        }
    }

    /**
     * Add a consumable to a map screen using an array of properties
     *
     * @param mapScreen      The map screen to add the consumable to
     * @param propertyValues The values that describe the consumable
     * @author Michael
     */
    private void addConsumable(MapScreen mapScreen, String[] propertyValues) {
        float x = Float.parseFloat(propertyValues[2]);
        float y = Float.parseFloat(propertyValues[3]);
        switch (propertyValues[1]) {
            case "HealthPotion":
                mapScreen.addConsumable(new HealthPotion(x, y,
                        Integer.parseInt(propertyValues[4]), false, mapScreen));
                break;
            case "DamagePotion":
                mapScreen.addConsumable(new DamagePotion(x, y,
                        Integer.parseInt(propertyValues[4]), false, mapScreen));
                break;
            case "SpeedPotion":
                mapScreen.addConsumable(new SpeedPotion(x, y, false,
                        mapScreen));
                break;
            case "ArrowDrop":
                mapScreen.addConsumable(new AmmoDrop(x, y, 10, 10,
                        Integer.parseInt(propertyValues[4]),
                        Inventory.AmmoType.ARROW, false, mapScreen));
                break;
        }
    }

    /**
     * Draws every layer except the last of the tilemap onto viewport
     *
     * @param canvas         Canvas to draw to
     * @param layerViewport  Layer Viewport
     * @param screenViewport Screen Viewport
     * @author Darren
     */
    public void drawGroundLayers(Canvas canvas, LayerViewport layerViewport, ScreenViewport screenViewport) {
        for (int c = GROUND_LAYER; c < DRAW_OVER_LAYER; c++) {
            for (Tile tile : tileMap.get(c).values()) {
                tile.draw(canvas, layerViewport, screenViewport);
            }
        }
    }

    /**
     * Draws final "draw over" layer of tile map
     *
     * @param canvas         Canvas to draw to
     * @param layerViewport  Layer Viewport
     * @param screenViewport Screen Viewport
     * @author Darren
     */
    public void drawLastLayer(Canvas canvas, LayerViewport layerViewport, ScreenViewport screenViewport) {
        for (Tile tile : tileMap.get(DRAW_OVER_LAYER).values()) {
            tile.draw(canvas, layerViewport, screenViewport);
        }
    }

    /**
     * Gets a specific tile by it's coordinate in the tilemap
     *
     * @param x     The x co-ordinate
     * @param y     The y co-ordinate
     * @param layer The layer the tile belongs to
     * @return The tile in the specified co-ordinates
     * @author Darren
     */
    public Tile getTileByCoord(int x, int y, int layer) {
        float xPos = (x * posFactor) + posOffset;
        float yPos = (y * posFactor) + posOffset;
        String pos = xPos + "," + yPos;
        return tileMap.get(layer).get(pos);
    }

    /**
     * Gets a specific tile by it's position in the tilemap
     *
     * @param x     The x position of the tile (in game units)
     * @param y     The y position of the tile (in game units)
     * @param layer The layer the tile belongs to
     * @return The tile at the specified position
     * @author Darren
     */
    public Tile getTileByPos(float x, float y, int layer) {
        String pos = x + "," + y;
        return tileMap.get(layer).get(pos);
    }

    /**
     * Adds a tile to the specified layer
     *
     * @param tile      Tile to add
     * @param layer     Layer to add the tile to
     * @author Darren
     */
    public void addTile(Tile tile, int layer) {
        tileMap.get(layer).put(tile.position.x + "," + tile.position.y, tile);
    }

    /**
     * Removes a tile at the specifcied position in the specified layer
     *
     * @param xPos  X position of the tile
     * @param yPos  Y position of the tile
     * @param layer Layer to remove the tile from
     */
    public void removeTile(float xPos, float yPos, int layer) {
        tileMap.get(layer).remove(xPos + "," + yPos);
    }

    /**
     * Getter
     *
     * @return The ground tiles
     * @author Darren
     */
    public HashMap<String, Tile> getGroundTiles() {
        return tileMap.get(GROUND_LAYER);
    }

    // todo javadoc
    // Put the tiles from array list tile and pass them to an Array list of Game objects (in mapscreen)
    // Pass this to the player class
    public HashMap<String, Tile> getCollidableTiles() {
        return tileMap.get(COLLISION_LAYER);
    }

    /**
     * Getter
     *
     * @return Path grid
     * @author Darren
     */
    public PathGrid getPathGrid() {
        return pathGrid;
    }

    /**
     * Getter
     *
     * @return map width (game units)
     * @author Darren
     */
    public int getMapWidth() {
        return mapWidth;
    }

    /**
     * Getter
     *
     * @return map height (game units)
     * @author Darren
     */
    public int getMapHeight() {
        return mapHeight;
    }

    /**
     * Getter
     *
     * @return tilesheet of this map
     */
    public TileSheet getTileSheet() {
        return tileSheet;
    }
}
