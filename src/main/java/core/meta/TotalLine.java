package core.meta;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Усольцев Иван
 */
@Target({TYPE})
@Retention(RUNTIME)
public @interface TotalLine {
    String title() default "Итого: ";
    int titlePosition() default -1;
    TextStyle lineStyle() default @TextStyle;
}
