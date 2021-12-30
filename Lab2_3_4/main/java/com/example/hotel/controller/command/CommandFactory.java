package com.example.hotel.controller.command;

import com.example.hotel.controller.command.impl.*;
import com.example.hotel.controller.command.impl.getpage.*;

import java.util.EnumMap;
import java.util.Map;

public class CommandFactory {
    private static final Map<CommandName, Command> commands = new EnumMap<>(CommandName.class);

    private CommandFactory() {
    }

    static {
        commands.put(CommandName.LOGIN, new LoginCommand());
        commands.put(CommandName.LOGOUT, new LogoutCommand());
        commands.put(CommandName.REGISTER, new RegisterCommand());
        commands.put(CommandName.ORDER, new OrderCommand());
        commands.put(CommandName.PAY, new PaymentCommand());
        commands.put(CommandName.RESERVE, new ReserveCommand());
        commands.put(CommandName.CANCEL_RESERVATION, new CancelReservationCommand());
        commands.put(CommandName.CANCEL_PAYMENT, new CancelPaymentCommand());
        commands.put(CommandName.SET_LOCALE, new SetLocaleCommand());

        commands.put(CommandName.GET_REGISTER_PAGE, new GetRegisterPageCommand());
        commands.put(CommandName.GET_LOGIN_PAGE, new GetLoginPageCommand());
        commands.put(CommandName.GET_MAIN_PAGE, new GetMainPageCommand());
        commands.put(CommandName.GET_APARTMENT_PAGE, new GetApartmentPageCommand());
        commands.put(CommandName.GET_ORDER_PAGE, new GetOrderPageCommand());

        commands.put(CommandName.GET_ALL_PAYMENTS_PAGE, new GetAllPaymentsPageCommand());
        commands.put(CommandName.GET_ALL_ORDERS_PAGE, new GetAllOrdersPageCommand());
        commands.put(CommandName.GET_ALL_RESERVATIONS_PAGE, new GetAllReservationsPageCommand());

        commands.put(CommandName.GET_ORDER_PROCESSING_PAGE, new GetOrderProcessingPageCommand());
        commands.put(CommandName.PROCESS_ORDER, new ProcessOrderCommand());
        commands.put(CommandName.CANCEL_ORDER, new CancelOrderCommand());
        commands.put(CommandName.UPDATE_FAILED_PAYMENTS, new UpdateFailedPaymentsCommand());
    }

    public static Command get(String commandName) throws IllegalArgumentException {
        return commands.get(CommandName.valueOf(commandName));
   }
}
