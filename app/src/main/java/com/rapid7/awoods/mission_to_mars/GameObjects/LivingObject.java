package com.rapid7.awoods.mission_to_mars.GameObjects;


import android.graphics.Canvas;

import java.util.*;

public abstract class LivingObject extends MovingObject{

    private int health;

    public LivingObject(String image, PositionVector position, String name, float width, float height, float speed, int health) {
        super(image, position, name, width, height, speed);
        this.health = health;

    }

    public abstract void die();
    public abstract void attack();

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
