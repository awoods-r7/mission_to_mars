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

    public InputManager(ArrayList<GameObject> managedTouchObjects, View view){
        this.managedTouchObjects = managedTouchObjects;
        view.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (motionEvent.getAction() & motionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                for (GameObject object : managedTouchObjects) {
                    if (object.isTouchable()) {
                        PositionVector touchPosition = new PositionVector(motionEvent.getX() * scaleX,
                                motionEvent.getY() * scaleY);
                        if (object.containsPoint(touchPosition)) {
                            object.onTouched();
                            Log.i("dsd", "Ryan here");
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
