package com.rapid7.awoods.mission_to_mars.GameObjects.ObjectInstances;


import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import com.rapid7.awoods.mission_to_mars.GameObjects.LivingObject;
import com.rapid7.awoods.mission_to_mars.GameObjects.PositionVector;
import com.rapid7.awoods.mission_to_mars.GameObjects.Tool;

import java.util.ArrayList;
import java.util.Vector;

public class Player extends LivingObject{

    ArrayList<Tool> tools = new ArrayList<>();
    boolean pressingRight = false;
    boolean pressingLeft = false;


    public Player(Context context, int referenceId, PositionVector position, String name, float width, float height, float speed, int health) {
        super(context, referenceId, position, name, width, height, speed, health);
    }

    @Override
    public void update() {
        if (pressingRight){
            moveRight();
        }

        if (pressingLeft){
            moveLeft();
        }


    }

    @Override
    public void die() {

    }

    @Override
    public void attack() {

    }

    private void addTool(Tool tool) {
        tools.add(tool);
    }

    public void moveRight(){

        Log.i("player", "Moving right");
        PositionVector currentPosition = getPosition();
        PositionVector newPosition = new PositionVector(currentPosition.x + getSpeed(),
                currentPosition.y);

        setPosition(newPosition);
    }

    public void moveLeft(){
        Log.i("player", "Moving left");
        PositionVector currentPosition = getPosition();
        PositionVector newPosition = new PositionVector(currentPosition.x - getSpeed(),
                currentPosition.y);

        setPosition(newPosition);
    }

    public void jump(){}


    @Override
    public void onTouched() {

    }

    @Override
    public void onReleased() {

    }

    public ArrayList<Tool> getTools() {
        return tools;
    }

    public void setTools(ArrayList<Tool> tools) {
        this.tools = tools;
    }

    public boolean isPressingRight() {
        return pressingRight;
    }

    public void setPressingRight(boolean pressingRight) {
        this.pressingRight = pressingRight;
    }

    public boolean isPressingLeft() {
        return pressingLeft;
    }

    public void setPressingLeft(boolean pressingLeft) {
        this.pressingLeft = pressingLeft;
    }
}
