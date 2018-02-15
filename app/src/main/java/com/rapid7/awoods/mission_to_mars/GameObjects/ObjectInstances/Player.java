package com.rapid7.awoods.mission_to_mars.GameObjects.ObjectInstances;


import android.content.Context;
import android.graphics.Canvas;

import com.rapid7.awoods.mission_to_mars.GameObjects.LivingObject;
import com.rapid7.awoods.mission_to_mars.GameObjects.PositionVector;
import com.rapid7.awoods.mission_to_mars.GameObjects.Tool;

import java.util.ArrayList;
import java.util.Vector;

public class Player extends LivingObject{

    ArrayList<Tool> tools = new ArrayList<>();
    Canvas playerCanvas;



    public Player(Context context, int referenceId, PositionVector position, String name, float width, float height, float speed, int health) {
        super(context, referenceId, position, name, width, height, speed, health);
        playerCanvas = context;
        animationLoad(context);
    }

    @Override
    public void update() {

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
        PositionVector currentPosition = getPosition();
        PositionVector newPosition = new PositionVector(currentPosition.x + getSpeed(),
                currentPosition.y);

        setPosition(newPosition);
    }

    public void moveLeft(){

        PositionVector currentPosition = getPosition();
        PositionVector newPosition = new PositionVector(currentPosition.x - getSpeed(),
                currentPosition.y);

        setPosition(newPosition);
    }

    public void jump(){}


    @Override
    public void onTouched() {

    }

    public void animationLoad(Canvas canvas){

    }
}
