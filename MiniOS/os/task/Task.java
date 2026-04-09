package os.task;

/**
 * Abstract class representing a Task
 * Demonstrates abstraction - core OOP concept
 * All tasks must implement the execute method
 */
public abstract class Task {

    // Encapsulation: private variables with getters/setters
    private String taskName;
    private int taskId;
    private long creationTime;

    /**
     * Constructor to initialize task
     * @param taskName Name of the task
     * @param taskId Unique identifier for the task
     */
    public Task(String taskName, int taskId) {
        this.taskName = taskName;
        this.taskId = taskId;
        this.creationTime = System.currentTimeMillis();
    }

    /**
     * Abstract method that must be implemented by subclasses
     * Demonstrates abstraction
     */
    public abstract void execute();

    // Getters and Setters (Encapsulation)
    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskName='" + taskName + '\'' +
                ", taskId=" + taskId +
                ", creationTime=" + creationTime +
                '}';
    }
}
