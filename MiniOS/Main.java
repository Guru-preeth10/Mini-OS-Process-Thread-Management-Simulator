

import os.process.Process;
import os.thread.ThreadTask;
import os.task.Task;
import os.task.PrintTask;
import os.task.CalculationTask;
import os.scheduler.SimpleScheduler;
import os.utils.Logger;
import os.exception.InvalidProcessException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main.java - Interactive Menu-driven Mini OS Process & Thread Management Simulator
 *
 * USER-INTERACTIVE VERSION:
 * - User creates processes dynamically
 * - User adds threads to processes
 * - User executes specific processes
 * - Real-time OS simulator with user control
 *
 * Demonstrates:
 * - Interactive user input with Scanner
 * - Dynamic process and thread creation
 * - Proper exception handling
 * - OOP concepts in action
 *
 * Author: Java OOP Mini Project
 */
public class Main {

    private static Scanner scanner;
    private static SimpleScheduler scheduler;
    private static List<Process> processRepository;
    private static int processIdCounter = 1;
    private static int threadIdCounter = 1;
    private static int taskIdCounter = 101;
    private static Logger logger;

    public static void main(String[] args) {
        logger = Logger.getInstance();
        logger.writeInfoLog("Interactive Mini OS Simulator Started");

        try {
            // Initialize resources
            scanner = new Scanner(System.in);
            scheduler = new SimpleScheduler("Primary OS Scheduler");
            processRepository = new ArrayList<>();

            // Display welcome banner
            displayWelcomeBanner();

            // Main menu loop
            boolean running = true;
            while (running) {
                displayMainMenu();
                int choice = getUserChoice(1, 5);

                switch (choice) {
                    case 1:
                        createProcessMenu();
                        break;
                    case 2:
                        addThreadToProcessMenu();
                        break;
                    case 3:
                        runProcessMenu();
                        break;
                    case 4:
                        viewSchedulerInfo();
                        break;
                    case 5:
                        running = false;
                        exitProgram();
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }

        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            logger.writeErrorLog("Unexpected error: " + e.getMessage());
        } finally {
            if (scanner != null) {
                scanner.close();
            }
            logger.close();
        }
    }

    /**
     * Display welcome banner
     */
    private static void displayWelcomeBanner() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                            ║");
        System.out.println("║   Mini OS Process & Thread Management Simulator            ║");
        System.out.println("║   INTERACTIVE MENU-DRIVEN VERSION                          ║");
        System.out.println("║                                                            ║");
        System.out.println("║   Dynamic Process & Thread Creation with User Control      ║");
        System.out.println("║                                                            ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        System.out.println("Scheduler: " + scheduler.getSchedulerName());
        logger.writeInfoLog("Interactive simulator initialized with Scheduler: " + scheduler.getSchedulerName());
    }

    /**
     * Display main menu options
     */
    private static void displayMainMenu() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("MAIN MENU - Mini OS Simulator");
        System.out.println("═".repeat(60));
        System.out.println("1. Create a New Process");
        System.out.println("2. Add Thread to Process");
        System.out.println("3. Run a Process");
        System.out.println("4. View Scheduler Information");
        System.out.println("5. Exit Program");
        System.out.println("═".repeat(60));
        System.out.print("Enter your choice (1-5): ");
    }

