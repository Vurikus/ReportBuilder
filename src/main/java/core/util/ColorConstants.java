package core.util;

import java.util.Random;

public class ColorConstants {

    private static final Random random = new Random();

    public static final String BLACK = "#000000";
    public static final String WHITE = "#ffffff";
    public static final String YELLOW = "#fed42a";
    public static final String GREEN = "#19c158";
    public static final String RED = "#ed4224";
    public static final String GRAY = "#b6b6b6";
    public static final String LIGHT_GRAY = "#E0E0E0";
    public static final String BLUE = "#1a93e1";
    public static final String LIGHT_BLUE = "#6a89f7";
    public static final String ORANGE = "#e6915a";
    public static final String LIGHT_ORANGE = "#dcaa84";
    public static final String PURPLE = "#ba73ce";
    public static final String INDIGO = "#0c4168";

    /**
     * CONSTRUCTORS
     */
    private ColorConstants() {}

    /**
     * METHODS
     */
    public static byte[] getColorRGB(String rgb){
        int i = Integer.decode(rgb);
        int r = i >> 16 & 255;
        int g = i >> 8 & 255;
        int b = i & 255;
        return new byte[]{(byte) r, (byte) g, (byte) b};
    }
}
