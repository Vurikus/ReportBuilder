package core.presentation;

/**
 * @author Усольцев Иван
 */
public abstract class TableRowPresentation implements Presentation {

    protected TableRowPresentation(){}

    public static TableRowPresentation create(Object ... obj){
        throw new RuntimeException("Method has to override");
    }
}
