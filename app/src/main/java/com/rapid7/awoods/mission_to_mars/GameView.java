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
import com.rapid7.awoods.mission_to_mars.GameObjects.ObjectInstances.Background;
import com.rapid7.awoods.mission_to_mars.GameObjects.ObjectInstances.Movementbutton;
import com.rapid7.awoods.mission_to_mars.GameObjects.ObjectInstances.Player;
import com.rapid7.awoods.mission_to_mars.GameObjects.ObjectInstances.ToolCollected;
import com.rapid7.awoods.mission_to_mars.GameObjects.PositionVector;
import com.rapid7.awoods.mission_to_mars.GameObjects.Products;
import com.rapid7.awoods.mission_to_mars.GameObjects.Tool;

import java.util.ArrayList;
import java.util.Random;

import static com.rapid7.awoods.mission_to_mars.GameObjects.Products.IDR;

/**
 * Created by awoods on 14/02/18.
 */

public class GameView extends SurfaceView implements Runnable{

    volatile boolean playing;
    private Thread gameThread = null;
    //private Player player;

    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;;
    ArrayList<GameObject> gameObjects = new ArrayList<>();
    private InputManager inputManager;

    ArrayList<GameObject> allObjects;
    ArrayList<GameObject> managedAndDrawn;
    ArrayList<GameObject> touchableObjects;
    ArrayList<GameObject> backgrounds;
    ArrayList<GameObject> ui;
    ArrayList<GameObject> toolPickUps;
    ArrayList<MovingObject> movableObjects;
    Player player;
    int count = 0;

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

        ratio = screenX/screenY;

        // Create player
        player = new Player(context, R.drawable.main_player, 2, 4, new PositionVector(0,350*ratio), "", 10,10,40,1);

        // create scene
        GameObject background = new Background(context, R.drawable.backgroundmars, new PositionVector(0,0), "b", 4800, screenY);
        GameObject background1 = new Background(context, R.drawable.backgroundmars, new PositionVector(0,0), "b", 4799*2, screenY);
        //GameObject background3 = new Background(context, R.drawable.backgroundmars, new PositionVector(0,0), "b", -4799, screenY);
        GameObject rocket = new Background(context, R.drawable.crashed_rocket, new PositionVector(60*ratio, -400*ratio), "rocket", 300*ratio, 900*ratio);


        allObjects = new ArrayList<>();
        backgrounds = new ArrayList<>();
        touchableObjects = new ArrayList<>();
        movableObjects = new ArrayList<>();
        ui = new ArrayList<>();
        this.toolPickUps = new ArrayList<>();
        managedAndDrawn = new ArrayList<>();


        // Create buttons/ ui

        float buttonWidth = 120 * ratio;
        float buttonPaddingWidth = 20 * ratio;
        float buttonPaddingHeight = 20 * ratio;
        Movementbutton leftButton = new Movementbutton(context, R.drawable.blank_button, new PositionVector(buttonPaddingWidth,screenY - (buttonWidth/2*ratio) - buttonPaddingHeight), "left", buttonWidth, buttonWidth, player, false);
        Movementbutton rightButton = new Movementbutton(context, R.drawable.blank_button, new PositionVector((buttonPaddingWidth) + buttonWidth,screenY - (buttonWidth/2*ratio) - buttonPaddingHeight), "right", buttonWidth, buttonWidth, player, true);

//        Movementbutton weaponButton = new Movementbutton(context, R.drawable.blank_button, new PositionVector(screenX - (buttonWidth/2),screenY - (buttonWidth/2*ratio) - buttonPaddingHeight), "left", buttonWidth, buttonWidth, player, true);
//        Movementbutton jumpButton = new Movementbutton(context, R.drawable.blank_button, new PositionVector(screenX - (buttonWidth + buttonPaddingWidth + (2*buttonPaddingWidth)),screenY - (buttonWidth/2*ratio) - buttonPaddingHeight), "left", buttonWidth, buttonWidth, player, true);


        touchableObjects.add(leftButton);
        touchableObjects.add(rightButton);
//        touchableObjects.add(weaponButton);
//        touchableObjects.add(jumpButton);

        inputManager = new InputManager(touchableObjects, this, new PositionVector(screenX, screenY));

        ui.addAll(touchableObjects);
        allObjects.add(rocket);
        backgrounds.add(background);
        backgrounds.add(background1);
        // Set up draw objects

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

