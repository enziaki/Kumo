package org.example.kumo.utils;

public class HelperFunctions {
    public static void printInfo() {
        System.out.println("Kumo: A spider which crawls the web");
    }
    public static void printHelp() {
        printInfo();
        System.out.printf("Usage:%n" +
                "<Kumo> <Url> [csvFileName]%n" +
                "Note: if csvFileName is empty, then the <Url> will be used%n");
    }
}
