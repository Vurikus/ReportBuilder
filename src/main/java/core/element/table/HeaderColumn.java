package core.element.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Усольцев Иван
 */
public class HeaderColumn implements Comparable<HeaderColumn>{
    private final int lastPosition;
    private final String name;
    private List<HeaderColumn> children;
    private Position position;

    /**
     * CONSTRUCTORS
     */
    HeaderColumn(int startPosition, String name) {
        this.position = new Position(startPosition, 1);
        this.lastPosition = startPosition;
        this.name = name;
    }

    HeaderColumn(int startPosition, int lastPosition, String name, short level) {
        this.lastPosition = lastPosition;
        this.position = new Position(startPosition, level);
        this.name = name;
        this.children = new ArrayList<>(4);
    }

    /**
     * METHODS
     */
    void addChild(HeaderColumn hc){
        if(position.row == 1) throw new RuntimeException("Current column is first level. It doesn't have a child");
        if(hc.position.row >= position.row)
            throw new RuntimeException("Adding column has higher level. It can not be child for current column");
        this.children.add(hc);
    }

    public int getLevel(){
        return this.position.row;
    }

    public int getLength(){
        return lastPosition - this.position.startPosition + 1;
    }

    public int getStartPosition(){
        return this.position.startPosition;
    }

    @Override
    public int compareTo(HeaderColumn column) {
        int result = column.position.row - this.position.row;
        if(result == 0){
            result = this.position.startPosition - column.position.startPosition;
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HeaderColumn)) return false;
        HeaderColumn that = (HeaderColumn) o;
        return Objects.equals(getName(), that.getName()) &&
                Objects.equals(getPosition(), that.getPosition());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getPosition());
    }

    public boolean hasChild() {
        return children != null;
    }

    @Override
    public String toString() {
        return "HeaderColumn{" +
                "name='" + name + '\'' +
                ", position=" + position.startPosition + " : " + lastPosition + " level - " + position.row +
                '}';
    }

    public int getLastPosition() {
        return lastPosition;
    }

    public String getName() {
        return name;
    }

    public List<HeaderColumn> getChildren() {
        return children;
    }

    public Position getPosition() {
        return position;
    }
}



