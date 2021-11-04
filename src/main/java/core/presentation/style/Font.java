package core.presentation.style;

import core.util.ColorConstants;

import java.util.Objects;

/**
 * @author Усольцев Иван
 */
public final class Font implements Cloneable{

    public static final String TIMES_NEW_ROMAN = "TimesNewRoman";
    public static final String ARIAL = "Arial";
    public static final String CALIBRI = "Calibri";

    private boolean italic = false;
    private boolean bold = false;
    private String fontName = TIMES_NEW_ROMAN;
    private short height = 12;
    private String color = ColorConstants.BLACK;

    /**
     * CONSTRUCTORS
     */
    public Font() {}

    public Font(String fontName, int height) {
        this.fontName = fontName;
        this.height = (short) height;
    }

    /**
     * METHODS
     */
    public boolean isChangedDefaultColor(){
        return !color.equals(ColorConstants.BLACK);
    }

    @Override
    protected Font clone() throws CloneNotSupportedException {
        return (Font) super.clone();
    }

    public boolean getItalic() {
        return italic;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;
    }

    public boolean getBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public short getHeight() {
        return height;
    }

    public void setHeight(short height) {
        this.height = height;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Font)) return false;
        Font font = (Font) o;
        return getItalic() == font.getItalic() &&
                getBold() == font.getBold() &&
                getHeight() == font.getHeight() &&
                Objects.equals(getFontName(), font.getFontName()) &&
                Objects.equals(getColor(), font.getColor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getItalic(), getBold(), getFontName(), getHeight(), getColor());
    }
}
