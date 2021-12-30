package com.example.hotel.controller.command.impl;

import com.example.hotel.controller.command.Command;
import com.example.hotel.controller.command.validation.InvalidInputException;
import com.example.hotel.controller.command.validation.Validator;
import com.example.hotel.model.entity.Reservation;
import com.example.hotel.model.service.ApartmentService;
import com.example.hotel.model.service.ReservationService;
import com.example.hotel.model.service.ServiceFactory;
import com.example.hotel.model.service.exception.EntityAlreadyExistsServiceException;
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


public class ReserveCommand implements Command {
    private static final long serialVersionUID = 3884718368801131582L;
    private static final Logger logger = LogManager.getLogger(ReserveCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String page = COMMAND_GET_ALL_RESERVATIONS;

        String type = req.getParameter(REQ_PARAMETER_VAL_TYPE);
        if (type == null || (!type.equals(REQ_PARAMETER_VAL_AFTER_ORDER) && !type.equals(REQ_PARAMETER_VAL_NO_ORDER))) {
            logger.error("'Reserve type' is invalid!");
            page += PARAMETER_KEY_MESSAGE + MESSAGE_INVALID_INPUT;
            return REDIRECT_TO + page;
        }

        ServiceFactory serviceFactory = ServiceFactoryImpl.getInstance();
        ReservationService reservationService = serviceFactory.getReservationService();
        ApartmentService apartmentService = serviceFactory.getApartmentService();

        try {
            if (type.equals(REQ_PARAMETER_VAL_AFTER_ORDER)) {
                int orderId = Validator.returnPositiveIntIfValid(req.getParameter(REQ_PARAMETER_KEY_ORDER_ID));
                reservationService.reserveByOrderId(orderId);
            } else {
                Reservation reservation = buildReservationIfFree(req, apartmentService);
                reservationService.reserve(reservation);
            }
            page = COMMAND_GET_ALL_RESERVATIONS + PARAMETER_KEY_MESSAGE + MESSAGE_SUCCESS;
        } catch (InvalidInputException e) {
            logger.error(e.getMessage());
            page = COMMAND_GET_APARTMENT + PARAMETER_KEY_MESSAGE + MESSAGE_INVALID_INPUT;
        } catch (EntityAlreadyExistsServiceException e) {
            logger.error(e.getMessage());
            page = COMMAND_GET_APARTMENT + PARAMETER_KEY_MESSAGE + MESSAGE_RESERVATION_EXISTS;
        } catch (ServiceException e) {
            logger.error(e.getMessage());
            page += PARAMETER_KEY_MESSAGE + MESSAGE_ERROR;
        }

        return REDIRECT_TO + page;
    }

    private Reservation buildReservationIfFree(HttpServletRequest req, ApartmentService apartmentService) throws ServiceException, InvalidInputException {
        String paramDateIn = req.getParameter(REQ_PARAMETER_KEY_DATE_IN);
        String paramDateOut = req.getParameter(REQ_PARAMETER_KEY_DATE_OUT);
        LocalDate[] dates = Validator.dateInAndDateOutValidate(paramDateIn, paramDateOut, true);

        int apartmentId = Validator.returnPositiveIntIfValid(req.getParameter(REQ_PARAMETER_KEY_APARTMENT_ID));
        int placeCount = Validator.returnPositiveIntIfValid(req.getParameter(REQ_PARAMETER_KEY_PLACE_COUNT));
        LocalDate dateIn = dates[0];
        LocalDate dateOut = dates[1];

        if (!apartmentService.isApartmentFree(apartmentId, dateIn, dateOut)) {
            throw new EntityAlreadyExistsServiceException();
        }
        int userId = (int) req.getSession(false).getAttribute(ATTRIBUTE_USER_ID);

        Reservation reservation = new Reservation();
        reservation.setPersonCount(placeCount);
        reservation.setDateIn(dateIn);
        reservation.setDateOut(dateOut);
        reservation.setApartmentId(apartmentId);
        reservation.setReservationDate(Instant.now());
        reservation.setUserId(userId);
        return reservation;
    }
}
