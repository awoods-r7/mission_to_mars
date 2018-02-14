package com.rapid7.awoods.mission_to_mars.GameObjects;


public abstract class Button extends GameObject{
    public Button(String image, PositionVector position, String name, double width, double height) {
        super(image, position, name, width, height);
    }

    public abstract void onPress();

    public abstract void onHold();

}
