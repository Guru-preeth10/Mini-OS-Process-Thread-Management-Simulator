package os.task;

/**
 * PrintTask - A concrete implementation of Task
 * Demonstrates polymorphism: different task behavior
 * This task simply prints a message multiple times
 */
public class PrintTask extends Task {

    private String message;
    private int iterations;

    /**
     * Constructor for PrintTask
     * @param taskName Name of the task
     * @param taskId Unique identifier
     * @param message Message to print
     * @param iterations Number of times to print
     */
    public PrintTask(String taskName, int taskId, String message, int iterations) {
        super(taskName, taskId);
        this.message = message;
        this.iterations = iterations;
    }

    /**
     * Execute method - prints message multiple times with delays
     * Demonstrates polymorphic behavior
     */
    @Override
    public void execute() {
        try {
            for (int i = 0; i < iterations; i++) {
                String threadName = Thread.currentThread().getName();
                String output = String.format("[%s] %s - Iteration %d: %s",
                    threadName, this.getTaskName(), (i + 1), message);
                System.out.println(output);

                // Simulate work with sleep
                Thread.sleep(500);
            }
            System.out.println("[" + Thread.currentThread().getName() +
                "] PrintTask '" + this.getTaskName() + "' completed!");
        } catch (InterruptedException e) {
            System.err.println("PrintTask interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }
}
