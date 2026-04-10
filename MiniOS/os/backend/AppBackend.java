package os.backend;

import os.process.Process;
import os.scheduler.SimpleScheduler;
import os.thread.ThreadTask;
import os.task.PrintTask;
import os.task.CalculationTask;
import os.exception.InvalidProcessException;

import java.util.ArrayList;
import java.util.List;

/**
 * AppBackend - simple singleton backend to hold processes and scheduler
 */
public class AppBackend {

    private static AppBackend instance;

    private SimpleScheduler scheduler;
    private List<Process> processRepository;
    private int processIdCounter = 1;
    private int threadIdCounter = 1;
    private int taskIdCounter = 101;

    private AppBackend() {
        scheduler = new SimpleScheduler("Web API Scheduler");
        processRepository = new ArrayList<>();
    }

    public static synchronized AppBackend getInstance() {
        if (instance == null) {
            instance = new AppBackend();
        }
        return instance;
    }

    public synchronized Process createProcess(String name) {
        Process p = new Process(processIdCounter++, name);
        processRepository.add(p);
        scheduler.schedule(p);
        return p;
    }

    public synchronized boolean addPrintThread(int processId, String message, int iterations) throws InvalidProcessException {
        Process p = findProcess(processId);
        if (p == null) return false;
        PrintTask task = new PrintTask("PrintTask-" + taskIdCounter, taskIdCounter++, message, iterations);
        ThreadTask t = new ThreadTask(threadIdCounter++, task);
        p.addThreadTask(t);
        return true;
    }

    public synchronized boolean addCalculationThread(int processId, int num1, int num2, String op) throws InvalidProcessException {
        Process p = findProcess(processId);
        if (p == null) return false;
        CalculationTask task = new CalculationTask("CalcTask-" + taskIdCounter, taskIdCounter++, num1, num2, op);
        ThreadTask t = new ThreadTask(threadIdCounter++, task);
        p.addThreadTask(t);
        return true;
    }

    public synchronized boolean runProcess(int processId) throws InvalidProcessException {
        Process p = findProcess(processId);
        if (p == null) return false;
        p.startProcess();
        // run in background to avoid blocking server thread
        new Thread(() -> {
            try {
                p.waitForCompletion();
            } catch (InvalidProcessException e) {
                System.err.println("Error while running process: " + e.getMessage());
            }
        }).start();
        return true;
    }

    public synchronized List<Process> getProcesses() {
        return new ArrayList<>(processRepository);
    }

    public synchronized SimpleScheduler getScheduler() {
        return scheduler;
    }

    private Process findProcess(int id) {
        for (Process p : processRepository) {
            if (p.getProcessId() == id) return p;
        }
        return null;
    }
}
