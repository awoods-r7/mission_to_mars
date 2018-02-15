package com.rapid7.awoods.mission_to_mars.GameObjects;


import android.graphics.Canvas;

public abstract class Button extends GameObject {
    public Button(String image, PositionVector position, String name, float width, float height) {
        super(image, position, name, width, height, true);
    }

    public abstract void onPress();

    public abstract void onHold();


}