    /**
     * Get user choice with validation
     */
    private static int getUserChoice(int min, int max) {
        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            if (choice >= min && choice <= max) {
                return choice;
            } else {
                System.out.println("Invalid choice. Please enter a number between " + min + " and " + max);
                return getUserChoice(min, max);
            }
        } catch (Exception e) {
            scanner.nextLine(); // Clear invalid input
            System.out.println("Invalid input. Please enter a valid number.");
            return getUserChoice(min, max);
        }
    }

    /**
     * Create a new process
     */
    private static void createProcessMenu() {
        System.out.println("\n" + "─".repeat(60));
        System.out.println("CREATE NEW PROCESS");
        System.out.println("─".repeat(60));

        System.out.print("Enter process name: ");
        String processName = scanner.nextLine().trim();

        if (processName.isEmpty()) {
            System.out.println("❌ Process name cannot be empty!");
            logger.writeWarningLog("Attempted to create process with empty name");
            return;
        }

        try {
            Process newProcess = new Process(processIdCounter++, processName);
            processRepository.add(newProcess);
            scheduler.schedule(newProcess);

            System.out.println("\n✅ Process created successfully!");
            System.out.println("   Process ID: " + newProcess.getProcessId());
            System.out.println("   Process Name: " + newProcess.getProcessName());
            System.out.println("   Threads Count: " + newProcess.getThreadCount());

            logger.writeSuccessLog("Process created: " + processName + " (ID: " + newProcess.getProcessId() + ")");

        } catch (Exception e) {
            System.out.println("❌ Error creating process: " + e.getMessage());
            logger.writeErrorLog("Failed to create process: " + e.getMessage());
        }
    }

    /**
     * Add thread to a process
     */
    private static void addThreadToProcessMenu() {
        System.out.println("\n" + "─".repeat(60));
        System.out.println("ADD THREAD TO PROCESS");
        System.out.println("─".repeat(60));

        if (processRepository.isEmpty()) {
            System.out.println("❌ No processes available! Create a process first.");
            logger.writeWarningLog("Attempted to add thread but no processes exist");
            return;
        }

        // Display available processes
        System.out.println("\nAvailable Processes:");
        for (int i = 0; i < processRepository.size(); i++) {
            Process p = processRepository.get(i);
            System.out.println((i + 1) + ". " + p.getProcessName() + " (ID: " + p.getProcessId() +
                    ", Threads: " + p.getThreadCount() + ")");
        }

        System.out.print("\nSelect process number: ");
        int processChoice = getUserChoice(1, processRepository.size());
        Process selectedProcess = processRepository.get(processChoice - 1);

        // Select task type
        System.out.println("\nSelect Task Type:");
        System.out.println("1. Print Task");
        System.out.println("2. Calculation Task");
        System.out.print("Enter choice (1-2): ");
        int taskChoice = getUserChoice(1, 2);

        try {
            Task task;
            ThreadTask newThread;

            if (taskChoice == 1) {
                // Print Task
                System.out.print("Enter message to print: ");
                String message = scanner.nextLine().trim();

                System.out.print("Enter number of iterations: ");
                int iterations = scanner.nextInt();
                scanner.nextLine();

                if (iterations < 1) {
                    System.out.println("❌ Iterations must be at least 1");
                    logger.writeWarningLog("Invalid iterations value: " + iterations);
                    return;
                }

                task = new PrintTask("PrintTask-" + taskIdCounter, taskIdCounter++, message, iterations);
                newThread = new ThreadTask(threadIdCounter++, task);

                selectedProcess.addThreadTask(newThread);

                System.out.println("\n✅ Print Task added successfully!");
                System.out.println("   Task: " + task.getTaskName());
                System.out.println("   Message: " + message);
                System.out.println("   Iterations: " + iterations);

                logger.writeSuccessLog("PrintTask added to process: " + selectedProcess.getProcessName());

            } else {
                // Calculation Task
                System.out.print("Enter first number: ");
                int num1 = scanner.nextInt();

                System.out.print("Enter second number: ");
                int num2 = scanner.nextInt();
                scanner.nextLine();

                System.out.println("Available operations: +, -, *, /");
                System.out.print("Enter operation: ");
                String operation = scanner.nextLine().trim();

                if (!operation.matches("[+\\-*/]")) {
                    System.out.println("❌ Invalid operation. Use +, -, *, or /");
                    logger.writeWarningLog("Invalid operation attempted: " + operation);
                    return;
                }

                task = new CalculationTask("CalcTask-" + taskIdCounter, taskIdCounter++, num1, num2, operation);
                newThread = new ThreadTask(threadIdCounter++, task);

                selectedProcess.addThreadTask(newThread);

                System.out.println("\n✅ Calculation Task added successfully!");
                System.out.println("   Task: " + task.getTaskName());
                System.out.println("   Operation: " + num1 + " " + operation + " " + num2);

                logger.writeSuccessLog("CalculationTask added to process: " + selectedProcess.getProcessName());
            }

        } catch (InvalidProcessException e) {
            System.out.println("❌ Error adding thread: " + e.getMessage());
            logger.writeErrorLog("Failed to add thread: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Unexpected error: " + e.getMessage());
            logger.writeErrorLog("Unexpected error adding thread: " + e.getMessage());
        }
    }

    /**
     * Run a selected process
     */
    private static void runProcessMenu() {
        System.out.println("\n" + "─".repeat(60));
        System.out.println("RUN PROCESS");
        System.out.println("─".repeat(60));

        if (processRepository.isEmpty()) {
            System.out.println("❌ No processes available! Create a process first.");
            logger.writeWarningLog("Attempted to run process but no processes exist");
            return;
        }

        // Display available processes
        System.out.println("\nAvailable Processes:");
        for (int i = 0; i < processRepository.size(); i++) {
            Process p = processRepository.get(i);
            System.out.println((i + 1) + ". " + p.getProcessName() + " (ID: " + p.getProcessId() +
                    ", Threads: " + p.getThreadCount() + ", State: " + p.getState() + ")");
        }

        System.out.print("\nSelect process to run: ");
        int processChoice = getUserChoice(1, processRepository.size());
        Process selectedProcess = processRepository.get(processChoice - 1);

        if (selectedProcess.getThreadCount() == 0) {
            System.out.println("❌ Cannot run process with no threads! Add threads first.");
            logger.writeWarningLog("Attempted to run process with no threads: " + selectedProcess.getProcessName());
            return;
        }

        try {
            System.out.println("\n" + "─".repeat(60));
            logger.writeInfoLog("Starting process execution: " + selectedProcess.getProcessName());

            selectedProcess.startProcess();
            selectedProcess.waitForCompletion();

            logger.writeSuccessLog("Process execution completed: " + selectedProcess.getProcessName());
            System.out.println("─".repeat(60));

        } catch (InvalidProcessException e) {
            System.out.println("❌ Error running process: " + e.getMessage());
            logger.writeErrorLog("Error running process: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Unexpected error: " + e.getMessage());
            logger.writeErrorLog("Unexpected error running process: " + e.getMessage());
        }
    }

    /**
     * View scheduler information
     */
    private static void viewSchedulerInfo() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("SCHEDULER INFORMATION");
        System.out.println("═".repeat(60));

        System.out.println("Scheduler Name: " + scheduler.getSchedulerName());
        System.out.println("Total Scheduled Processes: " + scheduler.getScheduledProcessCount());

        if (processRepository.isEmpty()) {
            System.out.println("\n⚠️  No processes created yet.");
        } else {
            System.out.println("\nProcess Details:");
            for (Process p : processRepository) {
                System.out.println("\n  Process ID: " + p.getProcessId());
                System.out.println("  Name: " + p.getProcessName());
                System.out.println("  State: " + p.getState());
                System.out.println("  Threads: " + p.getThreadCount());

                if (p.getThreadCount() > 0) {
                    System.out.println("  Threads List:");
                    for (ThreadTask t : p.getThreads()) {
                        String status = t.isCompleted() ? "COMPLETED ✅" : "PENDING ⏳";
                        System.out.println("    - " + t.getName() + ": " + t.getTask().getTaskName() +
                                " [" + status + "]");
                    }
                }
            }
        }

        System.out.println("═".repeat(60));
        logger.writeInfoLog("Scheduler info viewed");
    }

    /**
     * Exit the program
     */
    private static void exitProgram() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("TERMINATING MINI OS SIMULATOR");
        System.out.println("═".repeat(60));

        // Display final statistics
        if (!processRepository.isEmpty()) {
            int totalThreads = 0;
            int completedProcesses = 0;

            for (Process p : processRepository) {
                totalThreads += p.getThreadCount();
                if (p.getState() == Process.ProcessState.COMPLETED) {
                    completedProcesses++;
                }
            }

            System.out.println("\nFinal Statistics:");
            System.out.println("  Total Processes Created: " + processRepository.size());
            System.out.println("  Processes Executed: " + completedProcesses);
            System.out.println("  Total Threads Created: " + totalThreads);
        } else {
            System.out.println("\nNo processes were created during this session.");
        }

        System.out.println("\n✅ Simulator terminated successfully.");
        System.out.println("📝 Check 'log.txt' for detailed execution logs.");
        System.out.println("═".repeat(60) + "\n");

        logger.writeInfoLog("Interactive Mini OS Simulator terminated");
    }
}
