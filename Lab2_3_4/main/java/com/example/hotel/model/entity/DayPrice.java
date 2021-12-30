package com.example.hotel.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class DayPrice extends Entity {
    private static final long serialVersionUID = -1735593706431106616L;

    private Integer apartmentId;
    private BigDecimal price;
    private LocalDate date;

    public Integer getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(Integer apartmentId) {
        this.apartmentId = apartmentId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DayPrice dayPrice = (DayPrice) o;
        return Objects.equals(apartmentId, dayPrice.apartmentId) && Objects.equals(date, dayPrice.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), apartmentId, date);
    }

    @Override
    public String toString() {
        return "DayPrice [apartment_id=" + apartmentId +
                ", price=" + price +
                ", date=" + date + ']';
    }
}
