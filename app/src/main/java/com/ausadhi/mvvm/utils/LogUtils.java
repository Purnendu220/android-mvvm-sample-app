package com.ausadhi.mvvm.utils;

import android.util.Log;


/**
 * Created by MyU10 on 1/18/2017.
 */

public class LogUtils {
    public static void networkError(String msg) {
        log("Network", "Error" + msg);
    }

    public static void networkSuccess(String msg) {
        loge("Network", "Success " + msg);
    }

    public static void xmppDebug(String msg) {
        log("XmppVarun", "Varun XMPP : " + msg);
    }

    public static void database(String msg) {
        log("DataBaseVarun", "SQL: " + msg);
    }

    public static void debug(String msg) {

        log("Purnendu Debug", msg);
    }
    public static void exception(Exception e) {
        log("EXCEPTION: ", e.toString());
    }

    private static void log(String tag, String msg) {
            Log.d(tag, msg);

    }
    private static void loge(String tag, String msg) {
        Log.d(tag, msg);

    }
}
