package os.process;

import os.thread.ThreadTask;
import os.exception.InvalidProcessException;
import java.util.ArrayList;
import java.util.List;

/**
 * Process class - Represents an OS process containing multiple threads
 * Demonstrates:
 * - Encapsulation (private variables with getters/setters)
 * - Collections (ArrayList of ThreadTask)
 * - Exception handling (InvalidProcessException)
 */
public class Process {

    private int processId;
    private String processName;
    private long creationTime;
    private List<ThreadTask> threads; // Collections - ArrayList
    private ProcessState state;
    private long startTime;
    private long endTime;

    /**
     * Enum for Process States
     */
    public enum ProcessState {
        CREATED, RUNNING, COMPLETED, TERMINATED
    }

    /**
     * Constructor for Process
     * @param processId Unique process identifier
     * @param processName Name of the process
     */
    public Process(int processId, String processName) {
        this.processId = processId;
        this.processName = processName;
        this.creationTime = System.currentTimeMillis();
        this.threads = new ArrayList<>(); // Using ArrayList from Collections
        this.state = ProcessState.CREATED;
    }

    /**
     * Add a thread task to the process
     * @param threadTask Thread to be added
     * @throws InvalidProcessException if process is not in CREATED or RUNNING state
     */
    public void addThreadTask(ThreadTask threadTask) throws InvalidProcessException {
        if (state == ProcessState.TERMINATED) {
            throw new InvalidProcessException("Cannot add thread to terminated process: " + processName);
        }
        threads.add(threadTask);
        System.out.println("Thread added to process '" + processName + "': " + threadTask.getName());
    }

    /**
     * Start all threads in the process
     * @throws InvalidProcessException if no threads have been added
     */
    public void startProcess() throws InvalidProcessException {
        if (threads.isEmpty()) {
            throw new InvalidProcessException("No threads available to start in process: " + processName);
        }

        state = ProcessState.RUNNING;
        startTime = System.currentTimeMillis();
        System.out.println("\n========== Starting Process: " + processName + " ==========");
        System.out.println("Process ID: " + processId + ", Total Threads: " + threads.size());
        System.out.println("========================================\n");

        // Start all threads (concurrent execution)
        for (ThreadTask thread : threads) {
            thread.start();
        }
    }

    /**
     * Wait for all threads to complete
     * @throws InvalidProcessException if process hasn't started
     */
    public void waitForCompletion() throws InvalidProcessException {
        if (state != ProcessState.RUNNING) {
            throw new InvalidProcessException("Process not in running state: " + processName);
        }

        try {
            for (ThreadTask thread : threads) {
                thread.join(); // Wait for thread to complete
            }
            endTime = System.currentTimeMillis();
            state = ProcessState.COMPLETED;
            System.out.println("\n========== Process Completed: " + processName + " ==========");
            System.out.println("Total execution time: " + (endTime - startTime) + "ms");
            printProcessStatistics();
            System.out.println("========================================\n");

        } catch (InterruptedException e) {
            System.err.println("Process interrupted: " + e.getMessage());
            state = ProcessState.TERMINATED;
        }
    }

    /**
     * Print statistics about process execution
     */
    private void printProcessStatistics() {
        System.out.println("\nProcess Statistics:");
        System.out.println("  Total Threads: " + threads.size());
        System.out.println("  Completed Threads: " + getCompletedThreadCount());

        long totalTime = 0;
        for (ThreadTask thread : threads) {
            if (thread.isCompleted()) {
                totalTime += thread.getExecutionTime();
            }
        }
        System.out.println("  Total Thread Execution Time: " + totalTime + "ms");
    }

    /**
     * Get count of completed threads
     * @return Number of completed threads
     */
    public int getCompletedThreadCount() {
        int count = 0;
        for (ThreadTask thread : threads) {
            if (thread.isCompleted()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Terminate the process
     */
    public void terminate() {
        state = ProcessState.TERMINATED;
        System.out.println("Process terminated: " + processName);
    }

    // Getters and Setters (Encapsulation)
    public int getProcessId() {
        return processId;
    }

    public void setProcessId(int processId) {
        this.processId = processId;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public List<ThreadTask> getThreads() {
        return new ArrayList<>(threads); // Return copy for immutability
    }

    public ProcessState getState() {
        return state;
    }

    public void setState(ProcessState state) {
        this.state = state;
    }

    public int getThreadCount() {
        return threads.size();
    }

    @Override
    public String toString() {
        return "Process{" +
                "processId=" + processId +
                ", processName='" + processName + '\'' +
                ", state=" + state +
                ", threadCount=" + threads.size() +
                '}';
    }
}
