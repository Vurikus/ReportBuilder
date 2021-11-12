package core;

/**
 * Базовый интерфейс для создания любого отчета
 * Наследники должны реализовать метод, который строит отчет, сужая выборку передаваемым параметром
 * @author Усольцев Иван
 */
public interface ReportBuilder {

    Report[] build(DateReportParameter interval, ReportParameter ... parameter);
}
