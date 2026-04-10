/**
 * Mini OS Control Center - Application Logic
 * Manages UI interactions, API calls, and real-time updates
 */

// ============================================================
// CONSTANTS & CONFIGURATION
// ============================================================

const API_BASE = 'http://localhost:8080/api';
const ENDPOINTS = {
    CREATE_PROCESS: '/createProcess',
    ADD_THREAD: '/addThread',
    RUN_PROCESS: '/runProcess',
    GET_PROCESSES: '/getProcesses',
    GET_SCHEDULER_INFO: '/getSchedulerInfo'
};

// ============================================================
// STATE MANAGEMENT
// ============================================================

const state = {
    processes: [],
    currentProcess: null,
    stats: {
        totalProcesses: 0,
        activeThreads: 0,
        completedTasks: 0,
        errors: 0
    }
};

// ============================================================
// INITIALIZATION
// ============================================================

document.addEventListener('DOMContentLoaded', () => {
    initializeEventListeners();
    initializeModals();
    loadInitialData();
    startActivityFeed();
});

// ============================================================
// EVENT LISTENERS
// ============================================================

function initializeEventListeners() {
    // Tab Navigation
    document.querySelectorAll('.nav-tab').forEach(tab => {
        tab.addEventListener('click', switchTab);
    });

    // Create Process Button
    document.getElementById('create-process-btn').addEventListener('click', () => {
        showModal('create-process-modal');
    });

    // Create Process Form
    document.getElementById('create-process-form').addEventListener('submit', handleCreateProcess);

    // Add Thread Form
    document.getElementById('add-thread-form').addEventListener('submit', handleAddThread);

    // Task Type Selection
    document.getElementById('task-type').addEventListener('change', handleTaskTypeChange);

    // Clear Activity Log
    document.getElementById('clear-log').addEventListener('click', clearActivityFeed);
}

// ============================================================
// MODAL MANAGEMENT
// ============================================================

function initializeModals() {
    // Close modal when clicking close button
    document.querySelectorAll('.modal-close').forEach(btn => {
        btn.addEventListener('click', (e) => {
            const modalId = e.target.getAttribute('data-modal');
            hideModal(modalId);
        });
    });

    // Close modal when clicking action buttons
    document.querySelectorAll('[data-modal]').forEach(btn => {
        if (btn.tagName === 'BUTTON' && !btn.classList.contains('modal-close')) {
            btn.addEventListener('click', (e) => {
                if (e.target.getAttribute('data-modal')) {
                    hideModal(e.target.getAttribute('data-modal'));
                }
            });
        }
    });

    // Close modal on background click
    document.querySelectorAll('.modal').forEach(modal => {
        modal.addEventListener('click', (e) => {
            if (e.target === modal) {
                hideModal(modal.id);
            }
        });
    });
}

function showModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.classList.add('active');
        document.body.style.overflow = 'hidden';
    }
}

function hideModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.classList.remove('active');
        document.body.style.overflow = '';
        // Reset form if it exists
        const form = modal.querySelector('form');
        if (form) form.reset();
    }
}

// ============================================================
// TAB NAVIGATION
// ============================================================

function switchTab(e) {
    const tabName = e.currentTarget.getAttribute('data-tab');

    // Update active tab button
    document.querySelectorAll('.nav-tab').forEach(tab => {
        tab.classList.remove('active');
    });
    e.currentTarget.classList.add('active');

    // Update active content
    document.querySelectorAll('.tab-content').forEach(content => {
        content.classList.remove('active');
    });
    document.getElementById(tabName).classList.add('active');

    // Load tab-specific data
    if (tabName === 'scheduler') {
        loadSchedulerData();
    }
}

// ============================================================
// PROCESS MANAGEMENT
// ============================================================

