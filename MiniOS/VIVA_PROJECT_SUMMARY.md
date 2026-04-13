# MiniOS Viva Project Summary

This file is a viva-focused summary of the current project source code. It is
written for quick revision before explaining the Java OOP concepts in front of
your teacher.

## 1. One-Line Project Introduction

MiniOS is a Java Object-Oriented Programming project that simulates a small
operating-system style process and thread management system. It lets the user
create processes, add task-based threads, run them concurrently, schedule them
by priority, log activity to a file, and optionally control the same backend
from a web UI.

## 2. 30-Second Viva Explanation

"My project is MiniOS, a process and thread management simulator in Java. It
uses OOP concepts like abstraction, inheritance, polymorphism, encapsulation,
interfaces, exception handling, collections, multithreading, and the Singleton
pattern. A Process object contains multiple ThreadTask objects. Each ThreadTask
extends Java Thread and executes a Task. Task is an abstract class, and concrete
tasks like PrintTask and CalculationTask override the execute method. A
SimpleScheduler implements the Scheduler interface and schedules processes by
priority using a PriorityQueue. The project also has a Logger singleton for
file handling and a WebServerMain API for the web dashboard."

## 3. Main Components

| File | Role |
| --- | --- |
| `Main.java` | Console menu entry point for creating processes, adding threads, running processes, viewing scheduler info, and exiting. |
| `WebServerMain.java` | REST API server using Java's built-in `HttpServer`; connects the web UI to Java backend logic. |
| `os/backend/AppBackend.java` | Singleton backend used by web server; stores processes, scheduler, counters, and execution log. |
| `os/process/Process.java` | Represents one OS-like process; owns multiple `ThreadTask` objects and process state. |
| `os/thread/ThreadTask.java` | Extends `Thread`; runs a `Task` object and records execution timing. |
| `os/task/Task.java` | Abstract base class for all task types. |
| `os/task/PrintTask.java` | Concrete task that prints a message multiple times. |
| `os/task/CalculationTask.java` | Concrete task that performs `+`, `-`, `*`, or `/` calculations. |
| `os/scheduler/Scheduler.java` | Interface that defines the scheduler contract. |
| `os/scheduler/SimpleScheduler.java` | Concrete scheduler using `PriorityQueue<Process>`. |
| `os/utils/Logger.java` | Singleton file logger using `BufferedWriter` and synchronized methods. |
| `os/exception/InvalidProcessException.java` | Custom checked exception for invalid process operations. |
| `ui/index.html`, `ui/css/styles.css`, `ui/js/app.js` | Web dashboard frontend for creating/running processes and viewing scheduler activity. |

## 4. Current Project Size

The Java and UI source code currently has about 4,088 source lines:

| Area | Lines |
| --- | ---: |
| Java backend and console code | 1,741 |
| Web UI HTML/CSS/JS | 2,347 |
| Total | 4,088 |

Important note: `WebServerMain.java` uses Java local variable type inference
with `var`, so the web server source needs JDK 10 or above. If your lab uses
Java 8, replace those `var` local variables with explicit types.

## 5. How To Compile And Run

From the project root:

```bash
cd MiniOS
javac -d out @sources.txt
java -cp out Main
```

To run the web backend:

```bash
cd MiniOS
javac -d out @sources.txt
java -cp out WebServerMain
```

Then open `MiniOS/ui/index.html` in a browser. The UI calls:

```text
http://localhost:8080/api
```

## 6. Basic Execution Flow

Console flow:

1. `Main.main()` initializes `Logger`, `Scanner`, `SimpleScheduler`, and `processRepository`.
2. User chooses menu option 1 to create a process.
3. `new Process(processIdCounter++, processName)` creates a process object.
4. Process is added to `processRepository`.
5. Process is scheduled using `scheduler.schedule(newProcess)`.
6. User chooses menu option 2 to add a thread.
7. Program creates either `PrintTask` or `CalculationTask`.
8. Program wraps the task inside `new ThreadTask(threadIdCounter++, task)`.
9. Process stores the thread using `selectedProcess.addThreadTask(newThread)`.
10. User chooses menu option 3 to run a process.
11. `selectedProcess.startProcess()` starts all Java threads.
12. `selectedProcess.waitForCompletion()` calls `join()` on each thread.
13. Process state changes from `CREATED` to `RUNNING` to `COMPLETED`.

