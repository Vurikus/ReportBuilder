package core.writer;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import core.presentation.style.Style;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import java.io.IOException;

/**
 * @author Усольцев Иван
 */
public class PdfStyle {

    private Font font;
    private int border;
//    private int rowHeight;
//    private int columnWidth;
    private BaseColor backgroundColor;
    private int h_align;
    private int v_align;
    private boolean noWrap;

    private static BaseFont baseFont;

    static{
        try {
            baseFont = BaseFont.createFont("font.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        }catch(Exception e){
            e.printStackTrace();
            try {
                baseFont = BaseFont.createFont();
            } catch (DocumentException | IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * CONSTRUCTORS
     */
    public PdfStyle(Style style) {
        this.font = convertFont(style.getFont());
        this.border = convertBorder(style.getBorder());
//        this.rowHeight = rowHeight;
//        this.columnWidth = columnWidth;
        this.backgroundColor = convertColor(style.getBackgroundColor());
        this.h_align = convertHorizontalAlign(style.getH_align());
        this.v_align = convertVerticalAlign(style.getV_align());
        this.noWrap = !style.getWrapText();
    }

    /**
     * METHODS
     */


    public Font getFont() {
        return font;
    }

    public int getBorder() {
        return border;
    }

    public BaseColor getBackgroundColor() {
        return backgroundColor;
    }

    public int getH_align() {
        return h_align;
    }

    public int getV_align() {
        return v_align;
    }

    public boolean getNoWrap() {
        return noWrap;
    }

    public static int convertVerticalAlign(VerticalAlignment align){
        int a;
        switch (align){
            case TOP: a = PdfPCell.ALIGN_TOP;
                break;
            case BOTTOM: a = PdfPCell.ALIGN_BOTTOM;
                break;
            case CENTER: a = PdfPCell.ALIGN_MIDDLE;
                break;
            default: a = PdfPCell.ALIGN_JUSTIFIED;
        }
        return a;
    }

    public static int convertHorizontalAlign(HorizontalAlignment align){
        int a;
        switch (align){
            case LEFT: a = PdfPCell.ALIGN_LEFT;
                break;
            case RIGHT: a = PdfPCell.ALIGN_RIGHT;
                break;
            case CENTER: a = PdfPCell.ALIGN_CENTER;
                break;
            default: a = PdfPCell.ALIGN_JUSTIFIED;
        }
        return a;
    }

    public static int convertBorder(BorderStyle border){
        return border.getCode() == 0 ? PdfPCell.NO_BORDER : PdfPCell.BOX;
    }

    public static BaseColor convertColor(String colorRGB){
        int argb = Integer.decode(colorRGB);
        int r = argb >> 16 & 255;
        int g = argb >> 8 & 255;
        int b = argb & 255;
        return new BaseColor(r, g, b, 255);
    }

    public static Font convertFont(core.presentation.style.Font tf){
        return convertFont(tf, baseFont);
    }

    public static Font convertFont(core.presentation.style.Font tf, BaseFont baseFont){
        BaseColor fontColor = convertColor(tf.getColor());
        Font font = baseFont != null ? new Font(baseFont) : new Font();
        if(tf.getBold() && tf.getItalic()) font.setStyle(Font.BOLDITALIC);
        else if(tf.getBold()) font.setStyle(Font.BOLD);
        else if(tf.getItalic()) font.setStyle(Font.ITALIC);
        font.setColor(fontColor);
        font.setSize(tf.getHeight());
        return font;
    }
}
