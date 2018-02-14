package com.rapid7.awoods.mission_to_mars.GameObjects.ObjectInstances;


import com.rapid7.awoods.mission_to_mars.GameObjects.LivingObject;
import com.rapid7.awoods.mission_to_mars.GameObjects.PositionVector;
import com.rapid7.awoods.mission_to_mars.GameObjects.Tool;

import java.util.ArrayList;
import java.util.Vector;

public class Player extends LivingObject{

    ArrayList<Tool> tools = new ArrayList<>();


    public Player(String image, PositionVector position, String name, double width, double height, double speed, int health) {
        super(image, position, name, width, height, speed, health);
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


}
