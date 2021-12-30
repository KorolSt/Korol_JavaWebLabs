package com.example.hotel.model.entity;

import com.example.hotel.model.entity.enums.ApartmentType;

import java.util.Objects;

public class Apartment extends Entity {
    private static final long serialVersionUID = 7299439092587824107L;

    private String name;
    private Integer placeCount;
    private boolean available;
    private ApartmentType type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public ApartmentType getType() {
        return type;
    }

    public void setType(ApartmentType type) {
        this.type = type;
    }

    public Integer getPlaceCount() {
        return placeCount;
    }

    public void setPlaceCount(Integer placeCount) {
        this.placeCount = placeCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Apartment apartment = (Apartment) o;
        return available == apartment.available && Objects.equals(name, apartment.name) && Objects.equals(placeCount, apartment.placeCount) && type == apartment.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, placeCount, available, type);
    }

    @Override
    public String toString() {
        return "Apartment [name=" + name + ", placeNumber=" + placeCount +
                ", available=" + available + ", type=" + type + ']';
    }
}
