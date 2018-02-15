package com.rapid7.awoods.mission_to_mars.GameObjects.ObjectInstances;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import com.rapid7.awoods.mission_to_mars.GameObjects.Button;
import com.rapid7.awoods.mission_to_mars.GameObjects.GameObject;
import com.rapid7.awoods.mission_to_mars.GameObjects.PositionVector;

import java.util.Vector;

public class Movementbutton extends GameObject{
    Player player;
    boolean moveRight;

    public Movementbutton(Context context, int referenceId, PositionVector position, String name, float width, float height,
                          Player player, boolean moveRight) {
        super(context, referenceId, position, name, width, height, true);
        this.player = player;
        this.moveRight = moveRight;
    }

    @Override
    public void onTouched() {

        player.setMoving(true);
        if (moveRight) {
        player.setPressingRight(true);
        player.setForward(true);

    } else {
        player.setPressingLeft(true);
        player.setForward(false);
    }
    }

    @Override
    public void onReleased() {
        player.setPressingLeft(false);
        player.setPressingRight(false);
        if (player.isMoving()){
            player.setMoving(false);
        }

    }
}
