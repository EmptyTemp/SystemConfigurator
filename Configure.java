package com.empty;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Configure {
    public static class Colors {
        public static final String RESET = "\u001B[0m";
        public static final String RED = "\u001B[31m";
        public static final String GREEN = "\u001B[32m";
        public static final String YELLOW = "\u001B[33m";
        public static final String BLUE = "\u001B[34m";
        public static final String PURPLE = "\u001B[35m";
        public static final String CYAN = "\u001B[36m";
        public static final String WHITE = "\u001B[37m";
        public static final String BOLD = "\u001B[1m";
    }

    private static Path getUniversalConfigPath() throws IOException {
        String userHome = System.getProperty("user.home");
        Path desktopPath = Paths.get(userHome, "Desktop");
        Path configDir = desktopPath.resolve("config");

        // Ensure config directory exists
        if (!Files.exists(configDir)) {
            Files.createDirectories(configDir);
        }

        return configDir;
    }

    private static void loadConfig(int[] values, Path configPath) throws IOException {
        // Skip if config file doesn't exist
        if (!Files.exists(configPath)) return;

        List<String> lines = Files.readAllLines(configPath);
        for (String line : lines) {
            if (line.startsWith("line_")) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    try {
                        // Extract setting index form "line_X" format
                        String numberStr = parts[0].substring(5); // "line_13" -> "13"
                        int index = Integer.parseInt(numberStr) - 1;

                        // Validate index bounds before assignment
                        if (index >= 0 && index < values.length) {
                            values[index] = Integer.parseInt(parts[1].trim());
                        }
                    } catch (NumberFormatException e) {
                        System.out.println(Colors.RED + "Config syntax error in line: " + line + Colors.RESET);
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {

        // Create index
        Scanner sc = new Scanner(System.in);
        int[] values = new int[16];
        int[] defaultValues = {300, 80, 85, 90, 30, 5000, 100, 3, 24, 100, 8080, 30, 512, 5, 10, 1};
        System.arraycopy(defaultValues, 0, values, 0, values.length);

        String[] settingNames = {
                "📊 Monitoring interval (sec)", "⚡ CPU threshold (%)", "💾 RAM Threshold (%)",
                "💿 Disk threshold (%)", "📁 Log retention (days)", "🌐 Network timeout (ms)",
                "🔗 Max connections", "📝 Logging level (1-5)", "💾 Backup interval (hours)",
                "🗃️ Log size limit (MB)", "🌍 Web interface port", "🔐 Auth timeout (sec)",
                "🚀 Cache size (MB)", "🔄 Sync interval (min)", "⚠️ Error threshold",
                "🔢 Config version"
        };

        // Build config file path
        Path configPath = getUniversalConfigPath().resolve("config.txt");

        System.out.println(Colors.WHITE + Colors.BOLD + "\n" +
                "╔══════════════════════════════════════╗\n" +
                "║          SYSTEM CONFIGURATOR         ║\n" +
                "║       Settings Management Tool       ║\n" +
                "╚══════════════════════════════════════╝" + Colors.RESET + "\n");

        // Load existing config or use defaults
        if (Files.exists(configPath)) {
            try {
                loadConfig(values, configPath);
                System.out.println(Colors.GREEN + "✅ Loaded existing configuration" + Colors.RESET);
            } catch (IOException e) {
                System.out.println(Colors.RED + "❌ Config loading error, using defaults" + Colors.RESET);
            }
        } else {
            System.out.println(Colors.CYAN + "📝 Config not found, using default settings" + Colors.RESET);
        }

        // Configuration menu loop
        for (int index = 0; index < 16; index++) {
            while (true) {
                System.out.print(Colors.BLUE + settingNames[index] + Colors.RESET + Colors.YELLOW + " (current: " + values[index] + "): " + Colors.RESET);

                String input = sc.nextLine().trim(); // Read full line

                if (input.isEmpty()) {
                    System.out.println(Colors.PURPLE + "⚠️  Empty input! Using current value: " + values[index] + Colors.RESET);
                    break; // Keep current value
                }

                try {
                    int newValue = Integer.parseInt(input);
                    values[index] = newValue;
                    break;
                } catch (NumberFormatException e) {
                    System.out.println(Colors.RED + "❌ Error! Please enter an integer." + Colors.RESET);
                }
            }
        }

        // Save configuration to file
        try (PrintWriter writer = new PrintWriter(configPath.toFile())){
            for (int index = 0; index < values.length; index++) {
                writer.println("line_" + (index + 1) + "=" + values[index]);
            }
            System.out.println(Colors.GREEN + Colors.BOLD + "💾 Settings saved to " + configPath + Colors.RESET);
        } catch (FileNotFoundException e) {
            System.out.println(Colors.RED + "❌ Save error: " + e.getMessage() + Colors.RESET);
        }

        sc.close();
    }
}