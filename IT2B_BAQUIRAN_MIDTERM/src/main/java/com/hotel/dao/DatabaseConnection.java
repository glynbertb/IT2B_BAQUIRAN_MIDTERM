package com.hotel.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/** Opens SQLite connections and prepares the schema. */
public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:scenario.db";

    private static final String PRAGMA_SQL = "PRAGMA foreign_keys = ON;";

    private static final String CREATE_TABLE_SQL = """
            CREATE TABLE IF NOT EXISTS reservations (
                id             INTEGER PRIMARY KEY AUTOINCREMENT,
                guest_name     TEXT    NOT NULL,
                room_type      TEXT    NOT NULL CHECK (room_type IN ('SINGLE','DOUBLE','SUITE')),
                nightly_rate   REAL    NOT NULL,
                payment_status TEXT    NOT NULL CHECK (payment_status IN ('PAID','PENDING'))
            )
            """;

    private DatabaseConnection() {
    }

    /** Opens a connection with foreign keys enabled. Callers must close it. */
    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        try (PreparedStatement ps = conn.prepareStatement(PRAGMA_SQL)) {
            ps.execute();
        } catch (SQLException e) {
            conn.close();
            throw e;
        }
        return conn;
    }

    /** Creates the reservations table if it does not exist yet. */
    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(CREATE_TABLE_SQL)) {
            ps.execute();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot initialize database: " + e.getMessage(), e);
        }
    }
}
