package core;

import core.util.IntervalDate;

/**
 * Базовый интерфейс для создания любого отчета
 * Наследники должны реализовать метод, который строит отчет, сужая выборку передаваемым параметром
 * @author Усольцев Иван
 */
public interface ReportBuilder {

    Report[] build(IntervalDate interval, ReportParameter ... parameter);
}
