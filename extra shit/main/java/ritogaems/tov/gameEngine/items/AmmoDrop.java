package ritogaems.tov.gameEngine.items;

import android.graphics.Canvas;

import ritogaems.tov.util.GraphicsUtil;
import ritogaems.tov.world.screens.MapScreen;
import ritogaems.tov.world.viewports.LayerViewport;
import ritogaems.tov.world.viewports.ScreenViewport;

/**
 * @author Michael Purdy
 *         A consumable that contains a certain amount of ammo
 */
public class AmmoDrop extends Consumable {

    /**
     * The amount of ammo in the drop
     */
    private int amount;

    /**
     * The current rotation of the drop (in degrees)
     */
    private int rotateDegrees;

    /**
     * The type of ammo
     */
    private Inventory.AmmoType ammoType;

    /**
     * Constructor
     *
     * @param xPos           X position (centre in game units)
     * @param yPos           Y position (centre in game units)
     * @param width          Width (game units)
     * @param height         Height (game units)
     * @param amount         Amount of ammo
     * @param ammoType       Type of ammo
     * @param checkCollision Check the collision on creation
     *                       (false for when generating in a map because map tiles don't yet exist
     *                       - useful when dropped by entity)
     * @param mapScreen      The map screen the drop belongs to
     */
    public AmmoDrop(float xPos, float yPos, float width, float height, int amount,
                    Inventory.AmmoType ammoType, boolean checkCollision, MapScreen mapScreen) {
        super(xPos, yPos, width, height,
                mapScreen.getGame().getAssetStore().getBitmap(ammoType.getAmmoDropName(), ammoType.getAmmoDropFilePath()),
                ConsumableType.AMMO, checkCollision, mapScreen);
        this.amount = amount;
        this.rotateDegrees = 0;
        this.ammoType = ammoType;
    }

    /**
     * Getter
     *
     * @return amount of ammo in the drop
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Getter
     *
     * @return drop ammo type
     */
    public Inventory.AmmoType getAmmoType() {
        return ammoType;
    }

    /**
     * Draw
     *
     * @param canvas         Canvas on which to draw
     * @param layerViewport  Layer Viewport to draw from
     * @param screenViewport Screen Viewport to translate the Layer Viewport
     */
    @Override
    public void draw(Canvas canvas, LayerViewport layerViewport, ScreenViewport screenViewport) {
        if (GraphicsUtil.getScreenRect(this, layerViewport, screenViewport)) {

            GraphicsUtil.drawMatrix.reset();
            GraphicsUtil.drawMatrix.postTranslate(GraphicsUtil.drawScreenRect.exactCenterX(),
                    GraphicsUtil.drawScreenRect.exactCenterY());
            GraphicsUtil.drawMatrix.preRotate(rotateDegrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
            canvas.drawBitmap(bitmap, GraphicsUtil.drawMatrix, null);


            // increase rotation each draw to spin drop
            rotateDegrees += 10;
            if (rotateDegrees > 360) rotateDegrees = 0;
        }
    }

}
