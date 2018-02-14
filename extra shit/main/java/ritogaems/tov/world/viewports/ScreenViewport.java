package ritogaems.tov.world.viewports;

import android.graphics.Rect;

public class ScreenViewport {

    //The source x point of the viewport
    public int xLeft;

    //The span x point of the viewport
    private int xRight;

    //The source y point of the viewport
    public int yTop;

    //The span y point of the viewport
    private int yBottom;

    //The width of the viewport
    public int width;

    //The height of the viewport
    public int height;

    //Rectangle representation of the viewport
    private Rect VPrect = new Rect();

    //--- CONSTRUCTORS

    //Constructor for a screen viewport with custom res
    public ScreenViewport(int left, int top, int right, int bottom) {
        this.xLeft = left;
        this.xRight = right;
        this.yTop = top;
        this.yBottom = bottom;

        this.width = right - left;
        this.height = bottom - top;
    }

    //--- METHODS

    //Sets the viewport to the specified res
    public void set(int left, int top, int right, int bottom) {
        this.xLeft = left;
        this.xRight = right;
        this.yTop = top;
        this.yBottom = bottom;

        this.width = right - left;
        this.height = bottom - top;
    }

    //Returns true if specified point is within screen viewport, false otherwise
    public boolean containsPoint(int x, int y) {
        return xLeft < xRight && yTop < yBottom &&
               x >= xLeft && x < xRight && y >= yTop && y < yBottom;
    }

    //Returns true if Rect is visible within viewport (ie any portion of the rect), false if not
    public boolean containsRect(Rect rect) {
        return xLeft < xRight && yTop < yBottom &&
               rect.left <= this.xRight && rect.right >= this.xLeft &&
               rect.top <= this.yBottom && rect.bottom >= this.yTop;
    }

    //Returns the viewport as a rectangle
    public Rect toRect() {
        VPrect.left = xLeft;
        VPrect.right = xRight;
        VPrect.top = yTop;
        VPrect.bottom = yBottom;

        return this.VPrect;
    }

    //--- GETTERS

    public final int getCentreX() {
        return width /2;
    }

    public final int getCentreY() {
        return height /2;
    }

}
