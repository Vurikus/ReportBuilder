package example;

import core.element.picture.MultiBarLine;
import core.meta.*;
import core.presentation.TableRowPresentation;
import core.util.ColorConstants;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.util.Comparator;

/**
 * @author Усольцев Иван
 */
@TotalLine(title = "Итого среднее: ", titlePosition = 1, lineStyle = @TextStyle(backgroundColor = ColorConstants.GRAY, h_align = HorizontalAlignment.CENTER))
@GroupTotalLine(title = "Среднее: ", lineStyle = @TextStyle(backgroundColor = ColorConstants.LIGHT_GRAY, h_align = HorizontalAlignment.CENTER), groupByColumn = 1)
@HeaderStyle(backgroundColor = ColorConstants.GRAY)
@TextStyle(h_align = HorizontalAlignment.CENTER, height = 400)
@MultiJoinTableColumn(values = {
        @JoinTableColumn(firstColumn = 0, lastColumn = 3, name = "SUPER PUPER TOP LEVEL", level = 5),
        @JoinTableColumn(firstColumn = 0, lastColumn = 3, name = "SUPER TEST LEVEL", level = 4),
        @JoinTableColumn(firstColumn = 1, lastColumn = 3, name = "SUB TEST LEVEL", level = 3),
        @JoinTableColumn(firstColumn = 2, lastColumn = 3, name = "Транспорт"),
        @JoinTableColumn(firstColumn = 4, lastColumn = 7, name = "Время работы транспорта", level = 3),
        @JoinTableColumn(firstColumn = 5, lastColumn = 7, name = "Время [мин]")
})
public class TransportDiagramTableRow extends TableRowPresentation {

    @TextStyle(h_align = HorizontalAlignment.CENTER,
            backgroundColor = ColorConstants.GRAY)
    @TableColumn(position = 0, name = "№", width = 1000)
    private Integer index = 0;

    @TableColumn(position = 1, name = "Парк", width = 5000)
    private String locationName = "";

    @TableColumn(position = 2, name = "Модель", width = 6000)
    private String model = "";

    @TableColumn(position = 3, name = "Гос.номер", width = 5000)
    private String number = "";

    @TableColumn(position = 4, name = "Диаграмма", width = 8000)
    private MultiBarLine bar;

    @CountTotalColumn(function = Function.MIDDLE)
    @TextStyle(backgroundColor = ColorConstants.GREEN, h_align = HorizontalAlignment.CENTER)
    @TableColumn(position = 5, name = "Работа", width = 3000)
    private Integer runTime = 0;

    @CountTotalColumn(function = Function.MIDDLE)
    @TextStyle(backgroundColor = ColorConstants.YELLOW, h_align = HorizontalAlignment.CENTER)
    @TableColumn(position = 6, name = "Простой", width = 3000)
    private Integer idleTime = 0;

    @CountTotalColumn(function = Function.MIDDLE)
    @TextStyle(backgroundColor = ColorConstants.RED, h_align = HorizontalAlignment.CENTER)
    @TableColumn(position = 7, name = "Ремонт", width = 3000)
    private Integer repairTime = 0;

    /**
     * CONSTRUCTORS
     */
    private TransportDiagramTableRow() {}

    /**
     * METHODS
     */
    public static TransportDiagramTableRow create(Transport transport, TransportTime time) {
        TransportDiagramTableRow row = new TransportDiagramTableRow();
        row.model = transport.getModel();
        row.number = transport.getLicenseNumber();
        row.locationName = transport.getLocationName();
        row.runTime = time.getRun();
        row.idleTime = time.getIdle();
        row.repairTime = time.getRepair();

        MultiBarLine bar = new MultiBarLine();
        row.bar = bar;
        bar.addBar(row.runTime, ColorConstants.GREEN);
        bar.addBar(row.idleTime, ColorConstants.YELLOW);
        bar.addBar(row.repairTime, ColorConstants.RED);
        return row;
    }

    public static Comparator<TransportDiagramTableRow> getComparator() {
        return Comparator.comparing(TransportDiagramTableRow::getLocationName)
                .thenComparing(TransportDiagramTableRow::getModel);
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getIndex() {
        return index;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getModel() {
        return model;
    }

    public String getNumber() {
        return number;
    }

    public MultiBarLine getBar() {
        return bar;
    }

    public Integer getRunTime() {
        return runTime;
    }

    public Integer getIdleTime() {
        return idleTime;
    }

    public Integer getRepairTime() {
        return repairTime;
    }
}
