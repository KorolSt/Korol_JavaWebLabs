package com.example.hotel.controller.command.impl;

import com.example.hotel.controller.command.Command;
import com.example.hotel.model.service.exception.ServiceException;
import com.example.hotel.model.service.impl.ServiceFactoryImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.hotel.controller.Constants.*;

public class UpdateFailedPaymentsCommand implements Command {
    private static final long serialVersionUID = -8560441599145846202L;

    private static final Logger logger = LogManager.getLogger(UpdateFailedPaymentsCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String page = COMMAND_GET_ALL_PAYMENTS;

        try {
            if (ServiceFactoryImpl.getInstance().getPaymentService().updateFailedPayments()) {
                page += PARAMETER_KEY_MESSAGE + MESSAGE_SUCCESS;
            }
        } catch (ServiceException e) {
            logger.error(e.getMessage());
            page += PARAMETER_KEY_MESSAGE + MESSAGE_ERROR;
        }
        return REDIRECT_TO + page;
    }
}
