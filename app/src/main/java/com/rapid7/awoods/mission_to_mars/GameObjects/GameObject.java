package com.rapid7.awoods.mission_to_mars.GameObjects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Vector;

public abstract class GameObject {
    private Bitmap image;

    private PositionVector position;
    private String name;
    private float width;
    private float height;
    private boolean isDraw;
    private boolean isTouchable;
    private int rows;
    private int columns;

    public GameObject(Context context, int resourceId, PositionVector position,
                      String name, float width, float height) {

        this.image = BitmapFactory.decodeResource(context.getResources(), resourceId);
        this.image = Bitmap.createScaledBitmap(this.image, (int)width, (int)height, true);
        this.position = position;
        this.name = name;
        this.width = width;
        this.height = height;
        this.isTouchable = false;
    }

    public GameObject(Context context, int resourceId, PositionVector position, String name, float width,
                      float height, boolean isTouchable) {


        this.image = BitmapFactory.decodeResource(context.getResources(), resourceId);
        this.image = Bitmap.createScaledBitmap(this.image, (int)width, (int)height, true);
        this.position = position;
        this.name = name;
        this.width = width;
        this.height = height;
        this.isTouchable = isTouchable;
    }

    //for player
    public GameObject(Context context, int rows, int columns, int resourceId, PositionVector position,
                      String name, float width, float height)
    {
        this.rows = rows;
        this.columns = columns;
        this.image = BitmapFactory.decodeResource(context.getResources(), resourceId);
        this.image = Bitmap.createScaledBitmap(this.image, (int)width, (int)height, true);
        this.position = position;
        this.name = name;
        this.width = width;
        this.height = height;
    }

    public abstract void onTouched();
    public abstract void onReleased();
    public void draw(Canvas canvas, Paint paint){
        canvas.drawBitmap(image, position.x, position.y, paint);
    };

    public boolean containsPoint(PositionVector queryPosition){

        // check y
        if((position.y + height) > queryPosition.y && position.y < queryPosition.y) {

            // check x
            if((position.x + width) > queryPosition.x && position.x < queryPosition.x) {
                return true;
            }
        }

        return false;
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