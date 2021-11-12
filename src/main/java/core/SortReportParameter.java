package core;

/**
 * @author Усольцев Иван
 */
public class SortReportParameter implements ReportParameter {

    private final int sortByIndexColumn;

    /**
     * CONSTRUCTORS
     */
    public SortReportParameter(int sortByIndexColumn) {
        this.sortByIndexColumn = sortByIndexColumn;
    }
    /**
     * METHODS
     */
    public int getSortByIndexColumn() {
        return sortByIndexColumn;
    }
}
