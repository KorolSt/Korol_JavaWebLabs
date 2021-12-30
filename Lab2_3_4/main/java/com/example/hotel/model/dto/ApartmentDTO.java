package com.example.hotel.model.dto;

import com.example.hotel.model.entity.enums.ApartmentType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class ApartmentDTO {
    private int apartmentId;
    private String name;
    private LocalDate dateIn;
    private LocalDate dateOut;
    private int placeCount;
    private BigDecimal amount;
    private boolean available;
    private ApartmentType type;
    private Status status;

    public enum Status {
        FREE, RESERVED, BUSY, NOT_AVAILABLE;

        public String getName() {
            return name().toLowerCase();
        }
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(int apartmentId) {
        this.apartmentId = apartmentId;
    }

    public LocalDate getDateIn() {
        return dateIn;
    }

    public void setDateIn(LocalDate dateIn) {
        this.dateIn = dateIn;
    }

    public LocalDate getDateOut() {
        return dateOut;
    }

    public void setDateOut(LocalDate dateOut) {
        this.dateOut = dateOut;
    }

    public int getPlaceCount() {
        return placeCount;
    }

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

    public void setPlaceCount(int placeCount) {
        this.placeCount = placeCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApartmentDTO that = (ApartmentDTO) o;
        return apartmentId == that.apartmentId && placeCount == that.placeCount && available == that.available && Objects.equals(name, that.name) && Objects.equals(dateIn, that.dateIn) && Objects.equals(dateOut, that.dateOut) && Objects.equals(amount, that.amount) && type == that.type && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(apartmentId, name, dateIn, dateOut, placeCount, amount, available, type, status);
    }

    @Override
    public String toString() {
        return "ApartmentDTO{" +
                "apartmentId=" + apartmentId +
                ", name='" + name + '\'' +
                ", dateIn=" + dateIn +
                ", dateOut=" + dateOut +
                ", placeCount=" + placeCount +
                ", amount=" + amount +
                ", available=" + available +
                ", type=" + type +
                ", status=" + status +
                '}';
    }
}
