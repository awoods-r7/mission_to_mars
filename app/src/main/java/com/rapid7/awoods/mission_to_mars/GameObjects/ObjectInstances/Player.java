package com.rapid7.awoods.mission_to_mars.GameObjects.ObjectInstances;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.rapid7.awoods.mission_to_mars.GameObjects.LivingObject;
import com.rapid7.awoods.mission_to_mars.GameObjects.PositionVector;
import com.rapid7.awoods.mission_to_mars.GameObjects.Tool;

import java.util.ArrayList;
import java.util.Vector;

public class Player extends LivingObject{

    ArrayList<Tool> tools = new ArrayList<>();
    ArrayList<Bitmap[]> animations = new ArrayList<>();
    Bitmap image;
    boolean moving, gun;
    Bitmap currentBitmap;

    public Player(Context context, int resourceId, int rows, int columns, PositionVector position,
                  String name, float width, float height, float speed, int health) {
        super(context, resourceId, position, name, width, height, speed, health);
        image = BitmapFactory.decodeResource(context.getResources(), resourceId);
        cutSprites(image, rows, columns);
        moving = true;
        gun = true;

    }


    @Override
    public void draw(Canvas canvas, Paint paint) {
        if (moving == true) {
            //moving so keep updating the frame
            if (gun == true) {
                currentBitmap = animations.get(1)[0];
            }
            else {
                currentBitmap = animations.get(0)[0];
            }
        }
        //if still just grab the first frame
        else{
            if (gun == true) {
                currentBitmap = animations.get(1)[0];
            }
            else {
                currentBitmap = animations.get(0)[0];
            }
        }
        canvas.drawBitmap(currentBitmap, getWidth()/2, getHeight()/2, paint);
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

    public void cutSprites(Bitmap bitmap, int rows, int columns) {
        int width = bitmap.getWidth() / columns;
        int height = bitmap.getHeight() / rows;
        for (int sheetRows = 0;sheetRows < rows; sheetRows++)
        {
            int i = 0;
            Bitmap[] bitmapArray = new Bitmap[columns];
            for (int sheetColumns = 0;sheetColumns < columns; sheetColumns++)
            {
                int scrX = sheetColumns * bitmap.getWidth() / columns;
                int scrY = sheetRows * bitmap.getHeight() / rows;
                if ( i < columns)
                {
                    Bitmap frame = Bitmap.createBitmap(bitmap, scrX, scrY, width, height);
                    bitmapArray[i] = frame;
                    i++;
                }

            }
            animations.add(bitmapArray);

        }
    }
}
