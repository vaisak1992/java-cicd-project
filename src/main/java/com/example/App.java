
package com.example;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class App {

    public static double add(double a, double b) {
        return a + b;
    }

    public static double subtract(double a, double b) {
        return a - b;
    }

    public static double multiply(double a, double b) {
        return a * b;
    }

    public static double divide(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("Cannot divide by zero");
        }
        return a / b;
    }

    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(
            new InetSocketAddress("0.0.0.0", 8080), 0
        );

        server.createContext("/", exchange -> {

            String html = """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Java Calculator</title>
                    <style>
                        body {
                            font-family: Arial;
                            background: #f0f2f5;
                            text-align: center;
                            padding-top: 60px;
                        }
                        .calculator {
                            background: white;
                            padding: 30px;
                            width: 320px;
                            margin: auto;
                            border-radius: 12px;
                            box-shadow: 0 4px 12px #ccc;
                        }
                        input, select, button {
                            width: 100%;
                            padding: 12px;
                            margin: 10px 0;
                            box-sizing: border-box;
                        }
                        button {
                            background: #2563eb;
                            color: white;
                            border: none;
                            cursor: pointer;
                        }
                    </style>
                </head>
                <body>
                    <div class="calculator">
                        <h2>Java Calculator</h2>
                        <form action="/calculate" method="get">
                            <input type="number" step="any"
                                name="a" placeholder="First number" required>
                            <input type="number" step="any"
                                name="b" placeholder="Second number" required>
                            <select name="operation">
                                <option value="add">Addition (+)</option>
                                <option value="subtract">Subtraction (-)</option>
                                <option value="multiply">Multiplication (*)</option>
                                <option value="divide">Division (/)</option>
                            </select>
                            <button type="submit">Calculate</button>
                        </form>
                    </div>
                </body>
                </html>
                """;

            String response = html;

            if (exchange.getRequestURI().getPath().equals("/calculate")) {
                try {
                    String query = exchange.getRequestURI().getRawQuery();
                    String[] params = query.split("&");

                    double a = 0;
                    double b = 0;
                    String operation = "";

                    for (String param : params) {
                        String[] pair = param.split("=", 2);
                        String key = URLDecoder.decode(
                            pair[0], StandardCharsets.UTF_8
                        );
                        String value = URLDecoder.decode(
                            pair[1], StandardCharsets.UTF_8
                        );

                        switch (key) {
                            case "a" -> a = Double.parseDouble(value);
                            case "b" -> b = Double.parseDouble(value);
                            case "operation" -> operation = value;
                        }
                    }

                    double result = switch (operation) {
                        case "add" -> add(a, b);
                        case "subtract" -> subtract(a, b);
                        case "multiply" -> multiply(a, b);
                        case "divide" -> divide(a, b);
                        default -> throw new IllegalArgumentException(
                            "Invalid operation"
                        );
                    };

                    response = "<html><body style='font-family:Arial;text-align:center;padding:60px'>"
                        + "<h1>Calculator Result</h1>"
                        + "<h2>Result: " + result + "</h2>"
                        + "<a href='/'>Back to Calculator</a>"
                        + "</body></html>";

                } catch (Exception e) {
                    response = "<html><body style='font-family:Arial;text-align:center;padding:60px'>"
                        + "<h2>Error: " + e.getMessage() + "</h2>"
                        + "<a href='/'>Back to Calculator</a>"
                        + "</body></html>";
                }
            }

            exchange.getResponseHeaders().set(
                "Content-Type", "text/html; charset=UTF-8"
            );
            byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, bytes.length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        });

        server.start();
        System.out.println("Calculator running on port 8080");
    }
}
