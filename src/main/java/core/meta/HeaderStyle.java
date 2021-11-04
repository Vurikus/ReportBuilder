package core.meta;

import core.util.ColorConstants;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Усольцев Иван
 */
@Target({TYPE})
@Retention(RUNTIME)
public @interface HeaderStyle {
    int fontSize() default 12;
    String fontName() default "TimesNewRoman";
    boolean bold() default false;
    boolean italic() default false;
    String fontColor() default ColorConstants.BLACK;
    HorizontalAlignment h_align() default HorizontalAlignment.CENTER;
    VerticalAlignment v_align() default VerticalAlignment.CENTER;
    BorderStyle border() default BorderStyle.MEDIUM;
    String backgroundColor() default ColorConstants.WHITE;
    short height() default -1;
}
