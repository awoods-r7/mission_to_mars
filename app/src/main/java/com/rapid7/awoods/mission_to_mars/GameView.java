package com.rapid7.awoods.mission_to_mars;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.rapid7.awoods.mission_to_mars.GameEngine.InputManager;
import com.rapid7.awoods.mission_to_mars.GameObjects.Button;
import com.rapid7.awoods.mission_to_mars.GameObjects.GameObject;
import com.rapid7.awoods.mission_to_mars.GameObjects.MovingObject;
import com.rapid7.awoods.mission_to_mars.GameObjects.ObjectInstances.Movementbutton;
import com.rapid7.awoods.mission_to_mars.GameObjects.ObjectInstances.Player;
import com.rapid7.awoods.mission_to_mars.GameObjects.PositionVector;

import java.util.ArrayList;

/**
 * Created by awoods on 14/02/18.
 */

public class GameView extends SurfaceView implements Runnable{

    volatile boolean playing;
    private Thread gameThread = null;
    //private Player player;

    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private InputManager inputManager;

    ArrayList<GameObject> allObjects;
    ArrayList<GameObject> touchableObjects;
    ArrayList<GameObject> ui;
    ArrayList<MovingObject> movableObjects;
    Player player;

    float screenX;
    float screenY;
    float ratio;

    public GameView(Context context, float screenX, float screenY) {
        super(context);
        resume();
        surfaceHolder = getHolder();
        paint = new Paint();
        this.screenX = screenX;
        this.screenY = screenY;

        allObjects = new ArrayList<>();
        touchableObjects = new ArrayList<>();
        movableObjects = new ArrayList<>();
        ui = new ArrayList<>();


        player = new Player(context, R.drawable.blank_button, new PositionVector(0,30), "", 100,100,1,1);

        // Create buttons/ ui

        ratio = screenX/screenY;
        float buttonWidth = 120 * ratio;
        float buttonPaddingWidth = 20 * ratio;
        float buttonPaddingHeight = 20 * ratio;
        Movementbutton leftButton = new Movementbutton(context, R.drawable.blank_button, new PositionVector(buttonPaddingWidth,screenY - (buttonWidth/2*ratio) - buttonPaddingHeight), "left", buttonWidth, buttonWidth, player, false);
        Movementbutton rightButton = new Movementbutton(context, R.drawable.blank_button, new PositionVector((buttonPaddingWidth) + buttonWidth,screenY - (buttonWidth/2*ratio) - buttonPaddingHeight), "right", buttonWidth, buttonWidth, player, true);
//
//        Movementbutton weaponButton = new Movementbutton(context, R.drawable.blank_button, new PositionVector(screenX - (buttonWidth/2),screenY - (buttonWidth/2*ratio) - buttonPaddingHeight), "left", buttonWidth, buttonWidth, player, true);
//        Movementbutton jumpButton = new Movementbutton(context, R.drawable.blank_button, new PositionVector(screenX - (buttonWidth + buttonPaddingWidth + (2*buttonPaddingWidth)),screenY - (buttonWidth/2*ratio) - buttonPaddingHeight), "left", buttonWidth, buttonWidth, player, true);


        touchableObjects.add(leftButton);
        touchableObjects.add(rightButton);
//        touchableObjects.add(weaponButton);
//        touchableObjects.add(jumpButton);

        inputManager = new InputManager(touchableObjects, this, new PositionVector(screenX, screenY));

        touchableObjects.add(leftButton);
        ui.add(rightButton);
        // Set up draw objects


        //allObjects.addAll(touchableObjects);
    }

    @Override
    public void run() {
        while (playing) {
            update();
            draw();
            control();
        }
    }

    private void update() {

    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {

            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.WHITE);

            for (GameObject uiObject:ui) {
                uiObject.draw(canvas, paint);
            }
            canvas.translate(-player.getPosition().x, 0);
            
            for (GameObject object: touchableObjects) {
                paint.setColor(Color.BLACK);
                object.draw(canvas, paint);

            }
            player.update();
            canvas.translate(0, 0);
            player.draw(canvas, paint);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }

    }

    private void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }


}
