package com.example.springboot.feature_report.logConstants;

public class LogConstants {
    public static String RECIEVED_MESSAGE = "Received Kafka message: ";
    public static String GENERATED_REPORT = "Generated report for ";
    public static String CACHED_REPORT = "Cached report with key: ";
    public static String FETCH_CACHE = "Fetching report from cache...";
    public static String FETCH_DB = "Fetching report from DB...";
    public static String NO_TRANSACTION_FOUND = "No transactions found for the given period.";
    public static String TRANSACTIONS_FOUND_FROM_DB = "Fetched {} transactions from DB";
}
