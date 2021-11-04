package core;

import core.element.Element;
import core.element.table.Table;
import core.element.text.TextBlock;
import core.presentation.TableRowPresentation;
import core.writer.DocumentConstructor;
import core.writer.PrintSetting;

import java.util.ArrayList;
import java.util.List;

public abstract class ReportManager {

    protected void createElementsFromReport(DocumentConstructor constructor, Report report){
        List<Element> elements = new ArrayList<>();
        elements.addAll(report.getHeaders());
        elements.addAll(report.getBody());
        for (Element e : elements) {
            if (e instanceof TextBlock) constructor.createTextBlock((TextBlock) e);
            else if (e instanceof Table) constructor.createTable((Table<? extends TableRowPresentation>) e);
        }
    }

    protected PrintSetting getDefaultPrintSetting(){
        PrintSetting ps = new PrintSetting();
        ps.put(PrintSetting.LANDSCAPE, Boolean.TRUE);
        ps.put(PrintSetting.FIT_TO_PAGE, Boolean.TRUE);
        ps.put(PrintSetting.FIT_HEIGHT, 0);
        ps.put(PrintSetting.FIT_WIDTH, 1);
        return ps;
    }
}
