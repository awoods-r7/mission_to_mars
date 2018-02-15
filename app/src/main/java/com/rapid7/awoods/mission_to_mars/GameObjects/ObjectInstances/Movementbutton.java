package com.rapid7.awoods.mission_to_mars.GameObjects.ObjectInstances;

import android.graphics.Canvas;

import com.rapid7.awoods.mission_to_mars.GameObjects.Button;
import com.rapid7.awoods.mission_to_mars.GameObjects.GameObject;
import com.rapid7.awoods.mission_to_mars.GameObjects.PositionVector;

import java.util.Vector;

public class Movementbutton extends GameObject{
    Player player;
    boolean moveRight;

    public Movementbutton(String image, PositionVector position, String name, float width, float height,
                          Player player, boolean moveRight) {
        super(image, position, name, width, height);
        this.player = player;
        this.moveRight = moveRight;
    }

    @Override
    public void onTouched() {
        if (moveRight) {
        player.moveRight();
    } else {
        player.moveLeft();
    }
    }
}
