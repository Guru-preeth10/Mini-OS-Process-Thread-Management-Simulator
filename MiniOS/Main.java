
import os.process.Process;
import os.thread.ThreadTask;
import os.task.Task;
import os.task.PrintTask;
import os.task.CalculationTask;
import os.scheduler.SimpleScheduler;
import os.utils.Logger;
import os.exception.InvalidProcessException;

/**
 * Main.java - Entry point for Mini OS Process & Thread Management Simulator
 *
 * Demonstrates:
 * - Process and Thread creation
 * - Task polymorphism (PrintTask and CalculationTask)
 * - Concurrent thread execution
 * - Exception handling
 * - Scheduler interface implementation
 * - File logging with Logger utility
 *
 * Author: Java OOP Mini Project
 */
public class Main {

    public static void main(String[] args) {
        Logger logger = Logger.getInstance();
        logger.writeInfoLog("Mini OS Simulator Started");

        try {
            System.out.println("\n╔════════════════════════════════════════════════════╗");
            System.out.println("║   Mini OS Process & Thread Management Simulator     ║");
            System.out.println("║           Demonstrating OOP Concepts                ║");
            System.out.println("╚════════════════════════════════════════════════════╝\n");

            // Create a scheduler
            SimpleScheduler scheduler = new SimpleScheduler("Primary OS Scheduler");
            logger.writeInfoLog("Scheduler created: " + scheduler.getSchedulerName());

            // ==================== PROCESS 1 ====================
            System.out.println(">>> Creating Process 1: Background Tasks <<<\n");
            Process process1 = new Process(1, "Background Tasks");
            logger.writeInfoLog("Process 1 created: Background Tasks");

            // Create tasks for Process 1
            Task printTask1 = new PrintTask("System Status Printer", 101,
                "System running normally...", 3);
            Task printTask2 = new PrintTask("Error Logger", 102,
                "Checking for errors...", 2);

            // Create threads for Process 1 and add them
            ThreadTask thread1 = new ThreadTask(1, printTask1);
            ThreadTask thread2 = new ThreadTask(2, printTask2);

            process1.addThreadTask(thread1);
            process1.addThreadTask(thread2);

            // Schedule Process 1
            scheduler.schedule(process1);

            // ==================== PROCESS 2 ====================
            System.out.println("\n>>> Creating Process 2: Calculations <<<\n");
            Process process2 = new Process(2, "Calculations");
            logger.writeInfoLog("Process 2 created: Calculations");

            // Create calculation tasks for Process 2
            Task calcTask1 = new CalculationTask("Addition Task", 201, 100, 50, "+");
            Task calcTask2 = new CalculationTask("Multiplication Task", 202, 12, 15, "*");
            Task calcTask3 = new CalculationTask("Division Task", 203, 144, 12, "/");

            // Create threads for Process 2
            ThreadTask thread3 = new ThreadTask(3, calcTask1);
            ThreadTask thread4 = new ThreadTask(4, calcTask2);
            ThreadTask thread5 = new ThreadTask(5, calcTask3);

            process2.addThreadTask(thread3);
            process2.addThreadTask(thread4);
            process2.addThreadTask(thread5);

            // Schedule Process 2
            scheduler.schedule(process2);

            // ==================== PROCESS 3 ====================
            System.out.println("\n>>> Creating Process 3: Mixed Tasks <<<\n");
            Process process3 = new Process(3, "Mixed Tasks");
            logger.writeInfoLog("Process 3 created: Mixed Tasks");

            // Create mixed tasks
            Task printTask3 = new PrintTask("Data Sync", 301, "Syncing data to server...", 2);
            Task calcTask4 = new CalculationTask("Hash Calculation", 302, 2048, 256, "+");
            Task printTask4 = new PrintTask("Cleanup Handler", 303, "Cleaning temporary files...", 1);

            // Create threads for Process 3
            ThreadTask thread6 = new ThreadTask(6, printTask3);
            ThreadTask thread7 = new ThreadTask(7, calcTask4);
            ThreadTask thread8 = new ThreadTask(8, printTask4);

            process3.addThreadTask(thread6);
            process3.addThreadTask(thread7);
            process3.addThreadTask(thread8);

            // Schedule Process 3
            scheduler.schedule(process3);

            // ==================== DISPLAY SCHEDULER INFO ====================
            System.out.println("\n╔════════════════════════════════════════════════════╗");
            System.out.println("║              Scheduler Information                  ║");
            System.out.println("╚════════════════════════════════════════════════════╝\n");
            System.out.println("Scheduler Name: " + scheduler.getSchedulerName());
            System.out.println("Total Scheduled Processes: " + scheduler.getScheduledProcessCount());
            logger.writeInfoLog("Scheduler info: " + scheduler);

            for (Process p : scheduler.getScheduledProcesses()) {
                System.out.println("  - " + p);
            }

            // ==================== EXECUTE ALL PROCESSES ====================
            System.out.println("\n╔════════════════════════════════════════════════════╗");
            System.out.println("║           Executing All Processes                   ║");
            System.out.println("╚════════════════════════════════════════════════════╝");

            long overallStartTime = System.currentTimeMillis();
            scheduler.executeAllProcesses();
            long overallEndTime = System.currentTimeMillis();

            // ==================== FINAL REPORT ====================
            System.out.println("\n╔════════════════════════════════════════════════════╗");
            System.out.println("║              Execution Complete Report              ║");
            System.out.println("╚════════════════════════════════════════════════════╝\n");

            System.out.println("Total Overall Execution Time: " + (overallEndTime - overallStartTime) + "ms");
            System.out.println("Total Processes Completed: " + scheduler.getScheduledProcessCount());

            int totalThreads = 0;
            for (Process p : scheduler.getScheduledProcesses()) {
                totalThreads += p.getThreadCount();
            }
            System.out.println("Total Threads Executed: " + totalThreads);

            logger.writeSuccessLog("All processes executed successfully");
            logger.writeInfoLog("Overall execution time: " + (overallEndTime - overallStartTime) + "ms");

            // ==================== EXCEPTION HANDLING DEMO ====================
            System.out.println("\n>>> Exception Handling Demonstration <<<\n");
            demonstrateExceptionHandling();

            System.out.println("\n╔════════════════════════════════════════════════════╗");
            System.out.println("║     Mini OS Simulator Execution Completed           ║");
            System.out.println("║     Check 'log.txt' for detailed execution logs     ║");
            System.out.println("╚════════════════════════════════════════════════════╝\n");

            logger.writeInfoLog("Mini OS Simulator Completed");
            logger.close();

        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            Logger.getInstance().writeErrorLog("Unexpected error: " + e.getMessage());
            Logger.getInstance().close();
        }
    }

    /**
     * Demonstrates exception handling with custom InvalidProcessException
     */
    private static void demonstrateExceptionHandling() {
        Logger logger = Logger.getInstance();

        // Demo 1: Adding threads to completed process
        System.out.println("1. Attempting to add thread to completed process:");
        try {
            Process completedProcess = new Process(99, "Completed Process");
            completedProcess.setState(Process.ProcessState.COMPLETED);
            ThreadTask dummyThread = new ThreadTask(99, new PrintTask("Dummy", 999, "test", 1));
            completedProcess.addThreadTask(dummyThread);
        } catch (InvalidProcessException e) {
            System.out.println("   ✓ Caught Exception: " + e.getMessage());
            logger.writeInfoLog("Exception caught: " + e.getMessage());
        }

        // Demo 2: Starting process without threads
        System.out.println("\n2. Attempting to start process without threads:");
        try {
            Process emptyProcess = new Process(98, "Empty Process");
            emptyProcess.startProcess();
        } catch (InvalidProcessException e) {
            System.out.println("   ✓ Caught Exception: " + e.getMessage());
            logger.writeInfoLog("Exception caught: " + e.getMessage());
        }

        System.out.println("\n   Exception handling demonstration completed!");
    }
}
