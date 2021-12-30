package com.example.hotel.controller.command.impl;

import com.example.hotel.controller.command.Command;
import com.example.hotel.controller.command.validation.InvalidInputException;
import com.example.hotel.controller.command.validation.Validator;
import com.example.hotel.model.entity.Order;
import com.example.hotel.model.entity.enums.ApartmentType;
import com.example.hotel.model.entity.enums.OrderStatus;
import com.example.hotel.model.service.OrderService;
import com.example.hotel.model.service.exception.ServiceException;
import com.example.hotel.model.service.impl.ServiceFactoryImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;

import static com.example.hotel.controller.Constants.*;

public class OrderCommand implements Command {
    private static final long serialVersionUID = -15804511820676776L;
    private static final Logger logger = LogManager.getLogger(OrderCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String page = COMMAND_GET_ORDER;

        OrderService orderService = ServiceFactoryImpl.getInstance().getOrderService();

        try {
            Order order = validateAndBuildOrder(req);
            orderService.makeOrderByClient(order);
            page += PARAMETER_KEY_MESSAGE + MESSAGE_SUCCESS;
        } catch (InvalidInputException e) {
            logger.error(e.getMessage());
            page += PARAMETER_KEY_MESSAGE + MESSAGE_INVALID_INPUT;
        } catch (ServiceException e) {
            logger.error(e.getMessage());
            page += PARAMETER_KEY_MESSAGE + MESSAGE_ERROR;
        }

        return REDIRECT_TO + page;
    }

    private Order validateAndBuildOrder(HttpServletRequest req) throws InvalidInputException {
        ApartmentType apartmentType = Validator.returnTypeIfValid(req.getParameter(REQ_PARAMETER_KEY_APARTMENT_TYPE));
        LocalDate[] dates = Validator.dateInAndDateOutValidate(req.getParameter(REQ_PARAMETER_KEY_DATE_IN), req.getParameter(REQ_PARAMETER_KEY_DATE_OUT), true);
        int placeCount = Validator.returnPositiveIntIfValid(req.getParameter(REQ_PARAMETER_KEY_PLACE_COUNT));

        LocalDate dateIn = dates[0];
        LocalDate dateOut = dates[1];
        int userId = (int) req.getSession(false).getAttribute("userId");

        Order order = new Order();
        order.setPersonCount(placeCount);
        order.setDateIn(dateIn);
        order.setDateOut(dateOut);
        order.setStatus(OrderStatus.SENT);
        order.setApartmentType(apartmentType);
        order.setOrderDate(Instant.now());
        order.setUserId(userId);
        return order;
    }
}
