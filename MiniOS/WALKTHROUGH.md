════════════════════════════════════════════════════════════════════════════════
                    INTERACTIVE VERSION - COMPLETE WALKTHROUGH
════════════════════════════════════════════════════════════════════════════════

This document shows a complete example of using the interactive Mini OS simulator.
Follow along to understand all features!


════════════════════════════════════════════════════════════════════════════════
🚀 STARTING THE SIMULATOR
════════════════════════════════════════════════════════════════════════════════

$ cd MiniOS
$ javac -d . Main.java
$ java os.Main

OUTPUT:
═══════════════════════════════════════════════════════════════════════════════

╔════════════════════════════════════════════════════════════╗
║                                                            ║
║   Mini OS Process & Thread Management Simulator            ║
║   INTERACTIVE MENU-DRIVEN VERSION                          ║
║                                                            ║
║   Dynamic Process & Thread Creation with User Control      ║
║                                                            ║
╚════════════════════════════════════════════════════════════╝

Scheduler: Primary OS Scheduler

════════════════════════════════════════════════════════════════════════════════
MAIN MENU - Mini OS Simulator
════════════════════════════════════════════════════════════════════════════════
1. Create a New Process
2. Add Thread to Process
3. Run a Process
4. View Scheduler Information
5. Exit Program
════════════════════════════════════════════════════════════════════════════════
Enter your choice (1-5):


════════════════════════════════════════════════════════════════════════════════
📌 STEP 1: Create First Process
════════════════════════════════════════════════════════════════════════════════

USER INPUT:  1
             ↓
MENU SHOWS:  "Enter your choice (1-5): 1"

THEN DISPLAYS:
────────────────────────────────────────────────────────────
CREATE NEW PROCESS
────────────────────────────────────────────────────────────
Enter process name: 

USER ENTERS:  My File Server

OUTPUT:
────────────────────────────────────────────────────────────
[SimpleScheduler] Scheduled process: My File Server (ID: 1)

✅ Process created successfully!
   Process ID: 1
   Process Name: My File Server
   Threads Count: 0

BACK TO MENU:
════════════════════════════════════════════════════════════════════════════════
MAIN MENU - Mini OS Simulator
════════════════════════════════════════════════════════════════════════════════
1. Create a New Process
2. Add Thread to Process
3. Run a Process
4. View Scheduler Information
5. Exit Program
════════════════════════════════════════════════════════════════════════════════
Enter your choice (1-5):


════════════════════════════════════════════════════════════════════════════════
📌 STEP 2: Add First Thread (PrintTask)
════════════════════════════════════════════════════════════════════════════════

USER INPUT:  2
             ↓
MENU SHOWS:  "Enter your choice (1-5): 2"

THEN DISPLAYS:
────────────────────────────────────────────────────────────
ADD THREAD TO PROCESS
────────────────────────────────────────────────────────────

Available Processes:
1. My File Server (ID: 1, Threads: 0)

Select process number: 

USER ENTERS:  1

THEN SHOWS TASK TYPES:
────────────────────────────────────────────────────────────
Select Task Type:
1. Print Task
2. Calculation Task
Enter choice (1-2): 

USER ENTERS:  1

PROMPTS FOR PRINT TASK DETAILS:
────────────────────────────────────────────────────────────
Enter message to print: 

USER ENTERS:  Accepting client connections...

THEN:
────────────────────────────────────────────────────────────
Enter number of iterations: 

USER ENTERS:  3

OUTPUT:
────────────────────────────────────────────────────────────
[SimpleScheduler] Scheduled process: My File Server (ID: 1)
Thread added to process 'My File Server': Thread-1

✅ Print Task added successfully!
   Task: PrintTask-101
   Message: Accepting client connections...
   Iterations: 3

BACK TO MENU:
════════════════════════════════════════════════════════════════════════════════

════════════════════════════════════════════════════════════════════════════════
📌 STEP 3: Add Second Thread (CalculationTask)
════════════════════════════════════════════════════════════════════════════════

USER INPUT:  2
             ↓
Available Processes:
1. My File Server (ID: 1, Threads: 1)

USER ENTERS:  1

Select Task Type:
1. Print Task
2. Calculation Task
Enter choice (1-2): 

USER ENTERS:  2

PROMPTS FOR CALCULATION TASK:
────────────────────────────────────────────────────────────
Enter first number: 

