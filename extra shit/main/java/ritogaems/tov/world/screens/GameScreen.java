package ritogaems.tov.world.screens;

import android.graphics.Canvas;

import java.io.Serializable;

import ritogaems.tov.GameFragment;
import ritogaems.tov.gameEngine.ElapsedTime;
import ritogaems.tov.gameEngine.entity.Player;

public abstract class GameScreen implements Serializable {

    /**
     * Different types of screen
     */
    public enum ScreenType {
        MENU,
        PLAY
    }

    /**
     * name and game to which it belongs
     */
    protected final String name;
    protected final GameFragment game;

    /**
     * Type of screen
     */
    private final ScreenType type;

    /**
     * Game screen constructor
     *
     * @param game The game the screen belongs to
     * @param name The name of the screen
     * @param type The type of screen it is
     */
    protected GameScreen(GameFragment game, String name, ScreenType type) {
        this.name = name;
        this.game = game;
        this.type = type;
    }

    /**
     * Getter
     *
     * @return screen type
     */
    public ScreenType getType() {
        return type;
    }

    /**
     * update the game screen - called from game
     *
     * @param elapsedTime Time since the last update
     */
    public abstract void update(ElapsedTime elapsedTime);

    /**
     * draw the game screen
     *
     * @param canvas The canvas to draw the screen to
     */
    public abstract void draw(Canvas canvas);

    /**
     * Getter
     *
     * @return name of the screen
     */
    public String getName() {
        return name;
    }

    /**
     * Getter
     *
     * @return game the screen belongs to
     */
    public GameFragment getGame() {
        return game;
    }

    /**
     * Getter returns null unless specifically overridden by sub class
     *
     * @return player on the screen (default null)
     */
    public Player getPlayer() {
        return null;
    }

    /**
     * called automatically when app is paused
     */
    public void pause() {

    }

    /**
     * called automatically when app is resumed
     */
    public void resume() {

    }

    /**
     * called automatically when app is disposed
     */
    public void dispose() {

    }

    /**
     * Pauses the screen's background music
     */
    public abstract void pauseMusic();

    /**
     * Plays the screen's background music
     */
    public abstract void playMusic();
}