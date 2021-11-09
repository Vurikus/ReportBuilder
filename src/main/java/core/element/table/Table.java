package core.element.table;

import core.element.Element;
import core.meta.*;
import core.presentation.TableRowPresentation;
import core.presentation.style.Style;
import core.util.Formatter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Усольцев Иван
 */
public class Table<T extends TableRowPresentation> implements Element {

    private final List<T> data = new ArrayList<>();
    private final Class<T> dataClass;
    private SortedSet<HeaderColumn> headers;
    private Style headerStyle;
    private short sizeHeaderRow = -1;
    private short sizeBodyRow = -1;
    private final List<Style> styles;
    private final Map<Integer, MetaDataColumn> metaColumns;
    private int leftPosition = -1;
    private int rightPosition = -1;
    private final int columnCount;
    private boolean autoSizeColumn = true;
    private boolean hasTotalLine = false;

    private MetaDataTotal blockMetaDataTotal;
    private MetaDataTotal tableMetaDataTotal;
    private Set<Integer> countingTotalColumn;

    /**
     * CONSTRUCTORS
     */
    public Table(Class<T> clazz) {
        this.dataClass = clazz;
        this.styles = new ArrayList<>();
        this.metaColumns = new HashMap<>();
        this.setDefaultHeaderAndBodyStyle();
        this.checkTotalLineAndSetMetaDataIfTrue();
        this.buildHeaders();
        this.columnCount = rightPosition - leftPosition + 1;
    }

    /**
     * METHODS
     */
    public void add(T obj) {
        this.data.add(obj);
    }

    public void sort(Comparator<T> comparator) {
        this.data.sort(comparator);
    }

    public List<TableRow> getRows() {
        List<TableRow> result = new ArrayList<>(data.size());
        if (hasTotalLine) result = this.getRowIncludeTotalRows();
        else {
            List<Integer> keys = new ArrayList<>(metaColumns.keySet());
            Collections.sort(keys);
            for (T t : this.data) {
                TableRow row = new TableRow(metaColumns.size());
                row.setHeight(this.sizeBodyRow);
                for (Integer key : keys) {
                    MetaDataColumn mdc = metaColumns.get(key);
                    Object fieldValue = this.getColumnValue(t, mdc);
                    row.push(fieldValue, mdc.styleIndex);
                }
                result.add(row);
            }
        }
        return result;
    }

    public Collection<Style> getAllStyles() {
        Collection<Style> result = new ArrayList<>(styles);
        result.add(headerStyle);
        return result;
    }

    public Style getCellStyle(TableRow.Cell cell) {
        return this.styles.get(cell.getCellStyleIndex());
    }

    public Style getDefaultBodyStyle() {
        return this.styles.get(0);
    }

    @Override
    public int getHeight() {
        HeaderColumn hc = this.headers.first();
        return hc.getLevel() + this.data.size();
    }

    public boolean hasStandardWidth(Integer columnIndex) {
        MetaDataColumn mdc = this.metaColumns.get(columnIndex);
        if (mdc == null) throw new RuntimeException("Нет колонки под индексом - " + columnIndex + " в классе " + this.dataClass.getName());
        return mdc.hasStandardWidth();
    }

    public boolean getAutoSizeHeaderRow() {
        return this.sizeHeaderRow == -1;
    }

    public int getColumnWidth(Integer columnIndex) {
        MetaDataColumn mdc = this.metaColumns.get(columnIndex);
        if (mdc == null) throw new RuntimeException("Нет колонки под индексом - " + columnIndex + " в классе " + this.dataClass.getName());
        return mdc.width;
    }

    public List<T> getData() {
        return data;
    }

    public SortedSet<HeaderColumn> getHeaders() {
        return Collections.unmodifiableSortedSet(headers);
    }

    public Style getHeaderStyle() {
        return headerStyle;
    }

    public short getSizeHeaderRow() {
        return sizeHeaderRow;
    }

    public short getSizeBodyRow() {
        return sizeBodyRow;
    }

    @Override
    public int getLeftPosition() {
        return leftPosition;
    }

    @Override
    public int getRightPosition() {
        return rightPosition;
    }

    public boolean getAutoSizeColumn() {
        return autoSizeColumn;
    }

    public int getColumnCount() {
        return columnCount;
    }

