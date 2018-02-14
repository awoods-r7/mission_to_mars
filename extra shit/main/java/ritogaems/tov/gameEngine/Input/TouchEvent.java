package ritogaems.tov.gameEngine.Input;

public class TouchEvent {

    // touch event constants
    public static final int NONE = 0;
    public static final int TOUCH_DOWN = 1;
    public static final int TOUCH_UP = 2;
    public static final int TOUCH_DRAGGED = 3;

    // type of touch event (TOUCH_DOWN, TOUCH_UP, TOUCH_DRAGGED)
    public int type;

    // screen position x and y of touch event
    public float x, y;

    // pointer ID associated with touch event
    public int pointer;
}