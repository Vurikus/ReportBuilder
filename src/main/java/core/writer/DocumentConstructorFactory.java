package core.writer;

import core.Format;

/**
 * @author Усольцев Иван
 */
public class DocumentConstructorFactory {

    /**
     * CONSTRUCTORS
     */
    private DocumentConstructorFactory(){}

    /**
     * METHODS
     */
    public static DocumentConstructor getConstructor(Format format){
        if(format == Format.EXCEL) return new ExcelConstructor();
        else throw new IllegalArgumentException("Формат пока не реализован");
    }

}
