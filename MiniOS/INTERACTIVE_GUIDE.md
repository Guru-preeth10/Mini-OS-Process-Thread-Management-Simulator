════════════════════════════════════════════════════════════════════════════
           INTERACTIVE MINI OS - USER GUIDE
════════════════════════════════════════════════════════════════════════════

🎮 INTERACTIVE MENU-DRIVEN VERSION
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

This is the UPDATED, USER-INTERACTIVE version of the Mini OS simulator.
Unlike the original automatic demo, you now have FULL CONTROL over:

✅ Creating processes
✅ Adding threads to processes
✅ Running specific processes
✅ Viewing scheduler information
✅ Creating multiple processes and managing them


════════════════════════════════════════════════════════════════════════════
⚙️  HOW TO RUN THE INTERACTIVE VERSION
════════════════════════════════════════════════════════════════════════════

STEP 1: Navigate to project directory
────────────────────────────────────────
cd /path/to/MiniOS

STEP 2: Compile
────────────────────────────────────────
javac -d . Main.java

STEP 3: Run
────────────────────────────────────────
java os.Main

You will see a welcome banner and main menu.


════════════════════════════════════════════════════════════════════════════
📋 MAIN MENU OPTIONS EXPLAINED
════════════════════════════════════════════════════════════════════════════

The main menu offers 5 options:

┌─ OPTION 1: Create a New Process
│  │
│  └─ What it does:
│     Creates a new process with a name you specify.
│     The process starts empty (no threads).
│
│  Example:
│     Enter choice: 1
│     Enter process name: Background Tasks
│     ✅ Process created successfully!
│        Process ID: 1
│        Process Name: Background Tasks
│        Threads Count: 0
│
│  Key points:
│  • Each process gets a unique ID (auto-incremented)
│  • Process name can be any string
│  • Process starts in CREATED state
│  • No threads run until you add them

┌─ OPTION 2: Add Thread to Process
│  │
│  └─ What it does:
│     Adds a thread with a task to an existing process.
│     You can create PrintTask or CalculationTask.
│
│  Workflow:
│     1. Select a process from the list
│     2. Choose task type:
│        - PrintTask: Prints messages repeatedly
│        - CalculationTask: Performs math (+, -, *, /)
│     3. Enter task parameters
│     4. Thread is added to the process
│
│  Example PrintTask:
│     Select process: 1
│     Select task type: 1 (Print Task)
│     Enter message: Hello from Thread
│     Enter iterations: 3
│     ✅ Print Task added successfully!
│
│  Example CalculationTask:
│     Select process: 1
│     Select task type: 2 (Calculation Task)
│     Enter first number: 100
│     Enter second number: 50
│     Enter operation: +
│     ✅ Calculation Task added successfully!
│        Operation: 100 + 50
│
│  Key points:
│  • Can add multiple threads to one process
│  • Threads are numbered automatically (Thread-1, Thread-2, etc.)
│  • Tasks are typed (PrintTask vs CalculationTask)
│  • Tasks execute concurrently when process runs

┌─ OPTION 3: Run a Process
│  │
│  └─ What it does:
│     Executes a selected process with all its threads.
│     Threads run concurrently.
│     Waits for all threads to complete.
│
│  Workflow:
│     1. Select a process from the list
│     2. Process starts (all threads run concurrently)
│     3. Watch output as threads execute
│     4. Process completes with statistics
│
│  Example Output:
│     Select process to run: 1
│
│     ========== Starting Process: My Process ==========
│     Process ID: 1, Total Threads: 2
│     ========================================
│
│     [Thread-1] Started with task: PrintTask-1
│     [Thread-2] Started with task: CalcTask-1
│     [Thread-1] PrintTask-1 - Iteration 1: Hello!
│     [Thread-2] CalcTask-1 Result: 100 + 50 = 150.00
│     [Thread-1] PrintTask-1 - Iteration 2: Hello!
│     [Thread-1] PrintTask-1 completed!
│     [Thread-1] Finished! Duration: 1005ms
│     [Thread-2] CalculationTask 'CalcTask-1' completed!
│     [Thread-2] Finished! Duration: 605ms
│
│     ========== Process Completed: My Process ==========
│     Total execution time: 1010ms
│
│     Process Statistics:
│       Total Threads: 2
│       Completed Threads: 2
│       Total Thread Execution Time: 1610ms
│     ========================================
│
│  Key points:
│  • Threads execute CONCURRENTLY (not sequentially)
│  • Each thread has its own execution time
│  • Process waits for all threads to complete
│  • Statistics show total and individual times

