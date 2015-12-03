package com.rentalgeek.android.logging;

import android.util.Log;
import android.util.SparseArray;

import com.crashlytics.android.Crashlytics;

import java.util.EnumSet;

public class AppLogger {

    private static final String TAG = "AppLogger";

    public static final boolean isEnabled = true;

    public enum LogLevel {
        INFO, WARN, DEBUG, ERROR, NETWORK, DATABASE, LOCATION, IMAGING
    }

    public static final EnumSet<LogLevel> loggingLevels =
            EnumSet.of(LogLevel.DEBUG
                    , LogLevel.ERROR
                    , LogLevel.NETWORK
                    , LogLevel.DATABASE
                    , LogLevel.IMAGING
                    //,LogLevel.LOCATION
            );

    private static final int loggingLevelBits = encode(loggingLevels);

    public static void log(Throwable ex) {
        if (isEnabled) log(ex, false);
    }

    public static void log(String tag, Throwable ex) {
        if (isEnabled) log(tag, ex, true);
    }

    public static void log(Throwable ex, boolean printStackTrace) {
        if (isEnabled) log(TAG, ex, printStackTrace);
    }

    public static void log(String tag, Throwable ex, boolean printStackTrace) {

        if (isEnabled) {
            if (ex != null && ex.getMessage() != null)
                Log.e(tag, ex.getMessage());
            if (ex != null && printStackTrace)
                ex.printStackTrace();

            try {
                Crashlytics.logException(ex);
            } catch (Exception e) {

            }
        }
    }

    public static void log(String tag, String message) {
        if (message != null)
            Log.d(tag, message);
    }

    public static void log(String message) {
        log(TAG, message);
    }

    public static void logError(String tag, String message) {
        log(tag, new Exception(message));
    }


    public static void log(String message, LogLevel level) {
        if (level == LogLevel.ERROR) {
            Log.e(TAG, message);
            //TODO: Send to ACRA or equivalent?
        } else
            log(TAG, message);
    }

    public static void log(String tag, String message, LogLevel level) {
        if (loggingLevels.contains(level)) {
            if (level == LogLevel.ERROR) {
                Log.e(tag, message);
                //TODO: Send to ACRA or equivalent?
            } else {
                log(tag, message);
            }
        }
    }

    public static void log(String tag, String message, EnumSet<LogLevel> levels) {
        if (loggingLevelExists(levels)) {
            if (levels.contains(LogLevel.ERROR)) {
                Log.e(tag, message);
                //TODO: Send to ACRA or equivalent?
            } else {
                log(tag, message);
            }
        }
    }

    private static boolean loggingLevelExists(EnumSet<LogLevel> levels) {
        int levelBits = encode(levels);
        return (loggingLevelBits & levelBits) == levelBits;
    }

    public static <E extends Enum<E>> Integer encode(EnumSet<E> set) {
        if (set == null)
            return null;

        int ret = 0;

        for (E val : set) {
            ret |= (1 << val.ordinal());
        }

        return ret;
    }

    public static <E extends Enum<E>> EnumSet<E> decode(int encoded,
                                                        Class<E> enumKlazz) {

        SparseArray<E> ordinalMap = new SparseArray<E>();
        for (E val : EnumSet.allOf(enumKlazz)) {
            ordinalMap.put(val.ordinal(), val);
        }

        EnumSet<E> ret = EnumSet.noneOf(enumKlazz);
        int ordinal = 0;

        for (int i = 1; i != 0; i <<= 1) {
            if ((i & encoded) != 0) {
                ret.add(ordinalMap.get(ordinal));
            }
            ++ordinal;
        }

        return ret;
    }
}
