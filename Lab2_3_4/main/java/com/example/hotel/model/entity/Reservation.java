package com.example.hotel.model.entity;

import com.example.hotel.model.entity.enums.ReservationStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

public class Reservation extends Entity {
    private static final long serialVersionUID = 8974678001037118830L;

    private Integer userId;
    private ReservationStatus status;
    private Integer apartmentId;
    private Instant reservationDate;
    private LocalDate dateIn;
    private LocalDate dateOut;
    private Integer personCount;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(Integer apartmentId) {
        this.apartmentId = apartmentId;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public Instant getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Instant reservationDate) {
        this.reservationDate = reservationDate;
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

    public Integer getPersonCount() {
        return personCount;
    }

    public void setPersonCount(Integer personCount) {
        this.personCount = personCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Reservation that = (Reservation) o;
        return apartmentId.equals(that.apartmentId) && Objects.equals(dateIn, that.dateIn) && Objects.equals(dateOut, that.dateOut);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), apartmentId, dateIn, dateOut);
    }

    @Override
    public String toString() {
        return "Reservation [userId=" + userId + ", status=" + status +
                ", apartmentId=" + apartmentId + ", reservationDate=" + reservationDate +
                ", dateIn=" + dateIn + ", dateOut=" + dateOut + ", personCount=" + personCount + ']';
    }
}
