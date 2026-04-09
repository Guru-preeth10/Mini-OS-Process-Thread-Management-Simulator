package os.task;

/**
 * CalculationTask - Another concrete implementation of Task
 * Demonstrates polymorphism: different task behavior
 * This task performs mathematical calculations
 */
public class CalculationTask extends Task {

    private int number1;
    private int number2;
    private String operation; // +, -, *, /

    /**
     * Constructor for CalculationTask
     * @param taskName Name of the task
     * @param taskId Unique identifier
     * @param number1 First number
     * @param number2 Second number
     * @param operation Mathematical operation (+, -, *, /)
     */
    public CalculationTask(String taskName, int taskId, int number1, int number2, String operation) {
        super(taskName, taskId);
        this.number1 = number1;
        this.number2 = number2;
        this.operation = operation;
    }

    /**
     * Execute method - performs calculation
     * Demonstrates polymorphic behavior
     */
    @Override
    public void execute() {
        try {
            String threadName = Thread.currentThread().getName();
            System.out.println("[" + threadName + "] Starting CalculationTask: " + this.getTaskName());

            // Simulate calculation time
            Thread.sleep(300);

            double result = performCalculation();

            String output = String.format("[%s] %s Result: %d %s %d = %.2f",
                threadName, this.getTaskName(), number1, operation, number2, result);
            System.out.println(output);

            // Simulate more work
            Thread.sleep(300);

            System.out.println("[" + threadName + "] CalculationTask '" +
                this.getTaskName() + "' completed!");

        } catch (InterruptedException e) {
            System.err.println("CalculationTask interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        } catch (ArithmeticException e) {
            System.err.println("Math error in CalculationTask: " + e.getMessage());
        }
    }

    /**
     * Helper method to perform the actual calculation
     * @return The result of the calculation
     */
    private double performCalculation() {
        switch (operation) {
            case "+":
                return number1 + number2;
            case "-":
                return number1 - number2;
            case "*":
                return number1 * number2;
            case "/":
                if (number2 == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                return (double) number1 / number2;
            default:
                throw new IllegalArgumentException("Unknown operation: " + operation);
        }
    }

    // Getters and Setters
    public int getNumber1() {
        return number1;
    }

    public void setNumber1(int number1) {
        this.number1 = number1;
    }

    public int getNumber2() {
        return number2;
    }

    public void setNumber2(int number2) {
        this.number2 = number2;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
