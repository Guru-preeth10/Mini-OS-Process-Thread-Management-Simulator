package os.scheduler;

import os.process.Process;
import os.exception.InvalidProcessException;
import java.util.ArrayList;
import java.util.List;

/**
 * SimpleScheduler - Implements the Scheduler interface
 * Demonstrates:
 * - Interface implementation
 * - Polymorphism (implementing interface method)
 * - Collections (ArrayList for storing processes)
 * - Exception handling
 */
public class SimpleScheduler implements Scheduler {

    private String schedulerName;
    private List<Process> scheduledProcesses;
    private int processExecutionOrder; // Counter for process execution

    /**
     * Constructor for SimpleScheduler
     * @param schedulerName Name of the scheduler
     */
    public SimpleScheduler(String schedulerName) {
        this.schedulerName = schedulerName;
        this.scheduledProcesses = new ArrayList<>();
        this.processExecutionOrder = 0;
    }

    /**
     * Schedule a process for execution
     * Demonstrates interface implementation
     * @param process The process to schedule
     */
    @Override
    public void schedule(Process process) {
        if (process == null) {
            System.err.println("Cannot schedule null process");
            return;
        }

        scheduledProcesses.add(process);
        System.out.println("[SimpleScheduler] Scheduled process: " + process.getProcessName() +
                " (ID: " + process.getProcessId() + ")");
    }

    /**
     * Execute all scheduled processes
     */
    public void executeAllProcesses() {
        if (scheduledProcesses.isEmpty()) {
            System.out.println("[SimpleScheduler] No processes to execute");
            return;
        }

        System.out.println("\n[SimpleScheduler] Executing " + scheduledProcesses.size() + " scheduled processes...\n");

        for (Process process : scheduledProcesses) {
            try {
                processExecutionOrder++;
                System.out.println("\n[SimpleScheduler] Executing process #" + processExecutionOrder);

                process.startProcess();
                process.waitForCompletion();

            } catch (InvalidProcessException e) {
                System.err.println("[SimpleScheduler] Error executing process '" +
                    process.getProcessName() + "': " + e.getMessage());
            }
        }

        System.out.println("\n[SimpleScheduler] All processes executed successfully!");
    }

    /**
     * Get the name of the scheduler
     * @return Scheduler name
     */
    @Override
    public String getSchedulerName() {
        return schedulerName;
    }

    /**
     * Get the number of scheduled processes
     * @return Count of scheduled processes
     */
    @Override
    public int getScheduledProcessCount() {
        return scheduledProcesses.size();
    }

    /**
     * Get all scheduled processes
     * @return List of scheduled processes
     */
    public List<Process> getScheduledProcesses() {
        return new ArrayList<>(scheduledProcesses);
    }

    /**
     * Clear all scheduled processes
     */
    public void clearScheduledProcesses() {
        scheduledProcesses.clear();
        System.out.println("[SimpleScheduler] All scheduled processes cleared");
    }

    @Override
    public String toString() {
        return "SimpleScheduler{" +
                "schedulerName='" + schedulerName + '\'' +
                ", scheduledProcessCount=" + scheduledProcesses.size() +
                '}';
    }
}
