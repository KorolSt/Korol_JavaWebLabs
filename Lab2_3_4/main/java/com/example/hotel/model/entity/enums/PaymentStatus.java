package com.example.hotel.model.entity.enums;

public enum PaymentStatus {
    PAID, FAILED, WAITING, CANCELED;

    public String getName() {
        return name().toLowerCase();
    }
}
