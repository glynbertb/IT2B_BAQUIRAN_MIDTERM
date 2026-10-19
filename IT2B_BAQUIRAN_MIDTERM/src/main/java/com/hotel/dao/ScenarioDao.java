package com.hotel.dao;

import com.hotel.model.Hotel_reservation_entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * All SQL lives here. Every query uses PreparedStatement parameters.
 * SQLException is wrapped in IllegalStateException so the UI never touches JDBC types.
 */
public class ScenarioDao {

    private static final String INSERT_SQL =
            "INSERT INTO reservations (guest_name, room_type, nightly_rate, payment_status) VALUES (?, ?, ?, ?)";

    private static final String SELECT_ALL_SQL =
            "SELECT id, guest_name, room_type, nightly_rate, payment_status FROM reservations ORDER BY id";

    public void insert(Hotel_reservation_entity reservation) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {
            ps.setString(1, reservation.guestName());
            ps.setString(2, reservation.roomType());
            ps.setDouble(3, reservation.nightlyRate());
            ps.setString(4, reservation.paymentStatus());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Could not save reservation: " + e.getMessage(), e);
        }
    }

    public List<Hotel_reservation_entity> findAll() {
        List<Hotel_reservation_entity> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Hotel_reservation_entity(
                        rs.getInt("id"),
                        rs.getString("guest_name"),
                        rs.getString("room_type"),
                        rs.getDouble("nightly_rate"),
                        rs.getString("payment_status")));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Could not load reservations: " + e.getMessage(), e);
        }
        return list;
    }
}
