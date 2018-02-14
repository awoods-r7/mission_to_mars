package com.rapid7.awoods.mission_to_mars.GameObjects.ObjectInstances;

import com.rapid7.awoods.mission_to_mars.GameObjects.Button;

import java.util.Vector;

public class Movementbutton extends Button{
    Player player;
    boolean moveRight;

    public Movementbutton(String image, Vector position, String name, double width, double height,
                          Player player, boolean moveRight) {
        super(image, position, name, width, height);
        this.player = player;
        this.moveRight = moveRight;
    }

    @Override
    public void onPress() {

    }

    @Override
    public void onHold() {
        if (moveRight) {
            player.moveRight();
        } else {
            player.moveLeft();
        }
    }
}
