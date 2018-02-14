package ritogaems.tov.ai;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import ritogaems.tov.util.BoundingBox;
import ritogaems.tov.util.Vector2;


/**
 * @author Darren
 */
public final class AIUtil {

    /**
     * Method to find the path between 2 points on a given path grid,
     * using the A* Algorithm
     *
     * @param originPos The point of origin
     * @param targetPos The destination point
     * @param pathGrid  The grid on which to find the path
     * @return an Array List of nodes representing the path
     */
    public static ArrayList<Vector2> FindPath(Vector2 originPos, Vector2 targetPos, PathGrid pathGrid) {
        //Used to keep track of nodes who's costs were modified for resetting
        ArrayList<Node> trackingList = new ArrayList<>();

        Node originNode = pathGrid.getNodeFromPos(originPos);
        Node targetNode = pathGrid.getNodeFromPos(targetPos);

        ArrayList<Node> openList = new ArrayList<>();
        HashSet<Node> closedList = new HashSet<>();

        openList.add(originNode);

        while (!openList.isEmpty()) {
            Node currentNode = openList.get(0);

            //Update the current node if the new node has a lower cost
            for (int c = 1; c < openList.size(); c++) {
                if (openList.get(c).pathCost() < currentNode.pathCost() || (openList.get(c).pathCost() == currentNode.pathCost() && openList.get(c).heuristicCost < currentNode.heuristicCost)) {
                    currentNode = openList.get(c);
                }
            }

            openList.remove(currentNode);
            closedList.add(currentNode);

            //If the target node is reached, trace and return the path
            if (currentNode == targetNode) {
                ArrayList<Vector2> path = pathGrid.convertNodePathToWorldPath(tracePath(originNode, targetNode));
                path.add(0, originPos);
                resetNodes(trackingList);

                return path;
            }

            //Look at the adjacent nodes and determine the best path by calculating costs
            for (Node adjacentNode : pathGrid.getAdjacentNodes(currentNode)) {
                if (closedList.contains(adjacentNode) || !adjacentNode.accessible) continue;

                int newCostToAdjacentNode = currentNode.givenCost + getCost(currentNode, adjacentNode);

                if (newCostToAdjacentNode < adjacentNode.givenCost || !openList.contains(adjacentNode)) {
                    adjacentNode.givenCost = newCostToAdjacentNode;
                    adjacentNode.heuristicCost = getCost(adjacentNode, targetNode);
                    adjacentNode.parent = currentNode;
                    trackingList.add(adjacentNode);

                    if (!openList.contains(adjacentNode)) openList.add(adjacentNode);
                }
            }
        }

        resetNodes(trackingList);
        return new ArrayList<>();
    }


    /**
     * Method to get the implied "cost" of moving across nodes
     *
     * @param node1 The first node
     * @param node2 The second node
     * @return  The cost of moving between the 2 nodes
     */
    private static int getCost(Node node1, Node node2) {
        int xDistance = Math.abs(node1.xCoord - node2.xCoord);
        int yDistance = Math.abs(node1.yCoord - node2.yCoord);

        //If the cost of moving non-diagonally is 1, then by pythagoras the diagonal cost is ~1.41.
        //To keep things in integers, 1.41 will be rounded to 1.4, and both numbers multiplied by 10.
        //This gives direct move cost = 10, diagonal move cost = 14.

        if (xDistance > yDistance) {
            return 14 * yDistance + 10 * (xDistance - yDistance);
        } else {
            return 14 * xDistance + 10 * (yDistance - xDistance);
        }
    }

    /**
     * Traces the path back by traversing the nodes via their parent node
     *
     * @param startNode The end of the path, start of the trace
     * @param endNode   The start of the path, end of the trace
     * @return
     */
    private static ArrayList<Node> tracePath(Node startNode, Node endNode) {
        ArrayList<Node> path = new ArrayList<>();
        Node currentNode = endNode;

        while (currentNode != startNode) {
            path.add(currentNode);
            currentNode = currentNode.parent;
        }

        //Reverse, as path is being traced from end to start
        Collections.reverse(path);
        return path;
    }

    /**
     * Reset the costs of nodes used in one path find
     *
     * @param nodes The list of nodes to be reset
     */
    private static void resetNodes(ArrayList<Node> nodes) {
        for (Node node : nodes) {
            node.givenCost = 0;
            node.heuristicCost = 0;
            node.parent = null;
        }
    }

    /**
     * Produces a list of nodes within a given area to implement
     * the Roaming functionality in the Entity hierarchy
     *
     * @param center    Centre of the area
     * @param radius    Radius of the area
     * @param pathGrid  The grid from which to produce the list
     * @return          The list of nodes which can be roamed to
     */
    public static ArrayList<Node> generateRoamNodes(Vector2 center, float radius, PathGrid pathGrid) {
        BoundingBox tempBox = new BoundingBox(center.x, center.y, radius, radius);
        ArrayList<Node> availableNodes = new ArrayList<>();

        for (Node[] nodeLine : pathGrid.grid) {
            for (Node node : nodeLine) {
                if (tempBox.contains(node.nodePos.x, node.nodePos.y) && node.nodePos.getDistance(center) <= radius) {
                    if (node.accessible) {
                        availableNodes.add(node);
                    }
                }
            }
        }

        return availableNodes;
    }
}