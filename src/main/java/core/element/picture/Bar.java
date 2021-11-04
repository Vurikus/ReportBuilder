package core.element.picture;

import core.element.Element;
import core.util.ColorConstants;

/**
 * @author Усольцев Иван
 */
public class Bar implements Element {

    private int leftPosition;
    private int topPosition;
    private int rightPosition;
    private int bottomPosition;
    private int width = 0;
    private int height = 0;
    private String colorRGB = ColorConstants.BLUE;
    private String title = "";

    /**
     * CONSTRUCTORS
     */
    public Bar() {
    }

    public Bar(int leftPosition, int topPosition, int rightPosition, int bottomPosition, String colorRGB) {
        this.leftPosition = leftPosition;
        this.topPosition = topPosition;
        this.rightPosition = rightPosition;
        this.bottomPosition = bottomPosition;
        this.colorRGB = colorRGB;
    }

    /**
     * METHODS
     */
    @Override
    public int getLeftPosition() {
        return leftPosition;
    }

    public void setLeftPosition(int leftPosition) {
        this.leftPosition = leftPosition;
    }

    public int getTopPosition() {
        return topPosition;
    }

    public void setTopPosition(int topPosition) {
        this.topPosition = topPosition;
    }

    @Override
    public int getRightPosition() {
        return rightPosition;
    }

    public void setRightPosition(int rightPosition) {
        this.rightPosition = rightPosition;
    }

    public int getBottomPosition() {
        return bottomPosition;
    }

    public void setBottomPosition(int bottomPosition) {
        this.bottomPosition = bottomPosition;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getColorRGB() {
        return colorRGB;
    }

    public void setColorRGB(String colorRGB) {
        this.colorRGB = colorRGB;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
