package core.writer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Усольцев Иван
 */
public class PrintSetting {

    public static final Short LANDSCAPE = 0;
    public static final Short AUTO_BREAKS = 1;

    public static final Short ALL_MARGIN = 3;
    public static final Short TOP_MARGIN = 4;
    public static final Short BOTTOM_MARGIN = 5;
    public static final Short LEFT_MARGIN = 6;
    public static final Short RIGHT_MARGIN = 7;
    public static final Short FOOTER_MARGIN = 8;

    public static final Short TOP_PRINT_AREA = 9;
    public static final Short BOTTOM_PRINT_AREA = 10;
    public static final Short LEFT_PRINT_AREA = 11;
    public static final Short RIGHT_PRINT_AREA = 12;

    public static final Short PAPER_SIZE = 13;
    public static final Short FIT_WIDTH = 14;
    public static final Short FIT_HEIGHT = 15;
    public static final Short FIT_TO_PAGE = 16;

    private final Map<Short, Object> setting = new HashMap<>();

    /**
     * METHODS
     */
    public void put(Short settingCode, Object value){
        this.setting.put(settingCode, value);
    }

    public Object get(Short settingCode){
        return this.setting.get(settingCode);
    }

    public boolean hasSetting(Short settingCode){
        return this.setting.containsKey(settingCode);
    }
}
