package core.writer;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import core.element.picture.PictureElement;
import core.element.table.HeaderColumn;
import core.element.table.Table;
import core.element.text.TextBlock;
import core.presentation.TableRowPresentation;
import core.presentation.style.Style;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

/**
 * @author Усольцев Иван
 */
public class PdfConstructor implements DocumentConstructor {

    private final Document document;
    private Map<Style, PdfStyle> styles;

//    private BaseFont baseFont;

    /**
     * CONSTRUCTORS
     */
    PdfConstructor() {
        document = new Document();
        styles = new HashMap<>();
//        try {
//            baseFont = BaseFont.createFont("font.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//        }catch(Exception e){
//            e.printStackTrace();
//            try {
//                baseFont = BaseFont.createFont();
//            } catch (DocumentException | IOException ex) {
//                ex.printStackTrace();
//            }
//        }
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

            this.fillStyles(table.getAllStyles());

            PdfPTable pdfTable = new PdfPTable(table.getColumnCount());
            this.createTableHeaders(table, pdfTable);

            pdfTable.addCell("row 1, col 1");
            pdfTable.addCell("row 1, col 2");
            pdfTable.addCell("row 1, col 3");
            pdfTable.addCell("row 2, col 1");
            pdfTable.addCell("row 2, col 2");
            pdfTable.addCell("row 2, col 3");

            document.add(pdfTable);
            document.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * INNER METHODS
     */
    private <T extends TableRowPresentation> void createTableHeaders(Table<T> table, PdfPTable pdfTable) {

        Style headerStyle = table.getHeaderStyle();

//        Font font = this.convertFont(headerStyle);
        final List<HeaderColumn> headers = new ArrayList<>(table.getHeaders());
        headers.sort(Comparator.comparing(HeaderColumn::getStartPosition));

//        BaseColor backgroundColor = this.convertColor(headerStyle.getBackgroundColor());

        final int maxLevel = table.getHeaders().first().getLevel();
        int currentRow = 1;

        int i = 0;
        int bufferLength = table.getColumnCount();

        while(i < headers.size()){
            HeaderColumn hc = headers.get(i);
            PdfPCell cell = new PdfPCell();
            this.applyStyleToCell(cell, headerStyle);
            int rowSpan;
            if(hc.hasChild()) {
                rowSpan = 1;
                List<HeaderColumn> children = new ArrayList<>(hc.getChildren());
                children.sort(Comparator.comparing(HeaderColumn::getStartPosition));
                headers.addAll(children);
            }
            else {
                bufferLength--;
                rowSpan = maxLevel - currentRow + 1;
            }
            this.setCellSize(cell, -1, table.getSizeHeaderRow() * rowSpan);
            cell.setRowspan(rowSpan);
            cell.setColspan(hc.getLength());
            PdfStyle pdfStyle = this.styles.get(headerStyle);
            Font font = pdfStyle != null ? pdfStyle.getFont() : PdfStyle.convertFont(headerStyle.getFont());
            cell.setPhrase(new Phrase(hc.getName(), font));
            pdfTable.addCell(cell);

            if(hc.getLastPosition() == table.getRightPosition() || (hc.getLength() == bufferLength && hc.getLevel() > 1)) {
                currentRow ++;
            }
            i++;
        }
    }

    private void setCellSize(PdfPCell cell, int width, int height){
        if(height != -1){
            cell.setCalculatedHeight((float)height/50);
        }
        if(width != -1){
            PdfPTable table = cell.getTable();
            table.setTotalWidth((float)width/1000);
        }
    }

    private void applyStyleToCell(PdfPCell cell, Style st){
        PdfStyle pst = this.styles.get(st);
        if(pst == null) return;
        cell.setBorder(pst.getBorder());
        cell.setHorizontalAlignment(pst.getH_align());
        cell.setVerticalAlignment(pst.getV_align());
        cell.setBackgroundColor(pst.getBackgroundColor());
        cell.setNoWrap(pst.getNoWrap());
    }

    private void fillStyles(Collection<Style> styles){
        for(Style s : styles){
            if(this.styles.containsKey(s)) continue;
            PdfStyle ps = new PdfStyle(s);
            this.styles.put(s, ps);
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