┌─ OPTION 4: View Scheduler Information
│  │
│  └─ What it does:
│     Displays all created processes and their details.
│     Shows thread count, state, and thread list.
│
│  Example Output:
│     ════════════════════════════════════════════════
│     SCHEDULER INFORMATION
│     ════════════════════════════════════════════════
│
│     Scheduler Name: Primary OS Scheduler
│     Total Scheduled Processes: 2
│
│     Process Details:
│
│       Process ID: 1
│       Name: My First Process
│       State: COMPLETED
│       Threads: 2
│       Threads List:
│         - Thread-1: PrintTask-1 [COMPLETED ✅]
│         - Thread-2: CalcTask-1 [COMPLETED ✅]
│
│       Process ID: 2
│       Name: Background Tasks
│       State: CREATED
│       Threads: 1
│       Threads List:
│         - Thread-3: PrintTask-2 [PENDING ⏳]
│     ════════════════════════════════════════════════
│
│  Key points:
│  • Shows all processes created so far
│  • Shows state of each process and thread
│  • Shows execution status (PENDING ⏳ or COMPLETED ✅)

└─ OPTION 5: Exit Program
   │
   └─ What it does:
      Exits the simulator gracefully.
      Shows final statistics.
      Closes the logger.
   
   Example Output:
      ════════════════════════════════════════════════
      TERMINATING MINI OS SIMULATOR
      ════════════════════════════════════════════════
      
      Final Statistics:
        Total Processes Created: 2
        Processes Executed: 1
        Total Threads Created: 3
      
      ✅ Simulator terminated successfully.
      📝 Check 'log.txt' for detailed execution logs.
      ════════════════════════════════════════════════
   
   Key points:
   • Shows summary of your work
   • All logs are saved in log.txt
   • Any unsaved processes are lost


════════════════════════════════════════════════════════════════════════════
📖 STEP-BY-STEP EXAMPLE WALKTHROUGH
════════════════════════════════════════════════════════════════════════════

Let's create a complete workflow:

STEP 1: Create First Process
────────────────────────────
Choice: 1
Name: Database Tasks
Result: Process ID 1 created
Threads: 0

STEP 2: Add Threads to Process 1
────────────────────────────────
Choice: 2
Select Process: 1
Task Type: 1 (Print)
Message: Connecting to database...
Iterations: 2
Result: Thread-1 added with PrintTask

STEP 3: Add Another Thread
─────────────────────────
Choice: 2
Select Process: 1
Task Type: 2 (Calculation)
Number 1: 1000
Number 2: 100
Operation: /
Result: Thread-2 added with CalculationTask

STEP 4: View Scheduler
──────────────────────
Choice: 4
Result: Shows Process 1 with 2 threads, both PENDING

STEP 5: Run Process
───────────────────
Choice: 3
Select Process: 1
Result:
  - Thread-1 prints message 2 times (1 sec each)
  - Thread-2 calculates 1000/100=10.0
  - Both threads run concurrently
  - Statistics shown

STEP 6: Create Second Process
──────────────────────────────
Choice: 1
Name: Cleanup Tasks
Result: Process ID 2 created

STEP 7: Add Thread to Process 2
───────────────────────────────
Choice: 2
Select Process: 2
Task Type: 1 (Print)
Message: Cleaning memory...
Iterations: 1
Result: Thread-3 added

STEP 8: View Scheduler Again
──────────────────────────────
Choice: 4
Result: Shows both processes
  - Process 1: COMPLETED with 2 completed threads
  - Process 2: CREATED with 1 pending thread

STEP 9: Run Process 2
──────────────────────
Choice: 3
Select Process: 2
Result: Thread-3 executes

STEP 10: Exit
──────────────
Choice: 5
Result: Final statistics, log file reference


════════════════════════════════════════════════════════════════════════════
💡 KEY FEATURES & BEHAVIOR
════════════════════════════════════════════════════════════════════════════

PROCESS STATES
──────────────
✓ CREATED
  - Initial state when process is created
  - Threads can be added
  - Process waiting to execute
  
✓ RUNNING
  - Process is executing threads
  - Cannot modify during execution
  
✓ COMPLETED
  - All threads finished
  - Statistics calculated
  - Process can be run again
  
✓ TERMINATED
  - Reserved for future use

THREAD BEHAVIOR
───────────────
✓ Automatic naming: Thread-1, Thread-2, etc.
✓ Concurrent execution: All threads run simultaneously
✓ Independent tasks: Each thread executes independently
✓ Timing: Task execution times vary by task type
  - PrintTask: 500ms per iteration
  - CalculationTask: 600ms per calculation

TASK TYPES
──────────
PrintTask:
✓ Prints a message N times
✓ Each iteration takes 500ms
✓ Useful for simulating I/O operations
✓ Examples: logging, status updates, notifications

