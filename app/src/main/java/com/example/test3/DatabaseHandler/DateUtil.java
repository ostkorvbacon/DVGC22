package com.example.test3.DatabaseHandler;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public interface DateUtil {

    String ISO_DATE_FORMAT_ZERO_OFFSET = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    String UTC_TIMEZONE_NAME = "GMT-2";

    static SimpleDateFormat provideDateFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ISO_DATE_FORMAT_ZERO_OFFSET);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(UTC_TIMEZONE_NAME));
        return simpleDateFormat;
    }
}
