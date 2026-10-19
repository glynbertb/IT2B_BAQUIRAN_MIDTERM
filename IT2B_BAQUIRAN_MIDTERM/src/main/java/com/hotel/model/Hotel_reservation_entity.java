package com.hotel.model;

/**
 * Hotel reservation record (5 attributes).
 * id is 0 for a new reservation that has not been saved yet.
 */
public record Hotel_reservation_entity(int id,
                                       String guestName,
                                       String roomType,
                                       double nightlyRate,
                                       String paymentStatus) {
}
