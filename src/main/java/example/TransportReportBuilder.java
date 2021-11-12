package example;

import core.Report;
import core.ReportBuilder;
import core.ReportParameter;
import core.element.table.Table;
import core.element.text.TextBlock;
import core.presentation.style.Font;
import core.presentation.style.Style;
import core.DateReportParameter;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

public class TransportReportBuilder implements ReportBuilder {
    @Override
    public Report[] build(ReportParameter... parameter) {
        String reportNameTitle = "Отчет о работе транспорта";
        Report report = new Report(reportNameTitle);
        Table<TransportDiagramTableRow> table = new Table<>(TransportDiagramTableRow.class);
        this.fillTableExampleData(table);
        report.addBody(table);
        int lp = table.getLeftPosition();
        int rp = table.getRightPosition();
        DateReportParameter interval = (DateReportParameter) parameter[0];
        this.constructHeaderAndAddToReport(report, lp, rp, interval);
        return new Report[]{report};
    }

    private void constructHeaderAndAddToReport(Report report, int startPos, int lastPos, DateReportParameter interval) {
        String orgNameTitle = "ООО \"Добровоз\"";
        String intervalTitle = "Сформировано за период с " + interval.asText();
        Font headerFont = new Font(Font.TIMES_NEW_ROMAN, 12);
        headerFont.setBold(true);
        Style headerStyle = new Style(headerFont, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, BorderStyle.NONE);
        TextBlock header1 = new TextBlock(startPos, lastPos, orgNameTitle);
        TextBlock header2 = new TextBlock(startPos, lastPos, report.getReportName());
        TextBlock header3 = new TextBlock(startPos, lastPos, intervalTitle);
        header1.setStyle(headerStyle);
        header2.setStyle(headerStyle);
        header3.setStyle(headerStyle);
        report.addHeader(header1, header2, header3);
    }

    private void fillTableExampleData(Table<TransportDiagramTableRow> table) {
        Transport t1 = new Transport("Парк 1", "п501ан", "Газель Соболь");
        Transport t2 = new Transport("Парк 1", "п502ан", "Газель NEXT");
        Transport t3 = new Transport("Парк 1", "п503ан", "Mercedes Sprinter");
        Transport t4 = new Transport("Парк 1", "п504ан", "Mercedes Sprinter");
        TransportTime tt1 = new TransportTime(80, 20, 0);
        TransportTime tt2 = new TransportTime(20, 0, 80);
        TransportTime tt3 = new TransportTime(70, 20, 10);
        TransportTime tt4 = new TransportTime(90, 10, 0);

        Transport t5 = new Transport("Парк 2", "в531ан", "Mercedes Sprinter");
        Transport t6 = new Transport("Парк 2", "в302мн", "Газель NEXT");
        Transport t7 = new Transport("Парк 2", "п565пн", "Mercedes Sprinter");
        TransportTime tt5 = new TransportTime(20, 10, 70);
        TransportTime tt6 = new TransportTime(90, 0, 10);
        TransportTime tt7 = new TransportTime(70, 20, 10);

        Transport t8 = new Transport("Парк 3", "н665пн", "Mercedes Sprinter");
        TransportTime tt8 = new TransportTime(90, 10, 0);

        TransportDiagramTableRow row1 = TransportDiagramTableRow.create(t1, tt1);
        TransportDiagramTableRow row2 = TransportDiagramTableRow.create(t2, tt2);
        TransportDiagramTableRow row3 = TransportDiagramTableRow.create(t3, tt3);
        TransportDiagramTableRow row4 = TransportDiagramTableRow.create(t4, tt4);
        TransportDiagramTableRow row5 = TransportDiagramTableRow.create(t5, tt5);
        TransportDiagramTableRow row6 = TransportDiagramTableRow.create(t6, tt6);
        TransportDiagramTableRow row7 = TransportDiagramTableRow.create(t7, tt7);
        TransportDiagramTableRow row8 = TransportDiagramTableRow.create(t8, tt8);

        table.add(row1);
        table.add(row2);
        table.add(row3);
        table.add(row4);
        table.add(row5);
        table.add(row6);
        table.add(row7);
        table.add(row8);

        table.sort(TransportDiagramTableRow.getComparator());

        Integer index = 1;
        for (TransportDiagramTableRow row : table.getData()) {
            row.setIndex(index);
            index++;
        }
    }
}
