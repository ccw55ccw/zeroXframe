package com.zerox.base.common;

import cn.hutool.log.StaticLog;

public class XLogger {

    public static void info(String msg, Object... strs){
        StaticLog.info(msg, strs);
    }

    public static void error(String msg, Object... strs){
        StaticLog.error(msg, strs);
    }

    public static void debug(String msg, Object... strs){
        StaticLog.debug(msg, strs);
    }
}
