package core.meta;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Усольцев Иван
 */
@Target({TYPE})
@Retention(RUNTIME)
public @interface GroupTotalLine {
    int groupByColumn();
    boolean includeDelimiterInTitle() default true;
    String title() default "Итого: ";
    TextStyle lineStyle() default @TextStyle;
}

