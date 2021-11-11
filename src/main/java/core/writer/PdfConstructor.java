package core.writer;

//import com.itextpdf.kernel.pdf.PdfDocument;
//import com.itextpdf.layout.Document;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import core.element.picture.MultiBarLine;
import core.element.picture.PictureElement;
import core.element.table.HeaderColumn;
import core.element.table.Table;
import core.element.table.TableRow;
import core.element.text.TextBlock;
import core.presentation.TableRowPresentation;
import core.presentation.style.Style;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

import static core.writer.PrintSetting.LANDSCAPE;

/**
 * @author Усольцев Иван
 */
public class PdfConstructor implements DocumentConstructor {

    private final Document document;
    private PdfWriter writer;
    private String pathname;
    private Map<Style, PdfStyle> styles;
    private boolean appliedSetting = false;

//    private BaseFont baseFont;

    /**
     * CONSTRUCTORS
     */
    PdfConstructor() {
        document = new Document();
        styles = new HashMap<>();

//        this.pathname = fileName + "_" + new Date().getTime() + ".pdf";
//        File file = new File("../" + pathname);
//        try {
//            writer = PdfWriter.getInstance(document, new FileOutputStream(file));
//        } catch (DocumentException | FileNotFoundException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * METHODS
     */

    @Override
    public int createNewPageAndSetCurrent(String pageName, PrintSetting setting) {
        if (writer == null) {
            this.pathname = pageName + "_" + new Date().getTime() + ".pdf";
            File file = new File("../" + pathname);
            try {
                writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            } catch (DocumentException | FileNotFoundException e) {
                e.printStackTrace();
            }
            this.setPrintSetting(setting);
            document.open();
        }
        document.newPage();
        return this.getCurrentPageIndex();
    }

    @Override
    public int getCurrentPageIndex() {
        return this.document.getPageNumber();
    }

    @Override
    public <T extends TableRowPresentation> void createTable(Table<T> table) {
        try {
            this.fillStyles(table.getAllStyles());
            float[] relativeColumns = new float[table.getColumnCount()];
            int arrIndex = 0;

            for (int i = table.getLeftPosition(); i <= table.getRightPosition(); i++) {
                int columnWidth = table.getColumnWidth(i);
                if (columnWidth == -1) columnWidth = 3000;
                float width = (float) columnWidth / 50;
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
        } catch (Exception e) {
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

        while (i < headers.size()) {
            HeaderColumn hc = headers.get(i);
            PdfPCell cell = new PdfPCell();
            this.applyStyleToCell(cell, headerStyle);
            int rowSpan;
            if (hc.hasChild()) {
                rowSpan = 1;
                List<HeaderColumn> children = new ArrayList<>(hc.getChildren());
                children.sort(Comparator.comparing(HeaderColumn::getStartPosition));
                headers.addAll(children);
            } else {
                bufferLength--;
                rowSpan = maxLevel - currentRow + 1;
            }
            cell.setRowspan(rowSpan);
            cell.setColspan(hc.getLength());
            PdfStyle pdfStyle = this.styles.get(headerStyle);
            Font font = pdfStyle != null ? pdfStyle.getFont() : PdfStyle.convertFont(headerStyle.getFont());
            cell.setPhrase(new Phrase(hc.getName(), font));
            pdfTable.addCell(cell);

            if (hc.getLastPosition() == table.getRightPosition() || (hc.getLength() == bufferLength && hc.getLevel() > 1)) {
                currentRow++;
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

                if (data instanceof PictureElement) {
                    try {
                        PictureElement pe = (PictureElement) data;
                        if (pe instanceof MultiBarLine) {
                            short rowHeight = tr.getHeight();
                            int columnIndex = table.getLeftPosition() + tr.getReadIndex();
                            int columnWidth = table.getColumnWidth(columnIndex);
                            if (columnWidth != -1) {
                                float factor = (float) columnWidth / rowHeight;
                                int a = Math.round(pe.getWidth() / factor);
                                pe.resize(pe.getWidth(), a);
                            }
                        }
                        Image img = Image.getInstance(pe.toByteArray());
                        img.scaleAbsoluteHeight(cell.getHeight());
                        cell.setImage(img);
                    } catch (IOException | BadElementException e) {
                        e.printStackTrace();
                    }
                } else if (data != null) {
                    PdfStyle pdfStyle = this.styles.get(st);
                    Font font = pdfStyle != null ? pdfStyle.getFont() : PdfStyle.convertFont(st.getFont());
                    cell.setPhrase(new Phrase(data.toString(), font));
                }
                pdfTable.addCell(cell);
            }
        }
    }

    private void applyStyleToCell(PdfPCell cell, Style st) {
        PdfStyle pst = this.styles.get(st);
        if (pst == null) return;
        cell.setBorder(pst.getBorder());
        cell.setHorizontalAlignment(pst.getH_align());
        cell.setVerticalAlignment(pst.getV_align());
        cell.setBackgroundColor(pst.getBackgroundColor());
        cell.setNoWrap(pst.getNoWrap());
    }

    private void fillStyles(Collection<Style> styles) {
        for (Style s : styles) {
            if (this.styles.containsKey(s)) continue;
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

    protected void setPrintSetting(PrintSetting ps) {

        if (ps.hasSetting(PrintSetting.ALL_MARGIN)) {
            float v = ((Number) ps.get(PrintSetting.ALL_MARGIN)).floatValue() * 10;
            document.setMargins(v, v, v, v);
        } else {
            float lm = ps.hasSetting(PrintSetting.LEFT_MARGIN) ? ((Number) ps.get(PrintSetting.LEFT_MARGIN)).floatValue() * 10 : 0;
            float rm = ps.hasSetting(PrintSetting.RIGHT_MARGIN) ? ((Number) ps.get(PrintSetting.RIGHT_MARGIN)).floatValue() * 10 : 0;
            float tm = ps.hasSetting(PrintSetting.TOP_MARGIN) ? ((Number) ps.get(PrintSetting.TOP_MARGIN)).floatValue() * 10 : 30;
            float bm = ps.hasSetting(PrintSetting.BOTTOM_MARGIN) ? ((Number) ps.get(PrintSetting.BOTTOM_MARGIN)).floatValue() * 10 : 30;
            document.setMargins(lm, rm, tm, bm);
        }
        Rectangle paperSize;
        if (ps.hasSetting(PrintSetting.PAPER_SIZE)) {
            Number num = (Number) ps.get(PrintSetting.PAPER_SIZE);
            switch (num.shortValue()) {
                case 3:
                    paperSize = PageSize.A3;
                    break;
                case 4:
                    paperSize = PageSize.A4;
                    break;
                case 5:
                    paperSize = PageSize.A5;
                    break;
                default:
                    throw new IllegalArgumentException("Use allowed short values from " + PrintSetting.PaperSize.class.getName());
            }
        } else paperSize = PageSize.A4;
        if (ps.hasSetting(PrintSetting.LANDSCAPE))  paperSize = paperSize.rotate();
        document.setPageSize(paperSize);
    }

    @Override
    public Path writeToFile() throws IOException {

        System.out.println(document.isOpen());
        document.close();
        writer.close();
        Path path = Paths.get(pathname);
        return path;
    }
}
