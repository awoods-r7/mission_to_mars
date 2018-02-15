package com.rapid7.awoods.mission_to_mars.GameObjects.ObjectInstances;

import android.content.Context;

import com.rapid7.awoods.mission_to_mars.GameObjects.LivingObject;
import com.rapid7.awoods.mission_to_mars.GameObjects.PositionVector;

/**
 * Created by rgallagher on 15/02/2018.
 */

public class SlugEnemy extends LivingObject {

    private float maxHeight = 10;
    private float currentHeight = 0;
    private float minHeight = 20;
    boolean rising = false;
    boolean near = false;

    public SlugEnemy(Context context, int referenceId, PositionVector position,
                     String name, float width, float height, float speed, int health) {
        super(context, referenceId, position, name, width, height, speed, health);
        //setDraw(true);
    }

    @Override
    public void update(float limit) {

    }

    @Override
    public void die() {


    }

    @Override
    public void attack() {
        if(near){
        if (rising){
            setDraw(true);
            if(currentHeight<maxHeight) {
                currentHeight++;
                position.y--;
            } else {
                rising = false;
                if (currentHeight > minHeight) {
                    currentHeight--;
                    position.y++;
                } else {
                    setDraw(true);
                }
            }
            }
        }
    }

    @Override
    public void onTouched() {

    }

    @Override
    public void onReleased() {

    }

    public float getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(float maxHeight) {
        this.maxHeight = maxHeight;
    }

    public float getCurrentHeight() {
        return currentHeight;
    }

    public void setCurrentHeight(float currentHeight) {
        this.currentHeight = currentHeight;
    }

    public float getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(float minHeight) {
        this.minHeight = minHeight;
    }

    public boolean isRising() {
        return rising;
    }

    public void setRising(boolean rising) {
        this.rising = rising;
    }

    public boolean isNear() {
        return near;
    }

    public void setNear(boolean near) {
        this.near = near;
    }
}
