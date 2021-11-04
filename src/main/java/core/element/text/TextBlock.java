package core.element.text;

import core.element.Element;
import core.presentation.style.Style;

/**
 * @author Усольцев Иван
 */
public class TextBlock implements Element {

    private String body;
    private final int leftPosition;
    private final int rightPosition;
    private Style style = Style.getDefaultStyle();

    /**
     * CONSTRUCTORS
     */
    public TextBlock() {
        this.leftPosition = 0;
        this.rightPosition = 0;
        this.body = "";
    }

    public TextBlock(String body) {
        this.leftPosition = 0;
        this.rightPosition = 0;
        this.body = body;
    }

    public TextBlock(int leftPosition, int rightPosition) {
        this.leftPosition = leftPosition;
        this.rightPosition = rightPosition;
        this.body = "";
    }

    public TextBlock(int leftPosition, int rightPosition, String body) {
        this.leftPosition = leftPosition;
        this.rightPosition = rightPosition;
        this.body = body;
    }

    /**
     * METHODS
     */
    @Override
    public int getHeight() {
        return 1;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public int getLeftPosition() {
        return leftPosition;
    }

    @Override
    public int getRightPosition() {
        return rightPosition;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }
}
