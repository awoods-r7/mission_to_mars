package com.rapid7.awoods.mission_to_mars.GameObjects.ObjectInstances;

import android.content.Context;

import com.rapid7.awoods.mission_to_mars.GameObjects.GameObject;
import com.rapid7.awoods.mission_to_mars.GameObjects.PositionVector;

/**
 * Created by rgallagher on 15/02/2018.
 */

public class Background extends GameObject {
    public Background(Context context, int resourceId, PositionVector position, String name, float width, float height) {
        super(context, resourceId, position, name, width, height);
    }

    @Override
    public void onTouched() {

    }

    @Override
    public void onReleased() {

    }
}
