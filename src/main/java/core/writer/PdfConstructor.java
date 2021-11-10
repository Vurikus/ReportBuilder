package core.writer;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import core.element.picture.PictureElement;
import core.element.table.HeaderColumn;
import core.element.table.Table;
import core.element.table.TableRow;
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
            document.setPageSize(PageSize.A4.rotate());
            document.open();

            this.fillStyles(table.getAllStyles());
            float[] relativeColumns = new float[table.getColumnCount()];
            int arrIndex = 0;

            for(int i = table.getLeftPosition(); i <= table.getRightPosition(); i++){
                int columnWidth = table.getColumnWidth(i);
                if(columnWidth == -1) columnWidth = 3000;
                float width = (float)columnWidth / 50;
                relativeColumns[arrIndex] = width;
                arrIndex++;
            }
            PdfPTable pdfTable = new PdfPTable(relativeColumns);
            float totalWidth = (document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin())
                    * pdfTable.getWidthPercentage() / 100;
            pdfTable.setTotalWidth(totalWidth);

            this.createTableHeaders(table, pdfTable);
            this.createTableBody(table, pdfTable);


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
        final List<HeaderColumn> headers = new ArrayList<>(table.getHeaders());
        headers.sort(Comparator.comparing(HeaderColumn::getStartPosition));

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
            this.applyCellSize(cell, -1, table.getSizeHeaderRow());
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

    private <T extends TableRowPresentation> void createTableBody(Table<T> table, PdfPTable pdfTable) {

        List<TableRow> rows = table.getRows();

        for (TableRow tr : rows) {
            while (tr.hasNext()) {
                TableRow.Cell tableCell = tr.next();
                Object data = tableCell.getData();
                Style st = table.getCellStyle(tableCell);
                PdfPCell cell = new PdfPCell();
                this.applyStyleToCell(cell, st);

                if(data instanceof PictureElement) System.out.println("picture");
                else if(data != null) {
                    PdfStyle pdfStyle = this.styles.get(st);
                    Font font = pdfStyle != null ? pdfStyle.getFont() : PdfStyle.convertFont(st.getFont());
                    cell.setPhrase(new Phrase(data.toString(), font));
                }
                pdfTable.addCell(cell);
            }
        }
    }

    private void applyCellSize(PdfPCell cell, int width, int height){
        if(width != -1){
            PdfPTable table = cell.getTable();
            table.setTotalWidth((float)width/1000);
        }
//        if(height != -1){
//            cell.setFixedHeight((float)height/50);
//        }

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