Web flow:

1. `ui/js/app.js` sends `fetch()` requests to `http://localhost:8080/api`.
2. `WebServerMain` receives requests through handler classes like `CreateProcessHandler`.
3. Handlers call `AppBackend.getInstance()`.
4. `AppBackend` creates processes, adds threads, runs processes, or returns JSON data.
5. UI displays process status, scheduler data, and timeline output.

## 7. OOP Concepts Implemented

### 7.1 Classes And Objects

Main classes:

```java
Process newProcess = new Process(processIdCounter++, processName);
Task task = new PrintTask("PrintTask-" + taskIdCounter, taskIdCounter++, message, iterations);
ThreadTask newThread = new ThreadTask(threadIdCounter++, task);
```

Why this matters:

- `Process`, `Task`, and `ThreadTask` are classes.
- `newProcess`, `task`, and `newThread` are objects.
- Objects interact with each other to form the simulator.

### 7.2 Encapsulation

Fields are private and accessed through public methods.

```java
public class Task {
    private String taskName;
    private int taskId;
    private long creationTime;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
```

Viva explanation:

"Encapsulation means hiding internal data and exposing controlled access through
methods. In this project, fields like `taskName`, `processId`, `threads`, and
`isCompleted` are private, and access is given through getters and setters."

### 7.3 Abstraction Using Abstract Class

`Task` gives a common structure but leaves the actual work to child classes.

```java
public abstract class Task {
    private String taskName;
    private int taskId;
    private long creationTime;

    public Task(String taskName, int taskId) {
        this.taskName = taskName;
        this.taskId = taskId;
        this.creationTime = System.currentTimeMillis();
    }

    public abstract void execute();
}
```

Viva explanation:

"Task is abstract because every task has common data like name, id, and creation
time, but the actual execution is different for each task type. So `execute()`
is abstract."

### 7.4 Inheritance

`PrintTask` and `CalculationTask` inherit from `Task`.

```java
public class PrintTask extends Task {
    public PrintTask(String taskName, int taskId, String message, int iterations) {
        super(taskName, taskId);
        this.message = message;
        this.iterations = iterations;
    }

    @Override
    public void execute() {
        // print message logic
    }
}
```

```java
public class CalculationTask extends Task {
    public CalculationTask(String taskName, int taskId, int number1, int number2, String operation) {
        super(taskName, taskId);
        this.number1 = number1;
        this.number2 = number2;
        this.operation = operation;
    }

    @Override
    public void execute() {
        // calculation logic
    }
}
```

Other inheritance examples:

```java
public class ThreadTask extends Thread
public class InvalidProcessException extends Exception
```

Viva explanation:

"Inheritance is used when a class reuses and specializes another class.
PrintTask and CalculationTask reuse the common Task properties, while ThreadTask
inherits Java thread behavior from Thread."

### 7.5 Polymorphism

The most important polymorphism is here:

```java
Task task;

if (taskChoice == 1) {
    task = new PrintTask("PrintTask-" + taskIdCounter, taskIdCounter++, message, iterations);
} else {
    task = new CalculationTask("CalcTask-" + taskIdCounter, taskIdCounter++, num1, num2, operation);
}

ThreadTask newThread = new ThreadTask(threadIdCounter++, task);
```

Inside `ThreadTask.run()`:

```java
@Override
public void run() {
    startTime = System.currentTimeMillis();
    System.out.println("[" + this.getName() + "] Started with task: " + task.getTaskName());

    task.execute();

    endTime = System.currentTimeMillis();
    isCompleted = true;
}
```

