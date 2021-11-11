package example;

import core.Format;
import core.Report;
import core.ReportManager;
import core.util.IntervalDate;
import core.writer.DocumentConstructor;
import core.writer.DocumentConstructorFactory;
import core.writer.PrintSetting;

import java.io.IOException;
import java.nio.file.Path;

public class ExampleReportManager extends ReportManager {

    public Path createTransportReport(Format format, IntervalDate interval) throws IOException {
        TransportReportBuilder reportBuilder = new TransportReportBuilder();
        Report[] reports = reportBuilder.build(interval);

        PrintSetting ps = this.getDefaultPrintSetting();
        DocumentConstructor constructor = DocumentConstructorFactory.getConstructor(format);
        for (Report report : reports) {
            constructor.createNewPageAndSetCurrent(report.getReportName(), ps);
            this.createElementsFromReport(constructor, report);
        }
        return constructor.writeToFile();
    }
}