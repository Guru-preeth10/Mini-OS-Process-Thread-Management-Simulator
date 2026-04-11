import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import os.backend.AppBackend;
import os.exception.InvalidProcessException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class WebServerMain {

    private static final int PORT = 8080;

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        System.out.println("Starting Web API server on port " + PORT);

        server.createContext("/api/createProcess", new CreateProcessHandler());
        server.createContext("/api/addThread", new AddThreadHandler());
        server.createContext("/api/runProcess", new RunProcessHandler());
        server.createContext("/api/runScheduler", new RunSchedulerHandler());
        server.createContext("/api/getProcesses", new GetProcessesHandler());
        server.createContext("/api/getSchedulerInfo", new GetSchedulerHandler());

        server.setExecutor(null);
        server.start();
    }

    static String readBody(InputStream is) throws IOException {
        byte[] buf = is.readAllBytes();
        return new String(buf, StandardCharsets.UTF_8);
    }

    static void writeJson(HttpExchange exchange, int status, String json) throws IOException {
        byte[] resp = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
        // CORS - allow local frontend to call
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        exchange.sendResponseHeaders(status, resp.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(resp);
        }
    }

    static class CreateProcessHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // handle CORS preflight
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
                exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                writeJson(exchange, 405, "{\"error\":\"Method Not Allowed\"}");
                return;
            }

            String body = readBody(exchange.getRequestBody());
            // very small JSON parsing: look for "name":"..."
            String name = extractString(body, "name");
            String pr = extractString(body, "priority");
            if (name == null || name.isBlank()) {
                writeJson(exchange, 400, "{\"error\":\"Missing name\"}");
                return;
            }

            AppBackend backend = AppBackend.getInstance();
            try {
                var p = backend.createProcess(name, pr);
                String json = String.format("{\"id\":%d,\"name\":\"%s\"}", p.getProcessId(), p.getProcessName());
                writeJson(exchange, 200, json);
            } catch (Exception e) {
                writeJson(exchange, 500, "{\"error\":\"Internal server error\"}");
            }
        }
    }

    static class AddThreadHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // handle CORS preflight
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
                exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                writeJson(exchange, 405, "{\"error\":\"Method Not Allowed\"}");
                return;
            }

            String body = readBody(exchange.getRequestBody());
            int processId = extractInt(body, "processId");
            String taskType = extractString(body, "taskType");
            String threadPriority = extractString(body, "priority");

            AppBackend backend = AppBackend.getInstance();
            boolean ok = false;
            try {
                if ("PrintTask".equalsIgnoreCase(taskType)) {
                    String message = extractString(body, "message");
                    int iterations = extractInt(body, "iterations");
                    ok = backend.addPrintThread(processId, message, iterations, threadPriority);
                } else if ("CalculationTask".equalsIgnoreCase(taskType)) {
                    int num1 = extractInt(body, "num1");
                    int num2 = extractInt(body, "num2");
                    String operator = extractString(body, "operator");
                    ok = backend.addCalculationThread(processId, num1, num2, operator, threadPriority);
                }
            } catch (InvalidProcessException e) {
                writeJson(exchange, 400, String.format("{\"error\":\"%s\"}", e.getMessage()));
                return;
            }

            if (ok) writeJson(exchange, 200, "{\"status\":\"ok\"}");
            else writeJson(exchange, 404, "{\"error\":\"Process not found\"}");
        }
    }

    static class RunProcessHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // handle CORS preflight
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
                exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                writeJson(exchange, 405, "{\"error\":\"Method Not Allowed\"}");
                return;
            }
            String body = readBody(exchange.getRequestBody());
            int processId = extractInt(body, "processId");

            AppBackend backend = AppBackend.getInstance();
            try {
                boolean ok = backend.runProcess(processId);
                if (ok) writeJson(exchange, 200, "{\"status\":\"started\"}");
                else writeJson(exchange, 404, "{\"error\":\"Process not found\"}");
            } catch (InvalidProcessException e) {
                writeJson(exchange, 400, String.format("{\"error\":\"%s\"}", e.getMessage()));
            }
        }
    }

    static class RunSchedulerHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                writeJson(exchange, 405, "{\"error\":\"Method Not Allowed\"}");
                return;
            }

            AppBackend backend = AppBackend.getInstance();
            // run scheduler in background thread to avoid blocking
            new Thread(() -> {
                backend.runScheduler();
            }).start();

            writeJson(exchange, 200, "{\"status\":\"scheduler_started\"}");
        }
    }

    static class GetProcessesHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // handle CORS preflight
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
                exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                writeJson(exchange, 405, "{\"error\":\"Method Not Allowed\"}");
                return;
            }
            AppBackend backend = AppBackend.getInstance();
            List<?> list = backend.getProcesses();
            StringBuilder sb = new StringBuilder();
            sb.append('[');
            boolean first = true;
            for (var o : list) {
                var p = (os.process.Process) o;
                if (!first) sb.append(',');
                first = false;
                sb.append(String.format("{\"id\":%d,\"name\":\"%s\",\"status\":\"%s\",\"threads\":%d,\"priority\":\"%s\"}",
                    p.getProcessId(), escapeJson(p.getProcessName()), p.getState().name(), p.getThreadCount(), p.getPriority().name()));
            }
            sb.append(']');
            writeJson(exchange, 200, sb.toString());
        }
    }

    static class GetSchedulerHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // handle CORS preflight
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
                exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                writeJson(exchange, 405, "{\"error\":\"Method Not Allowed\"}");
                return;
            }
            AppBackend backend = AppBackend.getInstance();
            var s = backend.getScheduler();
            String json = String.format("{\"name\":\"%s\",\"scheduledCount\":%d}", escapeJson(s.getSchedulerName()), s.getScheduledProcessCount());
            writeJson(exchange, 200, json);
        }
    }

    // Very small helpers: naive JSON extraction
    static String extractString(String body, String key) {
        String pattern = '"' + key + '"';
        int idx = body.indexOf(pattern);
        if (idx == -1) return null;
        int colon = body.indexOf(':', idx);
        if (colon == -1) return null;
        int firstQuote = body.indexOf('"', colon + 1);
        if (firstQuote == -1) return null;
        int secondQuote = body.indexOf('"', firstQuote + 1);
        if (secondQuote == -1) return null;
        return body.substring(firstQuote + 1, secondQuote);
    }

    static int extractInt(String body, String key) {
        String pattern = '"' + key + '"';
        int idx = body.indexOf(pattern);
        if (idx == -1) return -1;
        int colon = body.indexOf(':', idx);
        if (colon == -1) return -1;
        // read until comma or brace
        int start = colon + 1;
        while (start < body.length() && Character.isWhitespace(body.charAt(start))) start++;
        int end = start;
        while (end < body.length() && (Character.isDigit(body.charAt(end)) || body.charAt(end)=='-')) end++;
        try {
            return Integer.parseInt(body.substring(start, end));
        } catch (Exception e) {
            return -1;
        }
    }

    static String escapeJson(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
