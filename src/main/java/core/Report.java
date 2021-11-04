package core;

import core.element.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Наследники - представляют собой объекты виды отчетов.
 * @author Усольцев Иван
 */
public final class Report {

    private final List<Element> headers = new ArrayList<>();
    private final List<Element> body = new ArrayList<>();
    private final String reportName;

    /**
     * CONSTRUCTORS
     */
    public Report(String reportName) {
        this.reportName = reportName;
    }

    /**
     * METHODS
     */
    public List<Element> getHeaders() {
        return Collections.unmodifiableList(headers);
    }

    public List<Element> getBody() {
        return Collections.unmodifiableList(body);
    }

    public void addHeader(Element... elements){
        Collections.addAll(this.headers, elements);
    }

    public void addBody(Element... elements){
        Collections.addAll(this.body, elements);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Report)) return false;
        Report report = (Report) o;
        return Objects.equals(getHeaders(), report.getHeaders()) &&
                Objects.equals(getBody(), report.getBody()) &&
                Objects.equals(getReportName(), report.getReportName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHeaders(), getBody(), getReportName());
    }

    public String getReportName() {
        return reportName;
    }
}
