package ritogaems.tov;

import android.app.Fragment;
import android.graphics.Point;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;

import ritogaems.tov.gameEngine.Difficulty;
import ritogaems.tov.gameEngine.Input.Input;
import ritogaems.tov.gameEngine.AssetStore;
import ritogaems.tov.gameEngine.graphics.CanvasRenderSurface;
import ritogaems.tov.gameEngine.graphics.IRenderSurface;
import ritogaems.tov.gameEngine.ScreenManager;
import ritogaems.tov.gameEngine.io.FileHandler;
import ritogaems.tov.world.screens.Menu.LoadingScreen;
import ritogaems.tov.world.screens.Menu.StartScreen;

/**
 * @author Kevin Martin
 *         A placeholder fragment containing a simple view.
 */
public class GameFragment extends Fragment implements Serializable {

    /*
    FRAMES PER SECOND
     */

    // target FPS
    private int targetFPS;

    /*
    MANAGERS
     */

    // declare managers
    private AssetStore assetStore;
    private ScreenManager screenManager;
    private Input input;
    private FileHandler fileHandler;
    private IRenderSurface renderSurface;

    // getters
    public AssetStore getAssetStore() {
        return assetStore;
    }

    public ScreenManager getScreenManager() {
        return screenManager;
    }

    public Input getInput() {
        return input;
    }

    public FileHandler getFileIO() {
        return fileHandler;
    }

    public UpdateLoop updateLoop;
    public DrawLoop drawLoop;

    /**
     * The difficulty of the game
     */
    public Difficulty difficulty;

    /*
    SCREEN SIZE
     */

    // width and height
    private int screenWidth = -1;
    private int screenHeight = -1;

    // getters
    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    /*
    STATE MANAGEMENT
     */

    // initialising non-view/context managers and game loop
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialise managers
        fileHandler = new FileHandler(getActivity().getApplicationContext());
        assetStore = new AssetStore(fileHandler);

        renderSurface = new CanvasRenderSurface(this);

        // request control of the volume
        getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    // initialising view/context dependant managers
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // create the output view and associated renderer
        View view = renderSurface.getAsView(); // getAsView is a method that returns renderSurface

        // get input from view
        input = new Input(view);

        // screen manager
        screenManager = new ScreenManager();

        // store the size of the window
        Point size = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getRealSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        targetFPS = 30;

        // create a new game loop
        updateLoop = new UpdateLoop(screenManager, input, targetFPS);
        drawLoop = new DrawLoop(screenManager, targetFPS, renderSurface);

        // create start menu screen
        StartScreen startScreen = new StartScreen(this, "startMenu");
        screenManager.addScreen(startScreen);
        screenManager.setAsCurrentScreen(startScreen.getName());

        // create the loading screen
        screenManager.addScreen(new LoadingScreen(this, "loadingScreen"));

        return view;
    }

    /*
    PAUSE/RESUME/UPDATE/DRAW
     */

    @Override
    public void onPause() {
        // pause the game loop
        updateLoop.pause();
        drawLoop.pause();

        // if needed, pause the current game screen
        if (screenManager.getCurrentScreen() != null) {
            screenManager.getCurrentScreen().pause();    // getCurrentScreen() method don't exist
        }

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        // if needed, resume the current game screen
        if (screenManager.getCurrentScreen() != null) {
            screenManager.getCurrentScreen().resume();    // getCurrentScreen() method don't exist
        }

        // resume the update and draw loop
        updateLoop.resume();
        drawLoop.resume();
    }

    @Override
    public void onDestroy() {
        // dispose of any game screens
        screenManager.dispose();   // dispose() method don't exist

        super.onDestroy();
    }

}