package com.hotel.ui;

import com.hotel.dao.ScenarioDao;
import com.hotel.model.Hotel_reservation_entity;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/** Interactive console menu. No SQL or JDBC code in this class. */
public class Reservation_Menu {

    private static final String LINE = "=".repeat(54);
    private static final String WIDE_LINE = "=".repeat(88);
    private static final String TABLE_BORDER =
            "+----+--------------------+------------+--------------+----------------+";

    private final ScenarioDao dao;
    private final Scanner scanner = new Scanner(System.in);

    public Reservation_Menu(ScenarioDao dao) {
        this.dao = dao;
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1 -> addReservation();
                case 2 -> viewReservations();
                case 3 -> {
                    System.out.println("\nExiting system... Thank you!");
                    running = false;
                }
                default -> System.out.println("\nERROR: Invalid choice. Please enter 1, 2, or 3.\n");
            }
        }
        scanner.close();
    }

    private void printMenu() {
        System.out.println("================================================");
        System.out.println("          HOTEL ROOM RESERVATION SYSTEM");
        System.out.println("================================================");
        System.out.println("[1] Add New Reservation");
        System.out.println("[2] View All Reservations");
        System.out.println("[3] Exit System");
        System.out.println("================================================");
    }

    private void addReservation() {
        System.out.println("\n--- ADD NEW RESERVATION ---");

        String name = readNonEmpty("Enter Guest Name: ");
        String roomType = readChoice("Enter Room Type (SINGLE / DOUBLE / SUITE): ",
                "SINGLE", "DOUBLE", "SUITE");
        double rate = readPositiveDouble("Enter Nightly Rate: ");
        String payment = readChoice("Enter Payment Status (PAID / PENDING): ",
                "PAID", "PENDING");

        try {
            dao.insert(new Hotel_reservation_entity(0, name, roomType, rate, payment));
            System.out.println("\nSUCCESS: Reservation recorded in database!\n");
        } catch (IllegalStateException e) {
            System.out.println("\nERROR: " + e.getMessage() + "\n");
        }
    }

    private void viewReservations() {
        try {
            List<Hotel_reservation_entity> all = dao.findAll();

            System.out.println("========================================================================");
            System.out.println( "                        ALL ROOM RESERVATIONS                          ");
            System.out.println("========================================================================");
            System.out.println(TABLE_BORDER);
            System.out.printf("| %-2s | %-18s | %-10s | %-12s | %-14s |%n",
                    "ID", "GUEST NAME", "ROOM TYPE", "NIGHTLY RATE", "PAYMENT");
            System.out.println(TABLE_BORDER);

            for (Hotel_reservation_entity r : all) {
                System.out.printf(Locale.US, "| %-2d | %-18s | %-10s | %-12.2f | %-14s |%n",
                        r.id(), r.guestName(), r.roomType(), r.nightlyRate(), r.paymentStatus());
            }

            System.out.println(TABLE_BORDER);
            System.out.println("Total Records Displayed: " + all.size() + "\n");
        } catch (IllegalStateException e) {
            System.out.println("\nERROR: " + e.getMessage() + "\n");
        }
    }

    // ---------- input helpers (all use nextLine, so no leftover line breaks) ----------

    private int readInt(String prompt) {
        System.out.print(prompt);
        String line = scanner.nextLine().trim();
        try {
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                return line;
            }
            System.out.println("ERROR: This field cannot be empty.");
        }
    }

    private double readPositiveDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                double value = Double.parseDouble(line);
                if (value > 0) {
                    return value;
                }
                System.out.println("ERROR: Rate must be greater than 0.");
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Please enter a valid number (e.g., 2500.50).");
            }
        }
    }

    private String readChoice(String prompt, String... allowed) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim().toUpperCase();
            for (String option : allowed) {
                if (option.equals(line)) {
                    return option;
                }
            }
            System.out.println("ERROR: Invalid value. Allowed: " + String.join(", ", allowed));
        }
    }
}
