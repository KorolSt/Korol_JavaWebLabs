package com.example.hotel.controller.command.impl;

import com.example.hotel.controller.command.Command;
import com.example.hotel.controller.command.validation.InvalidInputException;
import com.example.hotel.controller.command.validation.Validator;
import com.example.hotel.model.service.OrderService;
import com.example.hotel.model.service.ServiceFactory;
import com.example.hotel.model.service.exception.ServiceException;
import com.example.hotel.model.service.impl.ServiceFactoryImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


import static com.example.hotel.controller.Constants.*;

public class ProcessOrderCommand implements Command {
    private static final long serialVersionUID = 688324566702235772L;

    private static final Logger logger = LogManager.getLogger(ProcessOrderCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String page = COMMAND_GET_ALL_ORDERS;

        String paramOrderId = req.getParameter(REQ_PARAMETER_KEY_ORDER_ID);
        String paramApartmentId = req.getParameter(REQ_PARAMETER_KEY_APARTMENT_ID);

        ServiceFactory serviceFactory = ServiceFactoryImpl.getInstance();
        OrderService orderService = serviceFactory.getOrderService();

        try {
            int orderId = Validator.returnPositiveIntIfValid(paramOrderId);
            int apartmentId = Validator.returnPositiveIntIfValid(paramApartmentId);

            orderService.connectApartmentAndOrder(orderId, apartmentId);
        } catch (InvalidInputException e) {
            logger.error(e.getMessage());
            page += PARAMETER_KEY_MESSAGE + MESSAGE_INVALID_INPUT;
        } catch (ServiceException e) {
            logger.error(e.getMessage());
            page += PARAMETER_KEY_MESSAGE + MESSAGE_ERROR;
        }

        return REDIRECT_TO + page;
    }
}
