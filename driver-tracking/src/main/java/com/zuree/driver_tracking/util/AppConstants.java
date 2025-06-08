package com.zuree.driver_tracking.util;

import java.time.format.DateTimeFormatter;

public class AppConstants {
    public static final DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final String ZONE_ID = "Asia/Kolkata";

    public static final long EXPIRATION_TIME = 7200000; // 2 -> 3600000 * 2 hour
    public static final String JWT_ALGORITHM = "HmacSHA256";
    public static final String ERROR = "error";
    public static final String MSG = "msg";
    public static final String DEVICE_NOT_FOUND = "Device Not Found 🚫";
    public static final String ALARM_NOT_FOUND = "Alarm Not Found 🚫";
    public static final String VEHICLE_NOT_FOUND = "Vehicle Not Found 🚫";
    public static final int CODE_NOT_FOUND = 404;
}