Why it is polymorphism:

- The variable type is `Task`.
- At runtime, the object can be `PrintTask` or `CalculationTask`.
- `task.execute()` calls the correct overridden method automatically.

Viva answer:

"Polymorphism means one reference can behave differently based on the actual
object. Here, a `Task` reference can hold either a `PrintTask` or a
`CalculationTask`. When `execute()` is called, Java uses dynamic method
dispatch and runs the correct child class method."

### 7.6 Method Overriding

Examples:

```java
@Override
public void execute() {
    // PrintTask implementation
}
```

```java
@Override
public void execute() {
    // CalculationTask implementation
}
```

```java
@Override
public void run() {
    task.execute();
}
```

```java
@Override
public String toString() {
    return "Process{" +
            "processId=" + processId +
            ", processName='" + processName + '\'' +
            ", state=" + state +
            ", threadCount=" + threads.size() +
            '}';
}
```

Viva answer:

"Overriding happens when a child class provides its own implementation of a
method already declared in the parent class or interface. In this project,
`PrintTask` and `CalculationTask` override `execute()`, and `ThreadTask`
overrides `run()` from Thread."

### 7.7 Constructor Overloading

`Process` has two constructors:

```java
public Process(int processId, String processName) {
    this.processId = processId;
    this.processName = processName;
    this.creationTime = System.currentTimeMillis();
    this.threads = new ArrayList<>();
    this.state = ProcessState.CREATED;
}

public Process(int processId, String processName, Priority priority) {
    this(processId, processName);
    if (priority != null) this.priority = priority;
}
```

`ThreadTask` also has constructor overloading:

```java
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
```

Viva answer:

"Constructor overloading means having multiple constructors with different
parameters. It is used here to support process/thread creation with or without
priority."

### 7.8 Interface

`Scheduler` is an interface:

```java
public interface Scheduler {
    void schedule(Process process);
    String getSchedulerName();
    int getScheduledProcessCount();
}
```

`SimpleScheduler` implements it:

```java
public class SimpleScheduler implements Scheduler {
    @Override
    public void schedule(Process process) {
        if (process == null) {
            System.err.println("Cannot schedule null process");
            return;
        }
        scheduledProcesses.add(process);
    }
}
```

Viva answer:

"An interface defines a contract. Scheduler says what a scheduler must do, and
SimpleScheduler provides the actual implementation. This also allows future
schedulers like RoundRobinScheduler or PriorityScheduler."

### 7.9 Composition

`Process` contains many `ThreadTask` objects:

```java
private List<ThreadTask> threads;
```

`ThreadTask` contains one `Task`:

```java
private Task task;
```

`AppBackend` contains scheduler and process repository:

```java
private SimpleScheduler scheduler;
private List<Process> processRepository;
```

Viva answer:

"Composition means building a class using objects of other classes. A Process
has threads, a ThreadTask has a Task, and the backend has a scheduler and list
of processes."

### 7.10 Enum

Process state:

```java
public enum ProcessState {
    CREATED, RUNNING, COMPLETED, TERMINATED
}
```

Priority:

```java
public static enum Priority { HIGH, MEDIUM, LOW }
```

Viva answer:

"Enums are used for fixed sets of constants. ProcessState prevents invalid
state strings, and Priority gives a fixed priority order."

### 7.11 Custom Exception

