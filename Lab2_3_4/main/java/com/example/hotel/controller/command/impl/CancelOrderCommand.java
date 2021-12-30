package com.example.hotel.controller.command.impl;

import com.example.hotel.controller.command.Command;
import com.example.hotel.controller.command.validation.InvalidInputException;
import com.example.hotel.controller.command.validation.Validator;
import com.example.hotel.model.service.OrderService;
import com.example.hotel.model.service.exception.ServiceException;
import com.example.hotel.model.service.impl.ServiceFactoryImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.hotel.controller.Constants.*;

public class CancelOrderCommand implements Command {
    private static final long serialVersionUID = -1919276209975849841L;

    private static final Logger logger = LogManager.getLogger(CancelOrderCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String page = COMMAND_GET_ALL_ORDERS;

        OrderService orderService = ServiceFactoryImpl.getInstance().getOrderService();
        try {
            int orderId = Validator.returnPositiveIntIfValid(req.getParameter(REQ_PARAMETER_KEY_ORDER_ID));

            orderService.cancelOrder(orderId);
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
