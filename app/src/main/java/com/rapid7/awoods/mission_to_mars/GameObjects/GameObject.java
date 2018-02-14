package com.rapid7.awoods.mission_to_mars.GameObjects;

import java.util.Vector;

public abstract class GameObject {
    private String image;
    private PositionVector position;
    private String name;
    private double width;
    private double height;
    private boolean isDraw;
    private boolean isTouchable;

    public GameObject(String image, PositionVector position, String name, double width, double height) {


        this.image = image;
        this.position = position;
        this.name = name;
        this.width = width;
        this.height = height;
        this.isTouchable = false;
    }

    public GameObject(String image, PositionVector position, String name, double width,
                      double height, boolean isTouchable) {


        this.image = image;
        this.position = position;
        this.name = name;
        this.width = width;
        this.height = height;
        this.isTouchable = isTouchable;
    }

    public abstract void onTouched();
    public void draw(){};

    public boolean containsPoint(PositionVector queryPosition){

        // check y
        if((position.y + height) < queryPosition.y && position.y < queryPosition.y) {

            // check x
            if((position.x + width) < queryPosition.x && position.x < queryPosition.x) {
                return true;
            }
        }

        return false;
        }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public PositionVector getPosition() {
        return position;
    }

    public void setPosition(PositionVector position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public boolean isDraw() {
        return isDraw;
    }

    public void setDraw(boolean draw) {
        isDraw = draw;
    }

    public boolean isTouchable() {
        return isTouchable;
    }

    public void setTouchable(boolean touchable) {
        isTouchable = touchable;
    }
}