CalculationTask:
✓ Performs mathematical operations: +, -, *, /
✓ Each calculation takes 600ms
✓ Division by zero is handled (error message shown)
✓ Examples: data processing, scientific computing

LOGGING
───────
✓ All events logged to log.txt
✓ Timestamped entries: [YYYY-MM-DD HH:MM:SS.mmm]
✓ Log levels: INFO, WARNING, ERROR, SUCCESS
✓ Check log.txt for full execution trace


════════════════════════════════════════════════════════════════════════════
⚠️  IMPORTANT NOTES
════════════════════════════════════════════════════════════════════════════

1. PROCESS vs EXECUTION
   - Creating a process (Option 1): Just schedules it
   - Adding threads (Option 2): Prepares the process
   - Running a process (Option 3): ACTUALLY executes it
   - Threads don't run until you select Option 3!

2. MULTIPLE THREADS
   - Can add as many threads as you want to a process
   - All threads in a process run CONCURRENTLY
   - You'll see interleaved output as threads write to console
   - This demonstrates real multithreading!

3. RUNNING SAME PROCESS TWICE
   - You can run the same process multiple times
   - Process state will be COMPLETED after first run
   - Can run it again (threads will execute again)
   - Statistics will be updated

4. OUTPUT ORDER
   - With concurrent threads, output order is non-deterministic
   - Thread-1 might not finish before Thread-2 starts
   - This is real multithreading, not pseudo-concurrency

5. IDLE TIME
   - Threads sleep to simulate work
   - PrintTask: 500ms per iteration
   - CalculationTask: 300ms before + 300ms after calculation
   - This makes execution times more realistic


════════════════════════════════════════════════════════════════════════════
🐛 TROUBLESHOOTING
════════════════════════════════════════════════════════════════════════════

Q: "Cannot run process with no threads"
A: You must add at least one thread before running.
   Step: Option 2 → Add a thread first, then Option 3

Q: "No processes available"
A: Create a process first.
   Step: Option 1 → Create a process, then Option 2 or 3

Q: Output looks jumbled/mixed
A: This is NORMAL! With concurrent threads:
   - Threads write to console at different times
   - Output gets interleaved
   - This proves multithreading is working!
   - Check log.txt for clean, timestamped log

Q: Process didn't run?
A: Could be:
   - No threads added (need at least 1)
   - Process was already completed (can run again)
   - Input validation failed (check messages)

Q: How long does it take?
A: Depends on your tasks:
   - PrintTask(1 iteration) = ~500ms
   - CalculationTask = ~600ms
   - 2 concurrent threads = ~600ms (not 1.1s)
   - 3 concurrent threads = ~600ms (all same duration)


════════════════════════════════════════════════════════════════════════════
📊 REAL-WORLD SIMULATION EXAMPLE
════════════════════════════════════════════════════════════════════════════

Simulate a Web Server:

Process 1: "Request Handler"
- Thread-1: PrintTask("Processing request 1", 2)
- Thread-2: PrintTask("Processing request 2", 2)
- Thread-3: PrintTask("Processing request 3", 1)
→ Run it: All 3 requests handled concurrently in ~1 second!

Process 2: "Data Processing"
- Thread-4: CalculationTask(1000, 100, /)
- Thread-5: CalculationTask(2000, 50, *)
- Thread-6: CalculationTask(500, 200, +)
→ Run it: All 3 calculations happen in parallel!

This simulates how a real OS manages multiple processes!


════════════════════════════════════════════════════════════════════════════
✅ DIFFERENCES FROM ORIGINAL VERSION
════════════════════════════════════════════════════════════════════════════

ORIGINAL (Non-Interactive):
❌ 3 hardcoded processes auto-created
❌ 8 threads pre-defined
❌ Everything executed automatically
❌ No user control
❌ Demo-only version
❌ Single run-through

NEW (Interactive):
✅ Create unlimited processes dynamically
✅ Add threads on-demand
✅ Full user control via menu
✅ Real simulator for learning
✅ Multiple workflows possible
✅ Explore different scenarios
✅ Same OOP concepts, but interactive!


════════════════════════════════════════════════════════════════════════════
🎯 LEARNING OUTCOMES
════════════════════════════════════════════════════════════════════════════

By using the interactive version, you'll understand:

✓ How processes manage threads
✓ How threads execute concurrently
✓ Real-world OS scheduling concepts
✓ State management in OS
✓ User control and interactivity
✓ Practical OOP in action
✓ Multithreading in real applications


════════════════════════════════════════════════════════════════════════════
                          Enjoy simulating! 🚀
════════════════════════════════════════════════════════════════════════════
