package ritogaems.tov;

import android.util.Log;

import ritogaems.tov.gameEngine.ElapsedTime;
import ritogaems.tov.gameEngine.Input.Input;
import ritogaems.tov.gameEngine.ScreenManager;
import ritogaems.tov.world.screens.GameScreen;

/**
 * @author Michael Purdy
 *         Loop that performs game update on separate thread
 *         from draw loop
 */
public class UpdateLoop implements Runnable {

    // /////////////////////////////
    // Properties
    // /////////////////////////////

    private ScreenManager screenManager;
    private float averageFPS;

    private Input input;

    // /////////////////////////////
    // Constructors
    // /////////////////////////////

    /**
     * Constructor for update loop
     *
     * @param screenManager The manager for the current game screen
     * @param input         The manager for input to the game
     * @param targetFPS     The target number of updates per second
     */
    public UpdateLoop(ScreenManager screenManager, Input input, int targetFPS) {
        this.screenManager = screenManager;
        this.averageFPS = 0;
        this.input = input;

        // set target time per frame
        targetStepPeriod = 1000000000 / targetFPS;

        // create a new time structure
        elapsedTime = new ElapsedTime();

        // create update/draw locks
        update = new BooleanLock(false);
        draw = new BooleanLock(false);
    }

    public float getAverageFPS() {
        return averageFPS;
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

                // trigger update
                synchronized (update) {
                    update.isLocked = true;
                }
                doUpdate(elapsedTime);

                // wait for update
                synchronized (update) {
                    if (update.isLocked) {
                        update.wait();
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
    private void notifyUpdateCompleted() {
        synchronized (update) {
            update.isLocked = false;
            update.notifyAll();
        }
    }

    // notify game loop that draw is completed
    public void notifyDrawCompleted() {
        synchronized (draw) {
            draw.isLocked = false;
            draw.notifyAll();
        }
    }

    // pause the update loop
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

    // resume the update loop
    public void resume() {
        running = true;

        draw.isLocked = false;
        update.isLocked = false;

        renderThread = new Thread(this);
        renderThread.start();
    }

    private void doUpdate(ElapsedTime elapsedTime) {
        // reset accumulators for keys/touch events for current frame
        input.resetAccumulators();

        // get and update current game screen
        GameScreen gameScreen = screenManager.getCurrentScreen();
        if (gameScreen != null) {
            gameScreen.update(elapsedTime);           // GameScreen not being recognised as class
        }                                             // update() getCurrentScreen() methods don't exist

        // notify the update loop
        notifyUpdateCompleted();
    }
}
