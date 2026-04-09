package os.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Logger class - Demonstrates file handling
 * Writes log messages to a file (log.txt)
 * Thread-safe logging with synchronized methods
 */
public class Logger {

    private static final String LOG_FILE = "log.txt";
    private static Logger instance; // Singleton pattern
    private BufferedWriter writer;
    private SimpleDateFormat dateFormat;

    /**
     * Private constructor - Singleton pattern
     */
    private Logger() {
        try {
            writer = new BufferedWriter(new FileWriter(LOG_FILE, true)); // Append mode
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            writeLog("========== Logger Initialized ==========");
        } catch (IOException e) {
            System.err.println("Error initializing logger: " + e.getMessage());
        }
    }

    /**
     * Get singleton instance of Logger
     * @return Logger instance
     */
    public static synchronized Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    /**
     * Write a log message to file (thread-safe)
     * @param message The message to log
     */
    public synchronized void writeLog(String message) {
        try {
            String timestamp = dateFormat.format(new Date());
            String logEntry = "[" + timestamp + "] " + message;

            if (writer != null) {
                writer.write(logEntry);
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }

    /**
     * Write an error log message
     * @param message The error message
     */
    public synchronized void writeErrorLog(String message) {
        writeLog("ERROR: " + message);
    }

    /**
     * Write an info log message
     * @param message The info message
     */
    public synchronized void writeInfoLog(String message) {
        writeLog("INFO: " + message);
    }

    /**
     * Write a warning log message
     * @param message The warning message
     */
    public synchronized void writeWarningLog(String message) {
        writeLog("WARNING: " + message);
    }

    /**
     * Write a success log message
     * @param message The success message
     */
    public synchronized void writeSuccessLog(String message) {
        writeLog("SUCCESS: " + message);
    }

    /**
     * Close the logger and file writer
     */
    public synchronized void close() {
        try {
            if (writer != null) {
                writeLog("========== Logger Closed ==========\n");
                writer.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing logger: " + e.getMessage());
        }
    }

    /**
     * Clear the log file
     */
    public synchronized void clearLog() {
        try {
            writer = new BufferedWriter(new FileWriter(LOG_FILE, false)); // Overwrite mode
            writeLog("========== Log Cleared ==========");
        } catch (IOException e) {
            System.err.println("Error clearing log file: " + e.getMessage());
        }
    }

    /**
     * Get the log file path
     * @return Path to log file
     */
    public String getLogFilePath() {
        return LOG_FILE;
    }
}
