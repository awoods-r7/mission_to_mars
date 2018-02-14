package ritogaems.tov.gameEngine.graphics;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import ritogaems.tov.GameFragment;
import ritogaems.tov.world.screens.GameScreen;

/**
 * @author Michael
 *         Taken as is from Gage
 */
public class CanvasRenderSurface extends SurfaceView implements IRenderSurface {

    /**
     * Maintains the surface
     */
    private SurfaceHolder holder;

    /**
     * Constructor
     *
     * @param gameFragment The game fragment the surface belongs to
     */
    public CanvasRenderSurface(GameFragment gameFragment) {
        super(gameFragment.getActivity());

        holder = getHolder();
    }

    /**
     * Android life cycle pause
     */
    public void pause() {
    }

    /**
     * Android life cycle resume
     */
    public void resume() {
    }

    /**
     * Render the game screen to the canvas
     *
     * @param gameScreen The game screen to draw
     */
    @Override
    public void render(GameScreen gameScreen) {
        if (!holder.getSurface().isValid()) return;
        Canvas canvas = holder.lockCanvas();
        gameScreen.draw(canvas);
        holder.unlockCanvasAndPost(canvas);
    }

    /**
     * Getter
     *
     * @return SurfaceView as View
     */
    @Override
    public View getAsView() {
        return this;
    }
}