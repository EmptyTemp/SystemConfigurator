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

    // Simple interface for validation
    @FunctionalInterface
    private interface ValidationRule {
        boolean isValid(long value);
    }

    // Record class instead of regular class (more compact)
    private record SettingValidator(ValidationRule rule, String errorMessage) {
        public boolean isValid(long value) {
            return rule.isValid(value);
        }
    }

    // Array of validators with correct ranges
    private static final SettingValidator[] validators = {
            // 0: Monitoring interval (sec) - from 10 sec to 1 hour
            new SettingValidator(value -> value >= 10 && value <= 3600, "Must be between 10 and 3600 seconds"),

            // 1: CPU threshold (%) - from 50% to 100%
            new SettingValidator(value -> value >= 50 && value <= 100, "Must be between 50% and 100%"),

            // 2: RAM Threshold (%) - from 50% to 100%
            new SettingValidator(value -> value >= 50 && value <= 100, "Must be between 50% and 100%"),

            // 3: Disk threshold (%) - from 50% to 100%
            new SettingValidator(value -> value >= 50 && value <= 100, "Must be between 50% and 100%"),

            // 4: Log retention (days) - from 1 to 365 days
            new SettingValidator(value -> value >= 1 && value <= 365, "Must be between 1 and 365 days"),

            // 5: Network timeout (ms) - from 100 ms to 30 sec
            new SettingValidator(value -> value >= 100 && value <= 30000, "Must be between 100 and 30000 milliseconds"),

            // 6: Max connections - from 1 to 10000
            new SettingValidator(value -> value >= 1 && value <= 10000, "Must be between 1 and 10000 connections"),

            // 7: Logging level (1-5) - only 1-5
            new SettingValidator(value -> value >= 1 && value <= 5, "Must be between 1 and 5"),

            // 8: Backup interval (hours) - from 1 to 720 hours (30 days)
            new SettingValidator(value -> value >= 1 && value <= 720, "Must be between 1 and 720 hours"),

            // 9: Log size limit (MB) - from 1 to 1024 MB
            new SettingValidator(value -> value >= 1 && value <= 1024, "Must be between 1 and 1024 MB"),

            // 10: Web interface port - standard ports 1024-65535
            new SettingValidator(value -> value >= 1024 && value <= 65535, "Must be between 1024 and 65535"),

            // 11: Auth timeout (sec) - from 10 to 3600 sec
            new SettingValidator(value -> value >= 10 && value <= 3600, "Must be between 10 and 3600 seconds"),

            // 12: Cache size (MB) - from 16 to 4096 MB
            new SettingValidator(value -> value >= 16 && value <= 4096, "Must be between 16 and 4096 MB"),

            // 13: Sync interval (min) - from 1 to 1440 minutes (24 hours)
            new SettingValidator(value -> value >= 1 && value <= 1440, "Must be between 1 and 1440 minutes"),

            // 14: Error threshold - from 1 to 1000 errors
            new SettingValidator(value -> value >= 1 && value <= 1000, "Must be between 1 and 1000 errors"),

            // 15: Config version - only positive numbers
            new SettingValidator(value -> value >= 1, "Must be positive number")
    };

    private static Path getUniversalConfigPath() throws IOException {
        String userHome = System.getProperty("user.home");
        Path desktopPath = Paths.get(userHome, "Desktop");
        Path configDir = desktopPath.resolve("config");

        if (!Files.exists(configDir)) {
            Files.createDirectories(configDir);
        }

        return configDir;
    }

    private static void loadConfig(int[] values, Path configPath) throws IOException {
        if (!Files.exists(configPath)) return;

        List<String> lines = Files.readAllLines(configPath);
        for (String line : lines) {
            if (line.startsWith("line_")) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    try {
                        String numberStr = parts[0].substring(5);
                        int index = Integer.parseInt(numberStr) - 1;

                        if (index >= 0 && index < values.length) {
                            int loadedValue = Integer.parseInt(parts[1].trim());

                            // Validate loaded values
                            if (validators[index].isValid(loadedValue)) {
                                values[index] = loadedValue;
                            } else {
                                System.out.println(Colors.YELLOW + "⚠️  Invalid value in config for " +
                                        getSettingName(index) + ": " + loadedValue + Colors.RESET);
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println(Colors.RED + "❌ Config syntax error in line: " + line + Colors.RESET);
                    }
                }
            }
        }
    }

    // Method to get setting name
    private static String getSettingName(int index) {
        String[] settingNames = {
                "Monitoring interval", "CPU threshold", "RAM Threshold", "Disk threshold",
                "Log retention", "Network timeout", "Max connections", "Logging level",
                "Backup interval", "Log size limit", "Web interface port", "Auth timeout",
                "Cache size", "Sync interval", "Error threshold", "Config version"
        };
        return settingNames[index];
    }

    public static void main(String[] args) throws IOException {
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

        Path configPath = getUniversalConfigPath().resolve("config.txt");

        System.out.println(Colors.WHITE + Colors.BOLD + "\n" +
                "╔══════════════════════════════════════╗\n" +
                "║          SYSTEM CONFIGURATOR         ║\n" +
                "║       Settings Management Tool       ║\n" +
                "╚══════════════════════════════════════╝" + Colors.RESET + "\n");

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

        // Configuration menu loop with improved validation
        for (int index = 0; index < 16; index++) {
            while (true) {
                System.out.print(Colors.BLUE + settingNames[index] + Colors.RESET +
                        Colors.YELLOW + " (current: " + values[index] + "): " + Colors.RESET);

                String input = sc.nextLine().trim();

                if (input.isEmpty()) {
                    System.out.println(Colors.PURPLE + "⚠️  Empty input! Using current value: " + values[index] + Colors.RESET);
                    break;
                }

                try {
                    // Use long for handling large numbers
                    long longValue = Long.parseLong(input);

                    // Check if number fits in int
                    if (longValue < Integer.MIN_VALUE || longValue > Integer.MAX_VALUE) {
                        System.out.println(Colors.RED + "❌ Number is too large or too small! Must be between " +
                                Integer.MIN_VALUE + " and " + Integer.MAX_VALUE + Colors.RESET);
                        continue;
                    }

                    int newValue = (int) longValue;

                    // Value validation
                    if (validators[index].isValid(newValue)) {
                        values[index] = newValue;
                        break;
                    } else {
                        System.out.println(Colors.RED + "❌ Invalid value! " + validators[index].errorMessage() + Colors.RESET);
                        System.out.println(Colors.CYAN + "💡 Hint: Valid range for " + getSettingName(index) + Colors.RESET);
                    }
                } catch (NumberFormatException e) {
                    System.out.println(Colors.RED + "❌ Error! Please enter a valid integer number." + Colors.RESET);
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