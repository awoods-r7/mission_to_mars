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
    Bitmap bitmap;

    public GameView(Context context, int screenX, int screenY) {
        super(context);
        //player = new Player(context, screenX, screenY);
        //bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher_background);
        //canvas.drawBitmap(bitmap, 50, 50, paint);
        surfaceHolder = getHolder();
        paint = new Paint();
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
        //player.update();
    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);

            //canvas.drawBitmap(
                    //player.getBitmap(),
                    //player.getX(),
                    //player.getY(),
                    //paint);
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


    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                //player.stopBoosting();
                break;
            case MotionEvent.ACTION_DOWN:
                //player.setBoosting();
                break;
        }
        return true;
    }
}
