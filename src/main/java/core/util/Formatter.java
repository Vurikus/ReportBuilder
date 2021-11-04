package core.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Усольцев Иван
 */
public class Formatter {

    /**
     * METHODS
     */
    public static Number applyNumberPattern(Number num, DecimalFormat df) {
        Number result = num;
        try {
            double d = num.doubleValue();
            String value = df.format(d);
            result = df.parse(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Number applyNumberPattern(Number num, short scale) {
        Number result = num;
        try {
            if (scale >= 0) {
                String format = "%." + scale + "f";
                String value = String.format(format, num);
                result = Double.parseDouble(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String applyRegularExpression(String str, String pattern) {
        return str;
    }

    public static String applyDatePattern(SimpleDateFormat sdf, Date date) {
        return sdf.format(date);
    }
}
