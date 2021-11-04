package core.exception;

import core.presentation.Presentation;

/**
 * @author Усольцев Иван
 */
public class MetaModelException extends RuntimeException {

    /**
     * CONSTRUCTORS
     */
    public MetaModelException(Class<? extends Presentation> clazz, String message) {
        this("Ошибка при извлечении метаданных класса " + clazz + " для построения элемента. " + message);
    }

    public MetaModelException(String message) {
        super(message);
    }
}
