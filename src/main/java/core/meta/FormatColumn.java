package core.meta;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Усольцев Иван
 */
@Target({FIELD})
@Retention(RUNTIME)
public @interface FormatColumn {
    String pattern() default "";
    int length() default -1;
}
