package com.hotel;

import com.hotel.dao.DatabaseConnection;
import com.hotel.dao.ScenarioDao;
import com.hotel.ui.Reservation_Menu;

/** Application entry point. */
public class Main {
    public static void main(String[] args) {
        try {
            DatabaseConnection.initializeDatabase();
        } catch (IllegalStateException e) {
            System.out.println("FATAL: " + e.getMessage());
            return;
        }
        new Reservation_Menu(new ScenarioDao()).run();
    }
}
