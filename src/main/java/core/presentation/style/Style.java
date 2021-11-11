package core.presentation.style;

import core.meta.HeaderStyle;
import core.meta.TextStyle;
import core.util.ColorConstants;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import java.awt.*;
import java.util.Objects;

/**
 * @author Усольцев Иван
 */
public final class Style implements Cloneable{

    private static final Style defaultStyle = new Style();
    private final Font font;
    private final HorizontalAlignment h_align;
    private final VerticalAlignment v_align;
    private final BorderStyle border;
    private boolean wrapText = true;
    private String backgroundColor = ColorConstants.WHITE;

    /**
     * CONSTRUCTORS
     */
    public Style(Font font, HorizontalAlignment h_align, VerticalAlignment v_align, BorderStyle border) {
        this.font = font;
        this.h_align = h_align;
        this.v_align = v_align;
        this.border = border;
    }

    private Style(){
        border = BorderStyle.NONE;
        font = new Font();
        h_align = HorizontalAlignment.LEFT;
        v_align = VerticalAlignment.TOP;
    }

    /**
     * METHODS
     */
    public static Style getDefaultStyle(){
        return defaultStyle;
    }

    public boolean isChangedDefaultColor(){
        return !backgroundColor.equals(ColorConstants.WHITE);
    }

    public static Style createFromAnnotation(TextStyle ts){
        Font font = new Font(ts.fontName(), ts.fontSize());
        font.setBold(ts.bold());
        font.setItalic(ts.italic());
        font.setColor(ts.fontColor());
        Style st = new Style(font, ts.h_align(), ts.v_align(), ts.border());
        st.backgroundColor = ts.backgroundColor();
        return st;
    }

    public static Style createFromAnnotation(HeaderStyle hs){
        Font font = new Font(hs.fontName(), hs.fontSize());
        font.setBold(hs.bold());
        font.setItalic(hs.italic());
        font.setColor(hs.fontColor());
        Style st = new Style(font, hs.h_align(), hs.v_align(), hs.border());
        st.backgroundColor = hs.backgroundColor();
        return st;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Override
    public Style clone() throws CloneNotSupportedException {
        Font fontClone = this.font.clone();
        Style clone = new Style(fontClone, this.h_align, this.v_align, this.border);
        clone.wrapText = this.wrapText;
        clone.backgroundColor = this.backgroundColor;
        return clone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Style)) return false;
        Style style = (Style) o;
        return getWrapText() == style.getWrapText() &&
                Objects.equals(getFont(), style.getFont()) &&
                getH_align() == style.getH_align() &&
                getV_align() == style.getV_align() &&
                getBorder() == style.getBorder() &&
//                Objects.equals(getPatternFormat(), style.getPatternFormat()) &&
                Objects.equals(getBackgroundColor(), style.getBackgroundColor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFont(), getH_align(), getV_align(), getBorder(), getWrapText(), getBackgroundColor());
    }

    public Font getFont() {
        return font;
    }

    public HorizontalAlignment getH_align() {
        return h_align;
    }

    public VerticalAlignment getV_align() {
        return v_align;
    }

    public BorderStyle getBorder() {
        return border;
    }

    public boolean getWrapText() {
        return wrapText;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }
}
