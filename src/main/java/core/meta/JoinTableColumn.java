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
public @interface JoinTableColumn {
    int firstColumn();
    int lastColumn();
    short level() default 2;
    String name() default "";
}
