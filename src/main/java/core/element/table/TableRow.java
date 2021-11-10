package core.element.table;

import java.util.Objects;

/**
 * @author Усольцев Иван
 */
public class TableRow {

    private final Cell[] row;
    private short height = -1;
    private short readIndex;
    private short writeIndex;

    /**
     * CONSTRUCTORS
     */
    TableRow(int rowLength) {
        row = new Cell[rowLength];
        readIndex = 0;
        writeIndex = 0;
    }

    /**
     * METHODS
     */
    void push(Object data, short styleIndex){
        row[writeIndex] = new Cell(data, styleIndex);
        writeIndex++;
    }

    public Cell next(){
        Cell result = row[readIndex];
        readIndex++;
        return result;
    }

    public boolean hasNext(){
        return readIndex < row.length;
    }

    public boolean isAutoSizeRow(){
        return this.height == -1;
    }

    public short getHeight() {
        return height;
    }

    public void setHeight(short height) {
        this.height = height;
    }

    public short getReadIndex() {
        return readIndex;
    }

    /**
     * INNER CLASS
     */
    public static class Cell {
        private Object data;
        private short cellStyleIndex = 0;

        Cell(Object data) {
            this.data = data;
        }

        Cell(Object data, short cellStyleIndex) {
            this.data = data;
            this.cellStyleIndex = cellStyleIndex;
        }

        public Object getData() {
            return data;
        }

        public short getCellStyleIndex() {
            return cellStyleIndex;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Cell)) return false;
            Cell cell = (Cell) o;
            return getCellStyleIndex() == cell.getCellStyleIndex() &&
                    Objects.equals(getData(), cell.getData());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getData(), getCellStyleIndex());
        }
    }

}














