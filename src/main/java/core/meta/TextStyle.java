package core.meta;

import core.util.ColorConstants;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import java.awt.*;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Усольцев Иван
 */
@Target({TYPE, FIELD})
@Retention(RUNTIME)
public @interface TextStyle {
    int fontSize() default 12;
    String fontName() default "TimesNewRoman";
    boolean bold() default false;
    boolean italic() default false;
    HorizontalAlignment h_align() default HorizontalAlignment.LEFT;
    VerticalAlignment v_align() default VerticalAlignment.CENTER;
    BorderStyle border() default BorderStyle.THIN;
    String fontColor() default ColorConstants.BLACK;
    String backgroundColor() default ColorConstants.WHITE;
    short height() default -1;
}
