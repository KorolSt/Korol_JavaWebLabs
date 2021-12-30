package com.example.hotel.model.entity.enums;



public enum ApartmentType {
    SINGLE, DOUBLE, TRIPLE, QUAD,
    QUEEN, KING, TWIN, DOUBLE_DOUBLE,
    STUDIO, MASTER_SUITE, JUNIOR_SUITE;

    public String getName() {
        return name().toLowerCase();
    }

    }
