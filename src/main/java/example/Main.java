package example;

import core.Format;
import core.DateReportParameter;
import core.util.PeriodIntervalConstant;

import java.io.IOException;
import java.util.Date;

public class Main {

    public static void main(String[] args) throws IOException {
        Date now = new Date();
        Date start = DateReportParameter.roundDown(now, PeriodIntervalConstant.WEEK);
        Date end = DateReportParameter.roundUp(now, PeriodIntervalConstant.WEEK);
        DateReportParameter interval = new DateReportParameter(start, end);

        ExampleReportManager manager = new ExampleReportManager();
        manager.createTransportReport(Format.PDF, interval);
    }
}