    /**
     * PRIVATE METHODS
     */
    private void setDefaultHeaderAndBodyStyle() {
        TextStyle ts = dataClass.getAnnotation(TextStyle.class);
        HeaderStyle hs = dataClass.getAnnotation(HeaderStyle.class);

        Style defaultColumnStyle;
        if (ts != null) {
            defaultColumnStyle = Style.createFromAnnotation(ts);
            this.sizeBodyRow = ts.height();
        } else defaultColumnStyle = Style.getDefaultStyle();
        this.styles.add(defaultColumnStyle);

        if (hs != null) {
            this.headerStyle = Style.createFromAnnotation(hs);
            this.sizeHeaderRow = hs.height();
        } else this.headerStyle = Style.getDefaultStyle();
    }

    private void checkTotalLineAndSetMetaDataIfTrue() {
        GroupTotalLine gtl = dataClass.getAnnotation(GroupTotalLine.class);
        TotalLine tl = dataClass.getAnnotation(TotalLine.class);
        TextStyle ts = null;
        int styleIndex = 0;
        if (gtl != null) {
            ts = gtl.lineStyle();
            Style st = Style.createFromAnnotation(ts);
            styleIndex = this.registerNewStyle(st);
            this.blockMetaDataTotal = new MetaDataTotal(styleIndex);
            this.hasTotalLine = true;
        }
        if (tl != null) {
            if(ts == null || !ts.equals(tl.lineStyle())){
                Style st = Style.createFromAnnotation(tl.lineStyle());
                styleIndex = this.registerNewStyle(st);
            }
            this.tableMetaDataTotal = new MetaDataTotal(styleIndex);
            this.hasTotalLine = true;
        }
        if(this.hasTotalLine) this.countingTotalColumn = new HashSet<>();
    }

    private void buildHeaders() {
        Map<Position, HeaderColumn> headersByPosition = new HashMap<>();
        SortedSet<HeaderColumn> highLevelHeaders = new TreeSet<>();

        Field[] fields = dataClass.getDeclaredFields();
        dataClass.getAnnotation(TextStyle.class);

        for (Field f : fields) {
            TableColumn tc = f.getAnnotation(TableColumn.class);
            if (tc != null) {
                this.fillMetaDataStore(f);
                String name = tc.name();
                int position = tc.position();
                if (leftPosition == -1 || leftPosition > position) leftPosition = position;
                if (rightPosition < position) rightPosition = position;
                HeaderColumn c = new HeaderColumn(position, name);
                headersByPosition.put(c.getPosition(), c);
            }
        }

        Annotation[] annotations = dataClass.getAnnotations();
        for (Annotation an : annotations) {
            if (an instanceof JoinTableColumn) this.extractHighLevelFromMeta((JoinTableColumn) an, highLevelHeaders);
            else if (an instanceof MultiJoinTableColumn) {
                MultiJoinTableColumn mjtc = (MultiJoinTableColumn) an;
                for (JoinTableColumn jtc : mjtc.values()) this.extractHighLevelFromMeta(jtc, highLevelHeaders);
            }
        }
        while (!highLevelHeaders.isEmpty()) {
            HeaderColumn hc = highLevelHeaders.last();
            assert hc.getLevel() > 1;
            int levelDown = hc.getLevel() - 1;
            for (int level = levelDown; level > 0; level--) {
                HeaderColumn childHeader;
                int bufferNextStartPosition = hc.getPosition().startPosition;
                do {
                    Position levelDownPosition = new Position(bufferNextStartPosition, level);
                    childHeader = headersByPosition.get(levelDownPosition);
                    if (childHeader != null) {
                        hc.addChild(childHeader);
                        bufferNextStartPosition += childHeader.getLength();
                        headersByPosition.remove(levelDownPosition);
                    } else bufferNextStartPosition += 1;
                } while (bufferNextStartPosition <= hc.getLastPosition());
            }
            headersByPosition.put(hc.getPosition(), hc);
            highLevelHeaders.remove(hc);
        }
        this.headers = new TreeSet<>();
        this.headers.addAll(headersByPosition.values());
    }

