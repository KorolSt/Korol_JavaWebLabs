package com.example.hotel.model.entity.enums;

public enum UserRole {
    MANAGER, CLIENT, GUEST;

    public String getName() {
        return name().toLowerCase();
    }
}