USER ENTERS:  1024

THEN:
────────────────────────────────────────────────────────────
Enter second number: 

USER ENTERS:  64

THEN:
────────────────────────────────────────────────────────────
Available operations: +, -, *, /
Enter operation: 

USER ENTERS:  /

OUTPUT:
────────────────────────────────────────────────────────────
Thread added to process 'My File Server': Thread-2

✅ Calculation Task added successfully!
   Task: CalcTask-102
   Operation: 1024 / 64

════════════════════════════════════════════════════════════════════════════════
📌 STEP 4: View Scheduler Information
════════════════════════════════════════════════════════════════════════════════

USER INPUT:  4
             ↓
OUTPUT:
════════════════════════════════════════════════════════════════════════════════
SCHEDULER INFORMATION
════════════════════════════════════════════════════════════════════════════════

Scheduler Name: Primary OS Scheduler
Total Scheduled Processes: 1

Process Details:

  Process ID: 1
  Name: My File Server
  State: CREATED
  Threads: 2
  Threads List:
    - Thread-1: PrintTask-101 [PENDING ⏳]
    - Thread-2: CalcTask-102 [PENDING ⏳]

════════════════════════════════════════════════════════════════════════════════


════════════════════════════════════════════════════════════════════════════════
📌 STEP 5: Run the Process (Main Action!)
════════════════════════════════════════════════════════════════════════════════

USER INPUT:  3
             ↓
MENU SHOWS:
────────────────────────────────────────────────────────────
RUN PROCESS
────────────────────────────────────────────────────────────

Available Processes:
1. My File Server (ID: 1, Threads: 2, State: CREATED)

Select process to run: 

USER ENTERS:  1

OUTPUT: (Main Execution Happens Here!)
════════════════════════════════════════════════════════════════════════════════

────────────────────────────────────────────────────────────

========== Starting Process: My File Server ==========
Process ID: 1, Total Threads: 2
========================================

[Thread-1] Started with task: PrintTask-101
[Thread-2] Started with task: CalcTask-102
[Thread-1] PrintTask-101 - Iteration 1: Accepting client connections...
[Thread-2] Starting CalculationTask: CalcTask-102
[Thread-1] PrintTask-101 - Iteration 2: Accepting client connections...
[Thread-2] CalcTask-102 Result: 1024 / 64 = 16.00
[Thread-1] PrintTask-101 - Iteration 3: Accepting client connections...
[Thread-2] CalculationTask 'CalcTask-102' completed!
[Thread-2] Finished! Duration: 605ms
[Thread-1] PrintTask 'PrintTask-101' completed!
[Thread-1] Finished! Duration: 1505ms

========== Process Completed: My File Server ==========
Total execution time: 1510ms

Process Statistics:
  Total Threads: 2
  Completed Threads: 2
  Total Thread Execution Time: 2110ms

────────────────────────────────────────────────────────────


════════════════════════════════════════════════════════════════════════════════
📌 STEP 6: Create Second Process
════════════════════════════════════════════════════════════════════════════════

USER INPUT:  1
             ↓
OUTPUT:
────────────────────────────────────────────────────────────
CREATE NEW PROCESS
────────────────────────────────────────────────────────────
Enter process name: 

USER ENTERS:  Backup Service

OUTPUT:
════════════════════════════════════════════════════════════════════════════════
[SimpleScheduler] Scheduled process: Backup Service (ID: 2)

✅ Process created successfully!
   Process ID: 2
   Process Name: Backup Service
   Threads Count: 0

════════════════════════════════════════════════════════════════════════════════
📌 STEP 7: Add Thread to Second Process
════════════════════════════════════════════════════════════════════════════════

USER INPUT:  2
             ↓
Available Processes:
1. My File Server (ID: 1, Threads: 2)
2. Backup Service (ID: 2, Threads: 0)

USER ENTERS:  2

Select Task Type:
1. Print Task
2. Calculation Task
Enter choice (1-2): 

USER ENTERS:  1

Enter message to print: 

USER ENTERS:  Backing up data...

Enter number of iterations: 

USER ENTERS:  2

OUTPUT:
════════════════════════════════════════════════════════════════════════════════
Thread added to process 'Backup Service': Thread-3

✅ Print Task added successfully!
   Task: PrintTask-103
   Message: Backing up data...
   Iterations: 2

