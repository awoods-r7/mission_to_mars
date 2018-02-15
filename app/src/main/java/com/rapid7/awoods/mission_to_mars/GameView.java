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
    Bitmap bitmap, background, rocket;
    Player player;
    ArrayList<GameObject> gameObjects = new ArrayList<>();
    private InputManager inputManager;

    ArrayList<GameObject> allObjects;
    ArrayList<GameObject> touchableObjects;
    ArrayList<MovingObject> movableObjects;
    Player player;

    public GameView(Context context, float screenX, float screenY) {
        super(context);
        resume();
        surfaceHolder = getHolder();
        paint = new Paint();
        this.setTop(screenY);
        this.setLeft(screenX);
        //adding landscape
        background = BitmapFactory.decodeResource(context.getResources(), R.drawable.backgroundmars);
        background = Bitmap.createScaledBitmap(background, 4800, screenY, true);

        //adding rocket
        rocket = BitmapFactory.decodeResource(context.getResources(), R.drawable.crashed_rocket);
        rocket = Bitmap.createScaledBitmap(rocket, 500, 900, true);

        player = new Player(context, R.drawable.main_player, 2, 4, new PositionVector(500,500), "", 10,10,0,1);
        //Movementbutton test = new Movementbutton(context, R.drawable.blank_button, new PositionVector(0,0), "", 500, 500, player, true);
        //gameObjects.add(test);
        inputManager = new InputManager(gameObjects, this);

        allObjects = new ArrayList<>();
        touchableObjects = new ArrayList<>();
        movableObjects = new ArrayList<>();


        player = new Player(context, R.drawable.blank_button, new PositionVector(0,30), "", 100,100,1,1);

        // Create buttons/ ui

        float ratio = screenX/screenY;
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

        // Set up draw objects

        allObjects.addAll(touchableObjects);
        allObjects.add(player);
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
            //make the man runnnnnnnnnnnnnnn

            //adding background
            canvas.drawBitmap(background, 0, 0, paint);

            //adding rocket
            canvas.drawBitmap(rocket, 0, -26, paint);

            player.draw(canvas, paint);

    //        for (GameObject object: gameObjects) {
      //          paint.setColor(Color.BLACK);
  //              object.draw(canvas, paint);
//
            //}
            for (GameObject object: allObjects) {
                paint.setColor(Color.BLACK);
                object.draw(canvas, paint);

            }
            player.update();
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
