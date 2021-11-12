package core.writer;

import core.Format;

/**
 * @author Усольцев Иван
 */
public class DocumentConstructorFactory {

    private String path = "../";

    /**
     * CONSTRUCTORS
     */
    public DocumentConstructorFactory(){}

    /**
     * METHODS
     */
    public DocumentConstructor getConstructor(Format format){
        if(format == Format.EXCEL) return new ExcelConstructor(path);
        else if(format == Format.PDF) return new PdfConstructor(path);
        else throw new IllegalArgumentException("Формат пока не реализован");

    }

    public String getReportSavePath() {
        return path;
    }

    public void setReportSavePath(String path) {
        this.path = path;
    }
}