════════════════════════════════════════════════════════════════════════════════
📌 STEP 8: View Updated Scheduler Information
════════════════════════════════════════════════════════════════════════════════

USER INPUT:  4
             ↓
OUTPUT:
════════════════════════════════════════════════════════════════════════════════
SCHEDULER INFORMATION
════════════════════════════════════════════════════════════════════════════════

Scheduler Name: Primary OS Scheduler
Total Scheduled Processes: 2

Process Details:

  Process ID: 1
  Name: My File Server
  State: COMPLETED
  Threads: 2
  Threads List:
    - Thread-1: PrintTask-101 [COMPLETED ✅]
    - Thread-2: CalcTask-102 [COMPLETED ✅]

  Process ID: 2
  Name: Backup Service
  State: CREATED
  Threads: 1
  Threads List:
    - Thread-3: PrintTask-103 [PENDING ⏳]

════════════════════════════════════════════════════════════════════════════════

Notice:
- Process 1 now shows COMPLETED with ✅ on all threads
- Process 2 shows CREATED with ⏳ on pending thread


════════════════════════════════════════════════════════════════════════════════
📌 STEP 9: Run the Second Process
════════════════════════════════════════════════════════════════════════════════

USER INPUT:  3
             ↓
SELECT PROCESS:  2

OUTPUT:
════════════════════════════════════════════════════════════════════════════════

────────────────────────────────────────────────────────────

========== Starting Process: Backup Service ==========
Process ID: 2, Total Threads: 1
========================================

[Thread-3] Started with task: PrintTask-103
[Thread-3] PrintTask-103 - Iteration 1: Backing up data...
[Thread-3] PrintTask-103 - Iteration 2: Backing up data...
[Thread-3] PrintTask 'PrintTask-103' completed!
[Thread-3] Finished! Duration: 1005ms

========== Process Completed: Backup Service ==========
Total execution time: 1015ms

Process Statistics:
  Total Threads: 1
  Completed Threads: 1
  Total Thread Execution Time: 1005ms

────────────────────────────────────────────────────────────


════════════════════════════════════════════════════════════════════════════════
📌 STEP 10: Run First Process Again (Yes, You Can!)
════════════════════════════════════════════════════════════════════════════════

USER INPUT:  3
             ↓
SELECT PROCESS:  1

OUTPUT:
════════════════════════════════════════════════════════════════════════════════

────────────────────────────────────────────────────────────

========== Starting Process: My File Server ==========
Process ID: 1, Total Threads: 2
========================================

[Thread-1] Started with task: PrintTask-101
[Thread-2] Started with task: CalcTask-102
[Thread-1] PrintTask-101 - Iteration 1: Accepting client connections...
[Thread-2] Starting CalculationTask: CalcTask-102
[Thread-1] PrintTask-101 - Iteration 2: Accepting client connections...
[Thread-2] CalcTask-102 Result: 1024 / 64 = 16.00
[Thread-1] PrintTask-101 - Iteration 3: Accepting client connections...
[Thread-2] CalculationTask 'CalcTask-102' completed!
[Thread-2] Finished! Duration: 605ms
[Thread-1] PrintTask 'PrintTask-101' completed!
[Thread-1] Finished! Duration: 1505ms

========== Process Completed: My File Server ==========
Total execution time: 1510ms

Process Statistics:
  Total Threads: 2
  Completed Threads: 2
  Total Thread Execution Time: 2110ms

────────────────────────────────────────────────────────────

The same process executed again with the same results!


════════════════════════════════════════════════════════════════════════════════
📌 STEP 11: Exit Program
════════════════════════════════════════════════════════════════════════════════

USER INPUT:  5
             ↓
OUTPUT:
════════════════════════════════════════════════════════════════════════════════
TERMINATING MINI OS SIMULATOR
════════════════════════════════════════════════════════════════════════════════

Final Statistics:
  Total Processes Created: 2
  Processes Executed: 3              ← Note: Process 1 ran twice!
  Total Threads Created: 3

✅ Simulator terminated successfully.
📝 Check 'log.txt' for detailed execution logs.
════════════════════════════════════════════════════════════════════════════════


════════════════════════════════════════════════════════════════════════════════
📋 ALL ACTIVITIES LOGGED
════════════════════════════════════════════════════════════════════════════════

