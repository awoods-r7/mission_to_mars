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
    ArrayList<GameObject> managedAndDrawnTouchObjects;
    private float scaleX = 1, scaleY = 1;
    PositionVector position;

    public InputManager(ArrayList<GameObject> managedTouchObjects, View view, PositionVector position){
        this.managedTouchObjects = managedTouchObjects;
        managedAndDrawnTouchObjects = new ArrayList<>();
        view.setOnTouchListener(this);
        this.position = position;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        float ratio = position.x/position.y;
        switch (motionEvent.getAction() & motionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                for (GameObject object : managedTouchObjects) {
                    if (object.isTouchable()) {
                        PositionVector touchPosition = new PositionVector(motionEvent.getX(),
                                motionEvent.getY());
                        if (object.containsPoint(touchPosition)) {
                            Log.i("down action", object.getName());
                            object.onTouched();
                        }
                    }
                }

                for (GameObject object : managedAndDrawnTouchObjects) {
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

                for (GameObject object : managedAndDrawnTouchObjects) {
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

    public ArrayList<GameObject> getManagedAndDrawnTouchObjects() {
        return managedAndDrawnTouchObjects;
    }

    public void setManagedAndDrawnTouchObjects(ArrayList<GameObject> managedAndDrawnTouchObjects) {
        this.managedAndDrawnTouchObjects = managedAndDrawnTouchObjects;
    }

    public float getScaleX() {
        return scaleX;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    public PositionVector getPosition() {
        return position;
    }

    public void setPosition(PositionVector position) {
        this.position = position;
    }
}
