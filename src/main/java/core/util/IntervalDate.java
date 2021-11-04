package core.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class IntervalDate implements Cloneable{

    private Date start;
    private Date end;
    private SimpleDateFormat sdf;

    /**
     * CONSTRUCTORS
     */
    public IntervalDate(Date start, Date end) {
        this.start = start;
        this.end = end;
    }

    /**
     * METHODS
     */
    public long getTimeBetweenDate(){
        return this.getTimeBetweenDate(PeriodIntervalConstant.MILLISECONDS);
    }

    public long getTimeBetweenDate(PeriodIntervalConstant returnMeasure){
        double factor = PeriodIntervalConstant.multiplierBetween(PeriodIntervalConstant.MILLISECONDS, returnMeasure);
        return Math.round((end.getTime() - start.getTime()) * factor);
    }

    public boolean startAfterEnd(){
        return start.after(end);
    }

    public boolean startBeforeEnd(){
        return !startAfterEnd();
    }

    /**
     * Метод округления. Отсекает все значения для времени после переданного параметра точности с округлением вниз
     * Пример: время - 15:46:12:789, точность - минуты. Метод вернет: 15:46:00:000
     */
    public static Date roundDown(Date date, PeriodIntervalConstant r) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        switch (r){
            case MONTH: calendar.set(Calendar.DAY_OF_MONTH, 1);
            case WEEK: calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            case DAY: calendar.set(Calendar.HOUR, 0);
            case HOUR: calendar.set(Calendar.MINUTE, 0);
            case MINUTE: calendar.set(Calendar.SECOND, 0);
            case SECOND: calendar.set(Calendar.MILLISECOND, 0);
            case MILLISECONDS: calendar.set(Calendar.MILLISECOND, 0);
        }
        return calendar.getTime();
    }

    /**
     * Метод округления. Отсекает все значения для времени после переданного параметра точности с округлением вверх
     * Пример: время - 15:46:12:789, точность - минуты. Метод вернет: 15:47:00:000
     */
    public static Date roundUp(Date date, PeriodIntervalConstant r) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        switch (r){
            case MONTH: {
                calendar.setTime(roundDown(date, r));
                calendar.add(Calendar.MONTH, 1);
                break;
            }
            case WEEK: {
                calendar.setTime(roundDown(date, r));
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
                break;
            }
            case DAY: {
                calendar.setTime(roundDown(date, r));
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                break;
            }
            case HOUR: {
                calendar.setTime(roundDown(date, r));
                calendar.add(Calendar.HOUR, 1);
                break;
            }
            case MINUTE: {
                calendar.setTime(roundDown(date, r));
                calendar.add(Calendar.MINUTE, 1);
                break;
            }
            case SECOND: {
                calendar.setTime(roundDown(date, r));
                calendar.add(Calendar.SECOND, 1);
                break;
            }
            case MILLISECONDS: {
                calendar.setTime(roundDown(date, r));
                calendar.add(Calendar.MILLISECOND, 1);
                break;
            }
        }
        return calendar.getTime();
    }

    public String asText(SimpleDateFormat pattern){
        return pattern.format(start) + " по " + pattern.format(end);
    }

    public String asText(){
        if(sdf == null) sdf = new SimpleDateFormat("HH:mm dd-MM-yyyy");
        return this.asText(sdf);
    }

    @Override
    public IntervalDate clone() {
        try {
            return (IntervalDate)super.clone();
        }catch(Exception e){
            e.printStackTrace();
            return new IntervalDate(this.start, this.end);
        }
    }
}