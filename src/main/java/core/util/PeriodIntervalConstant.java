package core.util;

public enum PeriodIntervalConstant {
    MILLISECONDS("Миллисекунда", 0.001F),
    SECOND("Секунда", 1),
    MINUTE("Минута", 60),
    HOUR("Час", 3600),
    DAY("День", 86400),
    WEEK("Неделя", 604800),
    MONTH("Месяц", 2628000);

    private String display;
    private float countSecond;

    PeriodIntervalConstant(String display, float countSecond){
        this.countSecond = countSecond;
        this.display = display;
    }

    public String getDisplay() { return display; }

    public float getCountSecond() {
        return countSecond;
    }

    public static double multiplierBetween(PeriodIntervalConstant from, PeriodIntervalConstant to){
        return (double) from.countSecond / to.countSecond;
    }
}
