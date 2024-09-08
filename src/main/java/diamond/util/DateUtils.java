package diamond.util;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;

/**
 * @author hacds
 * @date 2024/4/6 9:35
 */
@Slf4j
public class DateUtils {


    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final String FORMATTER_STR = "yyyy-MM-dd";

    public static final long ONE_MIN = 60000;
    /**
     *
     * @param time
     * @return
     */
    public static long strToLong(String time){
        TemporalAccessor parse = FORMATTER.parse(time);
        return LocalDateTime.from(parse).toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    }

    public static String timestampToDateStr(long time){
        return FORMATTER.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneOffset.ofHours(8)));
    }


    public static String timestampToDateStr(long time, String formatStr){
        return DateTimeFormatter.ofPattern(formatStr).format(LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneOffset.ofHours(8)));
    }


    public static long strToLong(String time, String pattern, Integer patternType){
        TemporalAccessor parse = DateTimeFormatter.ofPattern(pattern).parse(time);
        switch (patternType) {
            case 1:
               return LocalDateTime.from(parse).toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
            case 2:
                return LocalDate.from(parse).atStartOfDay(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
            default:
                return 0;
        }
    }

    public static String formatDate(Integer num) {
        Date date = new Date();//获取当前时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//设置格式
        Calendar calendar = Calendar.getInstance(); //创建Calendar 的实例c
        calendar.set(Calendar.MINUTE,0);
        calendar.add(Calendar.DAY_OF_MONTH, -num); //当前时间减去一天，即一天前的时间
        return simpleDateFormat.format(calendar.getTime());

    }

    public static String formatTime(String type,Integer num ,Integer cronTime ) {

        Calendar calendar = Calendar.getInstance(); //创建Calendar 的实例
        calendar.set(Calendar.MINUTE,0);
        if ("day".equals(type)){
            calendar.add(Calendar.DAY_OF_MONTH, -num); //当前时间减去一天，即一天前的时间
            return String.valueOf(calendar.getTimeInMillis());
        }
        if ("hour".equals(type)){
            calendar.add(Calendar.HOUR, -num); //当前时间减去一天，即一天前的时间
            return  String.valueOf(calendar.getTimeInMillis());
        }
        if ("current".equals(type)){
            return  String.valueOf(calendar.getTimeInMillis());
        }else {
            calendar.add(Calendar.DAY_OF_MONTH, -num); //当前时间减去一天，即一天前的时间
            if (cronTime>0){
                calendar.add(Calendar.HOUR, -cronTime); //当前时间减去一天，即一天前的时间
            }
            return String.valueOf(calendar.getTimeInMillis());
        }

    }


    public static String formatTime(boolean flag) {
        LocalDate today = LocalDate.now();

        if (flag) {
            return today.toString();
        } else {
            LocalDate tenYear = today.plus(10, ChronoUnit.YEARS);
            return tenYear.toString();
        }
    }

    public static String stampToDate(Long s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static long getStartTime(long time,int count){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        cal.add(Calendar.SECOND,count);
        return cal.getTimeInMillis();
    }

    public static long getEndTime(long time,int count){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        cal.add(Calendar.SECOND,count);
        return cal.getTimeInMillis();
    }

    public static long calTime(long time,int count, int calendar){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        cal.add(calendar,count);
        return cal.getTimeInMillis();
    }
}