$ cat log.txt

OUTPUT:
════════════════════════════════════════════════════════════════════════════════
[2026-04-09 14:23:45.123] ========== Logger Initialized ==========
[2026-04-09 14:23:45.145] INFO: Interactive Mini OS Simulator Started
[2026-04-09 14:23:45.147] INFO: Scheduler created: Primary OS Scheduler
[2026-04-09 14:23:47.203] SUCCESS: Process created: My File Server (ID: 1)
[2026-04-09 14:23:49.567] SUCCESS: PrintTask added to process: My File Server
[2026-04-09 14:23:52.891] SUCCESS: CalculationTask added to process: My File Server
[2026-04-09 14:23:54.234] INFO: Scheduler info viewed
[2026-04-09 14:23:56.789] INFO: Starting process execution: My File Server
[2026-04-09 14:23:58.401] SUCCESS: Process execution completed: My File Server
[2026-04-09 14:24:00.145] SUCCESS: Process created: Backup Service (ID: 2)
[2026-04-09 14:24:02.312] SUCCESS: PrintTask added to process: Backup Service
[2026-04-09 14:24:03.789] INFO: Scheduler info viewed
[2026-04-09 14:24:05.145] INFO: Starting process execution: Backup Service
[2026-04-09 14:24:06.234] SUCCESS: Process execution completed: Backup Service
[2026-04-09 14:24:07.567] INFO: Starting process execution: My File Server
[2026-04-09 14:24:09.123] SUCCESS: Process execution completed: My File Server
[2026-04-09 14:24:10.456] INFO: Interactive Mini OS Simulator terminated

════════════════════════════════════════════════════════════════════════════════


════════════════════════════════════════════════════════════════════════════════
🎯 KEY OBSERVATIONS FROM THIS WALKTHROUGH
════════════════════════════════════════════════════════════════════════════════

1. CONCURRENT EXECUTION
   ✓ Thread-1 (PrintTask) and Thread-2 (CalculationTask) ran simultaneously
   ✓ Output shows interleaved execution (Thread-1, then Thread-2, then Thread-1)
   ✓ Both completed in ~1.5 seconds (not 2+ seconds if sequential)
   ✓ This proves real multithreading!

2. PROCESS REUSABILITY
   ✓ "My File Server" process ran twice (Step 5 and Step 10)
   ✓ Same results both times
   ✓ Process state changed from CREATED → COMPLETED → Can run again

3. USER CONTROL
   ✓ Created 2 processes
   ✓ Added threads dynamically
   ✓ Ran processes in any order we wanted
   ✓ Re-ran a process
   ✓ Viewed information anytime

4. LOGGING
   ✓ Every action logged with timestamp
   ✓ Clean, readable log file
   ✓ All events tracked for debugging/audit

5. STATE MANAGEMENT
   ✓ Process 1: Started as CREATED, became COMPLETED
   ✓ Process 2: Started as CREATED, became COMPLETED
   ✓ Threads marked as PENDING (⏳) before execution
   ✓ Threads marked as COMPLETED (✅) after execution

6. FLEXIBILITY
   ✓ Different process names possible
   ✓ Any number of threads possible
   ✓ Different task types possible
   ✓ Different parameters possible
   ✓ Not limited to hardcoded scenarios!


════════════════════════════════════════════════════════════════════════════════
💡 WHAT THIS DEMONSTRATES
════════════════════════════════════════════════════════════════════════════════

✓ OBJECT-ORIENTED PROGRAMMING
  - Process objects created and managed
  - ThreadTask objects with Task polymorphism
  - Encapsulation of process state
  - Inheritance from Thread class

✓ MULTITHREADING
  - Multiple threads in one process
  - Concurrent execution visible
  - Synchronization with join()
  - Real OS-like behavior

✓ USER INTERFACE DESIGN
  - Menu-driven interface
  - User input validation
  - Clear output formatting
  - Error messages

✓ SYSTEM DESIGN
  - Process management
  - Thread scheduling (user-controlled)
  - State transitions
  - Event logging

✓ REAL-WORLD CONCEPTS
  - Creating processes like OS does
  - Managing multiple threads
  - Running processes
  - Tracking execution


════════════════════════════════════════════════════════════════════════════════
                            Happy Simulating! 🚀
════════════════════════════════════════════════════════════════════════════════
