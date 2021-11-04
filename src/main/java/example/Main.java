package example;

import core.Format;
import core.Report;
import core.ReportManager;
import core.element.Element;
import core.element.table.Table;
import core.element.text.TextBlock;
import core.presentation.TableRowPresentation;
import core.util.IntervalDate;
import core.util.PeriodIntervalConstant;
import core.writer.DocumentConstructor;
import core.writer.DocumentConstructorFactory;
import core.writer.PrintSetting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        Date now = new Date();
        Date start = IntervalDate.roundDown(now, PeriodIntervalConstant.MONTH);
        Date end = IntervalDate.roundUp(now, PeriodIntervalConstant.MONTH);
        IntervalDate interval = new IntervalDate(start, end);

        ExampleReportManager manager = new ExampleReportManager();
        manager.createTransportReport(Format.EXCEL, interval);
    }
}