async function handleCreateProcess(e) {
    e.preventDefault();

    const processName = document.getElementById('process-name').value.trim();

    if (!processName) {
        showToast('Process name cannot be empty', 'error');
        return;
    }

    try {
        // Call backend API
        const response = await fetch(`${API_BASE}${ENDPOINTS.CREATE_PROCESS}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name: processName })
        });

        if (!response.ok) {
            throw new Error('Failed to create process');
        }

        const data = await response.json();
        const newProcess = {
            id: data.id || state.processes.length + 1,
            name: processName,
            status: 'CREATED',
            threads: [],
            createdAt: new Date()
        };

        state.processes.push(newProcess);
        state.stats.totalProcesses++;

        hideModal('create-process-modal');
        renderProcesses();
        updateStats();
        addActivityLog(`Process created: "${processName}"`, 'info');
        showToast(`Process "${processName}" created successfully!`, 'success');
    } catch (error) {
        console.error('Error creating process:', error);
        showToast('Failed to create process', 'error');
        addActivityLog(`Error creating process: ${error.message}`, 'error');
    }
}

function renderProcesses() {
    const container = document.getElementById('processes-container');

    if (state.processes.length === 0) {
        container.innerHTML = `
            <div class="empty-state">
                <div class="empty-icon">📭</div>
                <p>No processes created yet</p>
                <small>Click "Create New Process" to get started</small>
            </div>
        `;
        return;
    }

    container.innerHTML = state.processes.map(process => `
        <div class="process-card">
            <div class="process-header">
                <div class="process-title">
                    <div class="process-name">⚡ ${process.name}</div>
                    <div class="process-id">PID: ${process.id}</div>
                </div>
                <span class="status-badge ${process.status.toLowerCase()}">
                    ${process.status}
                </span>
            </div>

            <div class="process-stats">
                <div class="process-stat">
                    <div class="process-stat-value">${process.threads.length}</div>
                    <div class="process-stat-label">Threads</div>
                </div>
                <div class="process-stat">
                    <div class="process-stat-value">${Math.floor(Math.random() * 50 + 5)}</div>
                    <div class="process-stat-label">Duration (ms)</div>
                </div>
            </div>

            <div class="process-actions">
                <button class="btn btn-secondary btn-add-thread" data-process-id="${process.id}">
                    <span>➕</span> Add Thread
                </button>
                <button class="btn btn-primary btn-run-process" data-process-id="${process.id}">
                    <span>▶️</span> Run
                </button>
            </div>
        </div>
    `).join('');

    // Add event listeners to dynamically created buttons
    container.querySelectorAll('.btn-add-thread').forEach(btn => {
        btn.addEventListener('click', (e) => {
            const processId = parseInt(e.currentTarget.getAttribute('data-process-id'));
            state.currentProcess = state.processes.find(p => p.id === processId);
            document.getElementById('thread-process-id').value = processId;
            showModal('add-thread-modal');
        });
    });

    container.querySelectorAll('.btn-run-process').forEach(btn => {
        btn.addEventListener('click', (e) => {
            const processId = parseInt(e.currentTarget.getAttribute('data-process-id'));
            runProcess(processId);
        });
    });
}

// ============================================================
// THREAD MANAGEMENT
// ============================================================

function handleTaskTypeChange(e) {
    const taskType = e.target.value;
    document.getElementById('print-fields').classList.add('hidden');
    document.getElementById('calc-fields').classList.add('hidden');

    if (taskType === 'print') {
        document.getElementById('print-fields').classList.remove('hidden');
    } else if (taskType === 'calculation') {
        document.getElementById('calc-fields').classList.remove('hidden');
    }
}

async function handleAddThread(e) {
    e.preventDefault();

    const processId = parseInt(document.getElementById('thread-process-id').value);
    const taskType = document.getElementById('task-type').value;

    if (!taskType) {
        showToast('Please select a task type', 'error');
        return;
    }

    try {
        let threadData;

        if (taskType === 'print') {
            const message = document.getElementById('print-message').value.trim();
            const iterations = parseInt(document.getElementById('print-iterations').value);

            if (!message) {
                showToast('Please enter a message', 'error');
                return;
            }

            threadData = {
                processId,
                taskType: 'PrintTask',
                message,
                iterations
            };
        } else if (taskType === 'calculation') {
            const num1 = parseInt(document.getElementById('calc-num1').value);
            const num2 = parseInt(document.getElementById('calc-num2').value);
            const operator = document.getElementById('calc-operator').value;

            if (isNaN(num1) || isNaN(num2)) {
                showToast('Please enter valid numbers', 'error');
                return;
            }

            threadData = {
                processId,
                taskType: 'CalculationTask',
                num1,
                num2,
                operator
            };
        }

        // Call backend API
        const response = await fetch(`${API_BASE}${ENDPOINTS.ADD_THREAD}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(threadData)
        });

        if (!response.ok) {
            throw new Error('Failed to add thread');
        }

        const process = state.processes.find(p => p.id === processId);
        if (process) {
            process.threads.push({
                id: process.threads.length + 1,
                type: taskType,
                data: threadData
            });
            state.stats.activeThreads++;
        }

        hideModal('add-thread-modal');
        renderProcesses();
        updateStats();
        showToast('Thread added successfully!', 'success');
        addActivityLog(`Thread added to process "${process.name}"`, 'info');
    } catch (error) {
        console.error('Error adding thread:', error);
        showToast('Failed to add thread', 'error');
    }
}

