package core.writer;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import core.element.picture.PictureElement;
import core.element.table.Table;
import core.element.text.TextBlock;
import core.presentation.TableRowPresentation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * @author Усольцев Иван
 */
public class PdfConstructor implements DocumentConstructor{

    private final Document document;

    /**
     * CONSTRUCTORS
     */
    PdfConstructor(){
        document = new Document();
    }

    /**
     * METHODS
     */
    @Override
    public int createPageAndSetCurrent(String pageName) {
        return 0;
    }

    @Override
    public int getCurrentPageIndex() {
        return 0;
    }

    @Override
    public <T extends TableRowPresentation> void createTable(Table<T> table) {
        try {
            PdfWriter.getInstance(document, new FileOutputStream("iTextTable.pdf"));

            document.open();

            PdfPTable t = new PdfPTable(3);
            Stream.of("column header 1", "column header 2", "column header 3")
                    .forEach(columnTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        header.setBorderWidth(2);
                        header.setPhrase(new Phrase(columnTitle));
                        t.addCell(header);
                    });
            t.addCell("row 1, col 1");
            t.addCell("row 1, col 2");
            t.addCell("row 1, col 3");

            document.add(t);
            document.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void createTextBlock(TextBlock text) {

    }

    @Override
    public void createImage(PictureElement pe) {

    }

    @Override
    public void setPrintSetting(PrintSetting ps) {

    }

    @Override
    public void setPrintSetting(int pageIndex, PrintSetting ps) {

    }

    @Override
    public Path writeToFile(String fileName) throws IOException {
        return null;
    }
}
