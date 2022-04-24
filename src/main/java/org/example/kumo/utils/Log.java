package org.example.kumo.utils;

import java.util.logging.Logger;
import java.util.logging.Level;

// TODO: Switch to Logger
public class Log {

    public synchronized static void debug(String message, Object... args) {
        String threadName = Thread.currentThread().getName();
        System.out.println(String.format("[DEBUG] " + message, args));
    }

    public synchronized static void info(String message, Object... args) {
        String threadName = Thread.currentThread().getName();
        String infoMessage = String.format("[INFO (%s)] ", threadName);
        System.out.println(String.format(infoMessage + message, args));
    }

    public synchronized static void error(String message, Object... args) {
        String threadName = Thread.currentThread().getName();
        String errorMessage = String.format("[Error (%s)] ", threadName);
        System.out.println(String.format(errorMessage, args));
    }
}
