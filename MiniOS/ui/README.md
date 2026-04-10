# 🎨 Mini OS Control Center - Web UI

A professional, production-grade web dashboard for the Mini OS Process & Thread Management Simulator.

## 🌟 Features

✨ **Modern Dark UI**
- Glassmorphism design
- Smooth animations & transitions
- High contrast for accessibility
- Professional dashboard aesthetic

🎮 **Full Interactive Dashboard**
- Real-time statistics
- Process management
- Thread creation & execution
- Scheduler timeline view

📊 **Live Monitoring**
- Activity feed with auto-scrolling
- System resource indicators
- Execution timeline
- Process status tracking

🎯 **User-Friendly Interface**
- Intuitive modals for creation
- Toast notifications
- Loading indicators
- Responsive design (mobile-friendly)

⚡ **Smooth Interactions**
- Animated transitions
- Status badges with pulsing effects
- Typing animation for output
- Auto-scrolling execution logs

## 📁 File Structure

```
ui/
├── index.html          # Main HTML structure
├── css/
│   └── styles.css      # All styling (dark theme, animations, responsive)
├── js/
│   └── app.js          # JavaScript logic & API integration
└── README.md           # This file
```

## 🚀 Quick Start

### 1. Open in Browser

Simply open `index.html` in a modern web browser (Chrome, Firefox, Safari, Edge):

```bash
# Option 1: Open directly
open index.html

# Option 2: Use Python simple server
python -m http.server 8000
# Then visit: http://localhost:8000/ui
```

### 2. Features to Explore

#### Dashboard Tab
- View live statistics
- Recent activity feed
- System resources panel

#### Processes Tab
- **Create New Process** - Click button to create processes
- **Add Thread** - Click "Add Thread" on any process card
- **Run Process** - Execute with live output

#### Scheduler Tab
- View scheduler statistics
- Execution timeline
- Queue & completion status

## 🔌 Backend Integration

The UI communicates with the Java backend via REST API calls. Update the API endpoint in `js/app.js`:

```javascript
const API_BASE = 'http://localhost:8080/api';  // Change this to your backend URL
```

### Required Endpoints

The Java backend should provide these endpoints:

```
POST /api/createProcess
  Body: { name: "Process Name" }
  Response: { id: 1, status: "CREATED" }

POST /api/addThread
  Body: { processId: 1, taskType: "PrintTask", ... }
  Response: { success: true }

POST /api/runProcess
  Body: { processId: 1 }
  Response: { status: "RUNNING" }

GET /api/getProcesses
  Response: [{ id: 1, name: "...", status: "...", threads: [] }]

GET /api/getSchedulerInfo
  Response: { queued: 2, running: 1, completed: 5 }
```

## 🎨 Design System

### Colors
- **Primary**: `#58a6ff` (Blue)
- **Success**: `#3fb950` (Green)
- **Error**: `#f85149` (Red)
- **Warning**: `#d29922` (Orange)
- **Background**: `#0d1117` (Dark)

### Components

#### Status Badges
```html
<span class="status-badge created">CREATED</span>
<span class="status-badge running">RUNNING</span>
<span class="status-badge completed">COMPLETED</span>
```

#### Cards & Panels
All major sections use glassmorphic cards with backdrop blur effect.

#### Buttons
- **Primary** (Blue): Main actions
- **Secondary** (Outlined): Secondary actions
- **Icon** (Minimal): Utility buttons

## 📱 Responsive Design

The UI is fully responsive and works on:
- ✅ Desktop (1920px+)
- ✅ Tablet (768px - 1024px)
- ✅ Mobile (320px - 768px)

## 🎬 Animations

| Animation | Where | Duration |
|-----------|-------|----------|
| Fade In | Tab transitions | 400ms |
| Slide Up | Card entrance | 400ms |
| Pulse | Status indicators | 2s loop |
| Glow | Button hover | 300ms |
| Spinner | Logo | 3s loop |
| Toast Slide | Notifications | 300ms |

