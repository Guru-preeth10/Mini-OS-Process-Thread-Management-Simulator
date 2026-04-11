package os.thread;

import os.task.Task;


/**
 * ThreadTask - Represents a thread that executes a Task
 * Demonstrates inheritance from Thread class
 * Encapsulation of thread-related properties
 */
public class ThreadTask extends Thread {

    private Task task;
    private int threadId;
    private long startTime;
    private long endTime;
    private boolean isCompleted;
    private os.process.Process.Priority priority = os.process.Process.Priority.MEDIUM;

    /**
     * Constructor for ThreadTask
     * @param threadId Unique identifier for the thread
     * @param task The task to be executed by this thread
     */
    public ThreadTask(int threadId, Task task) {
        this.threadId = threadId;
        this.task = task;
        this.isCompleted = false;
        this.setName("Thread-" + threadId);
    }

    public ThreadTask(int threadId, Task task, os.process.Process.Priority priority) {
        this(threadId, task);
        if (priority != null) this.priority = priority;
    }

    /**
     * Run method - executed when thread starts
     * Demonstrates polymorphism by overriding Thread's run method
     */
    @Override
    public void run() {
        try {
            startTime = System.currentTimeMillis();
            System.out.println("[" + this.getName() + "] Started with task: " + task.getTaskName());

            // Execute the task
            task.execute();

            endTime = System.currentTimeMillis();
            isCompleted = true;

            long duration = endTime - startTime;
            System.out.println("[" + this.getName() + "] Finished! Duration: " + duration + "ms");

        } catch (Exception e) {
            System.err.println("[" + this.getName() + "] Error executing task: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Getters and Setters (Encapsulation)
    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public os.process.Process.Priority getThreadPriority() {
        return priority;
    }

    public void setThreadPriority(os.process.Process.Priority priority) {
        if (priority != null) this.priority = priority;
    }

    public long getExecutionTime() {
        if (isCompleted) {
            return endTime - startTime;
        }
        return -1; // Not yet completed
    }

    @Override
    public String toString() {
        return "ThreadTask{" +
                "threadId=" + threadId +
                ", taskName=" + task.getTaskName() +
                ", isCompleted=" + isCompleted +
                ", executionTime=" + getExecutionTime() + "ms" +
                '}';
    }
}
