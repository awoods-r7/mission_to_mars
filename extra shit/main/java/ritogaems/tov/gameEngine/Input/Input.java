package ritogaems.tov.gameEngine.Input;

import android.view.View;

import java.util.List;

public class Input {

    // declare the different handlers responsible for different types of input
    private KeyHandler keyHandler;
    private TouchHandler touchHandler;

    // constructor that initialises handlers
    public Input(View view) {
        keyHandler = new KeyHandler(view);
        touchHandler = new TouchHandler(view);
    }

    /*
    KEY INPUT EVENTS
     */

    // determine if the specific key is currently pressed
    public boolean isKeyPressed(int keyCode) {
        return keyHandler.isKeyPressed(keyCode);
    }

    // returns a list of captured key events for this update tick
    public List<KeyEvent> getKeyEvents() {
        return keyHandler.getKeyEvents();
    }

    /*
    TOUCH INPUT EVENTS
     */

    // determine if there is an ongoing touch event for the given pointerID
    public boolean existsTouch(int pointerID) {
        return touchHandler.existsTouch(pointerID);
    }

    // get the x and y values for specified pointerID
    public float getTouchX(int pointerID) {
        return touchHandler.getTouchX(pointerID);
    }

    public float getTouchY(int pointerID) {
        return touchHandler.getTouchY(pointerID);
    }

    // return a list of captured touch events occurring this update tick
    public List<TouchEvent> getTouchEvents() {
        return touchHandler.getTouchEvents();
    }

    // reset accumulators - called as part of game loops update
    public void resetAccumulators() {
        touchHandler.resetAccumulator();
        keyHandler.resetAccumulator();
    }
}