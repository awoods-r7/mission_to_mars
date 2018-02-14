package ritogaems.tov.gameEngine.entity.BlockPuzzle;

import java.util.ArrayList;
/**
 * @author Zoey Longridge
 *
 * A pressurePointCollection is an interaction of an arraylist of pressure points and a door
 */
public class PressurePointCollection {
    /**
     * An instance of an arraylist of pressurepoints
     */
    private ArrayList<PressurePoint> pressurePoints;
    /**
     * An instance of a door
     */
    private Door door;

    /**
     *
     * @param pressurePoints    Arraylist of Pressure Points
     * @param door              a door object
     */
    public PressurePointCollection(ArrayList<PressurePoint> pressurePoints, Door door){
        this.pressurePoints = pressurePoints;
        this.door = door;
    }

    /**
     * Method to update door object if all pressure points have been pressed on this frame of the game loop
     */
    public void update() {
        boolean allPressed = true;
        for (PressurePoint pressurePoint : pressurePoints) {
            if (pressurePoint.isPressed()) {
                allPressed = true;
            } else {
                allPressed = false;
                break;
            }
        }
        door.isOpen = allPressed;
    }
}