```java
public class InvalidProcessException extends Exception {
    public InvalidProcessException(String message) {
        super(message);
    }

    public InvalidProcessException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

Thrown in `Process`:

```java
if (threads.isEmpty()) {
    throw new InvalidProcessException("No threads available to start in process: " + processName);
}
```

Viva answer:

"InvalidProcessException is a custom checked exception. It is used when an
invalid operation is attempted, like running a process with no threads or adding
a thread to a terminated process."

### 7.12 Collections

`ArrayList` for dynamic storage:

```java
private List<ThreadTask> threads;
this.threads = new ArrayList<>();
```

Returning a copy for safer encapsulation:

```java
public List<ThreadTask> getThreads() {
    return new ArrayList<>(threads);
}
```

`PriorityQueue` for scheduling:

```java
this.scheduledProcesses = new PriorityQueue<>(
    Comparator.comparingInt(p -> p.getPriority().ordinal())
);
```

Viva answer:

"ArrayList is used because the number of processes and threads is dynamic.
PriorityQueue is used in the scheduler so HIGH priority processes are executed
before MEDIUM and LOW priority processes."

### 7.13 Multithreading

`ThreadTask` extends Java Thread:

```java
public class ThreadTask extends Thread {
    private Task task;

    @Override
    public void run() {
        task.execute();
    }
}
```

`Process` starts all threads:

```java
for (ThreadTask thread : threads) {
    thread.start();
}
```

`Process` waits for completion:

```java
for (ThreadTask thread : threads) {
    thread.join();
}
```

Viva answer:

"The project uses real Java multithreading. Calling `start()` creates a new
thread and internally calls `run()`. Calling `join()` makes the process wait
until all thread tasks finish."

### 7.14 Synchronization

`Logger` uses synchronized methods:

```java
public static synchronized Logger getInstance() {
    if (instance == null) {
        instance = new Logger();
    }
    return instance;
}

public synchronized void writeLog(String message) {
    // write to file safely
}
```

`AppBackend` also uses synchronized methods for shared web-server state:

```java
public synchronized Process createProcess(String name, String priorityStr) {
    Process.Priority pr = parsePriority(priorityStr);
    Process p = new Process(processIdCounter++, name, pr);
    processRepository.add(p);
    scheduler.schedule(p);
    return p;
}
```

Viva answer:

"Synchronization is used to prevent multiple threads from modifying shared data
at the same time. Logger uses it for safe file writing, and AppBackend uses it
for shared process and scheduler state."

### 7.15 Singleton Pattern

`Logger`:

```java
private static Logger instance;

private Logger() {
    // initialize writer
}

public static synchronized Logger getInstance() {
    if (instance == null) {
        instance = new Logger();
    }
    return instance;
}
```

`AppBackend`:

```java
private static AppBackend instance;

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
```

Viva answer:

"Singleton ensures only one object of a class is created. Logger is singleton
because one shared log file writer is enough. AppBackend is singleton because
the web server should use one shared process repository and scheduler."

### 7.16 File Handling

```java
writer = new BufferedWriter(new FileWriter(LOG_FILE, true));
dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
```

```java
public synchronized void writeLog(String message) {
    String timestamp = dateFormat.format(new Date());
    String logEntry = "[" + timestamp + "] " + message;

    if (writer != null) {
        writer.write(logEntry);
        writer.newLine();
        writer.flush();
    }
}
```

Viva answer:

"The Logger class demonstrates file handling. It uses FileWriter in append mode,
BufferedWriter for efficient writing, and SimpleDateFormat for timestamps."

## 8. Important Project Logic

### 8.1 Process State Management

```java
state = ProcessState.RUNNING;
startTime = System.currentTimeMillis();
```

After all threads complete:

```java
endTime = System.currentTimeMillis();
state = ProcessState.COMPLETED;
```

If interrupted:

```java
state = ProcessState.TERMINATED;
```

### 8.2 Thread Priority Sorting Inside A Process

```java
threads.sort((a, b) -> {
    int pa = a.getThreadPriority() == null ? Priority.MEDIUM.ordinal() : a.getThreadPriority().ordinal();
    int pb = b.getThreadPriority() == null ? Priority.MEDIUM.ordinal() : b.getThreadPriority().ordinal();
    return Integer.compare(pa, pb);
});
```

Explanation:

- `HIGH.ordinal()` is 0.
- `MEDIUM.ordinal()` is 1.
- `LOW.ordinal()` is 2.
- Sorting ascending means HIGH starts first.

### 8.3 Scheduler Priority Queue

```java
this.scheduledProcesses = new PriorityQueue<>(
    Comparator.comparingInt(p -> p.getPriority().ordinal())
);
```

Explanation:

"The scheduler stores processes in a PriorityQueue. Since HIGH has ordinal 0,
HIGH priority processes come out before MEDIUM and LOW."

### 8.4 Calculation Error Handling

```java
case "/":
    if (number2 == 0) {
        throw new ArithmeticException("Division by zero");
    }
    return (double) number1 / number2;
