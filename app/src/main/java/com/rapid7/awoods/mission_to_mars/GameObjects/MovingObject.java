package com.rapid7.awoods.mission_to_mars.GameObjects;


import java.util.*;

public abstract class MovingObject extends GameObject {

    private double speed;

    public MovingObject(String image, PositionVector position, String name, double width, double height, double speed) {
        super(image, position, name, width, height);
        this.speed = speed;
    }

    public abstract void update();


    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}

