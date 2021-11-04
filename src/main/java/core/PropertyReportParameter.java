package core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Усольцев Иван
 */
public class PropertyReportParameter implements ReportParameter {

    private final Map<Object, Object> map = new ConcurrentHashMap<>();

    /**
     * METHODS
     */
    public void putProperty(Object key, Object value){
        this.map.put(key, value);
    }

    public Object getProperty(Object key){
        return this.map.get(key);
    }
}
