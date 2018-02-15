package com.rapid7.awoods.mission_to_mars.GameObjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Vector;

public abstract class GameObject {
    private String image;
    private PositionVector position;
    private String name;
    private float width;
    private float height;
    private boolean isDraw;
    private boolean isTouchable;

    public GameObject(String image, PositionVector position, String name, float width, float height) {


        this.image = image;
        this.position = position;
        this.name = name;
        this.width = width;
        this.height = height;
        this.isTouchable = false;
    }

    public GameObject(String image, PositionVector position, String name, float width,
                      float height, boolean isTouchable) {


        this.image = image;
        this.position = position;
        this.name = name;
        this.width = width;
        this.height = height;
        this.isTouchable = isTouchable;
    }

    public abstract void onTouched();
    public void draw(Canvas canvas){

        Paint myPaint  = new Paint();
        myPaint.setColor(Color.rgb(0,0,0));
        canvas.drawRect(position.x, position.y + height, position.x + width, position.y, myPaint);

    };

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

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
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