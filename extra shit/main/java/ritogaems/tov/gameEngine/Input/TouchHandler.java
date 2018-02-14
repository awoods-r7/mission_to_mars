package ritogaems.tov.gameEngine.Input;

import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ritogaems.tov.util.Pool;

public class TouchHandler implements View.OnTouchListener {

    /*
    PROPERTIES
     */

    // define maximum of touch events supported
    public static final int MAX_TOUCH_POINTS = 3;

    // occurred and position information for max touch events
    private boolean[] existsTouch = new boolean[MAX_TOUCH_POINTS];
    private float touchX[] = new float[MAX_TOUCH_POINTS];
    private float touchY[] = new float[MAX_TOUCH_POINTS];

    // touch event pool and lists of current/unconsumed touch events
    private Pool<TouchEvent> pool;
    private List<TouchEvent> touchEvents = new ArrayList<>();
    private List<TouchEvent> unconsumedTouchEvents = new ArrayList<>();

    // axis scale values - can be used to scale the input range from native pixels to predefined range
    private float scaleX, scaleY;

    // define the maximum number of touch events that can be stored
    private final int TOUCH_POOL_SIZE = 100;

    // constructor
    public TouchHandler(View view) {
        pool = new Pool<>(new Pool.ObjectFactory<TouchEvent>() {
            public TouchEvent createObject() {
                return new TouchEvent();
            }
        }, TOUCH_POOL_SIZE);

        view.setOnTouchListener(this);

        // could be better defined against an ENUM ie. raw pixel or 0-1 or -1 to 1 ranges
        scaleX = 1.0f;
        scaleY = 1.0f;
    }

    /*
    TOUCH EVENTS
     */

    // set x and y scaling factors
    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }
    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        // update the locations of all occurring touch points
        // i = pointer index
        for (int i = 0; i < event.getPointerCount(); i++) {
            // update relevant touch point location
            int pointerId = event.getPointerId(i);
            touchX[pointerId] = event.getX(i) * scaleX;
            touchY[pointerId] = event.getY(i) * scaleY;
        }

        // extract details of this event
        int eventType = event.getActionMasked();
        int pointerId = event.getPointerId(event.getActionIndex());

        // retrieve and populate a touch event
        TouchEvent touchEvent = pool.get();
        // potential code to stop random crashing
        if (touchEvent == null) {
            return true;
        }
        try {
            touchEvent.pointer = pointerId;
            touchEvent.x = touchX[pointerId];
            touchEvent.y = touchY[pointerId];
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        switch (eventType) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                touchEvent.type = TouchEvent.TOUCH_DOWN;
                existsTouch[pointerId] = true;
                break;

            case MotionEvent.ACTION_MOVE:
                touchEvent.type = TouchEvent.TOUCH_DRAGGED;
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                touchEvent.type = TouchEvent.TOUCH_UP;
                existsTouch[pointerId] = false;
                break;
        }

        // add the event to list of unconsumed events
        synchronized (this) {
//            if (touchEvents.size() == 0) {
//               touchEvents.add(touchEvent);
//            } else {
//                unconsumedTouchEvents.add(touchEvent);
//            }
            unconsumedTouchEvents.add(touchEvent);
        }

        return true;
    }

    // determine if touch event exists for specified pointerID
    public boolean existsTouch(int pointerID) {
        synchronized (this) {
            return existsTouch[pointerID];
        }
    }

    // get scaled x and y location of the specified pointerID
    public float getTouchX(int pointerID) {
        synchronized (this) {
            // assumes the user will ensure correct range checking - for speed
            if (existsTouch[pointerID]) {
                return touchX[pointerID];
            } else {
                return Float.NaN;
            }
        }
    }

    public float getTouchY(int pointerID) {
        synchronized (this) {
            // assumes the user will ensure correct range checking - for speed
            if (existsTouch[pointerID]) {
                return touchY[pointerID];
            } else {
                return Float.NaN;
            }
        }
    }

    /*
    EVENT ACCUMULATION
     */
    public List<TouchEvent> getTouchEvents() {
        synchronized (this) {
            return touchEvents;
        }
    }

    // reset the accumulator
    public void resetAccumulator() {
        synchronized (this) {
            // release all existing touch events
            int length = touchEvents.size();
            for (int i = 0; i < length; i++) {
                pool.add(touchEvents.get(i));
            }
            touchEvents.clear();
            // copy across accumulated events
            touchEvents.addAll(unconsumedTouchEvents);
            unconsumedTouchEvents.clear();
        }
    }
}