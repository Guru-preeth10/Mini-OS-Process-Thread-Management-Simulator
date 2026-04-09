package os.scheduler;

import os.process.Process;

/**
 * Scheduler Interface
 * Demonstrates abstraction through Interface
 * Follows the strategy pattern for scheduling
 */
public interface Scheduler {

    /**
     * Schedule a process for execution
     * @param process The process to schedule
     */
    void schedule(Process process);

    /**
     * Get the name of the scheduler
     * @return Scheduler name
     */
    String getSchedulerName();

    /**
     * Get the number of scheduled processes
     * @return Count of scheduled processes
     */
    int getScheduledProcessCount();
}
