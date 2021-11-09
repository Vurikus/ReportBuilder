package core.writer;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import core.element.picture.PictureElement;
import core.element.table.HeaderColumn;
import core.element.table.Table;
import core.element.text.TextBlock;
import core.presentation.TableRowPresentation;
import core.presentation.style.Style;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;

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
    private <T extends TableRowPresentation> void createTableHeaders(Table<T> table, PdfPTable pdfTable) throws IOException, DocumentException {

        BaseFont bf = BaseFont.createFont("font.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font = new Font(bf, 6);
        final List<HeaderColumn> headers = new ArrayList<>(table.getHeaders());
        headers.sort(Comparator.comparing(HeaderColumn::getStartPosition));
        Style headerStyle = table.getHeaderStyle();
        int argb = Integer.decode(headerStyle.getBackgroundColor());
        int r = argb >> 16 & 255;
        int g = argb >> 8 & 255;
        int b = argb & 255;
        BaseColor backColor = new BaseColor(r, g, b);

        final int topRow = 1;
        final int maxLevel = table.getHeaders().first().getLevel();
        final int bottomRow = topRow + maxLevel - 1;
        int currentRow = topRow;
        System.out.println("bottomRow = " + bottomRow);
        System.out.println("topRow = " + topRow);

//        Iterator<HeaderColumn> iterator = headers.iterator();
        int i = 0;
        int beginChildLevelIndex = -1;
        int countColumnIndex = 0;
        int bufferLength = table.getColumnCount();

        while(i < headers.size()){
            HeaderColumn hc = headers.get(i);
            System.out.println(hc.getName());
//            HeaderColumn hc = iterator.next();
            PdfPCell cell = new PdfPCell();
            cell.setBackgroundColor(backColor);
            int rowSpan;
            if(hc.hasChild()) {
                rowSpan = 1;
                List<HeaderColumn> children = new ArrayList<>(hc.getChildren());
                children.sort(Comparator.comparing(HeaderColumn::getStartPosition));
                headers.addAll(children);
            }
            else {
                bufferLength --;
                rowSpan = maxLevel - currentRow + 1;
            }
            cell.setRowspan(rowSpan);
            int length = hc.getLength();
            cell.setColspan(length);
            cell.setPhrase(new Phrase(hc.getName() + " - S = " + rowSpan + ":" + length + " Lvl: " + hc.getLevel(), font));
            pdfTable.addCell(cell);

            if(hc.getLastPosition() == table.getRightPosition() || (hc.getLength() == bufferLength && hc.getLevel() > 1)) {
                currentRow ++;
            }
            i++;
        }
    }

//    private void drawTableHeaders(final int currentTopRow, final int bottomRowFirstLevel, HeaderColumn hc) {
//        int currentBottomRow;
//        if (hc.hasChild()) {
//            currentBottomRow = currentTopRow;
//            for (HeaderColumn childColumn : hc.getChildren())
//                this.drawTableHeaders(currentBottomRow + 1, bottomRowFirstLevel, childColumn);
//        } else {
//            currentBottomRow = bottomRowFirstLevel;
//        }
//        if (currentTopRow != currentBottomRow || hc.getStartPosition() != hc.getLastPosition()) {
//            CellRangeAddress cell = new CellRangeAddress(currentTopRow, currentBottomRow, hc.getStartPosition(), hc.getLastPosition());
//            currentSheet.addMergedRegion(cell);
//        }
//        Row row = currentSheet.getRow(currentTopRow);
//        if (row == null) row = currentSheet.createRow(currentTopRow);
//        CellUtil.createCell(row, hc.getStartPosition(), hc.getName(), headerStyle);
//    }

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
