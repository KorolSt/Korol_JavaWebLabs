package com.example.hotel.controller.command.impl;

import com.example.hotel.controller.command.Command;
import com.example.hotel.controller.command.validation.InvalidInputException;
import com.example.hotel.controller.command.validation.Validator;
import com.example.hotel.model.service.PaymentService;
import com.example.hotel.model.service.exception.ServiceException;
import com.example.hotel.model.service.impl.ServiceFactoryImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.hotel.controller.Constants.*;


public class PaymentCommand implements Command {
    private static final long serialVersionUID = -7132234422129744696L;

    private static final Logger logger = LogManager.getLogger(PaymentCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String page = COMMAND_GET_ALL_PAYMENTS;

        PaymentService paymentService = ServiceFactoryImpl.getInstance().getPaymentService();

        String paramPaymentId = req.getParameter(REQ_PARAMETER_KEY_PAYMENT_ID);

        try {
            int paymentId = Validator.returnPositiveIntIfValid(paramPaymentId);

            paymentService.pay(paymentId);
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
