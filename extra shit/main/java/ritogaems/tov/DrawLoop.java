package ritogaems.tov;

import android.util.Log;

import ritogaems.tov.gameEngine.ElapsedTime;
import ritogaems.tov.gameEngine.ScreenManager;
import ritogaems.tov.gameEngine.graphics.IRenderSurface;
import ritogaems.tov.world.screens.GameScreen;

/**
 * @author Michael Purdy
 *         Loop to draw the game to the screen
 *         Runs on a thread separate to the update loop
 *         <p/>
 *         Initially taken from Gage then adapted for TOV
 */
public class DrawLoop implements Runnable {

    // /////////////////////////////
    // Properties
    // /////////////////////////////

    /**
     * The surface the game is drawn to
     */
    private IRenderSurface renderSurface;

    /**
     * The manager which controls the current screen to be drawn
     */
    private ScreenManager screenManager;

    /**
     * The average frames per second being achieved by the draw loop
     */
    private float averageFPS;


    // /////////////////////////////
    // Constructors
    // /////////////////////////////

    /**
     * Constructor to create the draw loop
     *
     * @param screenManager The manager which controls the current screen to be drawn
     * @param targetFPS     The frames per second aimed for
     * @param renderSurface The surface to draw the game to
     */
    public DrawLoop(ScreenManager screenManager, int targetFPS, IRenderSurface renderSurface) {
        this.renderSurface = renderSurface;
        this.screenManager = screenManager;
        this.averageFPS = 0;

        // set target time per frame
        targetStepPeriod = 1000000000 / targetFPS;

        // create a new time structure
        elapsedTime = new ElapsedTime();

        // create update/draw locks
        update = new BooleanLock(false);
        draw = new BooleanLock(false);
    }

    // boolean lock to control update/draw inter-thread sequencing
    class BooleanLock {
        boolean isLocked;

        // constructor
        public BooleanLock(boolean isLocked) {
            this.isLocked = isLocked;
        }
    }

    // sequence locks for the update/draw
    private volatile BooleanLock update;
    private volatile BooleanLock draw;

    // thread on which the game loop will run
    private Thread renderThread = null;

    // flag if thread is running
    private volatile boolean running = false;

    // total time and frame time
    private ElapsedTime elapsedTime;

    // duration in ns of target frame period
    private long targetStepPeriod;

    // maximum frame time - 3x the target
    private double maximumStepPeriodScale = 3.0f;

    // starts the update/draw process - more sophisticated approach with prep/update/draw possible
    @Override
    public void run() {
        // ensure game screen is available to update/draw
        if (screenManager.getCurrentScreen() == null) { // getCurrentScreen doesn't exist
            String errorTag = "Gage Error:";
            String errorMessage = "You need to add a game screen to the screen manager";
            Log.e(errorTag, errorMessage);
            throw new RuntimeException(errorTag + errorMessage);
        }

        // set up initial timings
        long startRun = System.nanoTime() - targetStepPeriod;
        long startStep = startRun;
        long overSleepTime = 0L;
        long endStep, sleepTime;

        try {
            while (running) {
                // update the timing information
                long currentTime = System.nanoTime();
                elapsedTime.totalTime = (currentTime - startRun) / 1000000000.0;
                elapsedTime.stepTime = (currentTime - startStep) / 1000000000.0;
                startStep = currentTime;

                // weighted average update of averageFPS
                averageFPS = (0.85f * averageFPS) + (0.15f * (1.0f / (float) elapsedTime.stepTime));

                // ensure step time isn't too large
                if (elapsedTime.stepTime > (targetStepPeriod / 1000000000.0) * maximumStepPeriodScale) {
                    elapsedTime.stepTime = (targetStepPeriod / 1000000000.0) * maximumStepPeriodScale;
                }
                // trigger draw
                synchronized (draw) {
                    draw.isLocked = true;
                }
                doDraw(elapsedTime);

                // wait for draw
                synchronized (draw) {
                    if (draw.isLocked) {
                        draw.wait();
                    }
                }

                // check if we need to put the thread to sleep
                endStep = System.nanoTime();
                sleepTime = (targetStepPeriod - (endStep - startStep) - overSleepTime);

                // if needed put the thread to sleep
                if (sleepTime > 0) {
                    Thread.sleep(sleepTime / 1000000L); // converts ns to ms

                    // adjust base sleep time for next frame
                    overSleepTime = (System.nanoTime() - endStep) - sleepTime;
                } else {
                    overSleepTime = 0L;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // notify game loop that update is completed
    public void notifyUpdateCompleted() {
        synchronized (update) {
            update.isLocked = false;
            update.notifyAll();
        }
    }

    // notify game loop that draw is completed
    private void notifyDrawCompleted() {
        synchronized (draw) {
            draw.isLocked = false;
            draw.notifyAll();
        }
    }

    // pause the game loop
    public void pause() {
        running = false;
        while (true) {
            try {
                renderThread.join();
                return;
            } catch (InterruptedException e) {
                Log.d("Error", e.toString());
                // log something and retry?
            }
        }
    }

    // resume the game loop
    public void resume() {
        running = true;

        draw.isLocked = false;
        update.isLocked = false;

        renderThread = new Thread(this);
        renderThread.start();
    }

    private void doDraw(ElapsedTime elapsedTime) {
        // get and draw current screen
        GameScreen gameScreen = screenManager.getCurrentScreen();
        if (gameScreen != null) {
            renderSurface.render(gameScreen);
        } // see above

        notifyDrawCompleted();
    }

    public float getAverageFPS() {
        return averageFPS;
    }
}
