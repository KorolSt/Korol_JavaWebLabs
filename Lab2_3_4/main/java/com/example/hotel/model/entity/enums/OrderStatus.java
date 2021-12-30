package com.example.hotel.model.entity.enums;


public enum OrderStatus {
    SENT, CONFIRMED_BY_MANAGER, CONFIRMED_BY_CLIENT, CANCELED;

    public String getName() {
        return name().toLowerCase();
    }
}