            if (player.getPosition().x > backgrounds.get(0).getPosition().x + 4799) {

                GameObject b = backgrounds.get(0);
                backgrounds.remove(0);
                b.setPosition(new PositionVector(backgrounds.get(0).getPosition().x + 4799,0.0f));

                backgrounds.add(
                      b
                );

                Random rand = new Random();
                int n = rand.nextInt(5);

                if (n<3 && count <4) {
                    generateTool(count, player);
                }
            }

            canvas = surfaceHolder.lockCanvas();

            canvas.drawColor(Color.WHITE);
            canvas.translate(0, 0);


            //adding background

            for (GameObject uiObject : ui) {
                uiObject.draw(canvas, paint);
            }

            canvas.translate(-player.getPosition().x, 0);


            for (GameObject object : backgrounds) {
                // paint.setColor(Color.BLACK);
                object.draw(canvas, paint);

            }

            for (GameObject object : allObjects) {
                // paint.setColor(Color.BLACK);
                object.draw(canvas, paint);

            }

            for (GameObject object : managedAndDrawn) {
                // paint.setColor(Color.BLACK);
                object.draw(canvas, paint);

            }

            for (GameObject object : toolPickUps) {
                // paint.setColor(Color.BLACK);
                object.draw(canvas, paint);

                if (player.getPosition().x > object.getPosition().x){
                    toolPickUps.remove(object);
                    pickUpTool(player, count);
                    count++;
                }

            }

            player.draw(canvas, paint);

            player.update(backgrounds.get(0).getPosition().x);
            canvas.translate(0, 0);

            // player.draw(canvas, paint);
            for (GameObject uiObject : ui) {
                uiObject.draw(canvas, paint);
            }
            surfaceHolder.unlockCanvasAndPost(canvas);

            for (GameObject object: managedAndDrawn) {
                if (player.getPosition().x > object.getPosition().x){
                    managedAndDrawn.remove(object);
                }
            }
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

    private void pickUpTool(Player player, int count) {

        switch (count) {
            case 0:
                player.addTool(new Tool("IDR", "Identify and respond to threats"));
                ToolCollected tc = new ToolCollected(this.getContext(), R.drawable.idr,
                        new PositionVector(player.getPosition().x + (300*ratio),
                                player.getPosition().y + (-330*ratio)), "idr", 1000, 1000);

                managedAndDrawn = new ArrayList<>();
                managedAndDrawn.add(tc);

                break;

            case 1:
                player.addTool(new Tool("IDR", "Identify and respond to threats"));
                ToolCollected tc1 = new ToolCollected(this.getContext(), R.drawable.heisenberg,
                        new PositionVector(player.getPosition().x + (300*ratio),
                                player.getPosition().y + (-330*ratio)), "idr", 1000, 1000);

                managedAndDrawn = new ArrayList<>();
                managedAndDrawn.add(tc1);

                break;

            case 2:
                player.addTool(new Tool("IDR", "Identify and respond to threats"));
                ToolCollected tc2 = new ToolCollected(this.getContext(), R.drawable.vm,
                        new PositionVector(player.getPosition().x + (300*ratio),
                                player.getPosition().y + (-330*ratio)), "idr", 1000, 1000);

                managedAndDrawn = new ArrayList<>();
                managedAndDrawn.add(tc2);

                break;

            case 3:
                player.addTool(new Tool("IDR", "Identify and respond to threats"));
                ToolCollected tc3 = new ToolCollected(this.getContext(), R.drawable.appsec,
                        new PositionVector(player.getPosition().x + (300*ratio),
                                player.getPosition().y + (-330*ratio)), "idr", 1000, 1000);

                managedAndDrawn = new ArrayList<>();
                managedAndDrawn.add(tc3);

                break;


        }
    }

    private void generateTool(int toolCount, Player player) {

        switch (toolCount){
            case 0:
                toolPickUps.add(new Background(this.getContext(), R.drawable.heisenberg,
                        new PositionVector(player.getPosition().x + 500, player.getPosition().y),
                        "idr", 100, 100));
                break;

            case 1:
                toolPickUps.add(new Background(this.getContext(), R.drawable.heisenberg,
                        new PositionVector(player.getPosition().x + 500, player.getPosition().y),
                        "idr", 100, 100));
                break;

            case 2:
                toolPickUps.add(new Background(this.getContext(), R.drawable.heisenberg,
                        new PositionVector(player.getPosition().x + 500, player.getPosition().y),
                        "idr", 100, 100));
                break;

            case 3:
                toolPickUps.add(new Background(this.getContext(), R.drawable.heisenberg,
                        new PositionVector(player.getPosition().x + 500, player.getPosition().y),
                        "idr", 100, 100));
                break;

        }
    }


}
