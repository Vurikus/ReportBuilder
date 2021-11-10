package core.writer;

import core.element.picture.PictureElement;
import core.element.table.HeaderColumn;
import core.element.table.Table;
import core.element.table.TableRow;
import core.element.text.TextBlock;
import core.presentation.TableRowPresentation;
import core.presentation.style.Style;
import core.util.ColorConstants;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.ss.util.PropertyTemplate;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author Усольцев Иван
 */
public class ExcelConstructor implements DocumentConstructor {

    private final XSSFWorkbook book;
    private final List<Sheet> sheets;
    private final Map<Style, CellStyle> styles;
    private Sheet currentSheet;
    private String pathname;

    /**
     * CONSTRUCTORS
     */
    ExcelConstructor() {
        book = new XSSFWorkbook();
        sheets = new ArrayList<>();
        styles = new HashMap<>();

    }

    /**
     * PUBLIC METHODS
     */
    public Sheet getSheetByIndex(int index) {
        return this.sheets.get(index);
    }

    public int getCurrentPageIndex() {
        return book.getSheetIndex(currentSheet);
    }

    public int createNewPageAndSetCurrent(String sheetName) {
        if(currentSheet == null) this.pathname = sheetName + "_" + new Date().getTime() + ".xlsx";
        Sheet sh = book.createSheet(sheetName);
        sheets.add(sh);
        currentSheet = sh;
        return sheets.size() - 1;
    }

