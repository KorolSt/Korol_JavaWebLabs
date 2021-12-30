package com.example.hotel.model.entity.enums;


public enum ReservationStatus {
    PROCESSING, CANCELED, CONFIRMED;

    public String getName() {
        return name().toLowerCase();
    }
}
