package ritogaems.tov.ai;

import ritogaems.tov.util.Vector2;

/**
 * @author Darren
 */
public class Node {

    /**
     * Node position in game units, ie mid-point
      */
    public Vector2 nodePos;

    /**
     * Node coordinate position in array
     */
    public int xCoord, yCoord;

    /**
     * Whether the node is accessible for entities to traverse
     */
    public boolean accessible;

    /**
     * The cost to reach this node for the starting node
     */
    public int givenCost;

    /**
     * The cost to reach the destination node
     */
    public int heuristicCost;

    /**
     * Node through which this node was reached
     * Used to trace back a path when it is found
     */
    public Node parent;

    /**
     * Standard constructor
     *
     * @param xPos          X position of the node in the game world
     * @param yPos          Y position of the node in the game world
     * @param xCoord        X coordinate of the node in relation to the grid
     * @param yCoord        Y coordinate of the node in relation to the grid
     * @param accessible    Whether the node is accessible for entities to traverse
     */
    public Node(float xPos, float yPos, int xCoord, int yCoord, boolean accessible) {
        nodePos = new Vector2(xPos, yPos);
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.accessible = accessible;
    }

    /**
     * Getter for the total cost, adding the given and heuristic costs,
     * which composes the A* heuristic for path finding
     *
     * @return The total, or path cost
     */
    public int pathCost() {
        return givenCost + heuristicCost;
    }

}
