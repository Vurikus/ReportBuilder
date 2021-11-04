package core.element.table;

/**
 * @author Усольцев Иван
 */

import java.util.Objects;

class Position{
    int startPosition;
    int row;

    Position(int startPosition, int row) {
        this.startPosition = startPosition;
        this.row = row;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position position = (Position) o;
        return startPosition == position.startPosition &&
                row == position.row;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPosition, row);
    }
}
