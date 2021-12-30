package com.example.hotel.controller.command.impl;

import com.example.hotel.controller.command.Command;
import com.example.hotel.controller.command.validation.InvalidInputException;
import com.example.hotel.controller.command.validation.Validator;
import com.example.hotel.model.service.ReservationService;
import com.example.hotel.model.service.exception.ServiceException;
import com.example.hotel.model.service.impl.ServiceFactoryImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.hotel.controller.Constants.*;

public class CancelReservationCommand implements Command {
    private static final long serialVersionUID = -652353249791530871L;

    private static final Logger logger = LogManager.getLogger(CancelReservationCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String page = COMMAND_GET_ALL_RESERVATIONS;

        ReservationService reservationService = ServiceFactoryImpl.getInstance().getReservationService();
        try {
            int reservationId = Validator.returnPositiveIntIfValid(req.getParameter(REQ_PARAMETER_KEY_APARTMENT_ID));

            reservationService.cancelReservation(reservationId);
        } catch (InvalidInputException e) {
            logger.error(e.getMessage());
            page += PARAMETER_KEY_MESSAGE + MESSAGE_INVALID_INPUT;
        } catch (ServiceException e) {
            logger.error(e.getMessage());
            page += PARAMETER_KEY_MESSAGE + MESSAGE_ERROR;
        }

        return "redirect:" + page;
    }
}
