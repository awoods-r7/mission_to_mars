package com.rapid7.awoods.mission_to_mars.GameEngine;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.rapid7.awoods.mission_to_mars.GameObjects.GameObject;
import com.rapid7.awoods.mission_to_mars.GameObjects.PositionVector;

import java.util.ArrayList;

public class InputManager implements OnTouchListener {

    ArrayList<GameObject> managedTouchObjects;
    private float scaleX = 1, scaleY = 1;
    PositionVector position;

    public InputManager(ArrayList<GameObject> managedTouchObjects, View view, PositionVector position){
        this.managedTouchObjects = managedTouchObjects;
        view.setOnTouchListener(this);
        this.position = position;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        float ratio = position.x/position.y;
        switch (motionEvent.getAction() & motionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                for (GameObject object : managedTouchObjects) {
                    Log.i("down action", object.getName());
                    if (object.isTouchable()) {
                        PositionVector touchPosition = new PositionVector(motionEvent.getX(),
                                motionEvent.getY());
                        if (object.containsPoint(touchPosition)) {
                            Log.i("down action", object.getName());
                            object.onTouched();
                        }
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                for (GameObject object : managedTouchObjects) {
                    if (object.isTouchable()) {
                        PositionVector touchPosition = new PositionVector((motionEvent.getX()),
                                motionEvent.getY());
                        if (object.containsPoint(touchPosition)) {
                            Log.i("up action", object.getName());
                            object.onReleased();
                        }
                    }
                }
                break;


        }
        return true;
    }

    public ArrayList<GameObject> getManagedTouchObjects() {
        return managedTouchObjects;
    }

    public void setManagedTouchObjects(ArrayList<GameObject> managedTouchObjects) {
        this.managedTouchObjects = managedTouchObjects;
    }
}
