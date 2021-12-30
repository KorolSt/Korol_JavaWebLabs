package com.example.hotel.model.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Sorter {
    RESERVATIONS_BY_RESERVATION_DATE_ASC,
    RESERVATIONS_BY_RESERVATION_DATE_DESC,
    RESERVATIONS_BY_DATE_IN_ASC,
    RESERVATIONS_BY_DATE_IN_DESC,
    RESERVATIONS_BY_STATUS_ASC,
    RESERVATIONS_BY_STATUS_DESC,

    PAYMENTS_BY_STATUS_ASC,
    PAYMENTS_BY_STATUS_DESC,
    PAYMENTS_BY_EXPIRE_DATE_ASC,
    PAYMENTS_BY_EXPIRE_DATE_DESC,

    APARTMENTS_BY_PRICE_ASC,
    APARTMENTS_BY_PRICE_DESC,
    APARTMENTS_BY_PLACE_NUMBER_ASC,
    APARTMENTS_BY_PLACE_NUMBER_DESC,
    APARTMENTS_BY_STATUS_ASC,
    APARTMENTS_BY_STATUS_DESC,
    APARTMENTS_BY_APARTMENT_TYPE_ASC,
    APARTMENTS_BY_APARTMENT_TYPE_DESC,

    ORDERS_BY_ORDER_DATE_ASC,
    ORDERS_BY_ORDER_DATE_DESC,
    ORDERS_BY_DATE_IN_ASC,
    ORDERS_BY_DATE_IN_DESC,
    ORDERS_BY_STATUS_ASC,
    ORDERS_BY_STATUS_DESC;

    private static final String ASC_REGEX = "_asc(?=|$)";
    private static final String DESC_REPLACEMENT = "_desc";

    public String getName() {
        return name().toLowerCase();
    }

    public boolean isAscending() {
        return Pattern.compile(ASC_REGEX)
                .matcher(getName()).find();
    }

    public Sorter descendingOne() throws IllegalArgumentException {
        Matcher matcher = Pattern.compile(ASC_REGEX)
                .matcher(getName());
        if (matcher.find()) {
            return Sorter.valueOf(matcher.replaceAll(DESC_REPLACEMENT).toUpperCase());
        } else return this;
    }

}
