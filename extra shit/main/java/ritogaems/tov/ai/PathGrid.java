package ritogaems.tov.ai;


import java.util.ArrayList;

import ritogaems.tov.gameEngine.graphics.tilemapEngine.Tile;
import ritogaems.tov.gameEngine.graphics.tilemapEngine.TileMap;
import ritogaems.tov.gameEngine.graphics.tilemapEngine.TileSheet;
import ritogaems.tov.util.Vector2;

/**
 * @author Darren
 */
public class PathGrid {

    /**
     * Tilemap which this grid belongs to
     */
    private TileMap tileMap;

    /**
     * 2D array representing a grid of nodes
     */
    public Node[][] grid;

    /**
     * Size of the grid in terms of nodes
     */
    private int gridSizeX, gridSizeY;

    /**
     * Representative radius and diameter of the nodes in this grid
     */
    private float nodeRadius, nodeDiameter;

    /**
     * Factor used to generate the node grid from a tilemap.
     * Squaring this will give you the number of path finding
     * nodes in each tile
     */
    private int nodeFactor = 2;

    /**
     * Standard constructor
     *
     * @param tileMap  tileMap from which the grid will be generated
     */
    public PathGrid(TileMap tileMap) {
        this.tileMap = tileMap;

        nodeDiameter = TileSheet.TILE_SIZE / nodeFactor;
        nodeRadius = nodeDiameter / 2;

        gridSizeX = Math.round(this.tileMap.getMapWidth() / nodeDiameter);
        gridSizeY = Math.round(this.tileMap.getMapHeight() / nodeDiameter);

        grid = new Node[gridSizeX][gridSizeY];

        populateGrid();
    }

    /**
     * Populates the grid nodes, mathematically calculating their positions
     * based on the tiles of the tilemap and the factor
     */
    private void populateGrid() {
        // Populates the grid with accessible tiles
        for (Tile tile : tileMap.getGroundTiles().values()) {
            for (int x = 0; x < nodeFactor; x++) {
                for (int y = 0; y < nodeFactor; y++) {
                    int nodeX = tile.xCoord * nodeFactor + x;
                    int nodeY = tile.yCoord * nodeFactor + y;
                    grid[nodeX][nodeY] = new Node(tile.getCollisionBox().getLeft() + (nodeDiameter * x) + nodeRadius,
                            tile.getCollisionBox().getTop() + (nodeDiameter * y) + nodeRadius,
                            nodeX, nodeY, true);
                }
            }
        }

        // Populates the grid with inaccessible tiles, ie collidable tiles
        for (Tile tile : tileMap.getCollidableTiles().values()) {
            for (int x = 0; x < nodeFactor; x++) {
                for (int y = 0; y < nodeFactor; y++) {
                    int nodeX = tile.xCoord * nodeFactor + x;
                    int nodeY = tile.yCoord * nodeFactor + y;
                    grid[nodeX][nodeY] = new Node(tile.getCollisionBox().getLeft() + (nodeDiameter * x) + nodeRadius,
                            tile.getCollisionBox().getTop() + (nodeDiameter * y) + nodeRadius,
                            nodeX, nodeY, false);
                }
            }
        }

    }

    /**
     * Converts a world position into a node
     *
     * @param pos   Vector to be converted
     * @return      The node in which the vector lies
     */
    public Node getNodeFromPos(Vector2 pos) {
        float percentX = pos.x / tileMap.getMapWidth();
        float percentY = pos.y / tileMap.getMapHeight();

        int x = Math.round((gridSizeX) * percentX);
        int y = Math.round((gridSizeY) * percentY);

        return grid[x][y];
    }

    /**
     * Converts a path of nodes to world positions as vectors
     *
     * @param nodePath  The path in terms of nodes
     * @return          The path in terms of vectors
     */
    public ArrayList<Vector2> convertNodePathToWorldPath(ArrayList<Node> nodePath) {
        ArrayList<Vector2> worldPath = new ArrayList<>();
        for (Node node : nodePath) {
            worldPath.add(new Vector2(node.nodePos));
        }

        return worldPath;
    }

    /**
     * Gets the neighbouring nodes of a given node, ie
     * 8 nodes, one in every cardinal and intercardinal direction
     *
     * @param node  The base node from which to get the adjacent ones
     * @return      A list of the adjacent nodes
     */
    public ArrayList<Node> getAdjacentNodes(Node node) {
        ArrayList<Node> adjacentNodes = new ArrayList<>();

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (x == 0 && y == 0) continue;

                int tempX = node.xCoord + x;
                int tempY = node.yCoord + y;

                if (tempX >= 0 && tempX <= gridSizeX - 1 && tempY >= 0 && tempY <= gridSizeY - 1) {
                    if (grid[tempX][tempY] != null) adjacentNodes.add(grid[tempX][tempY]);
                }
            }
        }

        return adjacentNodes;
    }

}