    private void fillMetaDataStore(Field f) {
        TableColumn tc = f.getAnnotation(TableColumn.class);
        if (tc != null) {
            try {
                int pos = tc.position();
                String fieldName = f.getName();
                String getMethod = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Method method = dataClass.getDeclaredMethod(getMethod);
                MetaDataColumn mdc = new MetaDataColumn(method);
                Style ownColumnStyle = null;
                TextStyle ts = f.getAnnotation(TextStyle.class);
                if (ts != null) {
                    ownColumnStyle = Style.createFromAnnotation(ts);
                    mdc.columnStyle = ownColumnStyle;
                    if (ts.height() != -1) this.sizeBodyRow = ts.height();
                }
                mdc.width = tc.width();

                FormatColumn fc = f.getAnnotation(FormatColumn.class);
                if (fc != null) {
                    mdc.formatPattern = fc.pattern();
                    mdc.length = fc.length();
                    if (Number.class.isAssignableFrom(f.getType())) {
                        mdc.decimalFormat = new DecimalFormat(fc.pattern());
                    }else if(Date.class.isAssignableFrom(f.getType())) mdc.dateFormat = new SimpleDateFormat(fc.pattern());
                }
                if (ownColumnStyle != null) mdc.styleIndex = (short) this.registerNewStyle(ownColumnStyle);
                CountTotalColumn ctc = f.getAnnotation(CountTotalColumn.class);
                if (hasTotalLine && ctc != null) {
                    this.countingTotalColumn.add(pos);
                    mdc.totalFunction = ctc.function();
                }
                metaColumns.put(pos, mdc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void extractHighLevelFromMeta(JoinTableColumn jtc, SortedSet<HeaderColumn> highLevelHeaders) {
        String columnName = jtc.name();
        int firstColumn = jtc.firstColumn();
        int lastColumn = jtc.lastColumn();
        short level = jtc.level();
        HeaderColumn joinColumn = new HeaderColumn(firstColumn, lastColumn, columnName, level);
        highLevelHeaders.add(joinColumn);
    }

    private int registerNewStyle(Style style) {
        this.styles.add(style);
        return this.styles.size() - 1;
    }

    private Object getColumnValue(T t, MetaDataColumn mdc) {
        Object fieldValue;
        try {
            fieldValue = mdc.getMethod.invoke(t);
            if (fieldValue instanceof String) {
                String str = (String) fieldValue;
                if (mdc.hasFormatPattern()) str = Formatter.applyRegularExpression(str, mdc.formatPattern);
                if (!mdc.isInfiniteLength() && mdc.length > str.length()) str = str.substring(0, mdc.length);
                fieldValue = str;
            } else if (mdc.hasScale() && fieldValue instanceof Number) {
                fieldValue = Formatter.applyNumberPattern((Number) fieldValue, mdc.decimalFormat);
            } else if (fieldValue instanceof Date) {
                fieldValue = Formatter.applyDatePattern(mdc.dateFormat, (Date) fieldValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fieldValue = new Object();
        }
        return fieldValue;
    }

    private List<T> applyGrouping(int groupingColumn) {
        MetaDataColumn mdc = this.metaColumns.get(groupingColumn);
        Method method = mdc.getMethod;

        Comparator<T> c = (t1, t2) -> {
            int compareResult = 0;
            try {
                Object o1 = method.invoke(t1);
                Object o2 = method.invoke(t2);
                Class<?> returnType = method.getReturnType();

                if (returnType.equals(String.class)) {
                    String s1 = (String) o1;
                    String s2 = (String) o2;
                    compareResult = s1.compareTo(s2);
                } else if (Number.class.isAssignableFrom(returnType)) {
                    Number n1 = (Number) o1;
                    Double d1 = n1.doubleValue();
                    Number n2 = (Number) o2;
                    Double d2 = n2.doubleValue();
                    compareResult = d1.compareTo(d2);
                } else if (returnType.equals(Date.class)) {
                    Date d1 = (Date) o1;
                    Date d2 = (Date) o2;
                    compareResult = d1.compareTo(d2);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return compareResult;
        };
        List<T> result = this.data;
        result.sort(c);
        return result;
    }

    private List<TableRow> getRowIncludeTotalRows() {
        TotalLine tableTotalLineMeta = this.dataClass.getAnnotation(TotalLine.class);
        GroupTotalLine blockTotalLineMeta = this.dataClass.getAnnotation(GroupTotalLine.class);
        boolean hasGrouping = blockTotalLineMeta != null;
        boolean hasTableTotalLine = tableTotalLineMeta != null;
        int indexGroupColumn = -1;

        if (hasGrouping) indexGroupColumn = blockTotalLineMeta.groupByColumn();

        List<TableRow> result = new ArrayList<>(data.size());
        List<T> data;

        TotalRow tableTotalRow = null;
        TotalRow blockTotalRow = null;
        MetaDataColumn mdcGroupingColumn = null;
        Object preGroupingValue = null;

        if (hasGrouping) {
            data = this.applyGrouping(indexGroupColumn);
            blockTotalRow = new TotalRow(columnCount, this.blockMetaDataTotal.styleIndex, this.leftPosition);
            mdcGroupingColumn = metaColumns.get(indexGroupColumn);
            preGroupingValue = this.getColumnValue(data.get(0), mdcGroupingColumn);
        } else data = this.data;

        if (hasTableTotalLine) {
            tableTotalRow = new TotalRow(columnCount, this.tableMetaDataTotal.styleIndex, this.leftPosition);
        }

        List<Integer> columnIndexes = new ArrayList<>(metaColumns.keySet());
        Collections.sort(columnIndexes);
        List<MetaDataColumn> metaDataColumns = new ArrayList<>(this.metaColumns.values());

        for (int i = 0; i < data.size(); i++) {
            T t = data.get(i);
            if (hasGrouping) {
                Object currentGroupingValue = this.getColumnValue(t, mdcGroupingColumn);
                if (!currentGroupingValue.equals(preGroupingValue)) {
                    if (blockTotalLineMeta.includeDelimiterInTitle())
                        blockTotalRow.push(indexGroupColumn, blockTotalLineMeta.title() + preGroupingValue.toString());
                    else blockTotalRow.push(indexGroupColumn, blockTotalLineMeta.title());
                    TableRow summarize = blockTotalRow.summarize(metaDataColumns);
                    blockTotalRow.clearCellData();
                    summarize.setHeight(this.sizeBodyRow);
                    result.add(summarize);
                }
                preGroupingValue = currentGroupingValue;
            }
            TableRow row = new TableRow(columnCount);
            row.setHeight(this.sizeBodyRow);
            for (Integer columnIndex : columnIndexes) {
                MetaDataColumn mdc = metaColumns.get(columnIndex);
                Object fieldValue = this.getColumnValue(t, mdc);
                row.push(fieldValue, mdc.styleIndex);
                if(this.countingTotalColumn.contains(columnIndex)){
                    Function f = mdc.totalFunction;
                    if (hasTableTotalLine) tableTotalRow.push(columnIndex, fieldValue, f);
                    if (hasGrouping && this.countingTotalColumn.contains(columnIndex))
                        blockTotalRow.push(columnIndex, fieldValue, f);
                }
            }
            result.add(row);

            if (hasGrouping && i == data.size() - 1) {
                if (blockTotalLineMeta.includeDelimiterInTitle())
                    blockTotalRow.push(indexGroupColumn, blockTotalLineMeta.title() + preGroupingValue.toString());
                else blockTotalRow.push(indexGroupColumn, blockTotalLineMeta.title());
                TableRow summarize = blockTotalRow.summarize(metaDataColumns);
                blockTotalRow.clearCellData();
                summarize.setHeight(this.sizeBodyRow);
                result.add(summarize);
            }
        }
        if (hasTableTotalLine) {
            int titlePosition;
            if (tableTotalLineMeta.titlePosition() == -1) titlePosition = this.leftPosition;
            else titlePosition = tableTotalLineMeta.titlePosition();
            tableTotalRow.push(titlePosition, tableTotalLineMeta.title());
            TableRow summarize = tableTotalRow.summarize(metaDataColumns);
            summarize.setHeight(this.sizeBodyRow);
            result.add(summarize);
        }
        return result;
    }

    /**
     * INNER CLASS
     */
    static class MetaDataColumn {
        Method getMethod;
        Style columnStyle;
        //длина принятая за стандартную единицу указана как (-1),
        // все что отличное будет указывать как нестандарт и интерпретироваться в DocumentConstructor
        int width = -1;
        short styleIndex = 0;
        String formatPattern;
        SimpleDateFormat dateFormat;
        DecimalFormat decimalFormat;
        int length = -1;
        Function totalFunction = null;

        MetaDataColumn(Method getMethod) {
            this.getMethod = getMethod;
        }

        MetaDataColumn(Method getMethod, short styleIndex) {
            this.getMethod = getMethod;
            this.styleIndex = styleIndex;
        }

        boolean hasStandardWidth() {
            return width == -1;
        }

        boolean hasFormatPattern() {
            return formatPattern != null;
        }

        boolean isInfiniteLength() {
            return length == -1;
        }

        boolean hasScale() {
            return decimalFormat != null;
        }
    }

    static class MetaDataTotal {
        int styleIndex;
        Set<Integer> countingColumns = new HashSet<>();

        public MetaDataTotal(int styleIndex) {
            this.styleIndex = styleIndex;
        }
    }
}
