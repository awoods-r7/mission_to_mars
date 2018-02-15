package com.rapid7.awoods.mission_to_mars.GameObjects;


import android.content.Context;
import android.graphics.Canvas;

import java.util.*;

public abstract class MovingObject extends GameObject {

    private float speed;

    public MovingObject(Context context, int referenceId, PositionVector position, String name, float width, float height, float speed) {
        super(context, referenceId, position, name, width, height);
        this.speed = speed;
    }

    public abstract void update();


    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}

