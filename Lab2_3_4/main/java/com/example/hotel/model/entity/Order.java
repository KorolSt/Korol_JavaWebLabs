package com.example.hotel.model.entity;

import com.example.hotel.model.entity.enums.ApartmentType;
import com.example.hotel.model.entity.enums.OrderStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

public class Order extends Entity {
    private static final long serialVersionUID = 1721526492665102502L;

    private Integer userId;
    private OrderStatus status;
    private ApartmentType apartmentType;
    private Integer apartmentId;
    private Instant orderDate;
    private LocalDate dateIn;
    private LocalDate dateOut;
    private Integer personCount;

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public ApartmentType getApartmentType() {
        return apartmentType;
    }

    public void setApartmentType(ApartmentType apartmentType) {
        this.apartmentType = apartmentType;
    }

    public Instant getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
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

    public Integer getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(Integer apartmentId) {
        this.apartmentId = apartmentId;
    }

    public Integer getPersonCount() {
        return personCount;
    }

    public void setPersonCount(Integer personCount) {
        this.personCount = personCount;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Order order = (Order) o;
        return Objects.equals(userId, order.userId) && status == order.status && apartmentType == order.apartmentType && Objects.equals(apartmentId, order.apartmentId) && Objects.equals(orderDate.toEpochMilli(), order.orderDate.toEpochMilli()) && Objects.equals(dateIn, order.dateIn) && Objects.equals(dateOut, order.dateOut) && Objects.equals(personCount, order.personCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userId, status, apartmentType, apartmentId, orderDate, dateIn, dateOut, personCount);
    }

    @Override
    public String toString() {
        return "Order [userId=" + userId + ", status=" + status +
                ", apartmentType=" + apartmentType + ", apartmentId=" + apartmentId +
                ", orderDate=" + orderDate + ", dateIn=" + dateIn +
                ", dateOut=" + dateOut + ", personCount=" + personCount + ']';
    }
}