    @Override
    public Path writeToFile() throws IOException {
        long time = new Date().getTime();
        File file = new File("../" + pathname);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            book.write(fos);
        }
        Path path = Paths.get(pathname);
        return path;
    }

    @Override
    public <T extends TableRowPresentation> void createTable(Table<T> table) {
        if (currentSheet == null) throw new NullPointerException("Не создан лист для работы");
        for (Style s : table.getAllStyles()) {
            if (styles.containsKey(s)) continue;
            CellStyle cs = this.constructStyle(s);
            this.styles.put(s, cs);
        }
        for (int i = table.getLeftPosition(); i <= table.getRightPosition(); i++) {
            if(table.getAutoSizeColumn() && table.hasStandardWidth(i)) currentSheet.autoSizeColumn(i, table.getAutoSizeColumn());
            else if(!table.hasStandardWidth(i)) {
                int columnWidth = table.getColumnWidth(i);
                currentSheet.setColumnWidth(i, columnWidth);
            }
        }
        this.createTableHeaders(table);
        this.createTableBody(table);
    }

    @Override
    public void createTextBlock(TextBlock text) {
        if (currentSheet == null) throw new NullPointerException("Не создан лист для работы");
        final int topRow = currentSheet.getLastRowNum() + 1;
        CellRangeAddress cell = new CellRangeAddress(topRow, topRow, text.getLeftPosition(), text.getRightPosition());
        if (cell.getNumberOfCells() > 1) currentSheet.addMergedRegion(cell);
        Row row = currentSheet.createRow(topRow);
        Style s = text.getStyle();
        CellStyle cs;
        if (styles.containsKey(s)) cs = styles.get(s);
        else {
            cs = this.constructStyle(s);
            this.styles.put(s, cs);
        }
        CellUtil.createCell(row, text.getLeftPosition(), text.getBody(), cs);
    }

    @Override
    public void setPrintSetting(PrintSetting ps) {
        for (int i = 0; i < sheets.size(); i++) {
            this.setPrintSetting(i, ps);
        }
    }

    @Override
    public void setPrintSetting(int pageIndex, PrintSetting ps) {
        Sheet sheet = sheets.get(pageIndex);

        int topRow = ps.hasSetting(PrintSetting.TOP_PRINT_AREA) ? (Integer) ps.get(PrintSetting.TOP_PRINT_AREA) : sheet.getTopRow();
        int bottomRow = ps.hasSetting(PrintSetting.BOTTOM_PRINT_AREA) ? (Integer) ps.get(PrintSetting.BOTTOM_PRINT_AREA) : sheet.getLastRowNum();
        int leftCol = ps.hasSetting(PrintSetting.LEFT_PRINT_AREA) ? (Integer) ps.get(PrintSetting.LEFT_PRINT_AREA) : sheet.getLeftCol();
        int rightCol = ps.hasSetting(PrintSetting.RIGHT_PRINT_AREA) ? (Integer) ps.get(PrintSetting.RIGHT_PRINT_AREA) : sheet.getRow(topRow).getLastCellNum();

        book.setPrintArea(pageIndex, leftCol, rightCol, topRow, bottomRow);
        boolean landscape = false;
        if (ps.hasSetting(PrintSetting.LANDSCAPE)) landscape = (Boolean) ps.get(PrintSetting.LANDSCAPE);
        else{
            int sheetWidth = 0;
            for(int curCol = leftCol; curCol <= rightCol; curCol++) sheetWidth += sheet.getColumnWidth(curCol);
            //210 - ширина листа А4, 256 - какая то внутренняя Экселевская переменная
            if(sheetWidth > 210*256) landscape = true;
        }
        sheet.getPrintSetup().setLandscape(landscape);

        if (ps.hasSetting(PrintSetting.ALL_MARGIN)) {
            double v = ((Number) ps.get(PrintSetting.ALL_MARGIN)).doubleValue();
            sheet.setMargin(Sheet.LeftMargin, v);
            sheet.setMargin(Sheet.RightMargin, v);
            sheet.setMargin(Sheet.TopMargin, v);
            sheet.setMargin(Sheet.BottomMargin, v);
        } else {
            double lm = ps.hasSetting(PrintSetting.LEFT_MARGIN) ? ((Number) ps.get(PrintSetting.LEFT_MARGIN)).doubleValue() : 0.5D;
            double rm = ps.hasSetting(PrintSetting.RIGHT_MARGIN) ? ((Number) ps.get(PrintSetting.RIGHT_MARGIN)).doubleValue() : 0.5D;
            double tm = ps.hasSetting(PrintSetting.TOP_MARGIN) ? ((Number) ps.get(PrintSetting.TOP_MARGIN)).doubleValue() : 0.5D;
            double bm = ps.hasSetting(PrintSetting.BOTTOM_MARGIN) ? ((Number) ps.get(PrintSetting.BOTTOM_MARGIN)).doubleValue() : 0.5D;
            sheet.setMargin(Sheet.LeftMargin, lm);
            sheet.setMargin(Sheet.RightMargin, rm);
            sheet.setMargin(Sheet.TopMargin, tm);
            sheet.setMargin(Sheet.BottomMargin, bm);
        }
        if (ps.hasSetting(PrintSetting.PAPER_SIZE)) {
            Number num = (Number) ps.get(PrintSetting.PAPER_SIZE);
            sheet.getPrintSetup().setPaperSize(num.shortValue());
        }else sheet.getPrintSetup().setPaperSize(PrintSetup.A4_PAPERSIZE);

        if (ps.hasSetting(PrintSetting.FIT_TO_PAGE)) {
            boolean b = (Boolean) ps.get(PrintSetting.FIT_TO_PAGE);
            sheet.setFitToPage(b);
        }
        if (ps.hasSetting(PrintSetting.FIT_WIDTH)) {
            Number num = (Number) ps.get(PrintSetting.FIT_WIDTH);
            sheet.getPrintSetup().setFitWidth(num.shortValue());
        }
        if (ps.hasSetting(PrintSetting.FIT_HEIGHT)) {
            Number num = (Number) ps.get(PrintSetting.FIT_HEIGHT);
            sheet.getPrintSetup().setFitHeight(num.shortValue());
        }
        if (ps.hasSetting(PrintSetting.AUTO_BREAKS)) {
            boolean b = (Boolean) ps.get(PrintSetting.AUTO_BREAKS);
            sheet.setAutobreaks(b);
        } else sheet.setAutobreaks(true);
        if (ps.hasSetting(PrintSetting.FOOTER_MARGIN)) {
            Number num = (Number) ps.get(PrintSetting.FOOTER_MARGIN);
            sheet.getPrintSetup().setFooterMargin(num.doubleValue());
        } else sheet.getPrintSetup().setFooterMargin(0.25);
    }

    @Override
    public void createImage(PictureElement pe) {
        try {
            byte[] bytes = pe.toByteArray();
            int pictureIdx = book.addPicture(bytes, XSSFWorkbook.PICTURE_TYPE_PNG);
            CreationHelper helper = book.getCreationHelper();
            Drawing drawing = currentSheet.createDrawingPatriarch();
            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setCol1(pe.getLeftPosition());
            anchor.setRow1(pe.getTopPosition());
            Picture pict = drawing.createPicture(anchor, pictureIdx);
            if (!pe.getAutoSize()) {
                anchor.setCol2(pe.getRightPosition());
                anchor.setRow2(pe.getBottomPosition());
            }
            pict.resize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * INNER METHODS
     */
    private <T extends TableRowPresentation> void createTableHeaders(Table<T> table) {
        final SortedSet<HeaderColumn> headers = table.getHeaders();
        HeaderColumn firstColumn = headers.first();
        final int topRow = currentSheet.getLastRowNum() + 1;
        final int bottomRow = topRow + firstColumn.getLevel() - 1;
        Style hs = table.getHeaderStyle();
        CellStyle cs = this.styles.get(hs);
        this.drawBorder(hs.getBorder(), table.getLeftPosition(), table.getRightPosition(), topRow, bottomRow);
        for (HeaderColumn hc : headers) {
            drawTableHeaders(topRow, bottomRow, hc, cs);
        }
        for(int i = topRow; i <= bottomRow; i++) {
            Row row = currentSheet.getRow(i);
            row.setHeight(table.getSizeHeaderRow());
        }
    }

    private void drawBorder(BorderStyle borderStyle, int leftBorder, int rightBorder, int topBorder, int bottomBorder) {
        PropertyTemplate propertyTemplate = new PropertyTemplate();
        CellRangeAddress cra = new CellRangeAddress(topBorder, bottomBorder, leftBorder, rightBorder);
        propertyTemplate.drawBorders(cra, borderStyle, BorderExtent.ALL);
        propertyTemplate.applyBorders(currentSheet);
    }

    private <T extends TableRowPresentation> void createTableBody(Table<T> table) {
        final int topRow = currentSheet.getLastRowNum() + 1;
        int bufferRow = topRow;
        List<TableRow> dataRows = table.getRows();

        Style defaultStyle = table.getDefaultBodyStyle();
        final int bottomBorder = topRow + dataRows.size() - 1;
        this.drawBorder(defaultStyle.getBorder(), table.getLeftPosition(), table.getRightPosition(), topRow, bottomBorder);

        for (TableRow tr : dataRows) {
            Row row = currentSheet.createRow(bufferRow);
            int position = table.getLeftPosition();
            row.setHeight(tr.getHeight());
            while (tr.hasNext()) {
                TableRow.Cell tableCell = tr.next();
                Object data = tableCell.getData();
                Style st = table.getCellStyle(tableCell);
                CellStyle cs = this.styles.get(st);
                Cell excelCell = row.createCell(position);
                excelCell.setCellStyle(cs);
                if(data == null) {
                    excelCell.setCellValue("");
                } else if (data instanceof Number) {
                    Number num = (Number) data;
                    excelCell.setCellValue(num.doubleValue());
                } else if (data instanceof String) {
                    String v = (String) data;
                    excelCell.setCellValue(v);
                } else if (data instanceof Date) {
                    Date v = (Date) data;
                    excelCell.setCellValue(v);
                } else if (data instanceof Calendar) {
                    Calendar v = (Calendar) data;
                    excelCell.setCellValue(v);
                } else if (data instanceof PictureElement) {
                    PictureElement pe = (PictureElement) data;
                    pe.setTopPosition(row.getRowNum());
                    if (pe.getAutoSize()) {
                        //1.25 и 1.008 поправочные коэффициенты высчитанные эмпирически. Нужны, чтобы рисунок не выходил за рамки ячейки
                        int height = Math.round(row.getHeightInPoints() * 1.25F);
                        int column = excelCell.getAddress().getColumn();
                        int width = Math.round(currentSheet.getColumnWidthInPixels(column) * 1.008F);
                        pe.resize(width, height);
                        pe.setLeftPosition(column);
                        pe.setRightPosition(column);
                        pe.setBottomPosition(row.getRowNum());
                    }
                    this.createImage(pe);
                }else {
                    String v = data.toString();
                    excelCell.setCellValue(v);
                }
                position++;
            }
            bufferRow++;
        }
    }

    private CellStyle constructStyle(Style s) {
        XSSFFont font = book.createFont();
        font.setBold(s.getFont().getBold());
        font.setItalic(s.getFont().getItalic());
        font.setFontName(s.getFont().getFontName());
        font.setFontHeightInPoints(s.getFont().getHeight());
        if (s.getFont().isChangedDefaultColor()) {
            byte[] colorRGB = ColorConstants.getColorRGB(s.getFont().getColor());
            XSSFColor fontColor = new XSSFColor(colorRGB, new DefaultIndexedColorMap());
            font.setColor(fontColor);
        }

        XSSFCellStyle cs = book.createCellStyle();
        cs.setFont(font);
        cs.setAlignment(s.getH_align());
        cs.setVerticalAlignment(s.getV_align());
        cs.setWrapText(s.getWrapText());
        if (s.isChangedDefaultColor()) {
            byte[] colorRGB = ColorConstants.getColorRGB(s.getBackgroundColor());
            XSSFColor foregroundColor = new XSSFColor(colorRGB, new DefaultIndexedColorMap());
            cs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cs.setFillForegroundColor(foregroundColor);
        }
        BorderStyle border = s.getBorder();
        cs.setBorderTop(border);
        cs.setBorderLeft(border);
        cs.setBorderRight(border);
        cs.setBorderBottom(border);
        return cs;
    }

    private void drawTableHeaders(final int currentTopRow, final int bottomRowFirstLevel, HeaderColumn hc, CellStyle headerStyle) {
        int currentBottomRow;
        if (hc.hasChild()) {
            currentBottomRow = currentTopRow;
            for (HeaderColumn childColumn : hc.getChildren())
                this.drawTableHeaders(currentBottomRow + 1, bottomRowFirstLevel, childColumn, headerStyle);
        } else {
            currentBottomRow = bottomRowFirstLevel;
        }
        if (currentTopRow != currentBottomRow || hc.getStartPosition() != hc.getLastPosition()) {
            CellRangeAddress cell = new CellRangeAddress(currentTopRow, currentBottomRow, hc.getStartPosition(), hc.getLastPosition());
            currentSheet.addMergedRegion(cell);
        }
        Row row = currentSheet.getRow(currentTopRow);
        if (row == null) row = currentSheet.createRow(currentTopRow);
        CellUtil.createCell(row, hc.getStartPosition(), hc.getName(), headerStyle);
    }
}
