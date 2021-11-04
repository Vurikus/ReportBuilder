package core.element.table;

import core.meta.Function;
import core.util.Formatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Усольцев Иван
 */
public class TotalRow {

    private final Map<Integer, Object> cellsData;
    private int styleIndex;
    private final int rowLength;
    private final int leftPosition;

    /**
     * CONSTRUCTORS
     */
    TotalRow(int rowLength, int styleIndex, int leftPosition) {
        this.cellsData = new HashMap<>();
        this.rowLength = rowLength;
        this.styleIndex = styleIndex;
        this.leftPosition = leftPosition;
    }

    /**
     * METHODS
     */
    TableRow summarize(List<Table.MetaDataColumn> metaDataColumns) {
        TableRow row = new TableRow(rowLength);
        assert metaDataColumns.size() == rowLength;
        for (int i = 0; i < rowLength; i++) {
            Integer columnIndex = leftPosition + i;
            Object cellData = cellsData.getOrDefault(columnIndex, "");
            Table.MetaDataColumn mdc = metaDataColumns.get(i);
            if (mdc.totalFunction == Function.MIDDLE && cellData instanceof List) {
                List<Double> values = (List<Double>) cellData;
                if(!values.isEmpty()) {
                    Double[] doubles = values.toArray(new Double[values.size()]);
                    cellData = this.calculateFunction(mdc.totalFunction, doubles);
                }
            }
            if(mdc.hasScale() && cellData instanceof Number)
                cellData = Formatter.applyNumberPattern((Number) cellData, mdc.decimalFormat);
            row.push(cellData, (short)styleIndex);
        }
        return row;
    }

    void push(Integer columnIndex, Object value) {
        this.push(columnIndex, value, null);
    }

    void push(Integer columnIndex, Object value, Function function) {
        if (value instanceof Number && function != null) {
            Number num = (Number) value;
            double currentValue = num.doubleValue();
            if (function == Function.MIDDLE) {
                List values = (List) this.cellsData.getOrDefault(columnIndex, new ArrayList());
                values.add(currentValue);
                this.cellsData.put(columnIndex, values);
            } else {
                Double storeValue = (Double) this.cellsData.get(columnIndex);
                if(storeValue != null) currentValue = this.calculateFunction(function, storeValue, currentValue);
                this.cellsData.put(columnIndex, currentValue);
            }
        } else this.cellsData.put(columnIndex, value.toString());
    }

    void clearCellData() {
        this.cellsData.clear();
    }

    private Double calculateFunction(Function function, Double... doubles) {
        Double v = doubles[0];
        if (doubles.length == 1) return v;

        if (function == Function.MIDDLE) {
            Double sum = this.calculateFunction(Function.SUM, doubles);
            v = sum / doubles.length;
        } else {
            for (int i = 1; i < doubles.length; i++) {
                if (function == Function.SUM) v += doubles[i];
                else if (function == Function.MIN && doubles[i] < v) v = doubles[i];
                else if (function == Function.MAX && doubles[i] > v) v = doubles[i];
            }
        }
        return v;
    }

    public void setStyleIndex(int styleIndex) {
        this.styleIndex = styleIndex;
    }
}