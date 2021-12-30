package com.example.hotel.controller;

public final class Constants {
    private Constants() {
    }

    public static final String REDIRECT_TO = "redirect:";

    // pages
    public static final String PAGE_ERROR = "/WEB-INF/jsp/error.jsp";
    public static final String PAGE_LOGIN = "/WEB-INF/jsp/login.jsp";
    public static final String PAGE_ALL_ORDERS = "/WEB-INF/jsp/allOrders.jsp";
    public static final String PAGE_ALL_PAYMENTS = "/WEB-INF/jsp/allPayments.jsp";
    public static final String PAGE_ALL_RESERVATION = "/WEB-INF/jsp/allReservations.jsp";
    public static final String PAGE_APARTMENT = "/WEB-INF/jsp/apartment.jsp";
    public static final String PAGE_MAIN = "/WEB-INF/jsp/main.jsp";
    public static final String PAGE_ORDER = "/WEB-INF/jsp/client/order.jsp";
    public static final String PAGE_ORDER_PROCESS = "/WEB-INF/jsp/manager/processOrder.jsp";
    public static final String PAGE_REGISTER = "/WEB-INF/jsp/register.jsp";

    // commands
    public static final String COMMAND_GET_ALL_ORDERS = "/controller?command=get_all_orders_page";
    public static final String COMMAND_GET_ALL_PAYMENTS = "/controller?command=get_all_payments_page";
    public static final String COMMAND_GET_ALL_RESERVATIONS = "/controller?command=get_all_reservations_page";
    public static final String COMMAND_GET_MAIN = "/controller?command=get_main_page";
    public static final String COMMAND_GET_ORDER = "/controller?command=get_order_page";
    public static final String COMMAND_GET_LOGIN = "/controller?command=get_login_page";
    public static final String COMMAND_GET_REGISTER = "/controller?command=get_register_page";
    public static final String COMMAND_GET_APARTMENT = "/controller?command=get_apartment_page";

    // attributes
    public static final String ATTRIBUTE_ERROR_MESSAGE = "errorMessage";
    public static final String ATTRIBUTE_USER_ID = "userId";
    public static final String ATTRIBUTE_USER_ROLE = "userRole";
    public static final String ATTRIBUTE_LOGGED_USERS = "loggedUsers";
    public static final String ATTRIBUTE_APARTMENTS = "apartments";
    public static final String ATTRIBUTE_APARTMENT = "apartment";
    public static final String ATTRIBUTE_PRICE = "price";
    public static final String ATTRIBUTE_ORDER_ID = "order_id";
    public static final String ATTRIBUTE_RECORDS_PER_PAGE = "records_on_page";
    public static final String ATTRIBUTE_CURRENT_PAGE = "curr_page";
    public static final String ATTRIBUTE_PAGES_COUNT = "pages_count";
    public static final String ATTRIBUTE_RESERVATIONS = "reservations";
    public static final String ATTRIBUTE_ORDERS = "orders";
    public static final String ATTRIBUTE_PAYMENTS = "payments";
    public static final String ATTRIBUTE_LOCALE = "locale";
    public static final String ATTRIBUTE_DATE_IN = "date_in";
    public static final String ATTRIBUTE_DATE_OUT = "date_out";

    // parameters
    public static final String PARAMETER_KEY_MESSAGE = "&message=";
    public static final String MESSAGE_ERROR = "error";
    public static final String MESSAGE_USER_LOGGED = "user_logged";
    public static final String MESSAGE_INVALID_INPUT = "invalid_input";
    public static final String MESSAGE_USER_EXISTS = "user_exists";
    public static final String MESSAGE_RESERVATION_EXISTS = "reservation_exists";
    public static final String MESSAGE_INCORRECT_CREDENTIALS = "incorrect_credentials";
    public static final String MESSAGE_SUCCESS = "success";

    public static final String REQ_PARAMETER_KEY_APARTMENT_TYPE = "apartment_type";
    public static final String REQ_PARAMETER_KEY_APARTMENT_ID = "apartment_id";
    public static final String REQ_PARAMETER_KEY_RESERVATION_ID = "reservation_id";
    public static final String REQ_PARAMETER_KEY_PAYMENT_ID = "payment_id";
    public static final String REQ_PARAMETER_KEY_ORDER_ID = "order_id";
    public static final String REQ_PARAMETER_KEY_DATE_IN = "date_in";
    public static final String REQ_PARAMETER_KEY_DATE_OUT = "date_out";
    public static final String REQ_PARAMETER_KEY_PLACE_COUNT = "place_count";
    public static final String REQ_PARAMETER_KEY_DATE_NOW = "date_now";
    public static final String REQ_PARAMETER_KEY_DATE_TOMORROW = "date_tomorrow";
    public static final String REQ_PARAMETER_KEY_LOGIN = "login";
    public static final String REQ_PARAMETER_KEY_PASSWORD = "password";
    public static final String REQ_PARAMETER_KEY_RECORDS_PER_PAGE = "records_on_page";
    public static final String REQ_PARAMETER_KEY_CURRENT_PAGE = "curr_page";
    public static final String REQ_PARAMETER_KEY_COMMAND = "command";
    public static final String REQ_PARAMETER_KEY_STATUS = "status";
    public static final String REQ_PARAMETER_KEY_SORT_BY = "sort_by";
    public static final String REQ_PARAMETER_KEY_SORT = "sort";
    public static final String REQ_PARAMETER_KEY_ORDER = "order";
    public static final String REQ_PARAMETER_KEY_NAME = "name";
    public static final String REQ_PARAMETER_KEY_SURNAME = "surname";
    public static final String REQ_PARAMETER_KEY_PHONE = "phone";
    public static final String REQ_PARAMETER_KEY_EMAIL = "email";

    public static final String REQ_PARAMETER_VAL_ASC_ORDER = "asc";
    public static final String REQ_PARAMETER_VAL_AFTER_ORDER = "after_order";
    public static final String REQ_PARAMETER_VAL_NO_ORDER = "no_order";
    public static final String REQ_PARAMETER_VAL_TYPE = "type";

    // locale
    public static final String CONFIG_FMT_LOCALE = "javax.servlet.jsp.jstl.fmt.locale";
    public static final Object DEFAULT_LOCALE = "uk";

    // pagination
    public static final String DEFAULT_RECORDS_PER_PAGE = "5";
    public static final String DEFAULT_CURRENT_PAGE = "1";
}