```

Explanation:

"Division by zero is handled using ArithmeticException, so the program can
report a math error instead of silently producing invalid output."

## 9. Web API Summary

`WebServerMain` exposes these routes:

| Endpoint | Method | Purpose |
| --- | --- | --- |
| `/api/createProcess` | POST | Create process with name and priority. |
| `/api/addThread` | POST | Add PrintTask or CalculationTask thread to a process. |
| `/api/runProcess` | POST | Run a selected process. |
| `/api/runScheduler` | POST | Run all scheduled processes in background. |
| `/api/getProcesses` | GET | Return process list as JSON. |
| `/api/getSchedulerInfo` | GET | Return scheduler name and scheduled count. |
| `/api/getExecutionLog` | GET | Return process execution timeline data. |

Important viva point:

"The web server uses Java's built-in `com.sun.net.httpserver.HttpServer`, so no
external framework like Spring Boot is required."

Important limitation:

"The JSON parsing in `WebServerMain` is simple manual parsing using helper
methods like `extractString()` and `extractInt()`. It is enough for this project
demo, but in production we would use a JSON library like Jackson or Gson."

## 10. What Makes This An OOP Project

This project is not just a procedural menu program. It has a clear object model:

- `Task` is the abstract idea of work.
- `PrintTask` and `CalculationTask` are specialized tasks.
- `ThreadTask` is the executable unit that runs a task.
- `Process` groups multiple threads and manages their lifecycle.
- `Scheduler` defines scheduling behavior.
- `SimpleScheduler` provides priority-based scheduling.
- `Logger` handles logging separately.
- `AppBackend` separates backend state from the web server.
- `InvalidProcessException` separates invalid process errors from generic errors.

This separation is important in viva because it shows class responsibility,
modularity, and real OOP design.

## 11. Limitations You Can Mention Honestly

These are not failures; they are good answers if your teacher asks "future
scope" or "limitations":

1. This is a simulator, not a real operating system scheduler.
2. Java thread priority is not changed; the project uses its own custom priority enum.
3. The console version can start a process once; trying to restart the same Java Thread object after completion would be invalid in Java.
4. `WebServerMain` uses simple string-based JSON parsing; a real project should use Jackson or Gson.
5. `ThreadTask extends Thread`; for larger applications, implementing `Runnable` and using an `ExecutorService` is often cleaner.
6. Some UI execution output is simulated client-side for display, while the backend also runs the real process logic.
7. The scheduler uses priority order, not advanced algorithms like round-robin or shortest-job-first.

## 12. Viva Questions And Short Answers

### Project-Level Questions

1. What is your project?

Answer: MiniOS is a Java OOP project that simulates process and thread
management. It lets users create processes, add task-based threads, run them
concurrently, schedule them by priority, and log execution.

2. Why did you choose this project?

Answer: It demonstrates both OOP and Java core concepts in one project:
abstraction, inheritance, polymorphism, encapsulation, interfaces, collections,
custom exceptions, file handling, multithreading, and synchronization.

3. What is the main class?

Answer: `Main.java` is the console entry point. `WebServerMain.java` is the web
API entry point.

4. What is the real-world idea behind the project?

Answer: In an operating system, processes contain threads, and threads execute
work. This project simulates that concept in Java.

5. How is your project modular?

Answer: Each responsibility is separated into a class: tasks in `os.task`,
threads in `os.thread`, process logic in `os.process`, scheduling in
`os.scheduler`, logging in `os.utils`, and exceptions in `os.exception`.

### OOP Questions

6. Where is encapsulation used?

Answer: In classes like `Task`, `Process`, `ThreadTask`, and `Logger`, fields
are private and accessed through getters/setters or controlled public methods.

7. Where is abstraction used?

Answer: In abstract class `Task` with abstract method `execute()`, and in
interface `Scheduler`.

8. Why is `Task` abstract?

Answer: Because it defines common task data but does not know the exact task
behavior. Subclasses like `PrintTask` and `CalculationTask` provide the behavior.

9. Where is inheritance used?

Answer: `PrintTask extends Task`, `CalculationTask extends Task`, `ThreadTask
extends Thread`, and `InvalidProcessException extends Exception`.

10. Where is polymorphism used?

Answer: A `Task` reference can store either a `PrintTask` or `CalculationTask`.
When `task.execute()` is called in `ThreadTask`, Java runs the correct overridden
method at runtime.

11. What is method overriding in your project?

Answer: `PrintTask` and `CalculationTask` override `execute()`. `ThreadTask`
overrides `run()`. Several classes also override `toString()`.

12. What is constructor overloading in your project?

Answer: `Process` has one constructor with process id/name and another with
id/name/priority. `ThreadTask` also has constructors with and without priority.

13. Why did you use an interface for Scheduler?

Answer: It defines a contract for scheduling. `SimpleScheduler` implements it,
and future classes can implement different scheduling algorithms without
changing code that depends on the interface.

14. Difference between abstract class and interface?

Answer: An abstract class can have fields, constructors, and implemented
methods along with abstract methods. An interface mainly defines a contract.
Here, `Task` is abstract because tasks share data, while `Scheduler` is an
interface because it defines scheduling behavior.

15. What is composition in your project?

Answer: `Process` has a list of `ThreadTask` objects, `ThreadTask` has a `Task`,
and `AppBackend` has a scheduler and process repository.

### Java Multithreading Questions

16. Where is multithreading used?

Answer: `ThreadTask` extends `Thread`. Each process starts all its threads with
`thread.start()`.

17. What is the difference between `start()` and `run()`?

Answer: `start()` creates a new thread and then calls `run()` internally.
Calling `run()` directly behaves like a normal method call and does not create a
new thread.

18. Why do you use `join()`?

Answer: `join()` makes the process wait until each thread completes, so process
statistics are printed only after all threads finish.

19. What does `Thread.sleep()` do?

Answer: It pauses the current thread for a specified time. In this project it
simulates work delay in `PrintTask` and `CalculationTask`.

20. Is your priority enum the same as Java thread priority?

Answer: No. The project uses a custom `Priority` enum to decide scheduling
order. It does not call Java's `Thread.setPriority()`.

21. Can the same `Thread` object be started twice?

Answer: No. In Java, once a Thread has completed, the same Thread object cannot
be started again. A new Thread object must be created.

22. Why are some methods synchronized?

Answer: To protect shared state. `Logger` synchronizes file writing, and
`AppBackend` synchronizes process repository and scheduler operations.

### Exception Handling Questions

23. What is your custom exception?

Answer: `InvalidProcessException`, which extends `Exception`.

24. Is `InvalidProcessException` checked or unchecked?

Answer: It is checked because it extends `Exception`, not `RuntimeException`.

25. When is `InvalidProcessException` thrown?

Answer: When starting a process without threads, waiting for a process that is
not running, or adding a thread to a terminated process.

26. Where do you handle division by zero?

Answer: In `CalculationTask.performCalculation()`, division by zero throws
`ArithmeticException`, and `execute()` catches it.

27. Why catch `InterruptedException`?

Answer: `Thread.sleep()` and `Thread.join()` can throw `InterruptedException`,
so it must be handled to avoid abrupt program failure.

### Collections And Data Structure Questions

28. Which collections are used?

Answer: `ArrayList`, `List`, `PriorityQueue`, `Map`, and `HashMap`.

29. Why use `ArrayList`?

Answer: Processes and threads are dynamic, so ArrayList allows adding items at
runtime and iterating through them easily.

30. Why use `PriorityQueue` in scheduler?

Answer: It automatically orders scheduled processes according to priority, so
HIGH priority runs before MEDIUM and LOW.

31. Why return `new ArrayList<>(threads)` from `getThreads()`?

Answer: It returns a copy, so outside code cannot directly modify the internal
thread list of a process.

32. What is the use of enum here?

Answer: Enums define fixed constants for process state and priority, preventing
invalid string values.

### Design Pattern And File Handling Questions

33. Which design pattern is used?

Answer: Singleton pattern in `Logger` and `AppBackend`.

34. Why is Logger singleton?

Answer: Because one shared logger object is enough to write to one log file,
and it avoids multiple file writers fighting for the same file.

35. How does file handling work?

Answer: `Logger` uses `FileWriter` in append mode, wraps it in `BufferedWriter`,
adds timestamps using `SimpleDateFormat`, and writes messages to `log.txt`.

36. What does append mode mean?

Answer: `new FileWriter(LOG_FILE, true)` appends new logs to the existing file
instead of overwriting it.

### Web/API Questions

37. What is `WebServerMain`?

Answer: It is the Java REST API server for the web dashboard. It uses built-in
`HttpServer` and handler classes for API routes.

38. What is AppBackend?

Answer: It is a singleton backend that stores processes, scheduler, id counters,
and execution log for the web API.

39. How does the frontend communicate with Java?

Answer: The UI JavaScript uses `fetch()` calls to endpoints like
`/api/createProcess`, `/api/addThread`, and `/api/runProcess`.

40. What is CORS and where is it handled?

Answer: CORS allows the browser UI to call the Java backend from another origin.
`WebServerMain.writeJson()` adds headers like `Access-Control-Allow-Origin: *`.

### Improvement Questions

41. What future improvements can you add?

Answer: Add more scheduling algorithms like round-robin, use `ExecutorService`,
use a JSON library, add database persistence, add authentication to the web API,
and add unit tests.

42. Why not use Spring Boot?

Answer: This is an OOP mini project, so using Java's built-in `HttpServer` keeps
the project lightweight and focused on core Java concepts.

43. What is one weakness of your current code?

Answer: The web server uses simple manual JSON parsing, which is not robust for
complex JSON. A real project should use a library like Jackson or Gson.

44. What is one strong point of your project?

Answer: It demonstrates many core Java concepts together: OOP, threads,
collections, exceptions, file handling, synchronization, interface-based design,
and a web API.

## 13. Best Snippets To Memorize

### Abstract Class

```java
public abstract class Task {
    public abstract void execute();
}
```

### Inheritance And Polymorphism

```java
public class PrintTask extends Task {
    @Override
    public void execute() {
        // print behavior
    }
}
```

```java
Task task = new PrintTask(...);
task.execute();
```

### Threading

```java
public class ThreadTask extends Thread {
    @Override
    public void run() {
        task.execute();
    }
}
```

```java
thread.start();
thread.join();
```

### Interface

```java
public interface Scheduler {
    void schedule(Process process);
}
```

```java
public class SimpleScheduler implements Scheduler {
    @Override
    public void schedule(Process process) {
        scheduledProcesses.add(process);
    }
}
```

### Custom Exception

```java
public class InvalidProcessException extends Exception {
    public InvalidProcessException(String message) {
        super(message);
    }
}
```

### Singleton

```java
public static synchronized Logger getInstance() {
    if (instance == null) {
        instance = new Logger();
    }
    return instance;
}
```

## 14. Final Viva Closing Statement

"This project helped me implement Java OOP concepts practically. It is not only
a menu-driven program; it is designed using classes with separate
responsibilities. The process, thread, task, scheduler, logger, backend, and
exception classes work together. It also demonstrates Java multithreading,
collections, file handling, custom exceptions, synchronization, and a simple web
API."
