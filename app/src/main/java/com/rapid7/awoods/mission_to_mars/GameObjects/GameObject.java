package com.rapid7.awoods.mission_to_mars.GameObjects;

import java.util.Vector;

public abstract class GameObject {
    private String image;
    private PositionVector position;
    private String name;
    private double width;
    private double height;
    private boolean isDraw;

    public GameObject(String image, PositionVector position, String name, double width, double height) {


        this.image = image;
        this.position = position;
        this.name = name;
        this.width = width;
        this.height = height;
    }

    public void draw(){};


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
}