package com.example.hotel.controller.filter;

import com.example.hotel.controller.command.CommandName;
import com.example.hotel.model.entity.enums.UserRole;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static com.example.hotel.controller.Constants.*;

public class CommandAccessFilter implements Filter {
    public static final String CSS_PATH_REGEX = "^http://localhost:8080/css/.+?\\.css$";
    public static final String JS_PATH_REGEX = "^http://localhost:8080/.+?/.+?\\.js$";
    public static final String ICO_PATH_REGEX = "^http://localhost:8080/.+?/.+?\\.ico$";
    public static final String APP_PATH = "http://localhost:8080/";
    private static final String CUSTOM_TAG_PATH = "controller/customatag";
    private static final String ERROR_MESSAGE = "You do not have permission to access the requested resource";

    private static final Map<UserRole, List<CommandName>> accessMap = new EnumMap<>(UserRole.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        accessMap.put(UserRole.CLIENT, clientCommandList());
        accessMap.put(UserRole.MANAGER, managerCommandList());
        accessMap.put(UserRole.GUEST, guestCommandList());
    }

    private List<CommandName> guestCommandList() {
        List<CommandName> guestCommand = new ArrayList<>();
        guestCommand.add(CommandName.LOGIN);
        guestCommand.add(CommandName.REGISTER);
        guestCommand.add(CommandName.GET_APARTMENT_PAGE);
        guestCommand.add(CommandName.GET_LOGIN_PAGE);
        guestCommand.add(CommandName.GET_REGISTER_PAGE);
        guestCommand.add(CommandName.GET_MAIN_PAGE);
        guestCommand.add(CommandName.SET_LOCALE);
        return guestCommand;
    }

    private List<CommandName> managerCommandList() {
        List<CommandName> managerCommand = new ArrayList<>();
        managerCommand.add(CommandName.LOGOUT);
        managerCommand.add(CommandName.GET_MAIN_PAGE);
        managerCommand.add(CommandName.GET_APARTMENT_PAGE);

        managerCommand.add(CommandName.GET_ALL_ORDERS_PAGE);
        managerCommand.add(CommandName.GET_ALL_PAYMENTS_PAGE);
        managerCommand.add(CommandName.GET_ALL_RESERVATIONS_PAGE);

        managerCommand.add(CommandName.GET_ORDER_PROCESSING_PAGE);
        managerCommand.add(CommandName.PROCESS_ORDER);
        managerCommand.add(CommandName.CANCEL_ORDER);
        managerCommand.add(CommandName.CANCEL_RESERVATION);
        managerCommand.add(CommandName.CANCEL_PAYMENT);
        managerCommand.add(CommandName.UPDATE_FAILED_PAYMENTS);
        managerCommand.add(CommandName.SET_LOCALE);
        return managerCommand;
    }

    private List<CommandName> clientCommandList() {
        List<CommandName> clientCommand = new ArrayList<>();
        clientCommand.add(CommandName.LOGOUT);
        clientCommand.add(CommandName.SEARCH);
        clientCommand.add(CommandName.ORDER);
        clientCommand.add(CommandName.RESERVE);
        clientCommand.add(CommandName.PAY);

        clientCommand.add(CommandName.GET_ALL_ORDERS_PAGE);
        clientCommand.add(CommandName.GET_ALL_PAYMENTS_PAGE);
        clientCommand.add(CommandName.GET_ALL_RESERVATIONS_PAGE);

        clientCommand.add(CommandName.CANCEL_RESERVATION);
        clientCommand.add(CommandName.CANCEL_PAYMENT);
        clientCommand.add(CommandName.CANCEL_ORDER);
        clientCommand.add(CommandName.GET_MAIN_PAGE);
        clientCommand.add(CommandName.GET_APARTMENT_PAGE);
        clientCommand.add(CommandName.GET_ORDER_PAGE);
        clientCommand.add(CommandName.GET_CLIENT_PROFILE_PAGE);
        clientCommand.add(CommandName.SET_LOCALE);
        return clientCommand;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (accessAllowed(servletRequest)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        servletRequest.setAttribute(ATTRIBUTE_ERROR_MESSAGE, ERROR_MESSAGE);
        servletRequest.getRequestDispatcher(PAGE_ERROR)
                .forward(servletRequest, servletResponse);
    }

    private boolean accessAllowed(ServletRequest servletRequest) {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String paramCommand = req.getParameter(REQ_PARAMETER_KEY_COMMAND);

        CommandName commandName = null;
        try {
            if (paramCommand != null) {
                commandName = CommandName.valueOf(paramCommand.toUpperCase());
            }
        } catch (IllegalArgumentException ignored) {
        }

        if (commandName == null) {
            return accessCommonResources(servletRequest);
        }

        HttpSession session = req.getSession(false);
        if (session == null) {
            session = req.getSession(true);
            session.setAttribute(ATTRIBUTE_USER_ROLE, UserRole.GUEST);
        }

        UserRole userRole = (UserRole) session.getAttribute(ATTRIBUTE_USER_ROLE);

        if (userRole == null) {
            session.setAttribute(ATTRIBUTE_USER_ROLE, UserRole.GUEST);
            userRole = UserRole.GUEST;
        }
        return accessMap.get(userRole).contains(commandName);
    }

    private boolean accessCommonResources(ServletRequest servletRequest) {
        String requestUrl = ((HttpServletRequest) servletRequest).getRequestURL().toString();
        String requestQuery = ((HttpServletRequest) servletRequest).getQueryString();

        if (requestUrl.equals(APP_PATH) && requestQuery == null) {
            return true;
        }

        return requestUrl.matches(JS_PATH_REGEX) || requestUrl.matches(CSS_PATH_REGEX) ||
                requestUrl.matches(ICO_PATH_REGEX) ||
                requestUrl.equals(APP_PATH + CUSTOM_TAG_PATH);
    }

    @Override
    public void destroy() {
    }
}
