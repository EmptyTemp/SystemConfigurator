package com.empty;

import java.sql.*;
import java.util.Scanner;
import java.io.OutputStream;
import java.io.PrintStream;

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
            // 0: CPU threshold (%) - from 50% to 100%
            new SettingValidator(value -> value >= 50 && value <= 100, "Must be between 50% and 100%"),

            // 1: RAM Threshold (%) - from 50% to 100%
            new SettingValidator(value -> value >= 50 && value <= 100, "Must be between 50% and 100%"),

            // 2: Disk threshold (%) - from 50% to 100%
            new SettingValidator(value -> value >= 50 && value <= 100, "Must be between 50% and 100%"),
    };

    // Method to get setting name
    private static String getSettingName(int index) {
        String[] settingNames = {
                "CPU threshold",
                "RAM Threshold",
                "Disk threshold"
        };
        return settingNames[index];
    }

    private static Connection getConnection() throws SQLException {
        String url = "jdbc:sqlite:monitoring.db";
        return DriverManager.getConnection(url);
    }

    private static void initializeDatabase() throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS settings (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name VARCHAR(50) UNIQUE NOT NULL,
                    value INTEGER NOT NULL,
                    min_value INTEGER NOT NULL,
                    max_value INTEGER NOT NULL,
                    description TEXT)
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO settings (name, value, min_value, max_value, description)
                VALUES
                ('cpu_threshold', 80, 50, 100, 'CPU usage threshold (%)'),
                ('ram_threshold', 85, 50, 100, 'RAM usage threshold (%)'),
                ('disk_threshold', 90, 50, 100, 'Disk usage threshold (%)')
                """);
        }
    }

    private static void loadConfigFromDB(int[] values) throws SQLException {
        String[] settingNames = {"cpu_threshold", "ram_threshold", "disk_threshold"};

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT value FROM settings WHERE name = ?")) {

            for (int i = 0; i < settingNames.length; i++) {
                stmt.setString(1, settingNames[i]);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    values[i] = rs.getInt("value");
                }
            }
        }
    }

    private static void saveConfigToDB(String settingName, int value) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE settings SET value = ? WHERE name = ?")) {

            stmt.setInt(1, value);
            stmt.setString(2, settingName);
            stmt.executeUpdate();
        }
    }

    public static void main(String[] args){
        // REDIRECT SLF4J MESSAGES TO NOWHERE
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(new OutputStream() {
            public void write(int b) {
                // DO NOTHING - COMPLETE SILENCE
            }
        }));

        try {
            Scanner sc = new Scanner(System.in);
            int[] values = new int[3];
            int[] defaultValues = {80, 85, 90};
            System.arraycopy(defaultValues, 0, values, 0, values.length);

            String[] settingNames = {
                    "CPU threshold (%)",
                    "RAM Threshold (%)",
                    "Disk threshold (%)"
            };

            try {
                initializeDatabase();
            } catch (SQLException e) {
                System.out.println(Colors.RED + "Database initialization failed: " + e.getMessage() + Colors.RESET);
                return;
            }

            System.out.println(Colors.WHITE + Colors.BOLD + "\n" +
                    "╔══════════════════════════════════════╗\n" +
                    "║          SYSTEM CONFIGURATOR         ║\n" +
                    "║       Settings Management Tool       ║\n" +
                    "╚══════════════════════════════════════╝" + Colors.RESET + "\n");

            try {
                loadConfigFromDB(values);
                System.out.println(Colors.GREEN + "Loaded configuration from database" + Colors.RESET);
            } catch (SQLException e) {
                System.out.println(Colors.RED + "Database error: " + e.getMessage() + Colors.RESET);
                System.out.println(Colors.CYAN + "Using default settings" + Colors.RESET);
            }

            // Configuration menu loop with improved validation
            String[] dbSettingNames = {"cpu_threshold", "ram_threshold", "disk_threshold"};

            for (int index = 0; index < dbSettingNames.length; index++) {
                while (true) {
                    System.out.print(Colors.BLUE + settingNames[index] + Colors.RESET +
                            Colors.YELLOW + " (current: " + values[index] + "): " + Colors.RESET);

                    String input = sc.nextLine().trim();

                    if (input.isEmpty()) {
                        System.out.println(Colors.PURPLE + "Empty input! Using current value: " + values[index] + Colors.RESET);
                        break;
                    }

                    try {
                        // Use long for handling large numbers
                        long longValue = Long.parseLong(input);

                        // Check if number fits in int
                        if (longValue < Integer.MIN_VALUE || longValue > Integer.MAX_VALUE) {
                            System.out.println(Colors.RED + "Number is too large or too small! Must be between " +
                                    Integer.MIN_VALUE + " and " + Integer.MAX_VALUE + Colors.RESET);
                            continue;
                        }

                        int newValue = (int) longValue;

                        // Value validation
                        if (validators[index].isValid(newValue)) {
                            values[index] = newValue;
                            break;
                        } else {
                            System.out.println(Colors.RED + "Invalid value! " + validators[index].errorMessage() + Colors.RESET);
                            System.out.println(Colors.CYAN + "Hint: Valid range for " + getSettingName(index) + Colors.RESET);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println(Colors.RED + "Error! Please enter a valid integer number." + Colors.RESET);
                    }
                }
            }

            // Save configuration to database
            try {
                for (int i = 0; i < dbSettingNames.length; i++) {
                    saveConfigToDB(dbSettingNames[i], values[i]);
                }
                System.out.println(Colors.GREEN + Colors.BOLD + "Settings saved to database" + Colors.RESET);
            } catch (SQLException e) {
                System.out.println(Colors.RED + "Save error: " + e.getMessage() + Colors.RESET);
            }

            sc.close();

        } finally {
            System.setErr(originalErr); // Restore System.err when done
        }
    }
}