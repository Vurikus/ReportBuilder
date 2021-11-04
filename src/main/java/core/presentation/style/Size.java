package core.presentation.style;

/**
 * @author Усольцев Иван
 */
public class Size {
    private final short width;
    private final short height;

    /**
     * CONSTRUCTORS
     */
    public Size(short width, short height) {
        this.width = width;
        this.height = height;
    }

    /**
     * METHODS
     */
    public short getWidth() {
        return width;
    }

    public short getHeight() {
        return height;
    }
}
