package com.rapid7.awoods.mission_to_mars.GameObjects;


import android.content.Context;
import android.graphics.Canvas;

public abstract class Button extends GameObject {
    public Button(Context context, int referenceId, PositionVector position, String name, float width, float height) {
        super(context, referenceId, position, name, width, height, true);
    }

    public abstract void onPress();

    public abstract void onHold();


}
