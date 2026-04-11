package os.backend;

import os.process.Process;
import os.scheduler.SimpleScheduler;
import os.thread.ThreadTask;
import os.task.PrintTask;
import os.task.CalculationTask;
import os.exception.InvalidProcessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

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
    private List<Map<String, Object>> executionLog = new ArrayList<>();

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

    public synchronized Process createProcess(String name, String priorityStr) {
        Process.Priority pr = parsePriority(priorityStr);
        Process p = new Process(processIdCounter++, name, pr);
        processRepository.add(p);
        scheduler.schedule(p);
        return p;
    }

    public synchronized boolean addPrintThread(int processId, String message, int iterations, String priorityStr) throws InvalidProcessException {
        Process p = findProcess(processId);
        if (p == null) return false;
        PrintTask task = new PrintTask("PrintTask-" + taskIdCounter, taskIdCounter++, message, iterations);
        ThreadTask t = new ThreadTask(threadIdCounter++, task, parsePriority(priorityStr));
        p.addThreadTask(t);
        return true;
    }

    public synchronized boolean addCalculationThread(int processId, int num1, int num2, String op, String priorityStr) throws InvalidProcessException {
        Process p = findProcess(processId);
        if (p == null) return false;
        CalculationTask task = new CalculationTask("CalcTask-" + taskIdCounter, taskIdCounter++, num1, num2, op);
        ThreadTask t = new ThreadTask(threadIdCounter++, task, parsePriority(priorityStr));
        p.addThreadTask(t);
        return true;
    }

    public synchronized boolean runProcess(int processId) throws InvalidProcessException {
        Process p = findProcess(processId);
        if (p == null) return false;
        // run synchronously in background thread so caller can trigger
        new Thread(() -> {
            try {
                // record start
                Map<String,Object> startEvt = new HashMap<>();
                startEvt.put("processId", p.getProcessId());
                startEvt.put("name", p.getProcessName());
                long start = System.currentTimeMillis();
                startEvt.put("start", start);
                synchronized (executionLog) { executionLog.add(startEvt); }

                p.startProcess();
                p.waitForCompletion();

                long end = System.currentTimeMillis();
                Map<String,Object> endEvt = new HashMap<>();
                endEvt.put("processId", p.getProcessId());
                endEvt.put("end", end);
                endEvt.put("duration", end - start);
                synchronized (executionLog) { executionLog.add(endEvt); }
            } catch (InvalidProcessException e) {
                System.err.println("Error while running process: " + e.getMessage());
            }
        }).start();
        return true;
    }

    public synchronized List<Process> getProcesses() {
        return new ArrayList<>(processRepository);
    }

    public synchronized void runScheduler() {
        // Execute scheduled processes in priority order and record timeline
        List<Process> list = scheduler.getScheduledProcesses();
        for (Process p : list) {
            try {
                // start
                Map<String,Object> startEvt = new HashMap<>();
                startEvt.put("processId", p.getProcessId());
                startEvt.put("name", p.getProcessName());
                long start = System.currentTimeMillis();
                startEvt.put("start", start);
                synchronized (executionLog) { executionLog.add(startEvt); }

                p.startProcess();
                p.waitForCompletion();

                long end = System.currentTimeMillis();
                Map<String,Object> endEvt = new HashMap<>();
                endEvt.put("processId", p.getProcessId());
                endEvt.put("end", end);
                endEvt.put("duration", end - start);
                synchronized (executionLog) { executionLog.add(endEvt); }
            } catch (InvalidProcessException e) {
                System.err.println("Error while running process: " + e.getMessage());
            }
        }
        // clear scheduled processes after execution
        scheduler.clearScheduledProcesses();
    }

    public synchronized List<Map<String,Object>> getExecutionLog() {
        synchronized (executionLog) {
            return new ArrayList<>(executionLog);
        }
    }

    private Process.Priority parsePriority(String s) {
        if (s == null) return Process.Priority.MEDIUM;
        try {
            return Process.Priority.valueOf(s.trim().toUpperCase());
        } catch (Exception e) {
            return Process.Priority.MEDIUM;
        }
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
