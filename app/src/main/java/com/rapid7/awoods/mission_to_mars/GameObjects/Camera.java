package com.rapid7.awoods.mission_to_mars.GameObjects;

import com.rapid7.awoods.mission_to_mars.GameObjects.ObjectInstances.Player;

/**
 * Created by rgallagher on 15/02/2018.
 */

public class Camera {

    PositionVector position;
    float screenX;
    float screenY;

    public Camera(float screenX, float screenY) {
        this.screenX = screenX;
        this.screenY = screenY;
    }

    public PositionVector updatePosition(Player player){

        float camX = player.getPosition().x - (screenX/2);
        float camY = player.getPosition().y - (screenY/2);

        return new PositionVector(camX, camY);

    }
}
