package core.element.picture;

import core.element.Element;

import java.io.IOException;

/**
 * @author Усольцев Иван
 */
public interface PictureElement extends Element {

    void setLeftPosition(int pos);
    void setRightPosition(int pos);
    void setTopPosition(int pos);
    void setBottomPosition(int pos);

    int getLeftPosition();
    int getRightPosition();
    int getTopPosition();
    int getBottomPosition();

    void setAutoSize(boolean b);
    boolean getAutoSize();
    void resize(int width, int height);

    byte[] toByteArray() throws IOException;
}
