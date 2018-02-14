package ritogaems.tov.gameEngine.Input;

import android.util.Log;
import android.view.View;
import android.view.View.OnKeyListener;

import java.util.ArrayList;
import java.util.List;

import ritogaems.tov.util.Pool;

class KeyHandler implements OnKeyListener {

    /*
    PROPERTIES
     */

    // array of key down boolean flags - indexed by android (ranged from 0-127)
    private boolean[] pressedKeys = new boolean[128];

    // key event pool and current/unconsumed key events
    private Pool<KeyEvent> keyEventPool; // Pool is a class under Util in Gage (no import)
    private List<KeyEvent> unconsumedKeyEvents = new ArrayList<>();
    private List<KeyEvent> keyEvents = new ArrayList<>();

    // define the max number of key events
    private final int KEY_POOL_SIZE = 100;

    // constructor
    public KeyHandler(View view) {
        keyEventPool = new Pool<>(new Pool.ObjectFactory<KeyEvent>() {
            public KeyEvent createObject() {
                return new KeyEvent();
            }
        }, KEY_POOL_SIZE);

        view.setOnKeyListener(this);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }

    /*
    KEY EVENTS
     */
    @Override
    public boolean onKey(View view, int keyCode, android.view.KeyEvent event) {
        // multi key events not supported
        if (event.getAction() == android.view.KeyEvent.ACTION_MULTIPLE) {
            String warningTag = "Warning: ";
            String warningMessage = "ACTION_MULTIPLE event type encountered within" + this.getClass().toString();
            Log.v(warningTag, warningMessage);
            return false;
        }

        // store details of key events
        synchronized (this) {
            // retrieve and populate event
            KeyEvent keyEvent = keyEventPool.get();
            keyEvent.keyCode = keyCode;
            keyEvent.keyChar = (char) event.getUnicodeChar();

            if (event.getAction() == android.view.KeyEvent.ACTION_DOWN) {
                keyEvent.type = KeyEvent.KEY_DOWN;
                if (keyCode > 0 && keyCode < 127) {
                    pressedKeys[keyCode] = true;
                }
            }
            if (event.getAction() == android.view.KeyEvent.ACTION_UP) {
                keyEvent.type = KeyEvent.KEY_UP;
                if (keyCode > 0 && keyCode < 127) {
                    pressedKeys[keyCode] = false;
                }
            }

            // add the event to unconsumed key events
            unconsumedKeyEvents.add(keyEvent);
        }
        return false;
    }

    // indicate if the specified key is currently down
    public boolean isKeyPressed(int keyCode) {
        return !(keyCode < 0 || keyCode > 127) && pressedKeys[keyCode];
    }

    /*
    EVENT ACCUMULATION
     */

    // return the list of key events for the current frame
    public List<KeyEvent> getKeyEvents() {
        synchronized (this) {
            return keyEvents;
        }
    }

    // reset the accumulator
    public void resetAccumulator() {
        synchronized (this) {
            // release all existing key events
            int length = keyEvents.size();
            for (int i = 0; i < length; i++) {
                keyEventPool.add(keyEvents.get(i));
            }
            keyEvents.clear();
            // copy across accumulated events
            keyEvents.addAll(unconsumedKeyEvents);
            unconsumedKeyEvents.clear();
        }
    }
}
