package com.example.hotel.model.entity;

import com.example.hotel.model.entity.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public class Payment extends Entity {
    private static final long serialVersionUID = -2443660950991363456L;

    private Integer reservationId;
    private Instant payDate;
    private Instant expireDate;
    private PaymentStatus status;
    private BigDecimal amount;

    public Integer getReservationId() {
        return reservationId;
    }

    public void setReservationId(Integer reservationId) {
        this.reservationId = reservationId;
    }

    public Instant getPayDate() {
        return payDate;
    }

    public void setPayDate(Instant payDate) {
        this.payDate = payDate;
    }

    public Instant getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Instant expireDate) {
        this.expireDate = expireDate;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Payment payment = (Payment) o;
        return Objects.equals(reservationId, payment.reservationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), reservationId);
    }

    @Override
    public String toString() {
        return "Payment [reservationId=" + reservationId +
                ", payDate=" + payDate + ", expireDate=" + expireDate +
                ", paymentStatus=" + status + ", amount=" + amount + ']';
    }
}