## 🔧 Customization

### Change Theme Colors

Edit `:root` variables in `css/styles.css`:

```css
:root {
    --accent-primary: #58a6ff;  /* Change blue color */
    --success: #3fb950;          /* Change green color */
    --error: #f85149;            /* Change red color */
    /* ... more variables */
}
```

### Modify Animations

Adjust transition timing in `css/styles.css`:

```css
--transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);  /* Main transition */
--transition-fast: all 0.15s ease;                     /* Fast transitions */
```

## 📊 State Management

The app uses a simple state object in `app.js`:

```javascript
const state = {
    processes: [],           // Array of process objects
    currentProcess: null,    // Currently selected process
    stats: {
        totalProcesses: 0,
        activeThreads: 0,
        completedTasks: 0,
        errors: 0
    }
};
```

## 🐛 Debugging

### Enable Debug Mode

Open browser console and use:

```javascript
debugState()  // Logs current state to console
```

### Monitor Activity

Check the Activity Feed on the Dashboard tab for all system events.

## ⚡ Performance

- **CSS**: Optimized with GPU acceleration (transform, opacity)
- **JavaScript**: Minimal DOM manipulation, event delegation
- **Animations**: Hardware-accelerated with will-change
- **Scrolling**: Smooth scroll behavior enabled
- **Bundle Size**: Single file, no external dependencies

## 🌐 Browser Support

- ✅ Chrome/Chromium 90+
- ✅ Firefox 88+
- ✅ Safari 14+
- ✅ Edge 90+

## 📚 Code Structure

### HTML (`index.html`)
- Header with navigation
- Three tabs: Dashboard, Processes, Scheduler
- Three modals: Create Process, Add Thread, Execution Output
- Toast notification container

### CSS (`css/styles.css`)
- Root color variables
- Component styles (cards, buttons, modals)
- Animation definitions
- Responsive media queries
- Dark theme with glassmorphism

### JavaScript (`js/app.js`)
- Initialization & event setup
- Modal management
- Tab navigation
- Process CRUD operations
- Thread management
- Execution simulation
- Activity feed management
- Toast notifications
- API integration points

## 🎓 Learning Resources

### Glassmorphism
Modern design technique using:
- Semi-transparent backgrounds
- Backdrop blur filter
- Subtle borders
- Soft shadows

### CSS Grid & Flexbox
- Dashboard uses CSS Grid (responsive auto-fit)
- Cards use Flexbox for internal layout
- Modals centered with Flexbox

### Animations
- CSS keyframe animations for entrance effects
- CSS transitions for hover states
- JavaScript-triggered animations

## 🚀 Future Enhancements

Potential features to add:
- [ ] WebSocket support for real-time updates
- [ ] Process execution history graph
- [ ] Custom theme selector
- [ ] Export process logs as JSON/TXT
- [ ] Drag-drop process reordering
- [ ] Process templates
- [ ] Advanced filtering & search
- [ ] System performance graphs

## 📝 License

Part of the Mini OS Educational Project

## 🤝 Integration Notes

### For Backend Developers

1. **CORS**: Enable CORS for the UI origin
2. **JSON Response**: Return JSON, not XML
3. **Status Codes**: Use proper HTTP status codes
4. **Errors**: Return `{ error: "message" }` on failure
5. **Real-time**: Consider WebSocket for live updates
6. **Authentication**: Add token auth if needed

### For Frontend Developers

The API integration is in `js/app.js`. Key functions:
- `handleCreateProcess()` - Creates process via API
- `handleAddThread()` - Adds thread via API
- `runProcess()` - Executes process via API
- `simulateExecutionOutput()` - Shows live output

All use `fetch()` API, replace with your backend URL.

## 💬 Support

For issues with the UI:
1. Check browser console for errors
2. Verify API endpoint is correct
3. Check CORS headers
4. Run `debugState()` in console
5. Check network tab in DevTools

---

**Happy Process Simulating!** 🎉