// ============================================================
// PROCESS EXECUTION
// ============================================================

async function runProcess(processId) {
    const process = state.processes.find(p => p.id === processId);

    if (!process) {
        showToast('Process not found', 'error');
        return;
    }

    if (process.threads.length === 0) {
        showToast('Cannot run process without threads', 'error');
        return;
    }

    process.status = 'RUNNING';
    renderProcesses();
    showModal('execution-modal');

    try {
        // Call backend API to run process
        const response = await fetch(`${API_BASE}${ENDPOINTS.RUN_PROCESS}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ processId })
        });

        const data = await response.json();

        // Simulate execution output
        simulateExecutionOutput(process);

        // Wait and then mark as completed
        setTimeout(() => {
            process.status = 'COMPLETED';
            state.stats.completedTasks++;
            renderProcesses();
            updateStats();
            addActivityLog(
                `Process "${process.name}" completed successfully`,
                'success'
            );
        }, 3000);
    } catch (error) {
        console.error('Error running process:', error);
        process.status = 'CREATED';
        addActivityLog(`Error running process: ${error.message}`, 'error');
        state.stats.errors++;
        updateStats();
    }
}

function simulateExecutionOutput(process) {
    const output = document.getElementById('execution-output');
    output.innerHTML = '';

    const lines = [
        { text: `========== Starting Process: ${process.name} ==========`, type: 'info' },
        { text: `Process ID: ${process.id}, Total Threads: ${process.threads.length}`, type: 'info' },
        { text: '========================================', type: 'info' },
    ];

    process.threads.forEach((thread, idx) => {
        lines.push({ text: `[Thread-${idx + 1}] Started with task: ${thread.type}`, type: 'info' });
    });

    lines.push({ text: '', type: 'info' });

    // Simulate thread output
    process.threads.forEach((thread, idx) => {
        const threadNum = idx + 1;
        const taskType = thread.data.taskType;

        if (taskType === 'PrintTask') {
            for (let i = 0; i < thread.data.iterations; i++) {
                lines.push({
                    text: `[Thread-${threadNum}] ${thread.data.message} (iteration ${i + 1})`,
                    type: 'success'
                });
            }
        } else if (taskType === 'CalculationTask') {
            const { num1, num2, operator } = thread.data;
            let result;
            switch (operator) {
                case '+': result = num1 + num2; break;
                case '-': result = num1 - num2; break;
                case '*': result = num1 * num2; break;
                case '/': result = num2 !== 0 ? num1 / num2 : 'Error'; break;
            }
            lines.push({
                text: `[Thread-${threadNum}] Calculating: ${num1} ${operator} ${num2} = ${result}`,
                type: 'success'
            });
        }

        lines.push({ text: `[Thread-${threadNum}] Task completed! (Duration: ${Math.floor(Math.random() * 1500 + 300)}ms)`, type: 'info' });
    });

    lines.push({ text: '', type: 'info' });
    lines.push({ text: `========== Process Completed: ${process.name} ==========`, type: 'success' });
    lines.push({ text: 'Process Statistics:', type: 'info' });
    lines.push({ text: `  Total Threads: ${process.threads.length}`, type: 'info' });
    lines.push({ text: `  Completed Threads: ${process.threads.length}`, type: 'info' });
    lines.push({ text: `  Total Execution Time: ${Math.floor(Math.random() * 2000 + 1000)}ms`, type: 'info' });
    lines.push({ text: '========================================', type: 'info' });

    // Render output with typing animation
    let lineIndex = 0;

    function renderNextLine() {
        if (lineIndex < lines.length) {
            const line = lines[lineIndex];
            const div = document.createElement('div');
            div.className = `output-line ${line.type}`;
            div.textContent = line.text;
            output.appendChild(div);
            output.scrollTop = output.scrollHeight;
            lineIndex++;
            setTimeout(renderNextLine, 50);
        }
    }

    renderNextLine();
}

// ============================================================
// SCHEDULER
// ============================================================

function loadSchedulerData() {
    updateSchedulerStats();
    renderTimeline();
}

function updateSchedulerStats() {
    const queued = state.processes.filter(p => p.status === 'CREATED').length;
    const running = state.processes.filter(p => p.status === 'RUNNING').length;
    const completed = state.processes.filter(p => p.status === 'COMPLETED').length;

    document.getElementById('scheduler-queued').textContent = queued;
    document.getElementById('scheduler-running').textContent = running;
    document.getElementById('scheduler-completed').textContent = completed;
}

function renderTimeline() {
    const timeline = document.getElementById('timeline');

    if (state.processes.length === 0) {
        timeline.innerHTML = `
            <div class="empty-state">
                <p>No execution history yet</p>
            </div>
        `;
        return;
    }

    timeline.innerHTML = state.processes.map((process, idx) => `
        <div class="timeline-item">
            <div class="timeline-dot">${idx + 1}</div>
            <div class="timeline-content">
                <div class="timeline-title">${process.name}</div>
                <div class="timeline-subtitle">
                    <span class="status-badge ${process.status.toLowerCase()}">${process.status}</span>
                </div>
                <div class="timeline-time">
                    Created at ${formatTime(process.createdAt)}
                </div>
            </div>
        </div>
    `).join('');
}

// ============================================================
// STATISTICS & UPDATES
// ============================================================

function loadInitialData() {
    updateStats();
}

function updateStats() {
    document.getElementById('stat-processes').textContent = state.stats.totalProcesses;
    document.getElementById('stat-threads').textContent = state.stats.activeThreads;
    document.getElementById('stat-completed').textContent = state.stats.completedTasks;
    document.getElementById('stat-errors').textContent = state.stats.errors;

    updateSchedulerStats();
}

// ============================================================
// ACTIVITY FEED
// ============================================================

function startActivityFeed() {
    // Auto-add activity items periodically (for simulation)
    setInterval(() => {
        if (Math.random() < 0.1) {
            const messages = [
                'Thread pool capacity optimized',
                'Resource allocation updated',
                'Scheduler working normally',
                'Memory cleanup completed'
            ];
            const msg = messages[Math.floor(Math.random() * messages.length)];
            addActivityLog(msg, 'info');
        }
    }, 5000);
}

function addActivityLog(message, type = 'info') {
    const feed = document.getElementById('activity-feed');
    const time = new Date();
    const timeStr = time.toLocaleTimeString('en-US', { hour12: false });

    const item = document.createElement('div');
    item.className = `activity-item ${type}`;
    item.innerHTML = `
        <span class="time">${timeStr}</span>
        <span class="message">${message}</span>
    `;

    feed.insertBefore(item, feed.firstChild);

    // Keep only last 50 items
    while (feed.children.length > 50) {
        feed.removeChild(feed.lastChild);
    }
}

function clearActivityFeed() {
    const feed = document.getElementById('activity-feed');
    feed.innerHTML = `
        <div class="activity-item info">
            <span class="time">${new Date().toLocaleTimeString('en-US', { hour12: false })}</span>
            <span class="message">Activity log cleared</span>
        </div>
    `;
}

// ============================================================
// NOTIFICATIONS
// ============================================================

function showToast(message, type = 'info', duration = 3000) {
    const container = document.getElementById('toast-container');

    const toast = document.createElement('div');
    toast.className = `toast ${type}`;

    const icons = {
        success: '✓',
        error: '✕',
        info: 'ℹ',
        warning: '⚠'
    };

    toast.innerHTML = `
        <div class="toast-icon">${icons[type]}</div>
        <div class="toast-content">
            <div class="toast-message">${message}</div>
        </div>
    `;

    container.appendChild(toast);

    setTimeout(() => {
        toast.style.animation = 'toastSlideIn 0.3s ease-out reverse';
        setTimeout(() => toast.remove(), 300);
    }, duration);
}

// ============================================================
// UTILITY FUNCTIONS
// ============================================================

function formatTime(date) {
    return date.toLocaleTimeString('en-US', { hour12: false });
}

// ============================================================
// ERROR HANDLING
// ============================================================

window.addEventListener('error', (event) => {
    console.error('Global error:', event.error);
    addActivityLog(`System error: ${event.error.message}`, 'error');
    state.stats.errors++;
    updateStats();
});

// ============================================================
// DEBUGGING
// ============================================================

window.debugState = () => {
    console.log('Current State:', state);
    console.log('Processes:', state.processes);
};
