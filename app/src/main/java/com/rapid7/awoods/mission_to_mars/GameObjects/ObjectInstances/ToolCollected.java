package com.rapid7.awoods.mission_to_mars.GameObjects.ObjectInstances;


import android.content.Context;

import com.rapid7.awoods.mission_to_mars.GameObjects.GameObject;
import com.rapid7.awoods.mission_to_mars.GameObjects.PositionVector;

public class ToolCollected extends GameObject {

    public ToolCollected(Context context, int resourceId, PositionVector position, String name, float width, float height) {
        super(context, resourceId, position, name, width, height, true);
    }

    @Override
    public void onTouched() {
        setDraw(false);
    }

    @Override
    public void onReleased() {
        setDraw(false);
    }
}
