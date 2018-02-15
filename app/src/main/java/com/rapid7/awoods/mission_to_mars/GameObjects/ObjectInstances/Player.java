package com.rapid7.awoods.mission_to_mars.GameObjects.ObjectInstances;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

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
    float width, height;
    int i = 0;
    int frame = 0;
    int rows, columns;
    boolean pressingRight = false;
    boolean pressingLeft = false;
    private float positionDelta;


    public Player(Context context, int resourceId, int rows, int columns, PositionVector position,
                  String name, float width, float height, float speed, int health) {
        super(context, resourceId, position, name, width, height, speed, health);
        image = BitmapFactory.decodeResource(context.getResources(), resourceId);
        cutSprites(image, rows, columns);
        moving = true;
        //gun = true;
        this.rows = rows;
        this.columns = columns;
        this.width = width;
        this.height = height;
    }


    @Override
    public void draw(Canvas canvas, Paint paint) {
        if (moving == true) {
            //moving so keep updating the frame
            if (gun == true) {
                currentBitmap = animations.get(1)[getCurrentFrame(columns)];
            }
            else {
                currentBitmap = animations.get(0)[getCurrentFrame(columns)];
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
        currentBitmap = Bitmap.createScaledBitmap(currentBitmap, 200, 200, true);
        //canvas.drawBitmap(currentBitmap, 750, 700, paint);

        if (isForward() == false) {
            canvas.drawBitmap(rotateBitmap(currentBitmap), getPosition().x, getPosition().y, paint);
        }else {
            canvas.drawBitmap(currentBitmap, getPosition().x, getPosition().y, paint);
        }
    }
    @Override
    public void update(float limit) {
        if (pressingRight){
            moveRight();
        }

        if (pressingLeft){
            moveLeft(limit);
        }


    }

    @Override
    public void die() {

    }

    @Override
    public void attack() {

    }

    public void addTool(Tool tool) {
        tools.add(tool);
    }

    public void moveRight(){

        Log.i("player", "Moving right");
        PositionVector currentPosition = getPosition();
        PositionVector newPosition = new PositionVector(currentPosition.x + getSpeed(),
                currentPosition.y);
        setPositionDelta(getSpeed());
        setPosition(newPosition);

    }

    public void moveLeft(float limit){
        Log.i("player", "Moving left");
        PositionVector currentPosition = getPosition();
        PositionVector newPosition = new PositionVector(currentPosition.x - getSpeed(),
                currentPosition.y);
        setPositionDelta(-getSpeed());
        setPosition(newPosition);

        if (newPosition.x>limit) {
            setPosition(newPosition);
        }
        else {
            setMoving(false);
        }

    }

    public void jump(){
        PositionVector currentPosition = getPosition();
        PositionVector newPosition = new PositionVector(currentPosition.x,currentPosition.y);// + get vertical speed
    }


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
    public int getCurrentFrame(int noOfFrames)
    {
        if (i > 3)
        {
            i = 0;
            frame++;
            frame = frame % noOfFrames;
            return frame;
        }
        i++;
        return frame;
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

    public static Bitmap rotateBitmap(Bitmap source) {
        Matrix matrix = new Matrix();
        matrix.postScale(-1, 1, source.getWidth()/2.0f, source.getHeight()/2.0f);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public ArrayList<Bitmap[]> getAnimations() {
        return animations;
    }

    public void setAnimations(ArrayList<Bitmap[]> animations) {
        this.animations = animations;
    }

    @Override
    public Bitmap getImage() {
        return image;
    }

    @Override
    public void setImage(Bitmap image) {
        this.image = image;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public boolean isGun() {
        return gun;
    }

    public void setGun(boolean gun) {
        this.gun = gun;
    }

    public Bitmap getCurrentBitmap() {
        return currentBitmap;
    }

    public void setCurrentBitmap(Bitmap currentBitmap) {
        this.currentBitmap = currentBitmap;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public void setWidth(float width) {
        this.width = width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public void setHeight(float height) {
        this.height = height;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getFrame() {
        return frame;
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public void setRows(int rows) {
        this.rows = rows;
    }

    @Override
    public int getColumns() {
        return columns;
    }

    @Override
    public void setColumns(int columns) {
        this.columns = columns;
    }

    public void setPositionDelta(float positionDelta)
    {
        this.positionDelta = positionDelta;
    }

    public float getPositionDelta(){
        return positionDelta;
    }
}
